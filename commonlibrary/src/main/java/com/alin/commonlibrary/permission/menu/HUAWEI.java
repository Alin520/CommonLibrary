package com.alin.commonlibrary.permission.menu;

import android.content.ComponentName;
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
public class HUAWEI implements IMenu {
    @Override
    public Intent getAppMenuIntent(Context context) {
        Intent intent = new Intent();
        try {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");//华为权限管理
            intent.setComponent(comp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return intent;
    }
}
