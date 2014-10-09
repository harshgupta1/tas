package com.til.service.toi.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.til.service.common.api.TOIMinifiedURL;
import com.til.service.toi.UrlShortener;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:root-context.xml" })
public class TOIUrlShortenerFactoryTest {
	
	@Autowired
	TOIUrlShortenerFactory factory;
	
	@Test
    public void testgetMinifiedUrl() throws Exception {
		String url = "http://toi.in/micron/restshortner.html?login=times&apiKey=R_66d9bc26baca7507b68b196a47657721&format=json&url=http://timesofindia.indiatimes.com/india/90-of-I-T-arrears-owed-by-just-12-people-CAG/articleshow/11139905.cms";
		UrlShortener shortener = factory.getObject(url);
		TOIMinifiedURL toiMinifiedURL = shortener.getMinifiedURL(url);
		System.out.println(toiMinifiedURL.getData().getHash());
    }
}
