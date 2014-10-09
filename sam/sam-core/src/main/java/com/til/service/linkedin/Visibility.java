package com.til.service.linkedin;

import javax.xml.bind.annotation.XmlRootElement;

//Object represent the visibility to audiance attached with main share object

@XmlRootElement(name = "visibility")
public class Visibility {

	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}
