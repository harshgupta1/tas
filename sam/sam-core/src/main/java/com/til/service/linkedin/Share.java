package com.til.service.linkedin;
import javax.xml.bind.annotation.XmlRootElement;

//Object represent share object of xml payload which sent to linked for sharing
@XmlRootElement
public class Share {

	private String comment;
	
	private Content content;
	
	private Visibility visibility;
	
	public Visibility getVisibility() {
		return visibility;
	}
	public void setVisibility(Visibility visibility) {
		this.visibility = visibility;
	}
	public Content getContent() {
		return content;
	}
	public void setContent(Content content) {
		this.content = content;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
}
