package com.ysxsoft.fragranceofhoney.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.ysxsoft.fragranceofhoney.R;

public class CustomerPhoneDialog extends ABSDialog {
    public CustomerPhoneDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void initView() {
        ImageView img_close = getViewById(R.id.img_close);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.customer_phone_dialog_layout;
    }
}
