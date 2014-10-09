/**
 * 
 */
package com.til.service.ui.admin;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.URLEntity;
import twitter4j.conf.ConfigurationBuilder;

import com.til.security.acegi.CustomUser;
import com.til.service.common.api.Rss2Feeds;
import com.til.service.common.api.Rss2Feeds.ChannelItem;
import com.til.service.common.dao.ArticleDao;
import com.til.service.common.dao.TopicPageArticleDao;
import com.til.service.common.dao.TopicPageDao;
import com.til.service.common.dao.WebsiteDao;
import com.til.service.common.dao.hibernate.entity.Article;
import com.til.service.common.dao.hibernate.entity.TopicPage;
import com.til.service.common.dao.hibernate.entity.TopicPageArticle;
import com.til.service.common.dao.hibernate.entity.User;
import com.til.service.common.dao.hibernate.entity.Website;
import com.til.service.facebook.FacebookUtils;
import com.til.service.facebook.api.PageDetail;
import com.til.service.pagination.vo.NavigationInfo;
import com.til.service.toi.ToiUtils;
import com.til.service.toi.api.DayLifeFeeds;
import com.til.service.toi.api.DayLifeFeeds.Item;
import com.til.service.twitter.TwitterUtils;
import com.til.service.ui.admin.command.TopicListBean;
import com.til.service.utils.DateUtil;
import com.til.service.utils.Utilities;

/**
 * @author Harsh.Gupta
 *
 */
@Controller
public class ListTopicController{
	
	/** Logger for this class and subclasses */
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private WebsiteDao websiteDao;
    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private TopicPageDao     topicPageDao;
    @Autowired
    private TopicPageArticleDao topicPageArticleDao;
	
    @Autowired
	Jaxb2Marshaller jaxb2Marshaller;
    
    @Value("#{jdbcConfiguration['scheduleMessage.lockingPeriodInDays']}")
    int lockingPeriodInDays;
    
    @RequestMapping(value="/listTopic", method=RequestMethod.GET)
	public String handleRequest(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		
    	TopicListBean backingObject = new TopicListBean();
		backingObject = getData(request, backingObject, false);
		model.addAttribute("topicListBean", backingObject);
		
		/*String action = request.getParameter("m");
		String websiteId = request.getParameter("websiteId");
		if(action != null && !"".equals(action) && websiteId != null && !"".equals(websiteId))
		{
			//websiteDao.deleteByWebsiteId(websiteId);
		}*/
		
		return "listTopic";
	}
    
	private TopicListBean getData(HttpServletRequest request,Object command, boolean post)
	{
		String startDateStr = null;
		String endDateStr 	= null;
		Website website 	= null;
		String status 		= null;
		String page 		= null;
		String topicName	= null;
		String sortby 		= null;
		String order 		= null;
		NavigationInfo navInfo =null;
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Object obj = auth.getPrincipal();
		TopicListBean backingObject = (TopicListBean)command;
		User user = null;
		if (obj instanceof UserDetails) 
		{
			user = (User) ((CustomUser) obj).getUser();
			website = user.getWebsite();
		} 
		try 
		{
			if (post == false) 
			{
				startDateStr = request.getParameter("startDate");
				endDateStr = request.getParameter("endDate");
				status = request.getParameter("status");
				page = request.getParameter("page");
				topicName = request.getParameter("topicName");
			}
			else
			{
				startDateStr = backingObject.getStartDate();
				endDateStr = backingObject.getEndDate();
				status = backingObject.getStatus();
				page = backingObject.getPage() + "";
				topicName = backingObject.getTopicName();
				sortby = backingObject.getSortBy();
				order = backingObject.getOrder();
			}
			if(topicName != null)
			{
				topicName = topicName.trim();
			}
			logger.debug("topicName {} " , topicName);
			navInfo = backingObject.getNavInfo();
			
			SimpleDateFormat sourceDateFormat = new SimpleDateFormat(DateUtil.formatIndianDate);
			
			Date startDate = null;
			Date endDate = null;
			if (startDateStr != null) {
				startDate = DateUtil.parse(startDateStr, sourceDateFormat);
			}
			if (endDateStr != null) {
				endDate = DateUtil.parse(endDateStr, sourceDateFormat);
			}
			
			List<TopicPage> list = null;
			int rowCount = 0;
			if(website != null)
			{
				if(website.getId() == 1)
				{
					list = topicPageDao.findAllOrdered(topicName, page, "" + navInfo.getPageSize());
					rowCount = Integer.valueOf(topicPageDao.countAllOrdered(topicName).toString());
				}
				else
				{
					list = topicPageDao.findByUserId(user.getId(),page, "" + navInfo.getPageSize(), topicName,
												null, null) ;
					rowCount = Integer.valueOf(topicPageDao.countByUserId(user.getId(), topicName).toString());
				}
			}
				
			navInfo.setRowCount(rowCount);
			if (null == page)
				navInfo.setCurrentPage(backingObject.getPage());
			else
				navInfo.setCurrentPage(Integer.parseInt(page));
			
			navInfo.setDisplayCount(list.size());
			
			backingObject.setTopicPageList(list);

			backingObject.setEndDate(endDateStr);
			backingObject.setStartDate(startDateStr);
			backingObject.setStatus(status);
			
			backingObject.setFormname(TopicListBean.FORMNAME_TOPICFORM);
				
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			logger.error("Exception while listing WebUsers " , e);
		}
		return backingObject;
	}
	
