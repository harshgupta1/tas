/**
 * 
 */
package com.til.service.facebook.api;

/**
 * @author girish.gaurav
 * 
 */
public class Error {

	private String type;
	private String message;
	private int code;
	private String errorCode;
	private int error_subcode;
	// This is a JSON
	private String error_data;

	/**
	 * 
	 */
	public Error() {
	}
  
	
	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getError_subcode() {
		return error_subcode;
	}

	public void setError_subcode(int error_subcode) {
		this.error_subcode = error_subcode;
	}

	public String getError_data() {
		return error_data;
	}

	public void setError_data(String error_data) {
		this.error_data = error_data;
	}
	
}
