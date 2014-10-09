/**
 * 
 */
package com.til.service.common.dao.hibernate.entity;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
import com.til.service.common.api.Item;

/**
 * @author Harsh.Gupta
 */
@NamedQueries({
	//@Deprecated
	@NamedQuery (
	    name = "findArticleAndTopicPageByActive",
	    query = "SELECT a,t FROM Article a join a.topicPage t where t.active = :active"
	),
	//@Deprecated
	@NamedQuery (
		    name = "findArticleAndTopicPageByActiveAndProcessed",
		    query = "SELECT a,t FROM Article a join a.topicPage t where t.active = :active and" +
		    		" a.processed=:processed and (t.likes > 0 or t.friends > 0 or t.followers > 0)"
	),
	//@Deprecated
	@NamedQuery (
		    name = "findArticleAndTopicPageByWebsiteIdAndActiveAndProcessed",
		    query = "SELECT a,t FROM Article a join a.topicPage t where t.website.id=:websiteid" +
		    		" and t.active = :active and a.processed=:processed and (t.likes > 0 or t.friends > 0 or t.followers > 0)"
	),
	@NamedQuery (
		    name = "findArticleAndTopicPageBy_website_active_processed_rowlock",
		    query = "SELECT a,t FROM Article a join a.topicPage t where t.website.id=:websiteid and t.active = :active" +
		    		" and a.processed=:processed and a.rowlock=:rowlock and (t.likes > 0 or t.friends > 0 or t.followers > 0) and a.createdate > :createdate and a.scheduled=0"
	),
	@NamedQuery (
		    name = "findArticleAndTopicPageBy_website_active_processed_rowlock_scheduled",
		    query = "SELECT a,t FROM Article a join a.topicPage t where t.website.id=:websiteid and t.active = :active" +
		    		" and a.processed=:processed and a.rowlock=:rowlock and (t.likes > 0 or t.friends > 0 or t.followers > 0) and a.scheduled=:scheduled and a.feedtimestamp<:feedtimestamp"
	),
	@NamedQuery (
		    name = "article.findAllOrdered",
		    query = "SELECT a,t,w FROM Article a join a.topicPage t join t.website w where useindex(a, idx_createdate) is true and 1=1 order by a.createdate desc"
	),
	@NamedQuery (
		    name = "article.findAllOrderedByTopic",
		    query = "SELECT a,t,w FROM Article a join a.topicPage t join t.website w where t.entityName like :topicname order by a.createdate desc"
	),
	/**
	 * Queries to find scheduled articles
	 * 
	 * @author girish.gaurav
	 *
	 */
	@NamedQuery (
		    name = "article.findAllScheduledOrdered",
		    query = "SELECT a,t,w FROM Article a join a.topicPage t join t.website w where useindex(a, idx_createdate) is true and a.scheduled=true order by a.createdate desc"
	),
	@NamedQuery (
		    name = "article.findAllScheduledOrderedByTopic",
		    query = "SELECT a,t,w FROM Article a join a.topicPage t join t.website w where t.entityName like :topicname and a.scheduled=true order by a.createdate desc"
	),
	@NamedQuery (
		    name = "article.findAllScheduledOrderedByUserId",
		    query = "SELECT a,t,w FROM Article a join a.topicPage t join t.website w join w.userrole up join up.index u where u.id= :userid and a.scheduled=true order by a.createdate desc"
	),
	@NamedQuery (
		    name = "article.findAllScheduledOrderedByUserIdAndTopic",
		    query = "SELECT a,t,w FROM Article a join a.topicPage t join t.website w join w.userrole up join up.index u where u.id= :userid and t.entityName like :topicname and a.scheduled=true order by a.createdate desc"
	),
	@NamedQuery (
		    name = "article.countAllScheduledOrdered",
		    query = "SELECT count(a) FROM Article a where useindex(a, idx_createdate) is true and a.scheduled=true"
	),
	@NamedQuery (
		    name = "article.countAllScheduledOrderedByTopic",
		    query = "SELECT count(a) FROM Article a join a.topicPage t where t.entityName like :topicname and a.scheduled=true"
	),
	@NamedQuery (
		    name = "article.countAllScheduledOrderedByUserId",
		    query = "SELECT count(a) FROM Article a join a.topicPage t join t.website w join w.userrole up join up.index u where u.id= :userid and a.scheduled=true"
	),
	@NamedQuery (
		    name = "article.countAllScheduledOrderedByUserIdAndTopic",
		    query = "SELECT count(a) FROM Article a join a.topicPage t join t.website w join w.userrole up join up.index u where u.id= :userid and t.entityName like :topicname and a.scheduled=true"
	),
	/* End of scheduled articles queries */
	@NamedQuery (
		    name = "article.countAllOrdered",
		    query = "SELECT count(a) FROM Article a"
	),
	@NamedQuery (
		    name = "article.countAllOrderedByTopic",
		    query = "SELECT count(a) FROM Article a join a.topicPage t where t.entityName like :topicname"
	),
	@NamedQuery(
			name="article.countByUserId",
			query="select count(a) from Article a join a.website w join w.userrole up join up.index u where u.id= :userid"
	),//@depricate
	@NamedQuery(
			name="article.countByUserIdAndTopic",
			query="select count(t) from Article a join a.topicPage t join t.website w join w.userrole up join up.index u where u.id= :userid and t.entityName like :topicname"
	),
	@NamedQuery (
		    name = "article.findByIdLeftJoinWebsite",
		    query = "SELECT a FROM Article a left join fetch a.website w where a.id=:id"
	),
	@NamedQuery (
		    name = "article.findAllOrderedByUserId",
		    query = "SELECT a,t,w FROM Article a join a.topicPage t join t.website w join w.upjoin up where useindex(a,idx_createdate) is true and up.id= :userid order by a.createdate desc"
	),
	@NamedQuery (
		    name = "article.findAllOrderedByUserIdAndTopic",
		    query = "SELECT a,t,w FROM Article a join a.topicPage t join t.website w join w.upjoin up where up.id= :userid and t.entityName like :topicname order by a.createdate desc"
	),
	@NamedQuery(
			name = "article.findArticleByTopicPageId",
			query = "select a from Article a where a.topicPage.id = :topicpageid order by a.createdate desc"
	),
	//@Deprecated
	@NamedQuery(
			name = "article.findArticleByTopicPageIdDateAndMessage",
			query = "select a from Article a where a.topicPage.id = :topicpageid and a.createdate > :createdate and a.message=:message"
	),
	@NamedQuery(
			name = "article.findArticleByTopicPageIdDate",
			query = "select a from Article a where a.topicPage.id = :topicpageid and a.createdate > :createdate"
	)
})
@Entity
@Table(name="article")
public class Article implements Serializable {
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer  id ;
    
