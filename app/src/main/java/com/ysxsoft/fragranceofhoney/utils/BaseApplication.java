package com.ysxsoft.fragranceofhoney.utils;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.tencent.smtt.sdk.QbSdk;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;


/**
 * 描述： application的基类
 * 日期： 2018/10/23 0023 11:17
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class BaseApplication extends Application {

    public static BaseApplication sInstance;
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        sInstance = this;
        //初始化组件化基础库, 统计SDK/推送SDK/分享SDK都必须调用此初始化接口
        /**
         * s:  友盟   apkey
         * s1: 友盟 channel
         * 设备类型  UMConfigure.DEVICE_TYPE_PHONE
         * s2 : Push推送业务的secret
         */
        UMConfigure.init(this, "5c417c9af1f55683a20015ae", "Umeng", UMConfigure.DEVICE_TYPE_PHONE,null);
        initX5Environment();
    }

    private void initX5Environment() {
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + arg0);
//                Toast.makeText(getContext(),"onViewInitFinished is",Toast.LENGTH_SHORT) .show();
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(),  cb);
    }

    public static Context getContext(){
        return mContext;
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(base);
    }
    {
//        PlatformConfig.setWeixin("wxc523eebce9e041f3", "3baf1193c85774b3fd9d18447d76cab0");
//        PlatformConfig.setWeixin("wxc523eebce9e041f3", "8e5db3d549b200e934a127b0073e3099");//s:  appId   s1: AppSecret
        PlatformConfig.setWeixin("wxc523eebce9e041f3", "01fbabed46e4635ec9f6de8090d8c7d6");//s:  appId   s1: AppSecret
//        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
        PlatformConfig.setQQZone("101541625", "e0dff3680171cef4cd0a5776f169fd29");//qq 的s:APP ID   s1：APP Key
    }

}
