package com.ysxsoft.fragranceofhoney.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
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
        RoundedCorners roundedCorners = new RoundedCorners(20);
        //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
        RequestOptions options = RequestOptions.bitmapTransform(roundedCorners).override(300, 300);
        Glide.with(context).load(url)./*apply(options).*/into(view);
    }
}
