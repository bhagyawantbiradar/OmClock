package com.ast.clock.receivers;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.ast.clock.activities.AlarmTriggeredActivity;

import java.util.Calendar;

public class AlarmReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Calendar calendar = Calendar.getInstance();

        int hourOfTheDay = calendar.get(Calendar.HOUR_OF_DAY);

        if(hourOfTheDay<23 && hourOfTheDay>4){
            Intent i = new Intent(context, AlarmTriggeredActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            context.startActivity(i);
        }

    }
}
