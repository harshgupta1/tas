/**
 * 
 */
package com.til.service.common.dao.hibernate;

import static org.hibernate.type.StandardBasicTypes.INTEGER;
import static org.hibernate.type.StandardBasicTypes.TIMESTAMP;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.til.service.common.dao.TopicPageDao;
import com.til.service.common.dao.hibernate.entity.TopicPage;

/**
 * @author Harsh.Gupta
 *
 */
public class TopicPageDaoImpl extends BaseDaoImpl<TopicPage,Integer> implements TopicPageDao {
	
	private static final Logger log = LoggerFactory.getLogger(TopicPageDaoImpl.class);
	
	public List<TopicPage> findByActiveAndWebsiteId(Boolean active, Short id)
	{
		if (log.isDebugEnabled())
			log.debug("findByActiveAndWebsiteId for website {}",id);
		try {
			Query query = getSession().getNamedQuery("topicPage.findByWebsiteIdAndActiveTopic");
			query.setParameter("id", id);
			query.setParameter("active", active);
			return query.list();
		} catch (RuntimeException re) {
			log.error("findByActiveAndWebsiteId failed", re);
			throw re;
		}
	}
	
	public List<TopicPage> findByActiveAndWebsiteId(Boolean active, Short id, String type)
	{
		if (log.isDebugEnabled())
			log.debug("findByActiveAndWebsiteId for website {}",id);
		try {
			Query query = getSession().getNamedQuery("topicPage.findByWebsiteIdTypeAndActiveTopic");
			query.setParameter("id", id);
			query.setParameter("active", active);
			query.setParameter("type", type);
			return query.list();
		} catch (RuntimeException re) {
			log.error("findByActiveAndWebsiteId failed", re);
			throw re;
		}
	}
	
	public List<TopicPage> findByActiveAndWebsiteId(Boolean active, Short wid, int firstResult, int maxResults)
	{
		if (log.isDebugEnabled())
			log.debug("findByActiveAndWebsiteId for website {} starting from page {}", wid, firstResult);
		try {
			Query query = getSession().getNamedQuery("topicPage.findByWebsiteIdAndActiveTopic");
			query.setParameter("id", wid);
			query.setParameter("active", active);
			query.setFirstResult((firstResult - 1) * maxResults);
			query.setMaxResults(maxResults);
			return query.list();
		} catch (RuntimeException re) {
			log.error("findByActiveAndWebsiteId failed", re);
			throw re;
		}
	}
	
	public List findAllOrdered(String page, String pageSize)
	{
		log.debug("findAllOrdered");
		try {
			Query query = getSession().getNamedQuery("topicPage.findAllOrdered");
			
			if(null == page)
			{
				page = "1";
			}
			if(page != null && !"".equals(page) && pageSize != null && !"".equals(pageSize))
			{
				int pSize = Integer.parseInt(pageSize); 
				query.setFirstResult((Integer.parseInt(page) - 1) * pSize);
				query.setMaxResults(pSize);
			}
			return query.list();
		} catch (RuntimeException re) {
			log.error("findAllOrdered failed", re);
			throw re;
		}
	}
	
	public List findAllOrdered(String topicName, String page, String pageSize)
	{
		log.debug("findAllOrdered topicName= {}, page= {} " ,topicName, page);
		try {
			Query query = null;
			if(topicName != null && !"".equals(topicName.trim()))
			{
				query = getSession().getNamedQuery("topicPage.findAllOrderedByTopic");
				query.setParameter("topicname", topicName);
			}
			else
			{
				query = getSession().getNamedQuery("topicPage.findAllOrdered");
			}
			if(null == page)
			{
				page = "1";
			}
			if(page != null && !"".equals(page) && pageSize != null && !"".equals(pageSize))
			{
				int pSize = Integer.parseInt(pageSize); 
				query.setFirstResult((Integer.parseInt(page) - 1) * pSize);
				query.setMaxResults(pSize);
			}
			return query.list();
		} catch (RuntimeException re) {
			log.error("findAllOrdered failed", re);
			throw re;
		}
	}
	
