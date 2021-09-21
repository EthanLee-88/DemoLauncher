package com.example.demolauncher.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.demolauncher.R;

/*******
 * 字体变色的TextView
 *
 * created by Ethan Lee
 * on 2021/1/17
 *******/
public class ColorTrackTextView extends TextView {
    private static final String TAG = "ColorTrackTextView";
    private int mOriginColor = Color.BLACK;
    private int mChangeColor = Color.RED;
    private Paint textPaint, changeColorTextPaint;
    private Rect textBound;
    private int changePercent = 0;
    private boolean changeLeft = true;

    public ColorTrackTextView(Context context) {
        this(context, null);
    }

    public ColorTrackTextView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ColorTrackTextView(Context context, AttributeSet attributeSet, int def) {
        super(context, attributeSet, def);
        initRes(context, attributeSet, def);
    }

    private void initRes(Context context, AttributeSet attributeSet, int def) {
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.ColorTrackTextView);
        mOriginColor = typedArray.getColor(R.styleable.ColorTrackTextView_origin_color, mOriginColor);
        mChangeColor = typedArray.getColor(R.styleable.DiscolourTextView_ChangeColor, mChangeColor);
        textPaint = new Paint();
        textPaint.setColor(mOriginColor);
        textPaint.setTextSize(getTextSize());
        textPaint.setAntiAlias(true);
        textPaint.setDither(true);
        changeColorTextPaint = new Paint();
        changeColorTextPaint.setTextSize(getTextSize());
        changeColorTextPaint.setColor(mChangeColor);
        changeColorTextPaint.setAntiAlias(true);
        changeColorTextPaint.setDither(true);
        textBound = new Rect();
        changeLeftColor(80);
        typedArray.recycle();
    }

    public void changeLeftColor(int percent) {
        this.changeLeft = true;
        changePercent(percent);
    }

    public void changeRightColor(int percent) {
        this.changeLeft = false;
        changePercent(percent);
    }

    private void changePercent(int percent) {
        if (percent < 0) {
            this.changePercent = 0;
        } else if (percent > 100) {
            this.changePercent = 100;
        } else {
            this.changePercent = percent;
        }
        invalidate();
    }

    private Rect getTextRect() {
        if (textPaint != null) {
            textPaint.getTextBounds(getText().toString(), 0, getText().toString().length(), textBound);
            return textBound;
        } else {
            return null;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST) {
            textPaint.getTextBounds(getText().toString(), 0, getText().toString().length(), textBound);
            width = textBound.width() + getPaddingLeft() + getPaddingRight();
        }
        if (heightMode == MeasureSpec.AT_MOST) {
            textPaint.getTextBounds(getText().toString(), 0, getText().toString().length(), textBound);
            height = textBound.height() + getPaddingTop() + getPaddingBottom();
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);   // 自己画字
        Paint.FontMetricsInt fontMetricsInt = textPaint.getFontMetricsInt();
        int dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;
        int baseLine = getHeight() / 2 + dy;
        int x = getWidth() / 2 - getTextRect().width() / 2;
        canvas.save();
        canvas.drawText(getText().toString(), x, baseLine, textPaint);
        canvas.restore();
        canvas.save();
        if (changeLeft) {
            canvas.clipRect(0, 0, getWidth() * changePercent / 100, getHeight());
        } else {
            canvas.clipRect(getWidth() * changePercent / 100, 0, getWidth(), getHeight());
        }
        canvas.drawText(getText().toString(), x, baseLine, changeColorTextPaint);
        canvas.restore();
    }
}
