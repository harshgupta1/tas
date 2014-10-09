/**
 * 
 */
package com.til.service.common.dao.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.til.service.common.dao.WebsiteDao;
import com.til.service.common.dao.hibernate.entity.Website;

/**
 * @author Harsh.Gupta
 *
 */
public class WebsiteDaoImpl extends BaseDaoImpl<Website,Short> implements WebsiteDao {
	
	private static final Logger log = LoggerFactory.getLogger(WebsiteDaoImpl.class);
	
	public List<Website> findByActive(Boolean active)
	{
		if (log.isDebugEnabled())
			log.debug("findAllActive");
		try {
			Session s = getSession();
			Query query = s.getNamedQuery("findByActive");
			query.setParameter("active", active);
			/*List<Website> websiteCol = query.list();
			for (Website website : websiteCol) {
				Query q = s.createFilter(website.getTopicPageSet(), "where active=true");
				System.out.println(q.list());
			}*/
			
			return query.list();
		} catch (RuntimeException re) {
			re.printStackTrace();
			log.error("findAllActive failed", re);
			throw re;
		}
	}

	public List<Website> findBySiteCodeAndSocialcode(Boolean active, String siteCode, String socialAppId) {

		if (log.isDebugEnabled())
			log.debug("findBySiteCodeAndSocialcode");
		try {
			Session s = getSession();
			Query query = s.getNamedQuery("findBySiteCodeAndSocialcode");
			query.setParameter("sitecode", siteCode);
			query.setParameter("socialcode", socialAppId);
			query.setParameter("active", active);
			return query.list();
		} catch (RuntimeException re) {
			re.printStackTrace();
			log.error("findBySiteCodeAndSocialcode failed", re);
			throw re;
		}
	
	}
	
	public List<Website> findBySiteCodeAndSocialcode(Boolean active, String siteCode, String socialAppId, String socialAppName) {

		if (log.isDebugEnabled())
			log.debug("findBySiteCodeAndSocialcode");
		try {
			Session s = getSession();
			Query query = s.getNamedQuery("findBySiteCodeSocialcodeAndSocialApp");
			query.setParameter("socialAppName", socialAppName);
			query.setParameter("sitecode", siteCode);
			query.setParameter("socialcode", socialAppId);
			query.setParameter("active", active);
			return query.list();
		} catch (RuntimeException re) {
			re.printStackTrace();
			log.error("findBySiteCodeAndSocialcode failed", re);
			throw re;
		}
	
	}
	
	public List<Website> findByUserId(Integer userid)
	{
		if (log.isDebugEnabled())
			log.debug("findByUserId");
		try {
			Query query = getSession().getNamedQuery("findByUserId");
			query.setParameter("userid", userid);
			return query.list();
		} catch (RuntimeException re) {
			re.printStackTrace();
			log.error("findByUserId failed", re);
			throw re;
		}
	}
	
	
	/* (non-Javadoc)
	 * @see com.til.service.common.dao.WebsiteDao#findByWebsiteId(java.lang.String)
	 */
	public List<Object[]> findByWebsiteId(Short websiteId){
		if (log.isDebugEnabled())
			log.debug("findByWebsiteId");
		try {
			Query query = getSession().getNamedQuery("findByWebSiteId");
			query.setParameter("wid", websiteId);
			return query.list();
		} catch (RuntimeException re) {
			re.printStackTrace();
			log.error("findByUserId failed", re);
			throw re;
		}
	}
	
	@Override
	protected Class<Website> getEntityClass() {
		return Website.class;
	}

	
}
