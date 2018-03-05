package com.alin.commonlib;


import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.alin.commonlib.dialig.LoadingProgressDialog;
import com.alin.commonlibrary.base.CommonActivity;

import butterknife.OnClick;

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

    @OnClick({R.id.text_tv})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.text_tv:
                Log.e("BannerClick","BannerClick.......");
                break;
        }
    }
}
