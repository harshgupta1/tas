/**
 * 
 */
package com.til.service.common.dao.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.til.service.common.dao.ArticleDao;
import com.til.service.common.dao.hibernate.entity.Article;

/**
 * @author Harsh.Gupta
 *
 */
public class ArticleDaoImpl extends BaseDaoImpl<Article,Integer> implements ArticleDao {
	
	private static final Logger log = LoggerFactory.getLogger(ArticleDaoImpl.class);
	
	@Deprecated
	public List<Article> findArticleAndTopicPageByActive(Boolean active)
	{
		log.debug("findArticleAndTopicPageByActive");
		try {
			Query query = getSession().getNamedQuery("findArticleAndTopicPageByActive");
			query.setParameter("active", active);
			return query.list();
		} catch (RuntimeException re) {
			re.printStackTrace();
			log.error("findArticleAndTopicPageByActive failed", re);
			throw re;
		}
	}
	
	@Deprecated
	public List<Article> findArticleAndTopicPageByActiveAndProcessed(Boolean active, Boolean processed)
	{
		log.debug("findArticleAndTopicPageByActiveAndProcessed");
		try {
			Query query = getSession().getNamedQuery("findArticleAndTopicPageByActiveAndProcessed");
			query.setParameter("active", active);
			query.setParameter("processed", processed);
			return query.list();
		} catch (RuntimeException re) {
			re.printStackTrace();
			log.error("findArticleAndTopicPageByActiveAndProcessed failed", re);
			throw re;
		}
	}
	
	@Deprecated
	public List<Article> findArticleAndTopicPageByWebsiteIdAndActiveAndProcessed(Short websiteid, 
							Boolean active, Boolean processed)
	{
		log.debug("findArticleAndTopicPageByWebsiteIdAndActiveAndProcessed for websiteid {} processed {}", 
					websiteid, processed);
		try {
			Query query = getSession().getNamedQuery("findArticleAndTopicPageByWebsiteIdAndActiveAndProcessed");
			query.setParameter("websiteid", websiteid);
			query.setParameter("active", active);
			query.setParameter("processed", processed);
			return query.list();
		} catch (RuntimeException re) {
			re.printStackTrace();
			log.error("findArticleAndTopicPageByWebsiteIdAndActiveAndProcessed failed", re);
			throw re;
		}
	}

	public List<Article> findArticleAndTopicPageByWebsiteId_Active_Processed_Rowlock(Short websiteid, 
			Boolean active, Boolean processed, Boolean rowlock, Date lastDate)
	{
		log.debug("findArticleAndTopicPageByWebsiteId_Active_Processed_Rowlock for websiteid {} processed {}", 
			websiteid, processed);
		try {
			Query query = getSession().getNamedQuery("findArticleAndTopicPageBy_website_active_processed_rowlock");
			query.setParameter("websiteid", websiteid);
			query.setParameter("active", active);
			query.setParameter("processed", processed);
			query.setParameter("rowlock", rowlock);
			query.setParameter("createdate", lastDate);
			return query.list();
		} catch (RuntimeException re) {
			re.printStackTrace();
			log.error("findArticleAndTopicPageByWebsiteId_Active_Processed_Rowlock failed", re);
			throw re;
		}
	}
	
