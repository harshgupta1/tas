package com.til.service.google.api;

import java.io.Serializable;

/**
 * Model class containing a Google plus refresh token response information.
 * 
 * @author Sanjay Gupta
 */
@SuppressWarnings("serial")
public class GoogleRefTokenResp implements Serializable {

	private String access_token;
	private String id_token;
	private String token_type;
	private String expires_in;

	public String getId_token() {
		return id_token;
	}

	public void setId_token(String id_token) {
		this.id_token = id_token;
	}

	public String getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getToken_type() {
		return token_type;
	}

	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}

}
