/**
 * 
 */
package com.til.service.common.dao.hibernate;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.til.service.common.dao.BaseDao;

/**
 * Base class. Captures common hibernate code
 * 
 * @author Harsh.Gupta
 *
 */
public abstract class BaseDaoImpl<E, PK extends Serializable> extends HibernateDaoSupport implements BaseDao<E, PK > {
	
	private static final Logger log = LoggerFactory.getLogger(BaseDaoImpl.class);
	
	public int count() {
		
		if(log.isDebugEnabled())
			log.debug("count()");
		try {
	        List list = getHibernateTemplate().find(
	                "select count(*) from " + getEntityClass().getName() + " x");
	        Integer count = (Integer) list.get(0);
	        if(log.isDebugEnabled())
				log.debug("count successful, result size: " + count);
	        return count.intValue();
		}catch (RuntimeException re) {
			log.error("count failed", re);
			throw re;
		}
    }
	
	public long countById(String id) {
		
		if(log.isDebugEnabled())
			log.debug("countById()");
		try {
	        List list = getHibernateTemplate().find(
	                "select count(*) from " + getEntityClass().getName() + " x where id = '" + id + "'");
	        Long count = (Long) list.get(0);
	        if(log.isDebugEnabled())
				log.debug("countById successful, result size: " + count);
	        return count.longValue();
		}catch (RuntimeException re) {
			log.error("countById failed", re);
			throw re;
		}
    }
	
