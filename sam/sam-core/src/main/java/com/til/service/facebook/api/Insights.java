package com.til.service.facebook.api;

import com.til.service.common.api.Paging;

public class Insights {
	
	private Insight[] data;
	private Paging paging;
	private Error error;
	
	public Error getError() {
		return error;
	}

	public void setError(Error error) {
		this.error = error;
	}
	
	public Insight[] getData() {
		return data;
	}

	public void setData(Insight[] data) {
		this.data = data;
	}
	
	public Paging getPaging() {
		return paging;
	}

	public void setPaging(Paging paging) {
		this.paging = paging;
	}	
	
}