	public TopicPage findByIdLeftJoinWebsite(Integer id)
	{
		log.debug("findByIdLeftJoinWebsite");
		try {
			Query query = getSession().getNamedQuery("topicPage.findByIdLeftJoinWebsite");
			query.setParameter("id", id);
			List list = query.list();
			if(list.size() > 0)
				return (TopicPage)list.get(0);
		} catch (RuntimeException re) {
			log.error("findByIdLeftJoinWebsite failed", re);
			throw re;
		}
		return null;
	}
	
	public List<TopicPage> findByWebsiteId(Short websiteid)
	{
		log.debug("findByWebsiteId");
		try {
			Query query = getSession().getNamedQuery("topicPage.findByWebsiteId");
			query.setParameter("id", websiteid);
			return query.list();
		} catch (RuntimeException re) {
			log.error("findByWebsiteId failed", re);
			throw re;
		}
	}
	
	public List findByUserId(Integer userid)
	{
		log.debug("findByUserId");
		try {
			Query query = getSession().getNamedQuery("topicPage.findByUserId");
			query.setParameter("userid", userid);
			return query.list();
		} catch (RuntimeException re) {
			log.error("findByUserId failed", re);
			throw re;
		}
	}
	
	public List findByUserId(Integer userid,String page,String pageSize, String topicName, String sortby,
							String order)
	{
		log.debug("findByUserId Userid={} , TopicName = {}",userid, topicName);
		try {
			
			Query query = null;
			if(topicName == null || "".equals(topicName.trim()))
			{
				query = getSession().getNamedQuery("topicPage.findByUserId");
				query.setParameter("userid", userid);
			}
			else if(sortby != null && order != null)
			{
				/*if("asc".equalsIgnoreCase(order))
				{
					query = getSession().getNamedQuery("topicPage.findByUserIdAndsortbyAsc");
				}
				else
				{
					query = getSession().getNamedQuery("topicPage.findByUserIdAndsortbyDesc");
				}
				query.setParameter("userid", userid);
				query.setParameter("sortby", sortby);*/
			}
			else
			{
				query = getSession().getNamedQuery("topicPage.findByUserIdAndTopic");
				query.setParameter("userid", userid);
				query.setParameter("topicname", topicName);
			}
			if(null == page)
			{
				page = "1";
			}
			if(page != null && !"".equals(page) && pageSize != null && !"".equals(pageSize))
			{
				int pSize = Integer.parseInt(pageSize); 
				query.setFirstResult((Integer.parseInt(page) - 1) * pSize);
				query.setMaxResults(pSize);
			}
			
			return query.list();
		} catch (RuntimeException re) {
			log.error("findByUserId failed", re);
			throw re;
		}
		
	}
	
	public Long countAllOrdered(String topicName)
	{
		log.debug("countAllOrdered topicName= {} " ,topicName);
		try {
			Query query = null;
			if(topicName != null && !"".equals(topicName.trim()))
			{
				query = getSession().getNamedQuery("topicPage.countAllOrderedByTopic");
				query.setParameter("topicname", topicName);
			}
			else
			{
				query = getSession().getNamedQuery("topicPage.countAllOrdered");
			}
			
			return (Long)query.list().get(0);
		} catch (RuntimeException re) {
			log.error("findAllOrdered failed", re);
			throw re;
		}
	}
	
	public int countAllActive(short websiteid)
	{
		log.debug("countAllActive topicpages for website {}", websiteid);
		try {
			// This criteria query puts a join between website and topicpage
			// table to count rows
			Criteria crit = getSession().createCriteria(getEntityClass().getName());
			crit.createAlias("website","w");
			crit.add(Restrictions.eq("w.id",  websiteid));
			crit.add(Restrictions.eq("active",  true));
			ProjectionList projList = Projections.projectionList();
			projList.add(Projections.countDistinct("id"));
			crit.setProjection(projList); 
			crit.setComment("countAllActiveTopicPages");
			List list = crit.list(); 
			Long count = (Long) list.get(0);
			log.debug("countAllActive topicpages successful, result size: {}", count);
			return count.intValue();
		} catch (RuntimeException re) {
			log.error("countAllActive topicpages failed", re);
			throw re;
		}
	}
	
