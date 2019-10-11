package com.ysxsoft.fragranceofhoney.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ysxsoft.fragranceofhoney.R;
import com.ysxsoft.fragranceofhoney.impservice.ImpService;
import com.ysxsoft.fragranceofhoney.modle.BalanceMoneyBean;
import com.ysxsoft.fragranceofhoney.utils.BaseActivity;
import com.ysxsoft.fragranceofhoney.utils.NetWork;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MyWalletActivity extends BaseActivity implements View.OnClickListener {

    private View img_back;
    private TextView tv_balance_money;
    private LinearLayout ll_wallet_detail, ll_recharge, ll_withdraw_cash;
    private int stateBar;
    private String uid;
    private BalanceMoneyBean.DataBean data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_wallet_layout);
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        setHalfTransparent();
        setFitSystemWindow(false);
        stateBar = getStateBar();
        initView();
        initListener();
    }

    /**
     * 获取余额
     */
    private void requestBalanceData() {
        NetWork.getService(ImpService.class)
                .BalanceMoneyData(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BalanceMoneyBean>() {
                    private BalanceMoneyBean balanceMoneyBean;

                    @Override
                    public void onCompleted() {
                        if ("0".equals(balanceMoneyBean.getCode())) {
                            data = balanceMoneyBean.getData();
                            tv_balance_money.setText(data.getMoney());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(BalanceMoneyBean balanceMoneyBean) {
                        this.balanceMoneyBean = balanceMoneyBean;
                    }
                });
    }

    private void initView() {
        RelativeLayout ll_title = getViewById(R.id.ll_title);
        ll_title.setPadding(0, stateBar, 0, 0);
        img_back = getViewById(R.id.img_back);
        TextView tv_title = getViewById(R.id.tv_title);
        tv_title.setText("我的钱包");
        tv_balance_money = getViewById(R.id.tv_balance_money);
        ll_wallet_detail = getViewById(R.id.ll_wallet_detail);
        ll_recharge = getViewById(R.id.ll_recharge);
        ll_withdraw_cash = getViewById(R.id.ll_withdraw_cash);

    }

    private void initListener() {
        img_back.setOnClickListener(this);
        ll_wallet_detail.setOnClickListener(this);
        ll_recharge.setOnClickListener(this);
        ll_withdraw_cash.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.ll_wallet_detail:
                Intent detailcash = new Intent(mContext, WalletDetailActivity.class);
                detailcash.putExtra("uid", uid);
                startActivity(detailcash);
                break;
            case R.id.ll_recharge:
                Intent recharge = new Intent(mContext, RechargeActivity.class);
                recharge.putExtra("uid", uid);
                startActivity(recharge);
                break;
            case R.id.ll_withdraw_cash:
                Intent intentcash = new Intent(mContext, WithdrawCashActivity.class);
                intentcash.putExtra("uid", uid);
                intentcash.putExtra("money", data.getMoney());
                intentcash.putExtra("mobile", data.getMobile());
                startActivity(intentcash);
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestBalanceData();
    }
}
