package com.alin.commonlibrary.util;


import android.content.Context;
import android.content.res.ColorStateList;
import android.net.Uri;

/**
 * @创建者 hailin
 * @创建时间 2017/11/23 15:58
 * @描述 ${}.
 */

public class ResourcesUtil {

    public static Uri getFileURI(String url){
        AppUtil.checkNotNull(url,"url == null,input fileUrl is not null!");
        return Uri.parse("file://" + url);
    }

    public static String getString(Context context, int resourceId) {
        return context.getResources().getString(resourceId);
    }

    public static int getColor(Context context,int resId){
        AppUtil.checkLargeThanZero(resId,"getColor() must be resId> 0 !");
        return context.getResources().getColor(resId);
    }

    public static float getDimension(Context context,int resId){
        AppUtil.checkLargeThanZero(resId,"getColor() must be resId> 0 !");
        return context.getResources().getDimension(resId);
    }

    public static ColorStateList getColorStateList(Context context, int colorId){
        return context.getResources().getColorStateList(colorId);
    }
}
