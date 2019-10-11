package com.ysxsoft.fragranceofhoney.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ysxsoft.fragranceofhoney.R;
import com.ysxsoft.fragranceofhoney.modle.ClassifyDataBean;
import com.ysxsoft.fragranceofhoney.utils.ImageLoadUtil;

import java.util.List;

public class GridContentAdapter extends RecyclerView.Adapter<GridContentAdapter.MyViewHolde> {

    private FragmentActivity activity;
    public List<ClassifyDataBean.DataBean.OneBean> one;
    private OnItemClickListener onItemClickListener;
    public GridContentAdapter(FragmentActivity activity, List<ClassifyDataBean.DataBean.OneBean> one) {
        this.activity = activity;
        this.one = one;
    }

    @NonNull
    @Override
    public MyViewHolde onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.classify_content_item_layout, null);
        return new MyViewHolde(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolde myViewHolde, final int i) {
        ImageLoadUtil.GlideGoodsImageLoad(activity,one.get(i).getImgurl(),myViewHolde.img_goods_tupian);
        myViewHolde.tv_goods_name.setText(one.get(i).getBrand_name());
        myViewHolde.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener!=null){
                    onItemClickListener.onClick(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return one.size();
    }

    public class MyViewHolde extends RecyclerView.ViewHolder{
        private final ImageView img_goods_tupian;
        private final TextView tv_goods_name;
        public MyViewHolde(@NonNull View itemView) {
            super(itemView);
            img_goods_tupian = itemView.findViewById(R.id.img_goods_tupian);
            tv_goods_name = itemView.findViewById(R.id.tv_goods_name);

        }
    }
    public interface OnItemClickListener{
        void onClick(int i);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
