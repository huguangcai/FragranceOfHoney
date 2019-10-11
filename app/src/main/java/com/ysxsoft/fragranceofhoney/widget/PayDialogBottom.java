package com.ysxsoft.fragranceofhoney.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ysxsoft.fragranceofhoney.R;
import com.ysxsoft.fragranceofhoney.impservice.ImpService;
import com.ysxsoft.fragranceofhoney.modle.BalanceMoneyBean;
import com.ysxsoft.fragranceofhoney.utils.NetWork;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PayDialogBottom extends ABSDialog {


    public PayDialogBottom(@NonNull Context context) {
        super(context);
        setCanceledOnTouchOutside(false);//点击空白处  不消失
        setCancelable(false);
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);


    }

    @Override
    protected void initView() {
//        ImageView img_close = getViewById(R.id.img_close);
//        img_close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
        //需要支付金额
        TextView tv_pay_money = getViewById(R.id.tv_pay_money);

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.pay_dialog_layout;
    }
}
