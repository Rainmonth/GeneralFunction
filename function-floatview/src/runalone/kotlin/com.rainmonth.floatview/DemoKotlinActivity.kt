package com.rainmonth.floatview

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 * @author RandyZhang
 * @date  2022/3/16 7:14 下午
 */
class DemoKotlinActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.floatview_activity_float_view_main)

        val view = findViewById<TextView>(R.id.tv_test_combine)

        view.setOnClickListener(this::onClick)
    }

    private fun onClick(view: View) {
        val intent = Intent(this@DemoKotlinActivity, TestActivity::class.java)
        startActivity(intent)
    }
}