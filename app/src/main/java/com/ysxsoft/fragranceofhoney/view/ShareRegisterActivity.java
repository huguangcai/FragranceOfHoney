package com.ysxsoft.fragranceofhoney.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.ysxsoft.fragranceofhoney.R;
import com.ysxsoft.fragranceofhoney.utils.AppUtil;
import com.ysxsoft.fragranceofhoney.utils.BaseActivity;
import com.ysxsoft.fragranceofhoney.utils.CountDownTimeHelper;

public class ShareRegisterActivity extends BaseActivity implements View.OnClickListener{

    private EditText ed_phone, ed_pwd, ed_idenfy_code, ed_invatation_code;
    private TextView tv_get_idenfy_code, tv_register_agreement;
    private CheckBox cb_box;
    private Button btn_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_register_layout);
        setHalfTransparent();
        setFitSystemWindow(false);
        initView();
        initListener();
    }

    private void initView() {
        ed_phone = getViewById(R.id.ed_phone);
        ed_idenfy_code = getViewById(R.id.ed_idenfy_code);
        tv_get_idenfy_code = getViewById(R.id.tv_get_idenfy_code);
        ed_invatation_code = getViewById(R.id.ed_invatation_code);
        ed_pwd = getViewById(R.id.ed_pwd);
        cb_box = getViewById(R.id.cb_box);
        tv_register_agreement = getViewById(R.id.tv_register_agreement);
        btn_login = getViewById(R.id.btn_login);
    }

    private void initListener() {
        tv_get_idenfy_code.setOnClickListener(this);
        cb_box.setOnClickListener(this);
        tv_register_agreement.setOnClickListener(this);
        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_get_idenfy_code:
                if (checkPhoneNum()) return;
                CountDownTimeHelper timeHelper = new CountDownTimeHelper(60, tv_get_idenfy_code);
                //TODO 发送短信
                sendMessage();
                break;
            case R.id.cb_box:

                break;
            case R.id.tv_register_agreement:
                startActivity(RegisterAgreementActivity.class);
                break;
            case R.id.tv_login:
                finish();
                break;
            case R.id.btn_login:
                if (checkPhoneNum()) return;
                if (TextUtils.isEmpty(ed_idenfy_code.getText().toString().trim())) {
                    showToastMessage("验证码不能为空");
                    return;
                }
                if (TextUtils.isEmpty(ed_pwd.getText().toString().trim())) {
                    showToastMessage("密码不能为空");
                    return;
                }
                if (!cb_box.isChecked()) {
                    showToastMessage("请同意注册协议");
                    return;
                }
                showToastMessage("注册");
                startActivity(ShareLoadActivity.class);
                break;

        }
    }
    /**
     * 核对手机号
     *
     * @return
     */
    private boolean checkPhoneNum() {
        if (TextUtils.isEmpty(ed_phone.getText().toString().trim())) {
            showToastMessage("手机号不能为空");
            return true;
        }
        if (!AppUtil.checkPhoneNum(ed_phone.getText().toString().trim())) {
            showToastMessage("手机号输入不正确");
            return true;
        }
        return false;
    }

    /**
     * 发送短信
     */
    private void sendMessage() {

    }
}
