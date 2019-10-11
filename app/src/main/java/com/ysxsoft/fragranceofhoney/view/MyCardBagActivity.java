package com.ysxsoft.fragranceofhoney.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.ItemDecoration.LuDividerDecoration;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnItemLongClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.ysxsoft.fragranceofhoney.R;
import com.ysxsoft.fragranceofhoney.adapter.MyCardBagAdapter;
import com.ysxsoft.fragranceofhoney.impservice.ImpService;
import com.ysxsoft.fragranceofhoney.modle.BankCardListBean;
import com.ysxsoft.fragranceofhoney.modle.DeleteCardBean;
import com.ysxsoft.fragranceofhoney.utils.AppUtil;
import com.ysxsoft.fragranceofhoney.utils.BaseActivity;
import com.ysxsoft.fragranceofhoney.utils.NetWork;
import com.ysxsoft.fragranceofhoney.widget.LongDialog;

import java.lang.ref.WeakReference;
import java.security.IdentityScope;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MyCardBagActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private View img_back;
    private FrameLayout fl_add_bank_card, fl_have_data;
    private LinearLayout ll_no_have;
    private String uid, withdraw_cash;
    private TextView tv_title_right;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LuRecyclerView mRecyclerView = null;
    private LuRecyclerViewAdapter mLuRecyclerViewAdapter = null;
    private int page = 1;
    private MyCardBagAdapter mDataAdapter;
    private PreviewHandler mHandler = new PreviewHandler(this);
    private BankCardListBean bankCardListBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_card_bag_layout);
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        withdraw_cash = intent.getStringExtra("withdraw_cash");
        initView();
        initListener();
    }

    private void initView() {
        img_back = getViewById(R.id.img_back);
        TextView tv_title = getViewById(R.id.tv_title);
        tv_title.setText("我的卡包");
        tv_title_right = getViewById(R.id.tv_title_right);
        tv_title_right.setText("添加");
        ll_no_have = getViewById(R.id.ll_no_have);
        fl_have_data = getViewById(R.id.fl_have_data);
        fl_add_bank_card = getViewById(R.id.fl_add_bank_card);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (LuRecyclerView) findViewById(R.id.list);
        //设置刷新时动画的颜色，可以设置4个
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setProgressViewOffset(false, 0, AppUtil.dip2px(this, 48));
            mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_color);
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }
        mDataAdapter = new MyCardBagAdapter(mContext);
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
        mLuRecyclerViewAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                final int pid = mDataAdapter.getDataList().get(position).getPid();
                final LongDialog dialog = new LongDialog(mContext);
                TextView tv_delete = dialog.findViewById(R.id.tv_delete);
                tv_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteData(String.valueOf(pid), dialog);
                    }
                });
                dialog.show();
            }
        });
        mLuRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if ("withdraw_cash".equals(withdraw_cash) && !TextUtils.isEmpty(withdraw_cash) && withdraw_cash != null) {
                    BankCardListBean.DataBean dataBean = mDataAdapter.getDataList().get(position);
                    String house = dataBean.getHouse();
                    String number = dataBean.getNumber();
                    int pid = dataBean.getPid();
                    Intent intent = new Intent();
                    intent.putExtra("house", house);
                    intent.putExtra("pid", String.valueOf(pid));
                    intent.putExtra("number", number);
                    setResult(1646, intent);
                    finish();
                }
            }
        });
        mDataAdapter.setOnEditorListener(new MyCardBagAdapter.OnEditorListener() {
            @Override
            public void editorClick(int position) {
                BankCardListBean.DataBean dataBean = mDataAdapter.getDataList().get(position);
                int pid = dataBean.getPid();
                String realname = dataBean.getRealname();
                String house = dataBean.getHouse();
                String idcard = dataBean.getIdcard();
                String number = dataBean.getNumber();
                String phone = dataBean.getPhone();
                Intent intent = new Intent(mContext, EditorMyCardBagActivity.class);
                intent.putExtra("uid", uid);
                intent.putExtra("pid", pid);
                intent.putExtra("realname", realname);
                intent.putExtra("house", house);
                intent.putExtra("idcard", idcard);
                intent.putExtra("number", number);
                intent.putExtra("phone", phone);
                startActivity(intent);

            }
        });

        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (bankCardListBean != null) {
                    if (page < bankCardListBean.getLast_page()) {
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
     * 删除
     *
     * @param pid
     * @param dialog
     */
    private void deleteData(String pid, final LongDialog dialog) {
        NetWork.getRetrofit()
                .create(ImpService.class)
                .DeleteCardData(pid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DeleteCardBean>() {
                    private DeleteCardBean deleteCardBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(deleteCardBean.getMsg());
                        if ("0".equals(deleteCardBean.getCode())) {
                            onRefresh();
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(DeleteCardBean deleteCardBean) {
                        this.deleteCardBean = deleteCardBean;
                    }
                });
    }

    private void initListener() {
        img_back.setOnClickListener(this);
        tv_title_right.setOnClickListener(this);
        ll_no_have.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.ll_no_have:
                Intent intent1 = new Intent(mContext, EditorMyCardBagActivity.class);
                intent1.putExtra("uid", uid);
                startActivity(intent1);
                break;
            case R.id.tv_title_right:
                Intent intent = new Intent(mContext, EditorMyCardBagActivity.class);
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
        private WeakReference<MyCardBagActivity> ref;

        PreviewHandler(MyCardBagActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final MyCardBagActivity activity = ref.get();
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
                        activity.mRecyclerView.refreshComplete(bankCardListBean.getData().size());
                        activity.notifyDataSetChanged();
                    } else {
                        activity.mRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                            @Override
                            public void reload() {
                                activity.mRecyclerView.refreshComplete(bankCardListBean.getData().size());
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
                .BankCardListData(uid, String.valueOf(page))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BankCardListBean>() {
                    private BankCardListBean bankCardListBean;

                    @Override
                    public void onCompleted() {
                        if ("0".equals(bankCardListBean.getCode())) {
                            showData(bankCardListBean);
                            List<BankCardListBean.DataBean> data = bankCardListBean.getData();
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
                    public void onNext(BankCardListBean bankCardListBean) {
                        this.bankCardListBean = bankCardListBean;
                    }
                });

    }

    private void showData(BankCardListBean bankCardListBean) {
        this.bankCardListBean = bankCardListBean;
    }

    @Override
    protected void onResume() {
        super.onResume();
        RequestBankData();
        onRefresh();
    }

    private void RequestBankData() {
        NetWork.getRetrofit()
                .create(ImpService.class)
                .BankCardListData(uid, String.valueOf(page))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BankCardListBean>() {
                    private BankCardListBean bankCardListBean;

                    @Override
                    public void onCompleted() {
                        if ("0".equals(bankCardListBean.getCode())) {
                            int size = bankCardListBean.getData().size();
                            if (size <= 0) {
                                fl_add_bank_card.setVisibility(View.VISIBLE);
                                fl_have_data.setVisibility(View.GONE);
                                tv_title_right.setVisibility(View.GONE);
                            } else {
                                tv_title_right.setVisibility(View.VISIBLE);
                                fl_add_bank_card.setVisibility(View.GONE);
                                fl_have_data.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(BankCardListBean bankCardListBean) {
                        this.bankCardListBean = bankCardListBean;
                    }
                });
    }

}
