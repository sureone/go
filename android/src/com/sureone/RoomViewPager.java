package com.sureone;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import com.viewpagerindicator.PageIndicator;
import android.widget.ImageView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import com.viewpagerindicator.TitlePageIndicator;
import android.app.Activity;
import android.view.View.OnClickListener;
import android.util.TypedValue;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
public class RoomViewPager extends ViewPager {

    private boolean enabled;

    public RoomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.enabled = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onTouchEvent(event);
        }  
        return false;
    }
	

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onInterceptTouchEvent(event);
        }
 
        return false;
    }
 
    public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}