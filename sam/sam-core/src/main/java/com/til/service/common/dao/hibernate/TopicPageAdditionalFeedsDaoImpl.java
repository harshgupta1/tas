/**
 * 
 */
package com.til.service.common.dao.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.til.service.common.dao.TopicPageAdditionalFeedsDao;
import com.til.service.common.dao.hibernate.entity.TopicPageAdditionalFeeds;

/**
 * @author Harsh.Gupta
 *
 */
public class TopicPageAdditionalFeedsDaoImpl extends BaseDaoImpl<TopicPageAdditionalFeeds,Integer> implements TopicPageAdditionalFeedsDao {
	
	private static final Logger log = LoggerFactory.getLogger(TopicPageAdditionalFeedsDaoImpl.class);
	
	public List<TopicPageAdditionalFeeds> findByTopicPageId(int topicPageId)
	{
		if (log.isDebugEnabled())
			log.debug("findByTopicPageId for topicPage {}",topicPageId);
		try {
			Query query = getSession().getNamedQuery("topicPageAdditionalFeeds.findByTopicPageId");
			query.setParameter("topicpageid", topicPageId);
			return query.list();
		} catch (RuntimeException re) {
			log.error("findByTopicPageId failed", re);
			throw re;
		}
	}
	
	@Override
	protected Class<TopicPageAdditionalFeeds> getEntityClass() {
		return TopicPageAdditionalFeeds.class;
	}
}
