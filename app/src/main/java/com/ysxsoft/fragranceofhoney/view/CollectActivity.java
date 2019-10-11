package com.ysxsoft.fragranceofhoney.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.jdsjlzx.ItemDecoration.LuDividerDecoration;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.ysxsoft.fragranceofhoney.R;
import com.ysxsoft.fragranceofhoney.adapter.CollectAdapter;
import com.ysxsoft.fragranceofhoney.impservice.ImpService;
import com.ysxsoft.fragranceofhoney.modle.CollectsListBean;
import com.ysxsoft.fragranceofhoney.modle.DeleteCollectsBean;
import com.ysxsoft.fragranceofhoney.modle.ShopCardBean;
import com.ysxsoft.fragranceofhoney.utils.AppUtil;
import com.ysxsoft.fragranceofhoney.utils.BaseActivity;
import com.ysxsoft.fragranceofhoney.utils.NetWork;

import java.lang.ref.WeakReference;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CollectActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, CollectAdapter.CheckInterface {

    private View img_back;
    private TextView tv_title_right, tv_delete;
    private boolean isManager = false;
    private LinearLayout ll_is_delete;
    private CheckBox cb_box_all;
    private LinearLayout ll_no_hava_data;
    private RelativeLayout rl_have_data;
    private String uid;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LuRecyclerView mRecyclerView = null;
    private LuRecyclerViewAdapter mLuRecyclerViewAdapter = null;
    private PreviewHandler mHandler = new PreviewHandler(this);
    private CollectAdapter mDataAdapter;
    private CollectsListBean collectsListBean;
    private StringBuffer sectionDelete = new StringBuffer();
    private String pids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collect_layout);
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        initView();
        initLIstener();
    }

    private void initView() {
        img_back = getViewById(R.id.img_back);
        TextView tv_title = getViewById(R.id.tv_title);
        tv_title.setText("收藏");
        tv_title_right = getViewById(R.id.tv_title_right);
        tv_title_right.setText("管理");
        ll_is_delete = getViewById(R.id.ll_is_delete);
        cb_box_all = getViewById(R.id.cb_box_all);
        tv_delete = getViewById(R.id.tv_delete);
        rl_have_data = getViewById(R.id.rl_have_data);
        ll_no_hava_data = getViewById(R.id.ll_no_hava_data);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (LuRecyclerView) findViewById(R.id.list);
        //设置刷新时动画的颜色，可以设置4个
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setProgressViewOffset(false, 0, AppUtil.dip2px(this, 48));
            mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_color);
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }
        mDataAdapter = new CollectAdapter(mContext);
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
                CollectsListBean.DataBean dataBean = mDataAdapter.getDataList().get(position);
                int gid = dataBean.getGid();
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("gid", String.valueOf(gid));
                if (!TextUtils.isEmpty(uid) && uid != null) {
                    intent.putExtra("uid", uid);
                }
                String  url = NetWork.H5BaseUrl+"commodityDetails?gid="+gid+"&uid="+ uid+"&flag=1";
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });

        //设置底部加载颜色
        mRecyclerView.setFooterViewColor(R.color.btn_color, R.color.black, android.R.color.transparent);
        //设置底部加载文字提示
        mRecyclerView.setFooterViewHint("拼命加载中", "没有更多数据了", "网络不给力啊，点击再试一次吧");
        onRefresh();
    }

    private void initLIstener() {
        img_back.setOnClickListener(this);
        tv_title_right.setOnClickListener(this);
        tv_delete.setOnClickListener(this);
        cb_box_all.setOnClickListener(this);
        mDataAdapter.setCheckInterface(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.cb_box_all:
                if (mDataAdapter.getDataList().size() != 0) {
                    if (cb_box_all.isChecked()) {
                        for (int i = 0; i < mDataAdapter.getDataList().size(); i++) {
                            mDataAdapter.getDataList().get(i).setChoosed(true);
                        }
                        mDataAdapter.notifyDataSetChanged();
                    } else {
                        for (int i = 0; i < mDataAdapter.getDataList().size(); i++) {
                            mDataAdapter.getDataList().get(i).setChoosed(false);
                        }
                        mDataAdapter.notifyDataSetChanged();
                    }
                }
                break;
            case R.id.tv_title_right:
                if (isManager) {
                    isManager = false;
                    tv_title_right.setText("管理");
                    ll_is_delete.setVisibility(View.GONE);
                    mDataAdapter.isVisibility(false);
                } else {
                    isManager = true;
                    tv_title_right.setText("完成");
                    ll_is_delete.setVisibility(View.VISIBLE);
                    mDataAdapter.isVisibility(true);
                }
                break;
            case R.id.tv_delete:
                if (sectionDelete.length() != 0) {
                    sectionDelete.setLength(0);
                }
                for (CollectsListBean.DataBean dataBean : mDataAdapter.getDataList()) {
                    boolean choosed = dataBean.isChoosed();
                    if (choosed) {
                        sectionDelete.append(String.valueOf(dataBean.getId())).append(",");
                    }
                }
                pids = sectionDelete.deleteCharAt(sectionDelete.length() - 1).toString();
                if ("".equals(pids) && pids.length() == 0) {
                    showToastMessage("删除不能为空");
                    return;
                }
                deleteData(pids);
                break;

        }
    }

    /**
     * 删除收藏
     * @param pids
     */
    private void deleteData(String pids) {
        NetWork.getService(ImpService.class)
                .DeleteCollectsData(pids)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DeleteCollectsBean>() {
                    private DeleteCollectsBean deleteCollectsBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(deleteCollectsBean.getMsg());
                        if ("0".equals(deleteCollectsBean.getCode())) {
                            onRefresh();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(DeleteCollectsBean deleteCollectsBean) {

                        this.deleteCollectsBean = deleteCollectsBean;
                    }
                });
    }

    @Override
    public void onRefresh() {
        if (cb_box_all.isChecked()) {
            cb_box_all.setChecked(false);
        }
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

    @Override
    public void checkGroup(int position, boolean isChecked) {
        mDataAdapter.getDataList().get(position).setChoosed(isChecked);
        if (isAllCheck()) {
            cb_box_all.setChecked(true);
        } else {
            cb_box_all.setChecked(false);
        }
        notifyDataSetChanged();
    }

    /**
     * 全选
     *
     * @return
     */
    private boolean isAllCheck() {
        for (CollectsListBean.DataBean group : mDataAdapter.getDataList()) {
            if (!group.isChoosed())
                return false;
        }
        return true;
    }

    private class PreviewHandler extends Handler {
        private WeakReference<CollectActivity> ref;

        PreviewHandler(CollectActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final CollectActivity activity = ref.get();
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
                        activity.mRecyclerView.refreshComplete(collectsListBean.getData().size());
                        activity.notifyDataSetChanged();
                    } else {
                        activity.mRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                            @Override
                            public void reload() {
                                activity.mRecyclerView.refreshComplete(collectsListBean.getData().size());
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
                .CollectsListData(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CollectsListBean>() {
                    private CollectsListBean collectsListBean;

                    @Override
                    public void onCompleted() {
                        if ("0".equals(collectsListBean.getCode())) {
                            if (collectsListBean.getData().size() <= 0) {
                                ll_no_hava_data.setVisibility(View.VISIBLE);
                                rl_have_data.setVisibility(View.GONE);
                                tv_title_right.setVisibility(View.GONE);
                            } else {
                                ll_no_hava_data.setVisibility(View.GONE);
                                rl_have_data.setVisibility(View.VISIBLE);
                                tv_title_right.setVisibility(View.VISIBLE);
                            }
                            showData(collectsListBean);
                            List<CollectsListBean.DataBean> data = collectsListBean.getData();
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
                    public void onNext(CollectsListBean collectsListBean) {

                        this.collectsListBean = collectsListBean;
                    }
                });
    }

    private void showData(CollectsListBean collectsListBean) {
        this.collectsListBean = collectsListBean;
    }

    private void notifyDataSetChanged() {
        mLuRecyclerViewAdapter.notifyDataSetChanged();
    }

}
