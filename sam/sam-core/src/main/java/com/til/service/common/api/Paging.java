package com.til.service.common.api;

import java.io.Serializable;

/**
 * 
 * @author girish.gaurav
 * 
 */
public class Paging implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4478876741740752636L;
	private Cursor cursors;
	private String next;
	private String previous;

	public Paging() {
	}

	public Cursor getCursors() {
		return cursors;
	}

	public void setCursors(Cursor cursors) {
		this.cursors = cursors;
	}

	public String getNext() {
		return next;
	}

	public void setNext(String next) {
		this.next = next;
	}

	public String getPrevious() {
		return previous;
	}

	public void setPrevious(String previous) {
		this.previous = previous;
	}

	public static class Cursor {
		private String after;
		private String before;

		public Cursor() {
		}

		public String getAfter() {
			return after;
		}

		public void setAfter(String after) {
			this.after = after;
		}

		public String getBefore() {
			return before;
		}

		public void setBefore(String before) {
			this.before = before;
		}

	}
}
