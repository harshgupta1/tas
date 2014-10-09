package com.til.service.linkedin.api;
import java.io.Serializable;
import java.util.Date;

import com.til.service.facebook.api.Error;

/**
 * Model class containing a LinkedIn user's profile information.
 * @author Sanjay Gupta
 */
@SuppressWarnings("serial")
public class LinkedInProfile implements Serializable {

	private String id;

	private String name;
	
	private String firstName;
	
	private String lastName;
	
	private String headline;

	private String locale;

	private String link;

	private String emailAddress;

	private String pictureUrl;	

	private String numRecommenders;
	
	private String numConnections;
	
	private String publicProfileUrl;
	
	private String formattedName;
	
	private String message;
	
	private Error error;

	private String summary;

	private Integer timezone;
	
	private Date updated_time;

	private Boolean verified;

	public LinkedInProfile()
	{
		
	}
	
	public void setId(String id) {
		this.id = id;
	}	

	public void setName(String name) {
		this.name = name;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * The user's Linkedin ID
	 * @return The user's Linked ID
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * The user's full name
	 * @return The user's full name
	 */
	public String getName() {
		return firstName+" "+lastName;
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
	
	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	
	/**
	 * A link to the user's profile on LinkedIn
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


	
	public void setLink(String link) {
		this.link = link;
	}

	
	

	public String getHeadline() {
		return headline;
	}

	public void setHeadline(String hedline) {
		this.headline = hedline;
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
	
	public String getNumConnections() {
		return numConnections;
	}

	public void setNumConnections(String numConnections) {
		this.numConnections = numConnections;
	}

	public String getPublicProfileUrl() {
		return publicProfileUrl;
	}

	public void setPublicProfileUrl(String publicProfileUrl) {
		this.publicProfileUrl = publicProfileUrl;
	}

	public String getFormattedName() {
		return formattedName;
	}

	public void setFormattedName(String formattedName) {
		this.formattedName = formattedName;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public String getNumRecommenders() {
		return numRecommenders;
	}

	public void setNumRecommenders(String numRecommenders) {
		this.numRecommenders = numRecommenders;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Error getError() {
		return error;
	}

	public void setError(Error error) {
		this.error = error;
	}

}

