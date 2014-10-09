package com.til.service.ui.admin.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.til.service.utils.DateUtil;

public class DateFormatValidator implements ConstraintValidator<DateFormat, Object> {

	private String format = null;
	
	public void initialize(DateFormat dateFormat) {
		format = dateFormat.format();
	}

	public boolean isValid(Object object,ConstraintValidatorContext validatorContext) {
		
		if(object != null)
		{
			if(object instanceof String && !"".equals(object))
			{
				SimpleDateFormat sourceDateFormat = new SimpleDateFormat(format);
				try {
					DateUtil.parse((String)object, sourceDateFormat);
				} catch (ParseException e) {
					return false;
				}
			}
			
		}
		return true;
	}

}
