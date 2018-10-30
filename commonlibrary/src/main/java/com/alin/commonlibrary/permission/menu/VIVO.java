package com.alin.commonlibrary.permission.menu;

import android.content.Context;
import android.content.Intent;

import com.alin.commonlibrary.permission.util.PermissionUtil;

/**
 * ================================================
 * 作    者   ： alin
 * 创建时间    ： 2018/10/22 11:39
 * 版    本   ： ${TODO}
 * 描    述   ：  ${TODO}
 * ================================================
 */
public class VIVO implements IMenu {
    @Override
    public Intent getAppMenuIntent(Context context) {
        return PermissionUtil.findSystemIntent(context,"com.bairenkeji.icaller");
    }
}
