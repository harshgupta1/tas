package com.til.service.toi.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;

import com.til.service.common.api.BreakingNews;
import com.til.service.common.api.BreakingNews.News;
import com.til.service.common.api.Follow;
import com.til.service.common.api.Page;
import com.til.service.common.dao.ArticleDao;
import com.til.service.common.dao.TopicPageDao;
import com.til.service.common.dao.hibernate.entity.Article;
import com.til.service.common.dao.hibernate.entity.TopicPage;
import com.til.service.common.dao.hibernate.entity.Website;
import com.til.service.toi.ToiUtils;
import com.til.service.utils.Utilities;

@Service("toiTwitterBreakingNewsFollowImpl")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TOITwitterBreakingNewsFollowImpl implements Follow{
	
	private static final Logger log = LoggerFactory.getLogger(TOITwitterBreakingNewsFollowImpl.class);
	
	@Autowired
	private TopicPageDao topicPageDao;
	
	@Autowired
	private ArticleDao	articleDao;
	
	@Autowired
	Jaxb2Marshaller jaxb2Marshaller;
	
	@Autowired
	@Qualifier("twitterCategoryPage")
	private Page  twitterPage;
	
	private Website website;
	
	private int resultsPerThread;
	private int startPage;
	
	@Value("${twitter.post}")
	private String post;
	
	@Autowired
	public TOITwitterBreakingNewsFollowImpl(int startPage, int resultsPerThread, Website website)
	{
		this.startPage = startPage;
		this.resultsPerThread = resultsPerThread;
		this.website = website;
	}
	
	@Override
	public void run() {
		
		try
		{
			// Represents an object to build today's date at 12AM
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR, -100);
			cal.set(Calendar.MINUTE,0);
			cal.set(Calendar.SECOND,0);
			cal.set(Calendar.MILLISECOND,0);
			
			// Step-4: Get all the ACTIVE topicpages for this website
			log.debug("Executing thread request for page {}",startPage);
			// Step-4: Get all the ACTIVE topicpages for this website
			List<TopicPage> topicPageList = topicPageDao.findByActiveAndWebsiteId(true, website.getId(),startPage,resultsPerThread);
			
			for (TopicPage topicPage : topicPageList) {
				topicPage.setWebsite(website);
			}
			twitterPage.fetchPageDetails(topicPageList);
			
			String feedData = null;
			BreakingNews breakingNews  = null;
			List<Article> articleFeedList = new ArrayList<Article>();
			
			// Step-9: PULL the data that is to be sent to twitter users
			for (TopicPage topicPage : topicPageList) {
				//Override the default time interval
				if(topicPage.getFeedpicktime() != 0)
					cal.set(Calendar.HOUR, topicPage.getFeedpicktime());
				
				String topicPageTwitterId = topicPage.getPageId();
				if(topicPageTwitterId != null && !"".equals(topicPageTwitterId))
				{
					// Check if the page has any likes
					log.debug("Total likes for page {} are {} ", topicPage.getEntityName() ,topicPage.getLikes());
	
					// Check if the data for this topic has to be read from some feedurl or db
					if(topicPage.getFeedUrl() != null && !"".equals(topicPage.getFeedUrl()))
					{
						// Pull the data from feed
						String[] feedXML = null;
						try {
							feedXML = Utilities.executeGetWithResponsecode(topicPage.getFeedUrl());
							feedData = feedXML[1];
							log.debug("Getting data from TOI Breaking News feed {} \n {}" ,topicPage.getFeedUrl(),feedData);
						}
						catch (IOException e) {
							feedData = null;
							breakingNews = null;
							//log.error("IOException getting data from TOI Breaking News  feed {} \n {}" ,topicPage.getUrl(), e);
						} catch (Exception e) {
							feedData = null;
							breakingNews = null;
							//log.error("Exception getting data from TOI L1 category  feed {} \n {}" ,topicPage.getUrl(), e); 
						}
						if(feedData != null && !"".equals(feedData))
						{
							try
							{
								breakingNews = ToiUtils.parseBreakingNewsFeeds(jaxb2Marshaller, feedData);
							}
							catch(Exception e)
							{
								breakingNews = null;
								log.error("Error parsing TOI Breaking News feed data for url {} \n {} \n" + "", new Object[]{topicPage.getFeedUrl(), feedData}, e);
							}
						}
						
	
						// save in database
						if(breakingNews != null && breakingNews.getNewsList() != null 
								&& breakingNews.getNewsList().size() > 0)
						{
							// Fetch all the stories 
							//Check if the story has been posted earlier
							List<Article> articleList = articleDao.findArticleByTopicPageIdDate(topicPage.getId(),cal.getTime());
							
							boolean articleFound = false;
							// Post all the breaking news
							for (News news : breakingNews.getNewsList()) {
								for (Article article : articleList) {
									if(article.getMessage().equalsIgnoreCase(news.getNewsTitle()))
									{
										articleFound = true;
										break;
									}
								}
								if(!articleFound)
								{
									//If not found, then app posts story on the page.
									Article article = new Article(topicPage,topicPage.getEntityName(),website.getSocialAppId(),
														website,news.getNewsTitle(),"",false,new Date(),0 );
									
									article.setMessage(article.getMessage());
									articleFeedList.add(article);
									log.debug("Adding new article for topic {}," ,article.getTopicName());
								}
								else
								{
									articleFound = false;
								}
							}
						}
					}
				}
			}
			articleDao.saveOrUpdateAll(articleFeedList);
			
			log.debug("Saving {} articles in article table.", articleFeedList.size());
			
			// Step-10: Make a join between topicpage & article table & Pull the data from DB
			if("true".equalsIgnoreCase(post))
				twitterPage.postArticle(website);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			log.error("This exception was not handled earlier and due to this, it was causing current thread to die. Please check. ", e);
		}
	}

	public Website getWebsite() {
		return website;
	}

	public void setWebsite(Website website) {
		this.website = website;
	}

	public int getResultsPerThread() {
		return resultsPerThread;
	}

	public void setResultsPerThread(int resultsPerThread) {
		this.resultsPerThread = resultsPerThread;
	}

	public int getStartPage() {
		return startPage;
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}
	
}
