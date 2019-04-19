package com.ast.clock.utitilies;

import android.content.Context;
import android.text.format.DateFormat;

import com.ast.clock.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;



public class StringDateTime {

    public static String getDateInString(long millis, Context context) {
        SimpleDateFormat dateFormat;
        String df = "dd MMMM";
        dateFormat = new SimpleDateFormat(df, GlobalMethods.getCurrentLocale(context));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return dateFormat.format(calendar.getTime());
    }

    public static String getTimeInString(long millis, Context context) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        String ampm = GlobalMethods.getAMPM(hour);
        final boolean systemDateFormat = DateFormat.is24HourFormat(context);

        if (systemDateFormat) {
            if (hour <= 9)
                return "0" + hour + ":" + GlobalMethods.pad(min);
            return hour + ":" + GlobalMethods.pad(min);
        } else {
            if (hour >= 12) {
                if (hour != 12)
                    hour -= 12;
            }
            if (hour <= 9)
                return (hour == 0 ? 12 : "0" + hour) + ":" + GlobalMethods.pad(min) + " " + ampm;
            return (hour == 0 ? 12 : hour) + ":" + GlobalMethods.pad(min) + " " + ampm;
        }

    }

    public static String getDayOfWeek(Context context, long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek) {
            case Calendar.MONDAY:
                return context.getString(R.string.mon);
            case Calendar.TUESDAY:
                return context.getString(R.string.tue);
            case Calendar.WEDNESDAY:
                return context.getString(R.string.wed);
            case Calendar.THURSDAY:
                return context.getString(R.string.thu);
            case Calendar.FRIDAY:
                return context.getString(R.string.fri);
            case Calendar.SATURDAY:
                return context.getString(R.string.sat);
            case Calendar.SUNDAY:
                return context.getString(R.string.sun);
        }
        return "";
    }

    public static String getDayOfWeekInList(Context context, int weekInt) {
        switch (weekInt) {
            case Calendar.MONDAY:
                return context.getString(R.string.mon);
            case Calendar.TUESDAY:
                return context.getString(R.string.tue);
            case Calendar.WEDNESDAY:
                return context.getString(R.string.wed);
            case Calendar.THURSDAY:
                return context.getString(R.string.thu);
            case Calendar.FRIDAY:
                return context.getString(R.string.fri);
            case Calendar.SATURDAY:
                return context.getString(R.string.sat);
            case Calendar.SUNDAY:
                return context.getString(R.string.sun);
        }
        return "";
    }


}
