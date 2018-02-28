package com.alin.commonlibrary.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.alin.commonlibrary.annotations.TargetLog;
import com.alin.commonlibrary.util.LogUtil;


/**
 * ================================================
 * 作    者   ： hailinhe
 * github地址 ： https://github.com/Alin520/Mvp-Rxjava-Retrofit
 * CSDN地址   ： http://blog.csdn.net/hailin123123/article/details/78643330
 * 创建时间    ： 2018/1/23 22:42
 * 版    本   ： ${TODO}
 * 描    述   ：  ${TODO}
 * ================================================
 */
@TargetLog(CommonViewPager.class)
public class CommonViewPager extends ViewPager {

    private int mLastX;
    private int mLastY;

    public CommonViewPager(Context context) {
        this(context,null);
    }

    public CommonViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercepted = false;
        int x = (int) ev.getX();
        int y = (int) ev.getY();

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                intercepted = false;
                break;

            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                if(Math.abs(deltaX) > Math.abs(deltaY)){    //左右滑动
                    intercepted = true;
                }else {
                    intercepted = false;
                }
                break;

            case MotionEvent.ACTION_UP:
                intercepted = false;
                break;
        }

        mLastX = x;
        mLastY = y;

        LogUtil.showLog(CommonViewPager.class,"intercepted==" + intercepted);
        return intercepted;
    }
}
