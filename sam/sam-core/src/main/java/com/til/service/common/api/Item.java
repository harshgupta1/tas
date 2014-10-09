package com.til.service.common.api;

import java.util.Date;

public interface Item {
	
	public abstract String getTitle();
	public abstract String getUrl();
	public abstract Date getPubDate();
	public abstract String getSource();
}
