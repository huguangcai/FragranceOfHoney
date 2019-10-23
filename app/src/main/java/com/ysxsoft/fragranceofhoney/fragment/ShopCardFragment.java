package com.ysxsoft.fragranceofhoney.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jdsjlzx.ItemDecoration.LuDividerDecoration;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.ysxsoft.fragranceofhoney.R;
import com.ysxsoft.fragranceofhoney.adapter.ShopCardAdapter;
import com.ysxsoft.fragranceofhoney.impservice.ImpService;
import com.ysxsoft.fragranceofhoney.modle.DeleteShopCardBean;
import com.ysxsoft.fragranceofhoney.modle.EditcarBean;
import com.ysxsoft.fragranceofhoney.modle.SettlementBean;
import com.ysxsoft.fragranceofhoney.modle.ShopCardBalanceBean;
import com.ysxsoft.fragranceofhoney.modle.ShopCardBean;
import com.ysxsoft.fragranceofhoney.utils.AppUtil;
import com.ysxsoft.fragranceofhoney.utils.NetWork;
import com.ysxsoft.fragranceofhoney.view.WebViewActivity;
import com.ysxsoft.fragranceofhoney.widget.TextNumDialog;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 购物车的Fragment
 */
public class ShopCardFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, ShopCardAdapter.ModifyCountInterface, ShopCardAdapter.CheckInterface {
    private boolean isManager = false;
    private View img_back;
    private TextView tv_title_right, tv_goods_num, tv_goods_sum_money, tv_delete;
    private CheckBox cb_box;
    private LinearLayout ll_goods_sum, ll_no_hava_data;
    private int stateBar;
    private FrameLayout fl_have_data;
    private String uid;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LuRecyclerView mRecyclerView = null;
    private LuRecyclerViewAdapter mLuRecyclerViewAdapter = null;
    private ShopCardAdapter mDataAdapter;
    private ShopCardBean shopCardBean;
    private PreviewHandler mHandler = new PreviewHandler();
    private double totalPrice = 0.00;// 购买的商品总价
    private int totalCount = 0;// 购买的商品总数量

    private StringBuffer sectionDelete = new StringBuffer();
    private StringBuffer sectionPrice = new StringBuffer();
    private StringBuffer sectionNum = new StringBuffer();
    private StringBuffer pids = new StringBuffer();
    private String shopCardId, shopCardPriceId, shopCardNumId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shop_card_layout, null);
        SharedPreferences sp = getActivity().getSharedPreferences("UID", Context.MODE_PRIVATE);
        uid = sp.getString("uid", "");
        stateBar = getStateBar();
        initView(view);
        initListener();
        return view;
    }

    private void initView(View view) {
        RelativeLayout ll_title = view.findViewById(R.id.ll_title);
        ll_title.setPadding(0, stateBar, 0, 0);
        img_back = view.findViewById(R.id.img_back);
        img_back.setVisibility(View.INVISIBLE);
        TextView tv_title = view.findViewById(R.id.tv_title);
        tv_title.setText("购物车");
        tv_title_right = view.findViewById(R.id.tv_title_right);
//        tv_title_right.setVisibility(View.VISIBLE);
        tv_title_right.setText("编辑");
        cb_box = view.findViewById(R.id.cb_box);
        tv_goods_num = view.findViewById(R.id.tv_goods_num);
        tv_goods_sum_money = view.findViewById(R.id.tv_goods_sum_money);
        tv_delete = view.findViewById(R.id.tv_delete);
        ll_goods_sum = view.findViewById(R.id.ll_goods_sum);
        fl_have_data = view.findViewById(R.id.fl_have_data);
        ll_no_hava_data = view.findViewById(R.id.ll_no_hava_data);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (LuRecyclerView) view.findViewById(R.id.list);
        //设置刷新时动画的颜色，可以设置4个
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setProgressViewOffset(false, 0, AppUtil.dip2px(getActivity(), 48));
            mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_color);
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }
        mDataAdapter = new ShopCardAdapter(getActivity());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mLuRecyclerViewAdapter = new LuRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(mLuRecyclerViewAdapter);
        LuDividerDecoration divider = new LuDividerDecoration.Builder(getActivity(), mLuRecyclerViewAdapter)
                .setHeight(R.dimen.default_divider_height)
                .setPadding(R.dimen.default_divider_padding)
                .setColorResource(R.color.gray)
                .build();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(divider);
