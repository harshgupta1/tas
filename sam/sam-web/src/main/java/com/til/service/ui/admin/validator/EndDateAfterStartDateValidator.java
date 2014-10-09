package com.til.service.ui.admin.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.BeanUtils;

import com.til.service.utils.DateUtil;

public class EndDateAfterStartDateValidator implements ConstraintValidator<EndDateAfterStartDate, Object> {

	private String startdate = null;
	private String enddate = null;
	private String format = null;
	
	public void initialize(EndDateAfterStartDate date) {
		startdate = date.startdate();
		enddate = date.enddate();
		format = date.format();
	}

	public boolean isValid(final Object value,ConstraintValidatorContext validatorContext) {
		
		try
		{
			final Object startDateObj = BeanUtils.getProperty(value, startdate);
            final Object endDateObj = BeanUtils.getProperty(value, enddate);

			SimpleDateFormat sourceDateFormat = new SimpleDateFormat(format);
			try {
				Date startdatedt = DateUtil.parse((String)startDateObj, sourceDateFormat);
				Date enddatedt = DateUtil.parse((String)endDateObj, sourceDateFormat);
				if(startdatedt != null && enddatedt != null)
				{
					if(enddatedt.before(startdatedt))
					{
						return false;
					}
				}
			} catch (ParseException e) {
				return true;
			}
		}
		catch(Exception e)
		{
			// ignore
		}
		return true;
	}

}
