package com.ysxsoft.fragranceofhoney.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ysxsoft.fragranceofhoney.R;
import com.ysxsoft.fragranceofhoney.impservice.ImpService;
import com.ysxsoft.fragranceofhoney.modle.ForgetModifyPwdBean;
import com.ysxsoft.fragranceofhoney.utils.AppUtil;
import com.ysxsoft.fragranceofhoney.utils.BaseActivity;
import com.ysxsoft.fragranceofhoney.utils.CountDownTimeHelper;
import com.ysxsoft.fragranceofhoney.utils.NetWork;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ModifyLoginPwdActivity extends BaseActivity implements View.OnClickListener {

    private View img_back;
    private EditText ed_phone, ed_idenfy_code, ed_new_pwd, ed_second_pwd;
    private TextView tv_get_idenfy_code;
    private Button btn_submit;
    private String forgetPwd;
    private String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_login_pwd_layout);
        Intent intent = getIntent();
        forgetPwd = intent.getStringExtra("forgetPwd");
        mobile = intent.getStringExtra("mobile");
        initView();
        initListener();
    }

    private void initView() {
        img_back = getViewById(R.id.img_back);
        TextView tv_title = getViewById(R.id.tv_title);
        if ("forgetPwd".equals(forgetPwd)) {
            tv_title.setText("忘记登录密码");
        } else {
            tv_title.setText("修改登录密码");
        }
        ed_phone = getViewById(R.id.ed_phone);
        ed_idenfy_code = getViewById(R.id.ed_idenfy_code);
        tv_get_idenfy_code = getViewById(R.id.tv_get_idenfy_code);
        ed_new_pwd = getViewById(R.id.ed_new_pwd);
        ed_second_pwd = getViewById(R.id.ed_second_pwd);
        btn_submit = getViewById(R.id.btn_submit);
        if (mobile!=null&&!"".equals(mobile)){
            ed_phone.setText(mobile);
        }
    }

    private void initListener() {
        img_back.setOnClickListener(this);
        tv_get_idenfy_code.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_get_idenfy_code:
                if (CheckPhoneNum()) return;
                CountDownTimeHelper countDownTimeHelper = new CountDownTimeHelper(60, tv_get_idenfy_code);
                String s = sendMessage(ed_phone.getText().toString().trim());
                break;
            case R.id.btn_submit:
                if (CheckPhoneNum()) return;
                if (TextUtils.isEmpty(ed_idenfy_code.getText().toString().trim())) {
                    showToastMessage("验证码不能为空");
                    return;
                }
                if (TextUtils.isEmpty(ed_new_pwd.getText().toString().trim())) {
                    showToastMessage("新密码不能为空");
                    return;
                }
                if (ed_new_pwd.getText().toString().trim().length() < 6) {
                    showToastMessage("密码不能少于六位");
                    return ;
                }
                if (TextUtils.isEmpty(ed_second_pwd.getText().toString().trim())) {
                    showToastMessage("再次输入的新密码不能为空");
                    return;
                }
                if (!ed_new_pwd.getText().toString().trim().equals(ed_second_pwd.getText().toString().trim())) {
                    showToastMessage("两次输入的密码不一致");
                    return;
                }
                submitData();
                break;
        }
    }

    private void submitData() {
        NetWork.getRetrofit()
                .create(ImpService.class)
                .ForgetModifyPwd(ed_phone.getText().toString().trim(),
                        ed_new_pwd.getText().toString().trim(),
                        ed_idenfy_code.getText().toString().trim())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ForgetModifyPwdBean>() {
                    private ForgetModifyPwdBean forgetModifyPwdBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(forgetModifyPwdBean.getMsg());
                        if ("0".equals(forgetModifyPwdBean.getCode())) {
                            finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(ForgetModifyPwdBean forgetModifyPwdBean) {
                        this.forgetModifyPwdBean = forgetModifyPwdBean;
                    }
                });

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
