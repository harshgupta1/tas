package com.til.service.linkedin;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.til.service.common.api.Accounts;
import com.til.service.common.api.Paging;
import com.til.service.facebook.api.Error;
import com.til.service.facebook.api.PageDetail;
import com.til.service.facebook.exception.OAuthException;
import com.til.service.linkedin.api.LinkedInProfile;
import com.til.service.linkedin.api.OAuthRequest;
import com.til.service.linkedin.api.Response;
import com.til.service.linkedin.api.Verb;
import com.til.service.utils.Utilities;

@Service
public class LinkedInUtils {

	private static final Logger log = LoggerFactory
			.getLogger(LinkedInUtils.class);

	private static final String PEOPLE_API_URL = "https://api.linkedin.com/v1/people/~:(id,first-name,last-name,picture-url,headline,formatted-name,num-connections,summary,public-profile-url,num-recommenders,email-address)";
	
	/*public static OAuthService getAuthService(Website w) {
		OAuthService service = new ServiceBuilder().provider(LinkedInApi.class)
				.apiKey(w.getSocialAppId()).apiSecret(w.getSocialApiSecret())
				.build();

		return service;
	}*/

	public static String getprofiledetails(String userid, String accesstoken)
			throws IOException, Exception {
		/**
		 * https://api.linkedin.com/v1/people/~
		 */
		StringBuilder url = new StringBuilder();
		url.append(PEOPLE_API_URL);
		url.append("?format=json&oauth2_access_token=");
		url.append(accesstoken); // access_token= will/should be appended to
									// this
		log.debug("Getting profile details for userid {}", userid);
		return Utilities.executeGet(url.toString());
	}
	
	public static LinkedInProfile parseProfileDetails(String json)
			throws OAuthException {
		try {

			LinkedInProfile profile = new LinkedInProfile();
			LinkedInProfile linkedinProfile = (LinkedInProfile) JSONObject.toBean(JSONObject.fromObject(json), profile, new JsonConfig());
			log.debug("LinkedInProfile {}", json);
			return linkedinProfile;
		} catch (Exception e) {
			log.error("Exception parsing JSON String " + json
					+ " while building LinkedInProfile Object ", e);
			throw new OAuthException(e.getLocalizedMessage());
		}
	}

	public static Accounts parseLinkedInAccountsConnection(String json,
			String token) throws OAuthException {
		PageDetail pageDetail = new PageDetail();
		PageDetail linkedinProfile = (PageDetail) JSONObject.toBean(
				JSONObject.fromObject(json), pageDetail, new JsonConfig());
		try {
			pageDetail.setId(linkedinProfile.getFirstName() + "");
			pageDetail.setUsername(linkedinProfile.getFirstName());
			pageDetail.setName(linkedinProfile.getFirstName() + " " + linkedinProfile.getLastName());
			pageDetail.setAccess_token(token);
		}

		catch (Exception e) {
			log.error("Exception in getting details from LinkedIn", e);
		}
		PageDetail[] data = new PageDetail[1];
		Accounts accounts = new Accounts();
		data[0] = pageDetail;
		accounts.setData(data);
		Paging paging = new Paging();
		accounts.setPaging(paging);
		return accounts;
	}

	public static Accounts parseAccountsConnection(String json)
			throws OAuthException {

		try {
			JSONObject jsonObject = JSONObject.fromObject("{\"data\":[]}");
			Accounts accounts = (Accounts) JSONObject.toBean(jsonObject, Accounts.class);
			if (accounts.getData() != null)
				log.debug("Total pages retrieved {}", accounts.getData().length);
			else {
				log.debug("Error message {}", accounts.getError().getMessage());
			}
			return accounts;
		} catch (Exception e) {
			log.error("Exception parsing JSON String " + json + " while building Accounts Connection Object ", e);
			throw new OAuthException(e.getLocalizedMessage());
		}
	}

	public static PageDetail parsePageDetail(String jsonStr) throws Exception {

		try {
			JSONObject json = (JSONObject) JSONSerializer.toJSON(jsonStr);
			PageDetail pg = (PageDetail) JSONObject.toBean(json,
					PageDetail.class);
			return pg;
		} catch (Exception e) {
			log.error("Exception in method parsePageDetail(String jsonStr) while parsing json string " + jsonStr, e);
			throw e;
		}
	}

	public static PageDetail postfeed(String accesstoken, String pageid,
			String feedurl, String comment, String title, String imageurl,
			String description) {

		OAuthRequest oAuthRequest = new OAuthRequest(Verb.POST,
										"https://api.linkedin.com/v1/people/~/shares?format=json&oauth2_access_token=" + accesstoken + "");
		StringWriter sw = new StringWriter();

		// Preparing XML payload to Share Content via LinkedIn

		try {
			// create contents
			Content c = new Content();
			if (description != null) {
				c.setDescription(description);
			}

			if (feedurl != null) {
				c.setSubmittedurl(feedurl);
			}

			if (imageurl != null) {
				c.setSubmittedimageurl(imageurl);
			}

			if (title != null) {
				c.setTitle(title);
			}

			Visibility v = new Visibility();
			v.setCode("anyone");

			// create share object, assigning content,visibility
			Share s = new Share();
			s.setComment(comment);
			s.setVisibility(v);
			s.setContent(c);

			// create JAXB context and instantiate marshaller

			JAXBContext context = JAXBContext.newInstance(Share.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			// Write to String xml
			m.marshal(s, sw);
		} catch (Exception e) {
			e.printStackTrace();
		}
		oAuthRequest.addPayload(sw.toString());
		oAuthRequest.addHeader("Content-Type", "text/xml");
		// send the request
		Response response = oAuthRequest.send();
		// print the response from server
		log.debug("response.getBody() = " + response.getBody());
		PageDetail pD = null;
		String postid = null;
		String errorMsg = null;
		try {
			pD = parsePageDetail(response.getBody());
			postid = pD.getUpdateKey();
			log.debug("postid========" + postid);
		} catch (Exception e) {
		}
		if (postid != null) {
			return pD;
		} else {
			//PageDetail pageDetail = new PageDetail();
			Error error = new Error();
			error.setMessage(pD.getMessage());
			pD.setError(error);
			return pD;
		}

	}

}
