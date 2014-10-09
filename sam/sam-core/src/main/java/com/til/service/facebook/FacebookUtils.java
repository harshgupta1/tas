package com.til.service.facebook;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.NoRouteToHostException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLHandshakeException;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.til.service.common.api.Accounts;
import com.til.service.facebook.api.Error;
import com.til.service.facebook.api.FacebookProfile;
import com.til.service.facebook.api.Followers;
import com.til.service.facebook.api.Friends;
import com.til.service.facebook.api.Insight;
import com.til.service.facebook.api.Insights;
import com.til.service.facebook.api.PageDetail;
import com.til.service.facebook.exception.OAuthException;
import com.til.service.utils.Utilities;

@Service
public class FacebookUtils {

	private static final Logger log = LoggerFactory.getLogger(FacebookUtils.class);
	
	static final String GRAPH_API_URL = "https://graph.facebook.com/";
	
	public static String getapplicationaccesstoken(String clientId, String clientSecret) {
		
		String accesstoken = null;
		try {
			String param = "client_id=" + clientId + "&client_secret=" + clientSecret + "&grant_type=client_credentials";
			accesstoken = Utilities.executeGet(GRAPH_API_URL + "oauth/access_token?" + param);
			
		} catch (IOException e) {
			log.error("IOException getting accesstoken for facebook clientid "+clientId,e);
		} catch (Exception e) {
			log.error("Exception getting accesstoken for facebook clientid "+ clientId,e);
		} 
		return accesstoken;
	}
	
	public static String getextendedaccesstoken(String clientId, String clientSecret, String accesstoken) throws Exception {
		
		// Try to loop it 5 times
		String extendenAccessToken = null;
		for(int i=0;i<10;i++)
		{
			accesstoken = accesstoken.replace("access_token=", "");
			String param = "client_id=" + clientId + "&client_secret=" + clientSecret + "&grant_type=fb_exchange_token&fb_exchange_token=" + accesstoken;
			try {
				extendenAccessToken = Utilities.executeGet(GRAPH_API_URL + "oauth/access_token?" + param);
			} catch(NoRouteToHostException e){
				try {
					Thread.currentThread().sleep(5000);
					if(i==9)
					{
						log.error("NoRouteToHostException while regenerating  extended access token for link " + GRAPH_API_URL + "oauth/access_token?" + param 
										+ " after " + (i+1) + " attempts",e);
					}
					// If UnknownHostException, then continue next loop
					continue;
				} catch (InterruptedException e1) {
					log.error("Thread Interrupted", e1);
				}
			}catch(UnknownHostException e){
				try {
					Thread.currentThread().sleep(5000);
					if(i==9)
					{
						log.error("UnknownHostException while regenerating  extended access token for link " + GRAPH_API_URL + "oauth/access_token?" + param 
										+ " after " + (i+1) + " attempts",e);
					}
					// If UnknownHostException, then continue next loop
					continue;
				} catch (InterruptedException e1) {
					log.error("Thread Interrupted", e1);
				}
			}catch (IOException e) {
				log.error("IOException getting extended accesstoken for facebook clientid " + clientId + " with accesstoken " + accesstoken,e);
			} catch (Exception e) {
				log.error("Exception getting extended accesstoken for facebook clientid "+ clientId + " with accesstoken " + accesstoken,e);
			}
			break;
		}
		return extendenAccessToken;
	}
	
