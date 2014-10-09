package com.til.service.toi.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.til.service.toi.UrlShortener;

@Service
public class TOIUrlShortenerFactory implements FactoryBean<UrlShortener> {
	
	private static final Logger log = LoggerFactory.getLogger(TOIUrlShortenerFactory.class);
	
	@Autowired
	@Qualifier("jsonUrlShortener")
	private UrlShortener jsonUrlShortener;
	
	@Autowired
	@Qualifier("xmlUrlShortener")
	private UrlShortener xmlUrlShortener;
	
	@Override
	public UrlShortener getObject() throws Exception {
		return new JsonUrlShortener();
	}
	
	public UrlShortener getObject(String url) {
		if(url.indexOf("format=json") > 0)
		{
			return jsonUrlShortener;
		}
		else
		{
			return xmlUrlShortener;
		}
	}
	
	@Override
	public Class<UrlShortener> getObjectType() {
		return UrlShortener.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
