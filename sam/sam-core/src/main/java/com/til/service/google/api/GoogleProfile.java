package com.til.service.google.api;
import java.io.Serializable;
import java.util.Date;

import com.til.service.facebook.api.Error;

/**
 * Model class containing a Google plus user's profile information.
 * @author Sanjay Gupta
 */
@SuppressWarnings("serial")
public class GoogleProfile implements Serializable {

	private String id;

	private String name;

	private String first_name;
	
	private String last_name;
	
	private Error error;
	
	private String gender;

	private String locale;

	private String link;

	private String email;
	
	private String pictureurl;

	private Integer timezone;
	
	private Date updated_time;

	private Boolean verified;

	public GoogleProfile()
	{
		
	}
	
	public void setId(String id) {
		this.id = id;
	}	

	public void setName(String name) {
		this.name = name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	/**
	 * The user's google ID
	 * @return The user's google ID
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * The user's full name
	 * @return The user's full name
	 */
	public String getName() {
		return name;
	}

	/**
	 * The user's gender
	 * @return the user's gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * The user's locale
	 * @return the user's locale
	 */
	public String getLocale() {
		return locale;
	}

	/**
	 * The user's email address.
	 * Available only with "email" permission.
	 * @return The user's email address
	 */
	public String getEmail() {
	    return email;
    }
	
	/**
	 * A link to the user's profile on Google.
	 * Available only if requested by an authenticated user.
	 * @return the user's profile link or null if requested anonymously
	 */
	public String getLink() {
		return link;
	}

	/**
	 * The user's account verification status.
	 * Available only if requested by an authenticated user.
	 * @return true if the profile has been verified, false if it has not, or null if not available.
	 */
	public Boolean isVerified() {
		return verified;
	}
	

	public Boolean getVerified() {
		return verified;
	}

	public void setVerified(Boolean verified) {
		this.verified = verified;
	}


	public String getFirst_name() {
		return first_name;
	}


	public String getLast_name() {
		return last_name;
	}

	public void setLink(String link) {
		this.link = link;
	}

	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPictureurl() {
		return pictureurl;
	}

	public void setPictureurl(String pictureurl) {
		this.pictureurl = pictureurl;
	}

	public Integer getTimezone() {
		return timezone;
	}

	public void setTimezone(Integer timezone) {
		this.timezone = timezone;
	}

	public Date getUpdated_time() {
		return updated_time;
	}

	public void setUpdated_time(Date updated_time) {
		this.updated_time = updated_time;
	}

	public Error getError() {
		return error;
	}

	public void setError(Error error) {
		this.error = error;
	}

}

