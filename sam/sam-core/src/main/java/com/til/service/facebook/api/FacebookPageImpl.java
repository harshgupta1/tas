package com.til.service.facebook.api;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.til.service.common.api.Page;
import com.til.service.common.api.TOIMailSender;
import com.til.service.common.dao.ArticleDao;
import com.til.service.common.dao.EmailsDao;
import com.til.service.common.dao.TopicPageAdditionalFeedsDao;
import com.til.service.common.dao.TopicPageArticleDao;
import com.til.service.common.dao.TopicPageDao;
import com.til.service.common.dao.TopicPageHistoryDao;
import com.til.service.common.dao.WebsiteDao;
import com.til.service.common.dao.hibernate.entity.Article;
import com.til.service.common.dao.hibernate.entity.Emails;
import com.til.service.common.dao.hibernate.entity.TopicPage;
import com.til.service.common.dao.hibernate.entity.TopicPageArticle;
import com.til.service.common.dao.hibernate.entity.TopicPageHistory;
import com.til.service.common.dao.hibernate.entity.Website;
import com.til.service.facebook.FacebookUtils;
import com.til.service.utils.DateUtil;
import com.til.service.utils.Utilities;

public abstract class FacebookPageImpl implements Page{
	
	private static final Logger log = LoggerFactory.getLogger(FacebookPageImpl.class);
	
	@Autowired
	private WebsiteDao websiteDao;
	
	@Autowired
	private TopicPageDao topicPageDao;
	
	@Autowired
	private ArticleDao	articleDao;
	
	@Autowired
	private TopicPageArticleDao	topicPageArticleDao;
	
	@Autowired
	private TopicPageHistoryDao	topicPageHistoryDao;
	
	@Autowired
	TopicPageAdditionalFeedsDao topicPageAdditionalFeedsDao;
	
	@Autowired
	Jaxb2Marshaller jaxb2Marshaller;
	
	@Value("#{jdbcConfiguration['followJob.topicPageArticleBatch']}")
	private Integer topicPageArticleBatch;

	@Autowired
	private EmailsDao emailsDao;
	
	@Autowired
	private TOIMailSender toiMailSender;
	
	@Value("#{jdbcConfiguration['scheduleMessageJob.notificationMail.subject']}")
	private String notificationMailSubject;

	@Value("#{jdbcConfiguration['scheduleMessageJob.notificationMail.content']}")
	private String notificationMailContent;
	
