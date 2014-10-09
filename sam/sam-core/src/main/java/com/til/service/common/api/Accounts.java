package com.til.service.common.api;

import com.til.service.facebook.api.Error;
import com.til.service.facebook.api.PageDetail;


public class Accounts {
	
	private PageDetail profileData;
	
	private PageDetail[] data;
	
	private Error error;
	
	private Paging paging;
	
	public PageDetail getProfileData() {
		return profileData;
	}

	public void setProfileData(PageDetail profileData) {
		this.profileData = profileData;
	}

	public PageDetail[] getData() {
		return data;
	}

	public void setData(PageDetail[] data) {
		this.data = data;
	}

	public Paging getPaging() {
		return paging;
	}

	public void setPaging(Paging paging) {
		this.paging = paging;
	}	
	
	/**
	 * @return the error
	 */
	public Error getError() {
		return error;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(Error error) {
		this.error = error;
	}
	
}
