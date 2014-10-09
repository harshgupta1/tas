/**
 * 
 */
package com.til.service.common.dao;

import java.util.Date;
import java.util.List;

import com.til.service.common.dao.hibernate.entity.Article;


/**
 * @author Harsh.Gupta
 *
 */
public interface ArticleDao extends BaseDao<Article,Integer>{
	
	public List<Article> findArticleAndTopicPageByActive(Boolean active);
	public List<Article> findArticleAndTopicPageByActiveAndProcessed(Boolean active, Boolean processed);
	public List<Article> findArticleAndTopicPageByWebsiteIdAndActiveAndProcessed(Short websiteid, 
			Boolean active, Boolean processed);
	public List<Article> findArticleAndTopicPageByWebsiteId_Active_Processed_Rowlock(Short websiteid, 
			Boolean active, Boolean processed, Boolean rowlock, Date lastDate);
	public List<Article> findArticleAndTopicPageByWebsiteId_Active_Processed_Rowlock_Scheduled(Short websiteid, 
			Boolean active, Boolean processed, Boolean rowlock, Date lastDate, boolean scheduled);
	public List findAllOrdered(String topicName, String page, String pageSize);
	public Article findByIdLeftJoinWebsite(Integer id);
	public List findAllOrderedByUserId(Integer userid,String page,String pageSize, String topicName, String sortby,
			String order);
	public List findArticleByTopicPageId(int firstResult, int maxResults, Integer topicpageid);
	public Article findArticleByTopicPageIdDateAndMessage(Integer topicpageid, Date createdate, String message);
	public Long countAllOrdered(String topicName);
	@Deprecated
	public Long countByUserId(Integer userid, String topicName);
	public List<Article> findArticleByTopicPageIdDate(Integer topicpageid, Date createdate);
	
	List findAllScheduled(String topicName, String page, String pageSize);

	Long countAllScheduled(String topicName);

	List findAllScheduledByUserId(Integer userid, String page, String pageSize,
			String topicName, String sortby, String order);

	Long countAllScheduledByUserId(Integer userid, String topicName);
	
}
