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
import com.til.service.APIPostResponse;
import com.til.service.CodeEnum;
import com.til.service.common.dao.ArticleDao;
import com.til.service.common.dao.TopicPageArticleDao;
import com.til.service.common.dao.TopicPageDao;
import com.til.service.common.dao.WebsiteDao;
import com.til.service.common.dao.hibernate.entity.Article;
import com.til.service.common.dao.hibernate.entity.TopicPage;
import com.til.service.common.dao.hibernate.entity.TopicPageArticle;
import com.til.service.facebook.FacebookUtils;
import com.til.service.facebook.api.PageDetail;

/**
 * This Facebook API Controller is user for posting on facebook message,link and image
 * 
 * @author Sanjay Gupta
 * 
 */

@Controller
public class FacebookWebApi {

	private static final Logger log = LoggerFactory.getLogger(FacebookWebApi.class);
	
	@Autowired
	private TopicPageDao topicPageDao;
	
	@Autowired
	private WebsiteDao websiteDao;
	
	@Autowired
	private ArticleDao	articleDao;	
	
	@Autowired
	private MessageSource messageSource;	
	
	@Autowired
	private TopicPageArticleDao	topicPageArticleDao;
	
	Locale locale = LocaleContextHolder.getLocale();

	private String location = System.getProperty("catalina.base").replace("\\", "/")+"/upload";
	
	/**
	 * 
	 * @param version
	 * @param pageid
	 * @param url
	 * @param scheduleddate
	 * @param message
	 * @param resp
	 * @return APIPostResponse
	
	 */
	@RequestMapping(value = "/api/facebook/{version}/post/{pageid}", method = RequestMethod.POST)
	@ResponseBody
	public APIPostResponse postArticle(@PathVariable("version") String version, @PathVariable("pageid") String pageid, @RequestParam(value="url", required=true) String url,
										@RequestParam(value="message", required=true) String message, @RequestParam(value="scheduleddate", required=false) Long scheduleddate, 
										@RequestParam(value="appcode",required=true) String appCode, HttpServletResponse resp){
		
		log.debug("Facebook post intialize for api version {} ,pageid {} ,url {} ,message {} ,scheduleddate {}", 
						new Object[]{version, pageid, url, message, scheduleddate});
		PageDetail pageDetail = null;
		APIPostResponse postResponse = new APIPostResponse();
		TopicPage tpage = topicPageDao.findByPageId(pageid);
		if (tpage != null) {
			
			// Validate for valid access token
			postResponse = validateAccessToken(tpage);
			if(postResponse.getCode() != null)
			{
				return postResponse;
			}
			String accessToken = tpage.getAccessToken();

			if (scheduleddate == null || scheduleddate == 0) {
				pageDetail = FacebookUtils.postfeed(accessToken, pageid, decodeValue(url),decodeValue(message));				
				return updateArticle(pageDetail,tpage, url, message, appCode.toUpperCase(), null);
			}
			else {
				// Schedule Article
				return scheduleArticle(scheduleddate, tpage, url, message, appCode.toUpperCase(), null);
			}
		}
		else {
			return validatePageId(pageid);
		}
	}