	/**
	 * This function get the pagedetails for all the links/pages and updates the current 
	 * information in topicpage table and insert in topicpagehistory table
	 * 
	 * @param pagelinks			comma separated urls or facebookpageid
	 * @param facebookidMap		PageDetail map where key is either page url or facebookpageid  
	 * 							and value is detail of page itself
	 */
	public void updatepagedetails(StringBuffer pagelinks,Map<String,TopicPage> facebookidMap)
	{
		List<TopicPageHistory> topicPageHistoryList = new ArrayList<TopicPageHistory>();
		log.debug("Getting page details for pages {}" , pagelinks.toString());
		Map<String, PageDetail> pageDetailMap = Collections.EMPTY_MAP;
		if(pagelinks.toString().startsWith("http://"))
		{
			// Step-7: Parse the returned JSON
			String jsonStr = FacebookUtils.getPageDetail(pagelinks.toString());
			if(jsonStr != null && !"".equals(jsonStr) && !"[]".equals(jsonStr))
			{
				pageDetailMap = FacebookUtils.parsePageLinks(pagelinks.toString(), jsonStr);
			}
		}
		else
		{
			pageDetailMap = new HashMap<String, PageDetail>();
			String fbPageId[] = pagelinks.toString().split(",");
			for (String pageid : fbPageId) {
				PageDetail pageDetail = getPageDetail(facebookidMap.get(pageid),pageid);
        		pageDetailMap.put(pageid, pageDetail);
			}
		}
		
	    for (Map.Entry<String,PageDetail> e : pageDetailMap.entrySet()) {  
	        String url = e.getKey();  
	        PageDetail pageDetail = e.getValue();  
	        
	        // now do something with key and value
	        TopicPage topicPage = facebookidMap.get(url);
	        
	        if(pageDetail == null)
	        {
	        	log.warn("PageDetails is null for url {} \n JSON {}",url, e);
	        	// PageDetail is null in case page is not open to end users, 
	        	// In this case passing accesstoken along with each individual page 
	        	// will help in getting pagedetails
	        	pageDetail = getPageDetail(facebookidMap.get(url),url);
	        }
	        
	        if(pageDetail != null)
	        {
		        if(pageDetail.getId() == null)
		        {
		        	if(pageDetail.getError() != null)
		        	{
		        		topicPage.setError(pageDetail.getError().getMessage());
		        	}
		        }
		        else
		        {
		        	// Store dislikes
		        	int dislikes = 0;
		        	if(topicPage.getLikes() > pageDetail.getLikes())
		        	{
		        		dislikes = topicPage.getLikes() - pageDetail.getLikes();
		        	}
		        	topicPage.setCategory(pageDetail.getCategory());
		        	topicPage.setLikes(pageDetail.getLikes());
		        	topicPage.setFriends(pageDetail.getFriends());
		        	topicPage.setFollowers(pageDetail.getFollowers());
		        	topicPage.setDescription(pageDetail.getDescription());
		        	if(pageDetail.getLink() != null && !"".equals(pageDetail.getLink()))
		        	{
		        		topicPage.setLink(pageDetail.getLink());
		        	}
		        	else if(pageDetail.getWebsite() != null && !"".equals(pageDetail.getWebsite()))
		        	{
		        		topicPage.setLink(pageDetail.getWebsite());
		        	}
		        	topicPage.setUsername(pageDetail.getUsername());
		        	topicPage.setPicture(pageDetail.getPicture());
		        	topicPage.setPageName(pageDetail.getName());
		        	topicPage.setShares(pageDetail.getShares());
		        	topicPage.setTalking_about_count(pageDetail.getTalking_about_count());
		        	topicPage.setError("");
		        	try
					{
		        		Long.parseLong(pageDetail.getId());
		        		topicPage.setPageId(pageDetail.getId());
					}
		        	catch(NumberFormatException numberFormatException)
					{
						topicPage.setError(numberFormatException.toString());
					}
		        	TopicPageHistory history = topicPageHistoryDao.findByTopicPage(topicPage);
				    if(history == null || topicPage.getLikes() == null || !topicPage.getLikes().equals(history.getLikes()))
				    {
			        	TopicPageHistory topicPageHistory = new TopicPageHistory();
			        	topicPageHistory.setTopicPage(topicPage);
			        	topicPageHistory.setLikes(pageDetail.getLikes());
			        	topicPageHistory.setDislikes(dislikes);
			        	topicPageHistory.setShares(pageDetail.getShares());
			        	topicPageHistoryList.add(topicPageHistory);
				    }
		        }
	        }
	        
	    }  // for (Map.Entry<String,PageDetail> e : pageDetailMap.entrySet())
	    
	    // Step-8: Update the topicpage details in DB 
	    topicPageDao.saveOrUpdateAll(facebookidMap.values());
	    topicPageHistoryDao.saveOrUpdateAll(topicPageHistoryList);
    	log.debug("Updating {} topicpage & {} topicpage_history table ",
    			facebookidMap.values().size(), topicPageHistoryList.size());
		
	}
	
