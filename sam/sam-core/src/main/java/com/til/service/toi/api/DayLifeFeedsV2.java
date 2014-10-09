package com.til.service.toi.api;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.til.service.utils.DateUtil;

@XmlRootElement(name="daylifefeeds")
public class DayLifeFeedsV2 {
	
	private static final Logger log = LoggerFactory.getLogger(DayLifeFeedsV2.class);
	
	private List<Site> siteList = new ArrayList<Site>();
	
	public static class Site 
	{
	    protected String source;
		
		private List<ItemV2> itemList = new ArrayList<ItemV2>();
		
		@XmlElement(name="item")
		public List<ItemV2> getItemList() {
			return itemList;
		}

		public void setItemList(List<ItemV2> itemList) {
			this.itemList = itemList;
		}
		
		@XmlAttribute
		public String getSource() {
			return source;
		}

		public void setSource(String source) {
			this.source = source;
		}
		
		/**
		 * This class also acts as Adapter for Item Adaptee
		 * @author harsh
		 *
		 */
		public static class ItemV2 implements com.til.service.common.api.Item
		{
			private String 	headline;
			private String 	source;
			private String 	agname;
			private Long 	timestamp_epoch;
			private String 	timestamp;
			private String 	excerpt;
			private String 	url;
			
			public String getHeadline() {
				return headline;
			}
			public void setHeadline(String headline) {
				this.headline = headline;
			}
			public String getSource() {
				return source;
			}
			public void setSource(String source) {
				this.source = source;
			}
			public String getAgname() {
				return agname;
			}
			public void setAgname(String agname) {
				this.agname = agname;
			}
			public String getTimestamp() {
				return timestamp;
			}
			public void setTimestamp(String timestamp) {
				this.timestamp = timestamp;
			}
			public String getExcerpt() {
				return excerpt;
			}
			public void setExcerpt(String excerpt) {
				this.excerpt = excerpt;
			}
			public String getUrl() {
				return url;
			}
			public void setUrl(String url) {
				this.url = url;
			}
			public Long getTimestamp_epoch() {
				return timestamp_epoch;
			}
			public void setTimestamp_epoch(Long timestamp_epoch) {
				this.timestamp_epoch = timestamp_epoch;
			}
			
			public String getTitle()
			{
				return getHeadline();
			}
			
			public Date getPubDate()
			{
				try {
					return DateUtil.parse(getTimestamp(), DateUtil.defaultTimestampFormatInSec());
				} catch (ParseException e) {
					e.printStackTrace();
					log.error("Daylife feed item date ParseException:\n Error " , e);
				}catch(NullPointerException e)
				{
					e.printStackTrace();
					log.error("Daylife feed item date NullPointerException: \nItem {} \nError ", getUrl() , e);
				}
				return null;
			}
			
			@Override
			public String toString() {
				return "HeadLine: " + headline + "\nSource " + source + "\nTimestamp " + timestamp + "\nUrl " + url;
			}
		}
	}

	@XmlElement(name="site")
	public List<Site> getSiteList() {
		return siteList;
	}

	public void setSiteList(List<Site> siteList) {
		this.siteList = siteList;
	}
	
}
