package com.til.service.job;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.til.service.common.dao.EmailsDao;

/**
 * @author Harsh.Gupta
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:root-context.xml", "classpath:scheduler.xml" })
public class EmailNotificationJobTest {
	
	/*@Autowired
	private EmailsDao 			emailsDao = null ;
	@Autowired
	private JavaMailSender 		mailSender = null ;*/
	@Autowired
	EmailNotificationJob emailNotificationJob;
	
	@Test
	public void testEmailNotification() {

		emailNotificationJob.execute();
	}
	
}  
