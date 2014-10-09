/**
 * 
 */
package com.til.service.ui.admin;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.til.service.common.dao.ArticleDao;
import com.til.service.common.dao.WebsiteDao;
import com.til.service.common.dao.hibernate.entity.Article;
import com.til.service.common.dao.hibernate.entity.Website;

/**
 * @author Harsh.Gupta
 */
@Controller
@RequestMapping("addEditArticle")
public class AddEditArticleController {

	private static Log log = LogFactory.getLog(AddEditArticleController.class);

	@Autowired
	WebsiteDao websiteDao;

	@Autowired
	ArticleDao articleDao;

	@RequestMapping(method = RequestMethod.GET)
	protected String showForm(HttpServletRequest request, ModelMap model) {

		String id = request.getParameter("id");

		Article article = null;
		if (id == null) {
			article = new Article();
			request.setAttribute("m", "new");
		} else {
			// load website from db
			Integer iid = Integer.parseInt(id);
			article = articleDao.findByIdLeftJoinWebsite(iid);
			request.setAttribute("m", "edit");
		}
		getWebsite(model);
		model.addAttribute("article", article);
		return "addEditArticle";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String onSubmit(@Valid Article article, BindingResult result,
			ModelMap model, HttpServletRequest request) throws ServletException {

		if (result.hasErrors()) {
			return "addEditArticle";
		}

		try {
			if (article.getId() != null) {
				Article oldArticle = articleDao.get(article.getId());
				oldArticle.setMessage(article.getMessage());
				oldArticle.setUrl(article.getUrl());
				articleDao.saveOrUpdate(oldArticle);
				model.addAttribute("article", oldArticle);
				request.setAttribute("m", "edit");
			} else {
				// TODO: save new article.
				request.setAttribute("m", "edit");
			}
			model.addAttribute("sucessMsg", "Changes saved");
		} catch (RuntimeException e) {
			model.addAttribute("sucessMsg", e.getMessage());
			log.error("Runtime Exception while submitting Article form ", e);
		}
		getWebsite(model);
		return "addEditArticle";
	}

	private void getWebsite(ModelMap modelMap) {
		List<Website> websiteList = websiteDao.findAll();

		modelMap.addAttribute("websiteCol", websiteList);
	}
}
