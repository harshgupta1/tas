package com.til.service.ui.admin.validator;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = EndDateAfterStartDateValidator.class)
@Documented
public @interface EndDateAfterStartDate {

	String message() default "{com.til.service.ui.admin.validator.enddateafterstartdate}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	String startdate();

	String enddate();
	
	String format();
}