	public List<Article> findArticleAndTopicPageByWebsiteId_Active_Processed_Rowlock_Scheduled(
			Short websiteid, Boolean active, Boolean processed,
			Boolean rowlock, Date lastDate, boolean scheduled) {
		log.debug(
				"findArticleAndTopicPageBy_website_active_processed_rowlock_scheduled for websiteid {} processed {}",
				websiteid, processed);
		try {
			Query query = getSession()
					.getNamedQuery(
							"findArticleAndTopicPageBy_website_active_processed_rowlock_scheduled");
			query.setParameter("websiteid", websiteid);
			query.setParameter("active", active);
			query.setParameter("processed", processed);
			query.setParameter("rowlock", rowlock);
			// query.setParameter("createdate", lastDate);
			query.setParameter("scheduled", scheduled);
			query.setParameter("feedtimestamp", new Date());
			return query.list();
		} catch (RuntimeException re) {
			re.printStackTrace();
			log.error(
					"findArticleAndTopicPageBy_website_active_processed_rowlock_scheduled failed",
					re);
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
				query = getSession().getNamedQuery("article.findAllOrderedByTopic");
				query.setParameter("topicname", topicName);
			}
			else
			{
				query = getSession().getNamedQuery("article.findAllOrdered");
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
	
	public Article findByIdLeftJoinWebsite(Integer id)
	{
		log.debug("findByIdLeftJoinWebsite id {}",id);
		try {
			Query query = getSession().getNamedQuery("article.findByIdLeftJoinWebsite");
			query.setParameter("id", id);
			List list = query.list();
			if(list.size() > 0)
				return (Article)list.get(0);
		} catch (RuntimeException re) {
			re.printStackTrace();
			log.error("findByIdLeftJoinWebsite failed", re);
			throw re;
		}
		return null;
	}
	
	public List findAllOrderedByUserId(Integer userid,String page,String pageSize, String topicName, 
			String sortby, String order)
	{
		log.debug("findAllOrderedByUserId userid {} firstresult {}",userid, page);
		try {
			
			Query query = null;
			if(topicName == null || "".equals(topicName.trim()))
			{
				query = getSession().getNamedQuery("article.findAllOrderedByUserId");
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
				query = getSession().getNamedQuery("article.findAllOrderedByUserIdAndTopic");
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
	@Deprecated
	public Long countByUserId(Integer userid, String topicName)
	{
		log.debug("countByUserId Userid={} , TopicName = {}",userid, topicName);
		try {
			Query query = null;
			if(topicName == null || "".equals(topicName.trim()))
			{
				query = getSession().getNamedQuery("article.countByUserId");
				query.setParameter("userid", userid);
			}
			else
			{
				query = getSession().getNamedQuery("article.countByUserIdAndTopic");
				query.setParameter("userid", userid);
				query.setParameter("topicname", topicName);
			}
			return (Long)query.list().get(0);
		} catch (RuntimeException re) {
			log.error("countByUserId failed", re);
			throw re;
		}
	}
	
	public List findArticleByTopicPageId(int firstResult, int maxResults, Integer topicpageid)
	{
		log.debug("findArticleByTopicPageId TopicPage {} firstResult {}",topicpageid, firstResult);
		try {
			Query query = getSession().getNamedQuery("article.findArticleByTopicPageId");
			query.setParameter("topicpageid", topicpageid);
			query.setFirstResult(firstResult);
			query.setMaxResults(maxResults);
			return query.list();
		} catch (RuntimeException re) {
			re.printStackTrace();
			log.error("findArticleByTopicPageId failed", re);
			throw re;
		}
	}
	
	@Deprecated
	public Article findArticleByTopicPageIdDateAndMessage(Integer topicpageid, Date createdate, String message)
	{
		List list = null;
		log.debug("findArticleByTopicPageIdDateAndMessage TopicPage {} message {}",topicpageid, message);
		try {
			Query query = getSession().getNamedQuery("article.findArticleByTopicPageIdDateAndMessage");
			query.setParameter("topicpageid", topicpageid);
			query.setParameter("createdate",createdate);
			query.setParameter("message",message);
			list = query.list();
			if(list.size() > 0)
			{
				return (Article)list.get(0);
			}
			else
				return null;
		} catch (RuntimeException re) {
			re.printStackTrace();
			log.error("findArticleByTopicPageIdDateAndMessage failed", re);
			throw re;
		}
	}
	
	public List<Article> findArticleByTopicPageIdDate(Integer topicpageid, Date createdate)
	{
		log.debug("findArticleByTopicPageIdDate TopicPage {} createdate {}",topicpageid, createdate);
		try {
			Query query = getSession().getNamedQuery("article.findArticleByTopicPageIdDate");
			query.setParameter("topicpageid", topicpageid);
			query.setParameter("createdate",createdate);
			return query.list();
		} catch (RuntimeException re) {
			re.printStackTrace();
			log.error("findArticleByTopicPageIdDate failed", re);
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
				query = getSession().getNamedQuery("article.countAllOrderedByTopic");
				query.setParameter("topicname", topicName);
			}
			else
			{
				query = getSession().getNamedQuery("article.countAllOrdered");
			}
			
			return (Long)query.list().get(0);
		} catch (RuntimeException re) {
			log.error("findAllOrdered failed", re);
			throw re;
		}
	}
	
	@Override
	protected Class<Article> getEntityClass() {
		return Article.class;
	}

	@Override
	public List findAllScheduled(String topicName, String page, String pageSize) {
		log.debug("findAllScheduled topicName= {}, page= {} " ,topicName, page);
		try {
			Query query = null;
			if(topicName != null && !topicName.isEmpty())
			{
				query = getSession().getNamedQuery("article.findAllScheduledOrderedByTopic");
				query.setParameter("topicname", topicName);
			}
			else
			{
				query = getSession().getNamedQuery("article.findAllScheduledOrdered");
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
			log.error("findAllScheduled failed", re);
			throw re;
		}
	}

	@Override
	public Long countAllScheduled(String topicName) {
		log.debug("countAllScheduled topicName = {} " ,topicName);
		try {
			Query query = null;
			if(topicName != null && !topicName.isEmpty())
			{
				query = getSession().getNamedQuery("article.countAllScheduledOrderedByTopic");
				query.setParameter("topicname", topicName);
			}
			else
			{
				query = getSession().getNamedQuery("article.countAllScheduledOrdered");
			}
			return (Long)query.list().get(0);
		} catch (RuntimeException re) {
			log.error("countAllScheduled failed", re);
			throw re;
		}
	}

	@Override
	public List findAllScheduledByUserId(Integer userid, String page,
			String pageSize, String topicName, String sortby, String order) {
		log.debug("findAllScheduledByUserId userid {} firstresult {}",userid, page);
		try {
			Query query = null;
			if(topicName == null || topicName.isEmpty())
			{
				query = getSession().getNamedQuery("article.findAllScheduledOrderedByUserId");
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
				query = getSession().getNamedQuery("article.findAllScheduledOrderedByUserIdAndTopic");
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
			log.error("findAllScheduledByUserId failed", re);
			throw re;
		}
	}

	@Override
	public Long countAllScheduledByUserId(Integer userid, String topicName) {
		log.debug("countAllScheduledByUserId Userid={} , TopicName = {}",userid, topicName);
		try {
			Query query = null;
			if(topicName == null || topicName.isEmpty())
			{
				query = getSession().getNamedQuery("article.countAllScheduledOrderedByUserId");
				query.setParameter("userid", userid);
			}
			else
			{
				query = getSession().getNamedQuery("article.countAllScheduledOrderedByUserIdAndTopic");
				query.setParameter("userid", userid);
				query.setParameter("topicname", topicName);
			}
			return (Long)query.list().get(0);
		} catch (RuntimeException re) {
			log.error("countAllScheduledByUserId failed", re);
			throw re;
		}
	}
	
}
