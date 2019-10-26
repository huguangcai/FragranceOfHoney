package com.ysxsoft.fragranceofhoney.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ysxsoft.fragranceofhoney.R;
import com.ysxsoft.fragranceofhoney.com.ListBaseAdapter;
import com.ysxsoft.fragranceofhoney.com.SuperViewHolder;
import com.ysxsoft.fragranceofhoney.modle.MessageListBean;
import com.ysxsoft.fragranceofhoney.utils.ImageLoadUtil;
import com.ysxsoft.fragranceofhoney.widget.InfoRecyclerView;
import com.ysxsoft.fragranceofhoney.widget.MyRecyclerView;
import com.ysxsoft.fragranceofhoney.widget.MyWebView;

public class MyInfoAdapter extends ListBaseAdapter<MessageListBean.DataBean> {
    public MyInfoAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.my_info_item_layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        MessageListBean.DataBean dataBean = mDataList.get(position);
        TextView tv_is_title = holder.getView(R.id.tv_is_title);
        TextView tv_time = holder.getView(R.id.tv_time);
        TextView tv_red_point = holder.getView(R.id.tv_red_point);
        TextView tv_info_title = holder.getView(R.id.tv_info_title);
        FrameLayout fl_bg = holder.getView(R.id.fl_bg);
        MyWebView web_content = holder.getView(R.id.web_content);
        WebSettings webSettings = web_content.getSettings();
        web_content.setBackgroundColor(0);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setTextSize(WebSettings.TextSize.LARGEST);
//        webSettings.setTextZoom(260);
        web_content.setWebViewClient(new MyWebViewClient());
        InfoRecyclerView recyclerView = holder.getView(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        MyInfoItemAdapter myInfoItemAdapter = new MyInfoItemAdapter(mContext);
        tv_time.setText(dataBean.getAddtime());
        if (dataBean.getFlag() == 2) {//个人消息
            fl_bg.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(myInfoItemAdapter);
            myInfoItemAdapter.addAll(dataBean.getList());
            switch (dataBean.getTypes()) {
                case "1":
                    tv_is_title.setText("购买成功");
                    break;
                case "2":
                    tv_is_title.setText("商家已发货");
                    break;
                case "3":
                    tv_is_title.setText("退货成功");
                    break;
                case "4":
                    tv_is_title.setText("退货失败");
                    break;
                case "5":
                    tv_is_title.setText("购买失败");
                    break;
            }
        } else {//系统消息
            fl_bg.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            tv_is_title.setText("系统消息");
            tv_info_title.setText(dataBean.getText());
//            web_content.loadDataWithBaseURL(null,dataBean.getText(), "text/html", "utf-8", null);
        }
        if (dataBean.getNews() == 0) {//0是未读1是已读
            tv_red_point.setVisibility(View.VISIBLE);
        } else {
            tv_red_point.setVisibility(View.GONE);

        }

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
