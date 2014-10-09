package com.til.service.job;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.perf4j.aop.Profiled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.til.service.common.api.Follow;
import com.til.service.common.api.Page;
import com.til.service.common.dao.TopicPageDao;
import com.til.service.common.dao.WebsiteDao;
import com.til.service.common.dao.hibernate.entity.Website;
import com.til.service.toi.api.FacebookOLYFeedPush;
import com.til.service.toi.api.TOIBlogsFacebookCategoryFollowImpl;
import com.til.service.toi.api.TOIBlogsTwitterCategoryFollowImpl;
import com.til.service.toi.api.TOIFacebookCategoryFollowImpl;
import com.til.service.toi.api.TOIFacebookTopicFollowImpl;
import com.til.service.toi.api.TOITwitterBreakingNewsFollowImpl;
import com.til.service.toi.api.TOITwitterCategoryFollowImpl;
import com.til.service.toi.api.TwitterOLYFeedPush;

/**
 * @author Harsh.Gupta
 * 
 */
@Service
public class FollowNotificationJob {

	private static final Logger log = LoggerFactory.getLogger(FollowNotificationJob.class);

	@Autowired
	private WebsiteDao websiteDao;

	@Autowired
	private TopicPageDao topicPageDao;

	@Value("${toitopic.enabled}")
	private String toitopic_enabled;
	@Value("${toitopic.websiteid}")
	private String toitopic_websiteid;

	@Autowired
	private ApplicationContext	context; 
	
	ExecutorService threadExecutor = Executors.newFixedThreadPool(100);
	//threadExecutor.shutdown(); // shutdown worker threads

