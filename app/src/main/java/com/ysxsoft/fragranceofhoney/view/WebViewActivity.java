package com.ysxsoft.fragranceofhoney.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.smtt.export.external.extension.interfaces.IX5WebViewExtension;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.export.external.interfaces.JsPromptResult;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.TbsVideo;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.utils.SocializeUtils;
import com.ysxsoft.fragranceofhoney.MainActivity;
import com.ysxsoft.fragranceofhoney.R;
import com.ysxsoft.fragranceofhoney.impservice.ImpService;
import com.ysxsoft.fragranceofhoney.modle.AcountSafeBean;
import com.ysxsoft.fragranceofhoney.modle.AliPayBean;
import com.ysxsoft.fragranceofhoney.modle.BalanceMoneyBean;
import com.ysxsoft.fragranceofhoney.modle.PayBalanceBean;
import com.ysxsoft.fragranceofhoney.modle.WxPayBean;
import com.ysxsoft.fragranceofhoney.utils.AppUtil;
import com.ysxsoft.fragranceofhoney.utils.BaseActivity;
import com.ysxsoft.fragranceofhoney.utils.CustomDialog;
import com.ysxsoft.fragranceofhoney.utils.NetWork;
import com.ysxsoft.fragranceofhoney.utils.alipay.PayResult;
import com.ysxsoft.fragranceofhoney.widget.PayDialogBottom;
import com.ysxsoft.fragranceofhoney.widget.PayPwdDilaog;
import com.ysxsoft.fragranceofhoney.widget.PayPwdEditText;
import com.ysxsoft.fragranceofhoney.widget.ShareDialog;
import com.ysxsoft.fragranceofhoney.widget.browser.OpenFileWebChromeClient;
import com.ysxsoft.fragranceofhoney.widget.browser.X5WebView;

