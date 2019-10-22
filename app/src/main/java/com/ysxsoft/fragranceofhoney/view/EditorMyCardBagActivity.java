package com.ysxsoft.fragranceofhoney.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ysxsoft.fragranceofhoney.R;
import com.ysxsoft.fragranceofhoney.impservice.ImpService;
import com.ysxsoft.fragranceofhoney.modle.AddCardBean;
import com.ysxsoft.fragranceofhoney.modle.ModifyCardBean;
import com.ysxsoft.fragranceofhoney.utils.AppUtil;
import com.ysxsoft.fragranceofhoney.utils.BaseActivity;
import com.ysxsoft.fragranceofhoney.utils.CountDownTimeHelper;
import com.ysxsoft.fragranceofhoney.utils.NetWork;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class EditorMyCardBagActivity extends BaseActivity implements View.OnClickListener {

    private View img_back;
    private EditText ed_name, ed_Id_card, ed_bank_card_num, ed_phone_num, ed_idenfy_code, ed_bank_card;
    private TextView tv_get_idenfy_code, tv_bank_card;
    private LinearLayout ll_select_open_bank;
    private Button btn_submit;
    private String uid, realname, house, idcard, number, phone;
    private int pid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editor_my_card_bag_layout);
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        realname = intent.getStringExtra("realname");
        house = intent.getStringExtra("house");
        idcard = intent.getStringExtra("idcard");
        number = intent.getStringExtra("number");
        phone = intent.getStringExtra("phone");
        pid = intent.getIntExtra("pid", -1);
        initView();
        initIntentData();
        initListener();
    }

    private void initIntentData() {
        Intent intent = getIntent();
        String khh = intent.getStringExtra("khh");
        String khr = intent.getStringExtra("khr");
        String bank_card_num = intent.getStringExtra("bank_card_num");
        if (!TextUtils.isEmpty(khh) && khh != null) {
            ed_name.setText(khr);
            tv_bank_card.setText(khh);
            ed_bank_card_num.setText(bank_card_num);
        }

    }

    private void initView() {
        img_back = getViewById(R.id.img_back);
        TextView tv_title = getViewById(R.id.tv_title);
        tv_title.setText("我的卡包");
        ed_name = getViewById(R.id.ed_name);
        ed_Id_card = getViewById(R.id.ed_Id_card);
        tv_bank_card = getViewById(R.id.tv_bank_card);
        ed_bank_card_num = getViewById(R.id.ed_bank_card_num);
        ed_bank_card = getViewById(R.id.ed_bank_card);
        ed_phone_num = getViewById(R.id.ed_phone_num);
        ed_idenfy_code = getViewById(R.id.ed_idenfy_code);
        tv_get_idenfy_code = getViewById(R.id.tv_get_idenfy_code);
        ll_select_open_bank = getViewById(R.id.ll_select_open_bank);
        btn_submit = getViewById(R.id.btn_submit);
        if (!TextUtils.isEmpty(realname) || realname != null) {
            ed_name.setText(realname);
            ed_Id_card.setText(idcard);
            ed_bank_card_num.setText(number);
            ed_phone_num.setText(phone);
            ed_bank_card.setText(house);
        }
        ed_bank_card.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (ed_Id_card.getText().toString().trim().length()!=18){
                    showToastMessage("身份证输入有误");
                }
            }
        });

    }

    private void initListener() {
        img_back.setOnClickListener(this);
        tv_get_idenfy_code.setOnClickListener(this);
        ll_select_open_bank.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;

            case R.id.tv_get_idenfy_code:
                if (TextUtils.isEmpty(ed_phone_num.getText().toString().trim())) {
                    showToastMessage("手机号不能为空");
                    return;
                }
                if (!AppUtil.checkPhoneNum(ed_phone_num.getText().toString().trim())) {
                    showToastMessage("请输入正确的手机号");
                    return;
                }
                CountDownTimeHelper countDownTimeHelper = new CountDownTimeHelper(60, tv_get_idenfy_code);
                sendMessage(ed_phone_num.getText().toString().trim(),"2");
                break;

//            case R.id.ll_select_open_bank:
//                showToastMessage("选择开户行");
//                break;

            case R.id.btn_submit:
                if (checkData()) return;
                if (!TextUtils.isEmpty(realname) || realname != null) {
                    ModifyData();
                } else {
                    SubmitData();
                }
                break;
        }

    }

    /**
     * 修改数据
     */
    private void ModifyData() {
        NetWork.getRetrofit()
                .create(ImpService.class)
                .ModifyCardData(uid, String.valueOf(pid),
                        ed_name.getText().toString().trim(),
                        ed_Id_card.getText().toString().trim(),
                        ed_bank_card.getText().toString().trim(),
                        ed_phone_num.getText().toString().trim(),
                        ed_bank_card_num.getText().toString().trim(),
                        ed_idenfy_code.getText().toString().trim())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ModifyCardBean>() {
                    private ModifyCardBean modifyCardBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(modifyCardBean.getMsg());
                        if ("0".equals(modifyCardBean.getCode())) {
                            finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(ModifyCardBean modifyCardBean) {

                        this.modifyCardBean = modifyCardBean;
                    }
                });

    }

    /**
     * 提交数据
     */
    private void SubmitData() {
        NetWork.getRetrofit()
                .create(ImpService.class)
                .AddCardData(uid,
                        ed_name.getText().toString().trim(),
                        ed_Id_card.getText().toString().trim(),
                        ed_bank_card.getText().toString().trim(),
                        ed_phone_num.getText().toString().trim(),
                        ed_bank_card_num.getText().toString().trim(),
                        ed_idenfy_code.getText().toString().trim())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AddCardBean>() {
                    private AddCardBean addCardBean;

                    @Override
                    public void onCompleted() {
                        if ("0".equals(addCardBean.getCode())) {
                            finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(AddCardBean addCardBean) {
                        this.addCardBean = addCardBean;
                    }
                });
    }

    /**
     * 核对信息
     *
     * @return
     */
    private boolean checkData() {
        if (TextUtils.isEmpty(ed_name.getText().toString().trim())) {
            showToastMessage("持卡人姓名不能为空");
            return true;
        }

        if (TextUtils.isEmpty(ed_Id_card.getText().toString().trim())) {
            showToastMessage("身份证号不能为空");
            return true;
        }

        if (TextUtils.isEmpty(ed_bank_card.getText().toString().trim())) {
            showToastMessage("开户行不能为空");
            return true;
        }

        if (TextUtils.isEmpty(ed_bank_card_num.getText().toString().trim())) {
            showToastMessage("银行卡号不能为空");
            return true;
        }

        if (TextUtils.isEmpty(ed_phone_num.getText().toString().trim())) {
            showToastMessage("手机号不能为空");
            return true;
        }
        if (!AppUtil.checkPhoneNum(ed_phone_num.getText().toString().trim())) {
            showToastMessage("请输入正确的手机号");
            return true;
        }
        if (TextUtils.isEmpty(ed_idenfy_code.getText().toString().trim())) {
            showToastMessage("验证码不能为空");
            return true;
        }
        return false;
    }
}
