/**
 * 
 */
package com.til.service.common.api;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author girish.gaurav
 * 
 */
@XmlRootElement(name = "user")
public class TwitterUser {

	private long id;

	private String name;

	private String screenName;

	private long followersCount;

	/**
	 * 
	 */
	public TwitterUser() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public long getFollowersCount() {
		return followersCount;
	}

	public void setFollowersCount(long followersCount) {
		this.followersCount = followersCount;
	}

}
