/**
 * 
 */
package com.til.service.ui.admin;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.til.security.acegi.CustomUser;
import com.til.service.common.dao.TopicPageDao;
import com.til.service.common.dao.hibernate.entity.User;
import com.til.service.common.dao.hibernate.entity.Website;

/**
 * @author Harsh.Gupta
 *
 */
@Controller
public class AnalyticsController{
	
	/** Logger for this class and subclasses */
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    
    private static final String FACEBOOK="facebook"; 
    private static final String TWITTER="twitter";
    
    @Autowired
    private TopicPageDao     topicPageDao; 
	
    @RequestMapping(value="/analytics", method=RequestMethod.GET)
	public String handleRequest(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Object obj = auth.getPrincipal();

		User user = null;
		Website website = null;
		if (obj instanceof UserDetails) 
		{
			user = (User) ((CustomUser) obj).getUser();
			website = user.getWebsite();
		} 
		
    	List<Object[]> likeList;
    	List<Object[]> dislikeList;
		if (request.getParameter("pageid")!=null) {
			int topic = Integer.parseInt(request.getParameter("pageid"));
			String pageName = request.getParameter("pagename");
			likeList = topicPageDao.findWebsiteLikesGroupByDate(website.getId(),topic, 15);
			dislikeList = topicPageDao.findWebsiteDisLikesGroupByDate(website.getId(), topic, 15);
			model.addAttribute("likeList", likeList);
			model.addAttribute("dislikeList", dislikeList);
			model.addAttribute("pageName", pageName);
			
			return "analytics";
		}
		likeList = topicPageDao.findWebsiteLikesGroupByDate(user.getId());
		dislikeList = topicPageDao.findWebsiteDisLikesGroupByDate(user.getId());
		
		List<Object[]> faceBookLikes = new ArrayList<Object[]>();
		List<Object[]> twitterLikes = new ArrayList<Object[]>();
		List<Object[]> faceBookDislikes = new ArrayList<Object[]>();
		List<Object[]> twitterDislikes = new ArrayList<Object[]>();
		
		for(Object[] object :likeList){
			if(FACEBOOK.equalsIgnoreCase((String) object[2])){
				faceBookLikes.add(object);
			}else if(TWITTER.equalsIgnoreCase((String) object[2])){
				twitterLikes.add(object);
			}
		}
		
		for(Object[] object :dislikeList){
			if(FACEBOOK.equalsIgnoreCase((String) object[2])){
				faceBookDislikes.add(object);
			}else if(TWITTER.equalsIgnoreCase((String) object[2])){
				twitterDislikes.add(object);
			}
		}
	
		if(!faceBookLikes.isEmpty()){
			model.addAttribute("facebook", true);
			model.addAttribute("faceBookLikes", faceBookLikes);
			model.addAttribute("faceBookDislikes", faceBookDislikes);
		}else{
			model.addAttribute("twitter", false);
		}
		
		if(!twitterLikes.isEmpty()){
			model.addAttribute("twitter", true);
			model.addAttribute("twitterLikes", twitterLikes);
			model.addAttribute("twitterDislikes", twitterDislikes);
		}else{
			model.addAttribute("twitter", true);
		}
		
		return "analyticsall";
		
	}
    

    
}
