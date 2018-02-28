package com.alin.commonlibrary.base;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.alin.commonlibrary.util.HandlerUtil;
import com.alin.commonlibrary.util.LogUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;

/**
 * @创建者 hailin
 * @创建时间 2017/11/17 13:25
 * @描述 ${}.
 */

public abstract class CommonFragment extends Fragment {

    /**
     *  是否打开EventBus
     * @return
     */

    protected abstract int getContentViewId();

    public boolean openEventBus() {
        return false;
    }

    protected void initialize(Bundle savedInstanceState,@Nullable View view){
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


    /**
     *  获取handler
     * @return
     */
    public Handler getHandler(){
        Handler handler = null;
        try {
            if (this.getActivity() instanceof CommonActivity) {
                handler = ((CommonActivity) this.getActivity()).getHandler();
            }else {
                handler = new HandlerUtil.Build(Looper.getMainLooper()) {
                    @Override
                    public void receiveMessage(Message msg) {
                        CommonFragment.this.receiveMessage(msg);
                    }
                }.builer().getHandler();
            }
        }catch (Throwable throwable){
            throwable.printStackTrace();
        }
        return handler;
    }

    /**
     *  获取HandlerUtil
     * @return
     */
    public HandlerUtil getHandlerUtil(){
        HandlerUtil handlerUtil = null;
        try {
            if (this.getActivity() instanceof CommonActivity) {
                handlerUtil = ((CommonActivity) this.getActivity()).getHandlerUtil();
            }else {
                handlerUtil = new HandlerUtil.Build(Looper.getMainLooper()) {
                    @Override
                    public void receiveMessage(Message msg) {
                        CommonFragment.this.receiveMessage(msg);
                    }
                }.builer();
            }
        }catch (Throwable throwable){
            throwable.printStackTrace();
        }
        return handlerUtil;
    }

    /**
     *  @deprecated  收到消息
     * @param msg
     */
    protected void receiveMessage(Message msg) {

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (openEventBus()){
            EventBus.getDefault().register(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getContentViewId(), container, false);
        ButterKnife.bind(this,view);
        initialize(savedInstanceState,view);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (openEventBus()) {
            EventBus.getDefault().unregister(this);
        }
    }
}
