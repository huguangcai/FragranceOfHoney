package com.ysxsoft.fragranceofhoney.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.ysxsoft.fragranceofhoney.R;

/**
 * Create By 胡
 * on 2019/10/24 0024
 */
public class SelectDialog extends ABSDialog {
    public OnSelectDialogListener onSelectDialog;

    public SelectDialog(@NonNull Context context) {
        super(context);
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width= WindowManager.LayoutParams.MATCH_PARENT;
        params.height=WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
    }

    @Override
    protected void initView() {
        getViewById(R.id.tv_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        getViewById(R.id.tv_caputr).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
               if (onSelectDialog!=null){
                   onSelectDialog.onCaputr();
               }
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.select_dialog_layout;
    }

   public interface OnSelectDialogListener{
        void onCaputr();
        void onPhoto();
    }

    public void setOnSelectDialog(OnSelectDialogListener onSelectDialog){
        this.onSelectDialog = onSelectDialog;
    }
}
