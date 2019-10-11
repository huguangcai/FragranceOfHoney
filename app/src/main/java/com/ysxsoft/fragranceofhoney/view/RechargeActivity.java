package com.ysxsoft.fragranceofhoney.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.ysxsoft.fragranceofhoney.R;
import com.ysxsoft.fragranceofhoney.impservice.ImpService;
import com.ysxsoft.fragranceofhoney.modle.AliPayRechargeBean;
import com.ysxsoft.fragranceofhoney.modle.WxPayBean;
import com.ysxsoft.fragranceofhoney.utils.BaseActivity;
import com.ysxsoft.fragranceofhoney.utils.CustomDialog;
import com.ysxsoft.fragranceofhoney.utils.MoneyTextWatcher;
import com.ysxsoft.fragranceofhoney.utils.NetWork;
import com.ysxsoft.fragranceofhoney.utils.alipay.AuthResult;
import com.ysxsoft.fragranceofhoney.utils.alipay.PayResult;
import com.ysxsoft.fragranceofhoney.widget.ButtomDialog;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 充值金额界面
 */
public class RechargeActivity extends BaseActivity implements View.OnClickListener {

    private View img_back;
    private EditText ed_money;
    private Button btn_submit;
    private String uid;
    private LinearLayout ll_alipay;
    private LinearLayout ll_wechatpay;
    private ImageView img_alipay;
    private ImageView img_wechatpay;
    private int type = 1;
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recharge_layout);
        api = WXAPIFactory.createWXAPI(this, "wxc523eebce9e041f3");
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        initView();
        initListener();

    }

    private void initView() {
        img_back = getViewById(R.id.img_back);
        TextView tv_title = getViewById(R.id.tv_title);
        tv_title.setText("充值");
        ed_money = getViewById(R.id.ed_money);
        btn_submit = getViewById(R.id.btn_submit);
        ed_money.addTextChangedListener(new MoneyTextWatcher(ed_money));
        ll_alipay = getViewById(R.id.ll_alipay);
        ll_wechatpay = getViewById(R.id.ll_wechatpay);
        img_alipay = getViewById(R.id.img_alipay);
        img_wechatpay = getViewById(R.id.img_wechatpay);
    }

    private void initListener() {
        img_back.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        ll_alipay.setOnClickListener(this);
        ll_wechatpay.setOnClickListener(this);
        img_alipay.setOnClickListener(this);
        img_wechatpay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.ll_alipay:
                type = 1;
                img_alipay.setVisibility(View.VISIBLE);
                img_wechatpay.setVisibility(View.GONE);
                break;
            case R.id.ll_wechatpay:
                type = 2;
                img_alipay.setVisibility(View.GONE);
                img_wechatpay.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_submit:
                if (TextUtils.isEmpty(ed_money.getText().toString().trim())) {
                    showToastMessage("充值金额不能为空");
                    return;
                }
                if (Double.valueOf(ed_money.getText().toString().trim()) <= 0) {
                    showToastMessage("充值金额不能为0");
                    return;
                }
                btn_submit.setEnabled(false);
                if (type == 1) {
                    AliPayRecharge();
                } else {
                    WeChatRecharge();
                }
//                final ButtomDialog dialog = new ButtomDialog(mContext);
//                LinearLayout ll_alipay = dialog.findViewById(R.id.ll_alipay);
//                LinearLayout ll_wechatpay = dialog.findViewById(R.id.ll_wechatpay);
//                final ImageView img_alipay = dialog.findViewById(R.id.img_alipay);
//                final ImageView img_wechatpay = dialog.findViewById(R.id.img_wechatpay);
//                ll_alipay.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        img_alipay.setVisibility(View.VISIBLE);
//                        img_wechatpay.setVisibility(View.GONE);
//                    }
//                });
//                ll_wechatpay.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        img_alipay.setVisibility(View.GONE);
//                        img_wechatpay.setVisibility(View.VISIBLE);
//                    }
//                });
//                TextView tv_recharge_money = dialog.findViewById(R.id.tv_recharge_money);
//                tv_recharge_money.setText(ed_money.getText().toString().trim());
//                TextView tv_recharge = dialog.findViewById(R.id.tv_recharge);
//                tv_recharge.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                        AliPayRecharge();
//                    }
//                });
//                dialog.show();
                break;
        }
    }

    /**
     * 微信充值
     */
    private void WeChatRecharge() {
        final CustomDialog wxPay = new CustomDialog(mContext, "获取订单中...");
        wxPay.show();
        NetWork.getService(ImpService.class)
                .WxPaysData(uid, ed_money.getText().toString().trim())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WxPayBean>() {
                    private WxPayBean wxPayBean;

                    @Override
                    public void onCompleted() {
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
                            btn_submit.setEnabled(true);
                            finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(WxPayBean wxPayBean) {
                        this.wxPayBean = wxPayBean;
                    }
                });
    }

    /**
     * 支付宝充值
     */
    private void AliPayRecharge() {
        NetWork.getService(ImpService.class)
                .AliPayRechargeData(uid, ed_money.getText().toString().trim())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AliPayRechargeBean>() {
                    private AliPayRechargeBean aliPayRechargeBean;

                    @Override
                    public void onCompleted() {
                        if ("0".equals(aliPayRechargeBean.getCode())) {
                            aliPay(aliPayRechargeBean.getData());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(AliPayRechargeBean aliPayRechargeBean) {
                        this.aliPayRechargeBean = aliPayRechargeBean;
                    }
                });


    }

    /****************************************支付宝充值******************************************************/
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
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
                        finish();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        showToastMessage("支付失败");
                    }
                    btn_submit.setEnabled(true);
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();
                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        showToastMessage("授权成功");
                    } else {
                        // 其他状态值则为授权失败
                        showToastMessage("授权失败");
                    }
                    break;
                }


            }
        }
    };

    /**
     * 调用支付宝接口
     *
     * @param orderInfo
     */
    private void aliPay(final String orderInfo) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(RechargeActivity.this);
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
    /**********************************************************************************************/


}
