package com.ysxsoft.fragranceofhoney.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jdsjlzx.ItemDecoration.LuDividerDecoration;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.ysxsoft.fragranceofhoney.R;
import com.ysxsoft.fragranceofhoney.adapter.SearchDataAdapter;
import com.ysxsoft.fragranceofhoney.impservice.ImpService;
import com.ysxsoft.fragranceofhoney.modle.BankCardListBean;
import com.ysxsoft.fragranceofhoney.modle.DeleteHistoryBean;
import com.ysxsoft.fragranceofhoney.modle.SearchHistoryBean;
import com.ysxsoft.fragranceofhoney.modle.SearchRecommendBean;
import com.ysxsoft.fragranceofhoney.modle.SesarchBean;
import com.ysxsoft.fragranceofhoney.utils.AppUtil;
import com.ysxsoft.fragranceofhoney.utils.BaseActivity;
import com.ysxsoft.fragranceofhoney.utils.CustomDialog;
import com.ysxsoft.fragranceofhoney.utils.NetWork;
import com.ysxsoft.fragranceofhoney.widget.flowlayout.FlowLayout;
import com.ysxsoft.fragranceofhoney.widget.flowlayout.TagAdapter;
import com.ysxsoft.fragranceofhoney.widget.flowlayout.TagFlowLayout;

