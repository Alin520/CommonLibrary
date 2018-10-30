package com.alin.commonlibrary.permission.menu;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

/**
 * ================================================
 * 作    者   ： alin
 * 创建时间    ： 2018/10/22 15:00
 * 版    本   ： ${TODO}
 * 描    述   ：  ${TODO}
 * ================================================
 */
public class SONY implements IMenu {
    @Override
    public Intent getAppMenuIntent(Context context) {
        Intent intent = new Intent(context.getPackageName());
        ComponentName comp = new ComponentName("com.sonymobile.cta", "com.sonymobile.cta.SomcCTAMainActivity");
        intent.setComponent(comp);
        return intent;
    }
}
