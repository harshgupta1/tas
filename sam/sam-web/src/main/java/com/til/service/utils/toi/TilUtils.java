/**
 * 
 */
package com.til.service.utils.toi;

import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author girish.gaurav
 * 
 */
public class TilUtils extends TagSupport {

	private static final long serialVersionUID = -5377398460937192942L;

	public static String cleanArticleTitle(String title) {
		title = title.trim().replaceAll("-", "");
		title = title.replaceAll("[  ]", "-");
		title = title.replaceAll("[ ]", "-");
		return title.replaceAll("[^a-zA-Z0-9-/]*", "");
	}

}
