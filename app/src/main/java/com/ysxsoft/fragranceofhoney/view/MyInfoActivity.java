package com.ysxsoft.fragranceofhoney.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.ItemDecoration.LuDividerDecoration;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.ysxsoft.fragranceofhoney.R;
import com.ysxsoft.fragranceofhoney.adapter.MyInfoAdapter;
import com.ysxsoft.fragranceofhoney.impservice.ImpService;
import com.ysxsoft.fragranceofhoney.modle.LookedMessageBean;
import com.ysxsoft.fragranceofhoney.modle.MessageListBean;
import com.ysxsoft.fragranceofhoney.utils.AppUtil;
import com.ysxsoft.fragranceofhoney.utils.BaseActivity;
import com.ysxsoft.fragranceofhoney.utils.NetWork;

import java.lang.ref.WeakReference;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MyInfoActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private View img_back;
    private LinearLayout ll_no_hava_data;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LuRecyclerView mRecyclerView = null;
    private LuRecyclerViewAdapter mLuRecyclerViewAdapter = null;
    private MyInfoAdapter mDataAdapter;
    private PreviewHandler mHandler = new PreviewHandler(this);
    private String uid;
    private MessageListBean messageListBean;
    private int page = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_info_layout);
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        initView();
        initListener();
    }

    private void initView() {
        img_back = getViewById(R.id.img_back);
        TextView tv_title = getViewById(R.id.tv_title);
        tv_title.setText("消息");
        ll_no_hava_data = getViewById(R.id.ll_no_hava_data);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (LuRecyclerView) findViewById(R.id.list);
        //设置刷新时动画的颜色，可以设置4个
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setProgressViewOffset(false, 0, AppUtil.dip2px(this, 48));
            mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_color);
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }
        mDataAdapter = new MyInfoAdapter(mContext);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mLuRecyclerViewAdapter = new LuRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(mLuRecyclerViewAdapter);
        LuDividerDecoration divider = new LuDividerDecoration.Builder(this, mLuRecyclerViewAdapter)
                .setHeight(R.dimen.default_divider_height)
                .setPadding(R.dimen.default_divider_padding)
                .setColorResource(R.color.gray)
                .build();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(divider);
        mLuRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MessageListBean.DataBean dataBean = mDataAdapter.getDataList().get(position);
                String sid = dataBean.getSid();
                if (dataBean.getFlag() == 2) {
                    switch (dataBean.getTypes()) {
                        case "1"://待发货
                            Intent fahuointent = new Intent(mContext, WebViewActivity.class);
                            String fahuourl = NetWork.H5BaseUrl + "order?sc=2&status=" + "3" + "&uid=" + uid;
                            fahuointent.putExtra("uid", uid);
                            fahuointent.putExtra("url", fahuourl);
                            fahuointent.putExtra("flag", "myfragment");
                            startActivity(fahuointent);
                            break;
                        case "2"://待收货
                            Intent goodsintent = new Intent(mContext, WebViewActivity.class);
                            String goodsurl = NetWork.H5BaseUrl + "order?sc=2&status=" + "4" + "&uid=" + uid;
                            goodsintent.putExtra("uid", uid);
                            goodsintent.putExtra("url", goodsurl);
                            goodsintent.putExtra("flag", "myfragment");
                            startActivity(goodsintent);
                            break;
                        case "3"://退货
                            Intent returnintent = new Intent(mContext, WebViewActivity.class);
                            String returnurl = NetWork.H5BaseUrl + "order?sc=2&status=" + "5" + "&uid=" + uid;
                            returnintent.putExtra("uid", uid);
                            returnintent.putExtra("url", returnurl);
                            returnintent.putExtra("flag", "myfragment");
                            startActivity(returnintent);
                            break;
                        case "4"://退货
                            Intent returnintent1 = new Intent(mContext, WebViewActivity.class);
                            String returnurl1 = NetWork.H5BaseUrl + "order?sc=2&status=" + "5" + "&uid=" + uid;
                            returnintent1.putExtra("uid", uid);
                            returnintent1.putExtra("url", returnurl1);
                            returnintent1.putExtra("flag", "myfragment");
                            startActivity(returnintent1);
                            break;
                        case "5"://全部
                            Intent allintent = new Intent(mContext, WebViewActivity.class);
                            String allurl = NetWork.H5BaseUrl + "order?sc=2&status=" + "0" + "&uid=" + uid;
                            allintent.putExtra("uid", uid);
                            allintent.putExtra("url", allurl);
                            allintent.putExtra("flag", "myfragment");
                            startActivity(allintent);
                            break;
                    }
                } else {
                    Intent intent = new Intent(mContext, InfoDetailActivity.class);
                    intent.putExtra("sid", dataBean.getSid());
                    startActivity(intent);
                }
                LookInfo(sid);
            }
        });
        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (messageListBean != null) {
                    if (page < messageListBean.getLast_page()) {
                        page++;
                        requestData();
                    } else {
                        //the end
                        mRecyclerView.setNoMore(true);
                    }
                }
            }
        });
        //设置底部加载颜色
        mRecyclerView.setFooterViewColor(R.color.btn_color, R.color.black, android.R.color.transparent);
        //设置底部加载文字提示
        mRecyclerView.setFooterViewHint("拼命加载中", "没有更多数据了", "网络不给力啊，点击再试一次吧");
    }

    /**
     * 消息已读
     *
     * @param sid
     */
    private void LookInfo(String sid) {
        NetWork.getService(ImpService.class)
                .LookedMessageData(uid, sid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LookedMessageBean>() {
                    private LookedMessageBean lookedMessageBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(lookedMessageBean.getMsg());
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(LookedMessageBean lookedMessageBean) {
                        this.lookedMessageBean = lookedMessageBean;
                    }
                });
    }

    private void initListener() {
        img_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
        }
    }

    @Override
    public void onRefresh() {
        page = 1;
        mSwipeRefreshLayout.setRefreshing(true);
        mRecyclerView.setRefreshing(true);//同时调用LuRecyclerView的setRefreshing方法
        requestData();
    }

    private void requestData() {
        //判断网络是否可用
        if (AppUtil.isNetworkAvaiable(mContext)) {
            mHandler.sendEmptyMessage(-1);
        } else {
            mHandler.sendEmptyMessage(-3);
        }
    }

    private class PreviewHandler extends Handler {
        private WeakReference<MyInfoActivity> ref;

        PreviewHandler(MyInfoActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final MyInfoActivity activity = ref.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            switch (msg.what) {
                case -1:
                    if (activity.mSwipeRefreshLayout.isRefreshing()) {
                        activity.mDataAdapter.clear();
                    }
                    getData();
                    break;
                case -3:
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        activity.mSwipeRefreshLayout.setRefreshing(false);
                        activity.mRecyclerView.refreshComplete(messageListBean.getData().size());
                        activity.notifyDataSetChanged();
                    } else {
                        activity.mRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                            @Override
                            public void reload() {
                                activity.mRecyclerView.refreshComplete(messageListBean.getData().size());
                                activity.notifyDataSetChanged();
                                requestData();
                            }
                        });
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void getData() {
        NetWork.getService(ImpService.class)
                .MessageListData(uid,String.valueOf(page))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MessageListBean>() {
                    private MessageListBean messageListBean;

                    @Override
                    public void onCompleted() {
                        if ("0".equals(messageListBean.getCode())) {
                            if (messageListBean.getData().size()<=0){
                                ll_no_hava_data.setVisibility(View.VISIBLE);
                                mSwipeRefreshLayout.setVisibility(View.GONE);
                            }else {
                                ll_no_hava_data.setVisibility(View.GONE);
                                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                            }
                            showData(messageListBean);
                            List<MessageListBean.DataBean> data = messageListBean.getData();
                            mDataAdapter.addAll(data);
                            if (mSwipeRefreshLayout.isRefreshing()) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                            mRecyclerView.refreshComplete(data.size());
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(MessageListBean messageListBean) {
                        this.messageListBean = messageListBean;
                    }
                });
    }

    private void showData(MessageListBean messageListBean) {
        this.messageListBean = messageListBean;
    }

    private void notifyDataSetChanged() {
        mLuRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        onRefresh();
    }
}
