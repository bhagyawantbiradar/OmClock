package com.ast.clock.utitilies;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class GlobalMethods {

    @TargetApi(Build.VERSION_CODES.N)
    public static Locale getCurrentLocale(Context contexr) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return contexr.getResources().getConfiguration().getLocales().get(0);
        } else {
            //noinspection deprecation
            return contexr.getResources().getConfiguration().locale;
        }
    }

    public static String pad(int input) {
        String str;
        if (input >= 10)
            str = Integer.toString(input);
        else
            str = "0" + Integer.toString(input);
        return str;
    }

    public static String getAMPM(int hour) {
        String ampm;
        if (hour >= 12) {
            ampm = "PM";
        } else {
            ampm = "AM";
        }
        return ampm;
    }


    public static void displayCustomToastLong(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        ViewGroup group = (ViewGroup) toast.getView();
        TextView messageTextView = (TextView) group.getChildAt(0);
        messageTextView.setTextSize(25);
        toast.show();
    }

    public static void displayCustomToastShort(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        ViewGroup group = (ViewGroup) toast.getView();
        TextView messageTextView = (TextView) group.getChildAt(0);
        messageTextView.setTextSize(25);
        toast.show();
    }


    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);

        }
    }


}

