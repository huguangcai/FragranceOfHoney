package com.ysxsoft.fragranceofhoney.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ysxsoft.fragranceofhoney.R;

/**
 * 描述： 图片加载
 * 日期： 2018/11/8 0008 13:56
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class ImageLoadUtil {
    /**
     * 加载头像
     * @param context
     * @param url
     * @param view
     */
    public static void GlideHeadImageLoad(Context context, String url, ImageView view) {
        RequestOptions options = new RequestOptions().placeholder(R.mipmap.img_normal_head).error(R.mipmap.img_normal_head);
        Glide.with(context).load(url).apply(options).into(view);
    }
    /**
     * 加载商品图片
     */
    public static void GlideGoodsImageLoad(Context context,String url,ImageView view){
//        RequestOptions options = new RequestOptions().placeholder(R.mipmap.img_default).error(R.mipmap.img_default);
        Glide.with(context).load(url)/*.apply(options)*/.into(view);
    }
}
