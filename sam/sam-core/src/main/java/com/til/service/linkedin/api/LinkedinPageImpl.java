package com.til.service.linkedin.api;

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
import com.til.service.common.dao.hibernate.entity.Article;
import com.til.service.common.dao.hibernate.entity.Emails;
import com.til.service.common.dao.hibernate.entity.TopicPage;
import com.til.service.common.dao.hibernate.entity.TopicPageArticle;
import com.til.service.common.dao.hibernate.entity.TopicPageHistory;
import com.til.service.common.dao.hibernate.entity.Website;
import com.til.service.facebook.api.PageDetail;
import com.til.service.linkedin.LinkedInUtils;
import com.til.service.utils.Utilities;

public abstract class LinkedinPageImpl implements Page {

	private static final Logger log = LoggerFactory.getLogger(LinkedinPageImpl.class);
	
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
	private EmailsDao emailsDao;
	
	@Autowired
	private TOIMailSender toiMailSender;
	
	@Autowired
	TopicPageAdditionalFeedsDao topicPageAdditionalFeedsDao;
	
	@Autowired
	Jaxb2Marshaller jaxb2Marshaller;
	
	@Value("#{jdbcConfiguration['scheduleMessageJob.notificationMail.subject']}")
	private String notificationMailSubject;

	@Value("#{jdbcConfiguration['scheduleMessageJob.notificationMail.content']}")
	private String notificationMailContent;
	
	@Override
	public void updatepagedetails(StringBuffer pagelinks,Map<String, TopicPage> linkedinidMap) {

		TopicPage topicPage = linkedinidMap.get(pagelinks.toString());
		try 
			{
			String profileJson = LinkedInUtils.getprofiledetails(null,topicPage.getAccessToken());
			log.debug("linkedin user profile json {}",profileJson);
			LinkedInProfile linkedInProfile = LinkedInUtils.parseProfileDetails(profileJson);
			topicPage.setDescription(linkedInProfile.getHeadline());
	   		topicPage.setLink(linkedInProfile.getPublicProfileUrl());
			topicPage.setUsername(linkedInProfile.getEmailAddress());
			topicPage.setPicture(linkedInProfile.getPictureUrl());
			topicPage.setPageName(linkedInProfile.getFormattedName());
			topicPage.setFriends(Integer.parseInt(linkedInProfile.getNumConnections()));
			topicPage.setError("");
			topicPage.setPageId(linkedInProfile.getId());
			// Step-8: Update the topicpage details in DB 
		    topicPageDao.saveOrUpdate(topicPage);
		    
		    // If Last fetched topicpagehistory is same as per the current likes count, then don't create a new row
		    TopicPageHistory history = topicPageHistoryDao.findByTopicPage(topicPage);
		    int topicPageHistoryId = 0;
		    if(history == null || topicPage.getLikes() == null || !topicPage.getLikes().equals(history.getLikes()))
		    {
				TopicPageHistory topicPageHistory = new TopicPageHistory();
				topicPageHistory.setTopicPage(topicPage);
			    topicPageHistoryDao.saveOrUpdate(topicPageHistory);
			    topicPageHistoryId = topicPageHistory.getId();
		    }
	    	log.debug("Updating topicpageid {} & inserting topicpage_history id {}",
	    			topicPage.getId(),(history == null || history.getLikes() != topicPage.getLikes()) ? topicPageHistoryId : 0);
		} catch (Exception e) {
			log.warn("Exception generated while retrieving details for topic {}. Information not updated in topicpage table. \n {}"
					,topicPage.getEntityName(),e);
		}
		
	
	}


	@Override
	public void postArticle(Website website) {
		postArticle(website, false);
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
	

	@Override
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
		log.debug("Linkedin Post to be sent to {} pages.", list.size());
		
		List<TopicPageArticle> topicPageArticleList = new ArrayList<TopicPageArticle>(topicPageArticleBatch);
		for (int i=0; i< list.size(); i++) {
			Object[] object = (Object[]) list.get(i);
			Article article = (Article)object[0];
			TopicPage topicPage = (TopicPage)object[1];
			try
			{
				// Step-11: Post the data to Linkedin pages.
				String token = website.getAccessToken();
				if(topicPage.getAccessToken() != null && !"".equals(topicPage.getAccessToken()))
				{
					token = topicPage.getAccessToken();
				}
				PageDetail pageDetail = null;
				
				if(null != article.getUrl() && !"".equals(article.getUrl())){
					pageDetail = LinkedInUtils.postfeed(token, 
					topicPage.getPageId(),article.getUrl(),article.getMessage(),null,null,null);
				}
				
				if(log.isDebugEnabled())
				{
					log.debug("Feed " + i + " Posted to website with accesstoken " + website.getAccessToken() +
							" having linkedin page-id " + topicPage.getPageId() +
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

	@Override
	public void fetchPageDetails(List<TopicPage> topicPageList) {
		// This is done to get the updated details of topicPages
		for (int i=0; i<topicPageList.size(); i++) {
			TopicPage page = topicPageList.get(i);
			Map<String, TopicPage> linkedinidMap = new HashMap<String, TopicPage>(1);
			linkedinidMap.put(page.getPageId(),page);
			try
			{
				updatepagedetails(new StringBuffer(page.getPageId()),linkedinidMap);
			}
			catch(NullPointerException e)
			{
				log.error("NullPointerException for page "+ (page != null ? page.getEntityName() : "Page Object is null"),e);
			}
			catch(Exception e)
			{
				log.error("This exception was not handled earlier for page "+ (page != null ? page.getEntityName() : "Page Object is null"),e);
			}
		}
	}
	
	@Override
	public Object appendAdditionalFeedsData(TopicPage topicPage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void regenerateExpiredToken(Website w) {
		// TODO Auto-generated method stub

	}

}
