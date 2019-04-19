package com.ast.clock.activities;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ast.clock.R;
import com.ast.clock.utitilies.Constants;
import com.ast.clock.utitilies.StoredData;
import com.ast.clock.utitilies.StringDateTime;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AlarmTriggeredActivity extends AppCompatActivity {

    Calendar calendar;
    AssetFileDescriptor afd;
    @BindView(R.id.iv_minus)
    ImageView ivMinus;
    @BindView(R.id.iv_plus)
    ImageView ivPlus;
    @BindView(R.id.sb_volume)
    SeekBar sbVolume;
    MediaPlayer m = new MediaPlayer();
    @BindView(R.id.iv_om_or_bell)
    ImageView ivOmOrBell;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_date)
    TextView tvDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_triggered);
        ButterKnife.bind(this);
        wakeLockScreen();
        calendar = Calendar.getInstance();
        tvTime.setText(StringDateTime.getTimeInString(calendar.getTimeInMillis(), this));
        tvDate.setText(StringDateTime.getDayOfWeek(this, calendar.getTimeInMillis()) + ", " + StringDateTime.getDateInString(calendar.getTimeInMillis(), this));
        int hourOfTheDay = calendar.get(Calendar.HOUR_OF_DAY);
        sbVolume.setProgress(StoredData.getInt(this, Constants.PREF_VOLUME, 50));

        switch (hourOfTheDay) {
            case 12:
            case 5:
            case 6:
            case 7:
            case 17:
            case 18:
            case 19:
                ivOmOrBell.setImageResource(R.drawable.ic_om);
                playAudio("aum.mp3");
                break;
            default:
                ivOmOrBell.setImageResource(R.drawable.ic_om);
                playAudio("aum.mp3");
                break;
        }

        sbVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                AudioManager audioManager =
                        (AudioManager) getSystemService(Context.AUDIO_SERVICE);

                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                        progress, AudioManager.FLAG_PLAY_SOUND);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    private void wakeLockScreen() {
        @SuppressWarnings("deprecation") KeyguardManager.KeyguardLock lock = ((KeyguardManager) getSystemService(Activity.KEYGUARD_SERVICE)).newKeyguardLock(KEYGUARD_SERVICE);
        PowerManager powerManager = ((PowerManager) getSystemService(Context.POWER_SERVICE));
        PowerManager.WakeLock wake = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");

        lock.disableKeyguard();
        wake.acquire();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
    }

    public void playAudio(String audioFileName) {
        try {
            if (m.isPlaying()) {
                m.stop();
                m.release();
                m = new MediaPlayer();
            }

            AssetFileDescriptor descriptor = getAssets().openFd(audioFileName);
            m.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();

            m.prepare();
            m.setLooping(false);
            m.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        m.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                finish();
            }
        });

        AudioManager audioManager =
                (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                sbVolume.getProgress(), AudioManager.FLAG_PLAY_SOUND);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (m.isPlaying()) {
            m.stop();
            m.release();
        }
    }

    @OnClick({R.id.iv_minus, R.id.iv_plus})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_minus:
                changeVolume(false);
                break;
            case R.id.iv_plus:
                changeVolume(true);
                break;
        }
    }

    private void changeVolume(boolean isIncrease) {
        int currentVolume = sbVolume.getProgress();
        if (isIncrease && currentVolume < 100)
            currentVolume++;
        else if (!isIncrease && currentVolume > 1) currentVolume--;
        sbVolume.setProgress(currentVolume);
    }
}
