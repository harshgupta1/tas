/**
 * 
 */
package com.til.service.ui.admin;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.til.service.common.dao.WebsiteDao;
import com.til.service.common.dao.hibernate.entity.Website;


/**
 * @author Harsh.Gupta
 */
@Controller
@RequestMapping("addEditWebsite")
@SessionAttributes({"website"})
public class AddEditWebsiteController {
	
	private static Logger log = LoggerFactory.getLogger(AddEditWebsiteController.class);
	
	@Autowired
	WebsiteDao	websiteDao; 
	
	@RequestMapping(method=RequestMethod.POST)
	public String onSubmit(@Valid Website website, BindingResult result, 
				ModelMap model, HttpServletRequest request) throws ServletException {
		
		if (result.hasErrors()) {
            return "addEditWebsite";
		}
		
		try
		{
			website = websiteDao.merge(website);
			model.addAttribute("sucessMsg",URLEncoder.encode("Changes saved", "UTF-8"));
		}
		catch(UnsupportedEncodingException e)
		{
			log.error("Encoding not supported",e);
		}
		catch(RuntimeException e)
		{
			model.addAttribute("RuntimeException ", e.getMessage());
			log.error("Runtime Exception while submitting Website form ",e);
			e.printStackTrace();
		}
		return "redirect:addEditWebsite?sucessMsg=Changes Saved&websiteId=" + website.getId() + "&m=" + request.getParameter("m") ;
	}
	
	@RequestMapping(method=RequestMethod.GET)
	protected String showForm(HttpServletRequest request, ModelMap model) {
		
		String websiteId = request.getParameter("websiteId");
		
		Website website = null;
		if (websiteId == null) {
			website = new Website();
			request.setAttribute("m", "new");
		} else 
		{
			// load website from db
			Short id = Short.parseShort(websiteId);
			website = websiteDao.findById(id);
			request.setAttribute("m", "edit");
		}
		model.addAttribute("website",website);
		return "addEditWebsite";
	}
}
