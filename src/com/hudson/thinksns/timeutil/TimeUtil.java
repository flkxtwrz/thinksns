package com.hudson.thinksns.timeutil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * 
 * @author ʯ�ٲ�
 * 
 */
public class TimeUtil {
	public static long getTimestamp(Date d) throws ParseException {
		long l = d.getTime() / 1000L;
		System.out.println(l);
		return l;
	}

	/**
	 * 
	 * ��ʱ���ת����date
	 * 
	 * @param l
	 */

	public static String totime(Long l) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = sdf.format(new Date(l * 1000L));
		return date;
	}

}