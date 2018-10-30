package com.alin.commonlibrary.listener;

import android.view.View;

/**
 * ================================================
 * 作    者   ： alin
 * 创建时间    ： 2018/3/12 14:23
 * 版    本   ： ${TODO}
 * 描    述   ：  ${防止双击回调}
 * ================================================
 */
public class OnSingleClick implements View.OnClickListener {

    private long mIntervalOnClickTime = 1000;
    private long mStartTime;
    private long mEndTime;


    public OnSingleClick(OnSingleClickListener onSingleClickListener){
        this(onSingleClickListener,1000);
    }

    public OnSingleClick(OnSingleClickListener onSingleClickListener, long intervalOnClickTime){
        mOnSingleClickListener = onSingleClickListener;
        mIntervalOnClickTime = intervalOnClickTime;
    }

    @Override
    public void onClick(View v) {
        mStartTime = System.currentTimeMillis();
        if (Math.abs(mStartTime - mEndTime) > mIntervalOnClickTime) {
            mOnSingleClickListener.onSingleClick(v);
        }
        mEndTime = System.currentTimeMillis();

    }

    private OnSingleClickListener mOnSingleClickListener;

    public interface OnSingleClickListener {
      void onSingleClick(View view);
    }
}
