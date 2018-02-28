package com.alin.commonlibrary.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;


import com.alin.commonlibrary.R;

import java.text.DecimalFormat;

/**
 * ================================================
 * 作    者   ： hailinhe
 * github地址 ： https://github.com/Alin520/Mvp-Rxjava-Retrofit
 * CSDN地址   ： http://blog.csdn.net/hailin123123/article/details/78643330
 * 创建时间    ： 2017/12/28 16:56
 * 版    本   ： ${TODO}
 * 描    述   ：  ${TODO}
 * ================================================
 */
public class ScrollTextView extends android.support.v7.widget.AppCompatTextView {
    private float mStartNumber;
    private float mEndNumber;
    public ScrollTextView(Context context) {
        this(context,null);
    }

    public ScrollTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);

    }

    private void init(AttributeSet attrs) {

        startAnim();

    }

    private void startAnim() {
        ValueAnimator animator = ValueAnimator.ofFloat(mStartNumber, mEndNumber);
        animator.setDuration(1000);
        animator.setRepeatCount(4);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                if (fraction < 0.5 ){
                    ScrollTextView.this.setTextColor(getContext().getResources().getColor(R.color.grey_all_9));
                }else {
                    ScrollTextView.this.setTextColor(getContext().getResources().getColor(R.color.red));
                }
                float content = (float) animation.getAnimatedValue();
                String format = new DecimalFormat("##.##").format(content);
                ScrollTextView.this.setText(format);
            }
        });
    }

    public float getStartNumber() {
        return mStartNumber;
    }

    public void setStartNumber(float startNumber,float endNumber) {
        mStartNumber = startNumber;
        mEndNumber = endNumber;
        startAnim();
    }

    public void start(){
        startAnim();
    }

}
