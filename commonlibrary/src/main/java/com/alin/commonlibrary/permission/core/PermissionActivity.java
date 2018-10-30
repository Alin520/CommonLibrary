package com.alin.commonlibrary.permission.core;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.alin.commonlibrary.R;
import com.alin.commonlibrary.permission.util.PermissionUtil;


/**
 * ================================================
 * 作    者   ： alin
 * 创建时间    ： 2018/10/10 18:04
 * 版    本   ： ${TODO}
 * 描    述   ：  ${TODO}
 * ================================================
 */
public class PermissionActivity extends Activity{

    public static final String PERMISSION_VALUE = "permission_value";
    public static final String PERMISSION_CODE = "permission_code";
    public static final String STATUS_BAR_COLOR = "status_bar_color";
    private static IPermission sCallback;


    public static void requestPermission(Context context, int code, String[] value,int statusBarColor,IPermission callback) {
        sCallback = callback;
        Intent intent = new Intent(context,PermissionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle bundle = new Bundle();
        bundle.putStringArray(PERMISSION_VALUE,value);
        bundle.putInt(PERMISSION_CODE,code);
        bundle.putInt(STATUS_BAR_COLOR,statusBarColor);
        intent.putExtras(bundle);
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity)context).overridePendingTransition(0,0);
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permssion);
        Intent intent = getIntent();
        if (intent == null || intent.getExtras() == null || sCallback == null) {
            finish();
            return;
        }
        Bundle bundle = intent.getExtras();
        String[] permissions = bundle.getStringArray(PERMISSION_VALUE);
        int code = bundle.getInt(PERMISSION_CODE);
        int statusBarColor = bundle.getInt(STATUS_BAR_COLOR,Integer.valueOf(PermissionUtil.DEFAULT_PERMISSION_COLOR));
//        设置状态栏颜色
        if (statusBarColor != Integer.valueOf(PermissionUtil.DEFAULT_PERMISSION_COLOR)) {
            PermissionUtil.resetStatusBar(this,statusBarColor);
        }

        if (PermissionUtil.hasPermission(this,permissions)) {
            sCallback.granted();
            finish();
            return;
        }

        ActivityCompat.requestPermissions(this,permissions,code);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //请求权限成功
        if (PermissionUtil.verifyPermission(this,grantResults)){
            sCallback.granted();
            finish();
            return;
        }

        //用户点击了不再显示，拒绝授权
        if (PermissionUtil.shouldShowRequestPermissionRationale(this,permissions)){
            sCallback.denied(permissions,requestCode);
            finish();
            return;
        }

        //取消授权
        sCallback.canceled(permissions,requestCode);
        finish();
        return;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,0);
    }
}
