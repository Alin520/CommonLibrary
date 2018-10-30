package com.alin.commonlibrary.permission.menu;

import android.content.Context;
import android.content.Intent;


/**
 * ================================================
 * 作    者   ： alin
 * 创建时间    ： 2018/10/22 13:28
 * 版    本   ： ${TODO}
 * 描    述   ：  ${TODO}
 * ================================================
 */
public class MEIZU implements IMenu {
    @Override
    public Intent getAppMenuIntent(Context context) {
        Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra("packageName", context.getPackageName());
        return intent;
    }
}
