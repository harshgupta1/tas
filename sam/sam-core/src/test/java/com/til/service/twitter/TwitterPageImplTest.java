package com.til.service.twitter;

import java.net.URLEncoder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:root-context.xml" })
public class TwitterPageImplTest {
	
	
	
	/*@Test
    public void testgetMinifiedURL() throws Exception {
		
		String api = "http://toi.in/micron/restshortner.html?login=times&apiKey=R_66d9bc26baca7507b68b196a47657721&format=json&url=";
		String url = "http://timesofindia.indiatimes.com/world/us/Diamond-gifted-to-Liz-Taylor-by-Burton-sells-for-record-88m/articleshow/11114202.cms";
		TOIMinifiedURL toiMinifiedURL = Utilities.getMinifiedURL(api + URLEncoder.encode(url, "UTF-8"));
		System.out.println(toiMinifiedURL.getData().getUrl());
    }*/
	
	@Test
	public void testUpdateStatus()
	{
		ConfigurationBuilder cb = new ConfigurationBuilder();
    	cb.setDebugEnabled(true);
    	cb.setOAuthConsumerKey("fSdbBGUelGVVQd9cwPH55Q");
    	cb.setOAuthConsumerSecret("uhwW5r8tFM92ykbcefJVePbLmqoJceqO4h588L7Qhs");
    	cb.setOAuthAccessToken("112937676-keOz1iERFI9oDGb7W3FfI1YtsHWqtiQ1lipiHryw");
    	cb.setOAuthAccessTokenSecret("OA4zPHpS8TD0OFFvEUFYDmUmk4ExDyBM1JaYKArceBc");
    	Twitter twitter =  new TwitterFactory(cb.build()).getInstance();
    	
    	int statusCode = 0;
		String statusMsg = null;
		try
		{
    		Status status = TwitterUtils.updateStatus(twitter,"","http://timesofindia.indiatimes.com/india/Manmohan-presses-alarm-bell-on-drought-government-swings-into-action/articleshow/15107499.cms","Manmohan presses alarm bell on drought, government swings into action");
    	
		} catch (TwitterException e) {
			statusCode = e.getStatusCode();
			statusMsg = e.getErrorMessage();
		}
	}
}
