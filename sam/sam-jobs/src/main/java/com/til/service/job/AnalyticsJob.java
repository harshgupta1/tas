package com.til.service.job;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sf.ezmorph.MorphException;
import net.sf.ezmorph.bean.MorphDynaBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.til.service.common.dao.TopicPageArticleDao;
import com.til.service.common.dao.hibernate.entity.Article;
import com.til.service.common.dao.hibernate.entity.TopicPage;
import com.til.service.common.dao.hibernate.entity.TopicPageArticle;
import com.til.service.common.dao.hibernate.entity.Website;
import com.til.service.facebook.FacebookUtils;
import com.til.service.facebook.api.Insight;
import com.til.service.facebook.api.Insights;
import com.til.service.facebook.exception.OAuthException;

/**
 * @author Harsh.Gupta
 * 
 */
@Service
public class AnalyticsJob {

	private static final Logger log = LoggerFactory.getLogger(AnalyticsJob.class);

	@Autowired
	private TopicPageArticleDao topicPageArticleDao;
	
	@Value("${facebook.analytics.enabled}")
	private String facebookAnalyticsEnabled;

	//ExecutorService threadExecutor = Executors.newFixedThreadPool(100);

	// Schedule
	@Scheduled(cron = "${facebook.analytics.schedule}")
	public void runFacebookAnalytics() {
		log.info("Running runFacebookAnalytics");

		if ("false".equalsIgnoreCase(facebookAnalyticsEnabled)) {
			return;
		}

		try {
			// Step-1: Get active website
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -15);
			
			List<Object[]> list = null;
			List<TopicPageArticle> topicPageArticleList = new ArrayList<TopicPageArticle>(1000);
			int i = 0;
			int page = 1;
    		do
    		{
    			topicPageArticleList.clear();
	    		list = topicPageArticleDao.findAllBySocialAppAndCreateDate("FACEBOOK", cal.getTime(),page,1000);
	    		
	    		for (Object[] object : list) {
					log.debug("Processing {} record",++i);
					Website website = (Website)object[0];
					TopicPage topicPage = (TopicPage)object[1];
					Article article = (Article)object[2];
					TopicPageArticle topicPageArticle = (TopicPageArticle)object[3];
					String insightjson = FacebookUtils.getpostinsights(website.getAccessToken(),topicPage.getPageId(),topicPageArticle.getPostId());
					Insights insights = null;
					try
					{
						insights = FacebookUtils.parseInsightsConnection(insightjson);
					}
					catch(OAuthException e)
					{
						log.error("OAuthException for website with topicpageid " + topicPage.getPageId() +" and post-id "+topicPageArticle.getPostId() 
									+ " insights json is " + insights,e);
					}
					if(insights != null && insights.getData() != null && insights.getData().length > 0)
					{
						log.debug("{} Processing insights for post {}",i,insights.getData()[0].getId());
						Insight insight = FacebookUtils.getInsight(insights, "post_impressions_unique");
						try
						{
							if(insight != null)
							{
								MorphDynaBean bean =  insight.getValues().get(0);
								int value = (Integer)bean.get("value");
								topicPageArticle.setImpressionsUnique(value);
							}
						}catch(MorphException e)
						{
							log.error("Unable to find value object in insight for post id "+insight.getId(),e);
						}
						insight = FacebookUtils.getInsight(insights, "post_impressions");
						try
						{
							if(insight != null)
							{
								MorphDynaBean bean =  insight.getValues().get(0);
								int value = (Integer)bean.get("value");
								topicPageArticle.setImpressionsTotal(value);
							}
						}catch(MorphException e)
						{
							log.error("Unable to find value object in insight for post id "+insight.getId(),e);
						}
						insight = FacebookUtils.getInsight(insights, "post_consumptions_by_type_unique");
						try
						{
							if(insight != null)
							{
								MorphDynaBean bean =  insight.getValues().get(0);
								try
								{
									bean = (MorphDynaBean)bean.get("value");
									int value = (Integer)bean.get("link clicks");
									topicPageArticle.setClicksUnique(value);
								}
								catch(ClassCastException e)
								{
									topicPageArticle.setClicksUnique(0);
								}
							}
						}catch(MorphException e)
						{
							log.info("Unable to find link clicks object in insight for post id "+insight.getId(),e);
						}
						
						insight = FacebookUtils.getInsight(insights, "post_consumptions_by_type");
						try
						{
							if(insight != null)
							{
								MorphDynaBean bean =  insight.getValues().get(0);
								try
								{
									bean = (MorphDynaBean)bean.get("value");
									int value = (Integer)bean.get("link clicks");
									topicPageArticle.setClicksTotal(value);
								}
								catch(ClassCastException e)
								{
									topicPageArticle.setClicksTotal(0);
								}
							}
						}catch(MorphException e)
						{
							log.info("Unable to find link clicks object in insight for post id "+insight.getId(),e);
						}
						topicPageArticleList.add(topicPageArticle);
					}
				}
	    		topicPageArticleDao.saveOrUpdateAll(topicPageArticleList);
	    		++page;
    		}while(list.size()>0);
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception while executing runFacebookAnalytics,\n Error Message ", e);
		}
	}
	
}
