package com.ysxsoft.fragranceofhoney.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ysxsoft.fragranceofhoney.R;
import com.ysxsoft.fragranceofhoney.impservice.ImpService;
import com.ysxsoft.fragranceofhoney.modle.BalanceMoneyBean;
import com.ysxsoft.fragranceofhoney.modle.MyMsgBean;
import com.ysxsoft.fragranceofhoney.modle.OrderNumberBean;
import com.ysxsoft.fragranceofhoney.modle.QQNumberBean;
import com.ysxsoft.fragranceofhoney.utils.BaseFragment;
import com.ysxsoft.fragranceofhoney.utils.CustomDialog;
import com.ysxsoft.fragranceofhoney.utils.ImageLoadUtil;
import com.ysxsoft.fragranceofhoney.utils.MyObserver;
import com.ysxsoft.fragranceofhoney.utils.NetWork;
import com.ysxsoft.fragranceofhoney.view.AllOrderActivity;
import com.ysxsoft.fragranceofhoney.view.CollectActivity;
import com.ysxsoft.fragranceofhoney.view.GetGoodsAddressActivity;
import com.ysxsoft.fragranceofhoney.view.MyCardBagActivity;
import com.ysxsoft.fragranceofhoney.view.MyInfoActivity;
import com.ysxsoft.fragranceofhoney.view.MyTeamActivity;
import com.ysxsoft.fragranceofhoney.view.MyWalletActivity;
import com.ysxsoft.fragranceofhoney.view.PaySucessfulActivity;
import com.ysxsoft.fragranceofhoney.view.SettingActivity;
import com.ysxsoft.fragranceofhoney.view.SettingEditorActivity;
import com.ysxsoft.fragranceofhoney.view.WebViewActivity;
import com.ysxsoft.fragranceofhoney.widget.CircleImageView;

import cn.bingoogolapple.badgeview.BGABadgeView;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 我的的Fragment
 */
public class MyFragment extends BaseFragment implements View.OnClickListener {

    private ImageView img_info, img_setting;
    private CircleImageView img_head;
    private TextView tv_nikeName, tv_editor, tv_collect, tv_wait_pay, tv_wait_share,
            tv_wait_fahuo, tv_return_goods, tv_wait_get_goods, tv_wait_evaluate, tv_qq, tv_wx;
    private FrameLayout fl_img_arrow;
    private LinearLayout ll_my_wallet, ll_my_team, ll_get_goods_address, ll_my_card_bag, ll_img_info_center, ll_img_sc, ll_img_my_setting;
    private BGABadgeView badgeTextView1, badgeTextView2, badgeTextView3, badgeTextView4, badgeTextView5;
    private int stateBar;
    private String uid;
    private String headPath, sex;

