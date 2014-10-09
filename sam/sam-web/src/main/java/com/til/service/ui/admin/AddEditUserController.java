/**
 * 
 */
package com.til.service.ui.admin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.til.security.acegi.CustomUser;
import com.til.service.common.dao.RoleDao;
import com.til.service.common.dao.UserDao;
import com.til.service.common.dao.WebsiteDao;
import com.til.service.common.dao.hibernate.entity.Role;
import com.til.service.common.dao.hibernate.entity.User;
import com.til.service.common.dao.hibernate.entity.Website;
import com.til.service.utils.DateUtil;

/**
 * @author Harsh.Gupta
 *
 */
@Controller
@RequestMapping("addEditUser")
public class AddEditUserController {
	
	private static final Log log = LogFactory.getLog(AddEditUserController.class);
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private WebsiteDao websiteDao;
	
	@Autowired
	private RoleDao roleDao;
	
	@RequestMapping(method = RequestMethod.POST)
	public String onSubmit(@Valid User user, BindingResult result, 
					ModelMap model, HttpServletRequest request) throws ServletException {
    	
		if (result.hasErrors()) {
            return "addEditUser";
		}
		String mode = request.getParameter("m");
		String id = request.getParameter("id");
		String roleid = request.getParameter("r");
		String websiteid = request.getParameter("w");
		HttpSession session = request.getSession();
		
		if("edit".equalsIgnoreCase(mode) && id != null && !"".equals(id))
		{
			String password = (String)session.getAttribute("oldpassword");
			// If user's initial password does not matches the changed password(i..e if changed from screen), 
			// then, that means password has changed 
			if(!password.equals(user.getPassphrase()))
			{
				// This means user has changed password so encode it
				Md5PasswordEncoder md5PasswordEncoder = new Md5PasswordEncoder();
				String md5Hash = md5PasswordEncoder.encodePassword(user.getPassphrase(), user.getEmailAddress());
				log.debug("md5Hash User Password [" + md5Hash + "]");
				user.setDecrypted(user.getPassphrase());
				user.setPassphrase(md5Hash);
			}
			Role role = roleDao.load(user.getRole().getId());
			List<Website> websites = new ArrayList<Website>();
			for(Short websiteids:user.getWebsiteids()){
				Website website = websiteDao.get(websiteids);
				websites.add(website);
			}
			
			// Delete corresponding website and role entry
			Map<Website, Role> websiteRole = new HashMap<Website, Role>();
			for(Website website:websites){
				websiteRole.put(website, role);
			}
			user.setWebsiterole(websiteRole);
			userDao.update(user);
			model.addAttribute("sucessMsg","Changes saved");
			return "redirect:addEditUser?sucessMsg=ChangesSaved&id=" + user.getId() + "&m=" + request.getParameter("m");
		}
		else
		{
			String password = user.getPassphrase();
			log.debug("User Password [" + password + "]");
			Md5PasswordEncoder md5PasswordEncoder = new Md5PasswordEncoder();
			String md5Hash = md5PasswordEncoder.encodePassword(password, user.getEmailAddress());
			log.debug("md5Hash User Password [" + md5Hash + "]");
			user.setDecrypted(password);
			user.setPassphrase(md5Hash);
			
			try
			{	List<Website> websiteList = new ArrayList<Website>();
				Role role = roleDao.load(user.getRole().getId());
				for(Short siteid:user.getWebsiteids()){
					Website website = websiteDao.load(siteid);
					websiteList.add(website);
				}
				
				// Delete corresponding website and role entry
				Map<Website, Role> websiteRole = new HashMap<Website, Role>();
				for(Website website:websiteList){
					websiteRole.put(website, role);
				}
				
				user.setWebsiterole(websiteRole);
				userDao.save(user);
			}
			catch(ConstraintViolationException e)
			{
				e.printStackTrace();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			log.info("returning form add/edit user view to addEditUser");
			model.addAttribute("sucessMsg","Changes saved");
			return "redirect:addEditUser?sucessMsg=ChangesSaved&id=" + user.getId() + "&m=" + request.getParameter("m") ;
		}
    }
	
	@RequestMapping(method = RequestMethod.GET)
    protected String showform(HttpServletRequest request, ModelMap model) throws ServletException {
    	String id = request.getParameter("id") ;
    	String r = request.getParameter("r") ;
    	String w = request.getParameter("w") ;
    	
    	HttpSession session = request.getSession(true);
    	
    	User user = null ;
    	if(id == null) {
    		user = new User() ;	
    		session.setAttribute("user", user);
    	}
    	else {
    		// load website from db
    		user = userDao.findById(Integer.parseInt(id));
    		Map<Website, Role> websiteRoleMap = user.getWebsiterole();
    		List<Short> websiteids = new ArrayList<Short>();
    		for (Website website: websiteRoleMap.keySet()) {
    			websiteids.add(website.getId());
    		}
			user.setWebsiteids(websiteids );
    		/*Map<Website, Role> websiteRoleMap = user.getWebsiterole();
    		
    		
    		for (Website website: websiteRoleMap.keySet()) {
    			if(website.getId() == Short.parseShort(w))
    			{
    				Role role = websiteRoleMap.get(website);
    				if(role.getId() == Short.parseShort(r))
        			{
	    				user.setWebsite(website);
	        			user.setRole(role);
	        			//session.setAttribute("user", user);
	    				break;
        			}
    			}
			}*/
    		 
    		session.setAttribute("oldpassword", user.getPassphrase());
    		
    	}
    	model.addAttribute("user",user);
		return "addEditUser";
    }
    
	/*public Collection<Website> getAllWebsite(String websiteid) {
        return websiteDao.findOrderedWebsite(websiteid) ;
    }*/
    
    /*public Collection<Role> getAllRole(int roleOrder) {
        return roleDao.findAllOrdered(roleOrder);
    }*/
    
    //this method is called for each GET and POST request
	@ModelAttribute
	public ModelMap setupForm(ModelMap modelMap) {
		
		if (log.isDebugEnabled()) {
			log.debug(">>>>>Setting up reference data....");
		}
		
		String username = "";
		Website website = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Object obj = auth.getPrincipal();
		
		int roleOrder = 0; 
		if (obj instanceof UserDetails) {
			username = ((CustomUser) obj).getUsername();
			User user = (User)((CustomUser) obj).getUser();
			website = user.getWebsite();
			Collection<GrantedAuthority> authorities = auth.getAuthorities();
			for (GrantedAuthority grantedAuthority : authorities) {
				Role role = roleDao.findByRoleName(grantedAuthority.getAuthority());
				if(role != null)
					roleOrder = role.getSortOrder();
			}
			
		} else {
			username = obj.toString();
		}
		
    	Collection<Website> websiteCol = null;
    	if(1 == website.getId())
		{
    		websiteCol = websiteDao.findAllbyfind();
		}
    	else
    	{
    		// TODO
    		websiteCol = websiteDao.findAllbyfind();
    	}
    	
    	List<Role> roleAllList = roleDao.findAll();
    	List<Role> allowedRoleList = new ArrayList<Role>();
    	modelMap.put("websiteCol", websiteCol);
    	modelMap.put("roleCol", roleAllList);
    	for (Role role : roleAllList) {
    		if(role.getSortOrder() > roleOrder)
    		{
    			allowedRoleList.add(role);
    		}
		}
    	modelMap.put("allowedRoleCol", allowedRoleList);
    	
		//add reference data here
		return modelMap;
	}
	
	@InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
    	
		User user = (User)binder.getTarget();
		 
    	// set User's website from request parameter website id
    	String websiteId = null;
    	try {
    		websiteId = request.getParameter("w");
    		log.debug("WebsiteID " + websiteId);
		} catch (Exception e) {}		
		if (websiteId != null && !"".equals(websiteId)) {
			Website website = websiteDao.findById(Short.parseShort(websiteId));
			log.debug("WebsiteID......... " + websiteId);
			user.setWebsite(website);
		}
		else
		{
			Map<Website, Role> map = user.getWebsiterole();
			if(map != null)
			{
				Set<Entry<Website, Role>> set = map.entrySet();
				for (Iterator iterator = set.iterator(); iterator.hasNext();) {
					Entry<Website, Role> entry = (Entry<Website, Role>) iterator
							.next();
					user.setWebsite(entry.getKey());
					user.setRole(entry.getValue());
				}
				
			}
		}
		
		// set User's role from request parameter role id
    	String roleId = null;
    	try {
    		roleId = request.getParameter("r");
		} catch (Exception e) {}		
		if (roleId != null && !"".equals(roleId)) {
			Role role = roleDao.findById(Short.parseShort(roleId));
			user.setRole(role);
		}
		else
		{
			//Map<Website, Role> map = user.getWebsiterole();
		}
		
		SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtil.formatIndianDate);
		binder.registerCustomEditor(Date.class, null, new CustomDateEditor(dateFormat, true)); 
    }
}
