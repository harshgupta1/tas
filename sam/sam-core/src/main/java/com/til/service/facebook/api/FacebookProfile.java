package com.til.service.facebook.api;
import java.io.Serializable;
import java.util.Date;

/**
 * Model class containing a Facebook user's profile information.
 * @author Harsh Gupta
 */
@SuppressWarnings("serial")
public class FacebookProfile implements Serializable {

	private String id;

	private String username;

	private String name;

	private String first_name;
	
	private String middle_name;
	
	private String last_name;

	private String gender;

	private String locale;

	private String link;

	private String website;

	private String email;
	
	private String message;	

	private String third_party_id;

	private Integer timezone;
	
	private Date updated_time;

	private Boolean verified;

	private String about;
	
	private String bio;
	
	private String birthday;
	
	private Reference location;
	
	private Reference hometown;
	
	private String[] interested_in;

	private Reference[] languages;
	
	private Reference[] sports;
	
	private Reference[] favorite_teams;
	
	private Reference[] favorite_athletes;
	
	private Error error;
	
	private String religion;

	private String political;

	private String quotes;

	private String relationship_status;

	private Reference significant_other;
	
	private WorkEntry[] work;
	
	private EducationEntry[] education;
	
	public FacebookProfile()
	{
		
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public void setMiddle_name(String middle_name) {
		this.middle_name = middle_name;
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
	 * The user's Facebook ID
	 * @return The user's Facebook ID
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * The user's Facebook username
	 * @return the user's Facebook username
	 */
	public String getUsername() {
		return username;
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
	 * A link to the user's profile on Facebook.
	 * Available only if requested by an authenticated user.
	 * @return the user's profile link or null if requested anonymously
	 */
	public String getLink() {
		return link;
	}

	/**
	 * A link to the user's personal website. Available only with "user_website"
	 * or "friends_website" permission.
	 * 
	 * @return a link to the user's personal website.
	 */
	public String getWebsite() {
		return website;
	}

	/**
	 * The user's timezone offset from UTC.
	 * Available only for the authenticated user.
	 * @return the user's timezone offset from UTC or null if the user isn't the authenticated user
	 */
	public Integer getTimezone() {
		return timezone;
	}
	
	/**
	 * The user's account verification status.
	 * Available only if requested by an authenticated user.
	 * @return true if the profile has been verified, false if it has not, or null if not available.
	 */
	public Boolean isVerified() {
		return verified;
	}
	
	/**
	 * The user's brief about blurb.
	 * Available only with "user_about_me" permission for the authenticated user or "friends_about_me" for the authenticated user's friends.
	 * @return the user's about blurb, if available.
	 */
	public String getAbout() {
		return about;
	}

	/**
	 * The user's bio.
	 * Available only with "user_about_me" permission for the authenticated user.
	 * @return the user's bio, if available.
	 */
	public String getBio() {
		return bio;
	}
	
	/**
	 * The user's birthday.
	 * Available only with "user_birthday" permission for the authentication user or "friends_birthday" permission for the user's friends.
	 * @return the user's birthday
	 */
	public String getBirthday() {
		return birthday;
	}
	
	/**
	 * The user's location.
	 * Available only with "user_location" or "friends_location" permission.
	 * @return a {@link Reference} to the user's location, if available
	 */
	public Reference getLocation() {
		return location;
	}
	
	/**
	 * The user's hometown.
	 * Available only with "user_hometown" or "friends_hometown" permission.
	 * @return a {@link Reference} to the user's hometown, if available
	 */
	public Reference getHometown() {
		return hometown;
	}
	
	/**
	 * A list of references to languages the user claims to know.
	 * @return a list of {@link Reference} to languages the user knows, if available.
	 */
	public Reference[] getLanguages() {
		return languages;
	}
	
	/**
	 * A list of references to sports the user plays
	 * @return a list of {@link Reference}s to sports the user plays, if available.
	 */
	public Reference[] getSports() {
		return sports;
	}
	
	/**
	 * The user's religion. 
	 * Available only with "user_religion_politics" or "friends_religion_politics" permission.
	 * @return the user's religion, if available.
	 */
	public String getReligion() {
		return religion;
	}

	/**
	 * The user's political affiliation. 
	 * Available only with "user_religion_politics" or "friends_religion_politics" permission.
	 * @return the user's political affiliation, if available.
	 */
	public String getPolitical() {
		return political;
	}

	/**
	 * The user's quotations. 
	 * Available only with "user_about_me" permission.
	 * @return the user's quotations, if available.
	 */
	public String getQuotes() {
		return quotes;
	}

	/**
	 * The user's work history.
	 * Available only with "user_work_history" or "friends_work_history" permission.
	 * @return a list of {@link WorkEntry} items, one for each entry in the user's work history.
	 */
	public WorkEntry[] getWork() {
		return work;
	}
	
	/**
	 * The user's education history.
	 * Available only with "user_education_history" or "friends_education_history" permission.
	 * @return a list of {@link EducationEntry} items, one for each entry in the user's education history.
	 */
	public EducationEntry[] getEducation() {
		return education;
	}

	public String getThird_party_id() {
		return third_party_id;
	}

	public void setThird_party_id(String third_party_id) {
		this.third_party_id = third_party_id;
	}

	public Date getUpdated_time() {
		return updated_time;
	}

	public void setUpdated_time(Date updated_time) {
		this.updated_time = updated_time;
	}

	public Boolean getVerified() {
		return verified;
	}

	public void setVerified(Boolean verified) {
		this.verified = verified;
	}

	public String[] getInterested_in() {
		return interested_in;
	}

	public void setInterested_in(String[] interested_in) {
		this.interested_in = interested_in;
	}

	public Reference[] getFavorite_teams() {
		return favorite_teams;
	}

	public void setFavorite_teams(Reference[] favorite_teams) {
		this.favorite_teams = favorite_teams;
	}

	public Reference[] getFavorite_athletes() {
		return favorite_athletes;
	}

	public void setFavorite_athletes(Reference[] favorite_athletes) {
		this.favorite_athletes = favorite_athletes;
	}

	public String getRelationship_status() {
		return relationship_status;
	}

	public void setRelationship_status(String relationship_status) {
		this.relationship_status = relationship_status;
	}

	public Reference getSignificant_other() {
		return significant_other;
	}

	public void setSignificant_other(Reference significant_other) {
		this.significant_other = significant_other;
	}

	public String getFirst_name() {
		return first_name;
	}

	public String getMiddle_name() {
		return middle_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setTimezone(Integer timezone) {
		this.timezone = timezone;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public void setLocation(Reference location) {
		this.location = location;
	}

	public void setHometown(Reference hometown) {
		this.hometown = hometown;
	}

	public void setLanguages(Reference[] languages) {
		this.languages = languages;
	}

	public void setSports(Reference[] sports) {
		this.sports = sports;
	}

	public void setReligion(String religion) {
		this.religion = religion;
	}

	public void setPolitical(String political) {
		this.political = political;
	}

	public void setQuotes(String quotes) {
		this.quotes = quotes;
	}

	public void setWork(WorkEntry[] work) {
		this.work = work;
	}

	public void setEducation(EducationEntry[] education) {
		this.education = education;
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

