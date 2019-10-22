package com.ysxsoft.fragranceofhoney.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ysxsoft.fragranceofhoney.R;
import com.ysxsoft.fragranceofhoney.impservice.ImpService;
import com.ysxsoft.fragranceofhoney.modle.CustomerPhoneNumBean;
import com.ysxsoft.fragranceofhoney.utils.AppUtil;
import com.ysxsoft.fragranceofhoney.utils.BaseActivity;
import com.ysxsoft.fragranceofhoney.utils.NetWork;
import com.ysxsoft.fragranceofhoney.widget.CustomerPhoneDialog;
import com.ysxsoft.fragranceofhoney.widget.LoginOutDilaog;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private View img_back;
    private LinearLayout ll_account_security, ll_about_platform, ll_customer_phone, ll_version_updata, ll_help_center, ll_login_out;
    private String uid;
    private String phone;
    private TextView tv_version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_setting_layout);
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        requestCustomerPhoneData();
        initView();
        initListener();
    }

    private void requestCustomerPhoneData() {
        NetWork.getRetrofit()
                .create(ImpService.class)
                .CustomerPhoneNum()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CustomerPhoneNumBean>() {
                    private CustomerPhoneNumBean customerPhoneNumBean;

                    @Override
                    public void onCompleted() {
                        if ("0".equals(customerPhoneNumBean.getCode())) {
                            phone = customerPhoneNumBean.getData().getPhone();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(CustomerPhoneNumBean customerPhoneNumBean) {
                        this.customerPhoneNumBean = customerPhoneNumBean;
                    }
                });
    }

    private void initView() {
        img_back = getViewById(R.id.img_back);
        TextView tv_title = getViewById(R.id.tv_title);
        tv_title.setText("设置");
        tv_version = getViewById(R.id.tv_version);
        ll_account_security = getViewById(R.id.ll_account_security);
        ll_about_platform = getViewById(R.id.ll_about_platform);
        ll_customer_phone = getViewById(R.id.ll_customer_phone);
        ll_version_updata = getViewById(R.id.ll_version_updata);
        ll_help_center = getViewById(R.id.ll_help_center);
        ll_login_out = getViewById(R.id.ll_login_out);
        tv_version.setText("版本"+AppUtil.getVersionName(mContext));

    }

    private void initListener() {
        img_back.setOnClickListener(this);
        ll_account_security.setOnClickListener(this);
        ll_about_platform.setOnClickListener(this);
        ll_customer_phone.setOnClickListener(this);
        ll_version_updata.setOnClickListener(this);
        ll_help_center.setOnClickListener(this);
        ll_login_out.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.ll_account_security://账户安全
                Intent intent = new Intent(mContext, AccountSecurityActivity.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
                break;

            case R.id.ll_about_platform://关于平台
                startActivity(AboutPlatformActivity.class);
                break;

            case R.id.ll_customer_phone://客户电话
                final CustomerPhoneDialog dialog = new CustomerPhoneDialog(mContext);
                final TextView tv_phone_num = dialog.findViewById(R.id.tv_phone_num);
                tv_phone_num.setText(phone);
                TextView tv_call_phone = dialog.findViewById(R.id.tv_call_phone);
                tv_call_phone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        Uri data = Uri.parse("tel:" + tv_phone_num.getText().toString());
                        intent.setData(data);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;

            case R.id.ll_version_updata://版本更新
                startActivity(VersionUpdateActivity.class);
                break;

            case R.id.ll_help_center://帮助中心
                startActivity(HelpCenterActivity.class);
                break;

            case R.id.ll_login_out://退出登陆
                LoginOutDilaog loginOutDilaog = new LoginOutDilaog(mContext);
                loginOutDilaog.show();

                SharedPreferences.Editor is_first = getSharedPreferences("IS_FIRST", Context.MODE_PRIVATE).edit();
                is_first.clear();
                is_first.commit();
                SharedPreferences.Editor save_pwd = getSharedPreferences("SAVE_PWD", Context.MODE_PRIVATE).edit();
                save_pwd.clear();
                save_pwd.commit();
                SharedPreferences.Editor spUid = getSharedPreferences("UID", Context.MODE_PRIVATE).edit();
                spUid.clear();
                spUid.commit();
                break;
        }
    }
}
