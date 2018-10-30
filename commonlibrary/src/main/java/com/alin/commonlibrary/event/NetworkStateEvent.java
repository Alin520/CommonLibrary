package com.alin.commonlibrary.event;

import com.alin.commonlibrary.util.NetworkUtil;

import java.io.Serializable;

/**
 * ================================================
 * 作    者   ： alin
 * 创建时间    ： 2018/3/14 16:55
 * 版    本   ： ${TODO}
 * 描    述   ：  ${网络状态变化监听，事件}
 * ================================================
 */
public class NetworkStateEvent implements Serializable {
    private NetworkUtil.NetworkState networkState;
    private boolean hasNetwork;

    public NetworkStateEvent(NetworkUtil.NetworkState networkState, boolean hasNetwork) {
        this.networkState = networkState;
        this.hasNetwork = hasNetwork;
    }

    public NetworkUtil.NetworkState getNetworkState() {
        return networkState;
    }

    public boolean isHasNetwork() {
        return hasNetwork;
    }
}