	@RequestMapping(value="/listTopic", method=RequestMethod.POST)
	public String onSubmit(@Valid TopicListBean topicListBean,BindingResult result, ModelMap model,
						HttpServletRequest request)
	{
		if (result.hasErrors()) {
			/*for (ObjectError error : result.getGlobalErrors()) {
				System.out.println(error.getDefaultMessage());
			}*/
            return "listTopic";
		}
		topicListBean =(TopicListBean)getData(request,topicListBean, true);
		model.addAttribute("topicListBean", topicListBean);
	
		return "listTopic";
	}
	
    @RequestMapping(value = "/deleteTopic",method = RequestMethod.GET)
    public ModelAndView delete(@RequestParam("id") Integer id, ModelMap model)
    {
    	if(!"".equals(id))
    	{
    		topicPageDao.delete(topicPageDao.load(id));
    	}
    	logger.info("Deleting article with id {}" , id);
		return new ModelAndView(new RedirectView("listTopic"));
    }
    
    @RequestMapping(value = "/sendArticle",method = RequestMethod.GET)
    public String sendArticle(@RequestParam("id") Integer id, HttpServletRequest request, HttpServletResponse response, ModelMap model)
    {
    	TopicPage topicPage = topicPageDao.get(id);
    	String feedData = null;
    	DayLifeFeeds dayLifeFeeds = null;
    	Rss2Feeds rss2Feeds = null;
    	if(topicPage.getFeedUrl() != null && !"".equals(topicPage.getFeedUrl()))
		{
			// Pull the data from feed
			String feedUrl = topicPage.getFeedUrl().replace("{0}", topicPage.getEntityName());
			try {
				logger.debug("Getting data from daylife feed {} " , feedUrl);
				feedData = Utilities.executeGet(feedUrl);
			} catch (IOException e) {
				logger.error("IOException getting data from daylife feed " , e);
			} catch (Exception e) {
				logger.error("Exception getting data from daylife feed " , e); 
			}
			if(feedData != null && !"".equals(feedData))
			{
				try
				{
					dayLifeFeeds = ToiUtils.parseDayLifeFeeds(jaxb2Marshaller, feedData);
				}
				catch(Exception e)
				{
					try {
						rss2Feeds = ToiUtils.parseRss2Feeds(jaxb2Marshaller, feedData);
					} catch (Exception e1) {
						e1.printStackTrace();
						logger.error("Error parsing daylife feed data for url {} \n {} \n", new Object[]{feedUrl, feedData}, e);
					}
				}
			}
			
			String token = null;
			if(topicPage.getAccessToken() != null && !"".equals(topicPage.getAccessToken()))
			{
				token = topicPage.getAccessToken();
			}
			else
			{
				token = topicPage.getWebsite().getAccessToken();
			}
			
			// save in database
			Website website = topicPage.getWebsite();
			Article article = null;
			
			if (dayLifeFeeds != null && dayLifeFeeds.getItemList() != null
					&& dayLifeFeeds.getItemList().size() > 0) {
				Item item = dayLifeFeeds.getItemList().get(0);
				article = new Article(topicPage, website, item, true, 0, true,
						false);
				article.setUrl(item.getUrl());
				article.setMessage(item.getHeadline());
			} else {
				ChannelItem item = rss2Feeds.getChannel().getItemList().get(0);
				article = new Article(topicPage, website, item, true, 0, true,
						false);
				article.setUrl(item.getLink());
				//article.setMessage(item.getTitle());
			}
			
			if (article.getUrl().endsWith("cms")) {
				article.setArticleid(article.getUrl()
						.substring(article.getUrl().lastIndexOf("/") + 1)
						.replaceFirst(".cms", ""));
			} else {
				article.setArticleid(Utilities.encodePassword(article.getUrl()
						.substring(article.getUrl().lastIndexOf("/") + 1),
						"MD5"));
			}

			TopicPageArticle topicPageArticle = new TopicPageArticle();
			
			if (website.getSocialAppName().equalsIgnoreCase("facebook")) {
				article.addUtmParameters(topicPage);
				PageDetail pageDetail = FacebookUtils.postlinks(token,
						topicPage.getPageId(), article.getUrl(),
						article.getMessage());

				if (pageDetail.getId() == null) {
					if (pageDetail.getError() != null) {
						topicPageArticle.setRemarks(pageDetail.getError()
								.getMessage());
						topicPageArticle.setStatus(TopicPageArticle.PENDING);
					}
					// Check Follow redirects incase we get this exception
					// HTTP_MOVED_PERM
					try {
						StringBuffer redirectUrl = new StringBuffer();
						int rCode = Utilities.getResponseCode(article.getUrl(),
								redirectUrl);
						if (HttpURLConnection.HTTP_MOVED_PERM == rCode) {
							// Change the existing url to new url
							topicPageArticle.setRemarks("URL changed from "
									+ article.getUrl() + " to " + redirectUrl);
							article.setUrl(redirectUrl.toString());
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				} else {
					if (pageDetail.getId().indexOf("_") > 0) {
						topicPageArticle
								.setPostId(pageDetail.getId().substring(
										pageDetail.getId().indexOf("_") + 1));
					} else {
						topicPageArticle.setPostId(pageDetail.getId());
					}

					topicPageArticle.setStatus(TopicPageArticle.PROCESSED);
				}
			} else if (website.getSocialAppName().equalsIgnoreCase("twitter")) {
				article.addUtmParameters(topicPage);
				ConfigurationBuilder cb = new ConfigurationBuilder();
				cb.setDebugEnabled(true);
				cb.setOAuthConsumerKey(website.getSocialAppId());
				cb.setOAuthConsumerSecret(website.getSocialApiSecret());
				cb.setOAuthAccessToken(website.getAccessToken());
				cb.setOAuthAccessTokenSecret(website.getAccessTokenSecret());
				Twitter twitter = new TwitterFactory(cb.build()).getInstance();
				Status status = null;
				int statusCode = 0;
				String statusMsg = null;
				try {
					status = TwitterUtils.updateStatus(twitter, null,
							article.getUrl(), article.getMessage());
					if (status != null) {
						for (URLEntity urlEntity : status.getURLEntities()) {
							article.setShortenerurl(urlEntity.getURL()
									.toString());
							article.setShortenerhash(urlEntity.getURL()
									.toString().substring(12));
						}
					}
				} catch (TwitterException e) {
					logger.error("error while posting on twitter", e);
					e.printStackTrace();
					statusCode = e.getStatusCode();
					statusMsg = e.getErrorMessage();
					if (statusMsg == null) {
						statusMsg = e.getMessage();
					}
					if (statusCode == 401) {
						e.printStackTrace();
						logger.error("Unable to authenticate twitter account "
								+ topicPage.getEntityName() + " . Article "
								+ article.getUrl() + " " + article.getMessage()
								+ " not posted", e);
					}
				}
				if (status != null) {
					topicPageArticle.setPostId(status.getId() + "");
					topicPageArticle.setStatus(TopicPageArticle.PROCESSED);
					article.setProcessed(true);
				} else {
					// Duplicate tweet check
					if (statusCode != 403) {
						topicPageArticle.setStatus(TopicPageArticle.PENDING);
					} else {
						topicPageArticle.setStatus(TopicPageArticle.PROCESSED);
					}
					topicPageArticle.setRemarks(statusCode + ":" + statusMsg);
				}
			}
			
			articleDao.save(article);
			topicPageArticle.setArticle(article);
			topicPageArticle.setTopicPage(topicPage);
			if (topicPageArticle.getRemarks() != null) {
				topicPageArticle.setRemarks("TestPush: "
						+ topicPageArticle.getRemarks());
			} else {
				topicPageArticle.setRemarks("TestPush");
			}
			topicPageArticleDao.save(topicPageArticle);
			
			if(logger.isDebugEnabled())
			{
				logger.debug("Feed Posted to website with accesstoken " + token +
						" having facebook page-id " + topicPage.getPageId() +
						" topicpage id " + topicPage.getId());
			}
			model.addAttribute("msg", article.getUrl() + " posted to topic");
		}
    	
    	TopicListBean backingObject = new TopicListBean();
		backingObject = getData(request, backingObject, false);
		model.addAttribute("topicListBean", backingObject);
    	return "listTopic";
    }

	@RequestMapping(value = "/scheduleMessage", method = RequestMethod.POST)
	public String scheduleMessage(@Valid TopicListBean topicListBean, BindingResult result, ModelMap model, HttpServletRequest request,
									@RequestParam("topicPage.id") Integer topicId,@RequestParam("message") String msg, @RequestParam("url") String url) {
		if (result.hasErrors()) {
			return "listTopic";
		}
		try {
			if (url.isEmpty() || !url.startsWith("http")) {
				throw new RuntimeException("Please enter a valid url to schedule a message.");
			}

			Calendar cal = Calendar.getInstance();
			Article article = new Article();
			article.setScheduled(true);
			article.setCreatedate(cal.getTime());
			article.setMessage(msg);
			article.setUrl(url);

			Boolean autoSchedule = Boolean.parseBoolean(request.getParameter("autoSchedule"));
			if (autoSchedule != null && !autoSchedule) {
				article.setFeedtimestamp(article.getCreatedate());
			} else {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String date = request.getParameter("scheduleDate");
				int hour = Integer.parseInt(request.getParameter("scheduleHour"));
				int min = Integer.parseInt(request.getParameter("scheduleMinute"));
				Boolean afterNoon = Boolean.parseBoolean(request.getParameter("afterNoon"));

				cal.setTime(sdf.parse(date));
				cal.set(Calendar.HOUR, hour);
				cal.set(Calendar.MINUTE, min);
				if (afterNoon) {
					cal.set(Calendar.AM_PM, Calendar.PM);
				} else {
					cal.set(Calendar.AM_PM, Calendar.AM);
				}
				article.setFeedtimestamp(cal.getTime());
				if (article.getCreatedate().after(article.getFeedtimestamp())) {
					throw new RuntimeException("Schedule date/time must be after current date/time");
				}

				Boolean sendEmail = Boolean.parseBoolean(request.getParameter("emailMe"));
				article.setNotificationMail(sendEmail);
				if(sendEmail) {
					String email = request.getParameter("email");
					if(email == null || email.isEmpty()) {
						throw new RuntimeException(
								"You must enter an email to get notification.");
					}
					article.setEmail(email);
				}
			}
			TopicPage topicPage = topicPageDao.findById(topicId);
			Website website = topicPage.getWebsite();
			article.setTopicPage(topicPage);
			article.setTopicName(topicPage.getEntityName());
			article.setWebsite(website);
			article.setProcessed(false);
			article.setSocialappid(website.getSocialAppId());
			if (article.getUrl() != null && !article.getUrl().isEmpty()) {
				if (article.getUrl().endsWith("cms")) {
					article.setArticleid(article.getUrl().substring(article.getUrl().lastIndexOf("/") + 1).replaceFirst(".cms", ""));
				} else {
					article.setArticleid(Utilities.encodePassword(article.getUrl().substring(article.getUrl().lastIndexOf("/") + 1), "MD5"));
				}
				article.addUtmParameters(topicPage);
			}

			// Check whether the article is in locking period or not?
			cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - lockingPeriodInDays);
			List<Article> articleList = articleDao.findArticleByTopicPageIdDate(topicPage.getId(),cal.getTime());
			for (Article oldArticle : articleList) {
				if (article.getArticleid().equalsIgnoreCase(oldArticle.getArticleid())) {
					if (oldArticle.getProcessed()) {
						throw new RuntimeException("Given link already posted on " + oldArticle.getCreatedate());
					} else if (oldArticle.getScheduled()) {
						throw new RuntimeException("Given link already scheduled on " + oldArticle.getFeedtimestamp());
					}
				}
			}
			articleDao.saveOrUpdate(article);
			model.addAttribute("msg", "Message scheduled for " + article.getFeedtimestamp());
		} catch (Exception e) {
			model.addAttribute("msg", e.getMessage());
		}

		return onSubmit(topicListBean, result, model, request);
	}

}
