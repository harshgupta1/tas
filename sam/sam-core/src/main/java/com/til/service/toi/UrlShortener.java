package com.til.service.toi;

import com.til.service.common.api.TOIMinifiedURL;

public interface UrlShortener {
	
	public TOIMinifiedURL getMinifiedURL(String url);
}
