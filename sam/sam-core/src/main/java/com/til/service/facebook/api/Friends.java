/**
 * 
 */
package com.til.service.facebook.api;

import java.io.Serializable;

import com.til.service.common.api.Paging;

/**
 * @author girish.gaurav
 * 
 */
public class Friends implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5806492378807858117L;

	private Reference[] data;

	private Paging paging;

	private Error error;

	public Friends() {
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

	public Error getError() {
		return error;
	}

	public void setError(Error error) {
		this.error = error;
	}

}