import java.lang.ref.WeakReference;
import java.security.IdentityScope;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SearchDataActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private View img_back;
    private TextView tv_title_right, tv_colligate, tv_sales_volume, tv_delete, tv_price;
    private EditText ed_title_search;
    private TagFlowLayout mFlowLayout, mFlowLayoutRecord;
    private LinearLayout ll_goods_view, ll_search_view;
    private RadioGroup rg_up_down;
    private RadioButton rb_up, rb_down;
    private LinearLayout ll_is_gone, ll_no_hava_data;
    private boolean isUp = true;
    private String uid, bid;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LuRecyclerView mRecyclerView = null;
    private LuRecyclerViewAdapter mLuRecyclerViewAdapter = null;
    private PreviewHandler mHandler = new PreviewHandler(this);
    private int page = 1;
    private SesarchBean sesarchBean;
    private SearchDataAdapter mDataAdapter;
    private String goods_name;
    private CustomDialog customDialog;
    private  int type=1;  //1是综合2是销量3是价格升序4是降序
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);
        customDialog = new CustomDialog(mContext, "正在加载...");
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        bid = intent.getStringExtra("gid");
        SharedPreferences sp = getSharedPreferences("UID", Context.MODE_PRIVATE);
        uid = sp.getString("uid", "");
        initView();
        searchRecommendData();
        searchHistoryData();
        initListener();
    }

    /**
     * 推荐数据
     */
    private void searchRecommendData() {
        NetWork.getRetrofit()
                .create(ImpService.class)
                .SearchRecommendData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SearchRecommendBean>() {
                    private SearchRecommendBean searchRecommendBean;

                    @Override
                    public void onCompleted() {
                        if ("0".equals(searchRecommendBean.getCode())) {
                            final List<SearchRecommendBean.DataBean> data = searchRecommendBean.getData();
                            mFlowLayout.setAdapter(new TagAdapter<SearchRecommendBean.DataBean>(data) {

                                @Override
                                public View getView(FlowLayout parent, int position, SearchRecommendBean.DataBean dataBean) {
                                    TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.flow_item_layout, mFlowLayout, false);
                                    tv.setText(dataBean.getGoods_name());
                                    return tv;
                                }
                            });
                            mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                                @Override
                                public boolean onTagClick(View view, int position, FlowLayout parent) {
                                    goods_name = data.get(position).getGoods_name();
                                    ed_title_search.setText(goods_name);
                                    getData();
                                    return true;
                                }
                            });
//                            mFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
//                                @Override
//                                public void onSelected(Set<Integer> selectPosSet) {
//                                    showToastMessage("choose:" + selectPosSet.toString());
//                                }
//                            });
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(SearchRecommendBean searchRecommendBean) {
                        this.searchRecommendBean = searchRecommendBean;
                    }
                });
    }

    /**
     * 获取历史记录的数据
     */
    private void searchHistoryData() {
        customDialog.show();
        NetWork.getRetrofit()
                .create(ImpService.class)
                .SearchHistoryData(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SearchHistoryBean>() {
                    private SearchHistoryBean searchHistoryBean;

                    @Override
                    public void onCompleted() {
                        customDialog.dismiss();
                        if ("0".equals(searchHistoryBean.getCode())) {
                            final List<SearchHistoryBean.DataBean> data = searchHistoryBean.getData();
                            if (data.size() <= 0) {
                                ll_is_gone.setVisibility(View.GONE);
                                return;
                            }

                            mFlowLayoutRecord.setAdapter(new TagAdapter<SearchHistoryBean.DataBean>(data) {

                                @Override
                                public View getView(FlowLayout parent, int position, SearchHistoryBean.DataBean dataBean) {
                                    TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.flow_item_layout, mFlowLayoutRecord, false);
                                    tv.setText(dataBean.getGoods_name());
                                    return tv;
                                }
                            });
                            mFlowLayoutRecord.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                                @Override
                                public boolean onTagClick(View view, int position, FlowLayout parent) {
                                    goods_name = data.get(position).getGoods_name();
                                    ed_title_search.setText(goods_name);
                                    getData();
                                    return true;
                                }
                            });
//                            mFlowLayoutRecord.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
//                                @Override
//                                public void onSelected(Set<Integer> selectPosSet) {
//                                    showToastMessage("choose:" + selectPosSet.toString());
//                                }
//                            });
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(SearchHistoryBean searchHistoryBean) {
                        this.searchHistoryBean = searchHistoryBean;
                    }
                });
    }

    private void initView() {
        img_back = getViewById(R.id.img_back);
        ed_title_search = getViewById(R.id.ed_title_search);
        tv_delete = getViewById(R.id.tv_delete);
        tv_title_right = getViewById(R.id.tv_title_right);
        tv_title_right.setVisibility(View.VISIBLE);
        tv_title_right.setText("搜索");
        ll_is_gone = getViewById(R.id.ll_is_gone);
        ll_no_hava_data = getViewById(R.id.ll_no_hava_data);
        mFlowLayout = getViewById(R.id.id_flowlayout);
        mFlowLayoutRecord = getViewById(R.id.flowlayout_record);

        ll_search_view = getViewById(R.id.ll_search_view);
        ll_goods_view = getViewById(R.id.ll_goods_view);
        tv_colligate = getViewById(R.id.tv_colligate);
        tv_sales_volume = getViewById(R.id.tv_sales_volume);
        tv_price = getViewById(R.id.tv_price);
        rg_up_down = getViewById(R.id.rg_up_down);
        rb_up = getViewById(R.id.rb_up);
        rb_down = getViewById(R.id.rb_down);
        tv_colligate.setTextColor(mContext.getResources().getColor(R.color.btn_color));
        rg_up_down.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_up:
                        tv_colligate.setTextColor(mContext.getResources().getColor(R.color.black));
                        tv_sales_volume.setTextColor(mContext.getResources().getColor(R.color.black));
                        tv_price.setTextColor(mContext.getResources().getColor(R.color.btn_color));
                        isUp = false;
                        break;
                    case R.id.rb_down:
                        tv_colligate.setTextColor(mContext.getResources().getColor(R.color.black));
                        tv_sales_volume.setTextColor(mContext.getResources().getColor(R.color.black));
                        tv_price.setTextColor(mContext.getResources().getColor(R.color.btn_color));
                        isUp = true;
                        break;
                }
            }
        });

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (LuRecyclerView) findViewById(R.id.list);
        //设置刷新时动画的颜色，可以设置4个
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setProgressViewOffset(false, 0, AppUtil.dip2px(this, 48));
            mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_color);
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }
        mDataAdapter = new SearchDataAdapter(mContext);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mLuRecyclerViewAdapter = new LuRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(mLuRecyclerViewAdapter);
        LuDividerDecoration divider = new LuDividerDecoration.Builder(this, mLuRecyclerViewAdapter)
                .setHeight(R.dimen.default_divider_height)
                .setPadding(R.dimen.default_divider_padding)
                .setColorResource(R.color.transparent)
                .build();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(divider);
        mLuRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                SesarchBean.DataBean dataBean = mDataAdapter.getDataList().get(position);
                String gid = String.valueOf(dataBean.getGid());
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("gid", String.valueOf(gid));
                intent.putExtra("uid", uid);
                String  url = NetWork.H5BaseUrl+"commodityDetails?gid="+gid+"&uid="+ uid+"&flag=1";
                intent.putExtra("url", url);
                startActivity(intent);
