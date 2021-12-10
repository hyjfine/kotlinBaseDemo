package com.qimiaosiwei.android.playasm;

import android.util.Log;

/**
 * author : heyongjian
 * time   : 2021/12/6
 * desc   :
 */
public class TestBytecode {

    @StatisticTime
    void sayST() {
        long start = System.currentTimeMillis();
        Log.d("testBytecode", "-----say " + start);
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @StatisticTime
    void hiST() {
        Log.d("testBytecode", "-----hi");
    }

    void world() {
        Log.d("testBytecode", "-----world");
    }
}
