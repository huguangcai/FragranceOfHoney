package com.ysxsoft.fragranceofhoney.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.ysxsoft.fragranceofhoney.view.LoginActivity;

public class IsLoginUtils {
    /**
     * 判断是否登录
     * @param context
     */
    public static void islogin(Context context){
        SharedPreferences sp = context.getSharedPreferences("SAVE_PWD", Context.MODE_PRIVATE);
        String phone = sp.getString("Phone", "");
        String pwd = sp.getString("pwd", "");
        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(pwd)) {
            ActivityPageManager instance = ActivityPageManager.getInstance();
            instance.finishAllActivity();
            context.startActivity(new Intent(context,LoginActivity.class));
            return;
        }
    }
    public static boolean isloginFragment(Context context){
        SharedPreferences sp = context.getSharedPreferences("UID", Context.MODE_PRIVATE);
        String uid = sp.getString("uid", "");
//        String pwd = sp.getString("pwd", "");
        if (TextUtils.isEmpty(uid) /*|| TextUtils.isEmpty(pwd)*/) {
            return true;
        }
        return false;
    }


}
