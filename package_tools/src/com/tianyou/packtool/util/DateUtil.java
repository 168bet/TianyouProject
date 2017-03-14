package com.tianyou.packtool.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	public static String getCurrentTime(){
		Date date=new Date();
		DateFormat format=new SimpleDateFormat("yyyy_MM_dd-HH_mm_ss");
		String time=format.format(date);
		time = time.split("-")[1];
		return time;
	}
}
