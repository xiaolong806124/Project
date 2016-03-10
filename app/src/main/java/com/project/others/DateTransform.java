package com.project.others;

import java.util.Calendar;
import java.util.Date;

public class DateTransform {
	// transform each time to millisecond.
	final static long SECOND = 1000;
	final static long MINUTE = 60 * SECOND;
	final static long HOUR = MINUTE * 60;
	final static long DAY = HOUR * 24;

	public DateTransform() {
	}

	public Date getDate(int hour, int minute) {
		// we should consider the effect of time zone.
		long now = Math.round((new Date().getTime() + 8 * HOUR) / DAY) * DAY;
		// as for the chinese time zone, here subtracts 8 hours.
		long time = now - (8 - hour) * HOUR + minute * MINUTE;
		return new Date(time);
	}

	public Date getDate(int hour, int minute, int second) {
		// 锟斤拷锟斤拷锟斤拷锟斤拷燃锟斤拷锟�小时锟斤拷时锟斤拷睿拷锟斤拷锟矫诧拷锟斤拷准确锟斤拷锟斤拷锟斤拷.
		long now = Math.round((new Date().getTime() + 8 * HOUR) / DAY) * DAY;
		// 锟斤拷为锟叫癸拷时锟斤拷锟斤拷锟斤拷猓拷锟斤拷锟斤拷锟揭拷偌锟饺�小时,锟斤拷然时锟斤拷锟�
		long time = now - (8 - hour) * HOUR + minute * MINUTE;
		return new Date(time);
	}

	public Date getDate(int day, int hour, int minute, int second) {
		// 锟斤拷锟斤拷只锟斤拷锟斤拷氐锟绞憋拷锟斤拷锟斤拷貌锟揭伙拷锟斤拷锟矫达拷欤�
		long now = Math.round((new Date().getTime() + 8 * HOUR) / DAY) * DAY;
		Calendar cal = Calendar.getInstance();
		int day1 = cal.get(Calendar.DAY_OF_MONTH);
		// 锟斤拷锟斤拷锟斤拷锟侥砫ay锟斤拷锟斤拷锟絤s.
		now = now - (day1 - day) * DAY;
		long time = now - (8 - hour) * HOUR + minute * MINUTE + second * SECOND;
		return new Date(time);
	}

	// ????
	public Date getDate(int month, int day, int hour, int minute, int second) {
		// 锟斤拷锟斤拷只锟斤拷锟斤拷氐锟绞憋拷锟斤拷锟斤拷貌锟揭伙拷锟斤拷锟矫达拷欤�
		long now = Math.round((new Date().getTime() + 8 * HOUR) / DAY) * DAY;
		Calendar cal = Calendar.getInstance();
		int day1 = cal.get(Calendar.DAY_OF_MONTH);
		// 锟斤拷锟斤拷锟斤拷锟絛ay锟斤拷锟斤拷锟絤s锟斤拷
		now = now - (day1 - day) * DAY;
		long time = now + hour * HOUR + minute * MINUTE + second * SECOND;
		return new Date(time);
	}
}
