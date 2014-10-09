/*
 * Copyright 2006-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.til.service.facebook.api;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.til.service.common.api.Page;
import com.til.service.common.dao.TopicPageDao;
import com.til.service.common.dao.WebsiteDao;
import com.til.service.common.dao.hibernate.entity.TopicPage;
import com.til.service.common.dao.hibernate.entity.Website;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:root-context.xml" })
public class FacebookPageImplTest {
	
	String accesstoken;
	
	@Before
    public void setUp() {
		//testGetapplicationaccesstoken();
    }
	
	@Autowired
	ApplicationContext	context;
	
	@Autowired
	WebsiteDao websiteDao;
	
	@Autowired
	private TopicPageDao topicPageDao;
	
	@Autowired
	@Qualifier("facebookOpenGraphPage")
	private Page facebookPage;
	
	//@Test
    public void testpostArticle() {
		Short websiteid = 16;
		Website website = websiteDao.get(websiteid);
		FacebookPageImpl pageImpl = new FacebookCategoryPageImpl();
		pageImpl.postArticle(website);			
    }

	//@Test
    public void testpagedetails() {
		
		/*Calendar currentDate = Calendar.getInstance();
		currentDate.set(Calendar.HOUR, -12);
		currentDate.set(Calendar.MINUTE,0);
		currentDate.set(Calendar.SECOND,0);
		currentDate.set(Calendar.MILLISECOND,0);
		
		Calendar last24Hours = Calendar.getInstance();
		last24Hours.add(Calendar.HOUR, -24);
		*/
		// Step-4: Get all the ACTIVE topicpages for this website
		//List<TopicPage> topicPageList = topicPageDao.findByActiveAndWebsiteId(true, Short.valueOf("6"),0,100);
		
		List<TopicPage> topicPageList = new ArrayList<TopicPage>();
		TopicPage topicPage = topicPageDao.findById(1092);		
		TopicPage topicPage1 = topicPageDao.findById(1093);		
		TopicPage topicPage2 = topicPageDao.findById(1094);		
		TopicPage topicPage3 = topicPageDao.findById(1095);		
		TopicPage topicPage4 = topicPageDao.findById(1096);
		
		
		topicPageList.add(topicPage);
		topicPageList.add(topicPage1);
		topicPageList.add(topicPage2);
		topicPageList.add(topicPage3);
		topicPageList.add(topicPage4);		
		
		//facebookPage.fetchPageDetails(topicPageList);
    }
	
    @Test
    public void testregenerateExpiredToken()
    {
    	List<Website> websiteList = websiteDao.findBySiteCodeAndSocialcode(true, "toi", "journalist");
    	for (Website website : websiteList) {
    		if("FACEBOOk".equalsIgnoreCase(website.getSocialAppName()))
    		{
    			facebookPage.regenerateExpiredToken(website);
    		}
		}
    	
    }
}