	/**
	 * 
	 * @param version
	 * @param pageid
	 * @param message
	 * @param url
	 * @param APIPostResponse
	 * @param file
	 * @param resp
	 * @return APIPostResponse
	
	 */
	@RequestMapping(value = "/api/facebook/{version}/postimage/{pageid}", method = RequestMethod.POST)
	@ResponseBody
	public APIPostResponse postArticleWithImage(@PathVariable("version") String version, @PathVariable("pageid") String pageid, @RequestParam(value="url", required=true) String url,
											@RequestParam(value="message", required=true) String message, @RequestParam(value="scheduleddate", required=false) Long scheduleddate, 
											@RequestParam(value="fileupload", required=true) MultipartFile file, @RequestParam(value="appcode", required=true) String appCode, 
											HttpServletResponse resp){
		
		log.debug("Facebook post intialize for api version {} ,pageid {} ,url {} ,message {} ,scheduleddate {}", 
					new Object[]{version, pageid, url, message, scheduleddate});
		PageDetail pageDetail = null;
		String responsetxt = null;
		APIPostResponse postResponse = new APIPostResponse();
		TopicPage tpage = topicPageDao.findByPageId(pageid);
		if (tpage != null) {
			
			// Validate for valid access token
			postResponse = validateAccessToken(tpage);
			if(postResponse.getCode() != null)
			{
				return postResponse;
			}
		
			try {
				if(file.getBytes() == null)
				{
					 responsetxt = messageSource.getMessage("webapi.invalidImage", new Object[] {},locale);
					 log.error("Error in posting image on facebook for pageid "+pageid+" and errorMessage "+responsetxt);
					 postResponse.setCode(CodeEnum.E103.toString());
					 postResponse.setMessage(responsetxt);
					 postResponse.setArticleId(0);
					 postResponse.setPageId(pageid);
					 postResponse.setPostId("");
					 return postResponse;
				}
			} catch (NoSuchMessageException e) {
				 log.error("Error in posting image on facebook for pageid "+pageid+" and errorMessage ",e);
				e.printStackTrace();
			} catch (IOException e) {
				 log.error("Error in posting image on facebook for pageid "+pageid+" and errorMessage ",e);
				e.printStackTrace();
			}
		
			if (!file.isEmpty()) {
				try{
					if (scheduleddate == null) 
					{
						String caption = decodeValue(message)+"\r\n\r\n"+decodeValue(url);
						File f = new File(location + "/" + file.getOriginalFilename() + "_" + Math.random());					
						FileOutputStream fos = new FileOutputStream(f);
						fos.write(file.getBytes());
						fos.close();
						pageDetail = FacebookUtils.postImage(tpage.getAccessToken(), pageid, f,caption);
						if(postResponse!=null)
						{	
							f.renameTo(new File(location + "/" + file.getOriginalFilename() + "_" + postResponse.getPostId()));
						}
						postResponse = updateArticle(pageDetail,tpage, url, message, appCode.toUpperCase(), f);
						return postResponse;
					}
					else
					{										
						// Schedule Article
						return scheduleArticle(scheduleddate, tpage, url, message, appCode.toUpperCase(), file);
					}	
				}
				catch(Exception e){
					log.error("Error in posting image on facebook for pageid "+pageid+" and errorMessage ",e);		 
				}
			}// file empty check if closed
		}//pageid null check if closed
		else {
			return validatePageId(pageid);
		}
		return postResponse;
	}
	
