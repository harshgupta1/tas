/**
 * 
 */
package com.til.service.common.dao.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.til.service.common.dao.UserDao;
import com.til.service.common.dao.hibernate.entity.User;
import com.til.service.common.vo.UserVO;

/**
 * @author Harsh.Gupta
 *
 */
public class UserDaoImpl extends BaseDaoImpl<User,Integer> implements UserDao {
	
	private static final Logger log = LoggerFactory.getLogger(UserDaoImpl.class);
	
	public List<UserVO> findAllUsers(Short websiteid)
	{
		if (log.isDebugEnabled())
			log.debug("findAllUsers");
		try {
			Query query = getSession().getNamedQuery("findAllUsersByWebsiteId");
			query.setParameter("websiteid", websiteid);
			return query.list();
		} catch (RuntimeException re) {
			re.printStackTrace();
			log.error("findAllUsers failed", re);
			throw re;
		}
	}
	
	public List<UserVO> findAllUsers()
	{
		if (log.isDebugEnabled())
			log.debug("findAllUsers");
		try {
			Query query = getSession().getNamedQuery("findAllUsers");
			return query.list();
		} catch (RuntimeException re) {
			re.printStackTrace();
			log.error("findAllUsers failed", re);
			throw re;
		}
	}
	
	@Override
	protected Class<User> getEntityClass() {
		return User.class;
	}
	
}
