/**
 * 
 */
package com.til.service.common.dao;

import java.util.List;

import com.til.service.common.dao.hibernate.entity.Website;


/**
 * @author Harsh.Gupta
 *
 */
public interface WebsiteDao extends BaseDao<Website,Short>{
	
	public List<Website> findByActive(Boolean active);
	public List<Website> findByUserId(Integer userid);
	public List<Website> findBySiteCodeAndSocialcode(Boolean active, String siteCode, String socailAppId);
	public List<Website> findBySiteCodeAndSocialcode(Boolean active, String siteCode, String socialAppId, String socialAppName);
	public List<Object[]> findByWebsiteId(Short websiteId);
}
