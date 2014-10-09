/**
 * 
 */
package com.til.service.job;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.til.service.common.api.Follow;
import com.til.service.common.dao.WebsiteDao;
import com.til.service.common.dao.hibernate.entity.Website;

/**
 * This file is called by scheduler. All the websites here are picked on the basis of data that is passed from JobDataMap
 * 
 * @author girish.gaurav
 * @author harsh.gupta
 */
public class SimpleFollowNotificationJob implements Job,
		FollowNotificationJobConstants {

	private static final Logger logger = LoggerFactory
			.getLogger(SimpleFollowNotificationJob.class);

	private static ApplicationContext applicationContext;

	private static ExecutorService threadExecutor;

	private WebsiteDao websiteDao;

	private Map<String, String> followJobsMapping;

	static {
		try {
			applicationContext = new ClassPathXmlApplicationContext(
					"root-context.xml", "follow-jobs-mapping.xml");
			threadExecutor = Executors.newCachedThreadPool();//FixedThreadPool(100);
			logger.info("Application context initialized successfully.");
		} catch (BeansException e) {
			logger.error("Exception while initializing Application Context.", e);
			//throw new IllegalArgumentException("Exception while initializing Application Context.", e);
		}
		
	}
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public SimpleFollowNotificationJob() {
		
		// Get current size of heap in bytes 
	    long heapSize = Runtime.getRuntime().totalMemory();
	    // Get maximum size of heap in bytes. The heap cannot grow beyond this size. 
	    // Any attempt will result in an OutOfMemoryException. 
	    long heapMaxSize = Runtime.getRuntime().maxMemory();
	    
	    // Get amount of free memory within the heap in bytes. This size will increase 
	    // after garbage collection and decrease as new objects are created.
	    long heapFreeSize = Runtime.getRuntime().freeMemory();
	     
	    logger.info("heapSize= " + heapSize + " , heapMaxSize= " + heapMaxSize + " , heapFreeSize= " + heapFreeSize);
	     
		try {
//			applicationContext = new ClassPathXmlApplicationContext(
//					"root-context.xml", "follow-jobs-mapping.xml");
			websiteDao = (WebsiteDao) getApplicationContext().getBean(
					"websiteDao");
			followJobsMapping = (Map<String, String>) getApplicationContext()
					.getBean("follow-jobs-mapping");
//			threadExecutor = Executors.newCachedThreadPool();//FixedThreadPool(100);
//			logger.info("Application context initialized successfully.");
		} catch (BeansException e) {
			logger.error("Exception while initializing Application Context.", e);
			//throw new IllegalArgumentException("Exception while initializing Application Context.", e);
		}
	}

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		JobDataMap trgrDataMap = context.getTrigger().getJobDataMap();

		String siteCode = jobDataMap.getString(JOB_DATA_MAP_KEY_SITE_CODE);
		if (siteCode == null || siteCode.isEmpty()) {
			siteCode = trgrDataMap.getString(JOB_DATA_MAP_KEY_SITE_CODE);
		}

		String socialCode = jobDataMap.getString(JOB_DATA_MAP_KEY_SOCIAL_CODE);
		if (socialCode == null || socialCode.isEmpty()) {
			socialCode = trgrDataMap.getString(JOB_DATA_MAP_KEY_SOCIAL_CODE);
		}

		if (siteCode != null && !siteCode.isEmpty() && socialCode != null
				&& !socialCode.isEmpty()) {
			logger.debug("Found SITE_CODE={} and SOCIAL_CODE={}.", siteCode,
					socialCode);
			processWebsiteBySiteCodeAndSocialCode(siteCode, socialCode);
		} else {
			logger.warn("SITE_CODE or SOCIAL_CODE found empty, going to process websiteId.");
			Short websiteId = null;
			try {
				websiteId = Short.parseShort(jobDataMap
						.getString(JOB_DATA_MAP_KEY_WEBSITE_ID));
			} catch (NumberFormatException e1) {
				// do nothing.
			}
			if (websiteId == null || websiteId <= 0) {
				try {
					websiteId = Short.parseShort(trgrDataMap
							.getString(JOB_DATA_MAP_KEY_WEBSITE_ID));
				} catch (NumberFormatException e1) {
					// do nothing.
				}
			}

			if (websiteId != null && websiteId > 0) {
				logger.debug("Found websiteId {}", websiteId);
				processWebsiteById(websiteId);
			} else {
				logger.error("Found siteCode={}, socialCode={}, and websiteId="
						+ websiteId
						+ ", one or more required field(s) missing.", siteCode,
						socialCode);
			}
		}
//		try {
//			threadExecutor.awaitTermination(30, TimeUnit.MINUTES);
//		} catch (InterruptedException e) {
//			logger.error("InterruptedException while running FollowNotification Job.");
//		}
//		threadExecutor.shutdown();
	}

	/**
	 * process all the websites with given <code>siteCode</code> and
	 * <code>socialCode</code>
	 * 
	 * @param siteCode
	 *            Site Code of the website to process
	 * @param socialCode
	 *            Social Code of the website to process
	 */
	protected void processWebsiteBySiteCodeAndSocialCode(String siteCode,
			String socialCode) {
		try {
			logger.debug(
					"Fetching websites with siteCode {} and socialCode {}",
					siteCode, socialCode);
			List<Website> websiteList = websiteDao.findBySiteCodeAndSocialcode(
					true, siteCode, socialCode);
			for (Website website : websiteList) {
				processWebsite(website);
			}
		} catch (Exception e) {
			logger.error("Exception for siteCode " + siteCode + " socialCode " + socialCode + " ,\n Error Message ",e);
		}
	}

	/**
	 * process website with given <code>websiteId</code>
	 * 
	 * @param websiteId
	 *            id of the website to process
	 */
	protected void processWebsiteById(Short websiteId) {
		try {
			logger.debug("Fetching website with websiteId {}", websiteId);
			Website website = websiteDao.findById(websiteId);
			processWebsite(website);
		} catch (Exception e) {
			logger.error("Exception while processing websiteId " + websiteId + ",\n Error Message ", e);
		}
	}

	/**
	 * @param website
	 */
	protected void processWebsite(Website website) {
		if (website != null && website.getActive()) {
			logger.debug("Processing data for Social App {} of website {}",
					website.getSocialAppName(), website.getName());

			Follow follow = getWebsiteFollowImpl(website);

			// start threads and place in runnable state
			if (follow != null) {
				// start multiple tasks
				threadExecutor.execute(follow);
			}
		}
	}

	protected Follow getWebsiteFollowImpl(Website website) {
		return getWebsiteFollowImpl(website, 1, 1);
	}

	protected Follow getWebsiteFollowImpl(Website website, int startPage,
			int resultsPerThread) {
		// Construction is allowed using this constructor only
		Follow follow = null;
		if (website != null && website.getActive()) {
			logger.debug("Processing data for Social App {} of website {}",
					website.getSocialAppName(), website.getName());
			String beanName = getFollowJobsMappingBeanName(website
					.getSocialAppName().toLowerCase(), website.getSitecode(),
					website.getSocialcode());
			logger.info("fetching beanName : {}", beanName);
			follow = (Follow) getApplicationContext().getBean(beanName,
					startPage, resultsPerThread, website);
		}
		return follow;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public ExecutorService getThreadExecutor() {
		return threadExecutor;
	}

	public WebsiteDao getWebsiteDao() {
		return websiteDao;
	}

	public String getFollowJobsMappingBeanName(String socialAppName,
			String siteCode, String socialCode) {
		StringBuilder key = new StringBuilder("job.");
		key.append(socialAppName).append(".");
		key.append(siteCode).append(".");
		key.append(socialCode).append(".beanName");
		logger.debug("fetching FollowJobMappingBean Name for : " + key);
		return followJobsMapping.get(key.toString());
	}

}