//                finish();
            }
        });
        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (sesarchBean != null) {
                    if (page < sesarchBean.getLast_page()) {
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
        if (!TextUtils.isEmpty(bid) && bid != null) {
            ll_search_view.setVisibility(View.GONE);
            onRefresh();
        }
    }

    private void initListener() {
        img_back.setOnClickListener(this);
        tv_title_right.setOnClickListener(this);
        tv_colligate.setOnClickListener(this);
        tv_sales_volume.setOnClickListener(this);
        tv_delete.setOnClickListener(this);
        tv_price.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                AppUtil.colsePhoneKeyboard(this);
                finish();
                break;
            case R.id.tv_title_right:
                page = 1;
                if (bid!=null||!"".equals(bid)){
                    bid=null;
                }
                getData();
                break;
            case R.id.tv_delete:
                deleteHistoryData();
                break;

            case R.id.tv_colligate:
                type=1;
                rb_up.setChecked(false);
                rb_down.setChecked(false);
                tv_colligate.setTextColor(mContext.getResources().getColor(R.color.btn_color));
                tv_sales_volume.setTextColor(mContext.getResources().getColor(R.color.black));
                tv_price.setTextColor(mContext.getResources().getColor(R.color.black));
                onRefresh();
                break;
            case R.id.tv_sales_volume:
                type=2;
                rb_up.setChecked(false);
                rb_down.setChecked(false);
                tv_sales_volume.setTextColor(mContext.getResources().getColor(R.color.btn_color));
                tv_colligate.setTextColor(mContext.getResources().getColor(R.color.black));
                tv_price.setTextColor(mContext.getResources().getColor(R.color.black));
                onRefresh();
                break;
            case R.id.tv_price:
                tv_price.setTextColor(mContext.getResources().getColor(R.color.btn_color));
                tv_colligate.setTextColor(mContext.getResources().getColor(R.color.black));
                tv_sales_volume.setTextColor(mContext.getResources().getColor(R.color.black));
                if (isUp) {
                    type=3;
                    isUp = false;
                    rb_up.setChecked(true);
                    rb_down.setChecked(false);
                } else {
                    type=4;
                    isUp = true;
                    rb_up.setChecked(false);
                    rb_down.setChecked(true);
                }
                onRefresh();
                break;
        }
    }

    /**
     * 删除历史记录
     */
    private void deleteHistoryData() {
        NetWork.getRetrofit()
                .create(ImpService.class)
                .DeleteHistoryData(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DeleteHistoryBean>() {
                    private DeleteHistoryBean deleteHistoryBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(deleteHistoryBean.getMsg());
                        if ("0".equals(deleteHistoryBean.getCode())) {
                            mFlowLayoutRecord.removeAllViews();
                            ll_is_gone.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(DeleteHistoryBean deleteHistoryBean) {
                        this.deleteHistoryBean = deleteHistoryBean;
                    }
                });
    }

    /**
     * 搜索数据提交
     */
    private void getData() {
        if (TextUtils.isEmpty(ed_title_search.getText().toString().trim())) {
            showToastMessage("搜索条件不能为空");
            return;
        }
        mDataAdapter.clear();
        customDialog.show();
        goods_name = ed_title_search.getText().toString().trim();
        NetWork.getRetrofit()
                .create(ImpService.class)
                .SesarchData(uid, goods_name, String.valueOf(page),String.valueOf(type))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SesarchBean>() {
                    private SesarchBean sesarchBean;

                    @Override
                    public void onCompleted() {
                        customDialog.dismiss();
                        showToastMessage(sesarchBean.getMsg());
                        if ("0".equals(sesarchBean.getCode())) {
                            int size = sesarchBean.getData().size();
                            if (size <= 0) {
                                ll_no_hava_data.setVisibility(View.VISIBLE);
                                ll_search_view.setVisibility(View.GONE);
                                ll_goods_view.setVisibility(View.GONE);
                            } else {
                                ll_no_hava_data.setVisibility(View.GONE);
                                ll_search_view.setVisibility(View.GONE);
                                ll_goods_view.setVisibility(View.VISIBLE);

                                showData(sesarchBean);
                                List<SesarchBean.DataBean> data = sesarchBean.getData();
                                mDataAdapter.addAll(data);
                                if (mSwipeRefreshLayout.isRefreshing()) {
                                    mSwipeRefreshLayout.setRefreshing(false);
                                }
                                mRecyclerView.refreshComplete(data.size());
                                notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(SesarchBean sesarchBean) {
                        this.sesarchBean = sesarchBean;
                    }
                });

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
        private WeakReference<SearchDataActivity> ref;

        PreviewHandler(SearchDataActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final SearchDataActivity activity = ref.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            switch (msg.what) {
                case -1:
                    if (activity.mSwipeRefreshLayout.isRefreshing()) {
                        activity.mDataAdapter.clear();
                    }
                    if (!TextUtils.isEmpty(bid) && bid != null) {
                        getBidData();
                    } else {
                        getData();
                    }
                    break;
                case -3:
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        activity.mSwipeRefreshLayout.setRefreshing(false);
                        activity.mRecyclerView.refreshComplete(sesarchBean.getData().size());
                        activity.notifyDataSetChanged();
                    } else {
                        activity.mRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                            @Override
                            public void reload() {
                                activity.mRecyclerView.refreshComplete(sesarchBean.getData().size());
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

    /**
     * 根据bid获取数据
     */
    private void getBidData() {
        customDialog.show();
        NetWork.getRetrofit().create(ImpService.class)
                .BidSearchData(bid, String.valueOf(page),String.valueOf(type))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SesarchBean>() {
                    private SesarchBean sesarchBean;

                    @Override
                    public void onCompleted() {
                        customDialog.dismiss();
                        if ("0".equals(sesarchBean.getCode())) {
                            int size = sesarchBean.getData().size();
                            if (size <= 0) {
                                ll_no_hava_data.setVisibility(View.VISIBLE);
                                ll_search_view.setVisibility(View.GONE);
                                ll_goods_view.setVisibility(View.GONE);
                            } else {
                                ll_no_hava_data.setVisibility(View.GONE);
                                ll_search_view.setVisibility(View.GONE);
                                ll_goods_view.setVisibility(View.VISIBLE);

                                showData(sesarchBean);
                                List<SesarchBean.DataBean> data = sesarchBean.getData();
                                mDataAdapter.addAll(data);
                                if (mSwipeRefreshLayout.isRefreshing()) {
                                    mSwipeRefreshLayout.setRefreshing(false);
                                }
                                mRecyclerView.refreshComplete(data.size());
                                notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(SesarchBean sesarchBean) {

                        this.sesarchBean = sesarchBean;
                    }
                });
    }

    private void notifyDataSetChanged() {
        mLuRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void showData(SesarchBean sesarchBean) {
        this.sesarchBean = sesarchBean;
    }
}
