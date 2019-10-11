package com.ysxsoft.fragranceofhoney.view;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ysxsoft.fragranceofhoney.R;
import com.ysxsoft.fragranceofhoney.utils.BaseActivity;

public class MyTeamActivity extends BaseActivity implements View.OnClickListener{

    private TextView tv_invitation_friend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_team_layout);
        setHalfTransparent();
        setFitSystemWindow(false);
        initView();

    }

    private void initView() {
        tv_invitation_friend = getViewById(R.id.tv_invitation_friend);
        tv_invitation_friend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_invitation_friend:
                startActivity(InviteQrcodeActivity.class);
                break;
        }

    }
}
