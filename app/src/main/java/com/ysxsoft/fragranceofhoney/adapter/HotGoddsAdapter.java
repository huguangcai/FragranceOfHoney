package com.ysxsoft.fragranceofhoney.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ysxsoft.fragranceofhoney.R;
import com.ysxsoft.fragranceofhoney.modle.HotGoodsBean;
import com.ysxsoft.fragranceofhoney.utils.ImageLoadUtil;

import java.util.List;

public class HotGoddsAdapter extends RecyclerView.Adapter<HotGoddsAdapter.MyViewHolder> {

    private Context context;
    public List<HotGoodsBean.DataBean> data;
    private OnItemClickListener onItemClickListener;

    public HotGoddsAdapter(Context context, List<HotGoodsBean.DataBean> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.recommend_item_layout, null);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener!=null){
                    onItemClickListener.onClick(i);
                }
            }
        });
        ImageLoadUtil.GlideGoodsImageLoad(context,data.get(i).getImgurl(),myViewHolder.img_goods_tupian);
        myViewHolder.tv_goods_desc.setText(data.get(i).getGoods_name());
        myViewHolder.tv_price.setText(data.get(i).getPrice());
        myViewHolder.tv_pintuan_num.setText(String.valueOf(data.get(i).getGroup()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private final ImageView img_goods_tupian;
        private final TextView tv_goods_desc;
        private final TextView tv_price;
        private final TextView tv_pintuan_num;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img_goods_tupian = itemView.findViewById(R.id.img_goods_tupian);
            tv_goods_desc = itemView.findViewById(R.id.tv_goods_desc);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_pintuan_num = itemView.findViewById(R.id.tv_pintuan_num);
        }
    }
    public interface OnItemClickListener{
        void onClick(int i);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
