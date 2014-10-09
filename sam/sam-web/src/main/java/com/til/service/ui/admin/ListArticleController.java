/**
 * 
 */
package com.til.service.ui.admin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.til.security.acegi.CustomUser;
import com.til.service.common.dao.ArticleDao;
import com.til.service.common.dao.TopicPageArticleDao;
import com.til.service.common.dao.TopicPageDao;
import com.til.service.common.dao.WebsiteDao;
import com.til.service.common.dao.hibernate.entity.Article;
import com.til.service.common.dao.hibernate.entity.TopicPage;
import com.til.service.common.dao.hibernate.entity.TopicPageArticle;
import com.til.service.common.dao.hibernate.entity.User;
import com.til.service.common.dao.hibernate.entity.Website;
import com.til.service.facebook.FacebookUtils;
import com.til.service.pagination.vo.NavigationInfo;
import com.til.service.ui.admin.command.ArticleListBean;
import com.til.service.utils.DateUtil;

/**
 * @author Harsh.Gupta
 *
 */
@Controller
public class ListArticleController{
	
	/** Logger for this class and subclasses */
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private ArticleDao     articleDao; 
	
    @Autowired
    private WebsiteDao     websiteDao;
    
    @Autowired
    private TopicPageDao    topicPageDao;
    
    @Autowired
    private TopicPageArticleDao     topicPageArticleDao;
    
    @RequestMapping(value="/listArticle", method=RequestMethod.GET)
	public String handleRequest(HttpServletRequest request, HttpServletResponse response,ModelMap model){
		
    	ArticleListBean backingObject = new ArticleListBean();
		backingObject = getData(request, backingObject, false);
		model.addAttribute("articleListBean", backingObject);
		
		return "listArticle";
	}

    @RequestMapping(value="/articleHistory", method=RequestMethod.GET)
    public String history(HttpServletRequest request, HttpServletResponse response,ModelMap model) {
    	ArticleListBean backingObject = new ArticleListBean();
    	backingObject.setFormname(ArticleListBean.FORMNAME_ARTICLEHISTORYFORM);
		model.addAttribute("articleListBean", backingObject);
    	return "articleHistory";
    }
    @RequestMapping(value="/articleHistory", method=RequestMethod.POST)
	public String history(@Valid ArticleListBean articleListBean,BindingResult result, ModelMap model,
						HttpServletRequest request)
	{
		if (result.hasErrors()) {
            return "articleHistory";
		}
		String topicName = request.getParameter("topicName");
		if(topicName==null || topicName.isEmpty()) {
			return "articleHistory";
		}
		articleListBean =(ArticleListBean)getData(request,articleListBean, true);
		articleListBean.setFormname(ArticleListBean.FORMNAME_ARTICLEHISTORYFORM);
		model.addAttribute("articleListBean", articleListBean);
	
		return "articleHistory";
	} 
    
    @RequestMapping(value="/listScheduledArticle", method=RequestMethod.GET)
    public String listScheduledArticle(HttpServletRequest request, HttpServletResponse response,ModelMap model) {
    	ArticleListBean backingObject = new ArticleListBean();
    	backingObject.setStatus("SCHEDULED");
    	backingObject = (ArticleListBean)getData(request,backingObject, true);
    	backingObject.setFormname(ArticleListBean.FORMNAME_SCHEDULED_ARTICLE_FORM);
		model.addAttribute("articleListBean", backingObject);
    	return "listScheduledArticle";
    }
    @RequestMapping(value="/listScheduledArticle", method=RequestMethod.POST)
	public String listScheduledArticle(@Valid ArticleListBean articleListBean,BindingResult result, ModelMap model,
						HttpServletRequest request)
	{
		if (result.hasErrors()) {
            return "listScheduledArticle";
		}
		/*String topicName = request.getParameter("topicName");
		if(topicName==null || topicName.isEmpty()) {
			return "listScheduledArticle";
		}*/
		articleListBean =(ArticleListBean)getData(request,articleListBean, true);
		articleListBean.setFormname(ArticleListBean.FORMNAME_SCHEDULED_ARTICLE_FORM);
		model.addAttribute("articleListBean", articleListBean);
	
		return "listScheduledArticle";
	} 
    
