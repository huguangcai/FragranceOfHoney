package com.ysxsoft.fragranceofhoney.view;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.ysxsoft.fragranceofhoney.R;
import com.ysxsoft.fragranceofhoney.utils.BaseActivity;

public class ShareLoadActivity extends BaseActivity implements View.OnClickListener {

    private FrameLayout fl_ios, fl_android;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_download_layout);
        setHalfTransparent();
        setFitSystemWindow(false);
        initView();
    }

    private void initView() {
        fl_ios = getViewById(R.id.fl_ios);
        fl_android = getViewById(R.id.fl_android);
        fl_ios.setOnClickListener(this);
        fl_android.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_ios:
                showToastMessage("ios下载");
                break;

            case R.id.fl_android:
                showToastMessage("android下载");
                break;
        }
    }
}
