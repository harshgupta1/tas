package com.til.service.common.dao.hibernate.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

//@Entity
//@Table(name = "user_permission")
public class WebsiteRole implements Serializable {

	//@Id
	/*private UserWebsiteRolePK primaryKey = new UserWebsiteRolePK();*/
	/*@Column(name = "userid")
	private User user;*/
	
	//@Column(name = "websiteid")
	private Website website;
	//@Column(name = "roleid")
	private Role role;

	/*public Website getWebsite() {
		return primaryKey.getWebsite();
	}

	public void setWebsite(Website website) {
		primaryKey.setWebsite(website);
	}

	public Role getRole() {
		return primaryKey.getRole();
	}

	public void setRole(Role role) {
		primaryKey.setRole(role);
	}*/
	
	public Website getWebsite() {
		return website;
	}

	public void setWebsite(Website website) {
		this.website = website;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
	/*public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}*/
	
	//@Embeddable
	private class UserWebsiteRolePK implements Serializable {
		//@ManyToOne
		private User user;
		//@ManyToOne
		private Website website;
		//@ManyToOne
		private Role role;

		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}

		public Website getWebsite() {
			return website;
		}

		public void setWebsite(Website website) {
			this.website = website;
		}

		public Role getRole() {
			return role;
		}

		public void setRole(Role role) {
			this.role = role;
		}
	}

}
