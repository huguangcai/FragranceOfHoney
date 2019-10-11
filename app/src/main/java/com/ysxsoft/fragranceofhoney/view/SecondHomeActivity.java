package com.ysxsoft.fragranceofhoney.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.jdsjlzx.ItemDecoration.LuDividerDecoration;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.ysxsoft.fragranceofhoney.R;
import com.ysxsoft.fragranceofhoney.adapter.SecondHomeAdapter;
import com.ysxsoft.fragranceofhoney.impservice.ImpService;
import com.ysxsoft.fragranceofhoney.modle.SecondHomeBean;
import com.ysxsoft.fragranceofhoney.utils.AppUtil;
import com.ysxsoft.fragranceofhoney.utils.BaseActivity;
import com.ysxsoft.fragranceofhoney.utils.NetWork;

import java.lang.ref.WeakReference;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SecondHomeActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private RelativeLayout rl_search;
    private TextView tv_title_right;
    private EditText ed_title_search;
    private String uid, cid;
    private LinearLayout ll_no_hava_data;
    private FrameLayout fl_have_data;
    private View img_back;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LuRecyclerView mRecyclerView = null;
    private LuRecyclerViewAdapter mLuRecyclerViewAdapter = null;
    private int page = 1;
    private SecondHomeAdapter mDataAdapter;
    private PreviewHandler mHandler = new PreviewHandler(this);
    private SecondHomeBean secondHomeBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_home_layout);
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        cid = intent.getStringExtra("gid");
        initView();
        initListener();
    }

    private void initView() {
        img_back = getViewById(R.id.img_back);
        rl_search = getViewById(R.id.rl_search);
        tv_title_right = getViewById(R.id.tv_title_right);
        tv_title_right.setVisibility(View.VISIBLE);
        tv_title_right.setText("搜索");
        ed_title_search = getViewById(R.id.ed_title_search);
        ed_title_search.setFocusable(false);
        ll_no_hava_data = getViewById(R.id.ll_no_hava_data);
        fl_have_data = getViewById(R.id.fl_have_data);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (LuRecyclerView) findViewById(R.id.list);
        //设置刷新时动画的颜色，可以设置4个
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setProgressViewOffset(false, 0, AppUtil.dip2px(this, 48));
            mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_color);
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }
        mDataAdapter = new SecondHomeAdapter(mContext);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mLuRecyclerViewAdapter = new LuRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(mLuRecyclerViewAdapter);
        LuDividerDecoration divider = new LuDividerDecoration.Builder(this, mLuRecyclerViewAdapter)
                .setHeight(R.dimen.default_divider_height)
                .setPadding(R.dimen.default_divider_padding)
                .setColorResource(R.color.white)
                .build();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(divider);
        mLuRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                SecondHomeBean.DataBean dataBean = mDataAdapter.getDataList().get(position);
                int bid = dataBean.getId();
                Intent intent = new Intent(mContext, SearchDataActivity.class);
                intent.putExtra("gid", String.valueOf(bid));
                intent.putExtra("uid", uid);
                mContext.startActivity(intent);
            }
        });

        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (secondHomeBean != null) {
                    if (page < secondHomeBean.getLast_page()) {
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

    private void initListener() {
        img_back.setOnClickListener(this);
        rl_search.setOnClickListener(this);
        tv_title_right.setOnClickListener(this);
        ed_title_search.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                AppUtil.colsePhoneKeyboard(this);
                finish();
                break;
            case R.id.ed_title_search:
            case R.id.rl_search:
            case R.id.tv_title_right:
//                if (IsLoginUtils.isloginFragment(getActivity())) {
//                    ActivityPageManager instance = ActivityPageManager.getInstance();
//                    instance.finishAllActivity();
//                    getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
//                } else {
                AppUtil.colsePhoneKeyboard(this);
                Intent intent = new Intent(mContext, SearchDataActivity.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
//                }
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
        private WeakReference<SecondHomeActivity> ref;
        PreviewHandler(SecondHomeActivity activity) {
            ref = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final SecondHomeActivity activity = ref.get();
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
                        activity.mRecyclerView.refreshComplete(secondHomeBean.getData().size());
                        activity.notifyDataSetChanged();
                    } else {
                        activity.mRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                            @Override
                            public void reload() {
                                activity.mRecyclerView.refreshComplete(secondHomeBean.getData().size());
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

    public void getData() {
        NetWork.getService(ImpService.class)
                .getSecondHomeData(String.valueOf(page), cid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SecondHomeBean>() {
                    private SecondHomeBean secondHomeBean;

                    @Override
                    public void onCompleted() {
                        if ("0".equals(secondHomeBean.getCode())) {
                            showData(secondHomeBean);
                            List<SecondHomeBean.DataBean> data = secondHomeBean.getData();
                            if (data.size()<=0){
                               ll_no_hava_data.setVisibility(View.VISIBLE);
                               fl_have_data.setVisibility(View.GONE);
                            }else {
                                ll_no_hava_data.setVisibility(View.GONE);
                                fl_have_data.setVisibility(View.VISIBLE);
                            }
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
                    public void onNext(SecondHomeBean secondHomeBean) {

                        this.secondHomeBean = secondHomeBean;
                    }
                });
    }

    private void showData(SecondHomeBean secondHomeBean) {
        this.secondHomeBean = secondHomeBean;
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
