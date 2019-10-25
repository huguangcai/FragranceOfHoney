package com.ysxsoft.fragranceofhoney.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.library.YLCircleImageView;
import com.ysxsoft.fragranceofhoney.R;
import com.ysxsoft.fragranceofhoney.com.ListBaseAdapter;
import com.ysxsoft.fragranceofhoney.com.SuperViewHolder;
import com.ysxsoft.fragranceofhoney.modle.MessageListBean;
import com.ysxsoft.fragranceofhoney.utils.ImageLoadUtil;

/**
 * Create By 胡
 * on 2019/10/25 0025
 */
public class MyInfoItemAdapter extends ListBaseAdapter<MessageListBean.DataBean.ListBean> {
    public MyInfoItemAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.my_info_item_item_layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        MessageListBean.DataBean.ListBean listBean = mDataList.get(position);
        YLCircleImageView img_goods_tupian = holder.getView(R.id.img_goods_tupian);
        TextView tv_goods_desc = holder.getView(R.id.tv_goods_desc);
        TextView tv_color = holder.getView(R.id.tv_color);
        TextView tv_size = holder.getView(R.id.tv_size);
        ImageLoadUtil.GlideHeadImageLoad(mContext,listBean.getGoods_img(),img_goods_tupian);
        tv_goods_desc.setText(listBean.getGoods_name());
        tv_color.setText(listBean.getColour());
        tv_size.setText(listBean.getSize());
    }
}
