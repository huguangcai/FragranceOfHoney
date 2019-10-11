package com.ysxsoft.fragranceofhoney.utils;

import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetWork {
//    public static String BaseUrl = "http://bzdsh.sanzhima.cn/admin.php/api/";
    public static String BaseUrl = "http://www.xiangqianmiyun.com/admin.php/api/";

//    public static String H5BaseUrl = "http://192.168.1.101:8080/#/";
//    public static String H5BaseUrl = "http://bzdsh.sanzhima.cn/#/";
    public static String H5BaseUrl = "http://www.xiangqianmiyun.com/indexs/#/";

    public static String getUid() {
        return uid;
    }

    public static void setUid(String uid) {
        NetWork.uid = uid;
    }

    private static String uid;

    /**
     * 获取Retrofit对象
     */
    public static Retrofit getRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetWork.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit;
    }

    //获取原生数据的网络请求
    public static Retrofit getRetrofit1() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetWork.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit;
    }

    /**
     * 获取Service对象
     */
    public static <T extends Object> T getService(Class<T> tClass) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetWork.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(tClass);
    }

}
