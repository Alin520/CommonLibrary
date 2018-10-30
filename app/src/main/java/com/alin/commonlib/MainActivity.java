package com.alin.commonlib;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.alin.commonlib.dialig.LoadingProgressDialog;
import com.alin.commonlib.permission.TestPermissionActivity;
import com.alin.commonlibrary.annotations.TargetLog;
import com.alin.commonlibrary.base.CommonActivity;
import com.alin.commonlibrary.util.LogUtil;

import butterknife.OnClick;
@TargetLog(MainActivity.class)
public class MainActivity extends CommonActivity {

    private LoadingProgressDialog mProgressDialog;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initialize(Bundle savedInstanceState) {
        //测试loading
        mProgressDialog = new LoadingProgressDialog(this, true, null);
//        progressDialog.show("正在测试中。。。。。");
    }

    @OnClick({R.id.text_tv,R.id.permission_tv})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.text_tv:
                mProgressDialog.show("正在测试中。。。。。");
                LogUtil.showLog(MainActivity.class,"xxxx打印的内容");
                LogUtil.showLog(MainActivity.class,"xxxx打印的内容", LogUtil.Logs.i);
                break;
                case R.id.permission_tv:
                    startActivity(new Intent(this, TestPermissionActivity.class));
                    break;
        }
    }


}
