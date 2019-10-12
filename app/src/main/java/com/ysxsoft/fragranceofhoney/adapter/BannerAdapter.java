package com.ysxsoft.fragranceofhoney.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;

import com.ysxsoft.fragranceofhoney.R;
import com.ysxsoft.fragranceofhoney.modle.HomeLunBoBean;
import com.ysxsoft.fragranceofhoney.utils.AppUtil;
import com.ysxsoft.fragranceofhoney.utils.ImageLoadUtil;
import com.ysxsoft.fragranceofhoney.widget.MyVideoView;

import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.view.View.inflate;

public class BannerAdapter extends PagerAdapter {

    private Context context;
    private List<HomeLunBoBean.DataBean> data;

    public BannerAdapter(Context context, List<HomeLunBoBean.DataBean> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = inflate(context, R.layout.home_lunbo_layout, null);
        FrameLayout fl_content = view.findViewById(R.id.fl_content);
        final MyVideoView video_view = view.findViewById(R.id.video_view);
        final ImageView img_player = view.findViewById(R.id.img_player);
        final ImageView img_mark = view.findViewById(R.id.img_mark);
        ImageView img_tupian = view.findViewById(R.id.img_tupian);
        HomeLunBoBean.DataBean dataBean = data.get(position);
        final String imgurl = dataBean.getImgurl();
        if (".mp4".equals(AppUtil.subAfter4Num(imgurl, 4))) {
            fl_content.setVisibility(VISIBLE);
            img_tupian.setVisibility(GONE);
            ImageLoadUtil.GlideGoodsImageLoad(context, dataBean.getType1(), img_mark);
//            ImageLoadUtil.GlideGoodsImageLoad(context, imgurl, img_mark);
            video_view.setVideoURI(Uri.parse(imgurl));
//            video_view.setMediaController(new MediaController(context));
            video_view.requestFocus();
        } else {
            fl_content.setVisibility(GONE);
            img_tupian.setVisibility(VISIBLE);
            ImageLoadUtil.GlideGoodsImageLoad(context, imgurl, img_tupian);
        }
        img_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                video_view.start();
                img_mark.setVisibility(GONE);
                img_player.setVisibility(GONE);
            }
        });
        fl_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (video_view.isPlaying()){
                    video_view.pause();
                    img_player.setVisibility(VISIBLE);
                }
            }
        });
        video_view.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                img_player.setVisibility(VISIBLE);
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
