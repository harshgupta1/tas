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
		    name = "topicPageAdditionalFeeds.findByTopicPageId",
		    query = "from TopicPageAdditionalFeeds t WHERE t.topicPage.id = :topicpageid"
	)
})

@Entity
@Table(name="topicpage_additionalfeeds")
public class TopicPageAdditionalFeeds implements Serializable {
	
	private static final long serialVersionUID = -8699283700706071856L;

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer  id ;
	
	@Column(name="topicurl" ,nullable= false)
	private String topicUrl;
	
	@Column(name="feedurl" ,nullable= false)
	private String feedUrl;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "topicpageid")
	private TopicPage topicPage;
	
	private Date createdate = new Date();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTopicUrl() {
		return topicUrl;
	}

	public void setTopicUrl(String topicUrl) {
		this.topicUrl = topicUrl;
	}

	public String getFeedUrl() {
		return feedUrl;
	}

	public void setFeedUrl(String feedUrl) {
		this.feedUrl = feedUrl;
	}

	public TopicPage getTopicPage() {
		return topicPage;
	}

	public void setTopicPage(TopicPage topicPage) {
		this.topicPage = topicPage;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
}