    @SuppressWarnings("unchecked")
	private ArticleListBean getData(HttpServletRequest request,Object command, boolean post)
	{
		String startDateStr = null;
		String endDateStr 	= null;
		Website website 	= null;
		String status 		= null;
		String page 		= null;
		String topicName	= null;
		String sortby 		= null;
		String order 		= null;
		NavigationInfo navInfo =null;
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Object obj = auth.getPrincipal();
		ArticleListBean backingObject = (ArticleListBean)command;
		User user = null;
		if (obj instanceof UserDetails) 
		{
			user = (User) ((CustomUser) obj).getUser();
			website = user.getWebsite();
		} 
		try 
		{
			if (post == false) 
			{
				startDateStr = request.getParameter("startDate");
				endDateStr = request.getParameter("endDate");
				status = backingObject.getStatus();
				if (status == null || status.isEmpty()) {
					status = request.getParameter("status") != null ? request
							.getParameter("status") : "PROCESSED";
				}
				page = request.getParameter("page");
				topicName = request.getParameter("topicName");
			}
			else
			{
				startDateStr = backingObject.getStartDate();
				endDateStr = backingObject.getEndDate();
				status = backingObject.getStatus();
				page = backingObject.getPage() + "";
				topicName = backingObject.getTopicName();
				sortby = backingObject.getSortBy();
				order = backingObject.getOrder();
			}
			if(topicName != null)
			{
				topicName = topicName.trim();
			}
			logger.debug("topicName {} " , topicName);
			navInfo = backingObject.getNavInfo();
			
			SimpleDateFormat sourceDateFormat = new SimpleDateFormat(DateUtil.formatIndianDate);
			
			Date startDate = null;
			Date endDate = null;
			if (startDateStr != null) {
				startDate = DateUtil.parse(startDateStr, sourceDateFormat);
			}
			if (endDateStr != null) {
				endDate = DateUtil.parse(endDateStr, sourceDateFormat);
			}
			
			List<Article> list = null;
			int rowCount = 0;
			if (website != null) {
				if (website.getId() == 1) {
					if ("SCHEDULED".equals(status)) {
						list = articleDao.findAllScheduled(topicName, page, ""
								+ navInfo.getPageSize());
						rowCount = Integer.valueOf(articleDao
								.countAllScheduled(topicName).toString());
					} else {
						list = topicPageArticleDao.findAllOrdered(topicName,
								status, page, "" + navInfo.getPageSize());
						rowCount = Integer.valueOf(topicPageArticleDao
								.countAllOrdered(topicName, status).toString());
					}
				} else {
					if ("SCHEDULED".equals(status)) {
						list = articleDao.findAllScheduledByUserId(
								user.getId(), page, "" + navInfo.getPageSize(),
								topicName, null, null);
						rowCount = Integer.valueOf(articleDao
								.countAllScheduledByUserId(user.getId(),
										topicName).toString());
					} else {
						list = topicPageArticleDao.findAllOrderedByUserId(
								user.getId(), page, "" + navInfo.getPageSize(),
								topicName, null, null);
						rowCount = Integer.valueOf(topicPageArticleDao
								.countByUserId(user.getId(), topicName)
								.toString());
					}
				}
				logger.debug("Retrieving article for website {}",
						website.getId());
			}
			
			String action = request.getParameter("m");
			String websiteId = request.getParameter("websiteId");
			if(action != null && !"".equals(action) && websiteId != null && !"".equals(websiteId))
			{
				//websiteDao.deleteByWebsiteId(websiteId);
			}
			
			navInfo.setRowCount(rowCount);
			if (null == page)
				navInfo.setCurrentPage(backingObject.getPage());
			else
				navInfo.setCurrentPage(Integer.parseInt(page));
			
			navInfo.setDisplayCount(list.size());
			
			backingObject.setArticleList(list);

			backingObject.setEndDate(endDateStr);
			backingObject.setStartDate(startDateStr);
			backingObject.setStatus(status);
			
			backingObject.setFormname(ArticleListBean.FORMNAME_ARTICLEFORM);
				
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			logger.error("Exception while listing WebUsers " , e);
		}
		return backingObject;
	}
	
	@RequestMapping(value="/listArticle", method=RequestMethod.POST)
	public String onSubmit(@Valid ArticleListBean articleListBean,BindingResult result, ModelMap model,
						HttpServletRequest request)
	{
		if (result.hasErrors()) {
			/*for (ObjectError error : result.getGlobalErrors()) {
				System.out.println(error.getDefaultMessage());
			}*/
            return "listArticle";
		}
		articleListBean =(ArticleListBean)getData(request,articleListBean, true);
		model.addAttribute("articleListBean", articleListBean);
	
		return "listArticle";
	}
	
    @RequestMapping(value = "/deleteArticle",method = RequestMethod.GET)
    public ModelAndView delete(@RequestParam("aid") Integer aid,@RequestParam("pid") String postid,
    		@RequestParam("app") String app, @RequestParam("wid") Short wid,
    		@RequestParam("tid") Integer tid,@RequestParam("tpaid") Long tpaid, ModelMap model)
    {
    	if(!"".equals(aid))
    	{
    		Website website = websiteDao.get(wid);
    		TopicPage topicPage = topicPageDao.get(tid);
    		if("FACEBOOK".equals(app))
    		{
    			String result = FacebookUtils.deletepost(website.getAccessToken(), 
    							topicPage.getPageId(), postid);
    			TopicPageArticle topicPageArticle = topicPageArticleDao.get(tpaid);
    			if("true".equals(result))
    			{
    				topicPageArticle.setStatus(TopicPageArticle.DELETED);
    				logger.debug("Deleting article with article id {}" , aid);
    			}
    			else
    			{
    				topicPageArticle.setRemarks(result);
    				topicPageArticle.setStatus(TopicPageArticle.DELETED);
    				logger.debug("Error while deleting article with article id {}, Msg {}" , aid, result);
    			}
				topicPageArticleDao.saveOrUpdate(topicPageArticle);
    		}
    		//articleDao.delete(articleDao.load(aid));
    	}
    	
		return new ModelAndView(new RedirectView("listArticle"));
    }
    
	@RequestMapping(value = "/deletePendingArticle", method = RequestMethod.GET)
	public ModelAndView deletePendingArticle(HttpServletRequest request,
			HttpServletResponse response, ModelMap model,
			@RequestParam("aid") Integer aid) {

		if (aid != null) {
			articleDao.delete(articleDao.load(aid));
		}
		return new ModelAndView(new RedirectView("listScheduledArticle"));
	}
}