	public Long countByUserId(Integer userid, String topicName)
	{
		log.debug("countByUserId Userid={} , TopicName = {}",userid, topicName);
		try {
			Query query = null;
			if(topicName == null || "".equals(topicName.trim()))
			{
				query = getSession().getNamedQuery("topicPage.countByUserId");
				query.setParameter("userid", userid);
			}
			else
			{
				query = getSession().getNamedQuery("topicPage.countByUserIdAndTopic");
				query.setParameter("userid", userid);
				query.setParameter("topicname", topicName);
			}
			
			return (Long)query.list().get(0);
		} catch (RuntimeException re) {
			log.error("countByUserId failed", re);
			throw re;
		}
	}
	
	public List findWebsiteLikesGroupByDate(Integer userid)
	{
		log.debug("findWebsiteLikesGroupByDate for website {} for",userid);
		
		/*
		 * Group By TopicPage first on same date and find max(likes) on individual topic
		 * Then group by date and sum to find sum across dates of all topics
		 */
		/*String query = "SELECT th.likes likes ,MAX(th.createdate) createdate ,w.socialcode socialcode " +
				"FROM website w INNER JOIN user_permission u INNER JOIN topicpage  t INNER JOIN topicpage_history th " +
				"ON w.id = u.websiteid AND w.id = t.websiteid AND  t.id = th.topicpageid WHERE u.userid=:userid  " +
				"GROUP BY DATE(th.createdate),w.socialcode ORDER BY DATE(th.createdate);";
		*/
		String query ="SELECT SUM(innerq.likes), innerq.createdate createdate,innerq.socialappname socialcode " +
				"FROM (SELECT th.likes likes, MAX(th.createdate) createdate,w.socialappname FROM website w INNER JOIN user_permission u INNER JOIN topicpage  t INNER JOIN topicpage_history th ON w.id = u.websiteid AND w.id = t.websiteid AND  t.id = th.topicpageid WHERE u.userid=:userid "+// AND DATE(th.createdate) ='2012-01-05'" +
				" GROUP BY th.topicpageid,DATE(th.createdate)) as innerq GROUP BY DATE(innerq.createdate),socialcode ORDER BY createdate ASC";
		
		
		try
		{
			SQLQuery sqlQuery = getSession().createSQLQuery(query);
			sqlQuery.setParameter("userid", userid);
			
			sqlQuery.addScalar("SUM(innerq.likes)", INTEGER); // 0
			sqlQuery.addScalar("createdate", TIMESTAMP);
			sqlQuery.addScalar("socialcode", org.hibernate.type.StandardBasicTypes.STRING);
			
			return sqlQuery.list();
		} catch (RuntimeException re) {
			log.error("findWebsiteLikesGroupByDate failed", re);
			throw re;
		}
	}
	
	public List findWebsiteDisLikesGroupByDate(Integer userid)
	{
		log.debug("findWebsiteLikesGroupByDate for website {} for ",userid);
		
		/*
		 * Group By TopicPage first on same date and find max(likes) on individual topic
		 * Then group by date and sum to find sum across dates of all topics
		 */
		String query = "SELECT SUM(innerq.dislikes) , innerq.createdate createdate,innerq.socialappname socialcode " +
				"FROM (SELECT sum(th.dislikes) dislikes, th.createdate createdate,w.socialappname FROM website w INNER JOIN user_permission u INNER JOIN topicpage  t INNER JOIN topicpage_history th ON w.id = u.websiteid AND w.id = t.websiteid AND  t.id = th.topicpageid WHERE u.userid=:userid "+// AND DATE(th.createdate) ='2012-01-05'" +
				" GROUP BY th.topicpageid,DATE(th.createdate)) as innerq GROUP BY DATE(innerq.createdate),socialcode ORDER BY createdate ASC";
		try
		{
			SQLQuery sqlQuery = getSession().createSQLQuery(query);
			sqlQuery.setParameter("userid", userid);
			
			sqlQuery.addScalar("SUM(innerq.dislikes)", INTEGER); // 0
			sqlQuery.addScalar("createdate",TIMESTAMP );
			sqlQuery.addScalar("socialcode", org.hibernate.type.StandardBasicTypes.STRING);
			return sqlQuery.list();
		} catch (RuntimeException re) {
			log.error("findWebsiteLikesGroupByDate failed", re);
			throw re;
		}
	
	}
	
