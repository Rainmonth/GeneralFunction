package com.rainmonth.function;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.CheckBox;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.rainmonth.function.widget.ClockView;

import java.util.Calendar;

/**
 * @author 张豪成
 * @date 2024/8/9 15:28
 */
public class ClockTestActivity extends AppCompatActivity {


    public static void start(Context context) {
        Intent intent = new Intent(context, ClockTestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_test);

        ClockView clockView = findViewById(R.id.clock_view);
        CheckBox checkBox = findViewById(R.id.cb_auto_update);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        clockView.setAutoUpdate(true);
        clockView.setTime(calendar);


        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            clockView.setAutoUpdate(isChecked);
        });

    }
}
