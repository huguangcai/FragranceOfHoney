package com.ysxsoft.fragranceofhoney.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Create By 胡
 * on 2019/10/25 0025
 */
public class InfoRecyclerView extends RecyclerView {
    public InfoRecyclerView(@NonNull Context context) {
        super(context);
    }

    public InfoRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return false;
    }
}
