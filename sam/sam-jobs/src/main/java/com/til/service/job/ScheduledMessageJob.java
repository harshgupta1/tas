/**
 * 
 */
package com.til.service.job;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.til.service.common.api.Page;
import com.til.service.common.dao.WebsiteDao;
import com.til.service.common.dao.hibernate.entity.Website;

/**
 * @author girish.gaurav
 * 
 */
public class ScheduledMessageJob implements Job {

	private static final Logger logger = LoggerFactory.getLogger(ScheduledMessageJob.class);

	private ApplicationContext applicationContext;

	private WebsiteDao websiteDao;

	private Page facebookCategoryPage;

	private Page twitterCategoryPage;
	
	private Page linkedinCategoryPage;

	/**
	 * 
	 */
	public ScheduledMessageJob() {
		try {
			applicationContext = new ClassPathXmlApplicationContext("root-context.xml", "follow-jobs-mapping.xml");
			websiteDao = (WebsiteDao) applicationContext.getBean("websiteDao");
			facebookCategoryPage = (Page) applicationContext.getBean("facebookCategoryPage");
			twitterCategoryPage = (Page) applicationContext.getBean("twitterCategoryPage");
			linkedinCategoryPage = (Page) applicationContext.getBean("linkedinCategoryPage");
			logger.info("Application context initialized successfully.");
		} catch (BeansException e) {
			logger.error("Exception while initializing Application Context.", e);
			throw new IllegalArgumentException("Exception while initializing Application Context.", e);
		}
	}

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		logger.debug("scheduleMessageJob Started.");
		try {
			// Step-1: Get active website
			List<Website> websiteList = websiteDao.findByActive(true);
			for (Website website : websiteList) {
				if ("FACEBOOK".equals(website.getSocialAppName())) {
					facebookCategoryPage.postArticle(website, true);
				} else if ("TWITTER".equals(website.getSocialAppName())) {
					twitterCategoryPage.postArticle(website, true);
				}
				  else if ("LINKEDIN".equals(website.getSocialAppName())) {
					  linkedinCategoryPage.postArticle(website, true);
				}
			}
		} catch (Exception e) {
			logger.error("Exception while executing ScheduledMessageJob,\n Error Message ", e);
		}
	}

}
