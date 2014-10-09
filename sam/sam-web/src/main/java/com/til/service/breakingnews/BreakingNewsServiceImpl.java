package com.til.service.breakingnews;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.til.service.common.api.Page;
import com.til.service.common.dao.ArticleDao;
import com.til.service.common.dao.TopicPageDao;
import com.til.service.common.dao.WebsiteDao;
import com.til.service.common.dao.hibernate.entity.Article;
import com.til.service.common.dao.hibernate.entity.TopicPage;
import com.til.service.ui.admin.BreakingNewsController;

@Service("breakingNewsServiceImpl")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class BreakingNewsServiceImpl implements IBreakingNewsService{
	
	private static Logger logger = LoggerFactory.getLogger(BreakingNewsController.class);
	private final String TWITTER="TWITTER";
	private final String FACEBOOK="FACEBOOK";
	
	private String siteName;
	private String news;
	
	@Autowired
	WebsiteDao websiteDao; 
	
	@Autowired
	TopicPageDao topicPageDao;
	
	@Autowired
	private ArticleDao	articleDao;
	
	@Autowired
	@Qualifier("twitterCategoryPage")
	private Page twitterPage;
	
	@Autowired
	@Qualifier("facebookCategoryPage")
	private Page facebookPage;
	
	
	@Autowired
	public BreakingNewsServiceImpl(String siteName, String news) {
		super();
		this.siteName = siteName;
		this.news = news;
	}

	@Override
	public void run() {
		logger.debug("Publishing breaking news for sitename {} and news is {}" ,siteName,news);
		List<TopicPage> topicPageList = topicPageDao.findByWebsiteName(siteName);
		List<TopicPage> twitterTopicPageList = new ArrayList<TopicPage>();
		List<TopicPage> facebookTopicPageList = new ArrayList<TopicPage>();
		
		for(TopicPage topicPage:topicPageList){
			String socialApp = topicPage.getWebsite().getSocialAppName();
			if(TWITTER.equalsIgnoreCase(socialApp)){
				twitterTopicPageList.add(topicPage);
			}else if(FACEBOOK.equalsIgnoreCase(socialApp)){
				facebookTopicPageList.add(topicPage);
			}
		}
		if(!twitterTopicPageList.isEmpty()){
			twitterPage.fetchPageDetails(twitterTopicPageList);
		}
		if(!facebookTopicPageList.isEmpty()){
			facebookPage.fetchPageDetails(facebookTopicPageList);
		}

		List<Article> articleFeedList  = getArticleList(topicPageList,news);
		if(!articleFeedList.isEmpty()){
			articleDao.saveOrUpdateAll(articleFeedList);
			logger.debug("Saving {} articles in article table.", articleFeedList.size());
		}
		if(!facebookTopicPageList.isEmpty()){
			facebookPage.postArticle(facebookTopicPageList.get(0).getWebsite());
		}
		
		if(!twitterTopicPageList.isEmpty()){
			twitterPage.postArticle(twitterTopicPageList.get(0).getWebsite());
		}
	}
	
	private List<Article> getArticleList(List<TopicPage> topicPageList, String news) {
		List<Article> articleList = new ArrayList<Article>();
		for(TopicPage topicPage:topicPageList){
			Article article = new Article(topicPage,topicPage.getEntityName(),topicPage.getWebsite().getSocialAppId(),topicPage.getWebsite(),news,"",false,new Date(),0 );
			articleList.add(article);
		}
		return articleList;
	}

}
