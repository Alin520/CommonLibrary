package com.alin.commonlib.permission;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alin.commonlib.R;
import com.alin.commonlibrary.base.CommonActivity;
import com.alin.commonlibrary.permission.annotation.Permission;
import com.alin.commonlibrary.permission.annotation.PermissionCancled;
import com.alin.commonlibrary.permission.annotation.PermissionColor;
import com.alin.commonlibrary.permission.annotation.PermissionDenied;


@PermissionColor(color = "#C81432")
public class TestPermissionActivity extends CommonActivity implements View.OnClickListener {

    private static final String TAG = "TestPermissionActivity";

    @Override
    public Integer getStatusBarColor() {
//        return getResources().getColor(R.color.red);
        Log.e(TAG,"color==>" + Color.parseColor("#C81432"));

        return Color.parseColor("#C81432");
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_test_permission;
    }

    @Override
    protected void initialize(Bundle savedInstanceState) {
        super.initialize(savedInstanceState);
        findViewById(R.id.btn_all).setOnClickListener(this);
        findViewById(R.id.btn_all_exclude).setOnClickListener(this);
        findViewById(R.id.btn_one_permission).setOnClickListener(this);
        findViewById(R.id.btn_two_permission).setOnClickListener(this);
        findViewById(R.id.btn_request_200).setOnClickListener(this);
        findViewById(R.id.btn_service).setOnClickListener(this);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frame_layout, new MyFragment());
        transaction.commit();


        Log.e(TAG,"color=22=>" + getResources().getColor(R.color.red));
    }

    private void requestAllExclue() {

    }

    private void requestAll() {

    }
    private void requestService() {
                Intent intent =new Intent(this,MyService.class);
                startService(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_all:
                requestAll();
                break;
            case R.id.btn_all_exclude:
                requestAllExclue();
                break;
            case R.id.btn_one_permission:
                requestOnePermission();
                break;
            case R.id.btn_two_permission:
                requestTwoPermission();
                break;
            case R.id.btn_request_200:
                requestRequest200();
                break;
            case R.id.btn_service:
                requestService();
                break;
        }
    }

    @Permission(values = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA})
    private void requestTwoPermission() {
        Toast.makeText(this, "请求两个权限成功（存储和相机）", Toast.LENGTH_SHORT).show();
    }

    @Permission(values = {Manifest.permission.WRITE_EXTERNAL_STORAGE})
    private void requestOnePermission() {
        Toast.makeText(this, "请求一个权限成功（存储权限）", Toast.LENGTH_SHORT).show();
    }

    /**
     * 发出权限申请请求
     */
    @Permission(values = {Manifest.permission.ACCESS_FINE_LOCATION},requestCode = 200)
    private void requestRequest200() {
        Toast.makeText(this, "请求定位权限成功，200", Toast.LENGTH_SHORT).show();
    }


    @PermissionCancled(requestCode = 200)
    private void cancelCode200(){
        Toast.makeText(this, "取消__200", Toast.LENGTH_SHORT).show();
    }

    /**
     * @return
     *  true   按照框架内部定义的执行，权限请求拒绝
     *  false   自定义权限请求拒绝内容
     */
    @PermissionDenied(requestCode = 200)
    private boolean denyCode200(){
        Toast.makeText(this, "禁止__200", Toast.LENGTH_SHORT).show();
        return true;
    }

    /**
     * @return
     *  true   按照框架内部定义的执行，权限请求取消
     *  false   自定义权限请求取消内容
     */
    @PermissionCancled()
    private boolean cancel() {
        Log.i(TAG, "writeCancel: " );
        Toast.makeText(this, "取消。。。", Toast.LENGTH_SHORT).show();
        return false;
    }

    @PermissionDenied()
    private void deny() {
        Log.i(TAG, "writeDeny:");
        Toast.makeText(this, "禁止。。。", Toast.LENGTH_SHORT).show();
    }

}