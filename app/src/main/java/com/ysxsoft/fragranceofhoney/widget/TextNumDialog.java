package com.ysxsoft.fragranceofhoney.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.ysxsoft.fragranceofhoney.R;

public class TextNumDialog extends ABSDialog {

    public TextNumDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void initView() {
        getViewById(R.id.tv_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.text_num_dialog_layout;
    }
}
