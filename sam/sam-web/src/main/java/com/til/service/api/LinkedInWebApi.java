package com.til.service.api;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.til.service.common.dao.TopicPageDao;
import com.til.service.common.dao.WebsiteDao;
import com.til.service.common.dao.hibernate.entity.TopicPage;
import com.til.service.common.dao.hibernate.entity.Website;
import com.til.service.facebook.api.PageDetail;
import com.til.service.linkedin.LinkedInUtils;

/**
 * This LinkedIn API Controller is user for posting on linkedin message,link and image
 * @author Sanjay Gupta
 * 
 */
@Controller
public class LinkedInWebApi {

	private static final Logger log = LoggerFactory.getLogger(LinkedInWebApi.class);
	
	@Autowired
	private TopicPageDao topicPageDao;
	@Autowired
	private WebsiteDao websiteDao;

	@RequestMapping(value = "/linkedin/{version}/post/{pageid}", method = RequestMethod.POST)
	@ResponseBody
	public String linkedInPost(@PathVariable(value="version") String version, @PathVariable("pageid") String pageid, @RequestParam("url") String url,
								@RequestParam(value="imageurl", required=false) String imageurl, @RequestParam("title") String title,
								@RequestParam("description") String description, @RequestParam(value="comment", required=false) String comment, 
								HttpServletResponse resp) {
		
		System.out.println("in controller linkedin post" + url);
		PageDetail pageDetail = null;
		Website website = null;
		String accessToken = null;
		TopicPage tpage = topicPageDao.findByPageId(pageid);
		accessToken = tpage.getAccessToken();
		String accessTokenres[] = accessToken.split("=");
		accessToken = accessTokenres[1];
		if (accessToken == null) {
			website = tpage.getWebsite();
			accessToken = website.getAccessToken();
		}

		pageDetail = LinkedInUtils.postfeed(accessToken, pageid, url, comment, title, imageurl, description);
		String response = pageDetail.getUpdateKey();
		String responsetxt=null;
		
		if(response!=null)
		{
			responsetxt="Successfully posted on LinkedIn for pageid "+pageid+" with Response returned for post on the LinkedIn server with postid " +response+"..";
		}
		
		if(response==null && pageDetail.getMessage()!=null)
		{
			responsetxt=pageDetail.getMessage();
		}

		return responsetxt;
	}

}
