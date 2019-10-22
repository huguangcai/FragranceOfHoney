package com.ysxsoft.fragranceofhoney.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gyf.immersionbar.ImmersionBar;
import com.ysxsoft.fragranceofhoney.impservice.ImpService;
import com.ysxsoft.fragranceofhoney.modle.SendMessageBean;
import com.ysxsoft.fragranceofhoney.view.LoginActivity;

import rx.Observer;
import rx.schedulers.Schedulers;
import rx.android.schedulers.AndroidSchedulers;

import static android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

/**
 * 描述： activity基类
 * 日期： 2018/10/23 0023 09:55
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class BaseActivity extends AppCompatActivity {

    public Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPageManager mActivityPageManager = ActivityPageManager.getInstance();
        mActivityPageManager.addActivity(this);
        mContext = this;
    }

    /**
     * 获取指定Id的View
     */
    protected <T extends View> T getViewById(int resId) {
        return (T) findViewById(resId);
    }

    /**
     * 弹出Toast信息
     */
    protected void showToastMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 弹出Toast信息
     */
    protected void showToastMessage(int resId) {
        showToastMessage(getResources().getString(resId));
    }

    /**
     * 打印Log，tag默认为类的名字
     */
    protected void log(String text) {
        log(this.getClass().getName(), text);
    }

    /**
     * 打印Log
     */
    protected void log(String tag, String text) {
        Log.i(tag, text);
    }

    protected void startActivity(Class clazz) {
        startActivity(new Intent(this, clazz));
    }

    /**
     * 跳转到登录界面
     */
    protected void JumpLogin() {
        ActivityPageManager instance = ActivityPageManager.getInstance();
        instance.finishAllActivity();
        startActivity(LoginActivity.class);
    }

    /**
     * 全透状态栏
     */
    protected void setStatusBarFullTransparent() {
        if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //虚拟键盘也透明
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 半透明状态栏
     */
    protected void setHalfTransparent() {
        if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //虚拟键盘也透明
            // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 如果需要内容紧贴着StatusBar
     * 应该在对应的xml布局文件中，设置根布局fitsSystemWindows=true。
     */
    private View contentViewGroup;

    protected void setFitSystemWindow(boolean fitSystemWindow) {
        if (contentViewGroup == null) {
            contentViewGroup = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        }
        contentViewGroup.setFitsSystemWindows(fitSystemWindow);
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    protected int getStateBar() {
        int result = 0;
        int resourceId = this.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = this.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private String data;

    /**
     * 发送短信验证码
     *
     * @param phone
     * @return
     */
    protected String sendMessage(String phone, String type) {
        NetWork.getRetrofit()
                .create(ImpService.class)
                .sendMessage(phone, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SendMessageBean>() {
                    private SendMessageBean sendMessageBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(sendMessageBean.getMsg());
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(SendMessageBean sendMessageBean) {
                        this.sendMessageBean = sendMessageBean;
                    }
                });
        return data;
    }

    protected void setLightStatusBar(Boolean dark) {
        // 5.0 以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //			activity.getWindow().addFlags(SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//SYSTEM_UI_FLAG_LIGHT_STATUS_BAR深色字体   释放会导致无法截屏
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//SYSTEM_UI_FLAG_LIGHT_STATUS_BAR深色字体
        }
        if (dark) {
            int originFlag = getWindow().getDecorView().getSystemUiVisibility();
            getWindow().getDecorView().setSystemUiVisibility(originFlag | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//黑色
        } else {
            int originFlag = getWindow().getDecorView().getSystemUiVisibility();
            getWindow().getDecorView().setSystemUiVisibility(originFlag | View.SYSTEM_UI_FLAG_VISIBLE);//白色
        }
    }

    protected void initStatusBar(View topView) {
        topView.setLayoutParams(new LinearLayout.LayoutParams(DeviceUtils.getScreenWidthAndHeight(mContext, true), getStateBar()));
    }


    //    @Override
//    protected void onResume() {
//        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        }
//        super.onResume();
//    }
}
