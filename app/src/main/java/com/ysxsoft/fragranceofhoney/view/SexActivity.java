package com.ysxsoft.fragranceofhoney.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ysxsoft.fragranceofhoney.R;
import com.ysxsoft.fragranceofhoney.impservice.ImpService;
import com.ysxsoft.fragranceofhoney.modle.ModifySexBean;
import com.ysxsoft.fragranceofhoney.utils.BaseActivity;
import com.ysxsoft.fragranceofhoney.utils.NetWork;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SexActivity extends BaseActivity implements View.OnClickListener {

    private View img_back;
    private LinearLayout ll_man, ll_woman;
    private ImageView tv_man, tv_woman;
    private int girl = 2;
    private Button btn_submit;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sex_layout);
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        initView();
        initListener();
    }

    private void initView() {
        img_back = getViewById(R.id.img_back);
        TextView tv_title = getViewById(R.id.tv_title);
        tv_title.setText("性别");
        ll_man = getViewById(R.id.ll_man);
        ll_woman = getViewById(R.id.ll_woman);
        tv_man = getViewById(R.id.tv_man);
        tv_woman = getViewById(R.id.tv_woman);
        btn_submit = getViewById(R.id.btn_submit);

    }

    private void initListener() {
        img_back.setOnClickListener(this);
        ll_man.setOnClickListener(this);
        ll_woman.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.ll_woman:
                girl = 2;
                tv_woman.setVisibility(View.VISIBLE);
                tv_man.setVisibility(View.INVISIBLE);
                break;
            case R.id.ll_man:
                girl = 1;
                tv_man.setVisibility(View.VISIBLE);
                tv_woman.setVisibility(View.INVISIBLE);
                break;
            case R.id.btn_submit:
                submitData();
                break;

        }
    }

    private void submitData() {
        NetWork.getRetrofit()
                .create(ImpService.class)
                .ModifySex(uid, String.valueOf(girl))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ModifySexBean>() {
                    private ModifySexBean modifySexBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(modifySexBean.getMsg());
                        if ("0".equals(modifySexBean.getCode())) {
                            Intent intent = new Intent("MODIFY_NAME_SEX");
                            intent.putExtra("type",String.valueOf(girl));
                            sendBroadcast(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(ModifySexBean modifySexBean) {
                        this.modifySexBean = modifySexBean;
                    }
                });
    }

}
