package com.ysxsoft.fragranceofhoney.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.utils.SocializeUtils;
import com.ysxsoft.fragranceofhoney.R;
import com.ysxsoft.fragranceofhoney.utils.BaseActivity;
import com.ysxsoft.fragranceofhoney.widget.ShareDialog;

public class InviteQrcodeActivity extends BaseActivity {
    private Button btn_share_invitation;
    private ImageView img_qrcode;
    private TextView tv_invitation_code;
    private int stateBar;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invatation_qrcode_layout);
        setHalfTransparent();
        setFitSystemWindow(false);
        stateBar = getStateBar();
        dialog = new ProgressDialog(this);
        img_qrcode = getViewById(R.id.img_qrcode);
        tv_invitation_code = getViewById(R.id.tv_invitation_code);
        btn_share_invitation = getViewById(R.id.btn_share_invitation);
        btn_share_invitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ShareDialog dialog = new ShareDialog(mContext);
                ImageView img_share_wechat = dialog.findViewById(R.id.img_share_wechat);
                img_share_wechat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        startActivity(ShareRegisterActivity.class);
//                        showToastMessage("分享到微信");
                        shareUrl();
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    public void shareUrl(){
        UMWeb web = new UMWeb("https://lanhuapp.com/web/#/item/board?pid=764865c0-f911-48f3-b638-942de26c2ed8");
        web.setTitle("This is wechat title");
        web.setThumb(new UMImage(this, R.mipmap.img_normal_head));
        web.setDescription("my description");
        new ShareAction(InviteQrcodeActivity.this).withMedia(web )
                .setPlatform(SHARE_MEDIA.WEIXIN)
                .setCallback(shareListener).share();
    }
    private UMShareListener shareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            SocializeUtils.safeShowDialog(dialog);
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            showToastMessage("成功了");
            SocializeUtils.safeCloseDialog(dialog);
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            SocializeUtils.safeCloseDialog(dialog);
            showToastMessage("失败"+t.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            SocializeUtils.safeCloseDialog(dialog);
            showToastMessage("取消了");
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode,resultCode,data);
    }
}