	public static String getPageDetail(String pagelinks)
	{
		// Try to loop it 5 times
		for(int i=0;i<5;i++)
		{
			try {
				String[] response = Utilities.executeGetWithResponsecode(GRAPH_API_URL + "?ids=" + pagelinks);
				if("200".equals(response[0]))
				{
					return response[1];
				}
				else
				{
					//log.error("Exception while getting pagedetail for url " + GRAPH_API_URL + "?ids=" + pagelinks + " response code " + response[0]);
				}
			}
			catch(NoRouteToHostException e)
			{
				try {
					Thread.currentThread().sleep(10000);
					/*if(i==4)
					{
						log.error("NoRouteToHostException getting pagedetail for link " + GRAPH_API_URL + "?ids=" + pagelinks 
								+ " after " + (i+1) + " attempts",e);
					}*/
					// If NoRouteToHostException, then continue next loop
					continue;
				} catch (InterruptedException e1) {
					log.error("Thread Interrupted", e1);
				}
			}
			catch(UnknownHostException e)
			{
				try {
					Thread.currentThread().sleep(5000);
					/*if(i==4)
					{
						log.error("UnknownHostException getting pagedetail for link " + GRAPH_API_URL + "?ids=" + pagelinks 
										+ " after " + (i+1) + " attempts",e);
					}*/
					// If UnknownHostException, then continue next loop
					continue;
				} catch (InterruptedException e1) {
					log.error("Thread Interrupted", e1);
				}
			}
			catch(SSLHandshakeException e)
			{
				try {
					Thread.currentThread().sleep(5000);
					/*if(i==4)
					{
						log.error("SSLHandshakeException getting pagedetail for link " + GRAPH_API_URL + "?ids=" + pagelinks 
										+ " after " + (i+1) + " attempts",e);
					}*/
					// If SSLHandshakeException, then continue next loop
					continue;
				} catch (InterruptedException e1) {
					log.error("Thread Interrupted", e1);
				}
			}
			catch (IOException e) {
				log.error("IOException getting pagedetail for link "+GRAPH_API_URL + "?ids=" + pagelinks,e);
			} catch (Exception e) {
				log.error("Exception getting pagedetail for link "+GRAPH_API_URL + "?ids=" + pagelinks,e);
			}
			// If everything is successful at once, then do not continue further
			break;
		}
		// TODO If there is still an error, then set error code in DB and take different steps 
		return null;
	}
	
	public static String getPageDetail(String fbpageid, String accesstoken)
	{
		StringBuilder _url = new StringBuilder(GRAPH_API_URL);
		_url.append(fbpageid);
		_url.append("?");
		_url.append(accesstoken);
		
		// Try to loop it 5 times
		for(int i=0;i<5;i++)
		{
			try {
				String[] response = Utilities.executeGetWithResponsecode(_url.toString());
				return response[1];
			}catch(NoRouteToHostException e)
			{
				try {
					Thread.currentThread().sleep(10000);
					/*if(i==4)
					{
						log.error("NoRouteToHostException getting pagedetail for link " + _url + " after " + (i+1) + " attempts",e);
					}*/
					// If NoRouteToHostException, then continue next loop
					continue;
				} catch (InterruptedException e1) {
					log.error("Thread Interrupted", e1);
				}
			}
			catch(UnknownHostException e)
			{
				try {
					Thread.currentThread().sleep(5000);
					/*if(i==4)
					{
						log.error("UnknownHostException getting pagedetail for link " + _url + " after " + (i+1) + " attempts",e);
					}*/
					// If UnknownHostException, then continue next loop
					continue;
				} catch (InterruptedException e1) {
					log.error("Thread Interrupted", e1);
				}
			}
			catch(SSLHandshakeException e)
			{
				try {
					Thread.currentThread().sleep(5000);
					/*if(i==4)
					{
						log.error("SSLHandshakeException getting pagedetail for link " + _url + " after " + (i+1) + " attempts",e);
					}*/
					continue;
				} catch (InterruptedException e1) {
					log.error("Thread Interrupted", e1);
				}
			}
			catch (IOException e) {
				log.error("IOException getting pagedetail for link "+_url,e);
			} catch (Exception e) {
				log.error("Exception getting pagedetail for link " + _url,e);
			} 
			// If everything is successful at once, then do not continue further
			break;
		}
		// TODO If there is still an error, then set error code in DB and take different steps 
		return null;
	}
	
