package com.ysxsoft.fragranceofhoney.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.ysxsoft.fragranceofhoney.R;
import com.ysxsoft.fragranceofhoney.com.ListBaseAdapter;
import com.ysxsoft.fragranceofhoney.com.SuperViewHolder;
import com.ysxsoft.fragranceofhoney.modle.SecondHomeBean;
import com.ysxsoft.fragranceofhoney.utils.ImageLoadUtil;
import com.ysxsoft.fragranceofhoney.widget.CircleImageView;

public class SecondHomeAdapter extends ListBaseAdapter<SecondHomeBean.DataBean> {

    public SecondHomeAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.classify_content_item_layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        SecondHomeBean.DataBean dataBean = mDataList.get(position);
        CircleImageView img_goods_tupian = holder.getView(R.id.img_goods_tupian);
        TextView tv_goods_name = holder.getView(R.id.tv_goods_name);
        ImageLoadUtil.GlideGoodsImageLoad(mContext,dataBean.getImgurl(),img_goods_tupian);
        tv_goods_name.setText(dataBean.getBrand_name());
    }
}
