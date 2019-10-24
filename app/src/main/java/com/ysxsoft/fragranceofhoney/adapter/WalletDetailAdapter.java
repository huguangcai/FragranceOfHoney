package com.ysxsoft.fragranceofhoney.adapter;

import android.content.Context;
import android.view.View;
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
        TextView tv_process = holder.getView(R.id.tv_process);
        TextView tv_withdraw_cash_money = holder.getView(R.id.tv_withdraw_cash_money);
        tv_withdraw_cash_type.setText(dataBean.getValues());
        tv_time.setText(dataBean.getAddtime());
        tv_withdraw_cash_money.setText(dataBean.getFalgs());
        switch (dataBean.getTypes()) {
            case 1:
                img_withdraw.setBackgroundResource(R.mipmap.img_recharge);
                tv_process.setVisibility(View.GONE);
                break;
            case 2:
                tv_process.setVisibility(View.VISIBLE);
                img_withdraw.setBackgroundResource(R.mipmap.img_withdraw_cash);
                tv_withdraw_cash_money.setText("-"+dataBean.getFalgs());
                break;
            case 3:
                img_withdraw.setBackgroundResource(R.mipmap.img_consume);
                tv_process.setVisibility(View.GONE);
                break;
        }
        switch (dataBean.getType()){
            case 0:
                tv_process.setText("提现中");
                break;
            case 2:
                tv_process.setText("已完成");
                break;
                case 3:
                tv_process.setText("不同意");
                break;
        }

    }
}
