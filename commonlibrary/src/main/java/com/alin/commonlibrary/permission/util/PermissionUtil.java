package com.alin.commonlibrary.permission.util;

import android.app.Activity;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Process;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.AppOpsManagerCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v4.util.SimpleArrayMap;
import android.text.TextUtils;
import android.util.Log;

import com.alin.commonlibrary.permission.annotation.PermissionCancled;
import com.alin.commonlibrary.permission.annotation.PermissionColor;
import com.alin.commonlibrary.permission.annotation.PermissionDenied;
import com.alin.commonlibrary.permission.menu.COOLPAD;
import com.alin.commonlibrary.permission.menu.DEFAULT;
import com.alin.commonlibrary.permission.menu.HUAWEI;
import com.alin.commonlibrary.permission.menu.IMenu;
import com.alin.commonlibrary.permission.menu.LETV;
import com.alin.commonlibrary.permission.menu.LG;
import com.alin.commonlibrary.permission.menu.MEIZU;
import com.alin.commonlibrary.permission.menu.MIUI;
import com.alin.commonlibrary.permission.menu.OPPO;
import com.alin.commonlibrary.permission.menu.SANGXIN;
import com.alin.commonlibrary.permission.menu.SONY;
import com.alin.commonlibrary.permission.menu.VIVO;
import com.alin.commonlibrary.util.CommonUtil;
import com.alin.commonlibrary.util.LogUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

/**
 * ================================================
 * 作    者   ： alin
 * 创建时间    ： 2018/10/15 10:56
 * 版    本   ： ${TODO}
 * 描    述   ：  ${TODO}
 * ================================================
 */
public class PermissionUtil {
    private static final String TAG = PermissionUtil.class.getSimpleName();


    /**
     * 请求
     */
    public static final int DEFAULT_REQUEST_CODE = 0x000001;
    /**
     * 拒绝
     */
    public static final int DEFAULT_DENIED_CODE = 0x000001;

    /**
     * 取消
     */
    public static final int DEFAULT_CANCELED_CODE = 0x000003;

    /**
     * 权限的状态栏颜色
     */
    public static final String DEFAULT_PERMISSION_COLOR = "-1";

    private static final HashMap<String,Class<? extends IMenu>> PERMISSION_MENU;

    private static final SimpleArrayMap<String,Integer> MIN_SDK_PERMISSIONS;

    private static final String MANUFACTURER_DEFAULT = "DEFAULT";//默认
    public static final String MANUFACTURER_HUAWEI = "huawei";//华为
    public static final String MANUFACTURER_HONOR = "honor";//荣耀
    public static final String MANUFACTURER_MEIZU = "meizu";//魅族
    public static final String MANUFACTURER_XIAOMI = "xiaomi";//小米
    public static final String MANUFACTURER_SONY = "sony";//索尼
    public static final String MANUFACTURER_OPPO = "oppo";
    public static final String MANUFACTURER_LG = "lg";
    public static final String MANUFACTURER_VIVO = "vivo";
    public static final String MANUFACTURER_SAMSUNG = "samsung";//三星
    public static final String MANUFACTURER_LETV = "lemobile";//乐视
    public static final String MANUFACTURER_ZTE = "zte";//中兴
    public static final String MANUFACTURER_YULONG = "yulong";//酷派
    public static final String MANUFACTURER_LENOVO = "lenovo";//联想

