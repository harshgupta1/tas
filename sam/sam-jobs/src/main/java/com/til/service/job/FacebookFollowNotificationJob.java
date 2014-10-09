/**
 * 
 */
package com.til.service.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.til.service.common.api.Follow;
import com.til.service.common.dao.TopicPageDao;
import com.til.service.common.dao.hibernate.entity.Website;

/**
 * This Job is supposed to be used for topic pages only which as of now is not used. Comment added by Harsh
 * @author girish.gaurav
 * 
 */
public class FacebookFollowNotificationJob extends SimpleFollowNotificationJob {

	private static final Logger logger = LoggerFactory
			.getLogger(FacebookFollowNotificationJob.class);

	private TopicPageDao topicPageDao;

	/**
	 * 
	 */
	public FacebookFollowNotificationJob() {
		topicPageDao = (TopicPageDao) getApplicationContext().getBean(
				"topicPageDao");
	}

	@Override
	protected void processWebsite(Website website) {
		if (website != null && website.getActive()) {
			logger.debug("Processing data for Social App {} of website {}",
					website.getSocialAppName(), website.getName());
			// TODO Step-2: If this website doesnot has accesstoken, then get
			// that

			int count = 0;
			int total = topicPageDao.countAllActive(website.getId());
			int resultsPerThread = 10;
			count = total / resultsPerThread;
			if (total % resultsPerThread > 0) {
				++count;
			}
			for (int i = 1; i <= count; i++) {
				// Construction is allowed using this constructor only
				Follow follow = getWebsiteFollowImpl(website, i,
						resultsPerThread);
				// start threads and place in runnable state
				getThreadExecutor().execute(follow);
			}
		}
	}

}
