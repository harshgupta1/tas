/**
 * 
 */
package com.til.service.common.dao;

import com.til.service.common.dao.hibernate.entity.Role;


/**
 * @author Harsh.Gupta
 *
 */
public interface RoleDao extends BaseDao<Role,Short>{
	
	public Role findByRoleName(String roleName);
}
