package com.til.service.toi;

import java.io.StringReader;

import javax.xml.bind.UnmarshalException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.oxm.GenericUnmarshaller;
import org.springframework.oxm.UnmarshallingFailureException;

import com.til.service.common.api.BreakingNews;
import com.til.service.common.api.Rss2Feeds;
import com.til.service.toi.api.DayLifeFeeds;
import com.til.service.toi.api.DayLifeFeedsV2;

public class ToiUtils {
	
	private static final Logger log = LoggerFactory.getLogger(ToiUtils.class);
	
	public static DayLifeFeeds parseDayLifeFeeds(GenericUnmarshaller unmarshaller, String daylifefeed)
			throws Exception,UnmarshalException
    {
    	DayLifeFeeds dayLifeFeeds = null;
    	
		try
		{
			Source source = new StreamSource(new StringReader(daylifefeed));
			dayLifeFeeds = (DayLifeFeeds) unmarshaller.unmarshal(source);
		}catch(Exception e)
		{
			throw e;
			//log.error("Error parsing daylife feed data \n {} \n", daylifefeed, e);
		}
		
		return dayLifeFeeds;
    }
    
	public static DayLifeFeedsV2 parseDayLifeFeedsV2(GenericUnmarshaller unmarshaller, String daylifefeed)
																	throws Exception,UnmarshalException
	{
		DayLifeFeedsV2 dayLifeFeeds = null;
		
		try
		{
			Source source = new StreamSource(new StringReader(daylifefeed));
			dayLifeFeeds = (DayLifeFeedsV2) unmarshaller.unmarshal(source);
		}catch(Exception e)
		{
			throw e;
			//log.error("Error parsing daylife feed data \n {} \n", daylifefeed, e);
		}
		
		return dayLifeFeeds;
	}
	
    public static Rss2Feeds parseRss2Feeds(GenericUnmarshaller unmarshaller, String rss)throws UnmarshallingFailureException
    {
    	Rss2Feeds rss2Feeds = null;
		try
		{
			Source source = new StreamSource(new StringReader(rss));
			rss2Feeds = (Rss2Feeds) unmarshaller.unmarshal(source);
		}catch(UnmarshallingFailureException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			log.error("Exception while parsing RSS XML {} \n {}",rss,e);
		}
		return rss2Feeds ;
    }
    
    public static BreakingNews parseBreakingNewsFeeds(GenericUnmarshaller unmarshaller, String breakingNews)throws UnmarshallingFailureException
    {
    	BreakingNews news = null;
		try
		{
			Source source = new StreamSource(new StringReader(breakingNews));
			news = (BreakingNews) unmarshaller.unmarshal(source);
		}catch(UnmarshallingFailureException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			log.error("Exception while parsing Breaking News XML {} \n {}",breakingNews,e);
		}
		return news ;
    }
    
    public static JSONObject parseiBeatFeed(String json)throws JSONException,Exception
    {
    	try {
    		log.debug("parseiBeatFeed {}",json);
			return JSONObject.fromObject( json );
		}catch(JSONException e){
			throw e;
		}
    	catch (Exception e) {
			log.error("Exception parsing JSON String {} while building JSON Object \n{}",json,e);
			throw new Exception(e.getLocalizedMessage());
		}
    }
    
    public static JSONObject parseiBeatLast4HoursFeed(String json)throws JSONException,Exception
    {
    	try {
    		log.debug("parseiBeatLast4HoursFeed {}",json);
			return JSONObject.fromObject( json );
		}catch(JSONException e){
			throw e;
		}catch (Exception e) {
			log.error("Exception parsing JSON String {} while building JSON Object \n{}",json,e);
			throw new Exception(e.getLocalizedMessage());
		}
    }
}
