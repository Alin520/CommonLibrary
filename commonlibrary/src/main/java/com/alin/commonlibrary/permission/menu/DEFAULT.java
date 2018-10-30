package com.alin.commonlibrary.permission.menu;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

/**
 * ================================================
 * 作    者   ： alin
 * 创建时间    ： 2018/10/22 11:37
 * 版    本   ： ${TODO}
 * 描    述   ：  ${TODO}
 * ================================================
 */
public class DEFAULT implements IMenu{

    public static DEFAULT sIntance;

    public DEFAULT(){

    }

    public static DEFAULT getInstance(){
        if (sIntance == null) {
            synchronized (DEFAULT.class){
                if (sIntance == null) {
                    sIntance = new DEFAULT();
                }
            }
        }
        return sIntance;
    }
    @Override
    public Intent getAppMenuIntent(Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        return intent;
    }
}
