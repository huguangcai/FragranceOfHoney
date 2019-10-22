package com.ysxsoft.fragranceofhoney.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ysxsoft.fragranceofhoney.MainActivity;
import com.ysxsoft.fragranceofhoney.R;
import com.ysxsoft.fragranceofhoney.impservice.ImpService;
import com.ysxsoft.fragranceofhoney.modle.BindingPhoneNumBean;
import com.ysxsoft.fragranceofhoney.modle.LoginDataBean;
import com.ysxsoft.fragranceofhoney.modle.SendMessageBean;
import com.ysxsoft.fragranceofhoney.utils.AppUtil;
import com.ysxsoft.fragranceofhoney.utils.BaseActivity;
import com.ysxsoft.fragranceofhoney.utils.CountDownTimeHelper;
import com.ysxsoft.fragranceofhoney.utils.NetWork;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BindingPhoneNumActivity extends BaseActivity implements View.OnClickListener {

    private View img_back;
    private EditText ed_phone, ed_idenfy_code, ed_invatation_code, ed_pwd;
    private TextView tv_get_idenfy_code;
    private Button btn_submit;
    private String uid;
    private String type, openidstr;
    private LinearLayout ll_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.binding_phone_layout);
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        type = intent.getStringExtra("type");
        openidstr = intent.getStringExtra("openid");
        initView();
        initListener();
    }

    private void initView() {
        img_back = getViewById(R.id.img_back);
        ll_pwd = getViewById(R.id.ll_pwd);
        TextView tv_title = getViewById(R.id.tv_title);
        tv_title.setText("绑定手机号");
        ed_phone = getViewById(R.id.ed_phone);
        ed_idenfy_code = getViewById(R.id.ed_idenfy_code);
        tv_get_idenfy_code = getViewById(R.id.tv_get_idenfy_code);
        ed_invatation_code = getViewById(R.id.ed_invatation_code);
        ed_pwd = getViewById(R.id.ed_pwd);
        btn_submit = getViewById(R.id.btn_submit);
    }

    private void initListener() {
        img_back.setOnClickListener(this);
        tv_get_idenfy_code.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }

    private SendMessageBean MessageBean;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_get_idenfy_code:
                if (CheckPhoneNum()) return;
                CountDownTimeHelper countDownTimeHelper = new CountDownTimeHelper(60, tv_get_idenfy_code);
//                String s = sendMessage(ed_phone.getText().toString().trim(), "3");
                NetWork.getRetrofit()
                        .create(ImpService.class)
                        .sendMessage(ed_phone.getText().toString().trim(), "3")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<SendMessageBean>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                showToastMessage(e.getMessage());
                            }

                            @Override
                            public void onNext(SendMessageBean sendMessageBean) {
                                MessageBean = sendMessageBean;
                                switch (MessageBean.getData().getPassword()) {
                                    case 1:
                                        ll_pwd.setVisibility(View.GONE);
                                        break;
                                    case 2:
                                        ll_pwd.setVisibility(View.VISIBLE);
                                        break;
                                }
                            }
                        });
                break;
            case R.id.btn_submit:
                if (CheckPhoneNum()) return;

                if (TextUtils.isEmpty(ed_idenfy_code.getText().toString().trim())) {
                    showToastMessage("验证码不能为空");
                    return;
                }
                switch (MessageBean.getData().getPassword()) {
                    case 1:
                        ll_pwd.setVisibility(View.GONE);
                        break;
                    case 2:
                        ll_pwd.setVisibility(View.VISIBLE);
                        if (TextUtils.isEmpty(ed_pwd.getText().toString().trim())) {
                            showToastMessage("密码不能为空");
                            return;
                        }
                        break;
                }
                submitData();
                break;
        }
    }

    private void submitData() {
        NetWork.getService(ImpService.class)
                .LoginPhoneNum(type,
                        openidstr,
                        ed_phone.getText().toString().trim(),
                        ed_pwd.getText().toString().trim(),
                        ed_idenfy_code.getText().toString().trim())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginDataBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(LoginDataBean loginDataBean) {
                        showToastMessage(loginDataBean.getMsg());
                        if (loginDataBean.getCode() == 0) {
                            SharedPreferences.Editor save_pwd = getSharedPreferences("SAVE_PWD", Context.MODE_PRIVATE).edit();
                            save_pwd.putString("Phone", ed_phone.getText().toString().trim());
                            save_pwd.putString("pwd", ed_pwd.getText().toString().trim());
                            save_pwd.commit();
                            SharedPreferences.Editor spUid = getSharedPreferences("UID", Context.MODE_PRIVATE).edit();
                            spUid.putString("uid", String.valueOf(loginDataBean.getData()));
                            spUid.commit();
                            startActivity(MainActivity.class);
                            finish();
                        }
                    }
                });


//        NetWork.getRetrofit()
//                .create(ImpService.class)
//                .BindingPhoneNum(ed_phone.getText().toString().trim(),
//                        ed_idenfy_code.getText().toString().trim(),
//                        ed_pwd.getText().toString().trim(),
//                        uid,
//                        ed_invatation_code.getText().toString().trim())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<BindingPhoneNumBean>() {
//                    private BindingPhoneNumBean bindingPhoneNumBean;
//
//                    @Override
//                    public void onCompleted() {
//                        showToastMessage(bindingPhoneNumBean.getMsg());
//                        if ("0".equals(bindingPhoneNumBean.getCode())) {
//                            SharedPreferences.Editor save_pwd = getSharedPreferences("SAVE_PWD", Context.MODE_PRIVATE).edit();
//                            save_pwd.putString("Phone", ed_phone.getText().toString().trim());
//                            save_pwd.putString("pwd", ed_pwd.getText().toString().trim());
//                            save_pwd.commit();
//                            SharedPreferences.Editor spUid = getSharedPreferences("UID", Context.MODE_PRIVATE).edit();
//                            spUid.putString("uid", uid);
//                            spUid.commit();
//                            startActivity(MainActivity.class);
//                            finish();
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        showToastMessage(e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(BindingPhoneNumBean bindingPhoneNumBean) {
//                        this.bindingPhoneNumBean = bindingPhoneNumBean;
//                    }
//                });
//

    }

    private boolean CheckPhoneNum() {
        if (TextUtils.isEmpty(ed_phone.getText().toString().trim())) {
            showToastMessage("手机号不能为空");
            return true;
        }
        if (!AppUtil.checkPhoneNum(ed_phone.getText().toString().trim())) {
            showToastMessage("请输入正确的手机号");
            return true;
        }
        return false;
    }

}
