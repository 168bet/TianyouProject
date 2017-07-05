package com.k3k.unplat.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.convert.converter.Converter;

public class DateConverter implements Converter<String, Date> {

	public Date convert(String arg0) {
		// TODO Auto-generated method stub
		if (StringUtils.isBlank(arg0))
			return null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		try {
			return dateFormat.parse(arg0);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
