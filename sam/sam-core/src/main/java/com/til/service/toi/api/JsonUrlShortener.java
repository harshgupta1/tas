package com.til.service.toi.api;

import java.net.SocketTimeoutException;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.til.service.common.api.TOIMinifiedURL;
import com.til.service.toi.UrlShortener;
import com.til.service.utils.Utilities;

@Service("jsonUrlShortener")
public class JsonUrlShortener implements UrlShortener {

	private static final Logger log = LoggerFactory.getLogger(JsonUrlShortener.class);
	
	@Override
	public TOIMinifiedURL getMinifiedURL(String url) {
		TOIMinifiedURL tOIMinifiedURL = null;
		for(int i=0;i<5;i++)
		{
			try {
				String minifiedUrl= Utilities.executeGet(url);
				log.debug("JSON for url {} is {}",url,minifiedUrl);
				JSONObject json = (JSONObject) JSONSerializer.toJSON(minifiedUrl);
				tOIMinifiedURL = (TOIMinifiedURL) JSONObject.toBean(json,TOIMinifiedURL.class);
			}catch (SocketTimeoutException e) {
				try {
					Thread.currentThread().sleep(5000);
					if(i==4)
					{
						log.error("SocketTimeoutException in shortening url {} after {} attempts \n Exception {}",
								new Object[]{url,i+1,e});
					}
					// If SocketTimeoutException, then continue next loop
					continue;
				} catch (InterruptedException e1) {
					log.error("Thread Interrupted", e1);
				}
			}
			catch(JSONException e)
			{
				try {
					Thread.currentThread().sleep(5000);
					if(i==4)
					{
						log.error("JSONException in shortening url {} after {} attempts \n Exception {}",
								new Object[]{url,i+1,e});
					}
					// If JSONException, then continue next loop
					continue;
				} catch (InterruptedException e1) {
					log.error("Thread Interrupted", e1);
				}
			}
			catch (Exception e) {
				log.error("Exception in shortening url {} \n Exception {}",new Object[]{url,e});
			}
			break;
		}
		return tOIMinifiedURL;
	}

}
