package com.ysxsoft.fragranceofhoney.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ysxsoft.fragranceofhoney.R;
import com.ysxsoft.fragranceofhoney.impservice.ImpService;
import com.ysxsoft.fragranceofhoney.modle.AcountSafeBean;
import com.ysxsoft.fragranceofhoney.modle.WithdrawBean;
import com.ysxsoft.fragranceofhoney.utils.AppUtil;
import com.ysxsoft.fragranceofhoney.utils.BaseActivity;
import com.ysxsoft.fragranceofhoney.utils.MoneyTextWatcher;
import com.ysxsoft.fragranceofhoney.utils.NetWork;
import com.ysxsoft.fragranceofhoney.widget.PayPwdDilaog;
import com.ysxsoft.fragranceofhoney.widget.PayPwdEditText;

import java.math.BigDecimal;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WithdrawCashActivity extends BaseActivity implements View.OnClickListener {

    private View img_back;
    private EditText ed_money;
    private TextView tv_balance_money, tv_withdraw_cash, tv_bank_card;
    private LinearLayout ll_select_bank_card;
    private String uid, money, mobile;
    private String pid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.withdraw_cash_layout);
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        money = intent.getStringExtra("money");
        mobile = intent.getStringExtra("mobile");
        initView();
        initListener();
    }

    private void initView() {
        img_back = getViewById(R.id.img_back);
        TextView tv_title = getViewById(R.id.tv_title);
        tv_title.setText("提现");
        ed_money = getViewById(R.id.ed_money);
        tv_balance_money = getViewById(R.id.tv_balance_money);
        tv_bank_card = getViewById(R.id.tv_bank_card);
        tv_withdraw_cash = getViewById(R.id.tv_withdraw_cash);
        ll_select_bank_card = getViewById(R.id.ll_select_bank_card);
        ed_money.addTextChangedListener(new MoneyTextWatcher(ed_money));
        tv_balance_money.setText(money);

    }

    private void initListener() {
        img_back.setOnClickListener(this);
        ll_select_bank_card.setOnClickListener(this);
        tv_withdraw_cash.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.ll_select_bank_card:
                if (TextUtils.isEmpty(ed_money.getText().toString().trim())) {
                    showToastMessage("提现金额不能为空");
                    return;
                }
                if (Double.valueOf(ed_money.getText().toString().trim()) <= 0) {
                    showToastMessage("提现金额不能为0");
                    return;
                }
                if (Double.valueOf(ed_money.getText().toString().trim()) > Double.valueOf(tv_balance_money.getText().toString().trim())) {
                    showToastMessage("输入金额不能大于账户余额");
                    return;
                }
                Intent intentMyCard = new Intent(mContext, MyCardBagActivity.class);
                intentMyCard.putExtra("uid", uid);
                intentMyCard.putExtra("withdraw_cash", "withdraw_cash");
                startActivityForResult(intentMyCard, 2019);
                break;
            case R.id.tv_withdraw_cash:


                if (TextUtils.isEmpty(ed_money.getText().toString().trim())) {
                    showToastMessage("提现金额不能为空");
                    return;
                }
                if (Double.valueOf(ed_money.getText().toString().trim()) <= 0) {
                    showToastMessage("提现金额不能为0");
                    return;
                }
                if (Double.valueOf(ed_money.getText().toString().trim()) > Double.valueOf(tv_balance_money.getText().toString().trim())) {
                    showToastMessage("输入金额不能大于账户余额");
                    return;
                }

                if (TextUtils.isEmpty(tv_bank_card.getText().toString().trim())) {
                    showToastMessage("银行卡不能为空");
                    return;
                }
                NetWork.getRetrofit()
                        .create(ImpService.class)
                        .AcountSafe(uid)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<AcountSafeBean>() {
                            private AcountSafeBean acountSafeBean;

                            @Override
                            public void onCompleted() {
                                if ("0".equals(acountSafeBean.getCode())) {
                                    if ("1".equals(acountSafeBean.getData().getTradepassword())) {

                                        final PayPwdDilaog payPwdDilaog = new PayPwdDilaog(mContext);
                                        ImageView img_close = payPwdDilaog.findViewById(R.id.img_close);
                                        img_close.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                payPwdDilaog.dismiss();
                                            }
                                        });
                                        TextView tv_forget_pwd = payPwdDilaog.findViewById(R.id.tv_forget_pwd);
                                        tv_forget_pwd.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(mContext, ModifyTradePwdActivity.class);
                                                intent.putExtra("uid", uid);
                                                intent.putExtra("mobile", mobile);
                                                startActivity(intent);
                                            }
                                        });
                                        PayPwdEditText ed_ppet = payPwdDilaog.findViewById(R.id.ed_ppet);
                                        ed_ppet.initStyle(R.drawable.edit_num_bg_red, 6, 0.33f, R.color.black, R.color.black, 20);
                                        ed_ppet.setFocus();
                                        ed_ppet.setOnTextFinishListener(new PayPwdEditText.OnTextFinishListener() {
                                            @Override
                                            public void onFinish(String str) {
                                                payPwdDilaog.dismiss();
                                                submitData(str);
                                            }
                                        });
                                        payPwdDilaog.show();
                                    } else {
                                        Intent intent=new Intent(mContext,TradePwdActivity.class);
                                        intent.putExtra("uid",uid);
                                        startActivity(intent);

                                    }

                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                showToastMessage(e.getMessage());
                            }

                            @Override
                            public void onNext(AcountSafeBean acountSafeBean) {
                                this.acountSafeBean = acountSafeBean;
                            }
                        });
                break;
        }
    }

    /**
     * 提现
     *
     * @param psd
     */
    private void submitData(String psd) {
        NetWork.getService(ImpService.class)
                .WithdrawData(uid, ed_money.getText().toString().trim(), psd, pid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WithdrawBean>() {
                    private WithdrawBean withdrawBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(withdrawBean.getMsg());
                        if ("0".equals(withdrawBean.getCode())) {
                            finish();
                        } else if ("3".equals(withdrawBean.getCode())) {
                            Intent intent=new Intent(mContext,TradePwdActivity.class);
                            intent.putExtra("uid",uid);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(WithdrawBean withdrawBean) {
                        this.withdrawBean = withdrawBean;
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2019 && resultCode == 1646) {
            pid = data.getStringExtra("pid");
            String house = data.getStringExtra("house");
            String number = data.getStringExtra("number");
            tv_bank_card.setText(house);
        }
    }
}
