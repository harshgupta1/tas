package com.til.service.ui.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.til.service.common.dao.RoleDao;
import com.til.service.common.dao.hibernate.entity.Role;


/**
 * @author Harsh.Gupta
 *
 */
@Controller
public class ListRoleController {
	
    protected final Log logger = LogFactory.getLog(getClass());
    
    @Autowired
    private RoleDao     roleDao ; 
	
	@RequestMapping(value="/listRole" , method = RequestMethod.GET)
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response){
		
		ModelAndView mv = new ModelAndView() ;
		List<Role> list = roleDao.findAll();
		mv.addObject("list", list) ;
		mv.setViewName("listRole") ;
		return mv;
	}
	
	@RequestMapping(value="/deleteRole" , method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam("id") Short id, ModelMap model){
		
		if(!"".equals(id))
    	{
    		roleDao.delete(roleDao.load(id));
    	}
		return new ModelAndView(new RedirectView("listRole"));
	}
}
