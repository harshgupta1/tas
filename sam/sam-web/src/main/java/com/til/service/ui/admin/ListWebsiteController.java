/**
 * 
 */
package com.til.service.ui.admin;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.til.security.acegi.CustomUser;
import com.til.service.common.api.Accounts;
import com.til.service.common.dao.TopicPageDao;
import com.til.service.common.dao.WebsiteDao;
import com.til.service.common.dao.hibernate.entity.TopicPage;
import com.til.service.common.dao.hibernate.entity.User;
import com.til.service.common.dao.hibernate.entity.Website;
import com.til.service.facebook.FacebookUtils;
import com.til.service.facebook.api.Error;
import com.til.service.facebook.api.FacebookProfile;
import com.til.service.facebook.api.Followers;
import com.til.service.facebook.api.Friends;
import com.til.service.facebook.api.PageDetail;
import com.til.service.google.GooglePlusUtils;
import com.til.service.linkedin.LinkedInUtils;
import com.til.service.linkedin.api.LinkedInProfile;
import com.til.service.twitter.TwitterUtils;
import com.til.service.ui.admin.command.PageListBean;
import com.til.service.utils.Utilities;

/**
 * Ref : https://developer.linkedin.com/documents/authentication
 * 
 * @author Harsh.Gupta
 * @author Shahan.Shah
 * 
 */
@Controller
@SessionAttributes({"pageListBean"})
public class ListWebsiteController{
	
	/** Logger for this class and subclasses */
    protected final Logger log = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private WebsiteDao     websiteDao; 
	
    @Autowired
    private TopicPageDao     topicPageDao;
    
    //@Value("#{jdbcConfiguration['facebook.redirect_uri']}")
    /* http://audiencemanager.indiatimes.com/getaccesstoken?id= */
    @Value("${facebook.redirect_uri}")
    private String facebook_redirect_uri;
    
    /* https://graph.facebook.com/oauth/authorize?client_id={0}&display=popup&redirect_uri={1}
     * &scope=manage_pages,offline_access,publish_stream */
    @Value("${facebook.authorizeurl}")
    private String facebook_authorizeurl;
    
    @Value("${facebook.accesstokenurl}")
    private String facebook_accesstoken_url;
    
    @Value("${twitter.redirect_uri}")
    private String twitter_redirect_uri;
    
    /*https://www.linkedin.com/uas/oauth2/authorization?response_type=code
        &client_id=YOUR_API_KEY&scope=SCOPE&state=STATE&redirect_uri=YOUR_REDIRECT_URI*/
    @Value("${linkedin.authorizeurl}")
    private String linkedin_authorizeurl;
    
    @Value("${linkedin.redirect_uri}")
    private String linkedin_redirect_uri;
    
    @Value("${linkedin.accesstokenurl}")
    private String linkedin_accesstoken_url;
    
    @Value("${googleplus.redirect_uri}")
    private String googleplus_redirect_uri;
    
    @Value("${googleplus.scopes}")
    private String googleplus_scopes;
    