//        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                if (shopCardBean != null) {
//                    if (page < shopCardBean.getLast_page()) {
//                        page++;
//                        requestData();
//                    } else {
//                        //the end
//                        mRecyclerView.setNoMore(true);
//                    }
//                }
//            }
//        });
        //设置底部加载颜色
        mRecyclerView.setFooterViewColor(R.color.btn_color, R.color.black, android.R.color.transparent);
        //设置底部加载文字提示
        mRecyclerView.setFooterViewHint("拼命加载中", "没有更多数据了", "网络不给力啊，点击再试一次吧");
    }

    private void initListener() {
        img_back.setOnClickListener(this);
        tv_title_right.setOnClickListener(this);
        tv_delete.setOnClickListener(this);
        cb_box.setOnClickListener(this);
        mDataAdapter.setCheckInterface(this);
        mDataAdapter.setModifyCountInterface(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_title_right:
                if (isManager) {
                    isManager = false;
                    tv_title_right.setText("编辑");
                    ll_goods_sum.setVisibility(View.VISIBLE);
                    tv_delete.setText("结算");
                } else {
                    isManager = true;
                    tv_title_right.setText("完成");
                    ll_goods_sum.setVisibility(View.INVISIBLE);
                    tv_delete.setText("删除");
                }
                break;
            case R.id.cb_box:
                if (mDataAdapter.getDataList().size() != 0) {
                    if (cb_box.isChecked()) {
                        for (int i = 0; i < mDataAdapter.getDataList().size(); i++) {
                            mDataAdapter.getDataList().get(i).setChoosed(true);
                        }
                        notifyDataSetChanged();
                    } else {
                        for (int i = 0; i < mDataAdapter.getDataList().size(); i++) {
                            mDataAdapter.getDataList().get(i).setChoosed(false);
                        }
                        notifyDataSetChanged();
                    }
                }
                statistics();
                break;
            case R.id.tv_delete:
                if (Integer.valueOf(tv_goods_num.getText().toString()) == 0) {
                    Toast.makeText(getActivity(), "所选商品不能为零", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (sectionDelete.length() != 0) {
                    sectionDelete.setLength(0);
                }
                if (sectionPrice.length() != 0) {
                    sectionPrice.setLength(0);
                }
                if (sectionNum.length() != 0) {
                    sectionNum.setLength(0);
                }
                for (ShopCardBean.DataBean dataBean : mDataAdapter.getDataList()) {
                    boolean choosed = dataBean.isChoosed();
                    if (choosed) {
                        sectionDelete.append(String.valueOf(dataBean.getId())).append(",");
                        sectionPrice.append(String.valueOf(dataBean.getPrice())).append(",");
                        sectionNum.append(String.valueOf(dataBean.getNumber())).append(",");
                    }
                }

                shopCardId = sectionDelete.deleteCharAt(sectionDelete.length() - 1).toString();
                shopCardPriceId = sectionPrice.deleteCharAt(sectionPrice.length() - 1).toString();
                shopCardNumId = sectionNum.deleteCharAt(sectionNum.length() - 1).toString();
                if (isManager) {
                    if (cb_box.isChecked()) {//删除所有
                        allDelete();
                    } else {//删除部分
                        sectionDelete();
                    }
                } else {
//                    BalanceData();
                    NewBalanceData();
                }
                break;
        }
    }

    private void NewBalanceData() {
        NetWork.getService(ImpService.class)
                .settlement(uid, shopCardId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SettlementBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(SettlementBean settlementBean) {
                        if (settlementBean.getCode() == 0) {
                            Intent intent = new Intent(getActivity(), WebViewActivity.class);
                            if (pids.length() != 0) {
                                pids.setLength(0);
                            }
                            for (int i = 0; i < settlementBean.getData().getList().size(); i++) {
                                String pid = settlementBean.getData().getList().get(i).getGoods_id();
                                pids.append(pid).append(",");
                            }
                            String pid = pids.deleteCharAt(pids.length() - 1).toString();
//                            String url = NetWork.H5BaseUrl + "confirmOrder?sc=2&pid="+pid;
                            String url = NetWork.H5BaseUrl + "confirmOrder?sc=2&pid=" + shopCardId;
                            intent.putExtra("uid", uid);
                            intent.putExtra("url", url);
                            getActivity().startActivity(intent);
                        }
                    }
                });
    }

    /**
     * 结算
     */
    private void BalanceData() {
        NetWork.getService(ImpService.class)
                .ShopCardBalanceData(shopCardId, uid, shopCardNumId, shopCardPriceId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ShopCardBalanceBean>() {
                    private ShopCardBalanceBean shopCardBalanceBean;

                    @Override
                    public void onCompleted() {
                        if ("0".equals(shopCardBalanceBean.getCode())) {
                            Intent intent = new Intent(getActivity(), WebViewActivity.class);
                            if (pids.length() != 0) {
                                pids.setLength(0);
                            }
                            for (int i = 0; i < shopCardBalanceBean.getData().size(); i++) {
                                String pid = shopCardBalanceBean.getData().get(i).getId();
                                pids.append(pid).append(",");
                            }
                            String pid = pids.deleteCharAt(pids.length() - 1).toString();
                            String url = NetWork.H5BaseUrl + "confirmOrder?sc=2&pid=" + pid;
                            intent.putExtra("uid", uid);
                            intent.putExtra("url", url);
                            getActivity().startActivity(intent);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getActivity(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(ShopCardBalanceBean shopCardBalanceBean) {
                        this.shopCardBalanceBean = shopCardBalanceBean;
                    }
                });

    }

    /**
     * 部分删除
     */
    private void sectionDelete() {
        NetWork.getService(ImpService.class)
                .SectionDeleteShopCardData(shopCardId, uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DeleteShopCardBean>() {
                    private DeleteShopCardBean deleteShopCardBean;

                    @Override
                    public void onCompleted() {
                        Toast.makeText(getActivity(), deleteShopCardBean.getMsg(), Toast.LENGTH_SHORT).show();
                        if ("0".equals(deleteShopCardBean.getCode())) {
                            onRefresh();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(DeleteShopCardBean deleteShopCardBean) {
                        this.deleteShopCardBean = deleteShopCardBean;
                    }
                });
    }

    /**
     * 删除购物车所有数据
     */
    private void allDelete() {
        NetWork.getService(ImpService.class)
                .DeleteShopCardData(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DeleteShopCardBean>() {
                    private DeleteShopCardBean deleteShopCardBean;

                    @Override
                    public void onCompleted() {
                        Toast.makeText(getActivity(), deleteShopCardBean.getMsg(), Toast.LENGTH_SHORT).show();
                        if ("0".equals(deleteShopCardBean.getCode())) {
                            onRefresh();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(DeleteShopCardBean deleteShopCardBean) {
                        this.deleteShopCardBean = deleteShopCardBean;
                    }
                });

    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    protected int getStateBar() {
        int result = 0;
        int resourceId = this.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = this.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

//    public void setUid(String uid) {
//        this.uid = uid;
//    }

    @Override
    public void onRefresh() {
        if (cb_box.isChecked()) {
            cb_box.setChecked(false);
        }
        tv_goods_num.setText("0");
        tv_goods_sum_money.setText("0.0");
        mSwipeRefreshLayout.setRefreshing(true);
        mRecyclerView.setRefreshing(true);//同时调用LuRecyclerView的setRefreshing方法
        requestData();
    }

    private void requestData() {
        //判断网络是否可用
        if (AppUtil.isNetworkAvaiable(getActivity())) {
            mHandler.sendEmptyMessage(-1);
        } else {
            mHandler.sendEmptyMessage(-3);
        }
    }

    /**
     * 单选
     *
     * @param position  元素位置
     * @param isChecked 元素选中与否
     */
    @Override
    public void checkGroup(int position, boolean isChecked) {
        mDataAdapter.getDataList().get(position).setChoosed(isChecked);
        if (isAllCheck()) {
            cb_box.setChecked(true);
        } else {
            cb_box.setChecked(false);
        }
        notifyDataSetChanged();
        statistics();
    }

    /**
     * 统计操作
     * 1.先清空全局计数器<br>
     * 2.遍历所有子元素，只要是被选中状态的，就进行相关的计算操作
     * 3.给底部的textView进行数据填充
     */
    private void statistics() {
        totalCount = 0;
        totalPrice = 0.00;
        for (int i = 0; i < mDataAdapter.getDataList().size(); i++) {
            ShopCardBean.DataBean dataBean = mDataAdapter.getDataList().get(i);
            if (dataBean.isChoosed()) {
                totalCount++;
                totalPrice += Double.valueOf(dataBean.getPrice()) * Double.valueOf(dataBean.getNumber());
            }
        }
        tv_goods_num.setText(totalCount + "");
        tv_goods_sum_money.setText(totalPrice + "");
    }

    /**
     * q全选
     *
     * @return
     */
    private boolean isAllCheck() {
        for (ShopCardBean.DataBean group : mDataAdapter.getDataList()) {
            if (!group.isChoosed())
                return false;
        }
        return true;
    }

    /**
     * 点击加号  购买数量增加
     *
     * @param position      元素位置
     * @param showCountView 用于展示变化后数量的View
     * @param isChecked     子元素选中与否
     */
    @Override
    public void doIncrease(int position, View showCountView, boolean isChecked) {
        ShopCardBean.DataBean dataBean = mDataAdapter.getDataList().get(position);
        int currentCount = Integer.valueOf(dataBean.getNumber());
        currentCount++;
        dataBean.setNumber(currentCount + "");
        ((TextView) showCountView).setText(currentCount + "");
        editcar(dataBean.getId(), currentCount + "");
//        notifyDataSetChanged();
//        statistics();
    }

    /**
     * 点击减号 数量减少
     *
     * @param position      元素位置
     * @param showCountView 用于展示变化后数量的View
     * @param isChecked     子元素选中与否
     */
    @Override
    public void doDecrease(int position, View showCountView, boolean isChecked) {
        ShopCardBean.DataBean dataBean = mDataAdapter.getDataList().get(position);
        int currentCount = Integer.valueOf(dataBean.getNumber());
        if (currentCount == 1) {
            return;
        }
        currentCount--;
        dataBean.setNumber(currentCount + "");
        ((TextView) showCountView).setText(currentCount + "");
        editcar(dataBean.getId(), currentCount + "");
//        notifyDataSetChanged();
//        statistics();
    }

    private void editcar(int id, String s) {
        NetWork.getService(ImpService.class)
                .editcar(String.valueOf(id), s)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<EditcarBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(EditcarBean editcarBean) {
                        if (editcarBean.getCode().equals("0")) {
//                            Toast.makeText(getActivity(),editcarBean.getMsg(),Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                            statistics();
                        }
                    }
                });
    }

    @Override
    public void doEdittext(int position, final View text, boolean isChecked) {
        final ShopCardBean.DataBean dataBean = mDataAdapter.getDataList().get(position);
        final TextNumDialog dialog = new TextNumDialog(getActivity());
        final EditText ed_num = dialog.findViewById(R.id.ed_num);
        final TextView tv_check = dialog.findViewById(R.id.tv_check);
        tv_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("0".equals(ed_num.getText().toString().trim()) || Integer.valueOf(ed_num.getText().toString().trim()) <= 0) {
                    Toast.makeText(getActivity(), "输入数量不能为零", Toast.LENGTH_SHORT).show();
                } else {
                    dataBean.setNumber(ed_num.getText().toString().trim());
                    ((TextView) text).setText(ed_num.getText().toString().trim());
                    editcar(dataBean.getId(), ed_num.getText().toString().trim());
//                    notifyDataSetChanged();
//                    statistics();
                }
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private class PreviewHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -1:
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        mDataAdapter.clear();
                    }
                    getData();
                    break;
                case -3:
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        mRecyclerView.refreshComplete(shopCardBean.getData().size());
                        notifyDataSetChanged();
                    } else {
                        mRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                            @Override
                            public void reload() {
                                mRecyclerView.refreshComplete(shopCardBean.getData().size());
                                notifyDataSetChanged();
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
                .ShopCardData(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ShopCardBean>() {
                    private ShopCardBean shopCardBean;

                    @Override
                    public void onCompleted() {
                        if ("0".equals(shopCardBean.getCode())) {
                            int size = shopCardBean.getData().size();
                            if (size <= 0 || shopCardBean == null) {
                                ll_no_hava_data.setVisibility(View.VISIBLE);
                                fl_have_data.setVisibility(View.GONE);
                                tv_title_right.setVisibility(View.GONE);
                            } else {
                                ll_no_hava_data.setVisibility(View.GONE);
                                fl_have_data.setVisibility(View.VISIBLE);
                                tv_title_right.setVisibility(View.VISIBLE);
                            }
                            showData(shopCardBean);
                            List<ShopCardBean.DataBean> data = shopCardBean.getData();
                            mDataAdapter.addAll(data);
                            if (mSwipeRefreshLayout.isRefreshing()) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                            mRecyclerView.refreshComplete(data.size());
                            notifyDataSetChanged();
                        }else {
                            ll_no_hava_data.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ShopCardBean shopCardBean) {
                        this.shopCardBean = shopCardBean;
                    }
                });

    }

    private void showData(ShopCardBean shopCardBean) {
        this.shopCardBean = shopCardBean;
    }

    private void notifyDataSetChanged() {
        mLuRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        isManager = false;
        onRefresh();
    }
}
