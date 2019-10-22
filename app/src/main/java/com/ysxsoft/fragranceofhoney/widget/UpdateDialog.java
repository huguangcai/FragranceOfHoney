package com.ysxsoft.fragranceofhoney.widget;

import android.content.Context;
import android.support.annotation.NonNull;

import com.ysxsoft.fragranceofhoney.R;

/**
 * Create By 胡
 * on 2019/10/11 0011
 */
public class UpdateDialog extends ABSDialog {
    public UpdateDialog(@NonNull Context context) {
        super(context);
        this.setCancelable(false);
        this.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.update_dialog_layout;
    }
}
