package com.ysxsoft.fragranceofhoney.utils;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public abstract class BaseFragment extends Fragment {

    @Nullable
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(getLayoutResId(), null);
        return view;
    }

    protected abstract int getLayoutResId();

    /**
     * 获取指定Id的View
     */
    protected <T extends View> T getViewById(int resId) {
        return (T) view.findViewById(resId);
    }

    /**
     * 弹出Toast信息
     */
    protected void showToastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 弹出Toast信息
     */
    protected void showToastMessage(int resId) {
        showToastMessage(getResources().getString(resId));
    }

    /**
     * 打印Log，tag默认为类的名字
     */
    protected void log(String text) {
        log(this.getClass().getName(), text);
    }

    /**
     * 打印Log
     */
    protected void log(String tag, String text) {
        Log.i(tag, text);
    }

    protected void startActivity(Class clazz) {
        startActivity(new Intent(getActivity(), clazz));
    }

    /**
     * 获取状态栏高度
     * @return
     */
    protected int getStateBar(){
        int result = 0;
        int resourceId = this.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = this.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
