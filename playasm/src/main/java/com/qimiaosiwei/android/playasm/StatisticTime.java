package com.qimiaosiwei.android.playasm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * author : heyongjian
 * time   : 2021/12/8
 * desc   :
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface StatisticTime {
}