    static {
        MIN_SDK_PERMISSIONS = new SimpleArrayMap<>(8);
        MIN_SDK_PERMISSIONS.put("com.android.voicemail.permission.ADD_VOICEMAIL", 14);
        MIN_SDK_PERMISSIONS.put("android.permission.BODY_SENSORS", 20);
        MIN_SDK_PERMISSIONS.put("android.permission.READ_CALL_LOG", 16);
        MIN_SDK_PERMISSIONS.put("android.permission.READ_EXTERNAL_STORAGE", 16);
        MIN_SDK_PERMISSIONS.put("android.permission.USE_SIP", 9);
        MIN_SDK_PERMISSIONS.put("android.permission.WRITE_CALL_LOG", 16);
        MIN_SDK_PERMISSIONS.put("android.permission.SYSTEM_ALERT_WINDOW", 23);
        MIN_SDK_PERMISSIONS.put("android.permission.WRITE_SETTINGS", 23);

//        不同品牌手机权限设置面板
        PERMISSION_MENU = new HashMap<>();
        PERMISSION_MENU.put(MANUFACTURER_DEFAULT, DEFAULT.class);
        PERMISSION_MENU.put(MANUFACTURER_VIVO, VIVO.class);
        PERMISSION_MENU.put(MANUFACTURER_OPPO,OPPO.class);
        PERMISSION_MENU.put(MANUFACTURER_LETV, LETV.class);
        PERMISSION_MENU.put(MANUFACTURER_HUAWEI, HUAWEI.class);
        PERMISSION_MENU.put(MANUFACTURER_HONOR, HUAWEI.class);
        PERMISSION_MENU.put(MANUFACTURER_MEIZU,MEIZU.class);
        PERMISSION_MENU.put(MANUFACTURER_XIAOMI,MIUI.class);
        PERMISSION_MENU.put(MANUFACTURER_YULONG, COOLPAD.class);
        PERMISSION_MENU.put(MANUFACTURER_SAMSUNG, SANGXIN.class);
        PERMISSION_MENU.put(MANUFACTURER_SONY,SONY.class);
        PERMISSION_MENU.put(MANUFACTURER_LG,LG.class);

    }


    private PermissionUtil(){}

    /**
     *  判断是否已经申请了权限
     * @param activity
     * @param permissions
     */
    public static boolean hasPermission(Activity activity, String[] permissions) {
        for (String permission : permissions) {
            if (permissionExists(permission) && !hasSelfPermission(activity,permission)){
                return false;
            }
        }
        return true;
    }

    /**
     *  sdk版本   >6.0，动态权限申请
     * @param permission
     * @return
     */
    private static boolean permissionExists(String permission) {
        Integer minVersion = MIN_SDK_PERMISSIONS.get(permission);
        return minVersion == null || Build.VERSION.SDK_INT > minVersion;
    }

