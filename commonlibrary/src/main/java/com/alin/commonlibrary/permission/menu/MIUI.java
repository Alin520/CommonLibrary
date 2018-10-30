package com.alin.commonlibrary.permission.menu;

import android.content.Context;
import android.content.Intent;

import com.alin.commonlibrary.permission.util.PermissionUtil;
import com.alin.commonlibrary.util.LogUtil;

/**
 * ================================================
 * 作    者   ： alin
 * 创建时间    ： 2018/10/22 13:29
 * 版    本   ： ${TODO}
 * 描    述   ：  ${TODO}
 * ================================================
 */
public class MIUI implements IMenu {
    @Override
    public Intent getAppMenuIntent(Context context) {
//        Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
//        ComponentName componentName = new ComponentName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
//        intent.setComponent(componentName);
//        intent.putExtra("extra_pkgname", context.getPackageName());
        String rom = PermissionUtil.getMiuiVersion();
        LogUtil.e("MIUI","goMiaoMiMainager --- rom : "+rom);
        Intent intent=new Intent();
        if ("V6".equals(rom) || "V7".equals(rom)) {
            intent.setAction("miui.intent.action.APP_PERM_EDITOR");
            intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
            intent.putExtra("extra_pkgname", context.getPackageName());
        } else if ("V8".equals(rom) || "V9".equals(rom)) {
            intent.setAction("miui.intent.action.APP_PERM_EDITOR");
            intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
            intent.putExtra("extra_pkgname", context.getPackageName());
        } else {
            intent = DEFAULT.getInstance().getAppMenuIntent(context);
         }

        return intent;
    }




}
