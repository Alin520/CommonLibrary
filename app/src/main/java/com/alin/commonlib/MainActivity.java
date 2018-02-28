package com.alin.commonlib;


import android.os.Bundle;

import com.alin.commonlib.dialig.LoadingProgressDialog;
import com.alin.commonlibrary.base.CommonActivity;

public class MainActivity extends CommonActivity {

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initialize(Bundle savedInstanceState) {
        //测试loading
        LoadingProgressDialog progressDialog = new LoadingProgressDialog(this, true, null);
        progressDialog.show("正在测试中。。。。。");
    }
}
