package com.til.service.common.dao;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.til.service.common.dao.hibernate.entity.TopicPage;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:root-context.xml" })
public class TopicPageDaoTest {
	
	@Autowired
    private TopicPageDao topicPageDao;

	//@Test
    public void testfindByWebsiteId() throws Exception {
		
		List<TopicPage> list = topicPageDao.findByActiveAndWebsiteId(true,new Short("1"));
		System.out.println(list.size());
    }
	
	@Test
    public void testcountAllActive() throws Exception {
		
		short s = 6;
		List count = topicPageDao.findWebsiteDisLikesGroupByDate(6);
		System.out.println(count);
    }
}
