package com.ast.clock.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.ast.clock.activities.AlarmTriggeredActivity;
import com.ast.clock.utitilies.ForegroundCheckTask;
import com.ast.clock.R;

import java.util.concurrent.ExecutionException;


public class AlarmService extends IntentService {


    public AlarmService() {
        super("AlarmService");
    }

    @Override
    public void onHandleIntent(Intent intent) {
        try {
            sendNotification();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sendNotification() throws ExecutionException, InterruptedException {
        NotificationManager alarmNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent;
        Intent intent;
        intent = new Intent(this, AlarmTriggeredActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        contentIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);


        NotificationCompat.Builder alamNotificationBuilder = new NotificationCompat.Builder(
                this).setContentTitle(getString(R.string.app_name)).setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(getString(R.string.what_to_do_with_reminder)))
                .setOngoing(true)
                .setContentText(getString(R.string.what_to_do_with_reminder));

        NotificationCompat.Action action;

        boolean isApplicationOnForegroud = new ForegroundCheckTask().execute(this).get();

        if (isApplicationOnForegroud)
            action = new NotificationCompat.Action(R.drawable.ic_more_apps, "Open", contentIntent);
        else
            action = new NotificationCompat.Action(R.drawable.ic_more_apps, "Open", null);
        alamNotificationBuilder.addAction(action);
        alarmNotificationManager.notify(0, alamNotificationBuilder.build());
    }
}
