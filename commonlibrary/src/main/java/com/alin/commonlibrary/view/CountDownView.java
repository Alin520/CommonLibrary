package com.alin.commonlibrary.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.alin.commonlibrary.R;
import com.alin.commonlibrary.annotations.TargetLog;
import com.alin.commonlibrary.util.AppUtil;
import com.alin.commonlibrary.util.CommonUtil;
import com.alin.commonlibrary.util.LogUtil;
import com.alin.commonlibrary.util.ResourcesUtil;


/**
 * @创建者 hailin
 * @创建时间 2017/11/23 11:48
 * @描述 ${倒计时}.
 */
@TargetLog(CountDownView.class)
public class CountDownView extends View{

    private int mInnerCircleColor;          //内圆颜色
    private int mOuterCircleColor;         //外圆颜色
    private float mInnerCircleRaduis;         //内圆半径
    private float mOuterCircleRaduis;         //外圆半径
    private float mTextSize;                     //字体大小
    private int mTextColor;                      //内圆半径
    private boolean mPositionDirection;          //方向，true正方向
    private int mTime;                           //倒计时时间
    private Paint mOuterCirclepaint;
    private Paint mInnerCirclePaint;
    private Paint mTextPaint;
    private int mStartProgress;                 //开始进度
    private int mEndProgress;                   //结束进度
    private float mCurrentProgress;                //当前进度
    private boolean mHasAnimator;               //是否需要动画效果

    private AnimatorSet mAnimatorSet;
    private String mText;
    private ValueAnimator mAnimator1;
    private int mCurrentTime;

    public CountDownView(Context context) {
        this(context,null);
    }

    public CountDownView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CountDownView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(attrs);
    }

    private void initialize(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.countdown_view);
            if (ta != null) {
                try {
                    mInnerCircleColor = ta.getColor(R.styleable.countdown_view_innerCircleColor, ResourcesUtil.getColor(getContext(),R.color.transparent_gray));
                    mOuterCircleColor = ta.getColor(R.styleable.countdown_view_outerCircleColor,  ResourcesUtil.getColor(getContext(),R.color.red));
                    mInnerCircleRaduis = ta.getDimension(R.styleable.countdown_view_innerCircleRaduis,  ResourcesUtil.getDimension(getContext(),R.dimen.default_large_text));
                    mOuterCircleRaduis = ta.getDimension(R.styleable.countdown_view_outerCircleRaduis,  ResourcesUtil.getDimension(getContext(),R.dimen.default_extra_large_text));
                    mTextSize = ta.getDimension(R.styleable.countdown_view_textSize, ResourcesUtil.getDimension(getContext(),R.dimen.default_text_size));
                    mTextColor = ta.getColor(R.styleable.countdown_view_textColor, ResourcesUtil.getColor(getContext(),R.color.white));
                    mPositionDirection = ta.getBoolean(R.styleable.countdown_view_positionDirection, false);
                    mTime = ta.getInt(R.styleable.countdown_view_time, 3);
                    mHasAnimator = ta.getBoolean(R.styleable.countdown_view_hasAnimator, true);
                }catch (Throwable t){
                    t.printStackTrace();
                }finally {
                    ta.recycle();
                }
            }
        }

//        初始化画笔
        initPaint();
        mText = CommonUtil.fillHtmlString(getContext(), R.string.format_splash_skip,mTime).toString();
        if (mPositionDirection) {
            mStartProgress = 360;
            mEndProgress = 0;
        }else {
            mStartProgress = 0;
            mEndProgress = 360;
        }

        mCurrentProgress = mStartProgress;
    }

    private void initPaint() {
//        内圆画笔
        mInnerCirclePaint = new Paint();
        mInnerCirclePaint.setAntiAlias(true);
        mInnerCirclePaint.setStyle(Paint.Style.FILL);
        mInnerCirclePaint.setColor(mInnerCircleColor);
        mInnerCirclePaint.setStrokeWidth(mInnerCircleRaduis);
//        外圆画笔
        mOuterCirclepaint = new Paint();
        mOuterCirclepaint.setAntiAlias(true);
        mOuterCirclepaint.setStyle(Paint.Style.STROKE);
        mOuterCirclepaint.setColor(mOuterCircleColor);
        mOuterCirclepaint.setStrokeWidth(mOuterCircleRaduis - mInnerCircleRaduis);
//        文字
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        AppUtil.checkLargeThanZero(mOuterCircleRaduis,"OuterCircleRaduis must be > 0!");
        setMeasuredDimension((int)mOuterCircleRaduis * 2,(int)mOuterCircleRaduis * 2);
    }


    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        AppUtil.checkLargeThanZero(mInnerCircleRaduis,"InnerCircleRaduis must be > 0!");
        AppUtil.checkLargeThanZero(mOuterCircleRaduis,"OuterCircleRaduis must be > 0!");
        AppUtil.checkLargeThanZero(mOuterCircleRaduis - mInnerCircleRaduis,"OuterCircleRaduis must be > InnerCircleRaduis!");
//        内圆
        canvas.drawCircle(mOuterCircleRaduis,mOuterCircleRaduis,mInnerCircleRaduis,mInnerCirclePaint);
