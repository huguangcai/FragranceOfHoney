package com.ysxsoft.fragranceofhoney.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ysxsoft.fragranceofhoney.R;
import com.ysxsoft.fragranceofhoney.com.ListBaseAdapter;
import com.ysxsoft.fragranceofhoney.com.SuperViewHolder;
import com.ysxsoft.fragranceofhoney.modle.GetGoodsAddressBean;

public class AddressManagerAdapter extends ListBaseAdapter<GetGoodsAddressBean.DataBean> {
    private OnDeleteClickListener onDeleteClickListener;

    public AddressManagerAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.get_address_manager_item_layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        GetGoodsAddressBean.DataBean dataBean = mDataList.get(position);
        TextView tv_name = holder.getView(R.id.tv_name);
        TextView tv_phone_num = holder.getView(R.id.tv_phone_num);
        TextView tv_address = holder.getView(R.id.tv_address);
        TextView tv_delete = holder.getView(R.id.tv_delete);
        TextView tv_editor = holder.getView(R.id.tv_editor);
        CheckBox cb_box = holder.getView(R.id.cb_box);
        tv_name.setText(dataBean.getLinkname());
        tv_phone_num.setText(dataBean.getPhone());
        tv_address.setText(dataBean.getAddress());
        if (dataBean.getIs_ture() == 0) {//0  默认  1是非默认
            cb_box.setChecked(true);
        } else {
            cb_box.setChecked(false);
        }
        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDeleteClickListener!=null){
                    onDeleteClickListener.deleteItem(position);
                }
            }
        });
        tv_editor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDeleteClickListener!=null){
                    onDeleteClickListener.editorItem(position);
                }
            }
        });

    }

    public interface OnDeleteClickListener{
        void deleteItem(int position);
        void editorItem(int position);
    }
    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener){
        this.onDeleteClickListener = onDeleteClickListener;
    }
}
