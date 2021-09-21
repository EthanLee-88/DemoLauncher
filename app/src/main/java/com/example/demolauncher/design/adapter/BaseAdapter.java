package com.example.demolauncher.design.adapter;

import android.view.View;

public abstract class BaseAdapter {

    abstract int getCount();

    abstract View getView(View parent, int index);

}
