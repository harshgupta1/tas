/**
 * 
 */
package com.til.service.common.dao.hibernate.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
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
		    name = "topicpagearticle.findAllOrdered",
		    query = "SELECT a,t,w,tpa FROM TopicPageArticle tpa join tpa.article a join a.topicPage t join t.website w where useindex(a,idx_createdate) is true and tpa.status='PROCESSED' order by a.createdate desc"
	),
	@NamedQuery (
		    name = "topicpagearticle.findAllOrderedByTopicAndStatus",
		    query = "SELECT a,t,w,tpa FROM TopicPageArticle tpa join tpa.article a join a.topicPage t join t.website w where tpa.status=:status and t.entityName like :topicname order by a.createdate desc"
	),
	@NamedQuery (
		    name = "topicpagearticle.findAllOrderedByTopic",
		    query = "SELECT a,t,w,tpa FROM TopicPageArticle tpa join tpa.article a join a.topicPage t join t.website w where t.entityName like :topicname order by a.createdate desc"
	),
	@NamedQuery (
		    name = "topicpagearticle.countAllOrdered",
		    query = "SELECT count(tpa) FROM TopicPageArticle tpa where tpa.status='PROCESSED'"
	),
	@NamedQuery (
		    name = "topicpagearticle.countAllOrderedByTopicAndStatus",
		    query = "SELECT count(tpa) FROM TopicPageArticle tpa join tpa.topicPage t where tpa.status=:status and t.entityName like :topicname"
	),
	@NamedQuery (
		    name = "topicpagearticle.countAllOrderedByTopic",
		    query = "SELECT count(tpa) FROM TopicPageArticle tpa join tpa.topicPage t where t.entityName like :topicname"
	),
	@NamedQuery (
		    name = "topicpagearticle.findAllOrderedByUserId",
		    query = "SELECT a,t,w,tpa FROM TopicPageArticle tpa join tpa.article a join a.topicPage t join t.website w join w.upjoin up where useindex(a,idx_createdate) is true and tpa.status='PROCESSED' and up.id= :userid order by a.createdate desc"
	),
	@NamedQuery (
		    name = "topicpagearticle.findAllOrderedByUserIdAndTopic",
		    query = "SELECT a,t,w,tpa FROM TopicPageArticle tpa join tpa.article a join a.topicPage t join t.website w join w.userrole up join up.index u where tpa.status='PROCESSED' and u.id= :userid and t.entityName like :topicname order by a.createdate desc"
	),
	@NamedQuery(
			name="topicpagearticle.countByUserId",
			query="select count(tpa) from TopicPageArticle tpa join tpa.article a join a.website w join w.userrole up join up.index u where tpa.status='PROCESSED' and u.id= :userid"
	),
	@NamedQuery(
			name="topicpagearticle.countByUserIdAndTopic",
			query="select count(tpa) from TopicPageArticle tpa join tpa.article a join a.topicPage t join t.website w join w.userrole up join up.index u where tpa.status='PROCESSED' and u.id= :userid and t.entityName like :topicname"
	),
	@NamedQuery(
			name="topicpagearticle.findAllBySocialAppAndCreateDate",
			query="select w,t,a,tpa from TopicPageArticle tpa join tpa.article a join a.topicPage t join t.website w where w.socialAppName = :name and t.active = true and tpa.status='PROCESSED' and a.createdate > :createdate order by a.createdate desc"
	)
})
@Entity
@Table(name="topicpage_article")
public class TopicPageArticle implements Serializable {
	
	private static final long serialVersionUID = -7140466275181965397L;

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long  id ;
	
	@ManyToOne
	@JoinColumn(name = "topicpageid")
	private TopicPage topicPage;
	
	@ManyToOne
	@JoinColumn(name = "articleid")
	private Article article;
	
	@Column(name="postid")
	private String postId;
	
	private String status;
	private String remarks;
	
	@Column(name="impressions_total")
	private int impressionsTotal;
	@Column(name="impressions_unique")
	private int impressionsUnique;
	@Column(name="clicks_unique")
	private int clicksUnique;
	@Column(name="clicks_total")
	private int clicksTotal;
	
	private Date createdate = new Date();
	private Date updatedate;
	
	public transient static String PROCESSED="PROCESSED";
	public transient static String PENDING="PENDING";
	public transient static String DELETED="DELETED";
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public TopicPage getTopicPage() {
		return topicPage;
	}
	public void setTopicPage(TopicPage topicPage) {
		this.topicPage = topicPage;
	}
	public Article getArticle() {
		return article;
	}
	public void setArticle(Article article) {
		this.article = article;
	}
	public String getPostId() {
		return postId;
	}
	public void setPostId(String postId) {
		this.postId = postId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public int getImpressionsTotal() {
		return impressionsTotal;
	}

	public void setImpressionsTotal(int impressionsTotal) {
		this.impressionsTotal = impressionsTotal;
	}

	public int getImpressionsUnique() {
		return impressionsUnique;
	}

	public void setImpressionsUnique(int impressionsUnique) {
		this.impressionsUnique = impressionsUnique;
	}

	public int getClicksUnique() {
		return clicksUnique;
	}

	public void setClicksUnique(int clicksUnique) {
		this.clicksUnique = clicksUnique;
	}

	public int getClicksTotal() {
		return clicksTotal;
	}

	public void setClicksTotal(int clicksTotal) {
		this.clicksTotal = clicksTotal;
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
}
