package com.til.service.common.api;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.til.service.utils.DateUtil;

public class DateAdapter extends XmlAdapter<String, Date> {

    private SimpleDateFormat dateFormat = DateUtil.getRfc822DateFormat();

    @Override
    public String marshal(Date v) throws Exception {
        return dateFormat.format(v);
    }

    @Override
    public Date unmarshal(String v) throws Exception {
    	return dateFormat.parse(v);
    }
}