	/**
	 * Deletes an object from the database
	 * @param object to be deleted
	 */
	public void delete(E object) {
		if(log.isDebugEnabled())
			log.debug("deleting subject instance");
		try {
			getHibernateTemplate().delete(object);
			if(log.isDebugEnabled())
				log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}
	
	public void deleteAll(Collection<E> entities) {
		if(log.isDebugEnabled())
			log.debug("deleting collection of subject instance");
		try {
			getHibernateTemplate().deleteAll(entities);
			if(log.isDebugEnabled())
				log.debug("delete all successful");
		} catch (RuntimeException re) {
			log.error("delete all failed", re);
			throw re;
		}
	}
	
	/**
	 * Gets the object based on the id
	 * @param id of the object to be loaded
	 * @return Object that matches the id or null
	 */
	public E findById(PK id) {
		if(log.isDebugEnabled())
			log.debug("getting Subject instance with id: " + id);
		try {
			return (E) getHibernateTemplate().get(getEntityClass(), id );
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}
	
	public E findByIdbyfind(PK id)
	{
		List<E> list = null ;
		if(log.isDebugEnabled())
			log.debug("findAllbyfind " );
		try 
		{
			list = getHibernateTemplate().find("from " + getEntityClass().getName() + " where id='" + id + "'");
			if(list.size() == 1)
				return list.get(0);
		} 
		catch (RuntimeException re) 
		{
			re.printStackTrace();
			log.error("findAllbyfind failed", re);
			throw re;
		}
		return null;
	}
	
	/**
	 * Loads the object based on the id
	 * @param id of the object to be loaded
	 * @return Object that matches the id or null
	 */
	public E load(PK id) {
		if(log.isDebugEnabled())
			log.debug("loading Subject instance with id: " + id);
		try {
			return (E) getHibernateTemplate().load(getEntityClass(), id );
		} catch (RuntimeException re) {
			log.error("load failed", re);
			throw re;
		}
	}
	
	/**
	 * Loads the object based on the id
	 * @param id of the object to be loaded
	 * @return Object that matches the id or null
	 */
	public E get(PK id) {
		if(log.isDebugEnabled())
			log.debug("loading Subject instance with id: " + id);
		try {
			return (E) getHibernateTemplate().get(getEntityClass(), id );
		} catch (RuntimeException re) {
			log.error("load failed", re);
			throw re;
		}
	}
	
	/**
	 * SavesOrUpdate the object 
	 * @param subject the object to be saved
	 */
	public void  saveOrUpdate(E subject) {
		if(log.isDebugEnabled()) 
			log.debug("saving or updating subject instance");
		try {
			getHibernateTemplate().saveOrUpdate(subject);
			if(log.isDebugEnabled())
				log.debug("saveOrUpdate successful");
		} catch (RuntimeException re) {
			log.error("saveOrUpdate failed", re);
			throw re;
		}
		
	}
	
	/**
	 * SavesOrUpdate the object 
	 * @param subject the object to be saved
	 */
	public void saveOrUpdateAll(Collection<E> entities) {
		if(log.isDebugEnabled()) 
			log.debug("saveOrUpdateAll subject instance");
		try {
			getHibernateTemplate().saveOrUpdateAll(entities);
			if(log.isDebugEnabled())
				log.debug("saveOrUpdateAll successful");
		} catch (RuntimeException re) {
			log.error("saveOrUpdateAll failed", re);
			throw re;
		}
	}
	/**
	 * Update the object 
	 * @param subject the object to be saved
	 */
	public void update(E subject) {
		if(log.isDebugEnabled()) 
			log.debug("updating subject instance");
		try {
			getHibernateTemplate().update(subject);
			if(log.isDebugEnabled())
				log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
			throw re;
		}
	}
	
	/**
	 * Saves the object 
	 * @param subject the object to be saved
	 */
	public PK save(E subject) {
		if(log.isDebugEnabled()) 
			log.debug("saving subject instance");
		try {
			if(log.isDebugEnabled())
				log.debug("save successful");
			return (PK)getHibernateTemplate().save(subject);
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}
	
	/**
	 * Merges (inserts or updates) the object 
	 * @param subject the object to be saved/updated
	 */
	public E merge(E subject) {
		if(log.isDebugEnabled())
				log.debug("merging the instance");
		try {
			if(log.isDebugEnabled())
				log.debug("merge successful");
			return (E) getHibernateTemplate().merge(subject);
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}
	
	/**
	 * Finds the objects matching the subject
	 * 
	 * @param subject criteria to be used for generic fetch
	 * @return list of objects matching the criteria
	 */
	public List<E> findByExample(E subject) {
		if(log.isDebugEnabled())
			log.debug("finding subject instance by example");
		try {
			/*List results = getSession().createCriteria(clazz).add(
					Example.create(subject)).list();*/
			List<E> results = getHibernateTemplate().findByExample(subject);
			if(log.isDebugEnabled())
					log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
	
	/**
	 * Finds the n objects matching the subject
	 * 
	 * @param subject criteria to be used for generic fetch
	 * @return list of objects matching the criteria
	 */
	public List<E> findByExample(E object, int firstResult, int maxResults) {
		
		if(log.isDebugEnabled())
			log.debug("finding subject instance by example");
		try{
			List<E> resultList = getHibernateTemplate().findByExample(object, firstResult, maxResults);
			return resultList;
		}catch (RuntimeException re) {
			log.error("find by example and size failed", re);
			throw re;
		}
	}
	
	public List<E> findAll() {
		if(log.isDebugEnabled())
			log.debug("findAll instance");
		try {
			/*List results = getSession().createCriteria(clazz).list();*/
			List<E> results = getHibernateTemplate().findByCriteria(createDetachedCriteria());
			if(log.isDebugEnabled())
				log.debug("find all successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("findAll failed", re);
			throw re;
		}
	}
	
	public List<E> findAllbyfind()
	{
		List<E> list = null ;
		if(log.isDebugEnabled())
			log.debug("findAllbyfind " );
		try 
		{
			list = getHibernateTemplate().find("from " + getEntityClass().getName());
		} 
		catch (RuntimeException re) 
		{
			re.printStackTrace();
			log.error("findAllbyfind failed", re);
			throw re;
		}
		return list  ;
	}
	
	public List<E> findAll(int firstResult, int maxResults) {
		if(log.isDebugEnabled())
			log.debug("findAll instance");
		try {
			/*List results = getSession().createCriteria(clazz).list();*/
			List<E> results = getHibernateTemplate().findByCriteria(createDetachedCriteria(),firstResult, maxResults);
			if(log.isDebugEnabled())
					log.debug("find all successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("findAll by size failed", re);
			throw re;
		}
	}
	
	public <T> Collection<T> filterCollection(Collection<T> collection, String query) {
		Query filterQuery = getSession().createFilter(collection, query);
		return filterQuery.list();
	}
	
	protected abstract Class<E> getEntityClass();
	
	protected DetachedCriteria createDetachedCriteria() {
        return DetachedCriteria.forClass(getEntityClass());
	}
}
