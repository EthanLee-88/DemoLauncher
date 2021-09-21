package com.example.demolauncher.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.demolauncher.R;

import androidx.annotation.ColorInt;
/*******
 * created by Ethan Lee
 * on 2021/1/10
 *******/
public class MyTextView extends View {
    private String mText = ""; //自定义属性
    private int mTextColor = Color.BLACK;
    private int mTextSize = 20;

    private Paint measurePaint; //测量文本尺寸

    public MyTextView(Context context){
        this(context , null);
    }
    public MyTextView(Context context , AttributeSet attributeSet){
        this(context , attributeSet , 0);
    }
    public MyTextView(Context context , AttributeSet attributeSet , int defStyle){
        super(context , attributeSet , defStyle);
        TypedArray array = context.obtainStyledAttributes(attributeSet , R.styleable.MyTextView);
        //获取文本
        mText = array.getString(R.styleable.MyTextView_MyText);
        //获取字体颜色值
        mTextColor = array.getColor(R.styleable.MyTextView_MyTextColor , mTextColor);
        //获取字体尺寸
        mTextSize = (int) array.getDimension(R.styleable.MyTextView_MyTextSize , mTextSize);

        array.recycle(); // 享元模式，对象格式化放入对象池复用

        measurePaint = new Paint();
        measurePaint.setAntiAlias(true); //抗锯齿

        measurePaint.setTextSize(mTextSize); //设置画笔文本字体大小，用于测量View
        measurePaint.setColor(mTextColor); //字体颜色
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);  //测量尺寸
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec); //测量模式
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST){
            Rect textBound = new Rect(); //文本矩形边界
            measurePaint.getTextBounds(mText , 0 , mText.length() , textBound);//用画笔测量文本矩形边界
            width = textBound.width() + getPaddingLeft() + getPaddingRight();//获取控件宽度
        }
        if (heightMode == MeasureSpec.AT_MOST){
            Rect bound = new Rect();
            measurePaint.getTextBounds(mText , 0 , mText.length() , bound);
            height = bound.height() + getPaddingTop() + getPaddingBottom();
        }
        Log.d("TAG" , "width=" + width + "height=" + height);
        setMeasuredDimension(width , height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint.FontMetricsInt metricsInt = measurePaint.getFontMetricsInt();
        // dy 是中线到基线baseLine的距离
        // metricsInt.bottom是基线到控件底部的距离，metricsInt.top是基线到控件顶部的距离
        // 中线位置减去基线到底部的距离即得到dy的距离
        int dy = (metricsInt.bottom - metricsInt.top) / 2 - metricsInt.bottom;
        // 控件高的一半加上dy即得到基线到控件顶部的距离y
        int baseLine = getHeight() / 2 + dy;
        Log.d("TAG" , "height=" + getHeight() + "\ttop=" +
                metricsInt.top + "\tbottom=" + metricsInt.bottom);
        canvas.drawText(mText , getPaddingLeft() , baseLine , measurePaint);
    }

    public void setText(String text){
        this.mText = text;
        invalidate();
    }
    public void setTextColor(@ColorInt int color){
        this.mTextColor = color;
        this.measurePaint.setColor(mTextColor);
        invalidate();
    }
}
