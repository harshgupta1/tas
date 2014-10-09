package com.til.service.google;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.til.service.common.api.Accounts;
import com.til.service.common.api.Paging;
import com.til.service.facebook.api.PageDetail;
import com.til.service.facebook.exception.OAuthException;
import com.til.service.google.api.GoogleProfile;
import com.til.service.google.api.GoogleRefTokenResp;

@Service
public class GooglePlusUtils {

	private static final Logger log = LoggerFactory.getLogger(GooglePlusUtils.class);
	
	private static JsonFactory JSON_FACTORY = new JacksonFactory();
	private static HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	static final String GRAPH_API_URL = "https://www.googleapis.com/oauth2/v1/userinfo";
	private static String message=null;
	
	public static String getprofiledetails(String accesstoken,String clientid,String clientsecret) throws IOException, Exception 
	{
		String jsonIdentity=null;

		try{	
			GoogleCredential credential = new GoogleCredential.Builder().setJsonFactory(JSON_FACTORY).setTransport(HTTP_TRANSPORT)
					.setClientSecrets(clientid, clientsecret).build().setAccessToken(accesstoken);
			final HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(credential);

			// Make an authenticated request
			final GenericUrl url = new GenericUrl(GRAPH_API_URL);
			final HttpRequest request = requestFactory.buildGetRequest(url);
			request.getHeaders().setContentType("application/json");
			jsonIdentity = request.execute().parseAsString();		
		}	
		catch(Exception e)
		{
			message=e.getLocalizedMessage();
			e.printStackTrace();							
		}				
		return jsonIdentity;		
	}
	
	public static String getErrorMessages()
	{
		return message;
	}

	public static Accounts parseGoogleAccountsConnection(String json, String token) throws OAuthException {

		PageDetail pageDetail = new PageDetail();
		PageDetail googleProfile = (PageDetail) JSONObject.toBean(
				JSONObject.fromObject(json), pageDetail, new JsonConfig());
		try {
			pageDetail.setId(googleProfile.getId());
			pageDetail.setUsername(googleProfile.getName());
			pageDetail.setName(googleProfile.getName());
			pageDetail.setPicture(googleProfile.getPicture());
			pageDetail.setAccess_token(token);

		} catch (Exception e) {
			log.error("Exception in getting details from googleplus", e);
		}
		// pageDetail.setPicture(user.get)
		PageDetail[] data = new PageDetail[1];
		Accounts accounts = new Accounts();
		data[0] = pageDetail;
		accounts.setData(data);
		Paging paging = new Paging();
		accounts.setPaging(paging);
		return accounts;

	}

	public static GoogleProfile parseProfileDetails(String json) throws OAuthException {
		try {
			GoogleProfile profile = new GoogleProfile();
			GoogleProfile googleProfile = (GoogleProfile) JSONObject.toBean(JSONObject.fromObject(json), profile, new JsonConfig());
			log.debug("GoogleProfile {}", json);
			return googleProfile;
		} catch (Exception e) {
			log.error("Exception parsing JSON String " + json + " while building GoogleProfile Object ", e);
			throw new OAuthException(e.getLocalizedMessage());
		}
	}
	
	public static String getNewAccessToken(String clientid,
			String clientsecreat, String reftoken, String grantType) {
		String urlParameters = "client_id=" + clientid + "&client_secret="
				+ clientsecreat + "&refresh_token=" + reftoken + "&grant_type="
				+ grantType;
		String newAccessToken = null;
		try {
			URL url = new URL("https://accounts.google.com/o/oauth2/token");
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter writer = new OutputStreamWriter(
					conn.getOutputStream());
			writer.write(urlParameters);
			writer.flush();
			String json = "";
			String response = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			while ((response = reader.readLine()) != null) {
				json = json + response;
			}
			writer.close();
			reader.close();
			GoogleRefTokenResp googleresp = new GoogleRefTokenResp();
			GoogleRefTokenResp googlerefresp = (GoogleRefTokenResp) JSONObject
					.toBean(JSONObject.fromObject(json), googleresp,
							new JsonConfig());
			newAccessToken = googlerefresp.getAccess_token();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newAccessToken;
	}
	
	public static Accounts parseAccountsConnection(String json) throws OAuthException {
		
		try {
			JSONObject jsonObject = JSONObject.fromObject("{\"data\":[]}");  
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


}
