package com.til.service.common.dao;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.til.service.common.dao.hibernate.entity.Website;
import com.til.service.common.vo.UserVO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:root-context.xml" })
public class UserDaoTest {
	
	@Autowired
    private UserDao userDao;

	@Test
    public void testfindByActive() throws Exception {
		
		/*List<UserVO> userVOList = userDao.findAllUsers(1);
		System.out.println(userVOList.size());*/
    }

}
