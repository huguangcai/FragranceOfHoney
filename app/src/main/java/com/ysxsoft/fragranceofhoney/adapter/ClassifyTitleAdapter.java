package com.ysxsoft.fragranceofhoney.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ysxsoft.fragranceofhoney.R;

import java.util.List;

public class ClassifyTitleAdapter extends RecyclerView.Adapter<ClassifyTitleAdapter.TitleViewHordle>{

    private FragmentActivity activity;
    private List<String> list;
    private OnItemClickListener onItemClickListener;
    private int posotion=0;

    public ClassifyTitleAdapter(FragmentActivity activity, List<String> list) {
        this.activity = activity;
        this.list = list;
    }

    @NonNull
    @Override
    public ClassifyTitleAdapter.TitleViewHordle onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.classify_title_item_layout, null);
        return new ClassifyTitleAdapter.TitleViewHordle(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClassifyTitleAdapter.TitleViewHordle titleViewHordle, final int i) {
        titleViewHordle.tv_title_classify.setText(list.get(i));
        if (posotion==i){
            titleViewHordle.tv_title_classify.setBackgroundResource(R.color.white);
            titleViewHordle.tv_title_classify.setTextColor(activity.getResources().getColor(R.color.btn_color));
        }else {
            titleViewHordle.tv_title_classify.setBackgroundResource(R.color.gray);
            titleViewHordle.tv_title_classify.setTextColor(activity.getResources().getColor(R.color.black));
        }
        titleViewHordle.itemView.setOnClickListener(new View.OnClickListener() {
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
        return list.size();
    }

    public void setSelect(int posotion) {
        this.posotion = posotion;
        notifyDataSetChanged();
    }

    public class TitleViewHordle extends RecyclerView.ViewHolder{

        private final TextView tv_title_classify;

        public TitleViewHordle(@NonNull View itemView) {
            super(itemView);
            tv_title_classify = itemView.findViewById(R.id.tv_title_classify);
        }
    }
    public interface OnItemClickListener{
        void onClick(int i);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
