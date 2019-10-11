package com.ysxsoft.fragranceofhoney.widget.browser;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

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

    private void takeOrPickPicture() {
        //系统选照片
        //Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //intent.addCategory(Intent.CATEGORY_OPENABLE);
        //intent.setType("*/*");
        //mContext.startActivityForResult(Intent.createChooser(intent, "File Chooser"), REQUEST_FILE_PICKER);
        Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        String IMAGE_UNSPECIFIED = "image/*";
        innerIntent.setType(IMAGE_UNSPECIFIED); // 查看类型
        mContext.startActivityForResult(innerIntent, REQUEST_FILE_PICKER);
    }
}