package com.ysxsoft.fragranceofhoney.view;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.ysxsoft.fragranceofhoney.R;
import com.ysxsoft.fragranceofhoney.impservice.ImpService;
import com.ysxsoft.fragranceofhoney.modle.AgreementBean;
import com.ysxsoft.fragranceofhoney.utils.BaseActivity;
import com.ysxsoft.fragranceofhoney.utils.NetWork;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RegisterAgreementActivity extends BaseActivity {

    private WebView web_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_agreement_layout);
        View img_back = getViewById(R.id.img_back);
        TextView tv_title = getViewById(R.id.tv_title);
        tv_title.setText("注册协议");
        requestData();
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        web_content = getViewById(R.id.web_content);
        WebSettings webSettings = web_content.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setTextSize(WebSettings.TextSize.LARGEST);
        webSettings.setLoadWithOverviewMode(true);
        web_content.setWebViewClient(new MyWebViewClient());
    }

    private void requestData() {
        NetWork.getService(ImpService.class)
                .AgreementData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AgreementBean>() {
                    private AgreementBean agreementBean;

                    @Override
                    public void onCompleted() {
                        if ("0".equals(agreementBean.getCode())) {
                            web_content.loadDataWithBaseURL(null, agreementBean.getData().getText(), "text/html", "utf-8", null);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(AgreementBean agreementBean) {

                        this.agreementBean = agreementBean;
                    }
                });
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        // 在WebView中而不在默认浏览器中显示页面
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
