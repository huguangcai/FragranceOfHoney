package com.ysxsoft.fragranceofhoney.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ysxsoft.fragranceofhoney.MainActivity;
import com.ysxsoft.fragranceofhoney.R;
import com.ysxsoft.fragranceofhoney.impservice.ImpService;
import com.ysxsoft.fragranceofhoney.modle.LauncherImgBean;
import com.ysxsoft.fragranceofhoney.utils.BaseActivity;
import com.ysxsoft.fragranceofhoney.utils.BaseApplication;
import com.ysxsoft.fragranceofhoney.utils.NetWork;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LauncherActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout ll_jump;
    private TextView tv_time;
    private boolean is_first;
    private CountDownTimer countDownTimer;
    private ImageView img_launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.laucher_layout);
        SharedPreferences sp = getSharedPreferences("IS_FIRST", Context.MODE_PRIVATE);
        is_first = sp.getBoolean("is_first", false);
        initView();
        requestData();
        initListener();
    }

    private void initView() {
        ll_jump = getViewById(R.id.ll_jump);
        tv_time = getViewById(R.id.tv_time);
        img_launcher = getViewById(R.id.img_launcher);
        countDownTimer = new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tv_time.setText("(" + String.valueOf(millisUntilFinished / 1000) + "s)");
            }

            @Override
            public void onFinish() {
                tv_time.setText("(0s)");
//                if (is_first) {
                startActivity(MainActivity.class);
                finish();
//                } else {
//                    startActivity(LoginActivity.class);
//                    finish();
//                }
            }
        }.start();
    }

    /**
     * 获取启动图
     */
    private void requestData() {
        NetWork.getRetrofit()
                .create(ImpService.class)
                .LauncherImg()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LauncherImgBean>() {
                    private LauncherImgBean launcherImgBean;

                    @Override
                    public void onCompleted() {
                        if ("0".equals(launcherImgBean.getCode())){
                            Glide.with(mContext).load(launcherImgBean.getData().getImgurl()).into(img_launcher);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(LauncherImgBean launcherImgBean) {
                        this.launcherImgBean = launcherImgBean;
                    }
                });
    }

    private void initListener() {
        ll_jump.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_jump:
//                if (is_first) {
                startActivity(MainActivity.class);
                finish();
//                } else {
//                    startActivity(LoginActivity.class);
//                    finish();
//                }
                countDownTimer.cancel();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
    }
}
