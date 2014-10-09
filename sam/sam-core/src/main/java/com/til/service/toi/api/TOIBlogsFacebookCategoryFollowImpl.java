package com.til.service.toi.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

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
import com.til.service.common.api.Item;
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

@Service("toiBlogsFacebookCategoryFollowImpl")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TOIBlogsFacebookCategoryFollowImpl implements Follow{
	
	private static final Logger log = LoggerFactory.getLogger(TOIBlogsFacebookCategoryFollowImpl.class);
	
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
	public TOIBlogsFacebookCategoryFollowImpl(int startPage, int resultsPerThread, Website website)
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
							log.debug("Getting data from TOI Blogs feed {} \n {}" ,topicPage.getFeedUrl(),feedData);
						}
						catch (IOException e) {
							feedData = null;
							rss2Feeds = null;
							log.error("IOException getting data from TOI Blogs feed {} \n {}" ,topicPage.getUrl(), e);
						} catch (Exception e) {
							feedData = null;
							rss2Feeds = null;
							log.error("Exception getting data from TOI Blogs feed {} \n {}" ,topicPage.getUrl(), e); 
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
								log.error("Error parsing TOI L1 category feed data for url {} \n {} \n" + "", new Object[]{topicPage.getFeedUrl(), feedData}, e);
							}
						}
						
						// save in database
						if(rss2Feeds != null && rss2Feeds.getChannel() != null 
								&& rss2Feeds.getChannel().getItemList() != null && 
									rss2Feeds.getChannel().getItemList().size() > 0)
						{
							// Fetch all the stories that appeared after 12AM
							//The universe from which the app will select stories will be from 12 am till now  in the folder for the category
							List<ChannelItem> channelItemList = rss2Feeds.getChannel().getItemList();
							
							// Get all the storyids
							StringBuffer articleIds = new StringBuffer();
							StringBuffer link = null;
							/*int $break = 0;*/
							for (ChannelItem channelItem : channelItemList) {
								// Include article with publish date as 12 AM
								if(channelItem.getPubDate().after(cal.getTime()) || channelItem.getPubDate().getTime() == cal.getTimeInMillis())
								{
									link = new StringBuffer(channelItem.getLink());
									int lastSlashPos = link.lastIndexOf("/");
									if(lastSlashPos > 0)
									{
										String articleid = Utilities.encodePassword(link.substring(lastSlashPos+1),"MD5");
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
							String host = link.replace(0,7,"").toString();
							int slashStartPos = host.indexOf("/");
							if(slashStartPos > 0)
							{
								host = host.substring(0,slashStartPos);
							}
							// format pageviewsapi
							String url = website.getPageviewsapi().replace("{articleid}",articleIds).replace("{host}",host);
							// Hit iBeat API
							JSONObject dataObject = null;
							
							String[] pageViewsJson = null;
							try
							{
								pageViewsJson = Utilities.executeGetWithResponsecode(url);
								JSONObject jsonObject = ToiUtils.parseiBeatLast4HoursFeed(pageViewsJson[1]);
								dataObject = jsonObject.getJSONObject("DATA");
							}
							catch(JSONException e)
							{
								log.error("JSONException getting data from iBeat page views api of url {} \n {}",url,e);
							}
							catch(Exception e)
							{
								log.error("Exception while getting pageviews from url {}",url,e);
							}
								
							long clicks = 0;
							long _4hourlyClicks = 0;
							Item item = null;
							//Check for the most read story in the last 4 hours from the universe
							if(dataObject != null)
							{
								for (ChannelItem channelItem : channelItemList) {
									if(channelItem.getPubDate().after(cal.getTime()) )
									{
										try
										{
											_4hourlyClicks = dataObject.getLong(channelItem.getArticleid());
											if(clicks < _4hourlyClicks)
											{
												clicks = _4hourlyClicks;
												item = channelItem;
											}
										}
										catch(JSONException e)
										{
											log.warn("Pageviews doesnot exists for article id {}",channelItem.getArticleid());
										}
									}
									else
									{
										break;
									}
								}
							}
							
							if(item != null)
							{
								//Check if the story has been posted earlier
								List<Article> articleList = articleDao.findArticleByTopicPageIdDate(topicPage.getId(),cal.getTime());
								boolean articleFound = false;
								// Additional logic for city pages with only 3 feeds per day
								if(topicPage.getPoststhreshold() == null || topicPage.getPoststhreshold() == 0 || topicPage.getPoststhreshold() > articleList.size())
								{
									for (Article article : articleList) {
										// Don't compare article title as some pushed articles may not have article title which is pushed
										// 12-Dec-2011. Added ignorecase check of URl as sometimes article urls are reentered with case ignored
										if(article.getUrl().substring(0,article.getUrl().indexOf("?")).equalsIgnoreCase(item.getUrl()))
										{
											articleFound = true;
											break;
										}
									}
									if(!articleFound)
									{	
										//If not, then app posts story on the page.
										Article article = new Article(topicPage, website, item, false, clicks, false, true);
										article.setArticleid(Utilities.encodePassword(item.getUrl().substring(item.getUrl().lastIndexOf("/")+1),"MD5"));
										article.addUtmParameters(topicPage);
										articleFeedList.add(article);
										log.debug("Adding new article for topic {}," ,article.getTopicName());
									}
								}
							}// if(item != null)
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
