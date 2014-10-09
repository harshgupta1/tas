package com.til.service.common.dao;

import java.util.List;

import com.til.service.common.dao.hibernate.entity.Emails;

public interface EmailsDao extends BaseDao<Emails,Integer>{
	
	// Finders
	public List findAllUnprocessedEmails();
}
