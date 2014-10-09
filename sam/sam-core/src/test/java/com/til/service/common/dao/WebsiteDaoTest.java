package com.til.service.common.dao;

import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.til.service.common.dao.hibernate.entity.TopicPage;
import com.til.service.common.dao.hibernate.entity.Website;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:root-context.xml" })
public class WebsiteDaoTest {
	
	@Autowired
    private WebsiteDao websiteDao;

	//@Test
    public void testfindByActive() throws Exception {
		
		List<Website> websiteList = websiteDao.findByActive(true);
		System.out.println(websiteList.size());
		for (Website website : websiteList) {
			/*Set<TopicPage> topicPageSet = website.getTopicPageSet();
			for (TopicPage topicPage : topicPageSet) {
				System.out.println(topicPage.getEntityName());
			}*/
			websiteDao.delete(website);
		}
    }
	
	@Test
    public void testfindAllUsers() throws Exception {
		
		/*List<UserVO> userVOList = websiteDao.findAllUsers(1);
		System.out.println(userVOList.size());*/
    }
	
	@Test
    public void testSaveorUpdate() throws Exception {
		
		Website website = websiteDao.findById(Short.parseShort("2"));
		website.setAccessToken(null);
		websiteDao.saveOrUpdate(website);
    }
}
