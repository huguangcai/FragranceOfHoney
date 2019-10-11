package com.ysxsoft.fragranceofhoney.widget;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.ysxsoft.fragranceofhoney.R;

public class PayPwdDilaog extends InPutAbsDialog {
    private Context context;

    public PayPwdDilaog(@NonNull Context context) {
        super(context);
        this.context = context;
        setCanceledOnTouchOutside(false);//点击空白处  不消失
        setCancelable(false);
    }

    @Override
    protected void initView() {
//        PayPwdEditText ed_ppet = getViewById(R.id.ed_ppet);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.pay_pwd_dialog_layout;
    }
}
