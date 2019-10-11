package com.ysxsoft.fragranceofhoney.widget;


import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.ysxsoft.fragranceofhoney.R;

public abstract class InPutAbsDialog extends Dialog {

    public InPutAbsDialog(@NonNull Context context) {
        super(context, R.style.Inputdialog);
        setContentView(getLayoutResId());
        initView();
    }

    /**
     * 根据Id获取View
     */
    @SuppressWarnings("unchecked")
    protected <T extends View> T getViewById(int id) {
        return (T) findViewById(id);
    }

    protected abstract void initView();

    protected abstract int getLayoutResId();
}