//        外圆
        if (mHasAnimator) {
            float left = (mOuterCircleRaduis - mInnerCircleRaduis) / 2;
            float right = 2 * mOuterCircleRaduis - (mOuterCircleRaduis - mInnerCircleRaduis) / 2;
            float top = (mOuterCircleRaduis - mInnerCircleRaduis) / 2;
            float bottom = 2 * mOuterCircleRaduis - (mOuterCircleRaduis - mInnerCircleRaduis) / 2;
            RectF oval = new RectF(left,top,right,bottom); //外圆的内接正方形
            canvas.drawArc(oval,-90,mCurrentProgress,false,mOuterCirclepaint);
        }else {
            canvas.drawCircle(mOuterCircleRaduis,mOuterCircleRaduis,mOuterCircleRaduis,mInnerCirclePaint);
        }

//        文字
        if (!TextUtils.isEmpty(mText)) {
            Rect bounds = new Rect();
            mTextPaint.getTextBounds(mText,0,mText.length(),bounds);
            float x = (getWidth() - bounds.width()) / 2;
            float y = (getHeight() + bounds.height()) / 2;
            canvas.drawText(mText,x,y,mTextPaint);
        }
    }

//    开始倒计时
    public void start(){
        if (mHasAnimator) {
            mAnimator1 = new ValueAnimator().ofFloat(mCurrentProgress,mEndProgress);
            mAnimator1.setInterpolator(new LinearInterpolator());
            mAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float animatedValue = (float) animation.getAnimatedValue();
                    mCurrentProgress = animatedValue;
                    LogUtil.showLog(CountDownView.class,"....animator1==" + animatedValue);
                    invalidate();

                }
            });
        }

        ValueAnimator animator2 = new ValueAnimator().ofInt(mTime, 0);
        animator2.setInterpolator(new LinearInterpolator());
        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrentTime = (int) animation.getAnimatedValue();
                mText = CommonUtil.fillHtmlString(getContext(), R.string.format_splash_skip,mCurrentTime).toString();
                if (mCurrentTime == 0) return;
                LogUtil.showLog(CountDownView.class,"animator2==" + mCurrentTime);
                if (!mHasAnimator) {
                    invalidate();
                }
            }
        });

        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.setDuration(mTime * 1000);
        if (mHasAnimator && mAnimator1 != null) {
            mAnimatorSet.playTogether(mAnimator1,animator2);
        }else {
            mAnimatorSet.playTogether(animator2);
        }
        mAnimatorSet.start();

        mAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                LogUtil.showLog(CountDownView.class,"onAnimationStart......");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                LogUtil.showLog(CountDownView.class,"....onAnimationEnd......");
                if (mFinishListener != null && mCurrentTime == 0) {
                    mFinishListener.onFinishListener();
                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {
                LogUtil.showLog(CountDownView.class,"........onAnimationCancel......");
                if (mFinishListener != null) {
                    mFinishListener.onCancleListener();
                }
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public int getInnerCircleColor() {
        return mInnerCircleColor;
    }

    public void setInnerCircleColor(int innerCircleColor) {
        mInnerCircleColor = innerCircleColor;
    }

    public int getOuterCircleColor() {
        return mOuterCircleColor;
    }

    public void setOuterCircleColor(int outerCircleColor) {
        mOuterCircleColor = outerCircleColor;
    }

    public float getInnerCircleRaduis() {
        return mInnerCircleRaduis;
    }

    public void setInnerCircleRaduis(float innerCircleRaduis) {
        AppUtil.checkLargeThanZero(innerCircleRaduis,"setInnerCircleRaduis() must be innerCircleRaduis > 0 !");
        mInnerCircleRaduis = CommonUtil.dp2px(getContext(),innerCircleRaduis);
    }

    public float getOuterCircleRaduis() {
        return mOuterCircleRaduis;
    }

    public void setOuterCircleRaduis(float outerCircleRaduis) {
        AppUtil.checkLargeThanZero(outerCircleRaduis,"setOuterCircleRaduis() must be outerCircleRaduis > 0 !");
        mOuterCircleRaduis = CommonUtil.dp2px(getContext(),outerCircleRaduis);
    }

    public float getTextSize() {
        return mTextSize;
    }

    public void setTextSize(float textSize) {
        AppUtil.checkLargeThanZero(textSize,"setTextSize() must be textSize > 0 !");
        mTextSize = CommonUtil.dp2px(getContext(),textSize);
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
    }

    public boolean isPositionDirection() {
        return mPositionDirection;
    }

    public void setPositionDirection(boolean positionDirection) {
        mPositionDirection = positionDirection;
    }

    public int getTime() {
        return mTime;
    }

    /**
     *  单位  秒
     * @param time
     */
    public void setTime(int time) {
        mTime = time;
    }

    public boolean isHasAnimator() {
        return mHasAnimator;
    }

    public void setHasAnimator(boolean hasAnimator) {
        mHasAnimator = hasAnimator;
    }

    public String getText() {
        return mText;
    }

    public void setText(@Nullable String text) {
        mText = text;
    }

    public OnCountDownFinishListener getFinishListener() {
        return mFinishListener;
    }

    public void setOnCountDownFinishListener(OnCountDownFinishListener finishListener) {
        mFinishListener = finishListener;
    }
    
    //    结束倒计时
    public void cancle(){
        if (mAnimatorSet != null && isRunning()) {
            mAnimatorSet.cancel();
            mAnimatorSet = null;
        }
    }

    //    倒计时是否还在进行
    public boolean isRunning(){
        return mAnimatorSet == null ? false : mAnimatorSet.isRunning();
    }


    public OnCountDownFinishListener mFinishListener;

    /**
     *  isClickFinsh true 点击结束计时
     */
    public interface OnCountDownFinishListener{
        //倒计时计时结束回调
        void onFinishListener();
        //取消倒计时回调
        void onCancleListener();
    }
}
