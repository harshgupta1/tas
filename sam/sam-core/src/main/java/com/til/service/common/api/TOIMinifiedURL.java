/**
 * 
 */
package com.til.service.common.api;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Shahan.Shah
 *
 */
@XmlRootElement(name="response")
public class TOIMinifiedURL {
	
	private String status_code;
	private String status_txt;
	private Data data;
	
	public String getStatus_code() {
		return status_code;
	}
	public void setStatus_code(String status_code) {
		this.status_code = status_code;
	}
	public String getStatus_txt() {
		return status_txt;
	}
	public void setStatus_txt(String status_txt) {
		this.status_txt = status_txt;
	}
	public Data getData() {
		return data;
	}
	public void setData(Data data) {
		this.data = data;
	}

	public static class Data
	{
		private String url;
		private String hash;
		private String long_urltype;
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getHash() {
			return hash;
		}
		public void setHash(String hash) {
			this.hash = hash;
		}
		public String getLong_urltype() {
			return long_urltype;
		}
		public void setLong_urltype(String long_urltype) {
			this.long_urltype = long_urltype;
		}
		
	}

}
