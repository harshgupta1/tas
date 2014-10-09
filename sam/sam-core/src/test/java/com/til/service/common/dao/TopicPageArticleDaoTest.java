package com.til.service.common.dao;

import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.til.service.common.dao.hibernate.entity.Website;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:root-context.xml" })
public class TopicPageArticleDaoTest {
	
	@Autowired
    private TopicPageArticleDao topicPageArticleDao;

	//@Test
    public void testfindByWebsiteId(){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2011);
		cal.set(Calendar.MONTH, 10);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		System.out.println(cal.getTime());
		List<Object[]> list = topicPageArticleDao.findAllBySocialAppAndCreateDate("FACEBOOK", cal.getTime());
		for (Object[] object : list) {
			System.out.println(((Website)object[0]).getName());
		}
		System.out.println(list.size());
    }
	
	@Test
    public void testfindAllOrderedByUserId()
	{
		List list = topicPageArticleDao.findAllOrderedByUserId(6,"1","30",null,null,null);
		System.out.println(list.size());
	}
}
