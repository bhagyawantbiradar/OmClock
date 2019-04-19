package com.ast.clock.activities;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.ast.clock.R;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlarmTriggeredActivity extends AppCompatActivity {

    Calendar calendar;
    MediaPlayer mediaPlayer;
    AssetFileDescriptor afd;
    @BindView(R.id.iv_minus)
    ImageView ivMinus;
    @BindView(R.id.iv_plus)
    ImageView ivPlus;
    @BindView(R.id.sb_volume)
    SeekBar sbVolume;

    MediaPlayer m = new MediaPlayer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_triggered);
        ButterKnife.bind(this);
        wakeLockScreen();
        calendar = Calendar.getInstance();
        int hourOfTheDay = calendar.get(Calendar.HOUR_OF_DAY);

        playAudio("aum.mp3");

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
            }

            m = new MediaPlayer();

            AssetFileDescriptor descriptor = getAssets().openFd(audioFileName);
            m.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();

            m.prepare();
            m.setVolume(1f, 1f);
            m.setLooping(true);
            m.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
