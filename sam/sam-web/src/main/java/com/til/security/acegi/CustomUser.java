package com.til.security.acegi;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class CustomUser extends User {
	
	private Object user; 

	public CustomUser(String username, String password, boolean isEnabled,
			GrantedAuthority[] authorities, Object user) {
		
		super(username, password, isEnabled, true, true, true, authorities);
		this.setUser(user);
	}

	public CustomUser(String username, String password, boolean isEnabled,
			GrantedAuthority[] arrayAuths) {
		super(username, password, isEnabled, true, true, true, arrayAuths);
		
	}

	public Object getUser() {
		return user;
	}

	public void setUser(Object user) {
		this.user = user;
	}
}
