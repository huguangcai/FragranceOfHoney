package com.ysxsoft.fragranceofhoney.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ysxsoft.fragranceofhoney.R;
import com.ysxsoft.fragranceofhoney.impservice.ImpService;
import com.ysxsoft.fragranceofhoney.modle.AcountSafeBean;
import com.ysxsoft.fragranceofhoney.utils.AppUtil;
import com.ysxsoft.fragranceofhoney.utils.BaseActivity;
import com.ysxsoft.fragranceofhoney.utils.NetWork;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AccountSecurityActivity extends BaseActivity implements View.OnClickListener {

    private View img_back;
    private LinearLayout ll_trade_pwd, ll_modify_phone_num, ll_modify_login_pwd;
    private TextView tv_phone_before, tv_phone_after;
    private String uid;
    private String tradepassword;
    private String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_account_security_layout);
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        initView();
        requestData();
        initListener();
    }

    private void requestData() {
        NetWork.getRetrofit()
                .create(ImpService.class)
                .AcountSafe(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AcountSafeBean>() {
                    private AcountSafeBean acountSafeBean;

                    @Override
                    public void onCompleted() {
                        if ("0".equals(acountSafeBean.getCode())) {
                            mobile = acountSafeBean.getData().getMobile();
                            tv_phone_before.setText(AppUtil.subBefore3Num(mobile, 3));
                            tv_phone_after.setText(AppUtil.subAfter4Num(mobile, 4));
                            tradepassword = acountSafeBean.getData().getTradepassword();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(AcountSafeBean acountSafeBean) {
                        this.acountSafeBean = acountSafeBean;
                    }
                });
    }

    private void initView() {
        img_back = getViewById(R.id.img_back);
        TextView tv_title = getViewById(R.id.tv_title);
        tv_title.setText("账户安全");
        ll_trade_pwd = getViewById(R.id.ll_trade_pwd);
        ll_modify_phone_num = getViewById(R.id.ll_modify_phone_num);
        ll_modify_login_pwd = getViewById(R.id.ll_modify_login_pwd);
        tv_phone_before = getViewById(R.id.tv_phone_before);
        tv_phone_after = getViewById(R.id.tv_phone_after);
    }

    private void initListener() {
        img_back.setOnClickListener(this);
        ll_trade_pwd.setOnClickListener(this);
        ll_modify_phone_num.setOnClickListener(this);
        ll_modify_login_pwd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.ll_trade_pwd:
                if ("1".equals(tradepassword)) {//tradepassword	1是设置过支付密码2是没有设置过
                    Intent intent = new Intent(mContext, ModifyTradePwdActivity.class);
                    intent.putExtra("uid", uid);
                    intent.putExtra("modify_pwd", "modify_pwd");
                    intent.putExtra("mobile", mobile);
                    startActivity(intent);
                } else {
//                    Intent intent = new Intent(mContext, TradePwdActivity.class);
                    Intent intent = new Intent(mContext, ModifyTradePwdActivity.class);
                    intent.putExtra("uid", uid);
                    intent.putExtra("modify_pwd", "");
                    intent.putExtra("mobile", mobile);
                    startActivity(intent);
                }
                break;
            case R.id.ll_modify_phone_num:
                Intent intent1 = new Intent(mContext, ModifyPhoneNumActivity.class);
                intent1.putExtra("uid", uid);
                startActivity(intent1);
                break;
            case R.id.ll_modify_login_pwd:
                Intent modifyintent = new Intent(mContext, ModifyLoginPwdActivity.class);
                modifyintent.putExtra("mobile", mobile);
                startActivity(modifyintent);
                break;
        }
    }
}
