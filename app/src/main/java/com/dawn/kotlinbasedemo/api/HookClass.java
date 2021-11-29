package com.dawn.kotlinbasedemo.api;

import android.util.Log;

import me.ele.lancet.base.Origin;
import me.ele.lancet.base.Scope;
import me.ele.lancet.base.annotations.Proxy;
import me.ele.lancet.base.annotations.TargetClass;

/**
 * author : heyongjian
 * time   : 2021/11/24
 * desc   :
 */
public class HookClass {

    @Proxy("i")
    @TargetClass("android.util.Log")
    public static int logName(String tag, String msg) {
        msg = msg + " this is lancet";
        return (int) Origin.call();
    }

    @TargetClass("android.telephony.TelephonyManager")
    @Proxy("getDataNetworkType")
    public int getDataNetworkType() {
        try {
//            return (int) Origin.<SecurityException>callThrowOne();
            return (int) Origin.call();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("hookclass", "----");
            return -1;
        }
    }

//    @TargetClass("android.telephony.TelephonyManager")
//    @Proxy("getImei")
//    public static String getImei() {
//        try {
//            return (String) Origin.<Exception>callThrowOne();
////            return (String) Origin.call();
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.d("hookclass", "----" + e);
//            return "e";
//        }
//    }

    @TargetClass(value = "android.telephony.TelephonyManager", scope = Scope.LEAF)
    @Proxy("hasCarrierPrivileges")
//    @Insert(value = "hasCarrierPrivileges", mayCreateSuper = true)
    public boolean hasCarrierPrivileges() {
        Log.d("hookclass", "----xixi");
        return true;
    }

}
