package com.sureone;

import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;


public class MyProgressBar extends View {

    int mMinWidth=40;
    int mWidth=40;
    int mMaxWidth=500;
    int mMinHeight=20;
    int mMaxHeight=20;

    private int mProgress=0;
    private int mMax=1;

    private Drawable mProgressDrawable;
    private Drawable mCurrentDrawable;


    public MyProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDrawingCacheEnabled(true);
    }
    public MyProgressBar(Context context) {
        super(context);
    }
    
    public void setProgressDrawable(Drawable d) {
        if (d != null) {
            d.setCallback(this);

            // Make sure the ProgressBar is always tall enough
            int drawableHeight = d.getMinimumHeight();
            if (mMaxHeight < drawableHeight) {
                mMaxHeight = drawableHeight;
                requestLayout();
            }
        }
        mProgressDrawable = d;
    }
    
    /**
     * @return The drawable currently used to draw the progress bar
     */
    Drawable getCurrentDrawable() {
        return mCurrentDrawable;
    }

    public synchronized void setProgress(int progress) {
        setProgress(progress, false);
    }
    
    synchronized void setProgress(int progress, boolean fromUser) {
        if (progress < 0) {
            progress = 0;
        }

        if (progress > mMax) {
            progress = mMax;
        }

        if (progress != mProgress) {
            mProgress = progress;
        }
		invalidate();
		
    }

    public synchronized int getProgress() {
        return mProgress;
    }

    public synchronized int getMax() {
        return mMax;
    }

    public synchronized void setMax(int max) {
        if (max < 0) {
            max = 0;
        }
        if (max != mMax) {
            mMax = max;
            if (mProgress > max) {
                mProgress = max;
            }
        }
    }
    

	public static final int BACK_COLOR= 0xff000000+221*0x10000+188*0x100+107;
    public static final int FRONT_COLOR=0xff000000+174*0x10000+148*0x100+84;
    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setColor(FRONT_COLOR);
        canvas.drawRect(0, 0, mWidth,mMinHeight, paint);
		float v = (float)mProgress/(float)mMax*(float)mWidth;
        paint.setColor(Color.GREEN);
        canvas.drawRect(0, 0, (int)v,mMinHeight, paint);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable d = mCurrentDrawable;

        int dw = 0;
        int dh = 0;
        if (d != null) {
            dw = Math.max(mMinWidth, Math.min(mMaxWidth, d.getIntrinsicWidth()));
            dh = Math.max(mMinHeight, Math.min(mMaxHeight, d.getIntrinsicHeight()));
        }
		int widthSpec = MeasureSpec.getMode(widthMeasureSpec);
		int width = MeasureSpec.getSize(widthMeasureSpec);
		mWidth=width;
        setMeasuredDimension(mWidth,mMinHeight);
    }
    
}
