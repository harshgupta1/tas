package com.til.service.common.dao;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.til.service.common.dao.hibernate.ArticleDaoImpl;
import com.til.service.common.dao.hibernate.entity.TopicPageAdditionalFeeds;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:root-context.xml" })
public class ArticleDaoImplTest {
	
	@Autowired
    private ArticleDao articleDao;

	@Test
    public void testfindAllOrdered() throws Exception {
		
		String topicName="topicname1112"; 
		String page ="1";
		String pageSize = "30";
		
		List list = articleDao.findAllOrdered(null, page, pageSize);
		System.out.println(list.size());
    }
}
