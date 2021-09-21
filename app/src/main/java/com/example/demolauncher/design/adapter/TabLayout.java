package com.example.demolauncher.design.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import static android.widget.LinearLayout.HORIZONTAL;

public class TabLayout extends ScrollView {

    private LinearLayout mLinearLayout;
    private BaseAdapter mBaseAdapter;
    private int itemCount;

    public TabLayout(Context context) {
        this(context, null);
    }

    public TabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr){
        mLinearLayout = new LinearLayout(context);
        mLinearLayout.setOrientation(HORIZONTAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        addView(mLinearLayout);
    }

    public void setAdapter(BaseAdapter baseAdapter) throws Exception {
        if (baseAdapter == null){
            throw  new NullPointerException("Adapter is null");
        }
        mBaseAdapter = baseAdapter;
        itemCount = mBaseAdapter.getCount();
        addItem();
    }

    private void addItem(){
       for (int i = 0 ; i < itemCount; i ++){
           mLinearLayout.addView(mBaseAdapter.getView(this, i));
       }
    }
}
