package com.til.service.common.dao.hibernate.entity;

import java.io.Serializable;

public class UserRole implements Serializable{
	
	private static final long serialVersionUID = -6740628212699200809L;
	
	private User user;
	private Role role;
	
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
}
