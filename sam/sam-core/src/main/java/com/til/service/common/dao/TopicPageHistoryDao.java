/**
 * 
 */
package com.til.service.common.dao;

import java.sql.Date;

import com.til.service.common.dao.hibernate.entity.TopicPage;
import com.til.service.common.dao.hibernate.entity.TopicPageHistory;


/**
 * @author Harsh.Gupta
 *
 */
public interface TopicPageHistoryDao extends BaseDao<TopicPageHistory,Integer>{
	
	public TopicPageHistory findByTopicPage(TopicPage topicPage);
	public TopicPageHistory findByTopicPageAndDate(TopicPage topicPage,Date  createdate);
}
