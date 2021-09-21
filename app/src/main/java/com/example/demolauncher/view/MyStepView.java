package com.example.demolauncher.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.demolauncher.R;

/*******
 * created by Ethan Lee
 * on 2021/1/3
 *******/
public class MyStepView extends View {
    private int mStepTextColor = Color.BLACK;
    private int mStepTextSize = 30;
    private int mStepBorderWidth = 8;
    private int mInnerBorderColor = Color.GRAY;
    private int mOuterBorderColor = Color.BLUE;
    private int mStepCurrentSteps = 0;
    private int mStepTargetSteps = 5000;

    private Paint outPaint;
    private RectF rectF;
    private Paint textPaint;

    public MyStepView(Context context){
        this(context , null);
    }
    public MyStepView(Context context , AttributeSet attributeSet){
        this(context , attributeSet , 0);
    }
    public MyStepView(Context context , AttributeSet attributeSet , int defStyle){
        super(context , attributeSet , defStyle);
        TypedArray array = context.obtainStyledAttributes(attributeSet , R.styleable.MyStepView);
        mStepTextSize = (int) array.getDimension(R.styleable.MyStepView_StepTextSize , mStepTextSize);
        mStepTextColor = array.getColor(R.styleable.MyStepView_StepTextColor , mStepTextColor);
        mStepBorderWidth = (int) array.getDimension(R.styleable.MyStepView_StepBorderWidth , mStepBorderWidth);
        mInnerBorderColor = array.getColor(R.styleable.MyStepView_InnerBorderColor , mInnerBorderColor);
        mOuterBorderColor = array.getColor(R.styleable.MyStepView_OuterBorderColor , mOuterBorderColor);
        mStepCurrentSteps = array.getInteger(R.styleable.MyStepView_StepCurrentSteps , mStepCurrentSteps);
        mStepTargetSteps = array.getInteger(R.styleable.MyStepView_StepTargetSteps , mStepTargetSteps);
        array.recycle();

        outPaint = new Paint();
        outPaint.setColor(mOuterBorderColor);
        outPaint.setAntiAlias(true);
        outPaint.setStyle(Paint.Style.STROKE);
        outPaint.setStrokeCap(Paint.Cap.ROUND);
        outPaint.setStrokeWidth(mStepBorderWidth);
        rectF = new RectF();

        textPaint = new Paint();
        textPaint.setColor(mStepTextColor);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(mStepTextSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if ((widthMode == MeasureSpec.AT_MOST) && (heightMode == MeasureSpec.AT_MOST)){
            setMeasuredDimension(680 , 680);
        }else if (widthMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(height , height);
        }else if (heightMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(width , width);
        }else {
            setMeasuredDimension(width > height ? height : width , width > height ? height : width);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        rectF.set(mStepBorderWidth / 2 , mStepBorderWidth / 2 ,
                getWidth() - mStepBorderWidth / 2 , getHeight() - mStepBorderWidth / 2);
        canvas.drawArc(rectF , -45 , 270 , false , outPaint);

        Paint.FontMetricsInt fontMetricsInt = textPaint.getFontMetricsInt();
        int dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;
        int baseLine = getHeight() / 2 + dy;

        Rect rect = new Rect();
        String text = mStepCurrentSteps + "\n步";
        textPaint.getTextBounds(text , 0 , text.length() , rect);
        int starX = getWidth() / 2 - rect.width() / 2;
        canvas.drawText(text , starX , baseLine , textPaint);
    }
}
