package com.ysxsoft.fragranceofhoney.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ysxsoft.fragranceofhoney.R;

public class ShareDialog extends ABSDialog {

    private TextView tv_title;

    public ShareDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void initView() {
        ImageView img_share_close = getViewById(R.id.img_share_close);
        tv_title = getViewById(R.id.tv_title);
        img_share_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.my_team_share_dialog_layout;
    }

    public void setTitle(String title){
        tv_title.setText(title);
    }
}
