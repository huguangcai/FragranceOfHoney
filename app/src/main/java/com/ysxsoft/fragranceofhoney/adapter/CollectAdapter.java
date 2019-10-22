package com.ysxsoft.fragranceofhoney.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.library.YLCircleImageView;
import com.ysxsoft.fragranceofhoney.R;
import com.ysxsoft.fragranceofhoney.com.ListBaseAdapter;
import com.ysxsoft.fragranceofhoney.com.SuperViewHolder;
import com.ysxsoft.fragranceofhoney.modle.CollectsListBean;
import com.ysxsoft.fragranceofhoney.utils.ImageLoadUtil;

public class CollectAdapter extends ListBaseAdapter<CollectsListBean.DataBean> {

    private CheckInterface checkInterface;
    private boolean isShow;

    public CollectAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.collect_item_layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        final CollectsListBean.DataBean dataBean = mDataList.get(position);
        CheckBox cb_box = holder.getView(R.id.cb_box);
        YLCircleImageView img_tupian = holder.getView(R.id.img_tupian);
        TextView tv_desc = holder.getView(R.id.tv_desc);
        TextView tv_money = holder.getView(R.id.tv_money);
        TextView tv_num = holder.getView(R.id.tv_num);
        ImageLoadUtil.GlideGoodsImageLoad(mContext, dataBean.getImgurl(), img_tupian);
        tv_desc.setText(dataBean.getName());
        tv_money.setText(dataBean.getPrice());
        tv_num.setText(String.valueOf(dataBean.getGroup()));
        if (dataBean.isChoosed()) {
            cb_box.setChecked(true);
        } else {
            cb_box.setChecked(false);
        }

        cb_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataBean.setChoosed(((CheckBox) v).isChecked());
                if (checkInterface != null) {
                    checkInterface.checkGroup(position, ((CheckBox) v).isChecked());
                }
            }
        });
        if (isShow){
            cb_box.setVisibility(View.VISIBLE);
        }else {
            cb_box.setVisibility(View.GONE);
        }
    }

    public void isVisibility(boolean isManager) {
        isShow = isManager;
        notifyDataSetChanged();
    }

    /**
     * 复选框接口
     */
    public interface CheckInterface {
        /**
         * 组选框状态改变触发的事件
         * @param position  元素位置
         * @param isChecked 元素选中与否
         */
        void checkGroup(int position, boolean isChecked);
    }
    /**
     * 单选接口
     * @param checkInterface
     */
    public void setCheckInterface(CheckInterface checkInterface) {
        this.checkInterface = checkInterface;
    }

}
