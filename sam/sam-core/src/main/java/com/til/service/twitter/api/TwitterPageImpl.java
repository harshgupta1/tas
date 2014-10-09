package com.til.service.twitter.api;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.URLEntity;
import twitter4j.conf.ConfigurationBuilder;

import com.til.service.common.api.Accounts;
import com.til.service.common.api.Page;
import com.til.service.common.api.TOIMailSender;
import com.til.service.common.dao.ArticleDao;
import com.til.service.common.dao.EmailsDao;
import com.til.service.common.dao.TopicPageArticleDao;
import com.til.service.common.dao.TopicPageDao;
import com.til.service.common.dao.TopicPageHistoryDao;
import com.til.service.common.dao.hibernate.entity.Article;
import com.til.service.common.dao.hibernate.entity.Emails;
import com.til.service.common.dao.hibernate.entity.TopicPage;
import com.til.service.common.dao.hibernate.entity.TopicPageArticle;
import com.til.service.common.dao.hibernate.entity.TopicPageHistory;
import com.til.service.common.dao.hibernate.entity.Website;
import com.til.service.facebook.api.PageDetail;
import com.til.service.toi.api.TOIUrlShortenerFactory;
import com.til.service.twitter.TwitterUtils;

/**
 * 
 * @author Harsh.Gupta
 *
 *	Ref https://dev.twitter.com/docs/error-codes-responses 
 */
public abstract class TwitterPageImpl implements Page {
	
private static final Logger log = LoggerFactory.getLogger(TwitterPageImpl.class);
	
	@Autowired
	private TopicPageDao topicPageDao;
	
	@Autowired
	private ArticleDao	articleDao;
	
	@Autowired
	private TopicPageArticleDao	topicPageArticleDao;
	
	@Autowired
	private TopicPageHistoryDao	topicPageHistoryDao;
	
	@Value("#{jdbcConfiguration['followJob.topicPageArticleBatch']}")
	private Integer topicPageArticleBatch;
	
	@Autowired
	private TOIUrlShortenerFactory urlShortenerFactory; 
	
	@Autowired
	private EmailsDao emailsDao;
	
	@Autowired
	private TOIMailSender toiMailSender;
	
	@Value("#{jdbcConfiguration['scheduleMessageJob.notificationMail.subject']}")
	private String notificationMailSubject;

	@Value("#{jdbcConfiguration['scheduleMessageJob.notificationMail.content']}")
	private String notificationMailContent;
	
	/**
	 * This function get the pagedetails for current twitter page and updates the current 
	 * information in topicpage table and insert in topicpagehistory table
	 * 
	 * @param pagelinks			twitterpageid.(Not used in case of Twitter 
	 * @param twitteridMap		PageDetail map where key is twitterpageid  
	 * 							and value is detail of page itself
	 */

	public void updatepagedetails(StringBuffer pagelinks,Map<String, TopicPage> twitteridMap) {

		TopicPage topicPage = twitteridMap.get(pagelinks.toString());
		Accounts accounts;
		try {
			accounts = TwitterUtils.getTwitterAccountDetails(topicPage.getWebsite());
			int dislikes = 0;
			PageDetail pageDetail = accounts.getData()[0];
			
			if(topicPage.getLikes() > pageDetail.getLikes())
	    	{
	    		dislikes = topicPage.getLikes() - pageDetail.getLikes();
	    	}
			
			topicPage.setCategory(pageDetail.getCategory());
			topicPage.setLikes(pageDetail.getLikes());
			topicPage.setDescription(pageDetail.getDescription());
	   		topicPage.setLink(pageDetail.getWebsite());
			topicPage.setUsername(pageDetail.getUsername());
			topicPage.setPicture(pageDetail.getPicture());
			topicPage.setPageName(pageDetail.getName());
			topicPage.setShares(pageDetail.getShares());
			topicPage.setTalking_about_count(pageDetail.getTalking_about_count());
			topicPage.setError("");
			if(pageDetail.getId() != null)
			{
				topicPage.setPageId(pageDetail.getId());
			}
			// Step-8: Update the topicpage details in DB 
		    topicPageDao.saveOrUpdate(topicPage);
		    
		    // If Last fetched topicpagehistory is same as per the current likes count, then don't create a new row
		    TopicPageHistory history = topicPageHistoryDao.findByTopicPage(topicPage);
		    int topicPageHistoryId = 0;
		    if(history == null || topicPage.getLikes() == null || !topicPage.getLikes().equals(history.getLikes()))
		    {
				TopicPageHistory topicPageHistory = new TopicPageHistory();
				topicPageHistory.setTopicPage(topicPage);
				topicPageHistory.setLikes(pageDetail.getLikes());
				topicPageHistory.setDislikes(dislikes);
				topicPageHistory.setShares(pageDetail.getShares());
			    topicPageHistoryDao.saveOrUpdate(topicPageHistory);
			    topicPageHistoryId = topicPageHistory.getId();
		    }
	    	log.debug("Updating topicpageid {} & inserting topicpage_history id {}",
	    			topicPage.getId(),(history == null || history.getLikes() != topicPage.getLikes()) ? topicPageHistoryId : 0);
		} catch (TwitterException e) {
			log.error("TwitterException generated while retrieving details for topic {}. Information not updated in topicpage table. \n {}"
					,topicPage.getEntityName(),e);
		}
		catch (Exception e) {
			log.error("Exception generated while retrieving details for topic {}. Information not updated in topicpage table. \n {}. This exception was not handled earlier."
					,topicPage.getEntityName(),e);
		}
	
	}

