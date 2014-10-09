package com.til.service.ui.admin.command;

import com.til.service.common.api.Accounts;

public class PageListBean {
	
	private Accounts accounts;
	private String pageid;
	private int limit = 50;
	private int offset = 0;
	private short websiteid = 1;
	
	public Accounts getAccounts() {
		return accounts;
	}
	public void setAccounts(Accounts accounts) {
		this.accounts = accounts;
	}
	public String getPageid() {
		return pageid;
	}
	public void setPageid(String pageid) {
		this.pageid = pageid;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public short getWebsiteid() {
		return websiteid;
	}
	public void setWebsiteid(short websiteid) {
		this.websiteid = websiteid;
	}
	
}
