package com.til.service.facebook.api;

/**
 * Model class representing an entry in the user's work history.
 * @author Harsh Gupta
 */
public class WorkEntry {

	private Reference employer;
	
	private Reference location;
	
	private Reference position;
	
	private Reference[] with;
	
	private Reference from;
	
	private String start_date;

	private String end_date;

	public void setEmployer(Reference employer) {
		this.employer = employer;
	}

	public void setLocation(Reference location) {
		this.location = location;
	}

	public void setPosition(Reference position) {
		this.position = position;
	}

	public void setWith(Reference[] with) {
		this.with = with;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	public Reference getEmployer() {
		return employer;
	}

	public Reference getLocation() {
		return location;
	}

	public Reference getPosition() {
		return position;
	}

	public Reference[] getWith() {
		return with;
	}

	public String getStart_date() {
		return start_date;
	}

	public String getEnd_date() {
		return end_date;
	}

	public Reference getFrom() {
		return from;
	}

	public void setFrom(Reference from) {
		this.from = from;
	}
	
}
