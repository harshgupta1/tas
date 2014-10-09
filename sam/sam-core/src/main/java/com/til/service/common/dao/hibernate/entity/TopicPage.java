/**
 * 
 */
package com.til.service.common.dao.hibernate.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * @author Harsh.Gupta
 */
@NamedQueries({
	@NamedQuery (
		    name = "topicPage.findByWebsiteIdAndActiveTopic",
		    query = "SELECT t FROM TopicPage t WHERE t.website.id = :id and t.active = :active"
	),
	@NamedQuery (
		    name = "topicPage.findByWebsiteIdTypeAndActiveTopic",
		    query = "SELECT t FROM TopicPage t WHERE t.website.id = :id and t.active = :active and t.type = :type"
	),
	@NamedQuery (
		    name = "topicPage.findAllOrdered",
		    query = "SELECT t,w FROM TopicPage t join t.website w order by t.createdate desc"
	),
	@NamedQuery (
		    name = "topicPage.countAllOrdered",
		    query = "SELECT count(t) FROM TopicPage t"
	),
	@NamedQuery (
		    name = "topicPage.findAllOrderedByTopic",
		    query = "SELECT t,w FROM TopicPage t join t.website w where t.entityName like :topicname order by t.createdate desc"
	),
	@NamedQuery (
		    name = "topicPage.countAllOrderedByTopic",
		    query = "SELECT count(t) FROM TopicPage t where t.entityName like :topicname"
	),
	@NamedQuery (
		    name = "topicPage.findByIdLeftJoinWebsite",
		    query = "SELECT t FROM TopicPage t left join fetch t.website w where t.id=:id"
	),
	@NamedQuery(
			name= "topicPage.findByWebsiteId",
			query = "SELECT t FROM TopicPage t where t.website.id =:id"
	),
	@NamedQuery(
			name="topicPage.findByUserId",
			query="select t,w from TopicPage t join t.website w join w.upjoin up where useindex(t,idx_createdate) is true and up.id= :userid order by t.createdate desc"
	),
	@NamedQuery(
			name="topicPage.countByUserId",
			query="select count(t) from TopicPage t join t.website w join w.userrole up join up.index u where u.id= :userid"
	),
	@NamedQuery(
			name="topicPage.findByUserIdAndTopic",
			query="select t,w from TopicPage t join t.website w join w.userrole up join up.index u where u.id= :userid and t.entityName like :topicname order by t.createdate desc"
	),
	@NamedQuery(
			name="topicPage.countByUserIdAndTopic",
			query="select count(t) from TopicPage t join t.website w join w.userrole up join up.index u where u.id= :userid and t.entityName like :topicname"
	),
	@NamedQuery(
			name="topicPage.findWebsiteDisLikesGroupByDate",
			query="select sum(tph.dislikes), tph.createdate from TopicPageHistory tph join tph.topicPage tp where tp.website.id = :websiteid group by date(tph.createdate) order by tph.createdate asc "
	),
	@NamedQuery(
			name= "topicPage.findByWebsiteAndPageId",
			query = "SELECT t FROM TopicPage t where t.website.id =:id and t.pageId=:pageId"
	),
	
	@NamedQuery(
			name= "topicPage.findByUrl",
			query = "SELECT t FROM TopicPage t where t.url=:url"
	),@NamedQuery(
			name= "topicPage.findByWebsiteName",
			query = "SELECT t FROM TopicPage t join fetch t.website w where w.name=:name order by t.createdate desc"
	)
	,@NamedQuery(
			name= "topicPage.findByPageId",
			query = "SELECT t FROM TopicPage t where t.pageId=:pageId"
	)
})
@Entity
@Table(name="topicpage")
public class TopicPage implements Serializable {
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer  id ;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "websiteid")
	private Website website;
	
	@Column(name="entityname", nullable= false, updatable=false)
	private String entityName;
	
	@Column(nullable= false)
	private String url;
	
	@Column(name="pageid")
	private String pageId;
	
	@Column(name="accesstoken")
	private String accessToken;
	
	@Column(name="pagename")
	private String pageName;
	private String username;
	private String picture;
	private String link;
	private Integer likes = 0;
	private Integer friends = 0;
	private Integer followers = 0;
	private Integer shares;
	private Integer talking_about_count;
	private String category;
	private String description;
	@Column(name="feedurl")
	private String feedUrl;
	private Boolean active;
	private int		feedpicktime;
	private Byte 	poststhreshold;
	@Column(name="multiplefeedurl")
	private Boolean multipleFeedUrl = false;
	@Column(name="push_all_article")
	private Boolean push_all_article = false;
	private String error;
	private String hashtags;
	@Column(name="hashtags_condition")
	private String hashtagsCondition;
	private Date createdate = new Date();
	private Date updatedate;
	
	@Column(name="headline")
	private String headline;
	
	public String getHeadline() {
		return headline;
	}
	public void setHeadline(String headline) {
		this.headline = headline;
	}
	@Column(name="utm_source")
	private String utmSource;
	
	@Column(name="utm_medium")
	private String utmMedium;
	
	@Column(name="utm_campaign")
	private String utmCampaign;
	
	@Column(name="push_time")
	private Float pushTime = 0F;
	
	private String email;
	private String remarks;
	
	// Possible values are PROFILE/PAGE
	private String type; 
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Website getWebsite() {
		return website;
	}
	public void setWebsite(Website website) {
		this.website = website;
	}
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getPageId() {
		return pageId;
	}
	public void setPageId(String pageId) {
		this.pageId = pageId;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getPageName() {
		return pageName;
	}
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public Integer getLikes() {
		return likes;
	}
	public void setLikes(Integer likes) {
		this.likes = likes;
	}
	public Integer getFriends() {
		return friends;
	}
	public void setFriends(Integer friends) {
		this.friends = friends;
	}
	public Integer getFollowers() {
		return followers;
	}
	public void setFollowers(Integer followers) {
		this.followers = followers;
	}
	public Integer getShares() {
		return shares;
	}
	public void setShares(Integer shares) {
		this.shares = shares;
	}
	public Integer getTalking_about_count() {
		return talking_about_count;
	}
	public void setTalking_about_count(Integer talking_about_count) {
		this.talking_about_count = talking_about_count;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getFeedUrl() {
		return feedUrl;
	}
	public void setFeedUrl(String feedUrl) {
		this.feedUrl = feedUrl;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public int getFeedpicktime() {
		return feedpicktime;
	}
	public void setFeedpicktime(int feedpicktime) {
		this.feedpicktime = feedpicktime;
	}
	public Byte getPoststhreshold() {
		return poststhreshold;
	}
	public void setPoststhreshold(Byte poststhreshold) {
		this.poststhreshold = poststhreshold;
	}
	public Boolean getMultipleFeedUrl() {
		return multipleFeedUrl;
	}
	public void setMultipleFeedUrl(Boolean multipleFeedUrl) {
		this.multipleFeedUrl = multipleFeedUrl;
	}
	public Boolean getPush_all_article() {
		return push_all_article;
	}
	public void setPush_all_article(Boolean push_all_article) {
		this.push_all_article = push_all_article;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getHashtags() {
		return hashtags;
	}
	public void setHashtags(String hashtags) {
		this.hashtags = hashtags;
	}
	public String getHashtagsCondition() {
		return hashtagsCondition;
	}
	public void setHashtagsCondition(String hashtagsCondition) {
		this.hashtagsCondition = hashtagsCondition;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	public Date getUpdatedate() {
		return updatedate;
	}
	public void setUpdatedate(Date updatedate) {
		this.updatedate = updatedate;
	}
	public String getUtmSource() {
		return utmSource;
	}
	public void setUtmSource(String utmSource) {
		this.utmSource = utmSource;
	}
	public String getUtmMedium() {
		return utmMedium;
	}
	public void setUtmMedium(String utmMedium) {
		this.utmMedium = utmMedium;
	}
	public String getUtmCampaign() {
		return utmCampaign;
	}
	public void setUtmCampaign(String utmCampaign) {
		this.utmCampaign = utmCampaign;
	}
	public Float getPushTime() {
		return pushTime;
	}
	public void setPushTime(Float pushTime) {
		this.pushTime = pushTime;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
