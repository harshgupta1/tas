/**
 * 
 */
package com.til.service.common.dao.hibernate;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.til.service.common.dao.RoleDao;
import com.til.service.common.dao.hibernate.entity.Role;

/**
 * @author Harsh.Gupta
 *
 */
public class RoleDaoImpl extends BaseDaoImpl<Role,Short> implements RoleDao {
	
	private static final Logger log = LoggerFactory.getLogger(RoleDaoImpl.class);
	
	public Role findByRoleName(String roleName)
	{
		if (log.isDebugEnabled())
			log.debug("findByRoleName");
		try {
			Query query = getSession().getNamedQuery("findByRoleName");
			query.setParameter("name", roleName);
			if(query.list().size() > 0)
			{
				return (Role)query.list().get(0);
			}
		} catch (RuntimeException re) {
			re.printStackTrace();
			log.error("findByRoleName failed", re);
			throw re;
		}
		return null;
	}
	
	@Override
	protected Class<Role> getEntityClass() {
		return Role.class;
	}
	
}
