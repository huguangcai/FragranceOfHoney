package com.ysxsoft.fragranceofhoney.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.ysxsoft.fragranceofhoney.R;
import com.ysxsoft.fragranceofhoney.impservice.ImpService;
import com.ysxsoft.fragranceofhoney.modle.PayBalanceBean;
import com.ysxsoft.fragranceofhoney.utils.BaseActivity;
import com.ysxsoft.fragranceofhoney.utils.CustomDialog;
import com.ysxsoft.fragranceofhoney.utils.NetWork;
import com.ysxsoft.fragranceofhoney.widget.PayDialogBottom;
import com.ysxsoft.fragranceofhoney.widget.PayPwdDilaog;
import com.ysxsoft.fragranceofhoney.widget.PayPwdEditText;
import com.ysxsoft.fragranceofhoney.widget.browser.OpenFileWebChromeClient;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PaySucessfulActivity extends BaseActivity {

    private WebView webView;
    private String uid, url;
    private CustomDialog customDialog;
    private ValueCallback<Uri> uploadFile;
    private OpenFileWebChromeClient mOpenFileWebChromeClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_layout);
        customDialog = new CustomDialog(mContext, "正在加载...");
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        url = intent.getStringExtra("url");
        initView();
    }

    private void initView() {
        webView = getViewById(R.id.web_content);
        webView.setWebViewClient(new MyWebViewClient());
//        webView.setWebChromeClient(new MyWebChromeClient());
        MyWebChromeClient myWebChromeClient = new MyWebChromeClient();
        webView.addJavascriptInterface(new JavaInterface(), "aa");//js 调用Java代码
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);//支持js
        settings.setBlockNetworkImage(false);
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);//开启本地DOM存储
        settings.setJavaScriptCanOpenWindowsAutomatically(true);//支持通过JS打开新窗口
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        mOpenFileWebChromeClient = new OpenFileWebChromeClient(this);
        webView.setWebChromeClient(mOpenFileWebChromeClient);
        webView.loadUrl(url);
    }

    public class MyWebViewClient extends WebViewClient {
        @Override
        public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
            sslErrorHandler.proceed();
        }

        @Override
        public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
            customDialog.show();
//            super.onPageStarted(webView, s, bitmap);
        }

        @Override
        public void onPageFinished(WebView webView, String s) {
            customDialog.dismiss();
//            super.onPageFinished(webView, s);
        }
    }

    public class MyWebChromeClient extends WebChromeClient {

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            WebView newWebView = new WebView(mContext);
            view.addView(newWebView);
            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(newWebView);
            resultMsg.sendToTarget();
            return true;
        }
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            Log.i("test", "openFileChooser 1");
            PaySucessfulActivity.this.uploadFile = uploadFile;
            openFileChooseProcess();
        }

        // For Android < 3.0
        public void openFileChooser(ValueCallback<Uri> uploadMsgs) {
            Log.i("test", "openFileChooser 2");
            PaySucessfulActivity.this.uploadFile = uploadFile;
            openFileChooseProcess();
        }

        // For Android  > 4.1.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            Log.i("test", "openFileChooser 3");
            PaySucessfulActivity.this.uploadFile = uploadFile;
            openFileChooseProcess();
        }

        // For Android  >= 5.0
        public boolean onShowFileChooser(com.tencent.smtt.sdk.WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
            Log.i("test", "openFileChooser 4:" + filePathCallback.toString());
            openFileChooseProcess();
            return true;
        }
    }

    public class JavaInterface {
        @JavascriptInterface
        public String getUserId() {
            return uid;
        }

        @JavascriptInterface
        public void goBack() {
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (webView.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OpenFileWebChromeClient.REQUEST_FILE_PICKER && resultCode == Activity.RESULT_OK) {
            onReceiveImage(data, mOpenFileWebChromeClient.mFilePathCallback, mOpenFileWebChromeClient.mFilePathCallbacks);

            mOpenFileWebChromeClient.mFilePathCallback = null;
            mOpenFileWebChromeClient.mFilePathCallbacks = null;
        } else if (resultCode == Activity.RESULT_CANCELED) {
            if (mOpenFileWebChromeClient.mFilePathCallbacks != null) {    //xie ：直接点击取消时，ValueCallback回调会被挂起，需要手动结束掉回调，否则再次点击选择照片无响应
                mOpenFileWebChromeClient.mFilePathCallbacks.onReceiveValue(null);
                mOpenFileWebChromeClient.mFilePathCallbacks = null;
            }
        }
    }
    private void onReceiveImage(final Intent intent, final ValueCallback<Uri> filePathCallback, final ValueCallback<Uri[]> filePathCallbacks) {
        Uri imageUri = intent.getData(); //获取系统返回的照片的Uri
        if (filePathCallback != null) {
            filePathCallback.onReceiveValue(imageUri);
        }
        if (filePathCallbacks != null) {
            if (imageUri != null) {
                filePathCallbacks.onReceiveValue(new Uri[]{imageUri});
            } else {
                filePathCallbacks.onReceiveValue(null);
            }
        }
    }
    private void openFileChooseProcess() {
        Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        String IMAGE_UNSPECIFIED = "image/*";
        innerIntent.setType(IMAGE_UNSPECIFIED); // 查看类型
        startActivityForResult(Intent.createChooser(innerIntent, "test"), 0);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.stopLoading();
        webView.removeAllViews();
        webView.destroy();
        webView.clearCache(true);
        webView.clearHistory();
        ;
        webView = null;
    }
}
