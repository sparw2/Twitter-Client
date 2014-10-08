package com.sandeep.apps.twitterclient.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import android.text.format.DateUtils;

public class DateUtil {

	
	// getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
	public static String getRelativeTimeAgo(String rawJsonDate) {
		String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
		SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
		sf.setLenient(true);
	 
		String relativeDate = "";
		try {
			long dateMillis = sf.parse(rawJsonDate).getTime();
			relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
					System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
			
			// hack but makes it better looking
			relativeDate = relativeDate.replace("minutes ago", "m");
			relativeDate = relativeDate.replace("minute ago", "m");
			
			relativeDate = relativeDate.replace("seconds ago", "s");
			relativeDate = relativeDate.replace("second ago", "s");
			relativeDate = relativeDate.replace("in 0 seconds", "0s");
			
			relativeDate = relativeDate.replace("hours ago", "h");
			relativeDate = relativeDate.replace("hour ago", "h");
		
			relativeDate = relativeDate.replace("days ago", "d");
			relativeDate = relativeDate.replace("day ago", "d");
		
			
			relativeDate = relativeDate.replace(" ","");
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
	 
		return relativeDate;
	}
}
