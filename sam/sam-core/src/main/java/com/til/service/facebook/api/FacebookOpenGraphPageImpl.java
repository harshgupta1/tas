package com.til.service.facebook.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.til.service.common.dao.hibernate.entity.TopicPage;

@Service("facebookOpenGraphPage")
public class FacebookOpenGraphPageImpl extends FacebookPageImpl{
	
	private static final Logger log = LoggerFactory.getLogger(FacebookOpenGraphPageImpl.class);
	
	@Value("#{jdbcConfiguration['followJob.topicPageBatch']}")
	private Integer topicPageBatch;
	
	public void fetchPageDetails(List<TopicPage> topicPageList){
		
		Map<String,TopicPage> facebookidMap = new HashMap<String, TopicPage>();
		StringBuffer pagelinks = new StringBuffer();
		
		// Get the topicPages in BatchSize of 10
		// This is done to get the updated details of topicPages
		
		for (int i=0; i<topicPageList.size(); i++) {
			
			TopicPage page = topicPageList.get(i);
			// Step-5:  Add topic to Map and append those to pagelinks buffer
			if(page.getUrl() == null || "".equals(page.getUrl()))
			{
				facebookidMap.put(page.getPageId(), page);
				pagelinks.append(page.getPageId());
				pagelinks.append(",");
			}
			else
			{
				facebookidMap.put(page.getUrl(), page);
				pagelinks.append(page.getUrl());
				pagelinks.append(",");
			}
			if(i%topicPageBatch==0 || i == topicPageList.size()-1)
			{
				log.trace("pagelinks for row {} {}",i,pagelinks);
				// Delete the last character("comma") from the string buffer
				pagelinks.deleteCharAt(pagelinks.length()-1);
				
				// Step-6: Get the PageDetails for all the pagelinks that are (comma seperated)
				updatepagedetails(pagelinks,facebookidMap);
				// Clear Buffer
				pagelinks.delete(0, pagelinks.length());
				facebookidMap.clear();
			}
			
		}
	}
	
	public Object appendAdditionalFeedsData(TopicPage topicPage)
	{
		return null;
	}
}
