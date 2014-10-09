package com.til.service.common.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author Harsh.Gupta
 *
 */
public interface BaseDao<E, PK extends Serializable> {
	
	public int count();
	
	public long countById(String id);
	
	public PK save(E persistentInstance);
	
	public void saveOrUpdate(E persistentInstance);
	
	public void saveOrUpdateAll(Collection<E> entities);
	
	public void update(E persistentInstance);
	
	public void delete(E persistentInstance) ;
	
	public void deleteAll(Collection<E> entities);
	
	public E merge(E persistentInstance);

	public E findById(PK id) ;
	public E findByIdbyfind(PK id);
	public E load(PK id);
	public E get(PK id);
	
	public List<E> findByExample(E persistentInstance);
	
	public List<E> findByExample(E object, int firstResult, int maxResults);
	
	public List<E> findAll();
	public List<E> findAllbyfind();
	public List<E> findAll(int firstResult, int maxResults);
	
	public <T> Collection<T> filterCollection(Collection<T> collection, String query);
}
