package com.til.service.toi.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.til.service.common.dao.WebsiteDao;
import com.til.service.common.dao.hibernate.entity.Website;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:root-context.xml" })
public class TOIFacebookCategoryFollowImplTest {
	
	TOIFacebookCategoryFollowImpl toiFacebookCategoryFollowImpl;
	
	@Autowired
	ApplicationContext	context;
	
	@Autowired
	WebsiteDao websiteDao;
	@Test
    public void testrun() {
		Short websiteid = 67;
		Website website = websiteDao.get(websiteid);
		toiFacebookCategoryFollowImpl = (TOIFacebookCategoryFollowImpl)context.getBean("toiFacebookCategoryFollowImpl",1,30,website);
		toiFacebookCategoryFollowImpl.run();
    }
}
