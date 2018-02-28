package com.alin.commonlibrary.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.alin.commonlibrary.R;


/**
 * ================================================
 * 作    者   ： hailinhe
 * github地址 ： https://github.com/Alin520/Mvp-Rxjava-Retrofit
 * CSDN地址   ： http://blog.csdn.net/hailin123123/article/details/78643330
 * 创建时间    ： 2017/12/27 13:34
 * 版    本   ： ${TODO}
 * 描    述   ：  ${弧形背景图}
 * ================================================
 */
public class ArcBackgroundImageView extends FrameLayout {

    private PointF mStartPoint;
    private PointF mEndPoint;
    private PointF mControlPoint;
    private Path mPath;
    private Paint mPaint;
    private float mArcHeight;
    private int mSaveLayerAlpha;

    public ArcBackgroundImageView(Context context) {
        this(context,null);
    }

    public ArcBackgroundImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ArcBackgroundImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(attrs);
    }

    private void initialize(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.arc_background_image_view);
        try {
            mArcHeight = ta.getDimension(R.styleable.arc_background_image_view_arcHeight, 50);
        }catch (Throwable throwable){
            throwable.printStackTrace();
        }finally {
            ta.recycle();
        }
        
        mStartPoint = new PointF();
        mEndPoint = new PointF();
        mControlPoint = new PointF();
        mPath = new Path();
        mPath.setFillType(Path.FillType.INVERSE_WINDING);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
//        mPaint.setStrokeWidth(10);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        this.setWillNotDraw(false);
        this.setLayerType(LAYER_TYPE_HARDWARE,null);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mStartPoint.x = 0;
        mStartPoint.y = h;
        mEndPoint.x = w;
        mEndPoint.y = h;

        if (mArcHeight < h){
            mControlPoint.x = w / 2;
            mControlPoint.y = h - mArcHeight;
        }
        mPath.reset();
        mPath.addRect(new RectF(0,0,mEndPoint.x,mEndPoint.y), Path.Direction.CCW);
        mPath.moveTo(mStartPoint.x,mStartPoint.y);
        mPath.quadTo(mControlPoint.x,mControlPoint.y,mEndPoint.x,mEndPoint.y);
        invalidate();
    }

    @SuppressLint("WrongConstant")
    @Override
    public void draw(Canvas canvas) {
        mSaveLayerAlpha = 0;
        int height = getHeight();
        int width = getWidth();
        if (height > 0 && width > 0 && mArcHeight > 0){
            mSaveLayerAlpha = canvas.saveLayerAlpha(0f, 0f, (float)width, (float) height, 255, Canvas.HAS_ALPHA_LAYER_SAVE_FLAG);
        }
        super.draw(canvas);
        if(mSaveLayerAlpha != 0){
            canvas.drawPath(mPath,mPaint);
            canvas.restoreToCount(mSaveLayerAlpha);
        }

    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}
