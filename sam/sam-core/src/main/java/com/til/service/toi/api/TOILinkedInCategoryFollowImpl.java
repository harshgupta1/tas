package com.til.service.toi.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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

@Service("toiLinkedInCategoryFollowImpl")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TOILinkedInCategoryFollowImpl implements Follow{
	
	private static final Logger log = LoggerFactory.getLogger(TOILinkedInCategoryFollowImpl.class);
	
	@Autowired
	private TopicPageDao topicPageDao;
	
	@Autowired
	private ArticleDao	articleDao;
	
	@Autowired
	Jaxb2Marshaller jaxb2Marshaller;
	
	@Autowired
	@Qualifier("linkedinCategoryPage")
	private Page  LinkedInPage;
	
	private Website website;
	
	private int resultsPerThread;
	private int startPage;
	
	@Value("${linkedin.post}")
	private String post;
	
	@Autowired
	public TOILinkedInCategoryFollowImpl(int startPage, int resultsPerThread, Website website)
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
			
			for (TopicPage topicPage : topicPageList) {
				topicPage.setWebsite(website);
			}
			LinkedInPage.fetchPageDetails(topicPageList);
			
			String feedData = null;
			Rss2Feeds rss2Feeds  = null;
			List<Article> articleFeedList = new ArrayList<Article>();
			
			// Step-9: PULL the data that is to be sent to LinkedIn users
			for (TopicPage topicPage : topicPageList) {

				// Check whether current time is valid push time or not.
				if (topicPage.getPushTime() != null
						&& !topicPage.getPushTime().isNaN()) {
					Float pushTime = topicPage.getPushTime() * 60;
					int hour = (int) (pushTime / 60);
					int min = (int) (pushTime % 60);
					Calendar cal1 = Calendar.getInstance();
					if (cal1.get(Calendar.HOUR_OF_DAY) < hour
							|| (cal1.get(Calendar.HOUR_OF_DAY) == hour && cal1
									.get(Calendar.MINUTE) < min)) {
						// if current time is less then push time do not process topicPage.
						continue;
					}
				}
				try
				{
					// Uncomment this to debug a specific topicpage
					/*if(topicPage.getId() == 389)
					{
						log.debug("topicpage to be debugged {}", topicPage.getEntityName());
					}*/
					//Override the default time interval
					if(topicPage.getFeedpicktime() != 0)
						cal.set(Calendar.HOUR, topicPage.getFeedpicktime());
					
					String topicPageLinkedinId = topicPage.getPageId();
					if(topicPageLinkedinId != null && !"".equals(topicPageLinkedinId))
					{
						// Check if the page has any likes
						log.debug("Total likes for page {} are {} ", topicPage.getEntityName() ,topicPage.getLikes());
		
						// Check if the data for this topic has to be read from some feedurl or db
						if(topicPage.getFeedUrl() != null && !"".equals(topicPage.getFeedUrl()))
						{
							// Pull the data from feed
							String[] feedXML = null;
							String feedUrl = topicPage.getFeedUrl().replace("{0}", topicPage.getEntityName().replaceAll(" ", "-"));
							try {
								feedXML = Utilities.executeGetWithResponsecode(feedUrl);
								feedData = feedXML[1]; 
								log.debug("Getting data from TOI L1 category feed {} \n {}" ,topicPage.getFeedUrl(),feedData);
							}
							catch (IOException e) {
								feedData = null;
								rss2Feeds = null;
								//log.error("IOException getting data from TOI L1 category  feed {} \n {}" ,topicPage.getUrl(), e);
							} catch (Exception e) {
								feedData = null;
								rss2Feeds = null;
								//log.error("Exception getting data from TOI L1 category  feed {} \n {}" ,topicPage.getUrl(), e); 
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
							
							int forwardSlashPos = -1, dotPos=-1, questionPos=-1;
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
								/*int $break = 0;*/
								for (ChannelItem channelItem : channelItemList) {
									link = new StringBuffer(channelItem.getLink());
									int lastSlashPos = link.lastIndexOf("/");
									if(lastSlashPos > 0)
									{
										String articleid = link.substring(lastSlashPos+1).replace(".cms", "");
										channelItem.setArticleid(articleid);
										// Include article with publish date as 12 AM
										if(channelItem.getPubDate().after(cal.getTime()) || channelItem.getPubDate().getTime() == cal.getTimeInMillis())
										{
											articleIds.append(articleid);
											articleIds.append(",");
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
								// NOTE: If there are no articles to be sent to get pageviews then exit
								// break condition only applicable for linkedin case where 1 website will have 1 topic
								if(articleIds.length() == 0)
								{
									break;
								}
								// format pageviewsapi
								String url = website.getPageviewsapi().replace("{articleid}",articleIds).replace("{host}",host);
								// Hit iBeat API
								JSONObject dataObject = null;
								String[] pageViewsJson = null;
								try
								{
									pageViewsJson = Utilities.executeGetWithResponsecode(url);
									dataObject = ToiUtils.parseiBeatFeed(pageViewsJson[1]);
								}
								catch(JSONException e)
								{
									log.error("JSONException getting data from iBeat page views api {} for topic {} \n {}",
											new Object[]{url,topicPage.getEntityName(),e});
								}
								catch(Exception e)
								{
									log.error("Exception while getting pageviews from url {}, Exception {}",url,e);
								}
								//Check for the most read story in the last 1 hours from the universe
								if(dataObject != null)
								{
									Collections.sort(channelItemList,new ItemComparator<ChannelItem>(dataObject));
								}
		
								if(channelItemList!=null && !channelItemList.isEmpty())
								{
									//Check if the story has been posted earlier
									List<Article> articleList = articleDao.findArticleByTopicPageIdDate(topicPage.getId(),cal.getTime());
									// Additional logic for city pages with only 3 feeds per day
									if(topicPage.getPoststhreshold() == null || topicPage.getPoststhreshold() == 0 || topicPage.getPoststhreshold() > articleList.size())
									{
										// This represents the item that is to be posted
										ChannelItem item = null;
										for (ChannelItem channelItem : channelItemList) {
											item = channelItem;
											String id="";
											try
											{
												id = channelItem.getUrl().substring(channelItem.getUrl().lastIndexOf("/")+1,channelItem.getUrl().lastIndexOf("."));
											}
											catch(StringIndexOutOfBoundsException e)
											{
												// This is for article url like this http://blogs.timesofindia.indiatimes.com/gray-areas/entry/omar-abdullah
												id = channelItem.getUrl().substring(channelItem.getUrl().lastIndexOf("/")+1);
												//log.error("StringIndexOutofBoundsException for channel url {} and title {} and topicpage " + topicPage.getEntityName(), channelItem.getUrl(), channelItem.getTitle());
											}
											for (Article article : articleList) {
												// Don't compare article title as some pushed articles may not have article title which is pushed
												// Compare article-ids
												if(article.getArticleid().equals(id))
												{
													item = null;
													break;
												}
											}
											if(item!=null)
											{
												break;
											}
										}
										
										if(item != null)
										{	
											//If not, then app posts story on the page.
											long clicks=0;
											try {
												clicks = dataObject.getJSONObject(item.getUrl().substring(item.getUrl().lastIndexOf("/")+1,item.getUrl().lastIndexOf("."))).getLong("hourlyClicks");
											} catch (Exception e) {
												continue;
											}
											// quite possible that sorted channel list has some items whose view count is 0 
											if(clicks > 0)
											{
												Article article = new Article(topicPage, website,item, false, clicks, false, true);
												article.setArticleid(item.getUrl(),"TOI");
												article.setMessage(item.getTitle());
												article.addUtmParameters(topicPage);
												articleFeedList.add(article);
												log.debug("Adding new article for topic {}," ,article.getTopicName());
											}
											else
											{
												log.debug("Clicks 0, not adding new article for topic {}," ,topicPage.getEntityName());
											}
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
									if(item.getPubDate() == null || item.getPubDate().after(cal.getTime()) || item.getPubDate().getTime() == cal.getTimeInMillis())
									{
										forwardSlashPos = item.getUrl().lastIndexOf("/");
										dotPos = item.getUrl().lastIndexOf(".");
										questionPos = item.getUrl().lastIndexOf("?");
										String a1 = null ;
										if(dotPos > forwardSlashPos)
										{
											a1 = item.getUrl().substring(forwardSlashPos+1,dotPos);
										}
										else
										{
											a1 = (questionPos > forwardSlashPos) ? 
													item.getUrl().substring(forwardSlashPos+1, questionPos) : item.getUrl().substring(forwardSlashPos+1);
												
										}
										for (Article article : articleList) {
 											if(article.getArticleid().equalsIgnoreCase(a1))
											{
												articleFound = true;
												break;
											}
										}
										if(!articleFound && a1 != null)
										{	
											//If not, then app posts story on the page.
											Article article = new Article(topicPage, website, item, false, 0, true, false);
											article.setArticleid(item.getUrl(),"TOI");
											article.addUtmParameters(topicPage);
											articleFeedList.add(article);
											log.debug("Adding new article for topic {}," ,article.getTopicName());
											break;
										}
										articleFound = false;
									}
								}
							}
						}
					}
				}
				catch(Exception e)
				{
					log.error("Exception processing feed for topic " + topicPage.getEntityName(), e);
				}
			}
			articleDao.saveOrUpdateAll(articleFeedList);
			
			log.debug("Saving {} articles for {} in article table.", articleFeedList.size(), website.getName());
			
			// Step-10: Make a join between topicpage & article table & Pull the data from DB
			if("true".equalsIgnoreCase(post)) {
				log.info("posting is enabled for Linkedin");
				LinkedInPage.postArticle(website);
			} else {
				log.warn("posting is disabled for LinkedIn");
			}
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
