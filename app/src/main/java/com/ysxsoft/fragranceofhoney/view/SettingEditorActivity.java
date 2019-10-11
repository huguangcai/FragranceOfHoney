package com.ysxsoft.fragranceofhoney.view;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.RxPermissions;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.ysxsoft.fragranceofhoney.MainActivity;
import com.ysxsoft.fragranceofhoney.R;
import com.ysxsoft.fragranceofhoney.impservice.ImpService;
import com.ysxsoft.fragranceofhoney.modle.UploadHeadImgBean;
import com.ysxsoft.fragranceofhoney.utils.BaseActivity;
import com.ysxsoft.fragranceofhoney.utils.CustomDialog;
import com.ysxsoft.fragranceofhoney.utils.FileUtils;
import com.ysxsoft.fragranceofhoney.utils.ImageLoadUtil;
import com.ysxsoft.fragranceofhoney.utils.NetWork;
import com.ysxsoft.fragranceofhoney.widget.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class SettingEditorActivity extends BaseActivity implements View.OnClickListener {

    private View img_back;
    private CircleImageView img_head;
    private TextView tv_nikeName, tv_sex;
    private LinearLayout ll_nikeName, ll_sex;
    private String uid, nikeName, headPath, sex;
    private CustomDialog customDialog;
    private RxPermissions rxPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_editor_layout);
        customDialog = new CustomDialog(mContext, "正在上传,请稍后...");
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        nikeName = intent.getStringExtra("nikeName");
        headPath = intent.getStringExtra("headPath");
        sex = intent.getStringExtra("sex");
        rxPermissions = new RxPermissions(this);
        initView();
        initListener();

    }

    private void initView() {
        img_back = getViewById(R.id.img_back);
        TextView tv_title = getViewById(R.id.tv_title);
        tv_title.setText("编辑");
        img_head = getViewById(R.id.img_head);
        ll_nikeName = getViewById(R.id.ll_nikeName);
        tv_nikeName = getViewById(R.id.tv_nikeName);
        ll_sex = getViewById(R.id.ll_sex);
        tv_sex = getViewById(R.id.tv_sex);
        if (!TextUtils.isEmpty(uid) || uid != null) {
            ImageLoadUtil.GlideHeadImageLoad(mContext, headPath, img_head);
            tv_nikeName.setText(nikeName);
            switch (sex) {
                case "1":
                    tv_sex.setText("男");
                    break;
                case "2":
                    tv_sex.setText("女");
                    break;
            }
        }
    }

    private void initListener() {
        img_back.setOnClickListener(this);
        img_head.setOnClickListener(this);
        ll_nikeName.setOnClickListener(this);
        ll_sex.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_head:
                rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            //申请的权限全部允许
//                           showToastMessage("允许了权限!");
                            openGallery();
                        } else {
                            //只要有一个权限被拒绝，就会执行
                            showToastMessage("未授权权限，部分功能不能使用");
                        }
                    }
                });
                break;
            case R.id.ll_nikeName:
                Intent intent1 = new Intent(mContext, UserNameActivity.class);
                intent1.putExtra("uid", uid);
                startActivity(intent1);
                break;
            case R.id.ll_sex:
                Intent intent = new Intent(mContext, SexActivity.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
                break;
        }
    }

    /**
     * 打开相册
     */
    private void openGallery() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofAll())
                .maxSelectNum(1)// 最大图片选择数量
                .selectionMode(PictureConfig.SINGLE)
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .enableCrop(true)// 是否裁剪
                .compress(true)// 是否压缩
                .compressSavePath(FileUtils.getSDCardPath())//压缩图片保存地址
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    customDialog.show();
                    // 图片选择结果回调
                    final List<LocalMedia> localMedia = PictureSelector.obtainMultipleResult(data);
                    File file = new File(localMedia.get(0).getPath());
                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    MultipartBody.Part body = MultipartBody.Part.createFormData("avatar", file.getName(), requestBody);
                    NetWork.getRetrofit()
                            .create(ImpService.class)
                            .UploadHeadImg(uid, body)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<UploadHeadImgBean>() {
                                private UploadHeadImgBean uploadHeadImgBean;

                                @Override
                                public void onCompleted() {
                                    showToastMessage(uploadHeadImgBean.getMsg());
                                    if ("0".equals(uploadHeadImgBean.getCode())) {
                                        ClearCache();
                                        ImageLoadUtil.GlideHeadImageLoad(mContext, localMedia.get(0).getPath(), img_head);
                                        customDialog.dismiss();
                                    }
                                }


                                @Override
                                public void onError(Throwable e) {
                                    showToastMessage(e.getMessage());
                                }

                                @Override
                                public void onNext(UploadHeadImgBean uploadHeadImgBean) {
                                    this.uploadHeadImgBean = uploadHeadImgBean;
                                }
                            });
            }
        }
    }

    private void ClearCache() {
        // 清空图片缓存，包括裁剪、压缩后的图片 注意:必须要在上传完成后调用 必须要获取权限
        RxPermissions permissions = new RxPermissions(this);
        permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new io.reactivex.Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            PictureFileUtils.deleteCacheDirFile(mContext);
                        } else {
                            showToastMessage(getString(R.string.picture_jurisdiction));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter("MODIFY_NAME_SEX");
        registerReceiver(broadcastReceiver, intentFilter);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("MODIFY_NAME_SEX".equals(intent.getAction())) {
                String type = intent.getStringExtra("type");
                switch (type) {
                    case "1":
                        tv_sex.setText("男");
                        break;
                    case "2":
                        tv_sex.setText("女");
                        break;
                    default:
                        tv_nikeName.setText(type);
                        break;
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
