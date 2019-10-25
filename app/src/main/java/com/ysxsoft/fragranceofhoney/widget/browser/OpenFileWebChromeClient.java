package com.ysxsoft.fragranceofhoney.widget.browser;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.permissions.RxPermissions;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.ysxsoft.fragranceofhoney.utils.FileUtils;
import com.ysxsoft.fragranceofhoney.view.WebViewActivity;
import com.ysxsoft.fragranceofhoney.widget.SelectDialog;

import io.reactivex.functions.Consumer;

/**
 * Created by zhaozhipeng on 18/5/7.
 */

public class OpenFileWebChromeClient extends WebChromeClient {
    public static final int REQUEST_FILE_PICKER = 1;
    public ValueCallback<Uri> mFilePathCallback;
    public ValueCallback<Uri[]> mFilePathCallbacks;
    Activity mContext;

    @Override
    public void onReceivedTitle(WebView webView, String s) {
        super.onReceivedTitle(webView, s);
    }

    public OpenFileWebChromeClient(Activity mContext) {
        super();
        this.mContext = mContext;
    }

    // Android < 3.0 调用这个方法
    public void openFileChooser(final ValueCallback<Uri> filePathCallback) {
        mFilePathCallback = filePathCallback;
        takeOrPickPicture();
    }

    // 3.0 + 调用这个方法
    public void openFileChooser(final ValueCallback filePathCallback, final String acceptType) {
        mFilePathCallback = filePathCallback;
        takeOrPickPicture();
    }

    // js上传文件的<input type="file" name="fileField" id="fileField" />事件捕获
    // Android > 4.1.1 调用这个方法
    public void openFileChooser(final ValueCallback<Uri> filePathCallback, final String acceptType, final String capture) {
        mFilePathCallback = filePathCallback;
        takeOrPickPicture();
    }

    @Override
    public boolean onShowFileChooser(final WebView webView, final ValueCallback<Uri[]> filePathCallback, final FileChooserParams fileChooserParams) {
        mFilePathCallbacks = filePathCallback;
        takeOrPickPicture();
        return true;
    }
    @SuppressLint("CheckResult")
    private void takeOrPickPicture() {
//       系统选照片
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("*/*");
//        mContext.startActivityForResult(Intent.createChooser(intent, "File Chooser"), REQUEST_FILE_PICKER);


//        Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT);
//        String IMAGE_UNSPECIFIED = "image/*";
//        innerIntent.setType(IMAGE_UNSPECIFIED); // 查看类型
//        mContext.startActivityForResult(innerIntent, REQUEST_FILE_PICKER);
        new RxPermissions(mContext).request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    //申请的权限全部允许
                    PictureSelector.create(mContext)
                            .openGallery(PictureMimeType.ofAll())
                            .maxSelectNum(1)// 最大图片选择数量
                            .selectionMode(PictureConfig.SINGLE)
                            .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                            .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                            .enableCrop(false)// 是否裁剪
                            .compress(true)// 是否压缩
                            .compressSavePath(FileUtils.getSDCardPath())//压缩图片保存地址
                            .synOrAsy(true)//同步true或异步false 压缩 默认同步
                            .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                            .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                            .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                            .minimumCompressSize(100)// 小于100kb的图片不压缩
                            .forResult(REQUEST_FILE_PICKER);//结果回调onActivityResult code
                } else {
                    //只要有一个权限被拒绝，就会执行
//                    showToastMessage("未授权权限，部分功能不能使用");
                }
            }
        });
    }
}