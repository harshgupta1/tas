/**
 * 
 */
package com.til.service.ui.admin;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.til.service.common.dao.RoleDao;
import com.til.service.common.dao.hibernate.entity.Role;


/**
 * @author Harsh.Gupta
 *
 */
@Controller
@RequestMapping("addEditRole")
public class AddEditRoleController {

	@Autowired
	private RoleDao roleDao;
	
	@RequestMapping(method = RequestMethod.POST)
    public String onSubmit(@Valid Role role, BindingResult result, ModelMap model) {
		
		if (result.hasErrors()) {
            return "addEditRole";
		}
		
		try
		{
			roleDao.saveOrUpdate(role);
			model.addAttribute("sucessMsg","Changes saved");
		}
		catch(RuntimeException e)
		{
			model.addAttribute("sucessMsg","Duplicate role. " + role.getRoleName() + " already exists.");
		}
		
		return "addEditRole";
    }

    @RequestMapping(method = RequestMethod.GET)
    public String formBackingObject(ModelMap model, HttpServletRequest request) {
    	String id = request.getParameter("id") ;
    	Role role = null ;
    	if(id == null || "".equals(id)) {
    		role = new Role() ;	
    	}
    	else {
    		// load role from db
    		role = roleDao.findById(Short.parseShort(id)) ;
    	}
    	model.addAttribute(role);
    	return "addEditRole";
    }
}
