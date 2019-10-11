package com.ysxsoft.fragranceofhoney.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ysxsoft.fragranceofhoney.R;
import com.ysxsoft.fragranceofhoney.modle.HomeClassifyBean;
import com.ysxsoft.fragranceofhoney.utils.ImageLoadUtil;
import com.ysxsoft.fragranceofhoney.widget.CircleImageView;

import java.util.List;

public class HomeClassifyAdapter extends RecyclerView.Adapter<HomeClassifyAdapter.MyViewHolder> {

    private Context context;
    private List<HomeClassifyBean.DataBean> data;
    private OnItemClickListener onItemClickListener;
    public HomeClassifyAdapter(Context context, List<HomeClassifyBean.DataBean> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_classify_item_layout, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        ImageLoadUtil.GlideGoodsImageLoad(context,data.get(i).getImgurl(),myViewHolder.img_goods_tupian);
        myViewHolder.tv_img_desc.setText(data.get(i).getCategory_name());
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
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
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private final CircleImageView img_goods_tupian;
        private final TextView tv_img_desc;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img_goods_tupian = itemView.findViewById(R.id.img_goods_tupian);
            tv_img_desc = itemView.findViewById(R.id.tv_img_desc);
        }
    }
    public interface OnItemClickListener{
        void onClick(int i);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
