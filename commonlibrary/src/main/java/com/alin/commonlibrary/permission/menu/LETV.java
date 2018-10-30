package com.alin.commonlibrary.permission.menu;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.alin.commonlibrary.BuildConfig;

/**
 * ================================================
 * 作    者   ： alin
 * 创建时间    ： 2018/7/19 11:11
 * 版    本   ： ${TODO}
 * 描    述   ：  ${TODO}
 * ================================================
 */
public class LETV implements IMenu {

    @Override
    public Intent getAppMenuIntent(Context context) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
        ComponentName comp = new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.PermissionAndApps");
        intent.setComponent(comp);
        return intent;
    }
}
