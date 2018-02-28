package com.alin.commonlibrary.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;

/**
 * 图标居中的RadioButton，不可设置文字
 */
public class CenterIconRadioButton extends AppCompatRadioButton
{
    private Drawable buttonDrawable;

    private ColorFilter greyFilter;

    public CenterIconRadioButton(Context context)
    {
        super(context);
        init(context);
    }

    public CenterIconRadioButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }

    private void init(Context context)
    {
        this.greyFilter = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
    }

    @Override
    public void setButtonDrawable(Drawable buttonDrawable)
    {
        this.buttonDrawable = buttonDrawable;
        super.setButtonDrawable(buttonDrawable);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        Drawable background = this.getBackground();

        if(background != null)
        {
            background.draw(canvas);
        }

        if(buttonDrawable != null)
        {
            int left = this.getPaddingLeft();
            int right = this.getWidth() - this.getPaddingRight();
            int top = this.getPaddingTop();
            int bottom = this.getHeight() - this.getPaddingBottom();
            int width = right - left;
            int height = bottom - top;

            if(width > 0 && height > 0)
            {
                int iconWidth = buttonDrawable.getIntrinsicWidth();
                int iconHeight = buttonDrawable.getIntrinsicHeight();
                int iconLeft = left + (width - iconWidth) / 2;
                int iconTop = top + (height - iconHeight) / 2;
                int iconRight = iconLeft + iconWidth;
                int iconBottom = iconTop + iconHeight;
                buttonDrawable.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                buttonDrawable.setColorFilter(isPressed() && isEnabled()? greyFilter: null);
                buttonDrawable.draw(canvas);
            }
        }
    }
}