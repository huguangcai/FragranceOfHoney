package com.ysxsoft.fragranceofhoney.widget;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.ysxsoft.fragranceofhoney.R;
import com.ysxsoft.fragranceofhoney.utils.ActivityPageManager;
import com.ysxsoft.fragranceofhoney.view.LoginActivity;

public class LoginOutDilaog extends ABSDialog {
    private Context context;

    public LoginOutDilaog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void initView() {
        TextView tv_cancle = getViewById(R.id.tv_cancle);
        TextView tv_login_out = getViewById(R.id.tv_login_out);
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tv_login_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityPageManager instance = ActivityPageManager.getInstance();
                instance.finishAllActivity();
                context.startActivity(new Intent(context,LoginActivity.class));
                dismiss();
            }
        });

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.login_out_dialog_layout;
    }
}
