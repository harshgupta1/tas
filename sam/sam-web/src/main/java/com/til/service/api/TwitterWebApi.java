package com.til.service.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Locale;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
import com.til.service.APIPostResponse;
import com.til.service.CodeEnum;
import com.til.service.common.dao.ArticleDao;
import com.til.service.common.dao.TopicPageArticleDao;
import com.til.service.common.dao.TopicPageDao;
import com.til.service.common.dao.WebsiteDao;
import com.til.service.common.dao.hibernate.entity.Article;
import com.til.service.common.dao.hibernate.entity.TopicPage;
import com.til.service.common.dao.hibernate.entity.TopicPageArticle;
import com.til.service.common.dao.hibernate.entity.Website;
import com.til.service.twitter.TwitterUtils;

/**
 * This Twitter API Controller is user for posting on twitter message,link and
 * image
 * 
 * @author Sanjay Gupta
 * 
 */
@Controller
public class TwitterWebApi {

	private static final Logger log = LoggerFactory.getLogger(TwitterWebApi.class);

	@Autowired
	private TopicPageDao topicPageDao;
	
	@Autowired
	private WebsiteDao websiteDao;
	
	@Autowired
	private ArticleDao	articleDao;
	
	@Autowired
	private TopicPageArticleDao	topicPageArticleDao;
	
	@Autowired
	private MessageSource messageSource;
	
	Locale locale = LocaleContextHolder.getLocale();

	private String location = System.getProperty("catalina.base").replace("\\", "/")+"/upload";
	
	/**
	 * 
	 * @param version
	 * @param pageid
	 * @param url
	 * @param minifiedUrl
	 * @param message
	 * @param resp
	 * @return
	
	 */
	@RequestMapping(value = "/api/twitter/{version}/post/{pageid}", method = RequestMethod.POST)
	@ResponseBody
	public APIPostResponse postArticle(@PathVariable("version") String version, @PathVariable("pageid") String pageid, @RequestParam(value="url", required=true) String url,
										@RequestParam(value="message", required=true) String message, @RequestParam(value="scheduleddate", required=false) Long scheduleddate, 
										@RequestParam(value="appcode",required=true) String appCode, HttpServletResponse resp){
		
		log.debug("Twitter post intialize for api version {} ,pageid {} ,url {} ,message {} ,scheduleddate {}", 
						new Object[]{version, pageid, url, message, scheduleddate});
		url = decodeValue(url);
		message = decodeValue(message);	
		APIPostResponse postResponse = new APIPostResponse();
		TopicPage topicPage = topicPageDao.findByPageId(pageid);
		if (topicPage != null) {
			// Validate for valid access token
			postResponse = validateAccessToken(topicPage);
			if(postResponse.getCode() != null)
			{
				return postResponse;
			}
			if (scheduleddate == null) {
				//Post article	
				return updateStatus(topicPage, url, message, appCode.toUpperCase(), null);	
			}
			else 
			{
				// Schedule Article	
				return scheduleArticle(scheduleddate, url, message, topicPage, appCode.toUpperCase(), null);	
			}
		}
		else
		{
			// Validate for valid pageid
			return validatePageId(pageid);
		}

	}
	
	/**
	 * 
	 * @param version
	 * @param pageid
	 * @param url
	 * @param file
	 * @param message
	 * @param resp
	 * @return
	 */
	@RequestMapping(value = "/api/twitter/{version}/postimage/{pageid}", method = RequestMethod.POST)
	@ResponseBody
	public APIPostResponse postArticleWithImage(@PathVariable("version") String version, @PathVariable("pageid") String pageid, 
												@RequestParam(value="url", required=true) String url, @RequestParam(value="message", required=true) String message, 
												@RequestParam(value="scheduleddate", required=false) Long scheduleddate, 
												@RequestParam(value="appcode",required=true) String appCode, @RequestParam("fileupload") MultipartFile file, 
												HttpServletResponse resp) {
			
		log.debug("Twitter post intialize for api version {} ,pageid {} ,url {} ,message {} ,scheduleddate {}", 
							new Object[]{version, pageid, url, message, scheduleddate});
		
		String responsetxt = null;
		url=decodeValue(url);
		message=decodeValue(message);	
		APIPostResponse postResponse = new APIPostResponse();
		TopicPage topicPage = topicPageDao.findByPageId(pageid);
		
		if (topicPage != null) {
			
			// Validate for valid access token
			postResponse = validateAccessToken(topicPage);
			if(postResponse.getCode() != null)
			{
				return postResponse;
			}
			
			// Validate for valid uploaded file
			try {
				if(file.getBytes() == null)
				{
					responsetxt = messageSource.getMessage("webapi.invalidImage", new Object[] {},locale);
					log.error("Error in posting on twitter for pageid "+pageid+" and errorMessage "+responsetxt);
					postResponse.setCode(CodeEnum.E103.toString());
					postResponse.setMessage(responsetxt);
					postResponse.setArticleId(0);
					postResponse.setPageId(pageid);
					postResponse.setPostId("");
					return postResponse;
				}
			} catch (NoSuchMessageException e) {
				log.error("Error in posting image on Twitter for pageid "+topicPage.getPageId()+" and errorMessage ",e);		
				e.printStackTrace();
			} catch (IOException e) {
				log.error("Error in posting image on Twitter for pageid "+topicPage.getPageId()+" and errorMessage ",e);		
				e.printStackTrace();
			}
			
			if (!file.isEmpty()) {
			
				if (scheduleddate == null) 
				{
					//Post article with Image
					return updateStatus(topicPage, url, message, appCode.toUpperCase(), file);
				}
				else 
				{
					// Schedule article				
					return scheduleArticle(scheduleddate, url, message, topicPage, appCode.toUpperCase(), file);				
				}
			}
		}
		else
		{
			// Validate for valid Page Id
			return validatePageId(pageid);
		}
		return postResponse;
	}
	