	/**
	 * This method is used to get pagedetails for individual facebook page based on its accesstoken
	 * 
	 * @param tp		TopicPage details for this facebook page		
	 * @param pageid	facebook page id
	 * @return
	 */
	private PageDetail getPageDetail(TopicPage tp,String pageid)
	{
		if(tp != null)
		{
			String jsonStr = FacebookUtils.getPageDetail(pageid,tp.getAccessToken());
	    	if(jsonStr != null && !"".equals(jsonStr) && !"[]".equals(jsonStr))
			{
	    		PageDetail detail = FacebookUtils.parsePageLinks(jsonStr);
				try {
					Followers followers = FacebookUtils
							.parseFacebookFollowers(FacebookUtils.getFacebookFollowers(pageid,tp.getAccessToken()));
					if (followers.getError() == null) {
						detail.setFollowers(followers.getSummary().getTotal_count());
					}
					Friends friends = FacebookUtils
							.parseFacebookFriends(FacebookUtils.getFacebookFriends(pageid,tp.getAccessToken()));
					if (friends.getError() == null) {
						detail.setFriends(friends.getData().length);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
	    		return detail;
			}
		}
		return null;
	}
	
	/**
	 * This method posts various article to facebook page in a batch
	 * 
	 * @param website	Object whose articles are pushed
	 */
	public void postArticle(Website website)
	{
		this.postArticle(website, false);
	}
	
	public void postArticle(Website website, boolean scheduled){
		List list = null;
		List<Article> articleList = new ArrayList<Article>();
		Calendar currentDate = Calendar.getInstance();
		currentDate.set(Calendar.HOUR, -12);
		currentDate.set(Calendar.MINUTE,0);
		currentDate.set(Calendar.SECOND,0);
		currentDate.set(Calendar.MILLISECOND,0);
		synchronized (website) {
			if(scheduled) {
			list = articleDao.findArticleAndTopicPageByWebsiteId_Active_Processed_Rowlock_Scheduled(
								website.getId(),true, false,false,currentDate.getTime(), scheduled);
			} else {
				list = articleDao.findArticleAndTopicPageByWebsiteId_Active_Processed_Rowlock(
						website.getId(),true, false,false,currentDate.getTime());
			}
			if(list==null) list = Collections.EMPTY_LIST;
			// Update row_lock field in db
			for (int i=0; i< list.size(); i++) 
			{
				Object[] object = (Object[]) list.get(i);
				Article article = (Article)object[0];
				article.setRowlock(true);
				articleList.add(article);
			}
			articleDao.saveOrUpdateAll(articleList);
			// Close the synchronized blocks here 
		}
		articleList.clear();
		log.debug("Facebook Post to be sent to {} pages.", list.size());
		
		List<TopicPageArticle> topicPageArticleList = new ArrayList<TopicPageArticle>(topicPageArticleBatch);
		for (int i=0; i< list.size(); i++) {
			Object[] object = (Object[]) list.get(i);
			Article article = (Article)object[0];
			TopicPage topicPage = (TopicPage)object[1];
			try
			{
				// Step-11: Post the data to facebook pages.
				String token = website.getAccessToken();
				if(topicPage.getAccessToken() != null && !"".equals(topicPage.getAccessToken()))
				{
					token = topicPage.getAccessToken();
				}
				PageDetail pageDetail = null;
				
				if(null != article.getUrl() && !"".equals(article.getUrl())){
					pageDetail = FacebookUtils.postlinks(token, 
						topicPage.getPageId(),article.getUrl(),article.getMessage());
				}
//				Implementation for web facebook APIs	
				else if(null !=article.getPostedphotoid()&&article.getApireq()==true)
				{
					String location=System.getProperty("catalina.base").replace("\\", "/")+"/upload";
					File f = new File(location+"/"+article.getPostedphotoid()+"_"+article.getArticleid());	
					pageDetail = FacebookUtils.postImage(token, topicPage.getPageId(), f,article.getMessage()+"\r\n\r\n"+article.getUrl());					
				}
				else{
					pageDetail = FacebookUtils.postfeed(token, 
							topicPage.getPageId(),null,article.getMessage());
				}	
				if(log.isDebugEnabled())
				{
					log.debug("Feed " + i + " Posted to website with accesstoken " + website.getAccessToken() +
							" having facebook page-id " + topicPage.getPageId() +
							" topicpage id " + topicPage.getId() + " . Posted article " + article.getUrl());
				}
				
				TopicPageArticle topicPageArticle = new TopicPageArticle();
				topicPageArticle.setArticle(article);
				topicPageArticle.setTopicPage(topicPage);
				
				if(pageDetail.getId() == null)
				{
					if(pageDetail.getError() != null)
					{
						topicPageArticle.setRemarks(pageDetail.getError().getMessage());
						topicPageArticle.setStatus(TopicPageArticle.PENDING);
					}
					// Check Follow redirects incase we get this exception HTTP_MOVED_PERM 
					try {
						StringBuffer redirectUrl = new StringBuffer();
						int rCode = Utilities.getResponseCode(article.getUrl(),redirectUrl);
						if(HttpURLConnection.HTTP_MOVED_PERM == rCode)
						{
							// Change the existing url to new url
							topicPageArticle.setRemarks("URL changed from " + article.getUrl() + " to " + redirectUrl);
							article.setUrl(redirectUrl.toString());
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					article.setProcessed(false);
				}
				else
				{
					if(pageDetail.getId().indexOf("_")>0){
						topicPageArticle.setPostId(pageDetail.getId().substring(pageDetail.getId().indexOf("_")+1));
					}else{
						topicPageArticle.setPostId(pageDetail.getId());
					}
					
					topicPageArticle.setStatus(TopicPageArticle.PROCESSED);
					article.setProcessed(true);
					sendNotificationMail(website, topicPage, article);
				}
				article.setRowlock(false);
				articleList.add(article);
				topicPageArticleList.add(topicPageArticle);
				// Step-12: Save the updated relationship in db
				if(i%topicPageArticleBatch==0 || i==list.size()-1)
				{
					articleDao.saveOrUpdateAll(articleList);
					topicPageArticleDao.saveOrUpdateAll(topicPageArticleList);
					articleList.clear();
					topicPageArticleList.clear();
				}
			}
			catch(NumberFormatException e)
			{
				if(i==list.size()-1)
				{
					articleDao.saveOrUpdateAll(articleList);
					topicPageArticleDao.saveOrUpdateAll(topicPageArticleList);
					articleList.clear();
					topicPageArticleList.clear();
				}
			}
		}
	}

	/**
	 * @param website
	 * @param article
	 * @param topicPage
	 */
	private void sendNotificationMail(Website website, TopicPage topicPage,
			Article article) {
		if (article.getProcessed() && article.isNotificationMail()) {
			Emails email = new Emails();
			email.setCreateDate(new Date());
			email.setUpdateDate(email.getCreateDate());
			email.setUserName(topicPage.getWebsite().getName());
			email.setUserEmail(topicPage.getWebsite().getContactEmail());
			email.setToEmail(article.getEmail());
			email.setCcemail(topicPage.getWebsite().getContactEmail());
			email.setFromEmail("mailservice@indiatimes.com");
			email.setfromName("Social Audience Manager");
			email.setSite("audiencemanager.indiatimes.com");
			email.setSubject(notificationMailSubject);
			email.setData(notificationMailContent
					.replaceAll("\\{WEBSITE_NAME\\}", website.getName())
					.replaceAll("\\{TOPIC_NAME\\}", topicPage.getEntityName())
					.replaceAll("\\{SOCIAL_APP_NAME\\}",
							website.getSocialAppName())
					.replaceAll("\\{SENT_TIME\\}",
							article.getFeedtimestamp().toString())
					.replaceAll(
							"\\{MESSAGE_CONTENT\\}",
							article.getMessage()
									+ " "
									+ (article.getShortenerurl() != null
											&& !article.getShortenerurl()
													.isEmpty() ? article
											.getShortenerurl() : article
											.getUrl())));
			email.setStatus(Emails.STATUS_PENDING);
			toiMailSender.sendEmail(email, true);
			emailsDao.save(email);
		}
	}
	
	public abstract void fetchPageDetails(List<TopicPage> topicPageList);
	public abstract Object appendAdditionalFeedsData(TopicPage topicPage);

	@Override
	public void regenerateExpiredToken(Website w) {
		
		String errorJSON = null;
		Date expiresAt = new Date(System.currentTimeMillis());
		if(w.getExpiresat() != null)
		{
			expiresAt = w.getExpiresat();
		}
		long daysDiff = DateUtil.getDaysDiff(expiresAt);
		
		try
		{
			// If only less than 2 days are left, then regenerate a new token 
			if(daysDiff < 2)
			{
				if(w.getAccessToken() != null)
				{
					String response = FacebookUtils.getextendedaccesstoken(w.getSocialAppId(), w.getSocialApiSecret(), w.getAccessToken());
					errorJSON = response;
					if(response != null)
					{
						String longLivedToken[] = response.split("&");
						if(longLivedToken.length == 2 || longLivedToken[0].startsWith("access_token="))
						{
							w.setAccessToken(longLivedToken[0]);
							// Sometimes new token is re-generated without expires parameter
							if(longLivedToken.length == 2)
							{
								longLivedToken[1] = longLivedToken[1].replace("expires=", "");
								w.setExpires(Integer.parseInt(longLivedToken[1]));
								w.setExpiresat(Utilities.expiresAt(Integer.parseInt(longLivedToken[1])));
							}
							// Get All the pages of type PROFILE and set their topicpage accesstoken equivalent to the new website accesstoken
							List<TopicPage> topicPageList = topicPageDao.findByActiveAndWebsiteId(true, w.getId(), "PROFILE");
							for (TopicPage topicPage : topicPageList) {
								topicPage.setAccessToken(longLivedToken[0]);
							}
							
							// Update website and topcipage
							websiteDao.saveOrUpdate(w);
							topicPageDao.saveOrUpdateAll(topicPageList);
						}
					}
				}
			}
		}
		catch(Exception e)
		{
			log.error("Error regenerating token value for website id " + w.getId() + " ,name " + w.getName() + " , Error Response " + errorJSON, e);
		}
	}

	public WebsiteDao getWebsiteDao() {
		return websiteDao;
	}

	public void setWebsiteDao(WebsiteDao websiteDao) {
		this.websiteDao = websiteDao;
	}

	public TopicPageDao getTopicPageDao() {
		return topicPageDao;
	}

	public void setTopicPageDao(TopicPageDao topicPageDao) {
		this.topicPageDao = topicPageDao;
	}

	public ArticleDao getArticleDao() {
		return articleDao;
	}

	public void setArticleDao(ArticleDao articleDao) {
		this.articleDao = articleDao;
	}

	public TopicPageArticleDao getTopicPageArticleDao() {
		return topicPageArticleDao;
	}

	public void setTopicPageArticleDao(TopicPageArticleDao topicPageArticleDao) {
		this.topicPageArticleDao = topicPageArticleDao;
	}

	public TopicPageHistoryDao getTopicPageHistoryDao() {
		return topicPageHistoryDao;
	}

	public void setTopicPageHistoryDao(TopicPageHistoryDao topicPageHistoryDao) {
		this.topicPageHistoryDao = topicPageHistoryDao;
	}

	public TopicPageAdditionalFeedsDao getTopicPageAdditionalFeedsDao() {
		return topicPageAdditionalFeedsDao;
	}

	public void setTopicPageAdditionalFeedsDao(
			TopicPageAdditionalFeedsDao topicPageAdditionalFeedsDao) {
		this.topicPageAdditionalFeedsDao = topicPageAdditionalFeedsDao;
	}
}
