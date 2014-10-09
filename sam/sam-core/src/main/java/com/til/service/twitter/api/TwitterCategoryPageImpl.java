package com.til.service.twitter.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.til.service.common.dao.hibernate.entity.TopicPage;

@Service("twitterCategoryPage")
public class TwitterCategoryPageImpl extends TwitterPageImpl{
	
	private static final Logger log = LoggerFactory.getLogger(TwitterCategoryPageImpl.class);
	
	public void fetchPageDetails(List<TopicPage> topicPageList){
		
		// This is done to get the updated details of topicPages
		for (int i=0; i<topicPageList.size(); i++) {
			TopicPage page = topicPageList.get(i);
			Map<String, TopicPage> twitteridMap = new HashMap<String, TopicPage>(1);
			twitteridMap.put(page.getPageId(),page);
			try
			{
				updatepagedetails(new StringBuffer(page.getPageId()),twitteridMap);
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
	
	public Object appendAdditionalFeedsData(TopicPage topicPage)
	{
		return null;
	}
}
