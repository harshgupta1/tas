/*
 * Copyright 2006-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.til.service.facebook;

import static com.til.service.facebook.FacebookUtils.getPageDetail;
import static com.til.service.facebook.FacebookUtils.getapplicationaccesstoken;
import static com.til.service.facebook.FacebookUtils.parsePageLinks;
import static com.til.service.facebook.FacebookUtils.postfeed;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import net.sf.ezmorph.MorphException;
import net.sf.ezmorph.bean.MorphDynaBean;

import org.junit.Test;

import com.til.service.facebook.api.Insight;
import com.til.service.facebook.api.Insights;
import com.til.service.facebook.api.PageDetail;
import com.til.service.utils.Utilities;

public class FacebookUtilsTests {
	
	String accesstoken;
	
	//@Before
    public void setUp() {
		//testGetapplicationaccesstoken();
    }
	
	public void testGetapplicationaccesstoken() {
		String clientid = "113447012037545";
		String clientsecret = "e6f849ad003f107f7cd5f437f52af93b";
		String accesstoken = getapplicationaccesstoken(clientid, clientsecret);
		this.accesstoken = accesstoken;
		System.out.println(accesstoken);
		assertNotNull(accesstoken);
		
	}
	
	//@Test
	public void testGetextendedapplicationaccesstoken()throws Exception {
		String clientid = "117787264903013";
		String clientsecret = "65fc0c185a330ac97bdb844d078000b3";
		String token = URLEncoder.encode("access_token=AAABrIHwVZA2UBAKNvcaA6Hi6ZByK1rJFGBhfBW8yv0h08jJFYZB6DjRytiCNIqzQb1ZA0IwUtO7QmWncR5k8SzOCwdI2nNgJqQ0LzI8ozgZDZD", "UTF-8");
		String accesstoken = FacebookUtils.getextendedaccesstoken(clientid, clientsecret,token);
		this.accesstoken = accesstoken;
		System.out.println(accesstoken);
		assertNotNull(accesstoken);
		
	}
	
	//@Test
	public void testParsePageDetailList() throws Exception
	{
		String targetURL = "https://graph.facebook.com/1449503695/accounts?access_token=113447012037545|60ff61bc2f5efbf0197ae063.1-1449503695|J3sBUMXOyG86EW2ZWDv3CGGTuCw&limit=100&offset=100";
		String json = Utilities.executeGet(targetURL);
		FacebookUtils.parseAccountsConnection(json);
	}
	
	//@Test
	public void testGetPageDetail()
	{
		StringBuilder pagelinks = new StringBuilder();
		//pagelinks.append("http://testtoi.blogs.indiatimes.com/masala-noodles/entry/ysr-brother-wants-he-man-image");
		//pagelinks.append("http://testtoi.blogs.indiatimes.com/masala-noodles/entry/budget-message-jai-bolo-spectrum-maharaj-ki");
		pagelinks.append("http://timesofindia.indiatimes.com/topic/Stephanie-Hightower");
		//pagelinks.append("http://testtoi.blogs.indiatimes.com/randomaccess/entry/in-digital-age-can-a-government-censor-a-map");
		//pagelinks.append(",http://testtoi.blogs.indiatimes.com/tiger-trail/entry/the-raj-of-rajnikanth");
		//pagelinks.append(",http://testtoi.blogs.indiatimes.com/Dads-Eye/entry/let-the-results-haze-lift-please");
		//pagelinks.append(",http://testtoi.blogs.indiatimes.com/Dads-Eye/entry/let");
		String pageDetail = getPageDetail(pagelinks.toString());
		System.out.println(pageDetail);
		assertNotNull(pageDetail);
		testParse(pagelinks.toString(), pageDetail);
	}
	
	
	public void testParse(String pagelinks, String pageDetail)
	{
		Random randomGenerator = new Random();
		
		String pageLinks[]={"http://timesofindia.indiatimes.com/tech/news/internet/Facebook-Skype-to-launch-video-chat-service-Report/articleshow/9109077.cms",
							"http://economictimes.indiatimes.com/tech/internet/facebook-may-launch-skype-powered-video-chat/articleshow/9088030.cms",
							//"http://timesofindia.indiatimes.com/entertainment/hollywood/news-interviews/Transformers-tiny-robots-call-Megan-Fox-mean/articleshow/9049073.cms",
							//"http://timesofindia.indiatimes.com/sports/more-sports/others/Sub-junior-district-basketball-from-July-12/articleshow/9092249.cms",
							"http://timesofindia.indiatimes.com/sports/football/english-premier-league/top-stories/top-stories/Tevez-says-he-must-quit-Man-City-Report/articleshow/9106493.cms",
							//"http://economictimes.indiatimes.com/news/politics/nation/govt-not-committed-to-any-timeline-to-clear-lokpal-bill/articleshow/9119688.cms"
							};
		String message[]={"Facebook-Skype-to-launch-video-chat-service",
						"facebook-may-launch-skype-powered-video-chat",
						//"Transformers-tiny-robots-call-Megan-Fo",
						//"Sub-junior-district-basketball",
						"Tevez-says:--",
						//"govt-not-committed"
						};
		
		Map<String, PageDetail> pageDetailMap = parsePageLinks(pagelinks, pageDetail);
		assertEquals("Map Size ", pagelinks.split(",").length, pageDetailMap.size());
		Set entries = pageDetailMap.entrySet(); 
		for (Iterator iterator = entries.iterator(); iterator.hasNext();) 
		{
			Map.Entry entry = (Map.Entry) iterator.next();
			PageDetail page = (PageDetail)entry.getValue();
			System.out.println(page.getId());
			
			try
			{
				Long.parseLong(page.getId());
				int randomInt = randomGenerator.nextInt(pageLinks.length);
				//testPostFeed(page.getId(),pageLinks[randomInt],message[randomInt]);
				PageDetail feedPageDetail= postfeed(accesstoken, page.getId(), pageLinks[randomInt], message[randomInt]);
				System.out.println("FeedId:- " + feedPageDetail.getId());
				System.out.println("Error Message:- " + feedPageDetail.getError().getMessage());
			}
			catch(NumberFormatException e)
			{
				e.printStackTrace();
			}
		}
		
	}
	
	//@Test
	public void testPostFeed(String pageid, String link, String message)
	{
		PageDetail pageDetail= postfeed(accesstoken, pageid, link, message);
		System.out.println("FeedId:- " + pageDetail.getId());
		System.out.println("Error Message:- " + pageDetail.getError().getMessage());
	}
	
	//@Test
	public void testPostFeed()
	{
		PageDetail pageDetail= postfeed("access_token=113447012037545|dK6_7raHKLIX2t0be-TNrl4IyJM", "242343665794400", "http://www.huffingtonpost.com/2011/07/15/belgian-court-acquits-sle_n_899652.html?ir=World%3Fncid%3Dedlinkusaolp00000009", "Is this acquittal fair?");
		System.out.println("FeedId:- " + pageDetail.getId());
		System.out.println("Error Message:- " + pageDetail.getError().getMessage());
	}
	
	//@Test
	public void testGetprofiledetails() throws Exception
	{
		String json = FacebookUtils.getprofiledetails(null,"access_token=AAABrIHwVZA2UBAAEmfKx6WCSO2qgdLTiMxWG7MnOKse7ps6M7gkgiZAsTXNVbaAjUEPX2W0Vn0KvtxijpgBjBrUDu59ZBAZD");
		FacebookUtils.parseProfileDetails(json);
	}
	
	//@Test
	public void testPostlinks()
	{
		String accesstoken = "access_token=CAABrIHwVZA2UBACDoRvrpGvmNMhLrg95d5NG0skvWPUEIsUhHj656RqZCiqDlgB0MxaUzw6Ux58y6ZAJxxxcmzkrNQcojvfZCtvF3aMbtldK8w8TnKwNsk9UDLJ0nSucgM00ODGoRHXSCeSU1TKe";
		//String accesstoken = "access_token=AAABrIHwVZA2UBANhBn5UtIJzXsXZA6QzL1TQKEKZAZBttlMeHoVo1OSSYkkRht45lXO4ZB6OdsVHiPF6Rn5oxGR9htKR9MVy41tpVSEqiXgZDZD";
		String pageid = "100004035193014";
		PageDetail pageDetail= FacebookUtils.postlinks(accesstoken, pageid, "http://timesofindia.indiatimes.com/india/Declare-Narendra-Modi-as-PM-candidate-RSS-tells-BJP/articleshow/21563169.cms?utm_source=facebook.com&utm_medium=referral&utm_campaign=Bosco-Dominique", "Declare Narendra Modi as PM candidate, RSS tells BJP");
		System.out.println("FeedId:- " + pageDetail.getId());
		System.out.println("Error Message:- " + pageDetail.getError().getMessage());
	}
	
	@Test
	public void testPostImage()
	{
		System.out.println(System.currentTimeMillis() + (5*60*1000));
		String accesstoken = "CAABrIHwVZA2UBAG9suZBBISEziKC1LbF5T3hbrXgJoMRZBZCl7ZBCUeftBUH09TQ1G34IhoxaAdQQE2e67xB14sCBZAd7i3zQTOU87vvO6YZBbz7QehdoDAJgyZBKiG5HMmi4cusnRqxgOhmZCJNEI9ckoRBwofh6ZBGskYPZAZBPgGZCNBsw9a17XLC5yj7IaLOpZB34ZD";
		String pageid = "me";
		File f = new File("D://shobhan.png");
		PageDetail pageDetail= FacebookUtils.postImage(accesstoken, pageid, f,  "Seer ready to deposit Rs 10 lakh as surety for another gold hunt\r\n\r\nhttp://timesofindia.indiatimes.com/india/Seer-ready-to-deposit-Rs-10-lakh-as-surety-for-another-gold-hunt/articleshow/26333475.cms");
		System.out.println("FeedId:- " + pageDetail.getId());
		System.out.println("Error Message:- " + pageDetail.getError().getMessage());
	}
	
	//@Test
	public void testdeletepost()
	{
		//String accesstoken = "access_token=AAABrIHwVZA2UBAD6ksboZB4VsTYRkSXV8IgBpaiqxUWPSZAy3DgKcJqJG7VnrW31WgpVDcwGyomkPELveTjmZC8EelQfwpqwOOQggMBJPwZDZD";
		String accesstoken = "access_token=117787264903013|ed0786c97288a304f884f54f.1-100000938118596|bl6ZG4f5udc-0keLGAT3ynKiLBA";
		String pageid = "272076842819775";
		String postid = "217672301634538";
		String result = FacebookUtils.deletepost(accesstoken, pageid, postid);
		System.out.println("Result is " + result);
	}
	
	//@Test
	public void testparseInsightsConnection()throws Exception
	{
		String targetURL = "https://graph.facebook.com/266680173356605_115318881937530/insights?access_token=AAABrIHwVZA2UBAMnauuXNy2kIVPLuqr7Wpr3eMzxif2g09wmjhisaKcbzsqTWcKWHqpZB2y9RdrFLDagTk4cpcbcZBWKnf70EmACVZCCdQZDZD";
		String json = Utilities.executeGet(targetURL);
		Insights result = FacebookUtils.parseInsightsConnection(json);
		System.out.println("Result is " + result.getData().length);
		for (Insight insight  : result.getData()) {
			System.out.println("Name " + insight.getName());
			MorphDynaBean bean =  insight.getValues().get(0);
			try
			{
				System.out.println(bean.contains("values","value"));
				System.out.println(bean.get("value"));
				System.out.println(bean.get("link clicks"));
			}catch(MorphException e)
			{
				e.printStackTrace();
			}
			System.out.println("Values " + insight.getValues().get(0));
		}
	}
	
	//@Test
	public void testgetpostinsights()
	{
		String accesstoken = "access_token=AAABrIHwVZA2UBAMnauuXNy2kIVPLuqr7Wpr3eMzxif2g09wmjhisaKcbzsqTWcKWHqpZB2y9RdrFLDagTk4cpcbcZBWKnf70EmACVZCCdQZDZD";
		String pageid = "266680173356605";
		String postid = "115318881937530";
		String json = FacebookUtils.getpostinsights(accesstoken, pageid, postid);
		System.out.println(json);
	}
	
	
}
