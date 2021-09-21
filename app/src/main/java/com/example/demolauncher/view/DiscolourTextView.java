package com.example.demolauncher.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

import com.example.demolauncher.R;

import androidx.appcompat.widget.AppCompatTextView;

/*******
 * created by Ethan Lee
 * on 2021/1/10
 *******/
public class DiscolourTextView extends AppCompatTextView {
    private int mChangeColor = Color.BLUE;
    private Paint originalPaint;
    private Paint changePaint;
    private Paint measurePaint;

    public DiscolourTextView(Context context){
       this(context , null);
    }
    public DiscolourTextView(Context context, AttributeSet attributeSet){
       this(context, attributeSet , 0);
    }
    public DiscolourTextView(Context context , AttributeSet attributeSet , int def){
       super(context , attributeSet , def);
       initRes(context, attributeSet);
    }

    private void initRes(Context context , AttributeSet attributeSet){
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet , R.styleable.DiscolourTextView);
        mChangeColor = typedArray.getColor(R.styleable.DiscolourTextView_ChangeColor , mChangeColor);
        setPaint();
        typedArray.recycle();
    }
    private void setPaint(){
        originalPaint = new Paint();
        originalPaint.setAntiAlias(true);//抗锯齿
        originalPaint.setDither(true);//芳抖动
        originalPaint.setTextSize(getTextSize());
        originalPaint.setColor(Color.BLACK);
        
        changePaint = new Paint();
        changePaint.setTextSize(getTextSize());
        changePaint.setColor(mChangeColor);
        changePaint.setAntiAlias(true);
        changePaint.setDither(true);

        measurePaint = new Paint();
        measurePaint.setTextSize(getTextSize());
        measurePaint.setDither(true);
        measurePaint.setAntiAlias(true);
        measurePaint.setColor(getCurrentTextColor());

    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        Paint.FontMetricsInt fontMetricsInt = measurePaint.getFontMetricsInt();
        int dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;
        int baseLine = getHeight() / 2 + dy;

        Rect rect = new Rect();
        String text = getText().toString();
        measurePaint.getTextBounds(text , 0 , text.length() , rect);
        int starX = getWidth() / 2 - rect.width() / 2;
        canvas.drawText(text , starX , baseLine , originalPaint);
    }
}
