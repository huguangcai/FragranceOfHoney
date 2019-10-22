package com.ysxsoft.fragranceofhoney.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.utils.SocializeUtils;
import com.ysxsoft.fragranceofhoney.MainActivity;
import com.ysxsoft.fragranceofhoney.R;
import com.ysxsoft.fragranceofhoney.impservice.ImpService;
import com.ysxsoft.fragranceofhoney.modle.LoginBean;
import com.ysxsoft.fragranceofhoney.modle.LoginDataBean;
import com.ysxsoft.fragranceofhoney.modle.QQLoginBean;
import com.ysxsoft.fragranceofhoney.utils.AppUtil;
import com.ysxsoft.fragranceofhoney.utils.BaseActivity;
import com.ysxsoft.fragranceofhoney.utils.NetWork;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText ed_phone, ed_pwd;
    private TextView tv_register, tv_forget_pwd;
    private ImageView img_wechat, img_QQ;
    private Button btn_login;
    private ProgressDialog dialog;
    public int type = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        setHalfTransparent();
        setFitSystemWindow(false);
//        if (Build.VERSION.SDK_INT >= 23) {
//            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.CALL_PHONE,
//                    Manifest.permission.READ_LOGS,
//                    Manifest.permission.READ_PHONE_STATE,
//                    Manifest.permission.READ_EXTERNAL_STORAGE,
//                    Manifest.permission.SET_DEBUG_APP,
//                    Manifest.permission.SYSTEM_ALERT_WINDOW,
//                    Manifest.permission.GET_ACCOUNTS,
//                    Manifest.permission.WRITE_APN_SETTINGS};
//            ActivityCompat.requestPermissions(this, mPermissionList, 123);
//        }
        initView();
        initListener();
    }

    private void initView() {
        dialog = new ProgressDialog(this);
        ed_phone = getViewById(R.id.ed_phone);
        ed_pwd = getViewById(R.id.ed_pwd);
        tv_register = getViewById(R.id.tv_register);
        tv_forget_pwd = getViewById(R.id.tv_forget_pwd);
        btn_login = getViewById(R.id.btn_login);
        img_wechat = getViewById(R.id.img_wechat);
        img_QQ = getViewById(R.id.img_QQ);
    }

    private void initListener() {
        tv_register.setOnClickListener(this);
        tv_forget_pwd.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        img_wechat.setOnClickListener(this);
        img_QQ.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_register:
                startActivity(RegisterActivity.class);
                break;
            case R.id.tv_forget_pwd:
                Intent intent = new Intent(mContext, ModifyLoginPwdActivity.class);
                intent.putExtra("forgetPwd", "forgetPwd");
                startActivity(intent);
                break;
            case R.id.btn_login:
                if (checkData()) return;
                submitData();
                break;
            case R.id.img_wechat:
                UMQQLoglin(SHARE_MEDIA.WEIXIN);
                type = 1;
                break;
            case R.id.img_QQ:
                UMQQLoglin(SHARE_MEDIA.QQ);
                type = 2;
                break;
        }
    }

    /**
     * 登录提交数据
     */
    private void submitData() {
        NetWork.getRetrofit()
                .create(ImpService.class)
                .Login(ed_phone.getText().toString().trim(),
                        ed_pwd.getText().toString().trim())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginBean>() {
                    private LoginBean loginBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(loginBean.getMsg());
                        if ("0".equals(loginBean.getCode())) {
                            SharedPreferences.Editor edit = getSharedPreferences("IS_FIRST", Context.MODE_PRIVATE).edit();
                            edit.putBoolean("is_first", true);
                            edit.commit();
                            SharedPreferences.Editor save_pwd = getSharedPreferences("SAVE_PWD", Context.MODE_PRIVATE).edit();
                            save_pwd.putString("Phone", ed_phone.getText().toString().trim());
                            save_pwd.putString("pwd", ed_pwd.getText().toString().trim());
                            save_pwd.commit();

                            String uid = loginBean.getUserinfo().getUid();
                            SharedPreferences.Editor spUid = getSharedPreferences("UID", Context.MODE_PRIVATE).edit();
                            spUid.putString("uid", uid);
                            spUid.commit();
                            NetWork.setUid(uid);
                            Intent intent = new Intent(mContext, MainActivity.class);
                            intent.putExtra("uid", uid);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(LoginBean loginBean) {
                        this.loginBean = loginBean;
                    }
                });


    }

    /**
     * 友盟第三方登录
     *
     * @param share_media
     */
    private void UMQQLoglin(SHARE_MEDIA share_media) {
        boolean authorize = UMShareAPI.get(mContext).isAuthorize(LoginActivity.this, share_media);
        if (authorize) {
            UMShareAPI.get(mContext).deleteOauth(LoginActivity.this, share_media, authListener);
        } else {
            UMShareAPI.get(mContext).doOauthVerify(LoginActivity.this, share_media, authListener);
        }
    }

    UMAuthListener authListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            SocializeUtils.safeShowDialog(dialog);
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            SocializeUtils.safeCloseDialog(dialog);
            getInfo(platform);
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            SocializeUtils.safeCloseDialog(dialog);
            showToastMessage("失败：" + t.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            SocializeUtils.safeCloseDialog(dialog);
            showToastMessage("取消了");
        }

    };

    /**
     * 获取第三方登录用户信息
     *
     * @param shareMedia
     */
    private void getInfo(SHARE_MEDIA shareMedia) {
        boolean authorize = UMShareAPI.get(LoginActivity.this).isAuthorize(LoginActivity.this, shareMedia);
        UMShareAPI.get(LoginActivity.this).getPlatformInfo(LoginActivity.this, shareMedia, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                SocializeUtils.safeShowDialog(dialog);
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                SocializeUtils.safeCloseDialog(dialog);
                StringBuilder sb = new StringBuilder();
                for (String key : map.keySet()) {
                    sb.append(key).append(" : ").append(map.get(key)).append("\n");
                }
                JSONObject jsonObject = new JSONObject(map);
                try {
                    final String openid = jsonObject.getString("uid");
                    String nickname = jsonObject.getString("name");
                    String sex = jsonObject.getString("gender");
                    String avatar = jsonObject.getString("iconurl");
                    NetWork.getService(ImpService.class)
                            .LoginData(openid, String.valueOf(type))
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


                                    if (loginDataBean.getCode() == 205) {
                                        Intent intent = new Intent(mContext, BindingPhoneNumActivity.class);
//                                        intent.putExtra("uid", String.valueOf(uid));
                                        intent.putExtra("type", String.valueOf(type));
                                        intent.putExtra("openid", openid);
                                        startActivity(intent);
                                    } else {
                                        int uid = loginDataBean.getData();
                                        Intent intent = new Intent(mContext, MainActivity.class);
                                        intent.putExtra("uid", String.valueOf(uid));
                                        startActivity(intent);
                                        SharedPreferences.Editor spUid = getSharedPreferences("UID", Context.MODE_PRIVATE).edit();
                                        spUid.putString("uid", String.valueOf(uid));
                                        spUid.commit();
                                        finish();
                                    }
                                }
                            });

