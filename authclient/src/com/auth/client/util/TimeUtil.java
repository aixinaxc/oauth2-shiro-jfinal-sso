package com.auth.client.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

	//转10位时间输出,时间为当前时间
	public static long getTime(){
		Date date=new Date();
		long time = date.getTime();
		return time/1000;	
	}
	
	//输入2000-02-16格式时间，输出10数组字符串
	public static long getTime(String times){
		 SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd");
	     Date date = null;
		try {
			date = simpleDateFormat .parse(times);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 long time = date.getTime();
		 return time/1000;	
	}
	
	
	
}
