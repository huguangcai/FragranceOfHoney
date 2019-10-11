package com.ysxsoft.fragranceofhoney.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ysxsoft.fragranceofhoney.R;
import com.ysxsoft.fragranceofhoney.com.ListBaseAdapter;
import com.ysxsoft.fragranceofhoney.com.SuperViewHolder;
import com.ysxsoft.fragranceofhoney.modle.MessageListBean;
import com.ysxsoft.fragranceofhoney.utils.ImageLoadUtil;

public class MyInfoAdapter extends ListBaseAdapter<MessageListBean.DataBean> {
    public MyInfoAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.my_info_item_layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        MessageListBean.DataBean dataBean = mDataList.get(position);
        TextView tv_is_title = holder.getView(R.id.tv_is_title);
        TextView tv_time = holder.getView(R.id.tv_time);
        TextView tv_red_point = holder.getView(R.id.tv_red_point);
        ImageView img_goods_tupian = holder.getView(R.id.img_goods_tupian);
        TextView tv_goods_desc = holder.getView(R.id.tv_goods_desc);
        TextView tv_color = holder.getView(R.id.tv_color);
        TextView tv_size = holder.getView(R.id.tv_size);
        LinearLayout ll_size = holder.getView(R.id.ll_size);
        tv_time.setText(dataBean.getAddtime());

        if (dataBean.getFlag() == 2) {//个人消息
            ll_size.setVisibility(View.VISIBLE);
            img_goods_tupian.setVisibility(View.VISIBLE);
            switch (dataBean.getTypes()) {
                case "1":
                    tv_is_title.setText("拼团成功");
                    break;
                case "2":
                    tv_is_title.setText("商家已发货");
                    break;
                case "3":
                    tv_is_title.setText("退货成功");
                    break;
                case "4":
                    tv_is_title.setText("退货失败");
                    break;
                case "5":
                    tv_is_title.setText("拼团失败");
                    break;
            }

            tv_goods_desc.setText(dataBean.getGoods_name());
            tv_color.setText(dataBean.getColour());
            tv_size.setText(dataBean.getSize());
            ImageLoadUtil.GlideGoodsImageLoad(mContext, dataBean.getGoods_img(), img_goods_tupian);
        } else {//系统消息
            ll_size.setVisibility(View.GONE);
            img_goods_tupian.setVisibility(View.GONE);
            tv_is_title.setText("系统消息");
            tv_goods_desc.setText(dataBean.getText());
        }
        if (dataBean.getNews() == 0) {//0是未读1是已读
            tv_red_point.setVisibility(View.VISIBLE);
        } else {
            tv_red_point.setVisibility(View.GONE);
        }

    }
}
