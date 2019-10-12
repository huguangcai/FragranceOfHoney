package com.ysxsoft.fragranceofhoney.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.ItemDecoration.LuDividerDecoration;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.ysxsoft.fragranceofhoney.R;
import com.ysxsoft.fragranceofhoney.adapter.AddressManagerAdapter;
import com.ysxsoft.fragranceofhoney.impservice.ImpService;
import com.ysxsoft.fragranceofhoney.modle.DelMyAreaBean;
import com.ysxsoft.fragranceofhoney.modle.GetGoodsAddressBean;
import com.ysxsoft.fragranceofhoney.modle.IsTrueAddressBean;
import com.ysxsoft.fragranceofhoney.utils.AppUtil;
import com.ysxsoft.fragranceofhoney.utils.BaseActivity;
import com.ysxsoft.fragranceofhoney.utils.NetWork;

import java.lang.ref.WeakReference;
import java.security.IdentityScope;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class GetGoodsAddressActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private View img_back;
    private Button btn_submit;
    private TextView tv_new_add_address;
    private LinearLayout ll_no_address;
    private FrameLayout fl_have_address;
    private String uid;
    private TextView tv_title;
    private int page = 1;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LuRecyclerView mRecyclerView = null;
    private LuRecyclerViewAdapter mLuRecyclerViewAdapter = null;
    private AddressManagerAdapter mDataAdapter;
    private PreviewHandler mHandler = new PreviewHandler(this);
    private GetGoodsAddressBean getGoodsAddressBean;
    private int is_check = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_goods_address_layout);
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        initView();
        initListener();
    }

    private void requestAddressData() {
        NetWork.getRetrofit()
                .create(ImpService.class)
                .getGoodsAddressData(uid, String.valueOf(page))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetGoodsAddressBean>() {
                    private GetGoodsAddressBean getGoodsAddressBean;

                    @Override
                    public void onCompleted() {
                        if ("0".equals(getGoodsAddressBean.getCode())) {
                            int size = getGoodsAddressBean.getData().size();
                            if (size <= 0) {
                                ll_no_address.setVisibility(View.VISIBLE);
                                fl_have_address.setVisibility(View.GONE);
                                tv_title.setText("收货地址");
                            } else {
                                ll_no_address.setVisibility(View.GONE);
                                fl_have_address.setVisibility(View.VISIBLE);
                                tv_title.setText("地址管理");
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(GetGoodsAddressBean getGoodsAddressBean) {
                        this.getGoodsAddressBean = getGoodsAddressBean;
                    }
                });

    }

    private void initView() {
        img_back = getViewById(R.id.img_back);
        tv_title = getViewById(R.id.tv_title);
        tv_title.setText("收货地址");
        ll_no_address = getViewById(R.id.ll_no_address);
        btn_submit = getViewById(R.id.btn_submit);
        fl_have_address = getViewById(R.id.fl_have_address);
        tv_new_add_address = getViewById(R.id.tv_new_add_address);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (LuRecyclerView) findViewById(R.id.list);
        //设置刷新时动画的颜色，可以设置4个
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setProgressViewOffset(false, 0, AppUtil.dip2px(this, 48));
            mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_color);
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }
        mDataAdapter = new AddressManagerAdapter(this);
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
        mDataAdapter.setOnDeleteClickListener(new AddressManagerAdapter.OnDeleteClickListener() {
            @Override
            public void deleteItem(int position) {
                GetGoodsAddressBean.DataBean dataBean = mDataAdapter.getDataList().get(position);
                int aid = dataBean.getAid();
                int is_ture = dataBean.getIs_ture();
                if (is_ture==0){
                    showToastMessage("默认地址不能删除");
                }else {
                    DeleteItemData(aid);
                }
            }

            @Override
            public void editorItem(int position) {
                GetGoodsAddressBean.DataBean dataBean = mDataAdapter.getDataList().get(position);
                int aid = dataBean.getAid();
                String provice = dataBean.getProvice();
                String city = dataBean.getCity();
                String area = dataBean.getArea();
                String address = dataBean.getAddress();
                String linkname = dataBean.getLinkname();
                String phone = dataBean.getPhone();
                int is_ture = dataBean.getIs_ture();
                Intent intent = new Intent(mContext, AddressManagerActivity.class);
                intent.putExtra("uid", uid);
                intent.putExtra("aid", aid);
                intent.putExtra("provice", provice);
                intent.putExtra("city", city);
                intent.putExtra("area", area);
                intent.putExtra("address", address);
                intent.putExtra("linkname", linkname);
                intent.putExtra("phone", phone);
                intent.putExtra("is_ture", is_ture);
                startActivity(intent);
            }
        });
        mLuRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            int currentNum = -1;

            @Override
            public void onItemClick(View view, int position) {
                if (mDataAdapter.getDataList().get(position).getIs_ture() == 0) {
                    return;
                }
                for (GetGoodsAddressBean.DataBean data : mDataAdapter.getDataList()) {
                    data.setIs_ture(1);
                }
                if (currentNum == -1) { //选中
                    mDataAdapter.getDataList().get(position).setIs_ture(0);
                    currentNum = position;
                } else if (currentNum == position) { //同一个item选中变未选中
                    for (GetGoodsAddressBean.DataBean date : mDataAdapter.getDataList()) { //遍历list集合中的数据
                        date.setIs_ture(1);//全部设为未选中
                    }
                    currentNum = -1;
                } else if (currentNum != position) { //不是同一个item选中当前的，去除上一个选中的
                    for (GetGoodsAddressBean.DataBean date : mDataAdapter.getDataList()) { //遍历list集合中的数据
                        date.setIs_ture(1);//全部设为未选中
                    }
                    mDataAdapter.getDataList().get(position).setIs_ture(0);
                    currentNum = position;
                }
                isSettingNormal(position);
                mDataAdapter.notifyDataSetChanged();
                mLuRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (getGoodsAddressBean != null) {
                    if (page < getGoodsAddressBean.getLast_page()) {
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
     * 删除地址
     * @param aid
     */
    private void DeleteItemData(int aid) {
        NetWork.getRetrofit()
                .create(ImpService.class)
                .DelMyAreaData(String.valueOf(aid))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DelMyAreaBean>() {
                    private DelMyAreaBean delMyAreaBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(delMyAreaBean.getMsg());
                        if ("0".equals(delMyAreaBean.getCode())){
                            onRefresh();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(DelMyAreaBean delMyAreaBean) {
                        this.delMyAreaBean = delMyAreaBean;
                    }
                });


    }

    /**
     * 是否设置默认
     *
     * @param position
     */
    private void isSettingNormal(int position) {
        NetWork.getRetrofit()
                .create(ImpService.class)
                .IsTrueAddressData(uid,
                        String.valueOf(mDataAdapter.getDataList().get(position).getAid()),
                        String.valueOf(is_check))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<IsTrueAddressBean>() {
                    private IsTrueAddressBean isTrueAddressBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(isTrueAddressBean.getMsg());
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(IsTrueAddressBean isTrueAddressBean) {
                        this.isTrueAddressBean = isTrueAddressBean;
                    }
                });

    }

    private void initListener() {
        img_back.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        tv_new_add_address.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_submit:
                Intent intent1 = new Intent(mContext, AddressManagerActivity.class);
                intent1.putExtra("uid", uid);
                startActivity(intent1);
                break;
            case R.id.tv_new_add_address:
                Intent intent = new Intent(mContext, AddressManagerActivity.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
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
        private WeakReference<GetGoodsAddressActivity> ref;

        PreviewHandler(GetGoodsAddressActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final GetGoodsAddressActivity activity = ref.get();
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
                        activity.mRecyclerView.refreshComplete(getGoodsAddressBean.getData().size());
                        activity.notifyDataSetChanged();
                    } else {
                        activity.mRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                            @Override
                            public void reload() {
                                activity.mRecyclerView.refreshComplete(getGoodsAddressBean.getData().size());
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

    private void notifyDataSetChanged() {
        mLuRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void getData() {
        NetWork.getRetrofit()
                .create(ImpService.class)
                .getGoodsAddressData(uid, String.valueOf(page))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetGoodsAddressBean>() {
                    private GetGoodsAddressBean getGoodsAddressBean;

                    @Override
                    public void onCompleted() {
                        if ("0".equals(getGoodsAddressBean.getCode())) {
                            showData(getGoodsAddressBean);
                            List<GetGoodsAddressBean.DataBean> data = getGoodsAddressBean.getData();
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
                    public void onNext(GetGoodsAddressBean getGoodsAddressBean) {
                        this.getGoodsAddressBean = getGoodsAddressBean;
                    }
                });


    }

    private void showData(GetGoodsAddressBean getGoodsAddressBean) {
        this.getGoodsAddressBean = getGoodsAddressBean;
    }

    @Override
    protected void onResume() {
        super.onResume();
        onRefresh();
        requestAddressData();
    }
}