	public TopicPage findByWebsiteAndPageId(Short websiteid,String pageId)
	{
		log.debug("findByWebsiteAndPageId: websiteid {} , pageId {}",websiteid, pageId);
		try {
			Query query = getSession().getNamedQuery("topicPage.findByWebsiteAndPageId");
			query.setParameter("id", websiteid);
			query.setParameter("pageId", pageId);
			List list = query.list();
			if(list.size() > 0)
			{
				return (TopicPage)list.get(0);
			}
			else
			{
				return null;
			}
			
		} catch (RuntimeException re) {
			log.error("findByWebsiteAndPageId failed", re);
			throw re;
		}
	}
	
	@Override
	public List findWebsiteLikesGroupByDate(Short websiteid, int topicPageId, int lastdays) {

		log.debug("findWebsiteLikesGroupByDate for website {} for last {} days",websiteid, lastdays);
		
		/*
		 * Group By TopicPage first on same date and find max(likes) on individual topic
		 * Then group by date and sum to find sum across dates of all topics
		 */
		String query = "SELECT likes, max(createdate) createdate FROM topicpage_history WHERE topicpageid= :topicpageid GROUP BY  DATE(createdate)  ORDER BY createdate ASC;";
		
		try
		{
			SQLQuery sqlQuery = getSession().createSQLQuery(query);
			sqlQuery.setParameter("topicpageid", topicPageId);
			sqlQuery.addScalar("likes", INTEGER); // 0
			sqlQuery.addScalar("createdate", TIMESTAMP);
			return sqlQuery.list();
		} catch (RuntimeException re) {
			log.error("findWebsiteLikesGroupByDate failed", re);
			throw re;
		}
	
	}

	@Override
	public List findWebsiteDisLikesGroupByDate(Short websiteid, int topicPageId,int lastdays) {

		log.debug("findWebsiteDisLikesGroupByDate for website {} for last {} days",websiteid, lastdays);
		
		/*
		 * Group By TopicPage first on same date and find max(likes) on individual topic
		 * Then group by date and sum to find sum across dates of all topics
		 */
		String query = "SELECT sum(tph.dislikes) , tph.createdate FROM topicpage_history tph WHERE tph.topicpageid= :topicpageid GROUP BY  DATE(tph.createdate)  ORDER BY tph.createdate ASC;";
		
		try
		{
			SQLQuery sqlQuery = getSession().createSQLQuery(query);
			sqlQuery.setParameter("topicpageid", topicPageId);
			
			
			sqlQuery.addScalar("sum(tph.dislikes)", INTEGER); // 0
			sqlQuery.addScalar("tph.createdate", TIMESTAMP);
			return sqlQuery.list();
		} catch (RuntimeException re) {
			log.error("findWebsiteDisLikesGroupByDate failed", re);
			throw re;
		}
	
	}
	
	public TopicPage findByUrl(String url)
	{
		log.debug("findByUrl: url {}",url);
		try {
			Query query = getSession().getNamedQuery("topicPage.findByUrl");
			query.setParameter("url", url);
			List list = query.list();
			if(list.size() > 0)
			{
				return (TopicPage)list.get(0);
			}
			else
			{
				return null;
			}
			
		} catch (RuntimeException re) {
			log.error("findByUrl failed", re);
			throw re;
		}
	}
	
	public List<TopicPage> findByWebsiteName(String websiteName)
	{
		log.debug("findByWebsiteName");
		try {
			Query query = getSession().getNamedQuery("topicPage.findByWebsiteName");
			query.setParameter("name", websiteName);
			return query.list();
		} catch (RuntimeException re) {
			log.error("findByUserId failed", re);
			throw re;
		}
	}
	
	public TopicPage findByPageId(String pageId)
	{
		log.debug("findByPageId: pageid {} , pageId {}", pageId);
		try {
			Query query = getSession().getNamedQuery("topicPage.findByPageId");
			query.setParameter("pageId", pageId);
			List list = query.list();
			if(list.size() > 0)
			{
				return (TopicPage)list.get(0);
			}
			else
			{
				return null;
			}
			
		} catch (RuntimeException re) {
			log.error("findByPageId failed", re);
			throw re;
		}
	}
	
	
	@Override
	protected Class<TopicPage> getEntityClass() {
		return TopicPage.class;
	}
}
