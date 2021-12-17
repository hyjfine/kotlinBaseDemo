package com.qimiaosiwei.android.playasm;

/**
 * author : heyongjian
 * time   : 2021/12/10
 * desc   :
 */
public class TestA {

    private void a() {
        long m = System.currentTimeMillis();

    }

    private void b() {
        long m = 1;
        long n = System.currentTimeMillis();

        System.out.println("----time: " + (n-m) + "ms");

    }
}