	@ManyToOne
	@JoinColumn(name = "topicpageid")
	private TopicPage topicPage;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "websiteid")
	private Website website;
	
	@Column(name="socialappid", nullable= false)
	private String socialappid;
	
	private String articleid;
	private String shortenerhash;
	private String shortenerurl;
	
	@Column(name="topicname", nullable= false)
	private String topicName;
	
	@Column(nullable= false)
	private String message;
	
	@Column(nullable= false)
	private String url;
	
	private Boolean processed;
	@Column(nullable=false)
	private Boolean scheduled=false;
	@Column(name="notificationmail", nullable=false)
	private boolean notificationMail=false;
	@Column(name="email", nullable=false)
	private String email;
	
	private long viewcount;
	private boolean rowlock;
	
	private Date createdate = new Date();
	private Date updatedate = createdate;
	private Date feedtimestamp;
	private Boolean apireq;
	private String postedphotoid;

	public Article()
	{
		
	}
	
	public Article(TopicPage topicPage, String topicName, String socialappid, Website website, String message,
				String url, boolean processed, Date feedtimestamp, long viewcount)
	{
		this.topicPage = topicPage;
		this.topicName = topicName;
		this.socialappid = socialappid;
		this.website = website;
		this.message = message;
		this.url = url;
		this.processed = processed;
		this.feedtimestamp = feedtimestamp;
		this.viewcount = viewcount;
	}
	
	public Article(TopicPage topicPage, Website website, Item item, boolean processed, long viewcount, 
			boolean pushtitle, boolean pushdifftitle) 
	{
		this.topicPage = topicPage;
		this.topicName = topicPage.getEntityName();
		this.socialappid = website.getSocialAppId();
		this.website = website;
		this.url = item.getUrl();
		this.processed = processed;
		this.feedtimestamp = item.getPubDate();
		this.viewcount = viewcount;
		Object returnObject=null;
		String headline=topicPage.getHeadline();
		String messagePattern=headline;
		String finalMessage=null;
		
		if(messagePattern!=null){				
			Pattern p = Pattern.compile("#(.*?)\\|");
			Matcher m = p.matcher(messagePattern);
				 while(m.find()){
				    String methodname=m.group(1);
				    String mname="get"+methodname;
				    String replacevalue=mname.replace("get","#").trim();
				    try {
						Method[] methods = Item.class.getMethods();
						for (Method method : methods) {
							if (method.getName().startsWith(mname)&& method.getGenericParameterTypes().length == 0) {				
								returnObject = method.invoke(item);
								headline=headline.replace(replacevalue,returnObject.toString());
								headline=headline.replace("|","");
								
							}
						}
					}
					
					catch(Exception e)
					{
						e.printStackTrace();
					} 
				    
				 }
				 
				 finalMessage=headline;
			}
		
		if (pushtitle) {
								
			 if(messagePattern!=null)
			{
				this.message=finalMessage;
			}
			else {
				this.message = item.getTitle();
				String hashTag = getTwitterHashTag(topicPage);
				if (hashTag != null)
					this.message = hashTag + this.message;
			}
		}
		else if (pushdifftitle) {
			 
			if(messagePattern!=null)
			{
				this.message=finalMessage;
			}
			else 
			{
				this.message = item.getTitle().trim().replaceAll("[ ]", "-");
				this.message = this.message.replaceAll("[^a-zA-Z0-9-/]*", "");
				if (item.getUrl().indexOf(message) > 0) {
					// Anuj's REQ: If mod. title matches in url then replace it
					// with blank message
					this.message = "";
				} else {
						this.message = item.getTitle(); // ELSE push original title
				}

			}

		}

	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getPostedphotoid() {
		return postedphotoid;
	}

	public void setPostedphotoid(String postedphotoid) {
		this.postedphotoid = postedphotoid;
	}

	public Boolean getApireq() {
		return apireq;
	}

	public void setApireq(Boolean apireq) {
		this.apireq = apireq;
	}
	public TopicPage getTopicPage() {
		return topicPage;
	}
	public void setTopicPage(TopicPage topicPage) {
		this.topicPage = topicPage;
	}
	public Website getWebsite() {
		return website;
	}
	public void setWebsite(Website website) {
		this.website = website;
	}
	public String getSocialappid() {
		return socialappid;
	}
	public void setSocialappid(String socialappid) {
		this.socialappid = socialappid;
	}
	public String getArticleid() {
		return articleid;
	}
	public void setArticleid(String articleid) {
		this.articleid = articleid;
	}
	public void setArticleid(String articleid, String application) {
		this.articleid = articleid.substring(articleid.lastIndexOf("/")+1);
		if("TOI".equals(application))
		{
			if(this.articleid.indexOf(".cms") > 0)
			{
				this.articleid = this.articleid.substring(0,this.articleid.indexOf(".cms"));

			}
			// Extra check for systems where they already contain just ? or some query parameters
			else if(this.articleid.indexOf("?") > 0)
			{
				this.articleid = this.articleid.substring(0,this.articleid.indexOf("?"));

			}
		}
	}
	public String getShortenerhash() {
		return shortenerhash;
	}
	public void setShortenerhash(String shortenerhash) {
		this.shortenerhash = shortenerhash;
	}
	public String getShortenerurl() {
		return shortenerurl;
	}
	public void setShortenerurl(String shortenerurl) {
		this.shortenerurl = shortenerurl;
	}

	public String getTopicName() {
		return topicName;
	}
	public void setTopicName(String topicname) {
		this.topicName = topicname;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Boolean getProcessed() {
		return processed;
	}
	public void setProcessed(Boolean processed) {
		this.processed = processed;
	}
	public Boolean getScheduled() {
		return scheduled;
	}

	public void setScheduled(Boolean scheduled) {
		this.scheduled = scheduled;
	}

	public boolean isNotificationMail() {
		return notificationMail;
	}

	public void setNotificationMail(boolean notificationMail) {
		this.notificationMail = notificationMail;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public long getViewcount() {
		return viewcount;
	}
	public void setViewcount(long viewcount) {
		this.viewcount = viewcount;
	}
	public boolean isRowlock() {
		return rowlock;
	}
	public void setRowlock(boolean rowlock) {
		this.rowlock = rowlock;
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
	public Date getFeedtimestamp() {
		return feedtimestamp;
	}
	public void setFeedtimestamp(Date feedtimestamp) {
		this.feedtimestamp = feedtimestamp;
	}
	
	private String getTwitterHashTag(TopicPage topicPage) {
		boolean pushTags = false;
		if(topicPage.getHashtagsCondition() != null  && !"".equals(topicPage.getHashtagsCondition()) )
		{
			if(getUrl().indexOf(topicPage.getHashtagsCondition()) != -1)
			{
				pushTags = true;
			}
		}
		else if((topicPage.getHashtags()!=null && !"".equals(topicPage.getHashtags()))){
			pushTags = true;
		}
		if(pushTags)
		{
			if(topicPage.getHashtags().indexOf(" ")!=-1){
				String hashtag = topicPage.getHashtags();
				hashtag = hashtag.replaceAll("( )+", " ").trim();
				String[] splitTag=hashtag.split(" ");
				int num=(int) Math.floor(Math.random()*splitTag.length);
				return splitTag[num];
			}else{
				return topicPage.getHashtags().trim();
			}
		}
		return null;
	}
	
	public void addUtmParameters(TopicPage topicPage) {
		// If ? appears at the end
		int questionpos = this.url.indexOf("?");
		if(questionpos != -1 && this.url.length() == questionpos + 1)
		{
			// DO Nothing
		}
		// If there are some query parameters at the end of ?
		else if(questionpos != -1 && this.url.length() > questionpos + 1)
		{
			this.url = this.url + "&";
		}
		else
		{
			this.url = this.url + "?";
		}
		this.url = this.url
				+ "utm_source="
				+ (topicPage.getUtmSource() != null
						&& !topicPage.getUtmSource().isEmpty() ? topicPage
						.getUtmSource() : topicPage.getWebsite()
						.getSocialAppName().toLowerCase()
						+ ".com")
				+ "&utm_medium="
				+ (topicPage.getUtmMedium() != null
						&& !topicPage.getUtmMedium().isEmpty() ? topicPage
						.getUtmMedium() : "referral")
				+ "&utm_campaign="
				+ (topicPage.getUtmCampaign() != null
						&& !topicPage.getUtmCampaign().isEmpty() ? topicPage
						.getUtmCampaign() : topicPage.getEntityName());
	}
}
