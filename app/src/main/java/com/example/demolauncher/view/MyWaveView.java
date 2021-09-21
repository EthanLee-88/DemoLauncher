package com.example.demolauncher.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.demolauncher.R;

public class MyWaveView extends View {
    private Context mContext;
    private int totalWidth = 0;
    private int totalHeight = 0;
    private float mCycle = 0f; //曲线周期
    private float[] myPoint;//曲线上的Y点值
    private int mOffset = 1; // 数组数字偏移个数

    private Paint mPaint = new Paint();

    public MyWaveView(Context context){
        super(context);
        init(context);

    }
    public MyWaveView(Context context , AttributeSet attributeSet){
        super(context ,attributeSet);
        init(context);
    }

    private void init(Context context){
        mContext = context;
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(context.getResources().getColor(R.color.actionsheet_blue));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        if ((widthMode == MeasureSpec.AT_MOST) && (heightMode == MeasureSpec.AT_MOST)){
            setMeasuredDimension(300 , 200);
        }else if (widthMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(measureHeight * 2 , measureHeight);
        }else if (heightMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(measureWidth , measureWidth / 2);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        totalWidth = getWidth();
        totalHeight = getHeight();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        totalWidth = getWidth();
        totalHeight = getHeight();
        myPoint = new float[totalWidth];
        mCycle = (float) (2 * Math.PI / totalWidth); // 根据宽度计算三角函数周期

        for (int i = 0 ; i < totalWidth ; i ++){   // 利用三角函数生成数组
         myPoint[i]  = (float) (50 * Math.sin(mCycle * i)); // Y = Asin（mCycle * X + phase）
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
         for (int i = 0 ; i < totalWidth ; i ++){  // 竖直方向画直线
             canvas.drawLine(i , myPoint[i] + totalHeight / 2 , i , totalHeight , mPaint);
         }
         setOffSet(5);
         invalidate();
    }

    public void setOffSet(int offSet){  // 旋转个数
        mOffset =  Math.min(totalHeight / 2 , offSet);
       for (int i = 0 ; i <= mOffset ; i ++){
           turnArray();
       }
    }

    private void turnArray(){  // 数组旋转
        float team = myPoint[0];
        for (int i = 1 ; i < myPoint.length ; i ++){
            myPoint[i - 1] = myPoint[i];
        }
        myPoint[myPoint.length - 1] = team;
    }
}
