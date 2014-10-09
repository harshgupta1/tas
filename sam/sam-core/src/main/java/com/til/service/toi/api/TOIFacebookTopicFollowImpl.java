package com.til.service.toi.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.oxm.UnmarshallingFailureException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;

import com.til.service.common.api.Follow;
import com.til.service.common.api.Page;
import com.til.service.common.dao.ArticleDao;
import com.til.service.common.dao.TopicPageDao;
import com.til.service.common.dao.hibernate.entity.Article;
import com.til.service.common.dao.hibernate.entity.TopicPage;
import com.til.service.common.dao.hibernate.entity.Website;
import com.til.service.toi.ToiUtils;
import com.til.service.toi.api.DayLifeFeedsV2.Site;
import com.til.service.toi.api.DayLifeFeedsV2.Site.ItemV2;
import com.til.service.utils.Utilities;

@Service("toiFacebookTopicFollowImpl")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class TOIFacebookTopicFollowImpl implements Follow{
	
	private static final Logger log = LoggerFactory.getLogger(TOIFacebookTopicFollowImpl.class);
	
	@Autowired
	private TopicPageDao topicPageDao;
	
	@Autowired
	private ArticleDao	articleDao;
	
	@Autowired
	Jaxb2Marshaller jaxb2Marshaller;
	
	@Autowired
	@Qualifier("facebookOpenGraphPage")
	private Page facebookPage;
	
	private Website website;
	
	private int resultsPerThread;
	private int startPage;
	
	@Value("${facebook.post}")
	private String post;
	
	@Autowired
	public TOIFacebookTopicFollowImpl(int startPage, int resultsPerThread, Website website)
	{
		this.startPage = startPage;
		this.resultsPerThread = resultsPerThread;
		this.website = website;
	}
	
	@Override
	public void run() {
		
		try
		{
			Calendar currentDate = Calendar.getInstance();
			currentDate.set(Calendar.HOUR, -12);
			currentDate.set(Calendar.MINUTE,0);
			currentDate.set(Calendar.SECOND,0);
			currentDate.set(Calendar.MILLISECOND,0);
			
			Calendar last24Hours = Calendar.getInstance();
			last24Hours.add(Calendar.HOUR, -24);
			
			log.debug("Executing thread request for page {}",startPage);
			// Step-4: Get all the ACTIVE topicpages for this website
			List<TopicPage> topicPageList = topicPageDao.findByActiveAndWebsiteId(true, website.getId(),startPage,resultsPerThread);
			
			facebookPage.fetchPageDetails(topicPageList);
			
			String feedData = null;
			DayLifeFeedsV2 dayLifeFeeds = null;
			List<Article> articleFeedList = new ArrayList<Article>();
			
			// Step-9: PULL the data that is to be sent to facebook users
			for (TopicPage topicPage : topicPageList) {
				String topicPageFacebookId = topicPage.getPageId();
				if(topicPageFacebookId != null && !"".equals(topicPageFacebookId))
				{
					// Check if the page has any likes
					log.debug("Total likes for page {} are {} ", topicPage.getEntityName() ,topicPage.getLikes());
					
					if(topicPage.getLikes() > 0)
					{
						// Check if any article has already been posted today or not
						List<Article> articleList = articleDao.findArticleByTopicPageId(0,1,topicPage.getId());
						if(articleList.size() > 0)
						{
							Article article = articleList.get(0);
							// If this article belongs to current day then continue next loop
							if(article.getCreatedate().after(currentDate.getTime()))
							{
								continue;
							}
						}
						// Check if the data for this topic has to be read from some feedurl or db
						if(topicPage.getFeedUrl() != null && !"".equals(topicPage.getFeedUrl()))
						{
							// Pull the data from feed
							String feedUrl = topicPage.getFeedUrl().replace("{0}", topicPage.getEntityName());
							String[] feedXML = null;
								
							try {
								feedXML = Utilities.executeGetWithResponsecode(feedUrl);
								feedData = feedXML[1]; 
								log.debug("Getting data from daylife feed {} \n {}" , new Object[]{feedUrl,feedData});
							}
							catch (IOException e) {
								log.error("IOException getting data from daylife feed url {},\n{} ",feedUrl,e);
								feedData = null;
								dayLifeFeeds = null;
							} catch (Exception e) {
								log.error("Exception getting data from daylife feed url {},\n{} ",feedUrl,e); 
								feedData = null;
								dayLifeFeeds = null;
							}
								
							if(feedData != null && !"".equals(feedData))
							{
								try
								{
									dayLifeFeeds = ToiUtils.parseDayLifeFeedsV2(jaxb2Marshaller, feedData);
								}
								catch(UnmarshallingFailureException e)
								{
									log.error("UnmarshallingFailureException getting data from daylife feed {} \n {}" , 
											new Object[]{topicPage.getUrl(),e});
									dayLifeFeeds = null;
								}
								catch(Exception e)
								{
									dayLifeFeeds = null;
									log.error("Error parsing daylife feed data for url {} \n {} \n", new Object[]{feedUrl, feedData}, e);
								}
							}
								
							if(dayLifeFeeds != null && dayLifeFeeds.getSiteList() != null &&
									dayLifeFeeds.getSiteList().size() > 0)
							{
								// Check if the "source" field in website table has some source websitename
								// If it is then fetch the article with the same source from daylifefeed
								List<ItemV2> itemList = null;
								for (Site site : dayLifeFeeds.getSiteList()) {
								
									if(website.getSource() != null && !"".equals(website.getSource()))
									{
										if(site.getSource().equalsIgnoreCase(website.getSource()))
										{
											itemList = site.getItemList();
											break;
										}
									}
									// Assume "Times Of India" as the default source
									else if("Times Of India".equalsIgnoreCase(site.getSource()))
									{
										itemList = site.getItemList();
										break;
									}
								}
								
								// Get all the storyids
								StringBuffer articleIds = new StringBuffer();
								// Fetch the items generated in Last 24Hours
								ItemV2 item = null;
								Map<String,ItemV2> itemMap = new HashMap<String, ItemV2>(itemList.size());
								for (ItemV2 itemV2 : itemList) {
									if(itemV2.getPubDate().after(last24Hours.getTime()))
									{
										int lastSlashPos = itemV2.getUrl().lastIndexOf("/");
										if(lastSlashPos > 0)
										{
											String articleid = itemV2.getUrl().substring(lastSlashPos+1).replace(".cms", "");
											articleIds.append(articleid);
											articleIds.append(",");
											itemMap.put(articleid,itemV2);
										}
									}
								}
								// If link is null, this means no news has been generated
								// Don't process further
								if(articleIds.length() == 0)
								{
									log.debug("No article has been generated in last 24 hours for url {}",feedUrl);
									continue;
								}
								
								// format pageviewsapi
								String url = website.getPageviewsapi().replace("{articleid}",articleIds);
								// Hit iBeat API & get PageViews
								JSONObject jsonObject = null;
								
								String[] pageViewsJson = null;
								try
								{
									pageViewsJson = Utilities.executeGetWithResponsecode(url);
									jsonObject = ToiUtils.parseiBeatFeed(pageViewsJson[1]);
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
								long _totalClicks = 0;
								// Fetch the item with highest pageviews
								if(jsonObject != null)
								{
									for (Entry<String, ItemV2> entry : itemMap.entrySet()) {
										try
										{
											_totalClicks = jsonObject.getJSONObject(entry.getKey()).getLong("hourlyClicks");
											if(clicks < _totalClicks)
											{
												clicks = _totalClicks;
												item = entry.getValue();
											}
										}
										catch(JSONException e)
										{
											log.warn("Pageviews doesnot exists for article {}",entry.getValue().getUrl());
										}
									}
								}
								else
								{
									log.debug("JSON Object is null for url {}", url);
								}
								
								// Item is null in case daylifefeed does not have the said item
								if(item != null)
								{
									// BUSINESS RULE: 1 update per day per topic
									Article article = new Article();
									article = new Article(topicPage, website, item, false, _totalClicks,true,false);
									article.setArticleid(item.getUrl(),"TOI");
									article.setUrl(article.getUrl() + "?utm_source=facebook&utm_medium=toitopicsonFB&utm_campaign=" + topicPage.getEntityName());
									articleFeedList.add(article);
									log.debug("New article {} for topic {} generated.",item.getTitle() , article.getTopicName());
								}// if(item != null)
							}
							else
							{
								if(dayLifeFeeds == null)
								{
									log.debug("DayLife feed is null for url {}.",feedUrl);
								}else if(dayLifeFeeds.getSiteList() == null)
								{
									log.debug("SiteList inside dayLife feed is null for url {}.",feedUrl);
								}
										
							}
						}
					} // if(topicPage.getLikes() > 0)
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
