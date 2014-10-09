package com.til.service.job;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:root-context.xml", 
                                    "classpath:scheduler.xml"})
public class AnalyticsJobTest {
	
	@Autowired
	AnalyticsJob analyticsJob;
	
	@Test
    public void testRunFacebookAnalytics() {
		
		analyticsJob.runFacebookAnalytics();
    }
}