import java.math.BigDecimal;
import java.util.Map;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WebViewActivity extends BaseActivity {

    private String uid, gid;
    private String url;
    //    private WebView webView;
    private CustomDialog customDialog;
    private int PAY_TYPE = 1;//支付方式  1 余额 2 支付宝 3  微信
    private BalanceMoneyBean.DataBean data;
    private String Stringtype;////type 1 拼团 2 订单详情   http://192.168.1.101:8080/#/spellTeam?cid=订单id   拼团链接
    private String goodsorderInfo, sucessurl, myfragment;
    private ValueCallback<Uri> uploadFile;
    private OpenFileWebChromeClient mOpenFileWebChromeClient;
    private ProgressDialog dialog;
    private IWXAPI api;
    private int expressfee;
    private X5WebView webView;
    private ViewGroup mViewParent;
    private TextView tv_pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        setContentView(R.layout.webview_layout);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        api = WXAPIFactory.createWXAPI(this, "wxc523eebce9e041f3");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        customDialog = new CustomDialog(mContext, "正在加载...");
        dialog = new ProgressDialog(this);
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        gid = intent.getStringExtra("gid");
        url = intent.getStringExtra("url");
        myfragment = intent.getStringExtra("flag");
        SharedPreferences sp = getSharedPreferences("UID", Context.MODE_PRIVATE);
        uid = sp.getString("uid", "");
        initView(url);
        requestBalanceData();
        IntentFilter filter = new IntentFilter("WXPAY");
        registerReceiver(receiver, filter);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("WXPAY".equals(intent.getAction())) {
                String pay = intent.getStringExtra("pay");
                switch (pay) {
                    case "ok":
                        sucessfulJump();
                        break;
                    case "no":
                        failJumpWaitPay();
                        break;

                }
            }
        }
    };

    /**
     * 获取余额
     */
    private void requestBalanceData() {
        NetWork.getService(ImpService.class)
                .BalanceMoneyData(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BalanceMoneyBean>() {
                    private BalanceMoneyBean balanceMoneyBean;

                    @Override
                    public void onCompleted() {
                        if ("0".equals(balanceMoneyBean.getCode())) {
                            data = balanceMoneyBean.getData();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(BalanceMoneyBean balanceMoneyBean) {
                        this.balanceMoneyBean = balanceMoneyBean;
                    }
                });
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initView(String url) {
        webView = new X5WebView(this, null);
        mViewParent = getViewById(R.id.acty_web_layout_content);
        mViewParent.addView(webView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
//        webView = getViewById(R.id.web_content);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        webView.getView().setOverScrollMode(View.OVER_SCROLL_ALWAYS);
        webView.setWebViewClient(new MyWebViewClient());
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
        settings.setPluginState(WebSettings.PluginState.ON_DEMAND);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setAllowFileAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);

        mOpenFileWebChromeClient = new OpenFileWebChromeClient(this);
        webView.setWebChromeClient(mOpenFileWebChromeClient);

        //这一段,尤其重要。
        IX5WebViewExtension x5WebViewExtension = webView.getX5WebViewExtension();
        if (webView.getX5WebViewExtension() != null) {
            x5WebViewExtension.setScrollBarFadingEnabled(false);//去除滚动条
            //开启X5全屏播放模式
            Bundle data = new Bundle();
            data.putBoolean("standardFullScreen", false);// true表示标准全屏，false表示X5全屏；不设置默认false，
            data.putBoolean("supportLiteWnd", true);// false：关闭小窗；true：开启小窗；不设置默认true，
            data.putInt("DefaultVideoScreen", 2);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1
            webView.getX5WebViewExtension().invokeMiscMethod("setVideoParams", data);
        }

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

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String s) {
            webView.loadUrl(s);
            return true;
        }
    }

    public class MyWebChromeClient extends WebChromeClient {

        @Override
        public boolean onJsAlert(WebView webView, String s, String s1, JsResult jsResult) {
            /**
             * 这里写入你自定义的window alert
             */
            return super.onJsAlert(webView, s, s1, jsResult);
        }

        @Override
        public boolean onJsConfirm(WebView webView, String s, String s1, JsResult jsResult) {
            return super.onJsConfirm(webView, s, s1, jsResult);
        }

        @Override
        public boolean onJsPrompt(WebView webView, String s, String s1, String s2, JsPromptResult jsPromptResult) {
            return super.onJsPrompt(webView, s, s1, s2, jsPromptResult);
        }

        View myVideoView;
        View myNormalView;
        IX5WebChromeClient.CustomViewCallback callback;

        @Override
        public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback customViewCallback) {
            super.onShowCustomView(view, customViewCallback);
            FrameLayout normalView = (FrameLayout) findViewById(R.id.acty_web_layout_content);
            ViewGroup viewGroup = (ViewGroup) normalView.getParent();
            viewGroup.removeView(normalView);
            viewGroup.addView(view);
            myVideoView = view;
            myNormalView = normalView;
            callback = customViewCallback;
        }

        @Override
        public void onHideCustomView() {
            super.onHideCustomView();
            if (callback != null) {
                callback.onCustomViewHidden();
                callback = null;
            }
            if (myVideoView != null) {
                ViewGroup viewGroup = (ViewGroup) myVideoView.getParent();
                viewGroup.removeView(myVideoView);
                viewGroup.addView(myNormalView);
            }
        }

//        @Override
//        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
//            WebView newWebView = new WebView(mContext);
//            view.addView(newWebView);
//            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
//            transport.setWebView(newWebView);
//            resultMsg.sendToTarget();
//            return true;
//        }

        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            Log.i("test", "openFileChooser 1");
            WebViewActivity.this.uploadFile = uploadFile;
            openFileChooseProcess();
        }

        // For Android < 3.0
        public void openFileChooser(ValueCallback<Uri> uploadMsgs) {
            Log.i("test", "openFileChooser 2");
            WebViewActivity.this.uploadFile = uploadFile;
            openFileChooseProcess();
        }

        // For Android  > 4.1.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            Log.i("test", "openFileChooser 3");
            WebViewActivity.this.uploadFile = uploadFile;
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

        @JavascriptInterface
        public void goShopCar() {
            startActivity(ShopCarActivity.class);
        }

        @JavascriptInterface
        public void copyUrl(String gid) {
            String copyUrl = NetWork.H5BaseUrl + "commodityDetailsShare?gid=" + gid;
            AppUtil.CopyToClipboard(mContext, copyUrl);
            showToastMessage("复制成功");
        }

        @JavascriptInterface
        public void shareDetailUrl(String gid, final String text, final String imgUrl) {
            final ShareDialog shareDialog = new ShareDialog(mContext);
            final String Url = NetWork.H5BaseUrl + "commodityDetailsShare?gid=" + gid;
            ImageView img_share_wechat = shareDialog.findViewById(R.id.img_share_wechat);
            img_share_wechat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareDialog.dismiss();
                    shareGoodsUrl(Url, text,"香芊蜜韵官方旗舰店",imgUrl);
                }
            });
            shareDialog.show();
        }

        @JavascriptInterface
        public void openVideo(String videoUrl) {
            Bundle extraData = new Bundle();
            extraData.putInt("screenMode", 102); //来实现默认全屏+控制栏等UI
            TbsVideo.openVideo(mContext, videoUrl, extraData);
        }

        @JavascriptInterface
        public void inviteFriend(String orderInfo) {//邀请好友  分享
            final ShareDialog shareDialog = new ShareDialog(mContext);
            final String loginurl = NetWork.H5BaseUrl + "login?tid=" + orderInfo;//登录的url
            ImageView img_share_wechat = shareDialog.findViewById(R.id.img_share_wechat);
            img_share_wechat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareDialog.dismiss();
                    shareUrl(loginurl);
                }
            });
            shareDialog.show();
        }

        @JavascriptInterface
        public void Pay(final String orderInfo, String type, final String sumMoney, int expressfee) {
            expressfee = expressfee;//快递费
            goodsorderInfo = orderInfo;
            Stringtype = type;//type 1 拼团 2 订单详情   http://192.168.1.101:8080/#/spellTeam?cid=订单id   拼团链接
            final PayDialogBottom dialog = new PayDialogBottom(mContext);
            final TextView tv_pay_money = dialog.findViewById(R.id.tv_pay_money);
//            tv_pay_money.setText(sumMoney);
            BigDecimal expressMoney = new BigDecimal(String.valueOf(expressfee));//快递费
            BigDecimal a1 = new BigDecimal("0.1");
            BigDecimal a2 = new BigDecimal(data.getRatio());//折扣
            BigDecimal multiply = a1.multiply(a2);//折扣率
            BigDecimal b2 = new BigDecimal(sumMoney);//总金额
            BigDecimal subtract = b2.subtract(expressMoney);//总金额 - 快递费
            final BigDecimal money = subtract.multiply(multiply);//折扣后金额
            final BigDecimal add = money.add(expressMoney);
            tv_pay_money.setText(String.valueOf(Double.valueOf(String.valueOf(add))));
            TextView tv_agio = dialog.findViewById(R.id.tv_agio);
            tv_agio.setText("使用余额支付，折扣价" + data.getRatio() + "折");
            TextView tv_balance_money = dialog.findViewById(R.id.tv_balance_money);
            tv_balance_money.setText(data.getMoney());
            tv_pay = dialog.findViewById(R.id.tv_pay);
            ImageView img_close = dialog.findViewById(R.id.img_close);
            img_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PAY_TYPE = 1;
                    dialog.dismiss();
                    failJumpWaitPay();
                }
            });
            LinearLayout ll_babance_money = dialog.findViewById(R.id.ll_babance_money);
            LinearLayout ll_alipay = dialog.findViewById(R.id.ll_alipay);
            LinearLayout ll_wechatpay = dialog.findViewById(R.id.ll_wechatpay);
            final ImageView img_balance = dialog.findViewById(R.id.img_balance);
            final ImageView img_alipay = dialog.findViewById(R.id.img_alipay);
            final ImageView img_wechatpay = dialog.findViewById(R.id.img_wechatpay);

            ll_babance_money.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PAY_TYPE = 1;
                    tv_pay_money.setText(String.valueOf(add));
                    img_balance.setVisibility(View.VISIBLE);
                    img_alipay.setVisibility(View.GONE);
                    img_wechatpay.setVisibility(View.GONE);
                }
            });
            ll_alipay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PAY_TYPE = 2;
                    tv_pay_money.setText(sumMoney);
                    img_balance.setVisibility(View.GONE);
                    img_alipay.setVisibility(View.VISIBLE);
                    img_wechatpay.setVisibility(View.GONE);
                }
            });
            ll_wechatpay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PAY_TYPE = 3;
                    tv_pay_money.setText(sumMoney);
                    img_balance.setVisibility(View.GONE);
                    img_alipay.setVisibility(View.GONE);
                    img_wechatpay.setVisibility(View.VISIBLE);
                }
            });
            tv_pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    switch (PAY_TYPE) {
                        case 1://余额支付
                            NetWork.getRetrofit()
                                    .create(ImpService.class)
                                    .AcountSafe(uid)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Observer<AcountSafeBean>() {
                                        private AcountSafeBean acountSafeBean;

                                        @Override
                                        public void onCompleted() {
                                            if ("0".equals(acountSafeBean.getCode())) {
                                                if ("1".equals(acountSafeBean.getData().getTradepassword())) {
                                                    final PayPwdDilaog payPwdDilaog = new PayPwdDilaog(mContext);
                                                    TextView tv_forget_pwd = payPwdDilaog.findViewById(R.id.tv_forget_pwd);
                                                    ImageView img_close = payPwdDilaog.findViewById(R.id.img_close);
                                                    img_close.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            AppUtil.colsePhoneKeyboard(WebViewActivity.this);
                                                            payPwdDilaog.dismiss();
                                                            failJumpWaitPay();
                                                        }
                                                    });
                                                    tv_forget_pwd.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            Intent intent = new Intent(mContext, ModifyTradePwdActivity.class);
                                                            intent.putExtra("uid", uid);
                                                            intent.putExtra("mobile", data.getMobile());
                                                            startActivity(intent);
                                                        }
                                                    });
                                                    final PayPwdEditText ed_ppet = payPwdDilaog.findViewById(R.id.ed_ppet);
                                                    ed_ppet.initStyle(R.drawable.edit_num_bg_red, 6, 0.33f, R.color.black, R.color.black, 20);
                                                    ed_ppet.setFocus();
                                                    ed_ppet.setOnTextFinishListener(new PayPwdEditText.OnTextFinishListener() {
                                                        @Override
                                                        public void onFinish(final String str) {
                                                            payPwdDilaog.dismiss();
                                                            payBalance(orderInfo, str);
                                                        }
                                                    });
                                                    payPwdDilaog.show();
                                                } else {
                                                    failJumpWaitPay();
                                                    Intent intent = new Intent(mContext, TradePwdActivity.class);
                                                    intent.putExtra("uid", uid);
                                                    startActivity(intent);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            showToastMessage(e.getMessage());
                                        }

                                        @Override
                                        public void onNext(AcountSafeBean acountSafeBean) {
                                            this.acountSafeBean = acountSafeBean;
                                        }
                                    });
                            break;
                        case 2://支付宝支付
                            tv_pay.setEnabled(false);
                            requestAliPay(orderInfo);
                            break;
                        case 3://微信支付
                            tv_pay.setEnabled(false);
                            requestWxPay(orderInfo);
                            break;
                    }
                }
            });
            dialog.show();
        }
    }

    /**
     * 分享商品详情界面
     * @param url
     * @param text 分享提示语
     * @param title 分享标题
     * @param imgUrl 分享展示图片
     */
    private void shareGoodsUrl(String url, String text, String title,String imgUrl) {
        UMWeb umWeb = new UMWeb(url);
        umWeb.setTitle(title);
        umWeb.setThumb(new UMImage(mContext, imgUrl));
        umWeb.setDescription(text);
        new ShareAction(WebViewActivity.this).withMedia(umWeb)
                .setPlatform(SHARE_MEDIA.WEIXIN)
                .setCallback(shareListener).share();
    }

    /**
     * 微信支付
     *
     * @param orderInfo
     */
    private void requestWxPay(String orderInfo) {
        final CustomDialog wxPay = new CustomDialog(mContext, "获取订单中...");
        wxPay.show();
        NetWork.getService(ImpService.class)
                .WxPayData(uid, orderInfo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WxPayBean>() {
                    private WxPayBean wxPayBean;

                    @Override
                    public void onCompleted() {
                        PAY_TYPE = 1;
                        if ("0".equals(wxPayBean.getCode())) {
                            PayReq req = new PayReq();
                            req.appId = wxPayBean.getData().getAppid();
                            req.partnerId = wxPayBean.getData().getPartnerid();
                            req.prepayId = wxPayBean.getData().getPrepayid();
                            req.nonceStr = wxPayBean.getData().getNoncestr();
                            req.timeStamp = String.valueOf(wxPayBean.getData().getTimestamp());
                            req.packageValue = wxPayBean.getData().getPackageX();
                            req.sign = wxPayBean.getData().getSign();
                            req.extData = "app data"; // optional
//                            showToastMessage("正常调起支付");
                            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                            api.sendReq(req);
                            wxPay.dismiss();
                            tv_pay.setEnabled(true);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        wxPay.dismiss();
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(WxPayBean wxPayBean) {
                        this.wxPayBean = wxPayBean;
                    }
                });
    }

    /**
     * 分享url
     * @param loginurl
     *    分享提示语
     */
    private void shareUrl(String loginurl) {
        UMWeb umWeb = new UMWeb(loginurl);
        umWeb.setTitle("香芊蜜韵");
        umWeb.setThumb(new UMImage(mContext, R.mipmap.img_logo));
        umWeb.setDescription("朋友邀请你参加商品拼团");
        new ShareAction(WebViewActivity.this).withMedia(umWeb)
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
            showToastMessage("分享成功");
            SocializeUtils.safeCloseDialog(dialog);
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            SocializeUtils.safeCloseDialog(dialog);
            showToastMessage("分享失败" + t.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            SocializeUtils.safeCloseDialog(dialog);
            showToastMessage("取消分享");

        }
    };

    /*********************************支付宝支付***************************************/
    private static final int SDK_PAY_FLAG = 1;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PAY_TYPE = 1;
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        showToastMessage("支付成功");
                        sucessfulJump();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        showToastMessage("支付失败");
                        failJumpWaitPay();
                    }
                    tv_pay.setEnabled(true);
                    break;
                }
            }
        }
    };

    /**
     * 支付宝支付
     *
     * @param orderInfo
     */
    private void requestAliPay(String orderInfo) {
        NetWork.getService(ImpService.class)
                .AliPayData(uid, orderInfo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AliPayBean>() {
                    private AliPayBean aliPayBean;

                    @Override
                    public void onCompleted() {
                        if ("0".equals(aliPayBean.getCode())) {
                            AliPay(aliPayBean.getData());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(AliPayBean aliPayBean) {
                        this.aliPayBean = aliPayBean;
                    }
                });
    }

    /**
     * 调用支付宝接口
     *
     * @param orderInfo
     */
    private void AliPay(final String orderInfo) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(WebViewActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                log(result.toString());
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /******************************************************************************/
    /**
     * 余额支付
     *
     * @param orderInfo
     * @param str
     */
    private void payBalance(final String orderInfo, String str) {
        NetWork.getService(ImpService.class)
                .PayBalanceData(uid, orderInfo, str)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PayBalanceBean>() {
                    private PayBalanceBean payBalanceBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(payBalanceBean.getMsg());
                        AppUtil.colsePhoneKeyboard(WebViewActivity.this);
                        if ("0".equals(payBalanceBean.getCode())) {
                            sucessfulJump();
                        } else if ("3".equals(payBalanceBean.getCode())) {

                        } else {
                            failJumpWaitPay();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(PayBalanceBean payBalanceBean) {

                        this.payBalanceBean = payBalanceBean;
                    }
                });

    }

    /**
     * 支付成功跳转界面
     */
    private void sucessfulJump() {
        if ("myfragment".equals(myfragment)) {
//            if ("1".equals(Stringtype)) {//type 1 拼团 2 订单详情
//                url = NetWork.H5BaseUrl + "spellTeam?cid=" + goodsorderInfo;
//            } else {
            webView.reload();
            url = NetWork.H5BaseUrl + "orderDetails?oid=" + goodsorderInfo + "&sc=2";
//            }
        } else {
            webView.reload();
//            if ("1".equals(Stringtype)) {//type 1 拼团 2 订单详情
//                url = NetWork.H5BaseUrl + "spellTeam?cid=" + goodsorderInfo;
//            } else {
//            }
//            http://bzdsh.sanzhima.cn/#/orderDetails?oid=%@&uid=%@&sc=2
            url = NetWork.H5BaseUrl + "orderDetails?oid=" + goodsorderInfo + "&sc=2";
            webView.loadUrl(url);
        }
    }

    /**
     * 支付失败跳转到待支付
     */
    private void failJumpWaitPay() {
        webView.reload();
        url = NetWork.H5BaseUrl + "order?sc=2&status=" + "1" + "&uid=" + uid;
        webView.loadUrl(url);
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (webView.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK) {
//            webView.goBack();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

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
        webView = null;
        unregisterReceiver(receiver);
    }


}
