package com.ysxsoft.fragranceofhoney.widget;

import android.content.Context;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class HotRecycleView extends RecyclerView {

    private float downX;
    private float downY;
    private float moveX;
    private float moveY;

    public HotRecycleView(@NonNull Context context) {
        super(context);
    }

    public HotRecycleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        System.out.println("onInterceptTouchEvent=====》》onInterceptTouchEvent》》");
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = e.getX();
                downY = e.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = e.getX();
                moveY = e.getY();
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }

        if (Math.abs(moveX - downX) > Math.abs(moveY - downY)) {
            System.out.println("左右滑动=====》？》》》"+(moveX - downX));
//            getParent().requestDisallowInterceptTouchEvent(true);
            return true;
        }
        return super.onTouchEvent(e);
//        return false;
    }

    /**
     * 返回true 说明成功拦截  交给本层的onTouchEvent处理事件
     *
     * @param e
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        return true;
    }

}
