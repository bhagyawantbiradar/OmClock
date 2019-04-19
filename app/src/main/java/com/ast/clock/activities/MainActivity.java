package com.ast.clock.activities;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ast.clock.utitilies.Constants;
import com.ast.clock.utitilies.MyAnalogClock;
import com.ast.clock.R;
import com.ast.clock.utitilies.StoredData;
import com.ast.clock.receivers.AlarmReceiver;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    @BindView(R.id.iv_info)
    ImageView ivInfo;
    @BindView(R.id.iv_share)
    ImageView ivShare;
    @BindView(R.id.iv_minus)
    ImageView ivMinus;
    @BindView(R.id.iv_plus)
    ImageView ivPlus;
    @BindView(R.id.sb_volume)
    SeekBar sbVolume;
    @BindView(R.id.tv_show_bell)
    TextView tvShowBell;
    @BindView(R.id.iv_show_bell)
    ImageView ivShowBell;
    @BindView(R.id.sb_bell)
    Switch sbBell;
    @BindView(R.id.iv_close_bell)
    ImageView ivCloseBell;
    @BindView(R.id.card_bell_settings)
    CardView cardBellSettings;
    @BindView(R.id.vector_analog_clock)
    MyAnalogClock vectorAnalogClock;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initSettings();


    }

    private void initSettings() {
        sbBell.setChecked(StoredData.getBoolean(this, Constants.PREF_IS_BELL_ENABLED, false));
        sbVolume.setProgress(StoredData.getInt(this, Constants.PREF_VOLUME, 50));
        initializeClockWithBells(sbBell.isChecked());
    }

    private void initializeClockWithBells(boolean isWithBell) {
        if(isWithBell)
        vectorAnalogClock.initializeCustom(R.drawable.ic_face_selected,R.drawable.hours_hand,R.drawable.minutes_hand,R.drawable.second_hand);
        else vectorAnalogClock.initializeCustom(R.drawable.ic_face_unselected,R.drawable.hours_hand,R.drawable.minutes_hand,R.drawable.second_hand);
        Calendar currentTimeCalendar = Calendar.getInstance();
        vectorAnalogClock.setCalendar(currentTimeCalendar)
                .setOpacity(1.0f)
                .setShowSeconds(false)
                .setColor(Color.WHITE);
    }

    @Override
    protected void onResume() {

        if (Constants.isForceStopped) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                String packageName = getPackageName();
                PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
                if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                    showDialogToOptOutFromBatteryOptimization();
                } else {
                    setAlarm();
                }
            } else {
                setAlarm();
            }
        }
        super.onResume();

    }

    private void showDialogToOptOutFromBatteryOptimization() {

        final AlertDialog alertDialog  = new AlertDialog.Builder(this)
                .setTitle("Allow permission")
                .setMessage("Please enable battery optimization permission.")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        openBatteryOptimization();
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void openBatteryOptimization() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
        startActivity(intent);
    }

    private void setAlarm() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,calendar.get(Calendar.HOUR_OF_DAY)+1);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);

        Log.d(TAG, "setAlarm: "+calendar.getTimeInMillis());

        Intent intentAlarmReciever = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, Constants.NOTIFY_ID, intentAlarmReciever, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_HOUR, pendingIntent);

        Constants.isForceStopped = false;
    }

    @OnClick({R.id.iv_share, R.id.iv_plus, R.id.iv_minus, R.id.iv_info, R.id.iv_show_bell, R.id.iv_close_bell, R.id.tv_show_bell})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_show_bell:
            case R.id.tv_show_bell:
                cardBellSettings.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_close_bell:
                cardBellSettings.setVisibility(View.GONE);
                break;
            case R.id.iv_minus:
                changeVolume(false);
                break;
            case R.id.iv_plus:
                changeVolume(true);
                break;
        }

    }

    @OnCheckedChanged(R.id.sb_bell)
    public void onBellSettingChanged() {
        if (sbBell.isChecked()) {
            StoredData.putBoolean(this, Constants.PREF_IS_BELL_ENABLED, true);
            initializeClockWithBells(true);
        } else {
            StoredData.putBoolean(this, Constants.PREF_IS_BELL_ENABLED, false);
            initializeClockWithBells(false);
        }
    }

    private void changeVolume(boolean isIncrease) {
        int currentVolume = sbVolume.getProgress();
        if (isIncrease && currentVolume < 100)
            currentVolume++;
        else if (!isIncrease && currentVolume > 1) currentVolume--;
        sbVolume.setProgress(currentVolume);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StoredData.putInt(MainActivity.this, Constants.PREF_VOLUME, sbVolume.getProgress());
    }
}
