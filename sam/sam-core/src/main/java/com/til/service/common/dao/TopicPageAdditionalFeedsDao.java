/**
 * 
 */
package com.til.service.common.dao;

import java.util.List;

import com.til.service.common.dao.hibernate.entity.TopicPageAdditionalFeeds;


/**
 * @author Harsh.Gupta
 *
 */

public interface TopicPageAdditionalFeedsDao extends BaseDao<TopicPageAdditionalFeeds,Integer>{
	
	public List<TopicPageAdditionalFeeds> findByTopicPageId(int topicPageId);
}