	private String decodeValue(String value)
	{
		try {
			return URLDecoder.decode(value,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.error("Error in posting on facebook errorMessage",e);
			e.printStackTrace();
			return null;
		}
	}
	
	private APIPostResponse validateAccessToken(TopicPage topicPage)
	{
		APIPostResponse postResponse = new APIPostResponse();
		String accessToken = topicPage.getAccessToken();
		if (accessToken == null || "".equals(accessToken)) {
			accessToken = topicPage.getWebsite().getAccessToken();
			if(accessToken != null)
			{
				accessToken = "access_token="+accessToken;
			}
		}
	
		if(accessToken == null)
		{
			String responsetxt = messageSource.getMessage("webapi.invalidAccessToken", new Object[] {},locale);
			log.error("Error in posting on facebook for pageid "+ topicPage.getPageId() + " and errorMessage "+responsetxt);
			postResponse.setCode(CodeEnum.E101.toString());
			postResponse.setMessage(responsetxt);
			postResponse.setArticleId(0);
			postResponse.setPageId(topicPage.getPageId());
			postResponse.setPostId("");
		}
		else
		{
			topicPage.setAccessToken(accessToken);
		}
		return postResponse;
	}

	private APIPostResponse updateArticle(PageDetail pageDetail, TopicPage topicPage, String url, String message, String appCode, File file)
	{
		APIPostResponse postResponse = new APIPostResponse();
		if (pageDetail.getId() != null) {
			Article article = new Article();
			article.setUrl(decodeValue(url));
			article.setMessage(decodeValue(message));
			article.setProcessed(true);
			article.setRowlock(false);
			article.setTopicName(topicPage.getPageName());
			article.setSocialappid(topicPage.getWebsite().getSocialAppId());
			article.setTopicPage(topicPage);
			article.setWebsite(topicPage.getWebsite());
			article.setNotificationMail(false);
			article.setArticleid(decodeValue(url), appCode);
			article.setUpdatedate(new Date());
			article.setScheduled(false);
			article.setApireq(true);
			if(file != null)
			{
				article.setPostedphotoid(file.getName());
			}	
			articleDao.save(article);
			log.debug("Article save  for pageid {}", topicPage.getPageId());
			TopicPageArticle ta = new TopicPageArticle();
			ta.setPostId(pageDetail.getId());
			ta.setTopicPage(topicPage);
			ta.setArticle(article);
			ta.setRemarks(null);
			ta.setStatus("PROCESSED");
			ta.setImpressionsTotal(0);
			ta.setClicksTotal(0);
			ta.setClicksUnique(0);
			ta.setImpressionsUnique(0);
			topicPageArticleDao.save(ta);
			log.debug("Article posted for pageid {} ,postid {}",topicPage.getPageId(), pageDetail.getId());
			String responsetxt = messageSource.getMessage("facebookapi.postResponse", new Object[] {topicPage.getPageId(),pageDetail.getId()},locale);
			postResponse.setCode("200");
			postResponse.setPageId(topicPage.getPageId());
			postResponse.setArticleId(article.getId());
			postResponse.setMessage(responsetxt);
			postResponse.setPostId(pageDetail.getId());
		}

		if (pageDetail.getError() != null && pageDetail.getId() == null) {
			String responsetxt = pageDetail.getError().getMessage();
			postResponse.setCode(CodeEnum.E104.toString());
			postResponse.setMessage(responsetxt);
			postResponse.setArticleId(0);
			postResponse.setPageId(topicPage.getPageId());
			postResponse.setPostId("");		
			log.error("Error in posting on facebook for pageid "+topicPage.getPageId()+" and errorMessage "+responsetxt);
		}
		
		return postResponse;
	}
	
	private APIPostResponse scheduleArticle(Long scheduleddate, TopicPage topicPage, String url, String message, String appCode, MultipartFile file) 
	{
		APIPostResponse postResponse = new APIPostResponse();
		Article article = new Article();
		article.setUrl(decodeValue(url));
		article.setMessage(decodeValue(message));
		article.setProcessed(false);
		article.setRowlock(false);
		article.setTopicName(topicPage.getPageName());
		article.setSocialappid(topicPage.getWebsite().getSocialAppId());
		article.setTopicPage(topicPage);
		article.setWebsite(topicPage.getWebsite());
		article.setNotificationMail(false);
		article.setArticleid(decodeValue(url), appCode);
		article.setCreatedate(new Date(scheduleddate));
		article.setScheduled(true);
		article.setFeedtimestamp(new Date(scheduleddate));
		article.setUpdatedate(new Date());
		if(file != null)
		{
			article.setPostedphotoid(file.getOriginalFilename());
		}
		article.setApireq(true);
		articleDao.save(article);
		
		if(file != null)
		{
			article.setPostedphotoid(file.getOriginalFilename());
			// Save Image File if it exists
			File f = new File(location+"/"+file.getOriginalFilename()+"_"+article.getArticleid());
			FileOutputStream fos;
			try {
				fos = new FileOutputStream(f);
				fos.write(file.getBytes());
				fos.close();
			} catch (FileNotFoundException e) {
				 log.error("Error in posting image on facebook for pageid "+topicPage.getPageId()+" and errorMessage ",e);		 
				e.printStackTrace();
			}catch (IOException e) {
				log.error("Error in posting image on facebook for pageid "+topicPage.getPageId()+" and errorMessage ",e);		
				e.printStackTrace();
			}
		}
		log.debug("Article id for pageid {} is {}", topicPage.getPageId(), article.getId());
		
		String responsetxt = messageSource.getMessage("webapi.postResponse", new Object[]{topicPage.getPageId(), article.getId()},locale);				
		postResponse.setCode("200");
		postResponse.setPageId(topicPage.getPageId());
		postResponse.setArticleId(article.getId());
		postResponse.setMessage(responsetxt);
		postResponse.setPostId("");
		return postResponse;
	}
	
	private APIPostResponse validatePageId(String pageid)
	{
		APIPostResponse postResponse = new APIPostResponse();
		postResponse.setCode(CodeEnum.E100.toString());
		postResponse.setMessage(messageSource.getMessage("webapi.invalidId", new Object[]{pageid},locale));
		postResponse.setArticleId(0);
		postResponse.setPageId(pageid);
		postResponse.setPostId("");
		log.error("Error in posting on facebook for invalid pageid "+pageid);
		return postResponse;
	}
	
}