	private JsonFactory JSON_FACTORY = new JacksonFactory();
	private HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

	
    @RequestMapping(value="/listWebsite", method=RequestMethod.GET)
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response){
		
		Website website = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Object obj = auth.getPrincipal();
		User user = null;
		if (obj instanceof UserDetails) {
			user = (User)((CustomUser) obj).getUser();
			website = user.getWebsite();
			
		}		
		ModelAndView mv = new ModelAndView() ;
		String action = request.getParameter("m");
		String websiteId = request.getParameter("websiteId");
		if(action != null && !"".equals(action) && websiteId != null && !"".equals(websiteId))
		{
			//websiteDao.deleteByWebsiteId(websiteId);
		}
		
		List list = null;
		if(website != null)
		{
			if(website.getId() == 1)
			{
				list = websiteDao.findAllbyfind();
			}
			else
			{
				list = websiteDao.findByUserId(user.getId());
			}
		}
		mv.addObject("list", list) ;
		mv.setViewName("listWebsite") ;
		return mv;
	}
    
    @RequestMapping(value = "/deleteWebsite",method = RequestMethod.GET)
    public ModelAndView delete(@RequestParam("id") Short id, ModelMap model)
    {
    	if(!"".equals(id))
    	{
    		websiteDao.delete(websiteDao.load(id));
    	}
		return new ModelAndView(new RedirectView("listWebsite"));
    }
    
    @RequestMapping(value = "/authorize/twitter",method = RequestMethod.GET)
    public String authorizetwitter(@RequestParam("id") Short id, ModelMap model, WebRequest request)
    {
    	log.debug("Authorizing Twitter account {}",id);
    	Website website = websiteDao.get(id);
    	RequestToken requestToken=null;
    	ConfigurationBuilder cb = new ConfigurationBuilder();
    	cb.setDebugEnabled(true);
  	  	cb.setOAuthConsumerKey(website.getSocialAppId());
  	    cb.setOAuthConsumerSecret(website.getSocialApiSecret());
  	    //Initializing twitter object usng app key and secret
  	    log.debug("Initializing twitter object usng app key {} and secret {}",website.getSocialAppId(),website.getSocialApiSecret());
    	Twitter twitter = new TwitterFactory(cb.build()).getInstance();
		try {
			String cURL = twitter_redirect_uri.concat("/gettwitteraccesstoken?id=" + id);
            requestToken = twitter.getOAuthRequestToken(cURL);
            log.debug("Redirect to call back url {}", cURL);
		} catch (TwitterException te) {
			if (401 == te.getStatusCode()) {
				log.error("AuthException. Unable to get the requestToken.",te);
			} else {
				log.error("Unable to get the requestToken. Reason Unknown",te);
			}
		}
		//This request token will be used for generating access token after user authorizes
		request.setAttribute("requestToken", requestToken, WebRequest.SCOPE_SESSION);
    	return "redirect:"+requestToken.getAuthorizationURL();
    }
    
    @RequestMapping(value = "/gettwitteraccesstoken",method = RequestMethod.GET)
    public String gettwitteraccesstoken(@RequestParam("id") Short websiteid, ModelMap model, WebRequest request) throws Exception
    {	
    	String msg = "success";
    	RequestToken requestToken=(RequestToken) request.getAttribute("requestToken",WebRequest.SCOPE_SESSION);
    	if(requestToken==null){
    		//request token not found in session, redirecting to authorize url for generating request token
    		log.error("Unable to authorize user of website {}",websiteid);
    		return "redirect:authorize/twitter?id="+websiteid;
    	}
    	Website website = websiteDao.get(websiteid);
    	ConfigurationBuilder cb = new ConfigurationBuilder();
    	cb.setDebugEnabled(true);
  	  	cb.setOAuthConsumerKey(website.getSocialAppId());
  	    cb.setOAuthConsumerSecret(website.getSocialApiSecret());
    	Twitter twitter = new TwitterFactory(cb.build()).getInstance();
    	AccessToken accessToken = null;
    	 try {
    		 accessToken = twitter.getOAuthAccessToken(requestToken,request.getParameter("oauth_verifier"));
         } catch (TwitterException te) {
        	 msg = "Authorization Failed";
             if (401 == te.getStatusCode()) {
                 log.error("Unauthorized user. Unable to get the access token.");
             } else {
            	 log.error("Unable to get the access token. Reason Unknown",te);
             }
         }
		if (accessToken!=null) {
			website.setAccessToken(accessToken.getToken().trim());
			website.setAccessTokenSecret(accessToken.getTokenSecret().trim());
			log.debug("Accesstoken {} , AccessTokenSecret {}",accessToken.getToken().trim(),accessToken.getTokenSecret().trim());
			websiteDao.saveOrUpdate(website);
			request.removeAttribute("requestToken",WebRequest.SCOPE_SESSION);
		}
		return "redirect:/listWebsite?msg="+msg+"&app=" + website.getSocialAppName();
    }
    
    @RequestMapping(value = "/authorize/facebook",method = RequestMethod.GET)
    public String authorizefacebook(@RequestParam("id") Short id, ModelMap model, 
    					HttpServletRequest request)
    {
    	String _facebook_authorizeurl = "";
    	if(!"".equals(id))
    	{
    		Website website = websiteDao.get(id);
    		/*
			 * String
			 * url="https://graph.facebook.com/oauth/authorize?client_id="
			 * +CLIENT_ID+ "&display=popup" + "&redirect_uri=" +CALLBACK_URL+
			 * "&scope=email,offline_access,user_birthday,user_about_me,user_hometown,user_location,"
			 * + "publish_stream"; //user_online_presence,user_photos,
			 */
    		
    		/**
    		 * https://graph.facebook.com/oauth/authorize?client_id=117787264903013&
    		 * display=popup&redirect_uri=http%3A%2F%2Faudiencemanager.indiatimes.com%2Fgetaccesstoken%3Fid%3D6&
    		 * scope=manage_pages,offline_access,publish_stream
    		 */
    		try
    		{
				log.debug("Geting accesstoken for website id {}", id);
				_facebook_authorizeurl = facebook_authorizeurl.replace("{0}",URLEncoder.encode(website.getSocialAppId(), "UTF-8"));
				_facebook_authorizeurl = _facebook_authorizeurl.replace("{1}",URLEncoder.encode(facebook_redirect_uri + id, "UTF-8"));
    		}
    		catch(UnsupportedEncodingException e)
    		{
    			log.error("UnsupportedEncodingException authoring accesstoken ", e);
    		}
    		catch(Exception e)
    		{
    			log.error("Exception authoring accesstoken ", e);
    		}
    	}
    	log.debug("Redirecting to url {}",_facebook_authorizeurl);
		return "redirect:" + _facebook_authorizeurl;
    }
    
    @RequestMapping(value = "/getaccesstoken",method = RequestMethod.GET)
    public String getaccesstoken(@RequestParam("id") Short websiteid, ModelMap model, 
    		HttpServletRequest request) throws Exception
    {
    	Website website = websiteDao.get(websiteid);
		String code = request.getParameter("code");
		String accessToken = null;
		String error = request.getParameter("error_reason");
		log.debug("code= {} , error= {}", code,error);
		String msg = "success";
		String longLivedToken[] = null;
		
		if(error != null)
		{
			msg = error;
		}
		if (code != null) {
			Integer expires = null;
			
			log.debug("AuthCode {}, client_id {} , client_secret {} , redirect_uri {}",
					new Object[]{code,website.getSocialAppId(), website.getSocialApiSecret(), facebook_redirect_uri + websiteid});
			String url = new String(facebook_accesstoken_url);
			String result = null;
			try {
				url = url.replace("{0}", URLEncoder.encode(website.getSocialAppId(), "UTF-8"));
				url = url.replace("{1}", URLEncoder.encode(facebook_redirect_uri + websiteid, "UTF-8"));
				url = url.replace("{2}", URLEncoder.encode(website.getSocialApiSecret(), "UTF-8"));
				url = url.replace("{3}", URLEncoder.encode(code, "UTF-8"));
				log.debug("Authorizing url {}", url);
			} catch (java.io.UnsupportedEncodingException e) {
				log.error("An error occured in method getuseraccesstoken: ", e);
			}
			try
			{
				String res[] = Utilities.executeGetWithResponsecode(url);
				result = res[1];
			}
			catch(Exception e)
			{
				log.error("Exception while executing url {} \n{}",url,e);
			}
			try {
				log.debug("response for the authorization url is {}", result);
				String[] pairs = result.split("&");
				for (String pair : pairs) {
					log.debug("Pair {}",pair);
					String[] kv = pair.split("=");
					if (kv.length != 2) {
						throw new RuntimeException("Unexpected auth response. Result " + result);
					} else {
						// As per various oauth drafts, it may not start with access_token.
						// In future this check may need to be modified
						if (kv[0].equals("access_token")) {
							accessToken = kv[0] + "=" + kv[1];
						}
						if (kv[0].equals("expires")) {
							expires = Integer.valueOf(kv[1]);
						}
					}
				}
				// As per the new Facebook draft, existing tokens will need to be made long lived and use of offline_access will be deprecated.
				// https://developers.facebook.com/roadmap/offline-access-removal/#extend_token
				log.debug("Generating long lived accesstoken for token {}",accessToken);
				accessToken = FacebookUtils.getextendedaccesstoken(website.getSocialAppId(), website.getSocialApiSecret(),accessToken);
				longLivedToken = accessToken.split("&");
				accessToken = longLivedToken[0];
				
			} catch (Exception e) {
				accessToken = null;
				msg = e.getLocalizedMessage();
				log.error("Exception parsing result",e);
			}
			log.debug("access Token= {} , expires= {}",accessToken,expires);
			if(accessToken != null)
			{
				accessToken = accessToken.trim();
			}
			website.setAccessToken(accessToken);
			// Set token expire date
			if(expires != null)
			{
				website.setExpires(expires);
				website.setExpiresat(Utilities.expiresAt(expires)); 
			}
			if(website.getUserid() == null || "".equals(website.getUserid()))
			{
				String profileJson = FacebookUtils.getprofiledetails(null, accessToken);
				log.debug("facebook user profile json {}",profileJson);
				FacebookProfile facebookProfile = FacebookUtils.parseProfileDetails(profileJson);
				website.setUserid(facebookProfile.getId());
				log.debug("facebook user profile id {}",facebookProfile.getId());
			}
			websiteDao.saveOrUpdate(website);
			
			// Get PageDetail using the accesstoken above
			//regeneratepages(websiteid, model, request);
		}
		
		if(longLivedToken != null && longLivedToken.length == 2)
		{
			return "redirect:listWebsite?msg=" + msg + "&app=" + website.getSocialAppName() + "&" + longLivedToken[1];
		}
		else
		{
			return "redirect:listWebsite?msg=" + msg + "&app=" + website.getSocialAppName();
		}
    }
    
    /**
     * This method is used to invoke linked-in api to initiate the authorization process in getting the accesstoken
     * 
     * @param id
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "/authorize/linkedin",method = RequestMethod.GET)
    public String authorizelinkedin(@RequestParam("id") Short id, ModelMap model, WebRequest request)
    {
    	log.debug("Authorizing Linked-In account {}",id);
    	
  	    String _linkedin_authorizeurl = "";
	  	if(!"".equals(id))
	  	{
	  		Website website = websiteDao.get(id);
	  		
	  		/**
	  		 * https://www.linkedin.com/uas/oauth2/authorization?response_type=code&client_id=yyesmepp56u2
	  						&scope=r_fullprofile%20r_emailaddress%20r_network%20r_contactinfo&state=SAM&redirect_uri={1}
	  		 */
	  		try
	  		{
					log.debug("Geting accesstoken for website id {}", id);
					_linkedin_authorizeurl = linkedin_authorizeurl.replace("{0}",URLEncoder.encode(website.getSocialAppId(), "UTF-8"));
					_linkedin_authorizeurl = _linkedin_authorizeurl.replace("{1}",URLEncoder.encode(linkedin_redirect_uri + id, "UTF-8"));
			  	    //Initializing linked-in object using app key and secret
			  	    log.debug("Initializing linked-in object usng app key {} and secret {}",website.getSocialAppId(),website.getSocialApiSecret());

	  		}
	  		catch(UnsupportedEncodingException e)
	  		{
	  			log.error("UnsupportedEncodingException authoring accesstoken ", e);
	  		}
	  		catch(Exception e)
	  		{
	  			log.error("Exception authoring accesstoken ", e);
	  		}
	  	}
	  	log.debug("Redirecting to url {}",_linkedin_authorizeurl);
		return "redirect:" + _linkedin_authorizeurl;
    }
    
    @RequestMapping(value = "/getlinkedinaccesstoken",method = RequestMethod.GET)
    public String getlinkedinaccesstoken(@RequestParam("id") Short websiteid, ModelMap model, WebRequest request) throws Exception
    {	
    	Website website = websiteDao.get(websiteid);
		String code = request.getParameter("code");
		String state = request.getParameter("state");
		String accessToken = null;
		String error = request.getParameter("error");
		String error_description = request.getParameter("error_description");
		log.debug("code= {} , state={}, error= {}, error_description={}",new Object[]{code,state,error, error_description});
		String msg = "success";
		String longLivedToken[] = null;
		
		if(error != null)
		{
			msg = error;
		}
		if (code != null) {
			Integer expires = null;
			
			log.debug("AuthCode {}, client_id {} , client_secret {} , redirect_uri {}",
					new Object[]{code,website.getSocialAppId(), website.getSocialApiSecret(), linkedin_redirect_uri + websiteid});
			String url = new String(linkedin_accesstoken_url);
			String result = null;
			String[] splitUrl = new String[2];
			try {
				url = url.replace("{0}", URLEncoder.encode(code, "UTF-8"));
				url = url.replace("{1}", URLEncoder.encode(linkedin_redirect_uri + websiteid, "UTF-8"));
				url = url.replace("{2}", URLEncoder.encode(website.getSocialAppId(), "UTF-8"));
				url = url.replace("{3}", URLEncoder.encode(website.getSocialApiSecret(), "UTF-8"));
				splitUrl = url.split("\\?");
				log.debug("[POST] Request Access Token by exchanging the authorization_code for url {} with params {}", splitUrl[0], splitUrl[1]);
			} catch (java.io.UnsupportedEncodingException e) {
				log.error("An error occured in method getuseraccesstoken: ", e);
			}
			try
			{
				String res[] = Utilities.executePostWithResponseCode(splitUrl[0], splitUrl[1]);
				result = res[1];
			}
			catch(Exception e)
			{
				log.error("Exception while executing url {} \n{}",url,e);
			}
			try {
				log.debug("response for the authorization url is {}", result);
				JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(result);
				accessToken = jsonObject.getString("access_token");
				expires = jsonObject.getInt("expires_in");
				// Set token expire date
				website.setExpires(expires);
				website.setExpiresat(Utilities.expiresAt(expires));
				
			} catch (Exception e) {
				accessToken = null;
				msg = e.getLocalizedMessage();
				log.error("Exception parsing result",e);
			}
			log.debug("access Token= {} , expires= {}",accessToken,expires);
			if(accessToken != null)
			{
				accessToken = accessToken.trim();
			}
			website.setAccessToken(accessToken);
			if(website.getUserid() == null || "".equals(website.getUserid()))
			{
				/*String profileJson = FacebookUtils.getprofiledetails(null, accessToken);
				log.debug("facebook user profile json {}",profileJson);
				FacebookProfile facebookProfile = FacebookUtils.parseProfileDetails(profileJson);
				website.setUserid(facebookProfile.getId());
				log.debug("facebook user profile id {}",facebookProfile.getId());*/
			}
			websiteDao.saveOrUpdate(website);
		}
		
		if(longLivedToken != null && longLivedToken.length == 2)
		{
			return "redirect:listWebsite?msg=" + msg + "&app=" + website.getSocialAppName() + "&" + longLivedToken[1];
		}
		else
		{
			return "redirect:listWebsite?msg=" + msg + "&app=" + website.getSocialAppName();
		}
    }
    
    @RequestMapping(value = "/authorize/googleplus",method = RequestMethod.GET)
    public String authorizegoogleplus(@RequestParam("id") Short id, ModelMap model, WebRequest request)
    {
    	log.debug("Authorizing Google Plus account {}",id);
    	Website website = websiteDao.get(id);
    	
  	    //Initializing googleplus object using app key and secret
  	    log.debug("Initializing googleplus object using app key {} and secret {}",website.getSocialAppId(),website.getSocialApiSecret());
  	    
  	    SecureRandom sr1 = new SecureRandom();
		String stateToken = "id="+id;
		
		
		GoogleAuthorizationCodeRequestUrl url = null;
		try {
			// Initializes the Google Authorization Code Flow with CLIENT ID, SECRET, and SCOPE
			Iterable<String> SCOPE = Arrays.asList(googleplus_scopes.split(";"));
			GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, 
															website.getSocialAppId(),website.getSocialApiSecret(), SCOPE).setAccessType("offline").build();
			
			url = flow.newAuthorizationUrl();
			
			url.setRedirectUri(googleplus_redirect_uri);
            log.debug("Redirect to call back url {}", url);
		} catch (Exception te) {
			te.printStackTrace();
		}
		//This request token will be used for generating access token after user authorizes
		return "redirect:" + url.setState(stateToken).build();
    }
    
    @RequestMapping(value = "/getgoogleplusaccesstoken",method = RequestMethod.GET)
    public String getgoogleplusaccesstoken(@RequestParam("state") String state, ModelMap model, WebRequest request) throws Exception
    {	
    	String msg = "success", accessToken = null,refToken=null;
    	/*RequestToken requestToken=(RequestToken) request.getAttribute("requestToken",WebRequest.SCOPE_SESSION);
    	if(requestToken==null){
    		//request token not found in session, redirecting to authorize url for generating request token
    		log.error("Unable to authorize user of website {}",websiteid);
    		return "redirect:authorize/twitter?id="+websiteid;
    	}*/
    	Short websiteid = Short.parseShort(state.split("=")[1]);
    	Website website = websiteDao.get(websiteid);
    	Iterable<String> SCOPE = Arrays.asList(googleplus_scopes.split(";"));
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, 
														website.getSocialAppId(),website.getSocialApiSecret(), SCOPE).setAccessType("offline").build();
		
    	if (request.getParameter("code") != null && request.getParameter("state") != null)
    	{
    		String authCode = request.getParameter("code");
    		log.debug("Authorization code {} received ",authCode);
    		String cURL = googleplus_redirect_uri;
    		GoogleTokenResponse response = flow.newTokenRequest(authCode).setRedirectUri(cURL).execute();
    		log.debug("AccessToken====== {} ",response.getAccessToken());
    		accessToken=response.getAccessToken();
    		refToken=response.getRefreshToken();
    	}
		
		if (refToken!=null) {
			website.setAccessToken(refToken.trim());
			websiteDao.saveOrUpdate(website);
		}
		return "redirect:/listWebsite?msg="+msg+"&app=" + website.getSocialAppName();
    }
    
    
    @RequestMapping(value = "/listPages",method = RequestMethod.GET)
    public String selectpages(@RequestParam("id") Short websiteid, @RequestParam("limit") int limit,
    		@RequestParam("offset") int offset, ModelMap model, 
    		HttpServletRequest request) throws Exception
    {
    	Website website = websiteDao.get(websiteid);
    	String appName=null;
    	Accounts accounts = new Accounts();
    	if(website != null)
    	{
	    	if("FACEBOOK".equalsIgnoreCase(website.getSocialAppName())) {
				String result = FacebookUtils.getaccountdetails(website.getUserid(), website.getAccessToken(), limit,offset);
				log.debug("Result for account connection {}", result);
				Error error = new Error();
				accounts = FacebookUtils.parseAccountsConnection(result); 	
				appName="Facebook";
				PageDetail profileData = new PageDetail();
				String profileJson = FacebookUtils.getprofiledetails(null, website.getAccessToken());
				log.debug("facebook user profile json {}",profileJson);	
				FacebookProfile facebookProfile = FacebookUtils.parseProfileDetails(profileJson);
				if(facebookProfile.getId()==null){
					accounts = FacebookUtils.parseAccountsConnection("{\"data\":[]}"); 
					error=facebookProfile.getError();
					error.setMessage(error.getMessage());
					accounts.setError(error);
				}
				Followers followers = FacebookUtils.parseFacebookFollowers(FacebookUtils.getFacebookFollowers(website.getAccessToken()));			
				Friends friends = FacebookUtils.parseFacebookFriends(FacebookUtils.getFacebookFriends(website.getAccessToken()));	
				profileData.setId(facebookProfile.getId());	
				profileData.setName(facebookProfile.getName());	
				profileData.setAccess_token(website.getAccessToken());	
				
				if(facebookProfile.getId()!=null)
				{	
				profileData.setFriends(friends.getData().length);
				}			
				
				if(facebookProfile.getId()!=null)
				{	
					profileData.setFollowers(followers.getSummary().getTotal_count());
				}
				
				TopicPage tpage = topicPageDao.findByWebsiteAndPageId(websiteid, profileData.getId());
				if(tpage != null) {
					profileData.setExists(true);
				}
				
				accounts.setProfileData(profileData);
			}
			else if ("GOOGLEPLUS".equalsIgnoreCase(website.getSocialAppName())) 
			{
				PageDetail googleprofileData = new PageDetail();
				String freshaccesstoken=GooglePlusUtils.getNewAccessToken(website.getSocialAppId(),website.getSocialApiSecret(),website.getAccessToken(),"refresh_token");
				String profileJson = GooglePlusUtils.getprofiledetails(freshaccesstoken,website.getSocialAppId(),website.getSocialApiSecret());
				accounts = GooglePlusUtils.parseAccountsConnection(profileJson); 
				appName="GooglePlus";
				log.debug("googleplus user profile json {}",profileJson);		
				PageDetail googleProfile = (PageDetail) JSONObject.toBean(
						JSONObject.fromObject(GooglePlusUtils
								.getprofiledetails(freshaccesstoken,
										website.getSocialAppId(),
										website.getSocialApiSecret())),
						googleprofileData, new JsonConfig());
				googleProfile.setId(googleprofileData.getId());
				googleprofileData.setUsername(googleProfile.getName());
				googleprofileData.setName(googleProfile.getName());
				googleprofileData.setPicture(googleProfile.getPicture());
				googleprofileData.setAccess_token(freshaccesstoken);
				Error error = new Error();
				if(googleprofileData.getId()==null){	
					error.setMessage(GooglePlusUtils.getErrorMessages());
					accounts.setError(error);				
				}		
				TopicPage tpage = topicPageDao.findByWebsiteAndPageId(websiteid, googleprofileData.getId());
				if(tpage != null) {
					googleprofileData.setExists(true);
				}
				accounts.setProfileData(googleprofileData);
			}
			else if ("LINKEDIN".equalsIgnoreCase(website.getSocialAppName())) 
			{										
				accounts = LinkedInUtils.parseAccountsConnection(LinkedInUtils.getprofiledetails(null,website.getAccessToken()));
				appName="LinkedIn";	
				Error error = new Error();
				PageDetail linkedInprofileData = new PageDetail();
				String profileJson = LinkedInUtils.getprofiledetails(null,website.getAccessToken());
				log.debug("linkedin user profile json {}",profileJson);
				LinkedInProfile linkedInProfile = LinkedInUtils.parseProfileDetails(profileJson);
				if(linkedInProfile.getId()==null){
					error.setMessage(linkedInProfile.getMessage());
					accounts.setError(error);
				}	
				linkedInprofileData.setId(linkedInProfile.getId());
				linkedInprofileData.setName(linkedInProfile.getName());	
				linkedInprofileData.setAccess_token(website.getAccessToken());	
				accounts.setProfileData(linkedInprofileData);
				TopicPage tpage = topicPageDao.findByWebsiteAndPageId(websiteid, linkedInprofileData.getId());
				if(tpage != null) {
					linkedInprofileData.setExists(true);
				}
				
			}		
			
			else if("TWITTER".equalsIgnoreCase(website.getSocialAppName())){
				accounts=TwitterUtils.getTwitterAccountDetails(website);
				System.out.println(accounts.getData().length);
				appName=  "Twitter";
			}
    	}
		//Code for twitter
		for (PageDetail pageDetail : accounts.getData()) {
			TopicPage topicPage = topicPageDao.findByWebsiteAndPageId(websiteid, pageDetail.getId());
			if(topicPage != null)
			{
				pageDetail.setExists(true);
			}
		}
		PageListBean pageListBean = new PageListBean();
		pageListBean.setAccounts(accounts);
		pageListBean.setLimit(limit);
		pageListBean.setOffset(offset);
		pageListBean.setWebsiteid(websiteid);
		
		model.addAttribute("pageListBean", pageListBean);
		
		model.addAttribute("appName",appName);
		return "listpages";
    }
    
    @RequestMapping(value = "/listPages", method=RequestMethod.POST)
	public String onSubmit(@ModelAttribute("pageListBean") PageListBean pageListBean, BindingResult result, 
						ModelMap modelMap, HttpServletRequest request)
    {
    	log.debug("saving {} pages to db",pageListBean.getPageid());
    	List<TopicPage> topicPagesList = new ArrayList<TopicPage>();
    	Accounts accounts = pageListBean.getAccounts();
    	for(PageDetail pageDetail : accounts.getData())
    	{
    		// Search each pagelist bean id in  selected facebook pages id
    		int fbPageFound = pageListBean.getPageid().indexOf(pageDetail.getId());
    		if(fbPageFound != -1)
    		{
    			TopicPage topicPage = new TopicPage();
    			topicPage.setWebsite(websiteDao.load(pageListBean.getWebsiteid()));
    			topicPage.setPageId(pageDetail.getId());
    			// EntityName may be changed by moderator to make use of this parameter in feed
    			if(pageDetail.getName().length() > 100)
    			{
    				topicPage.setEntityName(pageDetail.getName().substring(0,100));
    			}
    			else
    			{
    				topicPage.setEntityName(pageDetail.getName());
    			}
    			topicPage.setPageName(pageDetail.getName());
    			topicPage.setCategory(pageDetail.getCategory());
    			if(pageDetail.getAccess_token() != null && !"".equals(pageDetail.getAccess_token()))
    			{
    				// Case of facebook
    				if(!pageDetail.getAccess_token().startsWith("access_token"))
    				{
    					pageDetail.setAccess_token("access_token=" + pageDetail.getAccess_token());
    				}
    			}
    			topicPage.setAccessToken(pageDetail.getAccess_token());
    			if(pageDetail.getLikes()!=0){
    				topicPage.setLikes(pageDetail.getLikes());
    			}
    			if(pageDetail.getUsername()!=null){
    				topicPage.setEntityName(pageDetail.getUsername());
    				topicPage.setUsername(pageDetail.getUsername());
    			}
    			if(pageDetail.getWebsite()!=null){
    				topicPage.setUrl(pageDetail.getWebsite());
    			}else{
    				topicPage.setUrl("");
    			}
    			if(pageDetail.getPicture()!=null){
    				topicPage.setPicture(pageDetail.getPicture());
    			}
    			if(pageDetail.getDescription()!=null){
    				topicPage.setDescription(pageDetail.getDescription());
    			}
    				
    			topicPage.setType("PAGE");
    			topicPagesList.add(topicPage);
    		}
    	}
    	if(pageListBean.getAccounts().getProfileData()!=null) {
    		PageDetail pageDetail = pageListBean.getAccounts().getProfileData();
    		int fbPageFound = pageListBean.getPageid().indexOf(pageDetail.getId());
    		if(fbPageFound != -1)
    		{
    			TopicPage topicPage = new TopicPage();
    			topicPage.setWebsite(websiteDao.load(pageListBean.getWebsiteid()));
    			topicPage.setPageId(pageDetail.getId());
    			// EntityName may be changed by moderator to make use of this parameter in feed
    			if(pageDetail.getName().length() > 100)
    			{
    				topicPage.setEntityName(pageDetail.getName().substring(0,100));
    			}
    			else
    			{
    				topicPage.setEntityName(pageDetail.getName());
    			}
    			topicPage.setPageName(pageDetail.getName());
    			topicPage.setCategory(pageDetail.getCategory());
    			topicPage.setType("PROFILE");
    			if(pageDetail.getAccess_token() != null && !"".equals(pageDetail.getAccess_token()))
    			{
    				// Case of facebook
    				if(!pageDetail.getAccess_token().startsWith("access_token"))
    				{
    					pageDetail.setAccess_token("access_token=" + pageDetail.getAccess_token());
    				}
    			}
    			topicPage.setAccessToken(pageDetail.getAccess_token());
    			if(pageDetail.getFollowers() != 0) {
    				topicPage.setFollowers(pageDetail.getFollowers());
    			}
    			if(pageDetail.getFriends() != 0) {
    				topicPage.setFriends(pageDetail.getFriends());
    			}
    			if(pageDetail.getUsername()!=null){
    				topicPage.setEntityName(pageDetail.getUsername());
    				topicPage.setUsername(pageDetail.getUsername());
    			}
    			if(pageDetail.getWebsite()!=null){
    				topicPage.setUrl(pageDetail.getWebsite());
    			}else{
    				topicPage.setUrl("");
    			}
    			if(pageDetail.getPicture()!=null){
    				topicPage.setPicture(pageDetail.getPicture());
    			}
    			if(pageDetail.getDescription()!=null){
    				topicPage.setDescription(pageDetail.getDescription());
    			}
    				
    			
    			topicPagesList.add(topicPage);
    		}
    	}
    	topicPageDao.saveOrUpdateAll(topicPagesList);
    	return "redirect:listPages?msg=success&id=" + pageListBean.getWebsiteid() + "&limit=" + pageListBean.getLimit() +"&offset=" + pageListBean.getOffset();
    }
    
    @RequestMapping(value = "/regeneratepages",method = RequestMethod.GET)
    public String regeneratepages(@RequestParam("id") Short websiteid, ModelMap model, 
    		HttpServletRequest request) throws Exception
    {
    	Website website = websiteDao.get(websiteid);
    	
    	
		
		Accounts accounts = null;
		if ("Facebook".equalsIgnoreCase(website.getSocialAppName())) {
			String result = FacebookUtils.getaccountdetails(website.getUserid(),website.getAccessToken(),50,0);
			accounts = FacebookUtils.parseAccountsConnection(result);
			log.debug("Result for account connection {}",result);
		}else if("TWITTER".equalsIgnoreCase(website.getSocialAppName())){
			accounts = TwitterUtils.getTwitterAccountDetails(website);
		}
		
		
		log.debug("Size of pageDetailList {}, \n pageDetailList {}",accounts.getData().length,accounts.getData());
		
		List<TopicPage> topicPageList = topicPageDao.findByWebsiteId(website.getId());
		List<TopicPage> insertTopicPageList = new ArrayList<TopicPage>(); 
		boolean topicPageFound = false;
		for (PageDetail pageDetail : accounts.getData()) {
			if(pageDetail.getId() != null && !"".equals(pageDetail.getId()))
			{
				TopicPage updateTopic = null;
				for (TopicPage topicPage : topicPageList) {
					if(pageDetail.getId().equalsIgnoreCase(topicPage.getPageId()))
					{
						topicPageFound = true;
						updateTopic = topicPage;
						break;
					}
				}
				if(topicPageFound==false)
				{
					TopicPage insertTopicPage = new TopicPage();
					insertTopicPage.setAccessToken("access_token=" + pageDetail.getAccess_token());
					insertTopicPage.setCategory(pageDetail.getCategory());
					insertTopicPage.setEntityName(pageDetail.getName().replace(" ", "-"));
					insertTopicPage.setPageName(pageDetail.getName());
					insertTopicPage.setPageId(pageDetail.getId());
					insertTopicPage.setWebsite(website);
					insertTopicPage.setUrl("");
					insertTopicPageList.add(insertTopicPage);
				}
				else
				{
					topicPageFound = false;
					updateTopic.setAccessToken("access_token=" + pageDetail.getAccess_token());
					updateTopic.setCategory(pageDetail.getCategory());
					updateTopic.setEntityName(pageDetail.getName().replace(" ", "-"));
					updateTopic.setPageName(pageDetail.getName());
					updateTopic.setPageId(pageDetail.getId());
					updateTopic.setWebsite(website);
					if(pageDetail.getWebsite()!=null){
						updateTopic.setUrl(pageDetail.getWebsite());
					}else{
						updateTopic.setUrl("");
					}
					
					insertTopicPageList.add(updateTopic);
				}
			}
		}
		topicPageDao.saveOrUpdateAll(insertTopicPageList);
		
		return "redirect:listWebsite";
    }
    
    
    @RequestMapping(value = "/regenerateTopicAccessToken",method = RequestMethod.GET)
    public String regenerateTopicAccessToken(@RequestParam("id") Short websiteid, ModelMap model, 
    		HttpServletRequest request) throws Exception{
    	
    	Website website = websiteDao.get(websiteid);
    	
    	List<TopicPage> topicPageList = topicPageDao.findByWebsiteId(websiteid);
    	
    	List<TopicPage> updatedTopicList = new ArrayList<TopicPage>();
    	
    	if(topicPageList.size() == 0 && topicPageList.isEmpty()){
    		model.addAttribute("msg","None Topic Page Found");
    		return "redirect:listWebsite";
    	}
    	
    	String appName=null;
    	
    	int topicPageSize = topicPageList.size();
    	
    	Accounts accounts=null;
    	int limit = 50;
    	int offset = 0;
    	
    	while (topicPageSize != 0) {
    		
        	if ("FACEBOOK".equalsIgnoreCase(website.getSocialAppName())) {
    			String accountDetails = FacebookUtils.getaccountdetails(website.getUserid(),website.getAccessToken(),limit,offset);
    			log.debug("Result for account connection {}", accountDetails);
    			accounts = FacebookUtils.parseAccountsConnection(accountDetails);
    			appName="Facebook";
    		}
        	
        	if(accounts == null || accounts.getData() == null){
        		String message = accounts == null ? "Accounts data is null" : ( accounts.getError() != null ? accounts.getError().getMessage() : "Error Occurred in data");
        		model.addAttribute("msg",message );
        		return "redirect:listWebsite";
        	}        	
        	
    		for (PageDetail pageDetail : accounts.getData()) {    			
    			if(pageDetail.getId() != null && !"".equals(pageDetail.getId())){
	    			for (TopicPage topicPage : topicPageList) {
	    				if(pageDetail.getId().equalsIgnoreCase(topicPage.getPageId()))
	    	    		{
	    					topicPage.setAccessToken("access_token=".concat(pageDetail.getAccess_token()));
	    					updatedTopicList.add(topicPage);
		            		topicPageSize--;
	    	    		}	    				
	    			}
	    		}
    		}
    		
    		if(accounts.getData().length == 0)
    		{
    			topicPageSize--;
    		}
    		offset = offset + limit;
    		topicPageDao.saveOrUpdateAll(updatedTopicList);
		}
    	
    	return "redirect:listWebsite?msg=success&app=" + appName.concat(" Topic Pages");
    }
    
}
