/**
 * 
 */
package com.til.service.common.dao;

import java.util.Date;
import java.util.List;

import com.til.service.common.dao.hibernate.entity.TopicPageArticle;


/**
 * @author Harsh.Gupta
 *
 */
public interface TopicPageArticleDao extends BaseDao<TopicPageArticle,Long>{
	
	public List findAllOrdered(String topicName, String status, String page, String pageSize);
	public Long countAllOrdered(String topicName, String status);
	public List findAllOrderedByUserId(Integer userid,String page,String pageSize, String topicName, 
			String sortby, String order);
	public Long countByUserId(Integer userid, String topicName);
	public List findAllBySocialAppAndCreateDate(String socialAppName, Date createdate);
	public List findAllBySocialAppAndCreateDate(String socialAppName, Date createdate, int page, int pageSize);
}
