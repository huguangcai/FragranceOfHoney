package com.ysxsoft.fragranceofhoney.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ysxsoft.fragranceofhoney.R;
import com.ysxsoft.fragranceofhoney.fragment.AllFragment;
import com.ysxsoft.fragranceofhoney.fragment.AllOrderFragment;
import com.ysxsoft.fragranceofhoney.fragment.ClassifyFragment;
import com.ysxsoft.fragranceofhoney.fragment.ReturnGoodsFragment;
import com.ysxsoft.fragranceofhoney.fragment.WaitEvaluateFragment;
import com.ysxsoft.fragranceofhoney.fragment.WaitFaHuoFragment;
import com.ysxsoft.fragranceofhoney.fragment.WaitGetGoodsFragment;
import com.ysxsoft.fragranceofhoney.fragment.WaitPayFragment;
import com.ysxsoft.fragranceofhoney.fragment.WaitShareFragment;
import com.ysxsoft.fragranceofhoney.utils.BaseActivity;
import com.ysxsoft.fragranceofhoney.widget.OnTabSelectListener;
import com.ysxsoft.fragranceofhoney.widget.SlidingTabLayout;
import com.ysxsoft.fragranceofhoney.widget.ViewFindUtils;

import java.util.ArrayList;

public class AllOrderActivity extends BaseActivity {

    private String[] mTitles = new String[]{"全部", "待支付", "待分享", "待发货", "待收货", "待评价", "退款/售后"};
    private ViewPager vp_content;
    private FrameLayout fl_content;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private MyPagerAdapter mAdapter;
    private Fragment currentFragment = new Fragment();//（全局）
    private AllFragment allFragment = new AllFragment();
    private WaitPayFragment waitPayFragment = new WaitPayFragment();
    private WaitGetGoodsFragment waitGetGoodsFragment = new WaitGetGoodsFragment();
    private WaitEvaluateFragment waitEvaluateFragment = new WaitEvaluateFragment();
    private WaitShareFragment waitShareFragment = new WaitShareFragment();
    private WaitFaHuoFragment waitFaHuoFragment = new WaitFaHuoFragment();
    private ReturnGoodsFragment returnGoodsFragment = new ReturnGoodsFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_order_layout);
        Intent intent = getIntent();
        String allorder = intent.getStringExtra("allorder");
        for (String titile : mTitles) {
            mFragments.add(new AllOrderFragment());
        }
        View decorView = getWindow().getDecorView();
        fl_content = getViewById(R.id.fl_content);

        vp_content = ViewFindUtils.find(decorView, R.id.vp_content);
        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        vp_content.setAdapter(mAdapter);
        SlidingTabLayout stl_tab = ViewFindUtils.find(decorView, R.id.stl_tab);
        stl_tab.setViewPager(vp_content);
        if (allorder == null) {
            switchFragment(allFragment).commit();
        } else {
            switch (allorder) {
                case "1"://待支付
                    switchFragment(waitPayFragment).commit();
                    stl_tab.setCurrentTab(1);
                    break;
                case "2"://待分享
                    switchFragment(waitShareFragment).commit();
                    stl_tab.setCurrentTab(2);
                    break;
                case "3"://待发货
                    switchFragment(waitFaHuoFragment).commit();
                    stl_tab.setCurrentTab(3);
                    break;
                case "4":
                    break;
                case "5":
                    break;
            }
        }
        stl_tab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                switch (position) {
                    case 0://全部
                        switchFragment(allFragment).commit();
                        break;
                    case 1://待支付
                        switchFragment(waitPayFragment).commit();
                        break;
                    case 2://待分享
                        switchFragment(waitShareFragment).commit();
                        break;
                    case 3://待发货
                        switchFragment(waitFaHuoFragment).commit();
                        break;
                    case 4://待收货
                        switchFragment(waitGetGoodsFragment).commit();
                        break;
                    case 5://待评价
                        switchFragment(waitEvaluateFragment).commit();
                        break;
                    case 6://退货/售后
                        switchFragment(returnGoodsFragment).commit();
                        break;
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        TextView tv_title = getViewById(R.id.tv_title);
        tv_title.setText("订单");
        getViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private FragmentTransaction switchFragment(Fragment targetFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!targetFragment.isAdded()) {
//第一次使用switchFragment()时currentFragment为null，所以要判断一下
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
            transaction.add(R.id.fl_content, targetFragment, targetFragment.getClass().getName());
        } else {
            transaction.hide(currentFragment).show(targetFragment);
        }
        currentFragment = targetFragment;
        return transaction;
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }
}
