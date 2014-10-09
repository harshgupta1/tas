package com.til.service.common.api;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="BreakingNews")
public class BreakingNews {
	
	private List<News>	newsList;
	
	public static class News
	{
		private String 	newsTitle;

		public String getNewsTitle() {
			return newsTitle;
		}
		
		@XmlElement(name="NewsTitle")
		public void setNewsTitle(String newsTitle) {
			this.newsTitle = newsTitle;
		}
	}

	public List<News> getNewsList() {
		return newsList;
	}
	
	@XmlElement(name="News")
	public void setNewsList(List<News> newsList) {
		this.newsList = newsList;
	}
	
}
