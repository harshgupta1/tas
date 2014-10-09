package com.til.service.common.api;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name="rss")
public class Rss2Feeds {
	
	private Channel	channel;
	
	public static class Channel
	{
		private String 	title;
		private String	link;
		private String	description;
		private String	language;
		private Date	lastBuildDate;
		//private String	atom:link;
		private String	copyright;
		private String	docs;
		private Image	image;
		private List<ChannelItem>	itemList;
		
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getLink() {
			return link;
		}
		public void setLink(String link) {
			this.link = link;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getLanguage() {
			return language;
		}
		public void setLanguage(String language) {
			this.language = language;
		}
		public Date getLastBuildDate() {
			return lastBuildDate;
		}
		public void setLastBuildDate(Date lastBuildDate) {
			this.lastBuildDate = lastBuildDate;
		}
		public String getCopyright() {
			return copyright;
		}
		public void setCopyright(String copyright) {
			this.copyright = copyright;
		}
		public String getDocs() {
			return docs;
		}
		public void setDocs(String docs) {
			this.docs = docs;
		}
		public Image getImage() {
			return image;
		}
		public void setImage(Image image) {
			this.image = image;
		}
		@XmlElement(name="item")
		public List<ChannelItem> getItemList() {
			return itemList;
		}
		public void setItemList(List<ChannelItem> itemList) {
			this.itemList = itemList;
		}
		
	}
	
	public static class Image
	{
		private String 	title;
		private String	url;
		private String	link;
		
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getLink() {
			return link;
		}
		public void setLink(String link) {
			this.link = link;
		}
	}
	
	/**
	 * This class also acts as Adapter for ChannelItem Adaptee
	 * @author harsh
	 *
	 */
	public static class ChannelItem implements Item
	{
		private String 	title;
		private String	description;
		private String	link;
		private String	guid;
		private Date	pubDate;
		private String 	articleid;
		
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getLink() {
			return link;
		}
		public void setLink(String link) {
			this.link = link.trim();
		}
		public String getGuid() {
			return guid;
		}
		public void setGuid(String guid) {
			this.guid = guid;
		}
		@XmlJavaTypeAdapter(DateAdapter.class)
		public Date getPubDate() {
			return pubDate;
		}
		public void setPubDate(Date pubDate) {
			this.pubDate = pubDate;
		}
		public String getArticleid() {
			return articleid;
		}
		public void setArticleid(String articleid) {
			this.articleid = articleid;
		}
		public String getUrl()
		{
			return getLink();
		}
		public String getSource()
		{
			return "";
		}
		public String toString()
		{
			StringBuilder channelItem = new StringBuilder();
			channelItem.append("title = [");
			channelItem.append(title);
			channelItem.append("]");
			channelItem.append("description = [");
			channelItem.append(description);
			channelItem.append("]");
			channelItem.append("link = [");
			channelItem.append(link);
			channelItem.append("]");
			channelItem.append("pubDate = [");
			channelItem.append(pubDate);
			channelItem.append("]");
			channelItem.append("articleid = [");
			channelItem.append(articleid);
			channelItem.append("]");
			return channelItem.toString();
		}
	}
	
	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	
}
