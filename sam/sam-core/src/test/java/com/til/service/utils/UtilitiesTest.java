package com.til.service.utils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.til.service.common.dao.ArticleDao;
import com.til.service.common.dao.TopicPageDao;
import com.til.service.common.dao.WebsiteDao;
import com.til.service.common.dao.hibernate.entity.Article;
import com.til.service.common.dao.hibernate.entity.TopicPage;
import com.til.service.common.dao.hibernate.entity.Website;
import com.til.service.toi.ToiUtils;
import com.til.service.toi.api.DayLifeFeedsV2;
import com.til.service.toi.api.DayLifeFeedsV2.Site;
import com.til.service.toi.api.DayLifeFeedsV2.Site.ItemV2;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:root-context.xml" })
public class UtilitiesTest {
	
	@Autowired
	Jaxb2Marshaller jaxb2Marshaller;
	
	@Autowired
	ArticleDao articleDao;
	
	@Autowired
	WebsiteDao websiteDao;
	
	@Autowired
	TopicPageDao topicPageDao;
	
	//@Test
    /*public void testencodePassword() {
		String md5 = Utilities.encodePassword("a-requiem-for-mario-miranda", "MD5");
		System.out.println(md5);
    }*/
	
	/*@Test
    public void testgetMinifiedURL() throws Exception {
		
		String api = "http://toi.in/micron/restshortner.html?login=times&apiKey=R_66d9bc26baca7507b68b196a47657721&format=json&url=";
		String url = "http://timesofindia.indiatimes.com/world/us/Diamond-gifted-to-Liz-Taylor-by-Burton-sells-for-record-88m/articleshow/11114202.cms";
		TOIMinifiedURL toiMinifiedURL = Utilities.getMinifiedURL(api + URLEncoder.encode(url, "UTF-8"));
		System.out.println(toiMinifiedURL.getData().getUrl());
    }*/
	
	@Test
    public void testexecuteGetWithResponsecode()throws Exception {
		
		Website w = websiteDao.get(new Short("12"));
		TopicPage t = topicPageDao.get(393);
		
		String targetURL = "http://timesofindia.indiatimes.com/topic/ajay-devgan/latestnews";
		String[] data = Utilities.executeGetWithResponsecode(targetURL);
		data[1] = URLEncoder.encode(data[1], "UTF-8");
		data[1] = URLDecoder.decode(data[1], "UTF-8");
		DayLifeFeedsV2 obj =  ToiUtils.parseDayLifeFeedsV2(jaxb2Marshaller, data[1]);
		for (Site site : obj.getSiteList()) {
			for (ItemV2 item : site.getItemList()) {
				
				Article article = new Article();
				article.setTopicPage(t);
				article.setWebsite(w);
				article.setSocialappid("201934563233871");
				article.setTopicName("Ajay-Devgan");
				article.setMessage(item.getHeadline());
				article.setUrl(item.getUrl());
				article.setProcessed(false);
				article.setRowlock(false);
				article.setCreatedate(new Date());
				article.setUpdatedate(new Date());
				articleDao.save(article);
				break;
			}
			break;
		} 
		System.out.println(data[1]);
    }
	
	@Test
    public void testexecutePostWithResponsecode()throws Exception {
		
		
		String targetURL = "https://graph.facebook.com/216305435091412/links";
		String urlParameters = "access_token=AAABrIHwVZA2UBAC2JZBPsPzAhVZBStYyVUqpoxrMugWCIOZChRWoPolY30p0xZBQ4xYSfQmwPxBsfigpeZASOJSROOgfrYgSGPvZApOhggeEQZDZD&message=Seminar+held+on+new+IIT-JEE+pattern&link=http%3A%2F%2Ftimesofindia.indiatimes.com%2Fcity%2Fbhopal%2Fseminar-held-on-new-iit%2FJEE-pattern%2Farticleshow%2F12490178.cms%3Futm_source%3Dfacebook%26utm_medium%3DtoitopicsonFB%26utm_campaign%3DIIT-JEE%26from%3D1449503695";
		String[] data = Utilities.executePostWithResponseCode(targetURL, urlParameters);
		
    }
}
