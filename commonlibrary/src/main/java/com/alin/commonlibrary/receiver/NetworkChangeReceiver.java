package com.alin.commonlibrary.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.alin.commonlibrary.annotations.TargetLog;
import com.alin.commonlibrary.event.NetworkStateEvent;
import com.alin.commonlibrary.util.LogUtil;
import com.alin.commonlibrary.util.NetworkUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * @创建者 hailin
 * @创建时间 2017/8/22 22:32
 * @描述 ${广播接受者  网络变化}.
 */
@TargetLog(NetworkChangeReceiver.class)
public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            NetworkUtil networkUtil = NetworkUtil.getInstance();
            NetworkUtil.NetworkState networkState = networkUtil.getCurrentNetworkType(context);
            boolean hasNetwork = false;
            switch (networkState){
                case NETWORK_WIFI:  // WIFI
                    hasNetwork = true;
                    LogUtil.showLog(NetworkChangeReceiver.class,"this is wifi network now...");
                    break;
                case NETWORK_Net2G:     //2g
                    hasNetwork = true;
                    LogUtil.showLog(NetworkChangeReceiver.class,"this network is 2g now...");
                    break;
                case NETWORK_Net3G:     //3g
                    hasNetwork = true;
                    LogUtil.showLog(NetworkChangeReceiver.class,"this network is 3g now...");
                    break;
                case NETWORK_Net4G:     //4g
                    hasNetwork = true;
                    LogUtil.showLog(NetworkChangeReceiver.class,"this network is 4g now...");
                    break;
                case NETWORK_NONE:  //没有网络
                    LogUtil.showLog(NetworkChangeReceiver.class,"this is not network now...");
                    break;
            }
            EventBus.getDefault().post(new NetworkStateEvent(networkState,hasNetwork));
        }

    }
}
