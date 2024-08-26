package com.rainmonth.function;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.rainmonth.function.camera.VideoCamera2DemoActivity;
import com.rainmonth.function.camera.VideoCameraDemoActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View view = findViewById(R.id.fl_1);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoCameraDemoActivity.start(view.getContext());
            }
        });

        View view2 = findViewById(R.id.fl_2);
        view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    VideoCamera2DemoActivity.start(view.getContext());
                }
            }
        });

        View view3 = findViewById(R.id.fl_3);
        view3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClockTestActivity.start(v.getContext());
            }
        });
    }
}
