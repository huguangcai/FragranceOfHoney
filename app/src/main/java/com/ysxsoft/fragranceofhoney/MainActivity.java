package com.ysxsoft.fragranceofhoney;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.luck.picture.lib.permissions.RxPermissions;
import com.ysxsoft.fragranceofhoney.fragment.ClassifyFragment;
import com.ysxsoft.fragranceofhoney.fragment.HomeFragment;
import com.ysxsoft.fragranceofhoney.fragment.MyFragment;
import com.ysxsoft.fragranceofhoney.fragment.ShopCardFragment;
import com.ysxsoft.fragranceofhoney.impservice.ImpService;
import com.ysxsoft.fragranceofhoney.modle.LoginBean;
import com.ysxsoft.fragranceofhoney.modle.VersionBean;
import com.ysxsoft.fragranceofhoney.utils.ActivityPageManager;
import com.ysxsoft.fragranceofhoney.utils.AppUtil;
import com.ysxsoft.fragranceofhoney.utils.BaseActivity;
import com.ysxsoft.fragranceofhoney.utils.IsLoginUtils;
import com.ysxsoft.fragranceofhoney.utils.NetWork;
import com.ysxsoft.fragranceofhoney.utils.StatusBarUtil;
import com.ysxsoft.fragranceofhoney.utils.VersionUtils;
import com.ysxsoft.fragranceofhoney.view.LoginActivity;
import com.ysxsoft.fragranceofhoney.widget.MyViewPager;
import com.ysxsoft.fragranceofhoney.widget.UpdateDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import io.reactivex.functions.Consumer;
import me.ele.uetool.UETool;
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
    private UpdateDialog dialog;
    private ProgressBar proBar;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences spUid = getSharedPreferences("UID", Context.MODE_PRIVATE);
        uid = spUid.getString("uid", "");
        setFitSystemWindow(false);
//        StatusBarUtil.StatusBarLightMode(this); //设置白底黑字
        setStatusBarFullTransparent();
        ImmersionBar.with(this)
                .statusBarDarkFont(true)
                .init();

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
        UpdateVersion();
    }

    private void UpdateVersion() {
        NetWork.getService(ImpService.class)
                .version("1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<VersionBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(final VersionBean versionBean) {
                        if (versionBean.getCode() == 0) {
                            String versionName = AppUtil.getVersionName(mContext);
//                            String substring = versionBean.getData().getVersion().substring(1, versionBean.getData().getVersion().length());
                            int i = VersionUtils.compareVersion(versionName, versionBean.getData().getVersion());
                            // 0代表相等，1代表version1大于version2，-1代表version1小于version2
                            if (i == -1) {
                                dialog = new UpdateDialog(mContext);
                                TextView tv_update = dialog.findViewById(R.id.tv_update);
                                proBar = dialog.findViewById(R.id.proBar);
                                tv_update.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        proBar.setVisibility(View.VISIBLE);
                                        if (TextUtils.isEmpty(versionBean.getData().getLink())) {
                                            dialog.dismiss();
                                            return;
                                        }
                                        downloadAPK(versionBean.getData().getLink());
                                    }
                                });
                                dialog.show();
                            }
                        }
                    }
                });
    }

    //  进度
    private int mProgress;
    //  文件保存路径
    private String mSavePath;
    //  判断是否停止
    private boolean mIsCancel = false;

    /**
     * 下载APk
     *
     * @param apk_file_url
     */
    private void downloadAPK(final String apk_file_url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        String sdPath = Environment.getExternalStorageDirectory() + "/";
//                      文件保存路径
                        mSavePath = sdPath + "Fragrance";
                        File dir = new File(mSavePath);
                        if (!dir.exists()) {
                            dir.mkdir();
                        }
                        // 下载文件
                        HttpURLConnection conn = (HttpURLConnection) new URL(apk_file_url).openConnection();
                        conn.connect();
                        InputStream is = conn.getInputStream();
                        int length = conn.getContentLength();

                        File apkFile = new File(mSavePath, AppUtil.getVersionName(mContext));
                        FileOutputStream fos = new FileOutputStream(apkFile);

                        int count = 0;
                        byte[] buffer = new byte[1024];
                        while (!mIsCancel) {
                            int numread = is.read(buffer);
                            count += numread;
                            // 计算进度条的当前位置
                            mProgress = (int) (((float) count / length) * 100);
                            // 更新进度条
                            mUpdateProgressHandler.sendEmptyMessage(1);

                            // 下载完成
                            if (numread < 0) {
                                mUpdateProgressHandler.sendEmptyMessage(2);
                                break;
                            }
                            fos.write(buffer, 0, numread);
                        }
                        fos.close();
                        is.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    /**
     * 接收消息
     */
    @SuppressLint("HandlerLeak")
    private Handler mUpdateProgressHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    // 设置进度条
                    proBar.setProgress(mProgress);
                    break;
                case 2:
                    // 隐藏当前下载对话框
                    dialog.dismiss();
                    // 安装 APK 文件
                    installAPK();
            }
        }

        ;
    };

    /**
     * 安装Apk
     */
    private void installAPK() {
        File apkFile = new File(mSavePath, AppUtil.getVersionName(mContext));
        if (!apkFile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
//      安装完成后，启动app（源码中少了这句话）

        if (null != apkFile) {
            try {
                //兼容7.0
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Uri contentUri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".fileProvider", apkFile);
                    intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                    //兼容8.0
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        boolean hasInstallPermission = mContext.getPackageManager().canRequestPackageInstalls();
                        if (!hasInstallPermission) {
                            startInstallPermissionSettingActivity();
                            return;
                        }
                    }
                } else {
                    intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                if (mContext.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
                    mContext.startActivity(intent);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    private void startInstallPermissionSettingActivity() {
        //注意这个是8.0新API
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
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
//                            instance.finishAllActivity();
                            startActivity(LoginActivity.class);
                            instance.finishActivity(MainActivity.this);
                        } else {
                            vp_content.setCurrentItem(2);
//                            shopCardFragment.setUid(uid);
                        }
                        break;

                    case R.id.rb_my:
                        if (IsLoginUtils.isloginFragment(mContext)) {
                            ActivityPageManager instance = ActivityPageManager.getInstance();
//                            instance.finishAllActivity();
                            startActivity(LoginActivity.class);
                            instance.finishActivity(MainActivity.this);
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
                UETool.dismissUETMenu();
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
