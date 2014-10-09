package com.til.service.common.api;

import java.util.List;
import java.util.Map;

import com.til.service.common.dao.hibernate.entity.TopicPage;
import com.til.service.common.dao.hibernate.entity.Website;

/**
 * This interface represents an operations on any Social Pages
 * 
 * @author Harsh.Gupta
 *
 */
public interface Page {
	
	public void updatepagedetails(StringBuffer pagelinks,Map<String,TopicPage> facebookidMap);
	public void postArticle(Website website);
	public void postArticle(Website website, boolean scheduled);
	public void fetchPageDetails(List<TopicPage> topicPageList);
	public Object appendAdditionalFeedsData(TopicPage topicPage);
	public void regenerateExpiredToken(Website w);
	
}
