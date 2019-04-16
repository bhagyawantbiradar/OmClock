package com.ast.clock;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {


    ImageView ivShare;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivShare = findViewById(R.id.iv_share);

        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AlarmTriggeredActivity.class);
                startActivity(intent);
            }
        });

        if (Constants.isForceStopped) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                String packageName = getPackageName();
                PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
                if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
                    startActivity(intent);
                } else {
                    setAlarm();
                }
            } else {
                setAlarm();
            }
        }

    }

    private void setAlarm() {
        Calendar calendar = Calendar.getInstance();
        calendar.get(Calendar.HOUR_OF_DAY);


        Constants.isForceStopped = false;
    }
}