	public static PageDetail postfeed(String accesstoken, String pageid, String link, String message)
	{
		StringBuilder param = new StringBuilder(accesstoken);
		try {
			param.append("&message=");
			param.append(URLEncoder.encode(message, "UTF-8"));
			if(link != null){
				param.append("&link=");
				param.append(URLEncoder.encode(link, "UTF-8"));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		String[] postid = null;
		String errorMsg = null;
		try{
			//executePost(GRAPH_API_URL + pageid + "/feed", param.toString());
			postid = Utilities.executePostWithResponseCode(GRAPH_API_URL + pageid + "/feed", param.toString());
			
			if(!"200".equals(postid[0].toString())){
				PageDetail pageDetail = new PageDetail();
				Error error = new Error();
				error.setMessage(postid[1].toString());
				pageDetail.setError(error);
			}			
		}		
		catch(Exception e)
		{
			errorMsg = e.getMessage();
			log.error("Exception for page with id " + pageid + " and params " + param + " ,\n while trying to post feed "+link,e);
		}
		
		if(postid[1] != null)
		{
			PageDetail pD = null;
			try
			{
				pD =  parsePageDetail(postid[1]);
			}
			catch(Exception e)
			{
				log.error("Exception while posting on pageid " + pageid + " link " + link + " message " + message + " with accesstoken " + accesstoken, e);
			}
			
			return pD;
		}
		else
		{
			PageDetail pageDetail = new PageDetail();
			Error error = new Error();
			error.setMessage(errorMsg);
			pageDetail.setError(error);
			return pageDetail;
		}
	}
	
	public static PageDetail postImage(String accesstoken, String albumid, File source, String caption)
	{
		
		StringBuilder param = new StringBuilder(accesstoken);
		if(albumid == null || "".equals(albumid))
		{
			albumid = "me";
		}
		try {
			param.append("&name=");
			param.append(URLEncoder.encode(caption, "UTF-8"));
			param.append("&type=1");
		//	param.append(URLEncoder.encode(accesstoken, "UTF-8"));	
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		String[] postid = null;
		String errorMsg = null;
		try{
			System.out.println("param======"+param.toString());
			postid = Utilities.executePostMultipartWithResponseCode(GRAPH_API_URL + albumid + "/photos", source, param.toString());
			
			if(!"200".equals(postid[0].toString())){
				PageDetail pageDetail = new PageDetail();
				Error error = new Error();
				error.setMessage(postid[1].toString());
				pageDetail.setError(error);
			}			
		}		
		catch(Exception e)
		{
			errorMsg = e.getMessage();
			log.error("Exception while posting to album with id " + albumid + " and params " + param,e);
		}
		
		if(postid[1] != null)
		{
			PageDetail pD = null;
			try
			{
				pD =  parsePageDetail(postid[1]);
			}
			catch(Exception e)
			{
				log.error("Exception while posting on album id " + albumid + " message " + caption + " with accesstoken " + accesstoken, e);
			}
			
			return pD;
		}
		else
		{
			PageDetail pageDetail = new PageDetail();
			Error error = new Error();
			error.setMessage(errorMsg);
			pageDetail.setError(error);
			return pageDetail;
		}
	}
	
	public static PageDetail postlinks(String accesstoken, String pageid, String link, String message)
	{
		StringBuilder param = new StringBuilder();
		try {
			param.append(accesstoken);
			param.append("&message=");
			param.append(URLEncoder.encode(message, "UTF-8"));
			param.append("&link=");
			param.append(URLEncoder.encode(link, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			log.error("UnsupportedEncodingException",e);
		}
		
		String postid = null;
		String response[] = null;
		String errorMsg = null;
		for(int i=0;i<5;i++)
		{
			try
			{
				response = Utilities.executePostWithResponseCode(GRAPH_API_URL + pageid + "/links", param.toString());
			}
			catch(SSLHandshakeException e)
			{
				try {
					Thread.currentThread().sleep(5000);
					if(i==4)
					{
						log.error("SSLHandshakeException for page with id " + pageid + " and params " + param + ",\n while trying to post link " 
									+ link + " after " + i + " attempts",e);
					}
					// If SSLHandshakeException, then continue next loop
					continue;
				} catch (InterruptedException e1) {
					log.error("Thread Interrupted", e1);
				}
			}
			catch(UnknownHostException e)
			{
				try {
					Thread.currentThread().sleep(5000);
					if(i==4)
					{
						log.error("UnknownHostException for page with id " + pageid + " and params " + param + ",\n while trying to post link " 
									+ link + " after " + i + " attempts",e);
					}
					// If UnknownHostException, then continue next loop
					continue;
				} catch (InterruptedException e1) {
					log.error("Thread Interrupted", e1);
				}
			}
			catch(NoRouteToHostException e)
			{
				try {
					Thread.currentThread().sleep(10000);
					if(i==4)
					{
						log.error("NoRouteToHostException for page with id " + pageid + " and params " + param + ",\n while trying to post link " 
									+ link + " after " + i + " attempts",e);
					}
					// If NoRouteToHostException, then continue next loop
					continue;
				} catch (InterruptedException e1) {
					log.error("Thread Interrupted", e1);
				}
			}
			catch(IOException e)
			{
				errorMsg = e.getMessage();
				log.error("IOException for page with id " + pageid + " and params " + param + ",\n while trying to post link " 
									+ link + " after " + i + " attempts",e);
			}
			catch(Exception e)
			{
				errorMsg = e.getMessage();
				log.error("Exception for page with id for page with id " + pageid + " and params " + param + ",\n while trying to post link " 
									+ link,e);
			}
			break;
		}
		if("200".equalsIgnoreCase(response[0]))
		{
			postid = response[1];
		}
		else
		{
			errorMsg = response[1];
		}
		if(postid != null)
		{
			PageDetail pD = null;
			try
			{
				pD =  parsePageDetail(postid);
			}
			catch(Exception e)
			{
				log.error("Exception while posting on pageid " + pageid + " link " + link + " message " + message + " with accesstoken " + accesstoken, e);
			}
			
			return pD;
		}
		else
		{
			PageDetail pageDetail = new PageDetail();
			Error error = new Error();
			error.setMessage(errorMsg);
			pageDetail.setError(error);
			return pageDetail;
		}
	}
	
	public static String deletepost(String accesstoken, String pageid, String postid)
	{
		StringBuilder param = new StringBuilder(accesstoken);
		param.append("&method=delete");
		
		StringBuilder url = new StringBuilder(GRAPH_API_URL);
		url.append(pageid);
		url.append("_");
		url.append(postid);
		String errorMsg = null,msg=null;
		try
		{
			msg = Utilities.executePost(url.toString(),param.toString());
		}
		catch(IOException e)
		{
			errorMsg = e.getMessage();
			log.error("IOException while deleting post with id " + postid + " on page id " + pageid,e);
			return errorMsg;
		}
		catch(Exception e)
		{
			errorMsg = e.getMessage();
			log.error("Exception while deleting post with id " + postid + " on page id " + pageid,e);
			return errorMsg;
		}
		return msg;
	}
	
	public static String deletepostlikes(String accesstoken, String pageid, String postid)
	{
		StringBuilder param = new StringBuilder(accesstoken);
		param.append("&method=delete");
		
		StringBuilder url = new StringBuilder(GRAPH_API_URL);
		url.append(pageid);
		url.append("_");
		url.append(postid);
		url.append("/likes");
		String errorMsg = null,msg=null;
		try
		{
			msg = Utilities.executePost(url.toString(),param.toString());
		}
		catch(IOException e)
		{
			errorMsg = e.getMessage();
			log.error("IOException while deleting post with id " + postid + " on page id " + pageid,e);
			return errorMsg;
		}
		catch(Exception e)
		{
			errorMsg = e.getMessage();
			log.error("Exception while deleting post with id " + postid + " on page id " + pageid,e);
			return errorMsg;
		}
		return msg;
	}
	
	public static PageDetail parsePageDetail(String jsonStr) throws Exception {
		
		try 
		{
			JSONObject json = (JSONObject) JSONSerializer.toJSON(jsonStr);
			PageDetail pg = (PageDetail) JSONObject.toBean(json,PageDetail.class);
			return pg;
		} catch (Exception e) {
			log.error("Exception in method parsePageDetail(String jsonStr) while parsing json string "+jsonStr,e);
			throw e;
		}
	}
	
	public static PageDetail parsePageDetail(JSONObject json) throws JSONException {
		
		try 
		{
			PageDetail pg = (PageDetail) JSONObject.toBean(json,PageDetail.class);
			return pg;
		}catch(JSONException e)
		{
			log.error("Exception in method parsePageDetail(JSONObject json) while parsing json string "+json,e);
			throw e;
		}
		catch (Exception e) {
			log.error("Exception in method parsePageDetail(JSONObject json) while parsing json string "+json,e);
		}
		return null;
	}

	public static Map<String,PageDetail> parsePageLinks(String links, String jsonStr) {
		
		String linkPage[] = links.split(",");
		PageDetail pageDetail = null;
		Map<String, PageDetail> pageDetailMap = new HashMap<String, PageDetail>(linkPage.length);
		try {
			
				JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(jsonStr);
				
				for (String pagelink : linkPage) {
					JSONObject jsonPageDetail = (JSONObject)jsonObject.get(pagelink);
					try
					{
						pageDetail = parsePageDetail(jsonPageDetail);
					}
					catch(JSONException e)
					{
						log.error("Exception parsing json string in method Map<String,PageDetail> parsePageLinks "+jsonStr + " for link "+links,e);
					}
					pageDetailMap.put(pagelink, pageDetail);
				}
				
				return pageDetailMap;
			
		} catch (Exception e) {
			log.error("Exception parsing json string in method Map<String,PageDetail> parsePageLinks "+jsonStr + " for link "+links,e);
		}
		return null;
	}
	
	public static PageDetail parsePageLinks(String jsonStr) {
		try {
			JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(jsonStr);
			try
			{
				return parsePageDetail(jsonObject);
			}catch(JSONException e)
			{
				log.error("JSONException in method parsePageLinks while parsing json string "+jsonStr,e);
			}
		} catch (Exception e) {
			log.error("Exception in method parsePageLinks while parsing json string "+jsonStr,e);
		}
		return null;
	}

	public static String getaccountdetails(String fb_adminId, String fb_accessToken, int limit, int offset) 
					throws IOException, Exception 
	{
		/**
		 * https://graph.facebook.com/117787264903013/accounts?access_token=117787264903013|ed0786c97288a304f884f54f.1-100000938118596|bl6ZG4f5udc-0keLGAT3ynKiLBA
		 * &limit=50&offset=0
		 */
		StringBuilder url = new StringBuilder();
		url.append(GRAPH_API_URL);
		url.append(fb_adminId);
		url.append("/accounts?");
		url.append(fb_accessToken); //access_token= will/should be appended to this 
		url.append("&limit=");
		url.append(limit);
		url.append("&offset=");
		url.append(offset);
		log.debug("Getting account details for url {}",url);
		return Utilities.executeGet(url.toString());
	}
	
	public static String getprofiledetails(String userid, String accesstoken) 
			throws IOException, Exception 
	{
		/**
		* https://graph.facebook.com/me?access_token=117787264903013|ed0786c97288a304f884f54f.1-100000938118596|bl6ZG4f5udc-0keLGAT3ynKiLBA
		*/
		StringBuilder url = new StringBuilder();
		url.append(GRAPH_API_URL);
		if(null == userid)
		{
			url.append("me?");
		}
		else
		{
			url.append(userid + "?");
		}
		url.append(accesstoken); //access_token= will/should be appended to this 
		log.debug("Getting profile details for userid {}",userid);
		return Utilities.executeGet(url.toString());
	}
	
	public static FacebookProfile parseProfileDetails(String json) throws OAuthException
	{
		try {
			/*Map classMap = new HashMap();  
			classMap.put("locale", Locale.class);*/
			FacebookProfile profile = new FacebookProfile();
			FacebookProfile facebookProfile = (FacebookProfile)JSONObject.
													toBean(JSONObject.fromObject( json ),profile,new JsonConfig());
			log.debug("FacebookProfile {}",json);
			return facebookProfile;
		} catch (Exception e) {
			log.error("Exception parsing JSON String " + json + " while building FacebookProfile Object ",e);
			throw new OAuthException(e.getLocalizedMessage());
		}
	}
	
	public static Accounts parseAccountsConnection(String json) throws OAuthException {
		
		try {
			JSONObject jsonObject = JSONObject.fromObject( json );  
			Accounts accounts = (Accounts)JSONObject.toBean( jsonObject,Accounts.class );
			if(accounts.getData() != null)
				log.debug("Total pages retrieved {}",accounts.getData().length);
			else{
				log.debug("Error message {}",accounts.getError().getMessage());
			}
			return accounts;
		} catch (Exception e) {
			log.error("Exception parsing JSON String " + json + " while building Accounts Connection Object ",e);
			throw new OAuthException(e.getLocalizedMessage());
		}
	}
	
	public static String getpostinsights(String accesstoken, String pageid, String postid)
	{
		StringBuilder url = new StringBuilder(GRAPH_API_URL);
		url.append(pageid);
		url.append("_");
		url.append(postid);
		url.append("/insights?");
		url.append(accesstoken);
		String errorMsg = null;
		String msg[];
		
		// Try to loop it 5 times
		for(int i=0;i<5;i++)
		{
			try {
				msg = Utilities.executeGetWithResponsecode(url.toString());
				return msg[1];
			}catch(NoRouteToHostException e)
			{
				try {
					Thread.currentThread().sleep(10000);
					/*if(i==4)
					{
						log.error("NoRouteToHostException while getting post insights for page with id " + pageid + " ,post id "
									+ postid + " after " + (i+1) + " attempts",e);
					}*/
					// If NoRouteToHostException, then continue next loop
					continue;
				} catch (InterruptedException e1) {
					log.error("Thread Interrupted", e1);
				}
			}
			catch(UnknownHostException e)
			{
				try {
					Thread.currentThread().sleep(5000);
					/*if(i==4)
					{
						log.error("UnknownHostException while getting post insights for page with id " + pageid + " ,post id "+postid 
								+ " after " + (i+1) + " attempts",e);
					}*/
					// If UnknownHostException, then continue next loop
					continue;
				} catch (InterruptedException e1) {
					log.error("Thread Interrupted", e1);
				}
			}
			catch(SSLHandshakeException e)
			{
				try {
					Thread.currentThread().sleep(5000);
					/*if(i==4)
					{
						log.error("SSLHandshakeException while getting post insights for page with id " + pageid + " ,post id "+postid 
								+ " after " + (i+1) + " attempts",e);
					}*/
					continue;
				} catch (InterruptedException e1) {
					log.error("Thread Interrupted", e1);
				}
			}
			catch (IOException e) {
				errorMsg = e.getMessage();
				log.error("IOException while getting post insights for page with id " + pageid + " ,post id "+postid,e);
				return errorMsg;
			} catch (Exception e) {
				errorMsg = e.getMessage();
				log.error("Exception while getting post insights for page with id " + pageid + " ,post id "+postid,e);
				return errorMsg;
			} 
			// If everything is successful at once, then do not continue further
			break;
		}
		return null;
	}
	
	public static Insights parseInsightsConnection(String json) throws OAuthException {
		
		try {
			JSONObject jsonObject = JSONObject.fromObject( json );  
			Insights insights  = (Insights)JSONObject.toBean( jsonObject,Insights.class );
			if(insights.getError() != null)
			{
				log.error("Error retrieving insights: {}, JSON {}",insights.getError().getMessage(), json);
			}
			else
			{
				log.debug("Total insights retrieved {}",insights.getData().length);
			}
			return insights;
		} catch (Exception e) {
			log.error("Exception parsing JSON String " + json + " while building Insights Connection Object",e);
			throw new OAuthException(e.getLocalizedMessage());
		}
	}
	
	public static Insight getInsight(Insights insights, String apiName) throws OAuthException {
		
		try {
			for (Insight insight  : insights.getData()) {
				if(apiName.equals(insight.getName()))
				{
					return insight;
				}
			}
			return null;
		} catch (Exception e) {
			log.error("Exception getting Insights for api "+apiName ,e);
			throw new OAuthException(e.getLocalizedMessage());
		}
	}
	
	public static String getFacebookFollowers(String accesstoken)
			throws IOException, Exception {
		return getFacebookFollowers(null, accesstoken);
	}

	public static String getFacebookFollowers(String userid, String accesstoken)
			throws IOException, Exception {
		/**
		 * GET https://graph.facebook.com/me/subscribers?access_token={
		 * access_token}
		 */
		StringBuilder url = new StringBuilder();
		url.append(GRAPH_API_URL);
		url.append((null == userid) ? "me" : userid);
		url.append("/subscribers?");
		url.append(accesstoken);
		log.debug("Getting followers for userid {}", userid);
		return Utilities.executeGet(url.toString());
	}

	public static Followers parseFacebookFollowers(String json)
			throws OAuthException {
		try {
			Followers followers = new Followers();
			followers = (Followers) JSONObject.toBean(
					JSONObject.fromObject(json), followers, new JsonConfig());
			log.debug("FacebookFollowers {}", json);
			return followers;
		} catch (Exception e) {
			log.error("Exception parsing JSON String " + json + " while building FacebookFollowers Object ", e);
			throw new OAuthException(e.getLocalizedMessage());
		}
	}

	public static String getFacebookFriends(String accesstoken)
			throws IOException, Exception {
		return getFacebookFriends(null, accesstoken);
	}

	public static String getFacebookFriends(String userid, String accesstoken)
			throws IOException, Exception {
		/**
		 * GET https://graph.facebook.com/me/friends?access_token={access_token}
		 */
		StringBuilder url = new StringBuilder();
		url.append(GRAPH_API_URL);
		url.append((null == userid) ? "me" : userid);
		url.append("/friends?");
		url.append(accesstoken);
		log.debug("Getting friends for userid {}", userid);
		return Utilities.executeGet(url.toString());
	}

	public static Friends parseFacebookFriends(String json)
			throws OAuthException {
		try {
			Friends friends = new Friends();
			friends = (Friends) JSONObject.toBean(JSONObject.fromObject(json),
					friends, new JsonConfig());
			log.debug("FacebookFrineds {}", json);
			return friends;
		} catch (Exception e) {
			log.error("Exception parsing JSON String " + json + " while building FacebookFriends Object ", e);
			throw new OAuthException(e.getLocalizedMessage());
		}
	}
}