	public void postArticle(Website website) {
		postArticle(website, false);
	}
	
	public void postArticle(Website website, boolean scheduled) {
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
		log.debug("Twitter Post to be sent to {} pages.", list.size());
		
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
				ConfigurationBuilder cb = new ConfigurationBuilder();
	        	cb.setDebugEnabled(true);
	        	cb.setOAuthConsumerKey(website.getSocialAppId());
	        	cb.setOAuthConsumerSecret(website.getSocialApiSecret());
	        	cb.setOAuthAccessToken(website.getAccessToken());
	        	cb.setOAuthAccessTokenSecret(website.getAccessTokenSecret());
	        	Twitter twitter =  new TwitterFactory(cb.build()).getInstance();
	        	
	        	/*String shortenedUrl=null;
	        	TOIMinifiedURL tOIMinifiedURL = null;
	        	if(website.getShortenerapi() != null && !"".equalsIgnoreCase(website.getShortenerapi())){
			    	
					UrlShortener urlShortener = urlShortenerFactory.getObject(website.getShortenerapi());
					try {
						tOIMinifiedURL = urlShortener.getMinifiedURL(website.getShortenerapi() + URLEncoder.encode(article.getUrl(), "UTF-8"));
					} catch (UnsupportedEncodingException e) {
						log.error("UnsupportedEncodingException while encoding url {} , \n {}",article.getUrl(),e);
					}
			    }*/
				Status status  = null;
				int statusCode = 0;
				String statusMsg = null;
				// If Url is shortened, then only send the update
				// NOTE: Article URL is blank in case of breaking news
		    	/*if("".equals(article.getUrl()) || (tOIMinifiedURL != null && "200".equalsIgnoreCase(tOIMinifiedURL.getStatus_code()))){
		    		shortenedUrl = !"".equals(article.getUrl()) ? tOIMinifiedURL.getData().getUrl() : null;
		    		try {
						status = TwitterUtils.updateStatus(twitter,shortenedUrl,article.getUrl(),article.getMessage());
					} catch (TwitterException e) {
						statusCode = e.getStatusCode();
						statusMsg = e.getErrorMessage();
					}
		    		if(log.isDebugEnabled())
					{
						log.debug("Feed " + i + " Posted to website with accesstoken " + website.getAccessToken() +
								" having twitter page-id " + topicPage.getPageId() +
								" topicpage id " + topicPage.getId() + " . Posted article " + article.getUrl() +
								" Article id " + ((status !=null) ? status.getId() : null));
					}
	        	}*/
				try {
					
				// implementation for web twitter APIs	
					
					if(null !=article.getPostedphotoid()&&article.getApireq()==true)
					{
						String location=System.getProperty("catalina.base").replace("\\", "/")+"/upload";
						File f = new File(location+"/"+article.getPostedphotoid()+"_"+article.getArticleid());		
						status = TwitterUtils.updateStatuswithMedia(twitter, article.getMessage()+"\r\n\r\n"+article.getUrl(), f);	
					}
					else
					{	
						status = TwitterUtils.updateStatus(twitter,null,article.getUrl(),article.getMessage());
					}
					if(status != null)
					{
						for(URLEntity urlEntity : status.getURLEntities())
						{
							article.setShortenerurl(urlEntity.getURL().toString());
							article.setShortenerhash(urlEntity.getURL().toString().substring(12));
						}
					}
					
				} catch (TwitterException e) {
					statusCode = e.getStatusCode();
					statusMsg = e.getErrorMessage();
					if(statusMsg == null)
					{
						statusMsg = e.getMessage();
					}
					//https://dev.twitter.com/docs/error-codes-responses 
					// Twitter Over capacity messages
					if(statusCode != 503 && e.getErrorCode() != 130)
					{
						e.printStackTrace();
						log.error("For Twitter account " + topicPage.getEntityName() + 
								"\n Article " + article.getUrl() + " " + article.getMessage() + " not posted\n", e);
					}
				}
	    		if(log.isDebugEnabled())
				{
					log.debug("Feed " + i + " Posted to website with accesstoken " + website.getAccessToken() +
							" having twitter page-id " + topicPage.getPageId() +
							" topicpage id " + topicPage.getId() + " . Posted article " + article.getUrl() +
							" Article id " + ((status !=null) ? status.getId() : null));
				}
	    		
				TopicPageArticle topicPageArticle = new TopicPageArticle();
				topicPageArticle.setArticle(article);
				topicPageArticle.setTopicPage(topicPage);
				if(status != null)
				{
					topicPageArticle.setPostId(status.getId()+"");
					topicPageArticle.setStatus(TopicPageArticle.PROCESSED);
					article.setProcessed(true);
				}
				else
				{
					// Duplicate tweet check
			    	if(statusCode != 403)
			    	{
						topicPageArticle.setStatus(TopicPageArticle.PENDING);
						article.setProcessed(false);
			    	}
			    	else
			    	{
			    		topicPageArticle.setStatus(TopicPageArticle.PROCESSED);
						article.setProcessed(true);
			    	}
			    	topicPageArticle.setRemarks(statusCode + ":" + statusMsg);
				}
				sendNotificationMail(website, topicPage, article);
				/*if(shortenedUrl != null)
				{
					article.setShortenerhash(shortenedUrl.substring(shortenedUrl.lastIndexOf("/")+1));
					article.setShortenerurl(shortenedUrl);
				}*/
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

	private void sendNotificationMail(Website website, TopicPage topicPage, Article article) {
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

	@Override
	public void regenerateExpiredToken(Website w) {
		// TODO Auto-generated method stub
		
	}

}
