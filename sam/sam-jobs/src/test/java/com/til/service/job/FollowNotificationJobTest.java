package com.til.service.job;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.til.service.common.dao.TopicPageDao;
import com.til.service.common.dao.WebsiteDao;
import com.til.service.common.dao.hibernate.entity.TopicPage;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:root-context.xml", "classpath:scheduler.xml" })
public class FollowNotificationJobTest {

	@Autowired
	FollowNotificationJob followNotificationJob;
	
	@Autowired
	TopicPageDao topicPageDao;
	
	@Autowired
	WebsiteDao websiteDao;
	
	// @Test
	public void testexecute() {

		followNotificationJob.runToiTopic();
	}

	// @Test
	public void testRunToiL1Schedule() {

		followNotificationJob.runToiL1Schedule();
	}

	@Test
	public void addNewTopicPage() {
		
		Short websiteid=6;
		try {
			// Open the file that is the first
			// command line parameter
			FileInputStream fstream = new FileInputStream("C:\\TP.csv");
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {
				// Print the content on the console
				System.out.println(strLine);
				TopicPage topicPage = topicPageDao.findByUrl(strLine);
				if(topicPage == null)
				{
					topicPage = new TopicPage();
					topicPage.setWebsite(websiteDao.load(websiteid));
					topicPage.setEntityName(strLine.substring(strLine.lastIndexOf("/")+1));
					topicPage.setUrl(strLine);
					topicPage.setLink(strLine);
					topicPage.setFeedUrl("http://timesofindia.indiatimes.com/topic/{0}/latestnews");
					topicPage.setActive(true);
					topicPageDao.save(topicPage);
				}
				else
				{
					System.out.println(strLine + " already added.");
				}
				
			}
			// Close the input stream
			in.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
}
