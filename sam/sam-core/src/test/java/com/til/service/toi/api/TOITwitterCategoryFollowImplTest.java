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
@ContextConfiguration(locations = { "classpath:root-context.xml"})
public class TOITwitterCategoryFollowImplTest {
	
	@Autowired
	WebsiteDao websiteDao;
	
	@Autowired
	ApplicationContext	context;
	
	@Test
    public void testrun() {
		short id =48;
		Website website = websiteDao.get(id);
		if (website != null && website.getActive()) {
			// Construction is allowed using this constructor only
			TOITwitterCategoryFollowImpl toiTwitterCategoryFollowImpl = (TOITwitterCategoryFollowImpl)context.getBean("toiTwitterCategoryFollowImpl",1,1,website);
			// start threads and place in runnable state
			toiTwitterCategoryFollowImpl.run();
		}
    }
}
