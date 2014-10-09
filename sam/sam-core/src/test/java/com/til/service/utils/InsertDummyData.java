package com.til.service.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.til.service.common.dao.ArticleDao;
import com.til.service.common.dao.TopicPageArticleDao;
import com.til.service.common.dao.TopicPageDao;
import com.til.service.common.dao.WebsiteDao;
import com.til.service.common.dao.hibernate.entity.Article;
import com.til.service.common.dao.hibernate.entity.TopicPage;
import com.til.service.common.dao.hibernate.entity.TopicPageArticle;
import com.til.service.common.dao.hibernate.entity.Website;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:root-context.xml" })
public class InsertDummyData {

	@Autowired
	private TopicPageDao topicPageDao;

	@Autowired
	private WebsiteDao websiteDao;

	@Autowired
	private ArticleDao articleDao;

	@Autowired
	private TopicPageArticleDao topicPageArticleDao;

	
	@Test
	public void insertDummyData() throws Exception {

		for (int website_count = 1; website_count <=1; website_count++) {
			Calendar cal =Calendar.getInstance();
			cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)-3);
			Website website = new Website();
			website.setAccessToken("accessToken"+website_count);
			website.setAccessTokenSecret("accessTokenSecret"+website_count);
			website.setActive(true);
			website.setContactEmail("contactEmail"+website_count);
			website.setDescription("description"+website_count);
			website.setEmailFromAddress("emailFromAddress");
			website.setName("name"+website_count);
			website.setPageviewsapi("pageviewsapi"+website_count);
			website.setPostasuser(true);
			website.setShortenerapi("shortenerapi"+website_count);
			website.setSitecode("sitecode"+website_count);
			website.setSiteUrl("siteUrl"+website_count);
			website.setSocialApiSecret("socialApiSecret"+website_count);
			website.setSocialAppId("socialAppId"+website_count);
			website.setSocialcode("twitter"+website_count);
			website.setSocialAppName("socialAppName"+website_count);
			website.setSource("source"+website_count);
			website.setUserid("userid"+website_count);
			website.setUpdatedate(cal.getTime());
			website.setCreatedate(cal.getTime());
			website.setId((short) 55);
			//websiteDao.save(website);
			int art_count =533534;
			for(int tp_count = 21735; tp_count <=50000;tp_count ++)
			{
				Calendar createdate   = Calendar.getInstance();
				Calendar updatedate = Calendar.getInstance();
				TopicPage page =new TopicPage();
				page.setWebsite(website);
				page.setEntityName("entityName"+tp_count );             
				page.setUrl("url"+tp_count );                    
				page.setPageId("pageId"+tp_count);                 
				page.setAccessToken("accessToken"+tp_count);            
				page.setPageName("pageName"+tp_count);               
				page.setUsername("username"+tp_count);               
				page.setPicture("picture"+tp_count);                
				page.setLink("link"+tp_count);                   
				page.setLikes(0);                  
				page.setShares(0);                 
				page.setTalking_about_count(0);    
				page.setCategory("website");               
				page.setDescription("description"+tp_count);            
				page.setFeedUrl("feedUrl"+tp_count);                
				page.setActive(true);                 
				page.setFeedpicktime(0);     
				page.setPoststhreshold(null);         
				page.setMultipleFeedUrl(false);        
				page.setPush_all_article(false);       
				page.setError(null) ;                 
				page.setCreatedate(cal.getTime());
				page.setUpdatedate(cal.getTime());
				topicPageDao.save(page);
				List<Article> articlelist = new ArrayList<Article>();
				List<TopicPageArticle> topicPageArticleList = new ArrayList<TopicPageArticle>();
				System.out.println("Topic pages inserted:"+tp_count);
				for(int i=0; i<=100; i++){
					art_count++;
					Article article = new Article();
					article.setTopicPage(page);
					article.setWebsite(website);
					article.setSocialappid("socialappid"+ art_count);
					article.setArticleid("articleid"+ art_count);
					article.setShortenerhash(null);
					article.setShortenerurl("shortenerurl"+ art_count);
					article.setTopicName("topicname"+ art_count);
					article.setMessage("message"+ art_count);
					article.setUrl("url"+ art_count);
					article.setProcessed(true);
					article.setViewcount(0);
					article.setRowlock(false);
					article.setCreatedate(createdate.getTime());
					article.setFeedtimestamp(createdate.getTime());
					article.setUpdatedate(updatedate.getTime());
					articlelist.add(article);
					
					TopicPageArticle tparticle=new TopicPageArticle();
					tparticle.setTopicPage(page);
					tparticle.setArticle(article);
					tparticle.setPostId("postId"+art_count);
					tparticle.setStatus("PROCESSED");
					tparticle.setRemarks("remarks"+art_count);
					tparticle.setImpressionsTotal(0);
					tparticle.setImpressionsUnique(0);
					tparticle.setClicksTotal(0);
					tparticle.setClicksUnique(0);
					tparticle.setCreatedate(createdate.getTime());
					tparticle.setUpdatedate(updatedate.getTime());
					topicPageArticleList.add(tparticle);
					System.out.println("articles  inserted:"+art_count);
					createdate.set(Calendar.HOUR_OF_DAY, createdate.get(Calendar.HOUR_OF_DAY)-24);
					updatedate.set(Calendar.HOUR_OF_DAY, updatedate.get(Calendar.HOUR_OF_DAY)-24);
					
				}
				articleDao.saveOrUpdateAll(articlelist);
				topicPageArticleDao.saveOrUpdateAll(topicPageArticleList);
				articlelist = null;
				topicPageArticleList=null;
				page= null;
				createdate=null;
				updatedate =null;
				articlelist = new ArrayList<Article>();;
				topicPageArticleList = new ArrayList<TopicPageArticle>();
				try {
					finalize();
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
				
				
				
			
				System.out.println("createdate is "+createdate);
			}
		}
	}
}
