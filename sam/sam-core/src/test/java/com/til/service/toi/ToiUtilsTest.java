package com.til.service.toi;

import net.sf.json.JSONObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.til.service.common.api.BreakingNews;
import com.til.service.common.api.Rss2Feeds;
import com.til.service.toi.api.DayLifeFeeds;
import com.til.service.toi.api.DayLifeFeedsV2;
import com.til.service.toi.api.DayLifeFeedsV2.Site;
import com.til.service.utils.Utilities;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:root-context.xml" })
public class ToiUtilsTest {
	
	@Autowired
	Jaxb2Marshaller jaxb2Marshaller;
	
	//@Test
	public void testParseRss2Feeds()
	{
		String targetURL= "http://blogs.timesofindia.indiatimes.com/main/feed/entries/rss";
		String rssString = "<?xml version=\"1.0\"?><?xml-stylesheet type=\"text/xsl\" href=\"/xslshow.cms?path=/rssfeeds1\"?><rss version=\"2.0\" xmlns:atom=\"http://www.w3.org/2005/Atom\"><channel><title>Cricket on Times of India | Live Cricket Score, Cricket News, India Cricket</title><link>http://timesofindia.indiatimes.com/articlelist/4719161.cms</link><description>Times of India Cricket provide Live Cricket Score for India Cricket Matches and Cricket Series along with latest Cricket News, Cricket Records, Cricket Info, ICC Rankings and Indian Premier League</description><language>en-gb</language><lastBuildDate>Mon, 17 Oct 2011 09:16:24 GMT</lastBuildDate><atom:link rel=\"self\" type=\"application/rss+xml\" href=\"http://timesofindia.indiatimes.com/rssfeeds/4719161.cms\" /><copyright>Copyright:(C) 2011 Bennett Coleman &amp; Co. Ltd, http://in.indiatimes.com/policyterms/1554651.cms</copyright><docs>http://syndication.indiatimes.com/</docs><image><title>Cricket on Times of India | Live Cricket Score, Cricket News, India Cricket</title><url>http://timesofindia.indiatimes.com/photo/507610.cms</url><link>http://timesofindia.indiatimes.com/articlelist/4719161.cms</link></image><item><title>Kaneria rubbishes Salman Butt's claims</title><description>&lt;a href=\"http://timesofindia.indiatimes.com/sports/cricket/top-stories/Spot-fixing-Kaneria-rubbishes-Salman-Butts-claims/articleshow/10368863.cms\"&gt;&lt;img border=\"0\" hspace=\"10\" align=\"left\" style=\"margin-top:3px;margin-right:5px;\" src=\"http://timesofindia.indiatimes.com/photo/10368863.cms\" /&gt;&lt;/a&gt;Danish Kaneria has taken exception to the statement given by Salman Butt during spot-fixing trial that he was introduced to Mazhar Majeed by the leg-spinner.</description><link>http://timesofindia.indiatimes.com/sports/cricket/top-stories/Spot-fixing-Kaneria-rubbishes-Salman-Butts-claims/articleshow/10368863.cms</link><guid>http://timesofindia.indiatimes.com/sports/cricket/top-stories/Spot-fixing-Kaneria-rubbishes-Salman-Butts-claims/articleshow/10368863.cms</guid><pubDate>Sat, 15 Oct 2011 15:47:53 GMT</pubDate></item></channel></rss>";
		try {
			String rss = Utilities.executeGet(targetURL);
			Rss2Feeds rss2Feeds = ToiUtils.parseRss2Feeds(jaxb2Marshaller, rss);
			System.out.println(rss2Feeds.getChannel().getItemList().size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void testParseiBeatFeed()
	{
		String targetURL= "http://ibeat.indiatimes.com/iBeat/mashup.html?time=&type=quick&articleId=&host=timesofindia.indiatimes.com";
		try {
			String json = Utilities.executeGet(targetURL);
			JSONObject jsonObject = ToiUtils.parseiBeatFeed(json);
			long hourlyClicks = jsonObject.getJSONObject("11106547").getLong("hourlyClicks");
			//long hourlyClicks = clicks.getLong("hourlyClicks");
			System.out.println(hourlyClicks);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void testParseiBeatLast4HoursFeed()
	{
		String targetURL= "http://ibeat.indiatimes.com/DashboardUtil/ArticleCount?aid=10397262,10398414,10413182,10399250&jsoncallback=";
		try {
			String json = Utilities.executeGet(targetURL);
			JSONObject jsonObject = ToiUtils.parseiBeatLast4HoursFeed(json);
			JSONObject dataObject = jsonObject.getJSONObject("DATA");
			long $4hourlyClicks = dataObject.getLong("10413182");
			System.out.println($4hourlyClicks);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//@Test
	public void testParseDayLifeFeeds() throws Exception
	{
		String targetURL = "http://timesofindia.indiatimes.com/topic/john-abraham/latestnews";
		String daylifefeed = Utilities.executeGet(targetURL);
		DayLifeFeeds dayLifeFeeds = ToiUtils.parseDayLifeFeeds(jaxb2Marshaller, daylifefeed);
		System.out.println(dayLifeFeeds.getItemList().size());
		System.out.println(dayLifeFeeds.getItemList().get(0).getHeadline());
		System.out.println(dayLifeFeeds.getItemList().get(0).getPubDate());
	}
	
	//@Test
	public void testParseBreakingNewsFeeds() throws Exception
	{
		String targetURL = "http://timesofindia.indiatimes.com/breakingnewsxml.cms";
		String feed ;//= Utilities.executeGet(targetURL);
		feed = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><BreakingNews><News><NewsTitle>Rajya Sabha to discuss Lokpal Bill tomorrow: TV reports</NewsTitle></News><News><NewsTitle>Lok Sabha to discuss Lokpal Bill tomorrow: TV reports</NewsTitle></News></BreakingNews>";
		//feed = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><BreakingNews/>";
		BreakingNews breakingNews = ToiUtils.parseBreakingNewsFeeds(jaxb2Marshaller, feed);
		System.out.println(breakingNews.getNewsList());
	}
	
	@Test
	public void testParseDayLifeFeedsV2() throws Exception
	{
		String targetURL = "http://timesofindia.indiatimes.com/topic/anna-hazare/latestnews2";
		String daylifefeed = Utilities.executeGet(targetURL);
		DayLifeFeedsV2 dayLifeFeeds = ToiUtils.parseDayLifeFeedsV2(jaxb2Marshaller, daylifefeed);
		System.out.println(dayLifeFeeds.getSiteList().size());
		for (Site site : dayLifeFeeds.getSiteList()) {
			System.out.println(site.getSource());
		}
	}
}
