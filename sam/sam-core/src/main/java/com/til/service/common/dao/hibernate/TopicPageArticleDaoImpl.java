/**
 * 
 */
package com.til.service.common.dao.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.til.service.common.dao.TopicPageArticleDao;
import com.til.service.common.dao.hibernate.entity.TopicPageArticle;

/**
 * @author Harsh.Gupta
 *
 */
public class TopicPageArticleDaoImpl extends BaseDaoImpl<TopicPageArticle,Long> implements TopicPageArticleDao {
	
	private static final Logger log = LoggerFactory.getLogger(TopicPageArticleDaoImpl.class);
	
	public List findAllOrdered(String topicName, String status, String page, String pageSize)
	{
		log.debug("findAllOrdered topicName= {}, page= {} " ,topicName, page);
		try {
			Query query = null;
			if(topicName != null && !topicName.isEmpty())
			{
				if(status!=null && !status.isEmpty()) {
					query = getSession().getNamedQuery("topicpagearticle.findAllOrderedByTopicAndStatus");
					query.setParameter("status", status);
				} else {
					query = getSession().getNamedQuery("topicpagearticle.findAllOrderedByTopic");
				}
				query.setParameter("topicname", topicName);
			}
			else
			{
				query = getSession().getNamedQuery("topicpagearticle.findAllOrdered");
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
	
	public Long countAllOrdered(String topicName, String status)
	{
		log.debug("countAllOrdered topicName = {} " ,topicName);
		try {
			Query query = null;
			if(topicName != null && !"".equals(topicName.trim()))
			{
				if(status!=null && !status.isEmpty()) {
					query = getSession().getNamedQuery("topicpagearticle.countAllOrderedByTopicAndStatus");
					query.setParameter("status", status);
				} else {
					query = getSession().getNamedQuery("topicpagearticle.countAllOrderedByTopic");
				}
				query.setParameter("topicname", topicName);
			}
			else
			{
				query = getSession().getNamedQuery("topicpagearticle.countAllOrdered");
			}
			
			return (Long)query.list().get(0);
		} catch (RuntimeException re) {
			log.error("findAllOrdered failed", re);
			throw re;
		}
	}
	
	public List findAllOrderedByUserId(Integer userid,String page,String pageSize, String topicName, 
			String sortby, String order)
	{
		log.debug("findAllOrderedByUserId userid {} firstresult {}",userid, page);
		try {
			Query query = null;
			if(topicName == null || "".equals(topicName.trim()))
			{
				query = getSession().getNamedQuery("topicpagearticle.findAllOrderedByUserId");
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
				query = getSession().getNamedQuery("topicpagearticle.findAllOrderedByUserIdAndTopic");
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
			log.error("findAllOrderedByUserId failed", re);
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
				query = getSession().getNamedQuery("topicpagearticle.countByUserId");
				query.setParameter("userid", userid);
			}
			else
			{
				query = getSession().getNamedQuery("topicpagearticle.countByUserIdAndTopic");
				query.setParameter("userid", userid);
				query.setParameter("topicname", topicName);
			}
			return (Long)query.list().get(0);
		} catch (RuntimeException re) {
			log.error("countByUserId failed", re);
			throw re;
		}
	}
	
	public List findAllBySocialAppAndCreateDate(String socialAppName, Date createdate)
	{
		log.debug("findAllBySocialAppAndCreateDate socialAppName= {}, createdate= {} " ,socialAppName, createdate);
		try {
			Query query = getSession().getNamedQuery("topicpagearticle.findAllBySocialAppAndCreateDate");
			query.setParameter("name", socialAppName);
			query.setParameter("createdate", createdate);
			return query.list();
		} catch (RuntimeException re) {
			log.error("findAllBySocialAppAndCreateDate failed", re);
			throw re;
		}
	}
	
	public List findAllBySocialAppAndCreateDate(String socialAppName, Date createdate, int page, int pageSize)
	{
		log.debug("findAllBySocialAppAndCreateDate socialAppName= {}, createdate= {} " ,socialAppName, createdate);
		try {
			Query query = getSession().getNamedQuery("topicpagearticle.findAllBySocialAppAndCreateDate");
			query.setParameter("name", socialAppName);
			query.setParameter("createdate", createdate);
			query.setFirstResult((page - 1) * pageSize);
			query.setMaxResults(pageSize);
			return query.list();
		} catch (RuntimeException re) {
			log.error("findAllBySocialAppAndCreateDate failed", re);
			throw re;
		}
	}
	
	@Override
	protected Class<TopicPageArticle> getEntityClass() {
		return TopicPageArticle.class;
	}
	
}
