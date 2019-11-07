package com.ysxsoft.fragranceofhoney.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ysxsoft.fragranceofhoney.R;
import com.ysxsoft.fragranceofhoney.impservice.ImpService;
import com.ysxsoft.fragranceofhoney.modle.ModifyTradePwdBean;
import com.ysxsoft.fragranceofhoney.utils.BaseActivity;
import com.ysxsoft.fragranceofhoney.utils.NetWork;
import com.ysxsoft.fragranceofhoney.widget.PayPwdEditText;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TradePwdActivity extends BaseActivity {

    private PayPwdEditText ed_ppet;
    private String pwd;
    private String modify_pwd;
    private TextView tv_setting_pwd;
    private String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trade_pwd_layout);
        Intent intent = getIntent();
        modify_pwd = intent.getStringExtra("modify_pwd");
        uid = intent.getStringExtra("uid");
        initView();
    }

    private void initView() {
        View img_back = getViewById(R.id.img_back);
        TextView tv_title = getViewById(R.id.tv_title);
        tv_title.setText("交易密码");
        final Button btn_submit = getViewById(R.id.btn_submit);
        ed_ppet = getViewById(R.id.ed_ppet);
        tv_setting_pwd = getViewById(R.id.tv_setting_pwd);

        if ("modify_pwd".equals(modify_pwd)) {
            tv_setting_pwd.setText("修改密码");
        } else {
            tv_setting_pwd.setText("设置密码");
        }
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ed_ppet.initStyle(R.drawable.edit_num_bg_red, 6, 0.33f, R.color.black, R.color.black, 20);
        ed_ppet.setFocus();
        ed_ppet.setOnTextFinishListener(new PayPwdEditText.OnTextFinishListener() {
            @Override
            public void onFinish(String str) {
                pwd = str;
            }
        });
        ed_ppet.setOnChangeListener(new PayPwdEditText.OnChangeListener() {
            @Override
            public void change(int length) {
                if (length < 6) {
                    btn_submit.setVisibility(View.GONE);
                } else {
                    btn_submit.setVisibility(View.VISIBLE);
                }
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitData();
            }
        });
    }

    private void submitData() {
        NetWork.getRetrofit()
                .create(ImpService.class)
                .ModifyTradePwd(uid, pwd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ModifyTradePwdBean>() {
                    private ModifyTradePwdBean modifyTradePwdBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(modifyTradePwdBean.getMsg());
                        if ("0".equals(modifyTradePwdBean.getCode())) {
                            Intent intent = new Intent("FINISH");
                            sendBroadcast(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(ModifyTradePwdBean modifyTradePwdBean) {
                        this.modifyTradePwdBean = modifyTradePwdBean;
                    }
                });
    }
}
