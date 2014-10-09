package com.til.service.google.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.til.service.common.api.Page;
import com.til.service.common.api.TOIMailSender;
import com.til.service.common.dao.ArticleDao;
import com.til.service.common.dao.EmailsDao;
import com.til.service.common.dao.TopicPageArticleDao;
import com.til.service.common.dao.TopicPageDao;
import com.til.service.common.dao.TopicPageHistoryDao;
import com.til.service.common.dao.hibernate.entity.TopicPage;
import com.til.service.common.dao.hibernate.entity.TopicPageHistory;
import com.til.service.common.dao.hibernate.entity.Website;
import com.til.service.facebook.api.PageDetail;
import com.til.service.google.GooglePlusUtils;
import com.til.service.linkedin.LinkedInUtils;
import com.til.service.linkedin.api.LinkedInProfile;

public abstract class GooglePlusPageImpl implements Page {
	
	private static final Logger log = LoggerFactory.getLogger(GooglePlusPageImpl.class);
	
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
	
	@Value("#{jdbcConfiguration['scheduleMessageJob.notificationMail.subject']}")
	private String notificationMailSubject;

	@Value("#{jdbcConfiguration['scheduleMessageJob.notificationMail.content']}")
	private String notificationMailContent;
	
	@Override
	public void updatepagedetails(StringBuffer pagelinks,Map<String, TopicPage> linkedinidMap) {

		TopicPage topicPage = linkedinidMap.get(pagelinks.toString());
		Website website=null;
		website=topicPage.getWebsite();
		String accessToken=topicPage.getAccessToken();
		if(accessToken!=null)
		{	
		String accessTokenres[] = accessToken.split("=");
		accessToken = accessTokenres[1];
		}
		try 
			{
			String freshaccesstoken=GooglePlusUtils.getNewAccessToken(website.getSocialAppId(),website.getSocialApiSecret(),accessToken,"refresh_token");
			String profileJson = GooglePlusUtils.getprofiledetails(freshaccesstoken,website.getSocialAppId(),website.getSocialApiSecret());
			log.debug("googleplus user profile json {}",profileJson);
			GoogleProfile googleprofileData = new GoogleProfile();
			GoogleProfile googleprofile = (GoogleProfile) JSONObject.toBean(
					JSONObject.fromObject(GooglePlusUtils
							.getprofiledetails(freshaccesstoken,
									website.getSocialAppId(),
									website.getSocialApiSecret())),
					googleprofileData, new JsonConfig());
	   		topicPage.setLink(googleprofile.getLink());
			topicPage.setUsername(googleprofile.getEmail());
			topicPage.setPicture(googleprofile.getPictureurl());
			topicPage.setPageName(googleprofile.getFirst_name());	
			topicPage.setError("");
			topicPage.setPageId(googleprofile.getId());
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

	@Override
	public void postArticle(Website website, boolean scheduled) {
		// TODO Auto-generated method stub

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
