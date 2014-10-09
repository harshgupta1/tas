/**
 * 
 */
package com.til.service.common.dao;

import java.util.List;

import com.til.service.common.dao.hibernate.entity.TopicPage;


/**
 * @author Harsh.Gupta
 *
 */
public interface TopicPageDao extends BaseDao<TopicPage,Integer>{
	
	public List<TopicPage> findByActiveAndWebsiteId(Boolean active, Short id);
	public List<TopicPage> findByActiveAndWebsiteId(Boolean active, Short id, String type);
	public List<TopicPage> findByActiveAndWebsiteId(Boolean active, Short wid, int firstResult, int maxResults);
	public List findAllOrdered(String firstResult, String maxResults);
	public List findAllOrdered(String topicName, String page, String pageSize);
	public TopicPage findByIdLeftJoinWebsite(Integer id);
	public List<TopicPage> findByWebsiteId(Short websiteid);
	public List findByUserId(Integer userid);
	public List findByUserId(Integer userid,String page,String pageSize, String topicName, String sortby,
			String order);
	public Long countAllOrdered(String topicName);
	public int countAllActive(short websiteid);
	public Long countByUserId(Integer userid, String topicName);
	public List findWebsiteLikesGroupByDate(Integer userId);
	public List findWebsiteDisLikesGroupByDate(Integer userId);
	public TopicPage findByWebsiteAndPageId(Short websiteid,String facebookPageId);
	public List findWebsiteLikesGroupByDate(Short id, int topicPageId, int lastdays);
	public List findWebsiteDisLikesGroupByDate(Short id, int topicPageId, int lastdays);
	public TopicPage findByUrl(String url);
	public List<TopicPage> findByWebsiteName(String websiteName);
	public TopicPage findByPageId(String pageId);
}
