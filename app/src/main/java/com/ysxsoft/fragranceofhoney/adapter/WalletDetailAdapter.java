package com.ysxsoft.fragranceofhoney.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.ysxsoft.fragranceofhoney.R;
import com.ysxsoft.fragranceofhoney.com.ListBaseAdapter;
import com.ysxsoft.fragranceofhoney.com.SuperViewHolder;
import com.ysxsoft.fragranceofhoney.modle.WalletDetailBean;

public class WalletDetailAdapter extends ListBaseAdapter<WalletDetailBean.DataBean> {

    public WalletDetailAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.withdraw_cash_recorde_item_layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        WalletDetailBean.DataBean dataBean = mDataList.get(position);
        ImageView img_withdraw = holder.getView(R.id.img_withdraw);
        TextView tv_withdraw_cash_type = holder.getView(R.id.tv_withdraw_cash_type);
        TextView tv_time = holder.getView(R.id.tv_time);
        TextView tv_withdraw_cash_money = holder.getView(R.id.tv_withdraw_cash_money);
        tv_withdraw_cash_type.setText(dataBean.getValues());
        tv_time.setText(dataBean.getAddtime());
        tv_withdraw_cash_money.setText(dataBean.getFalgs());
        switch (dataBean.getType()) {
            case 1:
                img_withdraw.setBackgroundResource(R.mipmap.img_recharge);
                break;
            case 2:
                img_withdraw.setBackgroundResource(R.mipmap.img_withdraw_cash);
                break;
            case 3:
                img_withdraw.setBackgroundResource(R.mipmap.img_consume);
                break;
        }

    }
}
