package com.example.demolauncher.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Handler;
import android.os.MessageQueue;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

import androidx.lifecycle.LifecycleObserver;

public class MyViewGroup extends ViewGroup {
    private Scroller scroller;
    private VelocityTracker mVelocityTracker;
    private int lastX = 0;
    private int lastY = 0;
    private int mChildWidth = 0;
    private int currentIndex = 0;

    public MyViewGroup(Context context){
        super(context);
        init(context);
    }

    public MyViewGroup(Context context , AttributeSet attributeSet){
        super(context , attributeSet);
        init(context);
    }

    public MyViewGroup(Context context , AttributeSet attributeSet , int def){
        super(context , attributeSet , def);
        init(context);
    }

    private void init(Context context){
        scroller = new Scroller(context);
        mVelocityTracker = VelocityTracker.obtain();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        Log.d("show" , "Count=" + getChildCount());
        measureChildren(widthMeasureSpec , heightMeasureSpec);
        if (getChildCount() == 0)
            setMeasuredDimension(0 , 0);
        else if ((widthMode == MeasureSpec.AT_MOST) && (heightMode == MeasureSpec.AT_MOST))
        {
          View child = getChildAt(0);
         int childWidth = child.getMeasuredWidth();
         int childHeight = child.getMeasuredHeight();
         setMeasuredDimension(childWidth * getChildCount() ,  childHeight);
        }else if (widthMode == MeasureSpec.AT_MOST){
            View child = getChildAt(0);
            int childWidth = child.getMeasuredWidth();
            setMeasuredDimension(childWidth * getChildCount() , height);
        }else if (heightMode == MeasureSpec.AT_MOST){
            View child = getChildAt(0);
            int childHeight = child.getMeasuredHeight();
            setMeasuredDimension(width , childHeight);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        View child;
        int left = 0;
        for (int i = 0 ; i < childCount ; i ++){
            child = getChildAt(i);
            int childWidth = child.getMeasuredWidth();
            mChildWidth = childWidth;
            child.layout(left , 0 , left + childWidth , child.getMeasuredHeight());
            left += childWidth;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mVelocityTracker.addMovement(event);
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN : break;
            case MotionEvent.ACTION_MOVE :
                int moveX = x - lastX;
                scrollBy(-moveX , 0);
                break;
            case MotionEvent.ACTION_UP :
                int distance = getScrollX() - currentIndex * mChildWidth;
                if (Math.abs(distance) > mChildWidth / 2){
                    if (distance > 0){
                        currentIndex ++ ;
                    }else {
                        currentIndex --;
                    }
                }else {
                    mVelocityTracker.computeCurrentVelocity(1000);
                    float VX = mVelocityTracker.getXVelocity();
                    if (Math.abs(VX) > 50){
                        if (VX < 0) currentIndex ++ ;
                        else currentIndex -- ;
                    }
                }

                if (currentIndex < 0) currentIndex = 0;
                else if (currentIndex >= getChildCount()) currentIndex = getChildCount() - 1;
                smoothScroll(currentIndex * mChildWidth , 0);
                mVelocityTracker.clear();
                break;
            default: break;
        }
        lastX = x;
        lastY = y;
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()){
            scrollTo(scroller.getCurrX() , getScrollY());
            postInvalidate();
//            requestLayout();
        }
    }


    private void smoothScroll(int destX , int destY){
        scroller.startScroll(getScrollX() , getScrollY() ,
                destX - getScrollX() , destY - getScrollY() , 1000);
        invalidate();
//        requestLayout();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        boolean isIntercept = false;
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN :
                isIntercept = false;
                if (! scroller.isFinished())
                    scroller.abortAnimation();
                break;
            case MotionEvent.ACTION_MOVE :
                 int deltaX = x - lastX;
                 int deltaY = y - lastY;
                 if (Math.abs(deltaX) > Math.abs(deltaY))
                     isIntercept = true;
                 else
                     isIntercept = false;
                 break;
            case MotionEvent.ACTION_UP :  break;
            default: break;
        }
        lastX = x;
        lastY = y;
        return isIntercept;
    }
}
