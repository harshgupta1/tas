/**
 * 
 */
package com.til.service.common.dao;

import java.util.List;

import com.til.service.common.dao.hibernate.entity.User;
import com.til.service.common.vo.UserVO;


/**
 * @author Harsh.Gupta
 *
 */
public interface UserDao extends BaseDao<User,Integer>{
	
	public List<UserVO> findAllUsers(Short websiteid);
	public List<UserVO> findAllUsers();
}
