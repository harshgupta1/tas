/**
 * 
 */
package com.til.service.ui.admin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.til.security.acegi.CustomUser;
import com.til.service.common.dao.TopicPageDao;
import com.til.service.common.dao.WebsiteDao;
import com.til.service.common.dao.hibernate.entity.TopicPage;
import com.til.service.common.dao.hibernate.entity.User;
import com.til.service.common.dao.hibernate.entity.Website;


/**
 * @author Harsh.Gupta
 */
@Controller
@RequestMapping("addEditTopic")
@SessionAttributes({"topicPage"})
public class AddEditTopicController {
	
	private static Log log = LogFactory.getLog(AddEditTopicController.class);
	
	@Autowired
	WebsiteDao	websiteDao; 
	
	@Autowired
	TopicPageDao	topicPageDao;
	
	@RequestMapping(method=RequestMethod.POST)
	public String onSubmit(@Valid TopicPage topicPage, BindingResult result, 
				ModelMap model, HttpServletRequest request) throws ServletException {
		
		if (result.hasErrors()) {
            return "addEditTopic";
		}
		
		try
		{
			if(topicPage.getWebsite() == null)
			{
				TopicPage oTopicPage = topicPageDao.get(topicPage.getId());
				if(oTopicPage != null)
				{
					topicPage.setWebsite(oTopicPage.getWebsite());
				}
			}
			topicPageDao.saveOrUpdate(topicPage);
			model.addAttribute("sucessMsg","Changes saved");
		}
		catch(RuntimeException e)
		{
			model.addAttribute("sucessMsg", e.getMessage());
			log.error("Runtime Exception while submitting Topic form " + e.getMessage());
			e.printStackTrace();
		}
		return "redirect:addEditTopic?sucessMsg=Changes Saved&id=" + topicPage.getId() + "&m=" + request.getParameter("m") ;
	}
	
	@RequestMapping(method=RequestMethod.GET)
	protected String showForm(HttpServletRequest request, ModelMap model) {
		
		String username = "";
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Object obj = auth.getPrincipal();
		User user = null;
		if (obj instanceof UserDetails) {
			username = ((CustomUser) obj).getUsername();
			user = (User)((CustomUser) obj).getUser();
		} else {
			username = obj.toString();
		}
		
		String id = request.getParameter("id");
		
		TopicPage topicPage = null;
		if (id == null || "null".equalsIgnoreCase(id)) 
		{
			topicPage = new TopicPage();
			request.setAttribute("m", "new");
		} 
		else 
		{
			// load website from db
			Integer iid = Integer.parseInt(id);
			topicPage = topicPageDao.findByIdLeftJoinWebsite(iid);
			request.setAttribute("m", "edit");
		}
		getWebsites(model, request, user);
		if(topicPage != null)
			model.addAttribute("topicPage",topicPage);
		
		return "addEditTopic";
	}
	
	private void getWebsites(ModelMap modelMap, HttpServletRequest request, User user)
	{
		Set<Website> websiteList  = null;
		if(user.getWebsite().getId() == 1)
		{
			websiteList = new HashSet<Website>(websiteDao.findAll());
		}
		else
		{
			websiteList = user.getWebsiterole().keySet();
		}
    	
	    modelMap.addAttribute("websiteCol",websiteList);
	}
}
