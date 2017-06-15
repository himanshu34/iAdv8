package com.agl.product.adw8_new.utils;

import android.net.ParseException;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {
	private static final String TAG = "DateHelper ::";

	public static String getDateUrlFormat(int day, int month, int year) {

		String dateUrlFormat = year + "-" + month + "-" + day;
		return dateUrlFormat;
	}

	public static String getDateDisplayFormat(int day, int month, int year) {
		
		String strDate = day + "." + month + "." + year;
		String displayFormat = "MMM dd,yyyy";
		final SimpleDateFormat parseFormat = new SimpleDateFormat("dd.MM.yyyy");
		final SimpleDateFormat formattingFormat = new SimpleDateFormat(displayFormat);

		try {
			strDate = formattingFormat.format(parseFormat.parse(strDate));
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		return strDate;
	}

	public static String lastWeek() {
		String date = null;
		/*
		 * Calendar c = Calendar.getInstance(); int mdate =
		 * c.get(Calendar.DATE); System.out.println("mdate " + mdate);
		 */

		String currentdate = new SimpleDateFormat("MMM dd,yyyy")
				.format(new Date().getTime() - 86400000L); // 1 * 24 * 60 * 60 *
															// 1000)
		String lastWeek = new SimpleDateFormat("MMM dd,yyyy").format(new Date()
				.getTime() - 604800000L); // 7 * 24 * 60 * 60 * 1000);
		// System.out.println("mmdate " + currentdate +" last 7 days " +
		// lastWeek);

		date = String.valueOf(currentdate + " - " + lastWeek);
		Log.e(TAG, date);
		return String.valueOf(date);
	}

//	public static String getCurrentDate() {
//		String currentdate = new SimpleDateFormat("MMM dd,yyyy")
//				.format(new Date().getTime() - 86400000L); // 1 * 24 * 60 * 60 *
//															// 1000)
//		return currentdate;
//	}
//
//	public static String getCurrentDateUrlFormat() {
//		String currentdate = new SimpleDateFormat("yyyy-MM-dd")
//				.format(new Date().getTime() - 86400000L); // 1 * 24 * 60 * 60 *
//		// 1000)
//		return currentdate;
//	}

	public static String getLastSevenDays() {
		String lastWeek = new SimpleDateFormat("MMM dd,yyyy").format(new Date()
				.getTime() - 604800000L); // 7 * 24 * 60 * 60 * 1000);

		return lastWeek;

	}
    public static String getLastSevenDaysPrevoius() {
        String lastWeek = new SimpleDateFormat("MMM dd,yyyy").format(new Date()
                .getTime() - 518400000L); // 6 * 24 * 60 * 60 * 1000);

        return lastWeek;

    }

	public static String getLastSevenDaysUrlFormat() {
		String lastWeek = new SimpleDateFormat("yyyy-MM-dd").format(new Date()
				.getTime() - 604800000L); // 7 * 24 * 60 * 60 * 1000);
		return lastWeek;
	}

	public static String lastMonth() {
		String date = null;

		String lastmonth = new SimpleDateFormat("MMM dd,yyyy")
				.format(new Date().getTime() - 2592000000L); // 30 * 24 * 60 *
																// 60 * 1000);
		return lastmonth;
	}

	public static String lastMonthUrlFormat() {
		String lastmonth = new SimpleDateFormat("yyyy-MM-dd").format(new Date()
				.getTime() - 2592000000L); // 30 * 24 * 60 * // 60 * 1000);
		return lastmonth;
	}

	public static String yesterday() {
		String date = null;
		String lastDay = new SimpleDateFormat("MMM dd,yyyy").format(new Date()
				.getTime() - 86400000L); // 1 * 24 * 60 * 60 * 1000);
		return lastDay;
	}

	public static String yesterdayUrlFormat() {
		String date = null;
		String lastDay = new SimpleDateFormat("yyyy-MM-dd").format(new Date()
				.getTime() - 86400000L); // 1 * 24 * 60 * 60 * 1000);
		return lastDay;
	}
	public static String today() {
		String date = null;
		String toDay = new SimpleDateFormat("MMM dd,yyyy").format(new Date()
		.getTime() ); // 1 * 24 * 60 * 60 * 1000);
		return toDay;
	}
	
	public static String todayUrlFormat() {
		String date = null;
		String toDay = new SimpleDateFormat("yyyy-MM-dd").format(new Date()
		.getTime()); // 1 * 24 * 60 * 60 * 1000);
		return toDay;
	}

	public static String getLast8Days() {
		String last8Days = new SimpleDateFormat("MMM dd,yyyy")
				.format(new Date().getTime() - 691200000L); // 8 * 24 * 60 * 60*
															// 1000
		return last8Days;
	}
	
	public static String getLast8DaysUrlFormat() {
		String last8Days = new SimpleDateFormat("yyyy-MM-dd")
		.format(new Date().getTime() - 691200000L); // 8 * 24 * 60 * 60*
		// 1000
		return last8Days;
	}

	public static String getLast15Days() {
		String last15Days = new SimpleDateFormat("MMM dd,yyyy")
				.format(new Date().getTime() - 1296000000L); // 15 * 24 * 60 *
																// 60 * 1000
		return last15Days;
	}
	public static String getLast15DaysUrlFormat() {
		String last15Days = new SimpleDateFormat("yyyy-MM-dd")
		.format(new Date().getTime() - 1296000000L); // 15 * 24 * 60 *
		// 60 * 1000
		return last15Days;
	}

	public static long fromStringToDate(String mdate) {
		long getDate = 0L;
		Log.e(TAG, "new Date().getTime() " + new Date().getTime() + " mdate = "
				+ mdate);
		SimpleDateFormat format = new SimpleDateFormat("MMM dd,yyyy");
		try {

			Date date = format.parse(mdate);
			getDate = date.getTime();
			Log.e(TAG, "fromDateToString " + date);
		} catch (ParseException e) {
			Log.e(TAG, e.toString());
		} catch (java.text.ParseException e) {
			Log.e(TAG, e.toString());
		}
		return getDate;
	}

	public static String setBtnToEndDate(long toDate, long daysDifference) {
		String differenceDate = new SimpleDateFormat("MMM dd,yyyy")
				.format(toDate + (daysDifference * 24 * 60 * 60 * 1000));
		return differenceDate;
	}
	
	public static String setBtnToEndDateUrlFormat(long toDate, long daysDifference) {
		String differenceDate = new SimpleDateFormat("yyyy-MM-dd")
		.format(toDate + (daysDifference * 24 * 60 * 60 * 1000));
		return differenceDate;
	}

	private String monthToString(int month) {
		String mMonth = "";
		switch (month) {
		case 1:
			mMonth = "Jan";
			break;
		case 2:
			mMonth = "Feb";
			break;
		case 3:
			mMonth = "Mar";
			break;
		case 4:
			mMonth = "Apr";
			break;
		case 5:
			mMonth = "May";
			break;
		case 6:
			mMonth = "Jun";
			break;
		case 7:
			mMonth = "Jul";
			break;
		case 8:
			mMonth = "Aug";
			break;
		case 9:
			mMonth = "Sep";
			break;
		case 10:
			mMonth = "Oct";
			break;
		case 11:
			mMonth = "Nov";
			break;
		case 12:
			mMonth = "Dec";
			break;
		default:
			break;
		}
		return mMonth;

	}
	public static String getCurrentDateUrlFormat() {
	//  String currentdate = new SimpleDateFormat("yyyy-MM-dd") .format(new Date().getTime() - 86400000L); // 1  24  60  60  // 1000)
	  String currentdate = new SimpleDateFormat("yyyy-MM-dd") .format(new Date().getTime()); // 1  24  60  60  // 1000)
	  return currentdate;
	 }
	 public static String getCurrentDate() {
	//  String currentdate = new SimpleDateFormat("MMM dd,yyyy") .format(new Date().getTime() - 86400000L); // 1  24  60  60  // 1000)
	  String currentdate = new SimpleDateFormat("MMM dd,yyyy") .format(new Date().getTime()); // 1  24  60  60  // 1000)
	  return currentdate;
	 }
}
