package com.example.demolauncher.ecg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.demolauncher.R;

import java.util.ArrayList;

public class MyEcgView extends View {
    private int mWidth , mHeight;
    private float gridSpace = 30.0f;

    private int horizontalLineCount , verticalLineCount; //水平及竖直线条数
    private int points_per_grid = 18; //每小格点数
    private float points_space = gridSpace / points_per_grid; //每点横坐标间距

    private float action_down_X; //每次落点X坐标
    private float total_scroll_X = 0;//历次总的滑动距离
    private float per_scroll_X = 0;//滑动中偏移落点的距离

    private float left_scroll_offset_max_X;// 向左滑动能滑到的最大值，就是曲线的x轴上的长度减去view宽度

    private ArrayList<Integer> dataSource = new ArrayList<>();

    private void createData(){
        for (int i = 0 ; i < 60 ; i ++){
            dataSource.add(2071);dataSource.add(2086);dataSource.add(2099);dataSource.add(2109);
            dataSource.add(2117);dataSource.add(2124);dataSource.add(2130);dataSource.add(2134);
            dataSource.add(2136);dataSource.add(2138);dataSource.add(2139);dataSource.add(2141);
            dataSource.add(2143);dataSource.add(2145);dataSource.add(2147);dataSource.add(2148);
            dataSource.add(2148);dataSource.add(2148);dataSource.add(2147);dataSource.add(2145);
            dataSource.add(2141);dataSource.add(2134);dataSource.add(2125);dataSource.add(2116);
            dataSource.add(2107);dataSource.add(2098);dataSource.add(2088);dataSource.add(2078);
        }
    }

    public MyEcgView(Context context){
        super(context);
        createData();
        init();
    }
    public MyEcgView(Context context , AttributeSet attributeSet){
        super(context , attributeSet);
        createData();
        init();
    }
    public MyEcgView(Context context , AttributeSet attributeSet , int def){
        super(context , attributeSet , def);
        createData();
        init();
    }

    private Canvas cacheCanvas ;
    private Bitmap drawBitmap;
    private Path mPath;
    private Paint mPaint;

    private void init(){
        this.setBackgroundColor(getResources().getColor(R.color.black));
    }

    private void drawBitmap(){
        cacheCanvas = new Canvas();
        mPath = new Path();
        drawBitmap = Bitmap.createBitmap(mWidth , mHeight , Bitmap.Config.ARGB_8888);
        Log.d("tag" , "mWidth=" + mWidth + "\tmHeight=" + mHeight);
        cacheCanvas.setBitmap(drawBitmap);

        mPaint = new Paint(Paint.DITHER_FLAG);
        mPaint.setStrokeWidth(2.0f);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(getResources().getColor(R.color.red));
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);

        for (int i = 0 ; i < dataSource.size() ; i ++){
            if (i == 0) mPath.moveTo(i * points_space , getY_coordinate(dataSource.get(i)));
            else mPath.lineTo(i * points_space , getY_coordinate(dataSource.get(i)));
        }
        cacheCanvas.drawPath(mPath , mPaint);
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case  MotionEvent.ACTION_DOWN :
                  action_down_X = event.getX(0);
                  break;
            case  MotionEvent.ACTION_MOVE :
                    per_scroll_X = event.getX() - action_down_X;
                    invalidate();
                  break;
            case  MotionEvent.ACTION_UP :
                  Log.d("event=" , "index=" + event.getActionIndex());
                  break;
            default:break;
        }
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getWidth();
        mHeight = getHeight();
        horizontalLineCount = mHeight / (int) gridSpace;
        verticalLineCount = mWidth / (int) gridSpace;

        left_scroll_offset_max_X = mWidth - points_space * dataSource.size();
        drawBitmap();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawWave(canvas);
        drawGrid(canvas);

    }

    private void drawWave(Canvas canvas){
        Paint paint = new Paint();
        canvas.drawBitmap(drawBitmap , 0 , 0 , paint);
//        Paint paint = new Paint();
//        paint.setStrokeWidth(2.0f);
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setColor(getResources().getColor(R.color.red));
//        total_scroll_X += per_scroll_X;
//
//        if (total_scroll_X > 0) total_scroll_X = 0;
//        else if (total_scroll_X < left_scroll_offset_max_X) total_scroll_X = left_scroll_offset_max_X;
//
//        Path path = new Path();
//        int index = 0;
//        for (int i = 0 ; i < dataSource.size() ; i ++){
//            float currentX = points_space * i + total_scroll_X;
//            if (currentX >= 0){
//                index = i;
//                path.moveTo(currentX , getY_coordinate(dataSource.get(index)));
//                break;
//            }
//        }
//
//        for (int i = index ; i < dataSource.size() ; i ++){
//            float moveX = points_space * i + total_scroll_X;
//            if (moveX < mWidth){
//                path.lineTo(moveX , getY_coordinate(dataSource.get(i)));
//            }
//        }
//        canvas.drawPath(path , paint);
    }

    private void drawGrid(Canvas canvas){
        PathEffect pathEffect = new DashPathEffect(new float[]{2 , 5} , 0);//第一个数组代表实虚长度，第二个相位代表相对于虚实总长度的左移长度

        for (int i = 0 ; i < horizontalLineCount + 1 ; i ++){ //划横线 浮点型转成整型有损失,所以加一条
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(getResources().getColor(R.color.limegreen));
            paint.setStrokeWidth(1.0f);
            if (i % 5 == 0)paint.setPathEffect(null);
            else paint.setPathEffect(pathEffect);

            Path path = new Path();
            path.moveTo(0 ,  i * gridSpace + (mHeight - gridSpace * horizontalLineCount) / 2);//设置第一条线偏移出事位置
            path.lineTo(mWidth , i * gridSpace + (mHeight - gridSpace * horizontalLineCount) / 2);
            canvas.drawPath(path , paint);
        }
        for (int j = 0 ; j < verticalLineCount + 1 ; j ++ ){
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(getResources().getColor(R.color.limegreen));
            paint.setStrokeWidth(1.0f);
            if (j % 5 == 0) paint.setPathEffect(null);
            else paint.setPathEffect(pathEffect);

            Path path = new Path();
            path.moveTo(gridSpace * j + (mWidth - gridSpace * verticalLineCount) / 2 , 0 );
            path.lineTo( gridSpace * j + (mWidth - gridSpace * verticalLineCount) / 2 , mHeight);
            canvas.drawPath(path , paint);
        }
    }

    public void addData(ArrayList<Integer> data){
            if (data != null) dataSource.addAll(data);
    }

    private float getY_coordinate(int data){
        int y_int = data;
        y_int = (y_int - 2048) *(-1);
        float y_coor = 0.0f;

        y_coor = y_int *3/4 + mHeight / 2;
        return y_coor;
    }
}
