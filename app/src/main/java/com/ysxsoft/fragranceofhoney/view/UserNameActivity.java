package com.ysxsoft.fragranceofhoney.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ysxsoft.fragranceofhoney.R;
import com.ysxsoft.fragranceofhoney.impservice.ImpService;
import com.ysxsoft.fragranceofhoney.modle.ModifyUserNameBean;
import com.ysxsoft.fragranceofhoney.utils.BaseActivity;
import com.ysxsoft.fragranceofhoney.utils.NetWork;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserNameActivity extends BaseActivity implements View.OnClickListener {

    private View img_back;
    private ImageView img_delete;
    private EditText ed_nikeName;
    private Button btn_submit;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_name_layout);
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        initView();
        initListener();
    }

    private void initView() {
        img_back = getViewById(R.id.img_back);
        TextView tv_title = getViewById(R.id.tv_title);
        tv_title.setText("用户名");
        img_delete = getViewById(R.id.img_delete);
        ed_nikeName = getViewById(R.id.ed_nikeName);
        btn_submit = getViewById(R.id.btn_submit);
    }

    private void initListener() {
        img_back.setOnClickListener(this);
        img_delete.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_delete:
                ed_nikeName.setText("");
                break;
            case R.id.btn_submit:
                if (TextUtils.isEmpty(ed_nikeName.getText().toString().trim())) {
                    showToastMessage("昵称不能为空");
                    return;
                }
                submitData();
                break;
        }
    }

    private void submitData() {
        NetWork.getRetrofit()
                .create(ImpService.class)
                .ModifyUserName(uid, ed_nikeName.getText().toString().trim())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ModifyUserNameBean>() {
                    private ModifyUserNameBean modifyUserNameBean;
                    @Override
                    public void onCompleted() {
                        showToastMessage(modifyUserNameBean.getMsg());
                        if ("0".equals(modifyUserNameBean.getCode())) {
                            Intent intent=new Intent("MODIFY_NAME_SEX");
                            intent.putExtra("type",ed_nikeName.getText().toString().trim());
                            sendBroadcast(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(ModifyUserNameBean modifyUserNameBean) {
                        this.modifyUserNameBean = modifyUserNameBean;
                    }
                });
    }
}
