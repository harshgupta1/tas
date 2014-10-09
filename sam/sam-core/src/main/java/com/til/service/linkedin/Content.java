package com.til.service.linkedin;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

//This objects represent the content object to attached with main share object

@XmlRootElement(name = "content")
public class Content {

	private String title;
	
	private String submittedurl;

	private String description;

	private String submittedimageurl;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@XmlElement(name = "submitted-url")
	public String getSubmittedurl() {
		return submittedurl;
	}

	public void setSubmittedurl(String submittedurl) {
		this.submittedurl = submittedurl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@XmlElement(name = "submitted-image-url")
	public String getSubmittedimageurl() {
		return submittedimageurl;
	}

	public void setSubmittedimageurl(String submittedimageurl) {
		this.submittedimageurl = submittedimageurl;
	}
	

}
