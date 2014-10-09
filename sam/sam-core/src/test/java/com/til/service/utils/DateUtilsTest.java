package com.til.service.utils;

import java.text.ParseException;
import java.util.Date;

import org.junit.Test;

public class DateUtilsTest {
	
	@Test
	public void testDateParse()
	{
		try {
			Date feedtimestamp = DateUtil.parse("2011-07-24 22:06:00", DateUtil.defaultTimestampFormat());
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
