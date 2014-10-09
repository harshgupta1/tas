package com.til.service.toi.api;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.til.service.common.api.BreakingNews;
import com.til.service.common.dao.TopicPageDao;
import com.til.service.common.dao.TopicPageHistoryDao;
import com.til.service.common.dao.WebsiteDao;
import com.til.service.common.dao.hibernate.entity.TopicPage;
import com.til.service.common.dao.hibernate.entity.TopicPageHistory;
import com.til.service.common.dao.hibernate.entity.Website;
import com.til.service.toi.ToiUtils;
import com.til.service.utils.DateUtil;
import com.til.service.utils.Utilities;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:root-context.xml" })
public class TOITwitterBreakingNewsFollowImplTest {

	@Autowired
	WebsiteDao websiteDao;

	@Autowired
	ApplicationContext	context;

	@Autowired
	Jaxb2Marshaller jaxb2Marshaller;
	@Autowired
	TopicPageDao topicPageDao;  

	@Autowired
	TopicPageHistoryDao topicPageHistoryDao;


	@Test
	public void testrun() {

		Website website = websiteDao.get(new Short("44"));
		if (website != null && website.getActive()) {
			// Construction is allowed using this constructor only
			TOITwitterBreakingNewsFollowImpl toiTwitterBreakingNewsFollowImpl = (TOITwitterBreakingNewsFollowImpl)context.getBean("toiTwitterBreakingNewsFollowImpl",1,1,website);
			// start threads and place in runnable state
			toiTwitterBreakingNewsFollowImpl.run();
		}
	}


	public  void testFeed() {

		String[] feedXML = null;
		try {
			//<?xml version="1.0" encoding="UTF-8"?><daylifefeeds><site source="Times Of India"><item><headline>Sanjay Dutt’s Rs 40-lakh apology to Ajay Devgn</headline><source>Times Of India</source><timestamp_epoch type="int4"/><timestamp type="str">2012-01-12 10:54:56</timestamp><excerpt>Upset over the debacle of Rascals, the actor buys the rights of a hit Telugu film to gift his friend</excerpt><url>http://timesofindia.indiatimes.com/entertainment/bollywood/news-interviews/sanjay-dutts-rs-40/lakh-apology-to-Ajay-Devgn/articleshow/11458689.cms</url></item><item><headline>Ajay Devgan a big prankster: Arjan Bajwa</headline><source>Times Of India</source><timestamp_epoch type="int4"/><timestamp type="str">2012-01-02 17:15:24</timestamp><excerpt>Arjan Bajwa, who recently worked with Ajay Devgan in Son of a Sardar, says that Devgan is a big prankster</excerpt><url>http://timesofindia.indiatimes.com/entertainment/bollywood/news-interviews/Ajay-Devgan-a-big-prankster-Arjan-Bajwa/articleshow/11338985.cms</url></item><item><headline>Ajay Devgan a big prankster: Arjan Bajwa</headline><source>Times Of India</source><timestamp_epoch type="int4"/><timestamp type="str">2012-01-02 15:32:41</timestamp><excerpt>Arjan Bajwa, who recently worked with Ajay Devgan in Son of a Sardar, says that Devgan is a big prankster</excerpt><url>http://timesofindia.indiatimes.com/entertainment/bollywood/news-interviews/Ajay-Devgan-a-big-prankster-Arjan-Bajwa/articleshow/11337927.cms</url></item><item><headline>When Anurag Kashyap recommended Arjan Bajwa!</headline><source>Times Of India</source><timestamp_epoch type="int4"/><timestamp type="str">2011-12-26 14:59:55</timestamp><excerpt>Actor Arjan Bajwa says that Anurag Kashyap's recommendation about him to Mani Ratnam worked wonders for his career</excerpt><url>http://timesofindia.indiatimes.com/entertainment/bollywood/news-interviews/When-Anurag-Kashyap-recommended-Arjan-Bajwa/articleshow/11253668.cms</url></item><item><headline>Kangana hopes 2012 will be landmark year</headline><source>Times Of India</source><timestamp_epoch type="int4"/><timestamp type="str">2011-11-24 17:43:55</timestamp><excerpt>Kangana Ranaut has been on a roll this year with a long line-up of releases like "Tanu Weds Manu", "Game", "Double Dhamaal" and "Rascals".</excerpt><url>http://timesofindia.indiatimes.com/entertainment/bollywood/news-interviews/Kangana-hopes-2012-will-be-landmark-year/articleshow/10856787.cms</url></item></site></daylifefeeds>
			//feedXML = Utilities.executeGetWithResponsecode("http://timesofindia.indiatimes.com/topic/ajay-devgan/latestnews");
			feedXML = Utilities.executeGetWithResponsecode("http://navbharattimes.indiatimes.com/rssfeedsdefault.cms");
			DayLifeFeedsV2 breakingNews = ToiUtils.parseDayLifeFeedsV2(jaxb2Marshaller, feedXML[1]);
			System.out.println(breakingNews.getSiteList());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}





	//public static void main(String[] args) {

	//@Test
	@SuppressWarnings("deprecation")
	public void addDate(){
		//TopicPage tp = topicPageDao.findById(87);
		List<TopicPage> tpList = topicPageDao.findAll();
		//tpList.add(tp);
		int tpSize=tpList.size();
		try {
			for(TopicPage topicPage:tpList){
				tpSize--;
				System.out.println("list size {"+tpSize+"}");
				Date startDate = new java.sql.Date((new GregorianCalendar(2011, 11, 28)).getTime().getTime());
				Date endDate = new java.sql.Date((new GregorianCalendar(2012, 0, 3)).getTime().getTime());
				while(startDate.before(endDate)){
					Date tempDate = new Date(startDate.getYear(),startDate.getMonth(),startDate.getDate());
					TopicPageHistory tph= topicPageHistoryDao.findByTopicPageAndDate(topicPage, startDate);
					/*if(tph!=null){
						System.out.println("start Date:{"+startDate+"} list size {"+tpSize+"}");
						break;
					}*/
					System.out.println(" Entity " + topicPage.getEntityName());
					int i = 0;
					while((tph==null && i>=0) || (tph!=null && tph.getLikes()<=0)){
						i--;
						tempDate.setDate(tempDate.getDate()-1);
						tph = topicPageHistoryDao.findByTopicPageAndDate(topicPage, tempDate);
					}
					if(tph == null) break;
					while(tph!=null && tempDate.before(startDate)){

						tempDate.setDate(tempDate.getDate()+1);
						
						TopicPageHistory topicPageHistory = new TopicPageHistory();
			        	topicPageHistory.setTopicPage(topicPage);
			        	topicPageHistory.setLikes(tph.getLikes());
			        	topicPageHistory.setDislikes(tph.getDislikes());
			        	topicPageHistory.setShares(tph.getShares());
			        	java.util.Date date = new java.util.Date(tempDate.getTime());
			        	date.setHours(date.getHours()+1);
			        	topicPageHistory.setCreatedate(date);

						topicPageHistoryDao.save(topicPageHistory);
					}
					
					if(tph==null ){
						System.out.println("##########################################");
					}
					System.out.println("start Date:{"+startDate+"} list size {"+tpSize+"}");
					startDate.setDate(startDate.getDate()+1);
				}
				

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}




}
