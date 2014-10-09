/**
 * 
 */
package com.til.service.ui.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.til.security.acegi.CustomUser;
import com.til.service.common.dao.UserDao;
import com.til.service.common.dao.hibernate.entity.User;
import com.til.service.common.dao.hibernate.entity.Website;
import com.til.service.common.vo.UserVO;

/**
 * @author Harsh.Gupta
 *
 */
@Controller
@RequestMapping("listUser")
public class ListUserController{
	
	/** Logger for this class and subclasses */
    protected final Log logger = LogFactory.getLog(getClass());
    @Autowired
    private UserDao     userDao ; 
	
    @RequestMapping(method=RequestMethod.GET)
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String username = "";
		Website website = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Object obj = auth.getPrincipal();

		if (obj instanceof UserDetails) {
			username = ((CustomUser) obj).getUsername();
			User user = (User)((CustomUser) obj).getUser();
			website = user.getWebsite();
			
		} else {
			username = obj.toString();
		}
		
		ModelAndView mv = new ModelAndView() ;
		if(website != null)
		{
			List<UserVO> list = null;
			if(website.getId() == 1)
			{
				list = userDao.findAllUsers();
			}
			else
			{
				list = userDao.findAllUsers(website.getId());
			}
			
			mv.addObject("list", list) ;
		}
			mv.setViewName("listUser") ;
		return mv;
	}

}
