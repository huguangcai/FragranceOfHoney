package com.ysxsoft.fragranceofhoney.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ysxsoft.fragranceofhoney.R;
import com.ysxsoft.fragranceofhoney.com.ListBaseAdapter;
import com.ysxsoft.fragranceofhoney.com.SuperViewHolder;
import com.ysxsoft.fragranceofhoney.modle.RecommendBean;
import com.ysxsoft.fragranceofhoney.utils.ImageLoadUtil;

public class HomeRecommendAdapter extends ListBaseAdapter<RecommendBean.DataBean> {

    public HomeRecommendAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.recommend_item_layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        RecommendBean.DataBean dataBean = mDataList.get(position);
        ImageView img_goods_tupian = holder.getView(R.id.img_goods_tupian);
        TextView tv_goods_desc = holder.getView(R.id.tv_goods_desc);
        TextView tv_price = holder.getView(R.id.tv_price);
        TextView tv_pintuan_num = holder.getView(R.id.tv_pintuan_num);
        ImageLoadUtil.GlideGoodsImageLoad(mContext,dataBean.getImgurl(),img_goods_tupian);
        tv_goods_desc.setText(dataBean.getGoods_name());
        tv_price.setText(dataBean.getPrice());
        tv_pintuan_num.setText(String.valueOf(dataBean.getGroup()));

    }
}
