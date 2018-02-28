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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.alin.commonlibrary.R;


/**
 * ================================================
 * 作    者   ： hailinhe
 * github地址 ： https://github.com/Alin520/Mvp-Rxjava-Retrofit
 * CSDN地址   ： http://blog.csdn.net/hailin123123/article/details/78643330
 * 创建时间    ： 2017/12/28 16:36
 * 版    本   ： ${TODO}
 * 描    述   ：  ${TODO}
 * ================================================
 */
public class ArcFramLayout extends FrameLayout {
    private Paint paint;
    private Path path;
    private RectF rect;

    private PointF mStartPoint;
    private PointF mEndPoint;
    private PointF mControlPoint;
    private float mArcHeight;

    public ArcFramLayout(@NonNull Context context) {
        this(context,null);
    }

    public ArcFramLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context,null,0);
    }

    public ArcFramLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(attrs);
    }

    private void init(AttributeSet attrs) {
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

        this.path = new Path();
        this.paint = new Paint();
        this.path.setFillType(Path.FillType.INVERSE_WINDING);
        this.paint.setAntiAlias(true);
        this.paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        this.setWillNotDraw(false);
        this.setLayerType(LAYER_TYPE_HARDWARE, (Paint)null);
    }


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
        path.reset();
        path.addRect(new RectF(0,0,mEndPoint.x,mEndPoint.y), Path.Direction.CCW);
        path.moveTo(mStartPoint.x,mStartPoint.y);
        path.quadTo(mControlPoint.x,mControlPoint.y,mEndPoint.x,mEndPoint.y);
        invalidate();
    }

    @SuppressLint("WrongConstant")
    public void draw(Canvas canvas) {
        int saveLayerCount = 0;
        int width = this.getWidth();
        int height = this.getHeight();
        if( width > 0 && height > 0) {
            saveLayerCount = canvas.saveLayerAlpha(0.0F, 0.0F, (float)width, (float)height, 255, Canvas.HAS_ALPHA_LAYER_SAVE_FLAG);
        }

        super.draw(canvas);
        if(saveLayerCount != 0) {
            canvas.drawPath(this.path, this.paint);
            canvas.restoreToCount(saveLayerCount);
        }

    }


}