	/**
	 * Update article and topicpage_article table when immediate posted on twitter no date parameter i.e no scheduling
	 *  
	 * @param url
	 * @param message
	 * @param tpage
	 * @param postId
	 * @param appCode
	 * @param photoName
	 * @return
	 */
	public int updateArticle(String url,String message,TopicPage tpage,long postId, String appCode, String photoName)
	{
		// Create an entry in article table
		Article article = new Article();
		article.setUrl(url);
		article.setMessage(message);
		article.setProcessed(true);
		article.setRowlock(false);
		article.setTopicName(tpage.getPageName());
		article.setSocialappid(tpage.getWebsite().getSocialAppId());
		article.setTopicPage(tpage);
		article.setWebsite(tpage.getWebsite());
		article.setPostedphotoid(photoName);
		article.setNotificationMail(false);
		article.setArticleid(url,appCode);
		article.setUpdatedate(new Date());
		article.setScheduled(false);
		article.setApireq(true);
		articleDao.save(article);
		log.debug("Article id for pageid {} is {}", tpage.getPageId(), article.getId());
		
		// Create an entry in TopicPageArticle table
		TopicPageArticle ta = new TopicPageArticle();
		ta.setPostId(postId + "");
		ta.setTopicPage(tpage);
		ta.setArticle(article);
		ta.setRemarks(null);
		ta.setStatus("PROCESSED");
		ta.setImpressionsTotal(0);
		ta.setClicksTotal(0);
		ta.setClicksUnique(0);
		ta.setImpressionsUnique(0);
		topicPageArticleDao.save(ta);
		log.debug("Topic Page Article save  for pageid {}", tpage.getPageId());

		return article.getId();

	}

	/**
	 * Update only article table as date creation parameter is defined and its schedule for posting and posting through table with twittercategorypageimplementation
	 *  
	 * @param scheduledDate
	 * @param url
	 * @param message
	 * @param tpage
	 * @param appCode
	 * @param file
	 * @return
	 */
	public APIPostResponse scheduleArticle(Long scheduledDate,String url,String message,TopicPage tpage, String appCode, MultipartFile file)
	{
		APIPostResponse postResponse = new APIPostResponse();
		
		Article article = new Article();
		article.setUrl(url);
		article.setMessage(message);
		article.setProcessed(false);
		article.setRowlock(false);
		article.setTopicName(tpage.getPageName());
		article.setSocialappid(tpage.getWebsite().getSocialAppId());
		article.setTopicPage(tpage);
		article.setWebsite(tpage.getWebsite());
		article.setCreatedate(new Date(scheduledDate));
		article.setNotificationMail(false);
		article.setArticleid(url, appCode);
		if(file != null)
		{
			article.setPostedphotoid(file.getOriginalFilename());
		}
		article.setUpdatedate(new Date());
		article.setFeedtimestamp(new Date(scheduledDate));
		article.setScheduled(true);
		article.setApireq(true);
		articleDao.save(article);
		log.debug("Article id for pageid {} is {}", tpage.getPageId(), article.getId());
		
		// Save Uploaded Image file
		if(file != null)
		{
			File f = new File(location + "/" + file.getOriginalFilename() + "_" + article.getArticleid());			
			FileOutputStream fos;
			try {
				fos = new FileOutputStream(f);
				fos.write(file.getBytes());
				fos.close();
			} catch (FileNotFoundException e) {
				log.error("Error in posting image on Twitter for pageid "+tpage.getPageId()+" and errorMessage ",e);		
				e.printStackTrace();
			}catch (IOException e) {
				log.error("Error in posting image on Twitter for pageid "+tpage.getPageId()+" and errorMessage ",e);		
				e.printStackTrace();
			}
		}
		
		// Return success response
		String responsetxt = messageSource.getMessage("webapi.postResponse", new Object[] {tpage.getPageId(),article.getId()},locale);
		postResponse.setCode("200");
		postResponse.setPageId(tpage.getPageId());
		postResponse.setArticleId(article.getId());
		postResponse.setMessage(responsetxt);
		postResponse.setPostId("");
		
		return postResponse;
	}
	
