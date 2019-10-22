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
import com.ysxsoft.fragranceofhoney.modle.CheckCodeBean;
import com.ysxsoft.fragranceofhoney.utils.AppUtil;
import com.ysxsoft.fragranceofhoney.utils.BaseActivity;
import com.ysxsoft.fragranceofhoney.utils.CountDownTimeHelper;
import com.ysxsoft.fragranceofhoney.utils.NetWork;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ModifyTradePwdActivity extends BaseActivity implements View.OnClickListener {

    private View img_back;
    private TextView tv_phone_before, tv_phone_after, tv_get_identify_code;
    private Button btn_submit;
    private EditText ed_identify_code;
    private String uid;
    private String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_trade_pwd_layout);
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        mobile = intent.getStringExtra("mobile");
        initView();
        initListener();
    }

    private void initView() {
        img_back = getViewById(R.id.img_back);
        TextView tv_title = getViewById(R.id.tv_title);
        tv_title.setText("修改交易密码");
        tv_phone_before = getViewById(R.id.tv_phone_before);
        tv_phone_after = getViewById(R.id.tv_phone_after);
        ed_identify_code = getViewById(R.id.ed_identify_code);
        tv_get_identify_code = getViewById(R.id.tv_get_identify_code);
        btn_submit = getViewById(R.id.btn_submit);
        if (!TextUtils.isEmpty(mobile)){
            tv_phone_before.setText(AppUtil.subBefore3Num(mobile,3));
            tv_phone_after.setText(AppUtil.subAfter4Num(mobile,4));
        }
    }

    private void initListener() {
        img_back.setOnClickListener(this);
        tv_get_identify_code.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_get_identify_code:
                CountDownTimeHelper timeHelper=new CountDownTimeHelper(60,tv_get_identify_code);
                if (!TextUtils.isEmpty(mobile)){
                 sendMessage(mobile,"2");
                }else {
                    showToastMessage("获取错误");
                }
                break;
            case R.id.btn_submit:
                if (TextUtils.isEmpty(ed_identify_code.getText().toString().trim())){
                     showToastMessage("验证码不能为空");
                    return;
                }
                submitData();
                break;
        }
    }

    private void submitData() {
        NetWork.getService(ImpService.class)
                .CheckCodeData(mobile,ed_identify_code.getText().toString().trim())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CheckCodeBean>() {
                    private CheckCodeBean checkCodeBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(checkCodeBean.getMsg());
                        if ("0".equals(checkCodeBean.getCode())){
                            Intent intent=new Intent(mContext,TradePwdActivity.class);
                            intent.putExtra("modify_pwd","modify_pwd");
                            intent.putExtra("uid", uid);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(CheckCodeBean checkCodeBean) {

                        this.checkCodeBean = checkCodeBean;
                    }
                });
    }
}
