package com.til.service.facebook.api;

import java.io.Serializable;

import com.til.service.common.api.Paging;

/**
 * 
 * @author girish.gaurav
 * 
 */
public class Followers implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4408496505546876153L;

	private Reference[] data;

	private Paging paging;

	private Summary summary;

	private Error error;

	public Followers() {
	}

	public Reference[] getData() {
		return data;
	}

	public void setData(Reference[] data) {
		this.data = data;
	}

	public Paging getPaging() {
		return paging;
	}

	public void setPaging(Paging paging) {
		this.paging = paging;
	}

	public Summary getSummary() {
		return summary;
	}

	public void setSummary(Summary summary) {
		this.summary = summary;
	}

	public Error getError() {
		return error;
	}

	public void setError(Error error) {
		this.error = error;
	}

	public static class Summary {
		private int total_count = 0;

		public Summary() {
		}

		public int getTotal_count() {
			return total_count;
		}

		public void setTotal_count(int total_count) {
			this.total_count = total_count;
		}

	}
}
