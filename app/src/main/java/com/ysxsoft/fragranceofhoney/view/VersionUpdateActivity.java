package com.ysxsoft.fragranceofhoney.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ysxsoft.fragranceofhoney.R;
import com.ysxsoft.fragranceofhoney.utils.AppUtil;
import com.ysxsoft.fragranceofhoney.utils.BaseActivity;

public class VersionUpdateActivity extends BaseActivity implements View.OnClickListener {

    private View img_back;
    private TextView tv_version_code;
    private Button btn_check_version_code;
    private ImageView img_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.version_updata_layout);
        initView();
        initLIstener();
    }

    private void initView() {
        img_back = getViewById(R.id.img_back);
        TextView tv_title = getViewById(R.id.tv_title);
        tv_title.setText("版本更新");
        tv_version_code = getViewById(R.id.tv_version_code);
        tv_version_code.setText("当前版本V" + AppUtil.getVersionName(mContext));
        btn_check_version_code = getViewById(R.id.btn_check_version_code);
        img_logo = getViewById(R.id.img_logo);
        img_logo.setImageBitmap(AppUtil.getLogoBitmap(mContext));
    }

    private void initLIstener() {
        img_back.setOnClickListener(this);
        btn_check_version_code.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_check_version_code:
                showToastMessage("已是最新版本");
                break;

        }

    }
}
