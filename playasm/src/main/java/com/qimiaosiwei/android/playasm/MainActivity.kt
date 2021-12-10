package com.qimiaosiwei.android.playasm

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupListener()
    }

    @StatisticTime
    private fun setupListener() {
        val test = TestBytecode()
        findViewById<Button>(R.id.sayBtn).setOnClickListener {
            test.sayST()
            test.hiST()
        }
    }

}