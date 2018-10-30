package com.alin.commonlibrary.util.cache;

import android.content.Context;

/**
 * ================================================
 * 作    者   ： hailinhe
 * github地址 ： https://github.com/Alin520
 * CSDN地址   ： http://blog.csdn.net/hailin123123/article/details/78643330
 * 创建时间    ： 2018/6/30 17:40
 * 版    本   ： ${TODO}
 * 描    述   ：  $  配合使用@Prefs
 * ================================================
 */
public class AppPrefs extends BasePrefs {

    private static final String PREFS_NAME = "AppPrefs";

    private AppPrefs(Context context) {
        super(context, PREFS_NAME);
    }

    public static AppPrefs get(Context context) {
        return new AppPrefs(context);
    }
}
