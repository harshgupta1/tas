/**
 * 
 */
package com.til.service.common.dao.hibernate.entity;

import java.io.Serializable;
import java.util.Date;

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
@NamedQueries({
	@NamedQuery (
	    name = "findByTopicPage",
	    query = "SELECT t FROM TopicPageHistory t where t.topicPage.id = :topicpageid and t.createdate > Date(now()) order by createdate desc"
	),
	@NamedQuery(
			 name = "findByTopicPageAndDate",
			 query = "SELECT t FROM TopicPageHistory t where t.topicPage.id = :topicpageid and date(t.createdate)=:createdate order by createdate desc"
	)
})
/**
 * @author Harsh.Gupta
 */

@Entity
@Table(name="topicpage_history")
public class TopicPageHistory implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer  id ;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "topicpageid")
	private TopicPage topicPage;
	
	private Integer likes;
	private Integer shares;
	private Integer dislikes;
	
	private Date createdate = new Date();
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public TopicPage getTopicPage() {
		return topicPage;
	}
	public void setTopicPage(TopicPage topicPage) {
		this.topicPage = topicPage;
	}
	public Integer getLikes() {
		return likes;
	}
	public void setLikes(Integer likes) {
		this.likes = likes;
	}
	public Integer getShares() {
		return shares;
	}
	public void setShares(Integer shares) {
		this.shares = shares;
	}
	public Integer getDislikes() {
		return dislikes;
	}
	public void setDislikes(Integer dislikes) {
		this.dislikes = dislikes;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
}