    /**
     *  检查权限是否已经授权成功
     */
    private static boolean hasSelfPermission(Activity activity, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && "Xiaomi".equalsIgnoreCase(Build.MANUFACTURER)){
            return hasSelfPermissionForXiaomi(activity,permission);
        }
        try {
            return PermissionChecker.checkSelfPermission(activity,permission) == PackageManager.PERMISSION_GRANTED;
        }catch (RuntimeException e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 针对小米机型
     * @param activity
     * @param permission
     * @return
     */
    private static boolean hasSelfPermissionForXiaomi(Activity activity, String permission) {
        String permissionToOp = AppOpsManagerCompat.permissionToOp(permission);
        if (permissionToOp == null) {       //6.0以下,直接授权成功
           return true;
        }
        int noteOp = AppOpsManagerCompat.noteOp(activity, permissionToOp, Process.myUid(), activity.getPackageName());
        return noteOp == AppOpsManagerCompat.MODE_ALLOWED && PermissionChecker.checkSelfPermission(activity,permission) == PackageManager.PERMISSION_GRANTED;
    }



    /**
     *  判断是否已经申请了权限
     * @param activity
     * @param grantResults
     */
    public static boolean verifyPermission(Activity activity, int... grantResults) {
        if (grantResults == null || grantResults.length == 0) {
            return false;
        }
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    /**
     *  是否询问 申请权限
     * 1、第一次请求该权限，返回false。
     * 2、请求过该权限并被用户拒绝，返回true。
     * 3、请求过该权限，但用户拒绝的时候勾选不再提醒，返回false。
     *
     */
    public static boolean shouldShowRequestPermissionRationale(Activity activity, String[] permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,permission)) {
                return true;
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean shouldShowRequestPermissionRationale(Fragment fragment, String[] permissions) {
        for (String permission : permissions) {
            if (fragment.shouldShowRequestPermissionRationale(permission)){
                return true;
            }
        }
        return false;
    }

    public static boolean shouldShowRequestPermissionRationale(android.support.v4.app.Fragment fragment, String[] permissions) {
        for (String permission : permissions) {
            if (fragment.shouldShowRequestPermissionRationale(permission)){
                return true;
            }
        }
        return false;
    }


    /**
     *  申请权限
     * @param fragment
     * @param permissions
     * @param requestCode
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void requestPermission(Fragment fragment, String[] permissions, int requestCode){
        fragment.requestPermissions(permissions,requestCode);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void requestPermission(android.support.v4.app.Fragment fragment, String[] permissions, int requestCode){
        fragment.requestPermissions(permissions,requestCode);
    }

    /**
     * @return
     *  true   按照框架内部定义的执行，权限请求拒绝、取消
     *  false   自定义权限请求拒绝、取消内容
     */
    public static <T extends Annotation> boolean invokeAnnotation(Object target, Class<T> annotationClass, int code) {
        Method[] methods = target.getClass().getDeclaredMethods();
        if (methods == null || methods.length == 0 || annotationClass == null) {
            return false;
        }
        for (Method method : methods) {
            T annotation = method.getAnnotation(annotationClass);
            boolean isFindCode = false;
            if (annotation instanceof PermissionCancled) {
                isFindCode = ((PermissionCancled)annotation).requestCode() == code;
                Log.e(TAG,"caceled : code="+ code + ", realCode=" + ((PermissionCancled)annotation).requestCode() );
            }else if (annotation instanceof PermissionDenied){
                isFindCode = ((PermissionDenied)annotation).requestCode() == code;
                Log.e(TAG,"denied , code="+ code + ", realCode=" + ((PermissionDenied)annotation).requestCode() );

            }
            boolean isHasAnnotation = method.isAnnotationPresent(annotationClass);
            if (isHasAnnotation && isFindCode){
                try {
                    method.setAccessible(true);
                    Object invoke = method.invoke(target);
                    LogUtil.i(TAG,"invoke====>" + invoke);
                    if (invoke != null && invoke instanceof Boolean) {
                        return (boolean) invoke;
                    }else if (invoke == null){
                        return true;
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 查找状态栏颜色值
     */
    public static int findStatusBarColor(Object target) {
        int statusBarColor = Integer.valueOf(PermissionUtil.DEFAULT_PERMISSION_COLOR);
        if (target != null) {
            PermissionColor annotation = target.getClass().getAnnotation(PermissionColor.class);
            if (annotation != null) {
                String color =  annotation.color();
                if (!TextUtils.isEmpty(color)) {
                    if (color.equals(PermissionUtil.DEFAULT_PERMISSION_COLOR)) {
                        statusBarColor = Integer.valueOf(PermissionUtil.DEFAULT_PERMISSION_COLOR);
                    }else {
                        statusBarColor =  Color.parseColor(color);
                    }
                }
            }
        }

        return statusBarColor;
    }

    /**
     * 设置状态栏
     * */
    public static void resetStatusBar(Activity activity, int statusBarColor) {
        if (statusBarColor == 0) {
            CommonUtil.hideStatusBarIfSupporter(activity);
        }else {
            CommonUtil.setStatusBarColorIfSupporter(activity,statusBarColor);
        }
    }


    public static void goToAppMenu(Context context) {
        String brand = Build.BRAND.toLowerCase();
        if (TextUtils.isEmpty(brand)) {
            throw new RuntimeException("current phone system brand is empty error");
        }
        Log.i(TAG,"brand===>" + brand);
        Class<? extends IMenu> clzz = PERMISSION_MENU.get(brand);
        if (clzz == null) {
         PERMISSION_MENU.put(brand,DEFAULT.class);
        }
        try {
            IMenu iMenu = clzz.newInstance();
            Intent menuIntent = iMenu.getAppMenuIntent(context);
            if (menuIntent == null) {
                return;
            }
            context.startActivity(menuIntent);
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    /**
     * 获取小米版本
     * @return
     */
    public static String getMiuiVersion() {
        String propName = "ro.miui.ui.version.name";
        String line;
        BufferedReader input = null;
        try {
            java.lang.Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader( new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine(); input.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return line;
    }


    public static Intent findSystemIntent(Context context, String packagename) {
        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        Intent intent = null;
        try {
            packageinfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } if (packageinfo == null) {
            return null;
        }
        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);
        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList = context.getPackageManager() .queryIntentActivities(resolveIntent, 0);
        Log.e("PermissionPageManager", "resolveinfoList" + resolveinfoList.size());
        for (int i = 0; i < resolveinfoList.size(); i++) {
            Log.e("PermissionPageManager", resolveinfoList.get(i).activityInfo.packageName + resolveinfoList.get(i).activityInfo.name);
        }
        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            // packageName参数2 = 参数 packname
            String packageName = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packageName参数2.mainActivityname]
            String className = resolveinfo.activityInfo.name; // LAUNCHER Intent
            intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            // 设置ComponentName参数1:packageName参数2:MainActivity路径
            ComponentName cn = new ComponentName(packageName, className);
            intent.setComponent(cn);
            return intent;
        }

        return intent;
    }

}
