package com.til.service.common.dao;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.til.service.common.dao.hibernate.entity.TopicPageAdditionalFeeds;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:root-context.xml" })
public class TopicPageAdditionaFeedsDaoTest {
	
	@Autowired
    private TopicPageAdditionalFeedsDao topicPageAdditionalFeedsDao;

	@Test
    public void testfindByWebsiteId() throws Exception {
		
    	int topicpageid = 395;
		List<TopicPageAdditionalFeeds> list = topicPageAdditionalFeedsDao.findByTopicPageId(topicpageid);
		System.out.println(list.size());
    }
}
