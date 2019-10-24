package com.ysxsoft.fragranceofhoney.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.ysxsoft.fragranceofhoney.R;
import com.ysxsoft.fragranceofhoney.impservice.ImpService;
import com.ysxsoft.fragranceofhoney.modle.SystemDetialBean;
import com.ysxsoft.fragranceofhoney.utils.BaseActivity;
import com.ysxsoft.fragranceofhoney.utils.NetWork;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class InfoDetailActivity extends BaseActivity {
    private WebView web_content;
    private String sid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_detail_layout);
        Intent intent = getIntent();
        sid = intent.getStringExtra("sid");
        View img_back = getViewById(R.id.img_back);
        TextView tv_title = getViewById(R.id.tv_title);
        tv_title.setText("消息详情");
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
//        webSettings.setTextZoom(500);
        webSettings.setLoadWithOverviewMode(true);
        web_content.setWebViewClient(new MyWebViewClient());

    }

    private void requestData() {
        NetWork.getService(ImpService.class)
                .SystemDetialData(sid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SystemDetialBean>() {
                    private SystemDetialBean systemDetialBean;

                    @Override
                    public void onCompleted() {
                        if ("0".equals(systemDetialBean.getCode())) {
                            web_content.loadDataWithBaseURL(null, systemDetialBean.getData().getText(), "text/html", "utf-8", null);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(SystemDetialBean systemDetialBean) {

                        this.systemDetialBean = systemDetialBean;
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
