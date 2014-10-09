/**
 * 
 */
package com.til.service.common.dao.hibernate.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.MapKeyJoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

/**
 * @author Harsh.Gupta
 */
@NamedQueries({
@NamedQuery (
	    name = "findByActive",
	    query = "SELECT w FROM Website w WHERE w.active = :active"
),
@NamedQuery (
	    name = "findByUserId",
	    query = "select w from Website w join w.userrole up join up.index u where u.id= :userid"
),
@NamedQuery (
	    name = "findBySiteCodeAndSocialcode",
	    query = "SELECT w FROM Website w WHERE w.sitecode = :sitecode and w.socialcode= :socialcode and w.active = :active"
),
@NamedQuery (
	    name = "findBySiteCodeSocialcodeAndSocialApp",
	    query = "SELECT w FROM Website w WHERE w.socialAppName = :socialAppName and w.sitecode = :sitecode and w.socialcode= :socialcode and w.active = :active"
),
@NamedQuery (
	    name = "findByWebSiteId",
	    query = "SELECT count(topicpage),w.socialAppName,w.userid,w.accessToken FROM Website w join w.topicPageSet topicpage WHERE w.id = :wid group by topicpage.website.id "
)
})
@Entity
@Table(name="website")
public class Website implements Serializable {
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Short  id ;
    
    @Column(name="socialappname", nullable=false)
    private String socialAppName;
    
	@NotBlank(message = "Name cannot be empty.")
    private String name;
    
    private String description;
    
    @NotBlank(message = "Application Id cannot be empty.")
    @Column(name="socialappid", nullable=false)
    private String socialAppId;
    
    @NotBlank(message = "API Secrect Key cannot be empty.")
    @Column(name="socialapisecret", nullable=false)
    private String socialApiSecret;
    
    @NotBlank(message = "Site Url cannot be empty.")
    @Column(name="siteurl", nullable=false)
    private String siteUrl;
    
    @Column(name="contactemail")
    private String contactEmail;
    
    private Boolean active;
    
    @Column(name="emailfromaddress")
    private String emailFromAddress;
    
    @Column(name="accesstoken")
    private String accessToken;
    
    @Column(name="accesstokensecret")
    private String accessTokenSecret;
    
    private String source;
    private boolean postasuser = false;  
    private String userid;
    private String pageviewsapi;
    private String shortenerapi;
    
    @NotBlank(message = "SiteCode cannot be empty.")
    @Column(name="sitecode", nullable=false)
    private String sitecode;
    
    @NotBlank(message = "SocialCode cannot be empty.")
    @Column(name="socialcode", nullable=false)
    private String socialcode;
    
    private int expires = 0;
    
    private Date expiresat;
    
    private boolean regeneratetoken = false;
    
    private Date createdate = new Date();
    private Date updatedate;
    
    @OneToMany(fetch=FetchType.LAZY)
	@JoinTable(name = "user_permission", joinColumns = { @JoinColumn(name = "websiteid") }, inverseJoinColumns ={@JoinColumn(name="roleid")})
	@MapKeyJoinColumn(name="userid",referencedColumnName="id")
	private Map<User, Role> userrole;
    
    @OneToMany(fetch=FetchType.LAZY)
	@JoinTable(name = "user_permission", joinColumns = { @JoinColumn(name = "websiteid") }, inverseJoinColumns ={@JoinColumn(name="userid")})
	@MapKeyJoinColumn(name="roleid")
	private Map<Role, User> upjoin;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "website", fetch = FetchType.LAZY)
    private Set<TopicPage> topicPageSet;
    
	public Short getId() {
		return id;
	}
	public void setId(Short id) {
		this.id = id;
	}
	public String getSocialAppName() {
		return socialAppName;
	}
	public void setSocialAppName(String socialAppName) {
		this.socialAppName = socialAppName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSocialAppId() {
		return socialAppId;
	}
	public void setSocialAppId(String socialAppId) {
		this.socialAppId = socialAppId;
	}
	public String getSocialApiSecret() {
		return socialApiSecret;
	}
	public void setSocialApiSecret(String socialApiSecret) {
		this.socialApiSecret = socialApiSecret;
	}
	public String getSiteUrl() {
		return siteUrl;
	}
	public void setSiteUrl(String siteUrl) {
		this.siteUrl = siteUrl;
	}
	public String getContactEmail() {
		return contactEmail;
	}
	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public String getEmailFromAddress() {
		return emailFromAddress;
	}
	public void setEmailFromAddress(String emailFromAddress) {
		this.emailFromAddress = emailFromAddress;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getAccessTokenSecret() {
		return accessTokenSecret;
	}
	public void setAccessTokenSecret(String accessTokenSecret) {
		this.accessTokenSecret = accessTokenSecret;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public boolean isPostasuser() {
		return postasuser;
	}
	public void setPostasuser(boolean postasuser) {
		this.postasuser = postasuser;
	}
	public String getPageviewsapi() {
		return pageviewsapi;
	}
	public void setPageviewsapi(String pageviewsapi) {
		this.pageviewsapi = pageviewsapi;
	}
	public String getShortenerapi() {
		return shortenerapi;
	}
	public void setShortenerapi(String shortenerapi) {
		this.shortenerapi = shortenerapi;
	}
	public String getSitecode() {
		return sitecode;
	}
	public void setSitecode(String sitecode) {
		this.sitecode = sitecode;
	}
	public String getSocialcode() {
		return socialcode;
	}
	public void setSocialcode(String socialcode) {
		this.socialcode = socialcode;
	}
	public int getExpires() {
		return expires;
	}
	public void setExpires(int expires) {
		this.expires = expires;
	}
	public Date getExpiresat() {
		return expiresat;
	}
	public void setExpiresat(Date expiresat) {
		this.expiresat = expiresat;
	}
	public boolean isRegeneratetoken() {
		return regeneratetoken;
	}
	public void setRegeneratetoken(boolean regeneratetoken) {
		this.regeneratetoken = regeneratetoken;
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
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public Map<User, Role> getUserrole() {
		return userrole;
	}
	public void setUserrole(Map<User, Role> userrole) {
		this.userrole = userrole;
	}
	public Set<TopicPage> getTopicPageSet() {
		return topicPageSet;
	}
	public void setTopicPageSet(Set<TopicPage> topicPageSet) {
		this.topicPageSet = topicPageSet;
	}
	public Map<Role, User> getUpjoin() {
		return upjoin;
	}
	public void setUpjoin(Map<Role, User> upjoin) {
		this.upjoin = upjoin;
	}
    
}