	@Profiled(tag = "followNotificationJob")
	@Scheduled(cron = "${toitopic.schedule}")
	public void runToiTopic() {

		if ("false".equalsIgnoreCase(toitopic_enabled)) {
			return;
		}

		try {
			// Step-1: Get the active website
			Website website = websiteDao.findById(Short.parseShort(toitopic_websiteid));
			if (website != null && website.getActive()) {
				// TODO Step-2: If this website doesnot has accesstoken, then
				// get that
				log.debug("Processing data for Social App {} of website {}",
						website.getSocialAppName(), website.getName());

				if ("FACEBOOK".equals(website.getSocialAppName())) {
					int count = 0;
					int total = topicPageDao.countAllActive(website.getId());
					int resultsPerThread = 30;
					count = total / resultsPerThread;
					if (total % resultsPerThread > 0) {
						++count;
					}
					for (int i = 1; i <= count; i++) {
						// Construction is allowed using this constructor only
						Follow follow = (TOIFacebookTopicFollowImpl)context.getBean
										("toiFacebookTopicFollowImpl",i,resultsPerThread,website);
						// start threads and place in runnable state
						threadExecutor.execute(follow); // start multiple tasks
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception while executing runToiTopic,\n Error Message ",e);
		}

	}

	@Value("${toil1.enabled}")
	private String toil1_enabled;
	@Value("${toil1.websiteid}")
	private String toil1_websiteid;
	
	@Value("${toiblogsl1.enabled}")
	private String toiblogsl1_enabled;
	@Value("${toiblogsl1.websiteid}")
	private String toiblogsl1_websiteid;
	
	// Schedule 1 for TOI L1 Pages
	@Scheduled(cron = "${toil1.schedule1}")
	public void toiL1Schedule1() {
		log.info("Running toiL1Schedule1");
		runToiL1Schedule();
		runToiBlogsL1Schedule();
	}

	// Schedule 2 for TOI L1 Pages
	@Scheduled(cron = "${toil1.schedule2}")
	public void toiL1Schedule2() {
		log.info("Running toiL1Schedule2");
		runToiL1Schedule();
		runToiBlogsL1Schedule();
	}

	public void runToiL1Schedule() {

		if ("false".equalsIgnoreCase(toil1_enabled)) {
			return;
		}

		try {
			// Step-1: Get active website
			List<Website> websiteList = websiteDao.findBySiteCodeAndSocialcode(true,"toil1", "facebook");
			for(Website website: websiteList) {
				if (website != null && website.getActive()) {
					log.debug("Processing data for Social App {} of website {}",website.getSocialAppName(), website.getName());
					// TODO Step-2: If this website doesnot has accesstoken, then get that
	
					if ("FACEBOOK".equals(website.getSocialAppName())) {
						int count = 0;
						int total = topicPageDao.countAllActive(website.getId());
						int resultsPerThread = 10;
						count = total / resultsPerThread;
						if (total % resultsPerThread > 0) {
							++count;
						}
						for (int i = 1; i <= count; i++) {
							// Construction is allowed using this constructor only
							Follow follow = (TOIFacebookCategoryFollowImpl)context.getBean
										("toiFacebookCategoryFollowImpl",i,resultsPerThread,website);
							// start threads and place in runnable state
							threadExecutor.execute(follow); // start multiple tasks
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception while executing runToiL1Schedule,\n Error Message ", e);
		}
	}
	
	public void runToiBlogsL1Schedule() {

		if ("false".equalsIgnoreCase(toiblogsl1_enabled)) {
			return;
		}

		try {
			// Step-1: Get active website
			Website website = websiteDao.findById(Short.parseShort(toiblogsl1_websiteid));
			if (website != null && website.getActive()) {
				log.debug("Processing data for Social App {} of website {}",website.getSocialAppName(), website.getName());
				// TODO Step-2: If this website doesnot has accesstoken, then get that

				if ("FACEBOOK".equals(website.getSocialAppName())) {
					// Construction is allowed using this constructor only
					Follow follow = (TOIBlogsFacebookCategoryFollowImpl)context.getBean
								("toiBlogsFacebookCategoryFollowImpl",1,1,website);
					// start threads and place in runnable state
					threadExecutor.execute(follow); // start multiple tasks
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception while executing runToiBlogsL1Schedule,\n Error Message ", e);
		}
	}
	
	
	@Value("${twittertoi.enabled}")
	private String twittertoi_enabled;
	
	@Scheduled(cron = "${twittertoi.schedule}")
	public void runTwitterToiSchedule() {

		if ("false".equalsIgnoreCase(twittertoi_enabled)) {
			return;
		}

		try {
			// Step-1: Get active website
			List<Website> websiteList = websiteDao.findBySiteCodeAndSocialcode(true,"toi", "twitter");
			for(Website website:websiteList){
				if (website != null && website.getActive()) {
					log.debug("Processing data for Social App {} of website {}",website.getSocialAppName(), website.getName());
					// Construction is allowed using this constructor only
					Follow follow = (TOITwitterCategoryFollowImpl)context.getBean("toiTwitterCategoryFollowImpl",1,1,website);
					// start threads and place in runnable state
					threadExecutor.execute(follow); // start multiple tasks
				}
			}
			websiteList = websiteDao.findBySiteCodeAndSocialcode(true,"toi", "toibreakingnews");
			for(Website website:websiteList){
				if (website != null && website.getActive()) {
					log.debug("Processing data for Social App {} of website {}",website.getSocialAppName(), website.getName());
					// Construction is allowed using this constructor only
					Follow follow = (TOITwitterBreakingNewsFollowImpl)context.getBean("toiTwitterBreakingNewsFollowImpl",1,1,website);
					// start threads and place in runnable state
					threadExecutor.execute(follow); // start multiple tasks
				}
			}
			websiteList = websiteDao.findBySiteCodeAndSocialcode(true,"toi", "toiblogs");
			for(Website website:websiteList){
				if (website != null && website.getActive()) {
					log.debug("Processing data for Social App {} of website {}",website.getSocialAppName(), website.getName());
					// Construction is allowed using this constructor only
					Follow follow = (TOIBlogsTwitterCategoryFollowImpl)context.getBean("toiBlogsTwitterCategoryFollowImpl",1,1,website);
					// start threads and place in runnable state
					threadExecutor.execute(follow); // start multiple tasks
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception while executing runTwitterToiSchedule,\n Error Message ", e);
		}
	}
	
	@Scheduled(cron = "${olySchedule.schedule}")
	public void olySchedule() {
		log.info("Running olySchedule");
		runOLYSFaceBookchedule();
		runTOISFaceBookchedule();
	}

	@Value("${olySchedule.enabled}")
	private String olySchedule_enabled;
	
	@Value("${olyScheduleFaceBook.websiteid}")
	private String olyScheduleFaceBook_websiteid;
	
	@Value("${olyScheduleTwitter.websiteid}")
	private String olyScheduleTwitter_websiteid;
	
	public void runOLYSFaceBookchedule() {

		if ("false".equalsIgnoreCase(olySchedule_enabled)) {
			return;
		}
		try {
			// Step-1: Get active website
			Website websiteFaceBook = websiteDao.findById(Short.parseShort(olyScheduleFaceBook_websiteid));
			Website websiteTwitter = websiteDao.findById(Short.parseShort(olyScheduleTwitter_websiteid));
			if (websiteFaceBook != null && websiteFaceBook.getActive()) {
				log.debug("Processing data for Social App {} of website {}",websiteFaceBook.getSocialAppName(), websiteFaceBook.getName());
				Follow followFaceBook = (FacebookOLYFeedPush)context.getBean("facebookOLYFeedPush",1,1,websiteFaceBook);
				threadExecutor.execute(followFaceBook); // start multiple tasks
			}
			
			if (websiteTwitter != null && websiteTwitter.getActive()) {
				log.debug("Processing data for Social App {} of website {}",websiteTwitter.getSocialAppName(), websiteTwitter.getName());
				Follow followTwitter = (TwitterOLYFeedPush)context.getBean("twitterOLYFeedPush",1,1,websiteTwitter);
				threadExecutor.execute(followTwitter); // start multiple tasks
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception while executing olySchedule,\n Error Message ", e);
		}
	}
	
	@Value("${toiScheduleFaceBook.websiteid}")
	private String toiScheduleFaceBook_websiteid;
	
	@Value("${toiScheduleTwitter.websiteid}")
	private String toiScheduleTwitter_websiteid;
	
	@Value("${toiSchedule.enabled}")
	private String toiSchedule_enabled;
	
	public void runTOISFaceBookchedule() {

		if ("false".equalsIgnoreCase(toiSchedule_enabled)) {
			return;
		}
		try {
			// Step-1: Get active website
			Website websiteFaceBook = websiteDao.findById(Short.parseShort(toiScheduleFaceBook_websiteid));
			Website websiteTwitter = websiteDao.findById(Short.parseShort(toiScheduleTwitter_websiteid));
			if (websiteFaceBook != null && websiteFaceBook.getActive()) {
				log.debug("Processing data for Social App {} of website {}",websiteFaceBook.getSocialAppName(), websiteFaceBook.getName());
				Follow followFaceBook = (FacebookOLYFeedPush)context.getBean("facebookOLYFeedPush",1,1,websiteFaceBook);
				threadExecutor.execute(followFaceBook); // start multiple tasks
			}
			
			if (websiteTwitter != null && websiteTwitter.getActive()) {
				log.debug("Processing data for Social App {} of website {}",websiteTwitter.getSocialAppName(), websiteTwitter.getName());
				Follow followTwitter = (TwitterOLYFeedPush)context.getBean("twitterOLYFeedPush",1,1,websiteTwitter);
				threadExecutor.execute(followTwitter); // start multiple tasks
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception while executing main toiSchedule,\n Error Message ", e);
		}
	}
	public String getEnabled() {
		return toitopic_enabled;
	}

	public void setEnabled(String enabled) {
		this.toitopic_enabled = enabled;
	}

	@Value("${scheduleMessageJob.enabled}")
	private boolean scheduleMessageJobEnabled;
	@Autowired
	@Qualifier("facebookCategoryPage")
	private Page facebookCategoryPage;
	@Autowired
	@Qualifier("twitterCategoryPage")
	private Page twitterCategoryPage;
	@Scheduled(cron = "${scheduleMessageJob.schedule}")
	public void scheduledMessageJob() {
		log.debug("scheduleMessageJob Started.");
		if(!scheduleMessageJobEnabled) {
			return;
		}
		try {
			// Step-1: Get active website
			List<Website> websiteList = websiteDao.findByActive(true);
			for (Website website : websiteList) {
				if ("FACEBOOK".equals(website.getSocialAppName())) {
					facebookCategoryPage.postArticle(website, true);
				} else if ("TWITTER".equals(website.getSocialAppName())) {
					twitterCategoryPage.postArticle(website, true);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error(
					"Exception while executing main toiSchedule,\n Error Message ",
					e);
		}
	}
	
}
