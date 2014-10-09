package com.til.service.linkedin.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.til.service.common.api.Rss2Feeds;
import com.til.service.common.api.Rss2Feeds.Channel;
import com.til.service.common.dao.hibernate.entity.TopicPage;
import com.til.service.common.dao.hibernate.entity.TopicPageAdditionalFeeds;
import com.til.service.toi.ToiUtils;
import com.til.service.utils.Utilities;

@Service("linkedinCategoryPage")
public class LinkedInCategoryPageImpl extends LinkedinPageImpl {
	
	private static final Logger log = LoggerFactory.getLogger(LinkedInCategoryPageImpl.class);
	
	@Value("#{jdbcConfiguration['followJob.topicPageBatch']}")
	private Integer topicPageBatch;
	
	public void fetchPageDetails(List<TopicPage> topicPageList){
		Map<String,TopicPage> linkedinidMap = new HashMap<String, TopicPage>();
		StringBuffer pagelinks = new StringBuffer();

		// Get the topicPages in BatchSize of 10
		// This is done to get the updated details of topicPages
		
		for (int i=0; i<topicPageList.size(); i++) {
			
			TopicPage page = topicPageList.get(i);
			// Step-5:  Add topic to Map and append those to pagelinks buffer
			if(page.getPageId() == null || "".equals(page.getPageId()))
			{
				linkedinidMap.put(page.getUrl(), page);
				pagelinks.append(page.getUrl());
				pagelinks.append(",");
			}
			else
			{
				linkedinidMap.put(page.getPageId(), page);
				pagelinks.append(page.getPageId());
				pagelinks.append(",");
			}
			if(i%topicPageBatch==0 || i == topicPageList.size()-1)
			{
				log.trace("pagelinks for row {} {}",i,pagelinks);
				// Delete the last character("comma") from the string buffer
				pagelinks.deleteCharAt(pagelinks.length()-1);
				
				// Step-6: Get the PageDetails for all the pagelinks that are (comma seperated)
				updatepagedetails(pagelinks,linkedinidMap);
				
				// Clear Buffer
				pagelinks.delete(0, pagelinks.length());
				linkedinidMap.clear();
			}
		}
	}
	
	public Object appendAdditionalFeedsData(TopicPage topicPage)
	{
		if(topicPage.getMultipleFeedUrl())
		{
			Rss2Feeds rss2Feeds = new Rss2Feeds();
			rss2Feeds.setChannel(new Channel());
			rss2Feeds.getChannel().setItemList(new ArrayList<Rss2Feeds.ChannelItem>());
			// Get All the Additional Feed Urls from DB
			List<TopicPageAdditionalFeeds> list = topicPageAdditionalFeedsDao.findByTopicPageId(topicPage.getId());
			String[] feedXML = null;
			String feedData = null;
			for (TopicPageAdditionalFeeds topicPageAdditionalFeeds : list) {
				
				try {
					feedXML = Utilities.executeGetWithResponsecode(topicPageAdditionalFeeds.getFeedUrl());
					feedData = feedXML[1]; 
					log.debug("Getting data from L1 category feed {} \n {}" ,topicPageAdditionalFeeds.getFeedUrl(), feedData);
				}
				catch (IOException e) {
					feedData = null;
					rss2Feeds = null;
					log.error("IOException getting data from L1 category  feed {} \n {}" ,topicPageAdditionalFeeds.getFeedUrl(), e);
				} catch (Exception e) {
					feedData = null;
					rss2Feeds = null;
					log.error("Exception getting data from L1 category  feed {} \n {}" , topicPageAdditionalFeeds.getFeedUrl(), e); 
				}
				if(feedData != null && !"".equals(feedData))
				{
					try
					{
						Rss2Feeds feeds = ToiUtils.parseRss2Feeds(jaxb2Marshaller, feedData);
						if(feeds != null && feeds.getChannel() != null && feeds.getChannel().getItemList() != null && 
								feeds.getChannel().getItemList().size() > 0)
						{
							rss2Feeds.getChannel().getItemList().addAll(feeds.getChannel().getItemList());
						}
					}
					catch(Exception e)
					{
						log.error("Error parsing TOI L1 category feed data for url {} \n {} \n" + "", 
								new Object[]{topicPageAdditionalFeeds.getFeedUrl(), feedData}, e);
					}
				}
			}
			return rss2Feeds;
		}
		return null;
	}
}
