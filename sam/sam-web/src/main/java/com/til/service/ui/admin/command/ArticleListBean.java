package com.til.service.ui.admin.command;

import java.util.List;

import com.til.service.common.dao.hibernate.entity.Article;
import com.til.service.pagination.vo.NavigationInfo;
import com.til.service.ui.admin.validator.DateFormat;
import com.til.service.ui.admin.validator.EndDateAfterStartDate;

/**
 * @author Harsh.Gupta
 * 
 */

@EndDateAfterStartDate(startdate="startDate", enddate="endDate", format="dd-MM-yyyy", message="End Date should be after Start Date.")
public class ArticleListBean {
	
	public static String FORMNAME_ARTICLEFORM = "listArticle";
	public static String FORMNAME_ARTICLEHISTORYFORM = "articleHistory";
	public static String FORMNAME_SCHEDULED_ARTICLE_FORM = "listScheduledArticle";

	private NavigationInfo navInfo = new NavigationInfo();
	private List<Article> articleList;
	private String topicName;
	private String sortBy;
	private String order;
	
	@DateFormat(format="dd-MM-yyyy", message="Invalid StartDate.")
	private String startDate;
	
	@DateFormat(format="dd-MM-yyyy", message="Invalid EndDate.")
	private String endDate;
	
	private String status;
	private int page = 1;
	private String formname = null;

	public NavigationInfo getNavInfo() {
		return navInfo;
	}

	public void setNavInfo(NavigationInfo navInfo) {
		this.navInfo = navInfo;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFormname() {
		return formname;
	}

	public void setFormname(String formname) {
		this.formname = formname;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public List<Article> getArticleList() {
		return articleList;
	}

	public void setArticleList(List<Article> articleList) {
		this.articleList = articleList;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}
}