    //    public void setUid(String uid) {
//        this.uid = uid;
//    }
    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sp = getActivity().getSharedPreferences("UID", Context.MODE_PRIVATE);
        uid = sp.getString("uid", "");
        stateBar = getStateBar();
        initView();
        initLisetent();
//        requesrInfoData();
    }

    /**
     * 已读和未读消息获取
     */
    private void requesrInfoData() {
        NetWork.getService(ImpService.class)
                .BalanceMoneyData(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BalanceMoneyBean>() {
                    private BalanceMoneyBean balanceMoneyBean;

                    @Override
                    public void onCompleted() {
                        if ("0".equals(balanceMoneyBean.getCode())) {
                            if (balanceMoneyBean.getData().getNews() == 0) {//0是未读1是已读
                                img_info.setBackgroundResource(R.mipmap.img_have_info);
                            } else {
                                img_info.setBackgroundResource(R.mipmap.img_no_info);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(BalanceMoneyBean balanceMoneyBean) {
                        this.balanceMoneyBean = balanceMoneyBean;
                    }
                });

    }


    private void initLisetent() {
        img_info.setOnClickListener(this);
        img_head.setOnClickListener(this);
        img_setting.setOnClickListener(this);
        tv_editor.setOnClickListener(this);
        tv_collect.setOnClickListener(this);
        tv_wait_pay.setOnClickListener(this);
        tv_wait_share.setOnClickListener(this);
        tv_wait_fahuo.setOnClickListener(this);
        tv_return_goods.setOnClickListener(this);
        tv_wait_get_goods.setOnClickListener(this);
        tv_wait_evaluate.setOnClickListener(this);
        fl_img_arrow.setOnClickListener(this);
        ll_my_wallet.setOnClickListener(this);
        ll_my_team.setOnClickListener(this);
        ll_get_goods_address.setOnClickListener(this);
        ll_my_card_bag.setOnClickListener(this);
        ll_img_info_center.setOnClickListener(this);
        ll_img_my_setting.setOnClickListener(this);
        ll_img_sc.setOnClickListener(this);
        img_info.setOnClickListener(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.my_layout;
    }

    private void initView() {
        LinearLayout ll_title = getViewById(R.id.ll_title);
        ll_title.setPadding(0, stateBar, 0, 0);
//        View topView = getViewById(R.id.topView);
//        initStatusBar(topView);
        TextView tv_title = getViewById(R.id.tv_title);
        tv_title.setText("我的");
        img_head = getViewById(R.id.img_head);
        img_info = getViewById(R.id.img_info);
        img_info.setVisibility(View.INVISIBLE);
        img_setting = getViewById(R.id.img_setting);
        img_setting.setVisibility(View.INVISIBLE);
        tv_nikeName = getViewById(R.id.tv_nikeName);
        fl_img_arrow = getViewById(R.id.fl_img_arrow);
        tv_editor = getViewById(R.id.tv_editor);
        tv_collect = getViewById(R.id.tv_collect);
        tv_wait_pay = getViewById(R.id.tv_wait_pay);
        tv_wait_share = getViewById(R.id.tv_wait_share);
        tv_wait_fahuo = getViewById(R.id.tv_wait_fahuo);
        tv_return_goods = getViewById(R.id.tv_return_goods);
        tv_wait_get_goods = getViewById(R.id.tv_wait_get_goods);
        tv_wait_evaluate = getViewById(R.id.tv_wait_evaluate);
        ll_my_wallet = getViewById(R.id.ll_my_wallet);
        ll_my_team = getViewById(R.id.ll_my_team);
        ll_get_goods_address = getViewById(R.id.ll_get_goods_address);
        ll_my_card_bag = getViewById(R.id.ll_my_card_bag);
        ll_img_info_center = getViewById(R.id.ll_img_info_center);
        ll_img_my_setting = getViewById(R.id.ll_img_my_setting);
        ll_img_sc = getViewById(R.id.ll_img_sc);
        tv_qq = getViewById(R.id.tv_qq);
        tv_wx = getViewById(R.id.tv_wx);
        badgeTextView1 = getViewById(R.id.text1);
        badgeTextView2 = getViewById(R.id.text2);
        badgeTextView3 = getViewById(R.id.text3);
        badgeTextView4 = getViewById(R.id.text4);
        badgeTextView5 = getViewById(R.id.text5);
        setPoint();
    }

    private void setPoint() {
        badgeTextView1.showCirclePointBadge();
        //初始化
        badgeTextView1.getBadgeViewHelper().setBadgeTextSizeSp(10);
        badgeTextView1.getBadgeViewHelper().setBadgeTextColorInt(Color.parseColor("#FF5B97"));
        badgeTextView1.getBadgeViewHelper().setBadgeBgColorInt(Color.WHITE);
        badgeTextView1.getBadgeViewHelper().setDraggable(true);
        badgeTextView1.getBadgeViewHelper().setBadgeBorderWidthDp(1);
        badgeTextView1.getBadgeViewHelper().setBadgeBorderColorInt(Color.parseColor("#FF5B97"));

        badgeTextView2.showCirclePointBadge();
        //初始化
//        badgeTextView2.showTextBadge("9");
        badgeTextView2.getBadgeViewHelper().setBadgeTextSizeSp(10);
        badgeTextView2.getBadgeViewHelper().setBadgeTextColorInt(Color.parseColor("#FF5B97"));
        badgeTextView2.getBadgeViewHelper().setBadgeBgColorInt(Color.WHITE);
        badgeTextView2.getBadgeViewHelper().setDraggable(true);
        badgeTextView2.getBadgeViewHelper().setBadgeBorderWidthDp(1);
        badgeTextView2.getBadgeViewHelper().setBadgeBorderColorInt(Color.parseColor("#FF5B97"));


        badgeTextView3.showCirclePointBadge();
        //初始化
//        badgeTextView3.showTextBadge("9");
        badgeTextView3.getBadgeViewHelper().setBadgeTextSizeSp(10);
        badgeTextView3.getBadgeViewHelper().setBadgeTextColorInt(Color.parseColor("#FF5B97"));
        badgeTextView3.getBadgeViewHelper().setBadgeBgColorInt(Color.WHITE);
        badgeTextView3.getBadgeViewHelper().setDraggable(true);
        badgeTextView3.getBadgeViewHelper().setBadgeBorderWidthDp(1);
        badgeTextView3.getBadgeViewHelper().setBadgeBorderColorInt(Color.parseColor("#FF5B97"));

        badgeTextView4.showCirclePointBadge();
        //初始化
//        badgeTextView4.showTextBadge("9");
        badgeTextView4.getBadgeViewHelper().setBadgeTextSizeSp(10);
        badgeTextView4.getBadgeViewHelper().setBadgeTextColorInt(Color.parseColor("#FF5B97"));
        badgeTextView4.getBadgeViewHelper().setBadgeBgColorInt(Color.WHITE);
        badgeTextView4.getBadgeViewHelper().setDraggable(true);
        badgeTextView4.getBadgeViewHelper().setBadgeBorderWidthDp(1);
        badgeTextView4.getBadgeViewHelper().setBadgeBorderColorInt(Color.parseColor("#FF5B97"));

        badgeTextView5.showCirclePointBadge();
        //初始化
//        badgeTextView5.showTextBadge("9");
        badgeTextView5.getBadgeViewHelper().setBadgeTextSizeSp(10);
        badgeTextView5.getBadgeViewHelper().setBadgeTextColorInt(Color.parseColor("#FF5B97"));
        badgeTextView5.getBadgeViewHelper().setBadgeBgColorInt(Color.WHITE);
        badgeTextView5.getBadgeViewHelper().setDraggable(true);
        badgeTextView5.getBadgeViewHelper().setBadgeBorderWidthDp(1);
        badgeTextView5.getBadgeViewHelper().setBadgeBorderColorInt(Color.parseColor("#FF5B97"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_img_info_center://消息
                Intent infointent = new Intent(getActivity(), MyInfoActivity.class);
                infointent.putExtra("uid", uid);
                startActivity(infointent);
                break;
            case R.id.ll_img_my_setting://设置
                Intent intent0 = new Intent(getActivity(), SettingActivity.class);
                intent0.putExtra("uid", uid);
                startActivity(intent0);
                break;
            case R.id.img_head://编辑
                Intent intent = new Intent(getActivity(), SettingEditorActivity.class);
                intent.putExtra("uid", uid);
                intent.putExtra("nikeName", tv_nikeName.getText().toString().trim());
                intent.putExtra("headPath", headPath);
                intent.putExtra("sex", sex);
                startActivity(intent);
                break;
            case R.id.ll_img_sc://收藏
                Intent collectintent = new Intent(getActivity(), CollectActivity.class);
                collectintent.putExtra("uid", uid);
                startActivity(collectintent);
                break;
            case R.id.tv_wait_pay://待支付
                Intent payintent = new Intent(getActivity(), WebViewActivity.class);
                String payurl = NetWork.H5BaseUrl + "order?sc=2&status=" + "1" + "&uid=" + uid;
                payintent.putExtra("uid", uid);
                payintent.putExtra("url", payurl);
                payintent.putExtra("flag", "myfragment");
                startActivity(payintent);
                break;
            case R.id.tv_wait_share://待分享
                Intent shareintent = new Intent(getActivity(), WebViewActivity.class);
                String shareurl = NetWork.H5BaseUrl + "order?sc=2&status=" + "2" + "&uid=" + uid;
                shareintent.putExtra("uid", uid);
                shareintent.putExtra("url", shareurl);
                shareintent.putExtra("flag", "myfragment");
                startActivity(shareintent);
                break;
            case R.id.tv_wait_fahuo://待发货
                Intent fahuointent = new Intent(getActivity(), WebViewActivity.class);
                String fahuourl = NetWork.H5BaseUrl + "order?sc=2&status=" + "2" + "&uid=" + uid;
                fahuointent.putExtra("uid", uid);
                fahuointent.putExtra("url", fahuourl);
                fahuointent.putExtra("flag", "myfragment");
                startActivity(fahuointent);
                break;
            case R.id.tv_wait_get_goods://待收货
                Intent goodsintent = new Intent(getActivity(), WebViewActivity.class);
                String goodsurl = NetWork.H5BaseUrl + "order?sc=2&status=" + "3" + "&uid=" + uid;
                goodsintent.putExtra("uid", uid);
                goodsintent.putExtra("url", goodsurl);
                goodsintent.putExtra("flag", "myfragment");
                startActivity(goodsintent);
                break;
            case R.id.tv_wait_evaluate://待评价
                Intent evaluateintent = new Intent(getActivity(), WebViewActivity.class);
                String evaluateurl = NetWork.H5BaseUrl + "order?sc=2&status=" + "4" + "&uid=" + uid;
                evaluateintent.putExtra("uid", uid);
                evaluateintent.putExtra("url", evaluateurl);
                evaluateintent.putExtra("flag", "myfragment");
                startActivity(evaluateintent);

                break;
            case R.id.tv_return_goods://退货/售后
                Intent returnintent = new Intent(getActivity(), WebViewActivity.class);
                String returnurl = NetWork.H5BaseUrl + "order?sc=2&status=" + "5" + "&uid=" + uid;
                returnintent.putExtra("uid", uid);
                returnintent.putExtra("url", returnurl);
                returnintent.putExtra("flag", "myfragment");
                startActivity(returnintent);

                break;
            case R.id.ll_my_wallet://我的钱包
                Intent intentwallet = new Intent(getActivity(), MyWalletActivity.class);
                intentwallet.putExtra("uid", uid);
                startActivity(intentwallet);
                break;
            case R.id.ll_my_team://我的团队
                startActivity(MyTeamActivity.class);
                break;
            case R.id.ll_get_goods_address://收货地址
                Intent intentaddress = new Intent(getActivity(), GetGoodsAddressActivity.class);
                intentaddress.putExtra("uid", uid);
                startActivity(intentaddress);
                break;
            case R.id.ll_my_card_bag://我的卡包
                Intent intentMyCard = new Intent(getActivity(), MyCardBagActivity.class);
                intentMyCard.putExtra("uid", uid);
                startActivity(intentMyCard);
                break;
            case R.id.fl_img_arrow://全部订单
                Intent allintent = new Intent(getActivity(), WebViewActivity.class);
                String allurl = NetWork.H5BaseUrl + "order?sc=2&status=" + "0" + "&uid=" + uid;
                allintent.putExtra("uid", uid);
                allintent.putExtra("url", allurl);
                allintent.putExtra("flag", "myfragment");
                startActivity(allintent);
//                badgeTextView1.hiddenBadge();
//                badgeTextView2.hiddenBadge();
//                badgeTextView3.hiddenBadge();
//                badgeTextView4.hiddenBadge();
//                badgeTextView5.hiddenBadge();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        requestData();
        requestQQ();
        requestOrderNumber();
    }

    private void requestOrderNumber() {
        NetWork.getService(ImpService.class)
                .order_number(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OrderNumberBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(OrderNumberBean orderNumberBean) {
                        if (orderNumberBean.getCode() == 0) {
                            if (orderNumberBean.getData().getType1() == 0) {
                                badgeTextView1.hiddenBadge();
                            } else {
                                if (orderNumberBean.getData().getType1() > 99) {
                                    badgeTextView1.showTextBadge("99+");
                                } else {
                                    badgeTextView1.showTextBadge(String.valueOf(orderNumberBean.getData().getType1()));
                                }
                            }

                            if (orderNumberBean.getData().getType2() == 0) {
                                badgeTextView2.hiddenBadge();
                            } else {
                                if (orderNumberBean.getData().getType2() > 99) {
                                    badgeTextView2.showTextBadge("99+");
                                } else {
                                    badgeTextView2.showTextBadge(String.valueOf(orderNumberBean.getData().getType2()));
                                }
                            }

                            if (orderNumberBean.getData().getType3() == 0) {
                                badgeTextView3.hiddenBadge();
                            } else {
                                if (orderNumberBean.getData().getType3() > 99) {
                                    badgeTextView3.showTextBadge("99+");
                                } else {
                                    badgeTextView3.showTextBadge(String.valueOf(orderNumberBean.getData().getType3()));
                                }
                            }

                            if (orderNumberBean.getData().getType4() == 0) {
                                badgeTextView4.hiddenBadge();
                            } else {
                                if (orderNumberBean.getData().getType4() > 99) {
                                    badgeTextView4.showTextBadge("99+");
                                } else {
                                    badgeTextView4.showTextBadge(String.valueOf(orderNumberBean.getData().getType4()));
                                }
                            }

                            if (orderNumberBean.getData().getType5() == 0) {
                                badgeTextView5.hiddenBadge();
                            } else {
                                if (orderNumberBean.getData().getType5() > 99) {
                                    badgeTextView5.showTextBadge("99+");
                                } else {
                                    badgeTextView5.showTextBadge(String.valueOf(orderNumberBean.getData().getType5()));
                                }
                            }


                        }
                    }
                });

    }

    private void requestQQ() {
        NetWork.getService(ImpService.class)
                .QQNumberData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<QQNumberBean>() {
                    private QQNumberBean qqNumberBean;

                    @Override
                    public void onCompleted() {
                        if ("0".equals(qqNumberBean.getCode())) {
                            tv_qq.setText("官方qq：" + qqNumberBean.getData().getQq());
                            tv_wx.setText("官方微信：" + qqNumberBean.getData().getWx());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(QQNumberBean qqNumberBean) {

                        this.qqNumberBean = qqNumberBean;
                    }
                });
    }

    /**
     * 获取数据
     */
    private void requestData() {
        NetWork.getRetrofit()
                .create(ImpService.class)
                .MyMsg(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MyMsgBean>() {
                    private MyMsgBean myMsgBean;

                    @Override
                    public void onCompleted() {
                        if ("0".equals(myMsgBean.getCode())) {
                            headPath = myMsgBean.getData().getImgurl();
                            sex = myMsgBean.getData().getSex();
                            tv_nikeName.setText(myMsgBean.getData().getNickname());
                            ImageLoadUtil.GlideHeadImageLoad(getActivity(), myMsgBean.getData().getImgurl(), img_head);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(MyMsgBean myMsgBean) {
                        this.myMsgBean = myMsgBean;
                    }
                });

    }
}