//                    NetWork.getService(ImpService.class)
//                            .QQLoginData(openid, nickname, avatar, sex)
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe(new Observer<QQLoginBean>() {
//                                private QQLoginBean qqLoginBean;
//
//                                @Override
//                                public void onCompleted() {
//
//                                }
//
//                                @Override
//                                public void onError(Throwable e) {
//                                    showToastMessage(e.getMessage());
//                                }
//
//                                @Override
//                                public void onNext(QQLoginBean qqLoginBean) {
//                                    this.qqLoginBean = qqLoginBean;
//                                }
//                            });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
//                result.setText("错误" + throwable.getMessage());
                showToastMessage("失败");
                SocializeUtils.safeCloseDialog(dialog);
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                SocializeUtils.safeCloseDialog(dialog);
                showToastMessage("用户已取消");
            }
        });


    }

    /**
     * 校验数据
     */
    private boolean checkData() {
        if (TextUtils.isEmpty(ed_phone.getText().toString().trim())) {
            showToastMessage("手机号不能为空");
            return true;
        }
        if (!AppUtil.checkPhoneNum(ed_phone.getText().toString().trim())) {
            showToastMessage("手机号输入不正确");
            return true;
        }
        if (TextUtils.isEmpty(ed_pwd.getText().toString().trim())) {
            showToastMessage("密码不能为空");
            return true;
        }
        if (ed_pwd.getText().toString().trim().length() < 6) {
            showToastMessage("密码不能少于六位");
            return true;
        }
        return false;
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        UMShareAPI.get(this).onSaveInstanceState(outState);
    }

}
