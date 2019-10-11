package com.ysxsoft.fragranceofhoney.widget;

import android.content.Context;
import android.support.annotation.NonNull;

import com.ysxsoft.fragranceofhoney.R;


public class LongDialog extends ABSDialog {
    public LongDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.long_delete_dialog_layout;
    }
}
