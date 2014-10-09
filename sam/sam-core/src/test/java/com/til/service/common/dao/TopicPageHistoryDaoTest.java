package com.til.service.common.dao;

import java.sql.Date;
import java.util.GregorianCalendar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.til.service.common.dao.hibernate.entity.TopicPage;
import com.til.service.common.dao.hibernate.entity.TopicPageHistory;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:root-context.xml" })
public class TopicPageHistoryDaoTest {
	
	@Autowired
    private TopicPageDao topicPageDao;
	
	@Autowired
    private TopicPageHistoryDao topicPageHistoryDao;

	@Test
    public void testfindByTopicPage(){
		Date startDate = new java.sql.Date((new GregorianCalendar(2011, 9, 6)).getTime().getTime());
		TopicPage tp = topicPageDao.findById(257);
		TopicPageHistory tph = topicPageHistoryDao.findByTopicPageAndDate(tp, startDate);
		System.out.println(tph.getId());
	}
	
}
