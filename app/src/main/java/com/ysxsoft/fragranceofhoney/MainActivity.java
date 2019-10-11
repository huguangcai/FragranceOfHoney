package com.ysxsoft.fragranceofhoney;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.luck.picture.lib.permissions.RxPermissions;
import com.ysxsoft.fragranceofhoney.fragment.ClassifyFragment;
import com.ysxsoft.fragranceofhoney.fragment.HomeFragment;
import com.ysxsoft.fragranceofhoney.fragment.MyFragment;
import com.ysxsoft.fragranceofhoney.fragment.ShopCardFragment;
import com.ysxsoft.fragranceofhoney.impservice.ImpService;
import com.ysxsoft.fragranceofhoney.modle.LoginBean;
import com.ysxsoft.fragranceofhoney.utils.ActivityPageManager;
import com.ysxsoft.fragranceofhoney.utils.AppUtil;
import com.ysxsoft.fragranceofhoney.utils.BaseActivity;
import com.ysxsoft.fragranceofhoney.utils.IsLoginUtils;
import com.ysxsoft.fragranceofhoney.utils.NetWork;
import com.ysxsoft.fragranceofhoney.utils.StatusBarUtil;
import com.ysxsoft.fragranceofhoney.view.LoginActivity;
import com.ysxsoft.fragranceofhoney.widget.MyViewPager;

import java.util.ArrayList;

import io.reactivex.functions.Consumer;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity {

    private Fragment currentFragment = new Fragment();//（全局）
    private HomeFragment homeFragment = new HomeFragment();
    private ClassifyFragment classifyFragment = new ClassifyFragment();
    private ShopCardFragment shopCardFragment = new ShopCardFragment();
    private MyFragment myFragment = new MyFragment();
    private FrameLayout fl_content;
    private RadioGroup rg_home;
    private RadioButton rb_home, rb_classify, rb_shop_card, rb_my;
    private String uid;
    private MyViewPager vp_content;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private String flag;
    private RxPermissions rxPermissions;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences spUid = getSharedPreferences("UID", Context.MODE_PRIVATE);
        uid = spUid.getString("uid", "");
        setHalfTransparent();
        setFitSystemWindow(false);
        StatusBarUtil.StatusBarLightMode(this); //设置白底黑字
        Intent intent = getIntent();
        flag = intent.getStringExtra("flag");
        uid = intent.getStringExtra("uid");
        rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.CAMERA,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            //申请的权限全部允许
//                            showToastMessage("允许了权限!");
                        } else {
                            //只要有一个权限被拒绝，就会执行
                            showToastMessage("未授权权限，部分功能不能使用");
                        }
                    }
                });


        initView();
//        requestData();
        initData();
    }

    private void requestData() {
        SharedPreferences sp = getSharedPreferences("SAVE_PWD", Context.MODE_PRIVATE);
        String phone = sp.getString("Phone", "");
        String pwd = sp.getString("pwd", "");
        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(pwd)) {
            return;
        }
        NetWork.getRetrofit()
                .create(ImpService.class)
                .Login(phone, pwd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginBean>() {
                    private LoginBean loginBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(loginBean.getMsg());
                        if ("0".equals(loginBean.getCode())) {
                            uid = loginBean.getUserinfo().getUid();
                            homeFragment.setUid(uid);
                            NetWork.setUid(uid);
                            SharedPreferences.Editor spUid = getSharedPreferences("UID", Context.MODE_PRIVATE).edit();
                            spUid.putString("uid", uid);
                            spUid.commit();
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

    private void initView() {
        fl_content = getViewById(R.id.fl_content);
        rg_home = getViewById(R.id.rg_home);
        rb_home = getViewById(R.id.rb_home);
        rb_classify = getViewById(R.id.rb_classify);
        rb_shop_card = getViewById(R.id.rb_shop_card);
        rb_my = getViewById(R.id.rb_my);
        vp_content = getViewById(R.id.vp_content);

    }

    private void initData() {
        fragments.add(homeFragment);
        fragments.add(classifyFragment);
        fragments.add(shopCardFragment);
        fragments.add(myFragment);
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getSupportFragmentManager());
        vp_content.setAdapter(adapter);
        if ("3".equals(flag)) {
            vp_content.setCurrentItem(2);
            rg_home.check(R.id.rb_shop_card);
        } else {
            rg_home.check(R.id.rb_home);
            vp_content.setCurrentItem(0);
        }
        homeFragment.setUid(uid);
        rg_home.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
                        homeFragment.setUid(uid);
                        vp_content.setCurrentItem(0);
                        break;

                    case R.id.rb_classify:
                        classifyFragment.setUid(uid);
                        vp_content.setCurrentItem(1);
                        break;

                    case R.id.rb_shop_card:
                        if (IsLoginUtils.isloginFragment(mContext)) {
                            ActivityPageManager instance = ActivityPageManager.getInstance();
                            instance.finishAllActivity();
                            startActivity(LoginActivity.class);
                        } else {
                            vp_content.setCurrentItem(2);
//                            shopCardFragment.setUid(uid);
                        }
                        break;

                    case R.id.rb_my:
                        if (IsLoginUtils.isloginFragment(mContext)) {
                            ActivityPageManager instance = ActivityPageManager.getInstance();
                            instance.finishAllActivity();
                            startActivity(LoginActivity.class);
                        } else {
//                            switchFragment(myFragment).commit();
                            vp_content.setCurrentItem(3);
//                            myFragment.setUid(uid);
                        }
                        break;

                }
            }
        });
    }

    public class MyViewPagerAdapter extends FragmentPagerAdapter {

        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return fragments.get(i);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

    }

    private FragmentTransaction switchFragment(Fragment targetFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!targetFragment.isAdded()) {
//第一次使用switchFragment()时currentFragment为null，所以要判断一下
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
            transaction.add(R.id.fl_content, targetFragment, targetFragment.getClass().getName());
        } else {
            transaction.hide(currentFragment).show(targetFragment);
        }
        currentFragment = targetFragment;
        return transaction;
    }

    private boolean isBack = false;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isBack) {
                finish();
            } else {
                showToastMessage("再按一次退出");
                isBack = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isBack = false;
                    }
                }, 3000);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

//
//    @Override
//    protected void onPause() {
//        IntentFilter intentFilter = new IntentFilter("ShopCar");
//        registerReceiver(broadcastReceiver, intentFilter);
//        super.onPause();
//    }
//
//    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if ("ShopCar".equals(intent.getAction())) {
////                switchFragment(new ShopCardFragment()).commit();
////                new ShopCardFragment().setUid(uid);
//
//            }
//        }
//    };
//
//    @Override
//    protected void onDestroy() {
//        unregisterReceiver(broadcastReceiver);
//        super.onDestroy();
//    }
}
