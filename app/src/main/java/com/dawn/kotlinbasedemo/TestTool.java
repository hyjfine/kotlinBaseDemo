package com.dawn.kotlinbasedemo;

import static android.content.Context.TELEPHONY_SERVICE;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * author : heyongjian
 * time   : 2021/11/25
 * desc   :
 */
public class TestTool {
    public static void makeType(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        telephonyManager.getImei();
    }
}
