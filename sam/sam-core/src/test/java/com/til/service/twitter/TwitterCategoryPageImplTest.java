package com.til.service.twitter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.til.service.common.api.Page;
import com.til.service.common.dao.WebsiteDao;
import com.til.service.common.dao.hibernate.entity.Website;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:root-context.xml" })
public class TwitterCategoryPageImplTest {
	
	
	@Autowired
	@Qualifier("twitterCategoryPage")
	private Page twitterPage;
	
	@Autowired
	private WebsiteDao websiteDao;
	
	@Test
    public void testpostArticle() throws Exception {
		short id =48;
		Website website = websiteDao.findById(id);
		twitterPage.postArticle(website);
    }
}
