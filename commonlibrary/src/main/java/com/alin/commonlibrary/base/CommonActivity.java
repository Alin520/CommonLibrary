package com.alin.commonlibrary.base;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.alin.commonlibrary.manager.ActivityManager;
import com.alin.commonlibrary.util.CommonUtil;
import com.alin.commonlibrary.util.HandlerUtil;
import com.alin.commonlibrary.util.LogUtil;

import org.greenrobot.eventbus.EventBus;
import butterknife.ButterKnife;

/**
 * @创建者 hailin
 * @创建时间 2017/11/17 11:50
 * @描述 ${所有Activity基类}.
 */
public abstract class CommonActivity extends AppCompatActivity {

    private InputMethodManager mManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        resetStatusBar();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ActivityManager.getInstance().addActivity(this);
        ButterKnife.bind(this);
        if (openEventBus()){
            EventBus.getDefault().register(this);
        }
        initialize(savedInstanceState);
    }

    protected abstract int getContentViewId();

    //设置状态栏颜色值
    public Integer getStatusBarColor() {
        return null;
    }

    /**
     *  是否打开EventBus
     * @return
     */
    public static boolean openEventBus() {
        return false;
    }
    protected void initialize(Bundle savedInstanceState){
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void setContentView(@LayoutRes int layoutResID){
        super.setContentView(layoutResID);
//        对listview、SrollView、webview没有作用
        CommonUtil.getContentView(this).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtil.showOrHideKeyboard(CommonActivity.this,false);
            }
        });
    }

    //    设置状态栏
    public void resetStatusBar(){
        Integer statusBarColor = getStatusBarColor();
        if (statusBarColor != null) {
            if (statusBarColor.intValue() == 0) {
                CommonUtil.hideStatusBarIfSupporter(this);
            }else {
                CommonUtil.setStatusBarColorIfSupporter(this,statusBarColor);
            }
        }
    }

    /**
     * 展示日志
     * @param log
     */
    public void showLog(@Nullable String log){
        showLog(log,null);
    }

    public void showLog(String log, LogUtil.Logs type) {
        LogUtil.showLog(getClass(),log,type);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public boolean isFinishOrDestory(){
        return this.isFinishing() || this.isDestroyed();
    }

    /**
     *  获取handler
     * @return
     */
    public Handler getHandler(){
        return new HandlerUtil.Build(getMainLooper()) {
            @Override
            public void receiveMessage(Message msg) {
                CommonActivity.this.receiveMessage(msg);
            }
        }.builer().getHandler();
    }

    /**
     *  获取HandlerUtil
     * @return
     */
    public HandlerUtil getHandlerUtil(){
        return new HandlerUtil.Build(getMainLooper()) {
            @Override
            public void receiveMessage(Message msg) {
                CommonActivity.this.receiveMessage(msg);
            }
        }.builer();
    }

    /**
     *  @deprecated  收到消息
     * @param msg
     */
    protected void receiveMessage(Message msg) {
    }


    @Override
    protected void onDestroy() {
        if (openEventBus()) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();

        ActivityManager.getInstance().removeActivity(this);
    }
}
