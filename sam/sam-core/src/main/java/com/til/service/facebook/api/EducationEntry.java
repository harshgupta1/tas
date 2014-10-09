package com.til.service.facebook.api;

/**
 * Model class representing an entry in a user's education history.
 * @author Harsh Gupta
 */
public class EducationEntry {

	private Reference school;

	private Reference year;

	private Reference[] concentration;
	
	private String type;

	public void setSchool(Reference school) {
		this.school = school;
	}

	public void setYear(Reference year) {
		this.year = year;
	}

	public void setConcentration(Reference[] concentration) {
		this.concentration = concentration;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Reference getSchool() {
		return school;
	}

	public Reference getYear() {
		return year;
	}

	public Reference[] getConcentration() {
		return concentration;
	}

	public String getType() {
		return type;
	}
}

