/**
 * 
 */
package com.til.service.common.dao.hibernate;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.til.service.common.dao.TopicPageHistoryDao;
import com.til.service.common.dao.hibernate.entity.TopicPage;
import com.til.service.common.dao.hibernate.entity.TopicPageHistory;

/**
 * @author Harsh.Gupta
 *
 */
public class TopicPageHistoryDaoImpl extends BaseDaoImpl<TopicPageHistory,Integer> implements TopicPageHistoryDao {
	
	private static final Logger log = LoggerFactory.getLogger(TopicPageHistoryDaoImpl.class);
	
	public TopicPageHistory findByTopicPage(TopicPage topicPage)
	{
		log.debug("findByTopicPage {}", topicPage.getId());
		try {
			Query query = getSession().getNamedQuery("findByTopicPage");
			query.setParameter("topicpageid", topicPage.getId());
			query.setFirstResult(0);
			query.setMaxResults(1);
			List list = query.list();
			if(list.size() > 0)
			{
				return (TopicPageHistory)list.get(0);
			}
		} catch (RuntimeException re) {
			re.printStackTrace();
			log.error("findByTopicPage failed", re);
			throw re;
		}
		return null;
	}
	
	public TopicPageHistory findByTopicPageAndDate(TopicPage topicPage,java.sql.Date createdate)
	{
		log.debug("findByTopicPage {}", topicPage.getId());
		try {
			Query query = getSession().getNamedQuery("findByTopicPageAndDate");
			query.setParameter("topicpageid", topicPage.getId());
			query.setParameter("createdate", createdate);
			query.setFirstResult(0);
			query.setMaxResults(1);
			List list = query.list();
			if(list.size() > 0)
			{
				return (TopicPageHistory)list.get(0);
			}
		} catch (RuntimeException re) {
			re.printStackTrace();
			log.error("findByTopicPage failed", re);
			throw re;
		}
		return null;
	}
	
	@Override
	protected Class<TopicPageHistory> getEntityClass() {
		return TopicPageHistory.class;
	}
	
	public static void main(String[] args) {
		Date endDate = new java.sql.Date((new GregorianCalendar(2011, 11, 27)).getTime().getTime());
		endDate.setDate(endDate.getDate()+3);
		System.out.println();
	}
}
