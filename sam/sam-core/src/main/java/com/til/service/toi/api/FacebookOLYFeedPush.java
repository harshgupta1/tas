package com.til.service.toi.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
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

import com.til.service.common.api.Follow;
import com.til.service.common.api.Page;
import com.til.service.common.api.Rss2Feeds;
import com.til.service.common.api.Rss2Feeds.ChannelItem;
import com.til.service.common.dao.ArticleDao;
import com.til.service.common.dao.TopicPageDao;
import com.til.service.common.dao.hibernate.entity.Article;
import com.til.service.common.dao.hibernate.entity.TopicPage;
import com.til.service.common.dao.hibernate.entity.Website;
import com.til.service.toi.ToiUtils;
import com.til.service.utils.Utilities;

@Service("facebookOLYFeedPush")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class FacebookOLYFeedPush implements Follow{
	
	private static final Logger log = LoggerFactory.getLogger(FacebookOLYFeedPush.class);
	
	@Autowired
	private TopicPageDao topicPageDao;
	
	@Autowired
	private ArticleDao	articleDao;
	
	@Autowired
	Jaxb2Marshaller jaxb2Marshaller;
	
	@Autowired
	@Qualifier("facebookCategoryPage")
	private Page  facebookPage;
	
	private Website website;
	
	private int resultsPerThread;
	private int startPage;
	
	@Value("${facebook.post}")
	private String post;
	
	@Autowired
	public FacebookOLYFeedPush(int startPage, int resultsPerThread, Website website)
	{
		this.startPage = startPage;
		this.resultsPerThread = resultsPerThread;
		this.website = website;
	}
	
	@Override
	public void run() {
		// Represents an object to build today's date at 12AM
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR, -12);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MILLISECOND,0);
		
		// Step-4: Get all the ACTIVE topicpages for this website
		log.debug("Executing thread request for page {}",startPage);
		// Step-4: Get all the ACTIVE topicpages for this website
		List<TopicPage> topicPageList = topicPageDao.findByActiveAndWebsiteId(true, website.getId(),startPage,resultsPerThread);
		
		facebookPage.fetchPageDetails(topicPageList);
		
		String feedData = null;
		Rss2Feeds rss2Feeds  = null;
		List<Article> articleFeedList = new ArrayList<Article>();
		
		// Step-9: PULL the data that is to be sent to facebook users
		for (TopicPage topicPage : topicPageList) {
			//Override the default time interval
			if(topicPage.getFeedpicktime() != 0)
				cal.set(Calendar.HOUR, topicPage.getFeedpicktime());
			
			String topicPageFacebookId = topicPage.getPageId();
			if(topicPageFacebookId != null && !"".equals(topicPageFacebookId))
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
						log.debug("Getting data for facebook from Olympics feed {} \n {}" ,topicPage.getFeedUrl(), feedData);
					}
					catch (IOException e) {
						feedData = null;
						rss2Feeds = null;
						log.error("IOException getting data for facebook from Olympics feed {} \n {}" ,topicPage.getUrl(), e);
					} catch (Exception e) {
						feedData = null;
						rss2Feeds = null;
						log.error("Exception getting data for facebook from Olympics feed {} \n {}" , topicPage.getUrl(), e); 
					}
					if(feedData != null && !"".equals(feedData))
					{
						try
						{
							rss2Feeds = ToiUtils.parseRss2Feeds(jaxb2Marshaller, feedData);
						}
						catch(Exception e)
						{
							rss2Feeds = null;
							log.error("Error parsing Olympics feed data for facebook, url {} \n {} \n" + "", 
									new Object[]{topicPage.getFeedUrl(), feedData}, e);
						}
					}
					
					// Pull Additional Feeds and Add to Parent RssFeeds
					Rss2Feeds rssF = (Rss2Feeds)facebookPage.appendAdditionalFeedsData(topicPage);
					if(rssF != null && rssF.getChannel() != null && rssF.getChannel().getItemList() != null && 
							rssF.getChannel().getItemList().size() > 0)
					{
						if(rss2Feeds != null && rss2Feeds.getChannel() != null 
								&& rss2Feeds.getChannel().getItemList() != null && 
									rss2Feeds.getChannel().getItemList().size() > 0)
						{
							rss2Feeds.getChannel().getItemList().addAll(rssF.getChannel().getItemList());
						}
						else
						{
							rss2Feeds = rssF;
						}
					}
					int forwardSlashPos = 0, dotPos=0;
					// save in database
					if(rss2Feeds != null && rss2Feeds.getChannel() != null 
							&& rss2Feeds.getChannel().getItemList() != null && 
								rss2Feeds.getChannel().getItemList().size() > 0 && !topicPage.getPush_all_article())
					{
						// Fetch all the stories that appeared after 12AM
						//The universe from which the app will select stories will be from 12 am till now  in the folder for the category
						List<ChannelItem> channelItemList = rss2Feeds.getChannel().getItemList();

						// Get all the storyids
						StringBuffer articleIds = new StringBuffer();
						StringBuffer link = null;

						for (ChannelItem channelItem : channelItemList) {
							// Include article with publish date as 12 AM
							if(channelItem.getPubDate().after(cal.getTime()) || channelItem.getPubDate().getTime() == cal.getTimeInMillis())
							{
								link = new StringBuffer(channelItem.getLink());
								int lastSlashPos = link.lastIndexOf("/");
								if(lastSlashPos > 0)
								{
									String articleid = link.substring(lastSlashPos+1).replace(".cms", "");
									articleIds.append(articleid);
									articleIds.append(",");
									channelItem.setArticleid(articleid);
								}
							}
						}
						// If link is null, this means no news has been generated
						// Don't process further
						if(link == null)
						{
							continue;
						}

						ChannelItem item = channelItemList.get(0);
						if(item != null)
						{
							//Check if the story has been posted earlier
							List<Article> articleList = articleDao.findArticleByTopicPageIdDate(topicPage.getId(),cal.getTime());
							boolean articleFound = false;
							// Additional logic for city pages with only 3 feeds per day
							if(topicPage.getPoststhreshold() == null || topicPage.getPoststhreshold() == 0 || topicPage.getPoststhreshold() > articleList.size())
							{
								forwardSlashPos = item.getUrl().lastIndexOf("/");
								dotPos = item.getUrl().lastIndexOf(".");
								String a1 = null ;
								if(dotPos > forwardSlashPos)
								{
									a1 = item.getUrl().substring(forwardSlashPos+1,dotPos);
								}
								else
								{
									a1 = item.getUrl().substring(forwardSlashPos+1);
								}
								for (Article article : articleList) {
									// Don't compare article title as some pushed articles may not have article title which is pushed
									// 12-Dec-2011. Added ignorecase check of URl as sometimes article urls are reentered with case ignored
									// 17-Dec-2011. Compare only article-ids in case of TOI as sometimes article titles are changed with case ignored
									if(article.getArticleid().equalsIgnoreCase(a1))
									{
										articleFound = true;
										break;
									}
								}
								if(!articleFound)
								{	
									//If not, then app posts story on the page.
									Article article = new Article(topicPage, website, item, false, 0, false, true);
									article.setArticleid(item.getUrl(),"TOI");
									article.addUtmParameters(topicPage);
									articleFeedList.add(article);
									log.debug("Adding new article for topic {}," ,article.getTopicName());
								}
							}
						}// if(item != null)
					

					}
					// Additional logic to push all the articles without checking pageviews
					else if(rss2Feeds != null && rss2Feeds.getChannel() != null 
							&& rss2Feeds.getChannel().getItemList() != null && 
							rss2Feeds.getChannel().getItemList().size() > 0 && 
							topicPage.getPush_all_article())
					{
						//Check if the story has been posted earlier
						List<Article> articleList = articleDao.findArticleByTopicPageIdDate(topicPage.getId(),cal.getTime());
						boolean articleFound = false;
						for (ChannelItem item : rss2Feeds.getChannel().getItemList()) {
							if(item.getPubDate().after(cal.getTime()) || item.getPubDate().getTime() == cal.getTimeInMillis())
							{
								forwardSlashPos = item.getUrl().lastIndexOf("/");
								dotPos = item.getUrl().lastIndexOf(".");
								for (Article article : articleList) {
									String a1 = null ;
									if(dotPos > forwardSlashPos)
									{
										a1 = item.getUrl().substring(forwardSlashPos+1,dotPos);
									}
									else
									{
										a1 = item.getUrl().substring(forwardSlashPos+1);
									}
									if(article.getArticleid().equalsIgnoreCase(a1))
									{
										articleFound = true;
										break;
									}
								}
								if(!articleFound)
								{	
									//If not, then app posts story on the page.
									Article article = new Article(topicPage, website, item, false, 0, false, true);
									article.setArticleid(item.getUrl(),"TOI");
									article.addUtmParameters(topicPage);
									articleFeedList.add(article);
									log.debug("Adding new article for topic {}," ,article.getTopicName());
								}
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
			facebookPage.postArticle(website);
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