	public static String decodeValue(String value)
	{
		try {
			return URLDecoder.decode(value,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error("Error in posting on twitter errorMessage",e);
			e.printStackTrace();
			return null;
		}
	}
	
	private APIPostResponse validateAccessToken(TopicPage topicPage)
	{
		String accessToken = null;
		APIPostResponse postResponse = new APIPostResponse();
		if (topicPage.getAccessToken() != null) {
			String accessTokenres[] = topicPage.getAccessToken().split("=");
			accessToken = accessTokenres[1];
		}
		if (topicPage.getAccessToken() == null || "".equals(topicPage.getAccessToken())) {
			accessToken = topicPage.getWebsite().getAccessToken();
			topicPage.setAccessToken(accessToken);
		}
		
		if(accessToken == null)
		{
			postResponse.setCode(CodeEnum.E101.toString());
			String responsetxt = messageSource.getMessage("webapi.invalidAccessToken", new Object[] {},locale);
			log.error("Error in posting on twitter for pageid "+ topicPage.getPageId() +" and errorMessage "+responsetxt);
			postResponse.setMessage(responsetxt);
			postResponse.setArticleId(0);
			postResponse.setPageId(topicPage.getPageId());
			postResponse.setPostId("");
		}
		return postResponse;
	}
	
	private APIPostResponse validatePageId(String pageid)
	{
		APIPostResponse postResponse = new APIPostResponse();
		postResponse.setCode(CodeEnum.E100.toString());
		postResponse.setMessage(messageSource.getMessage("webapi.invalidId", new Object[]{pageid},locale));
		log.error("Error in posting on twitter for invalid pageid "+pageid);
		postResponse.setArticleId(0);
		postResponse.setPageId(pageid);
		postResponse.setPostId("");
		return postResponse;
	}
	
	private APIPostResponse updateStatus(TopicPage topicPage, String url, String message, String appCode, MultipartFile file) 
	{
		APIPostResponse postResponse = new APIPostResponse();
		Website website = topicPage.getWebsite();
		try {
			
			// Create Twitter Object
			ConfigurationBuilder cb = new ConfigurationBuilder();
			cb.setDebugEnabled(true);
			cb.setOAuthConsumerKey(website.getSocialAppId());
			cb.setOAuthConsumerSecret(website.getSocialApiSecret());
			String accessToken = null;
			if (topicPage.getAccessToken() != null) {
				String accessTokenres[] = topicPage.getAccessToken().split("=");
				accessToken = accessTokenres[1];
			}
			cb.setOAuthAccessToken(accessToken);
			cb.setOAuthAccessTokenSecret(website.getAccessTokenSecret());
			Twitter twitter = new TwitterFactory(cb.build()).getInstance();
			Status status = null;
			String photoName = null;
			if(file == null)
			{
				// Post only article
				status = TwitterUtils.updateStatus(twitter, null, url,message);
			}
			else
			{
				// Post article with Image
				String message1 = message+"\r\n\r\n"+decodeValue(url);
				
				// Save Image for backup Purpose
				File f = new File(location+"/"+file.getOriginalFilename()+"_"+Math.random());							
				FileOutputStream fos;
				try {
					fos = new FileOutputStream(f);
					fos.write(file.getBytes());
					fos.close();
				} catch (FileNotFoundException e) {
					 log.error("Error in posting image on Twitter for pageid "+topicPage.getPageId()+" and errorMessage ",e);
					 e.printStackTrace();
				}
				catch (IOException e) {
					 log.error("Error in posting image on Twitter for pageid "+topicPage.getPageId()+" and errorMessage ",e);
					 e.printStackTrace();
				}
				status = TwitterUtils.updateStatuswithMedia(twitter,message1,f);
				if(status != null){
					f.renameTo(new File(location+"/" + file.getOriginalFilename() + "_" + status.getId()));
				}
				photoName = f.getName();
			}
			
			// Create Article Object & associate saved twitter feed in TopciPage_Article table
			int articleid = updateArticle(url, message, topicPage, status.getId(), appCode, photoName);	
			String responsetxt =  messageSource.getMessage("twitterapi.postResponse", new Object[] {topicPage.getPageId(),status.getId()},locale);	
			log.debug(responsetxt);
			
			// Create success response and return
			postResponse.setCode("200");
			postResponse.setPageId(topicPage.getPageId());
			postResponse.setArticleId(articleid);
			postResponse.setPostId(status.getId()+"");
			postResponse.setMessage(responsetxt);
		} catch (TwitterException e) {
			// On failure create error response and return
			log.error("Error in posting on twitter for pageid "+topicPage.getPageId()+" errorMessage",e);
			postResponse.setCode(CodeEnum.E104.toString());
			postResponse.setMessage(e.getErrorMessage());
			postResponse.setArticleId(0);
			postResponse.setPageId(topicPage.getPageId());
			postResponse.setPostId("");
		}
		return postResponse;
	}
}
