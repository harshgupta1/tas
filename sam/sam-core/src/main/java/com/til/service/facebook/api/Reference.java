package com.til.service.facebook.api;

/**
 * A simple reference to another Facebook object without the complete set of object data.
 * @author Craig Walls
 */
public class Reference {

	private String id;

	private String name;

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

}

