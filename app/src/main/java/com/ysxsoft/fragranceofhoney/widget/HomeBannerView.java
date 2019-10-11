package com.ysxsoft.fragranceofhoney.widget;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ysxsoft.fragranceofhoney.R;
import com.ysxsoft.fragranceofhoney.adapter.BannerAdapter;
import com.ysxsoft.fragranceofhoney.adapter.HomeClassifyAdapter;
import com.ysxsoft.fragranceofhoney.adapter.HotGoddsAdapter;
import com.ysxsoft.fragranceofhoney.impservice.ImpService;
import com.ysxsoft.fragranceofhoney.modle.HomeClassifyBean;
import com.ysxsoft.fragranceofhoney.modle.HomeLunBoBean;
import com.ysxsoft.fragranceofhoney.modle.HotGoodsBean;
import com.ysxsoft.fragranceofhoney.modle.RecommendBean;
import com.ysxsoft.fragranceofhoney.utils.ActivityPageManager;
import com.ysxsoft.fragranceofhoney.utils.IsLoginUtils;
import com.ysxsoft.fragranceofhoney.utils.NetWork;
import com.ysxsoft.fragranceofhoney.view.LoginActivity;
import com.ysxsoft.fragranceofhoney.view.SearchDataActivity;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
@Deprecated
public class HomeBannerView extends AbsLinearLayout {

    private Context context;
    //    private CyclerViewPager vp_content;
    private ViewPager vp_content;

    private BannerAdapter bannerAdapter;
    private LinearLayout ll_point;
    private ImageView imageView;
    private MyRecyclerView rv_list;
    private RecyclerView hsv_list;
    private final String uid;

    public HomeBannerView(Context context) {
        super(context);
        this.context = context;
        SharedPreferences sp = context.getSharedPreferences("UID", Context.MODE_PRIVATE);
        uid = sp.getString("uid", "");
        requestLunBoData();//轮播
        requestClassifyData();//获取八宫格数据
        requestHorizontalBannerData();//RecyclerView 水平滑动的数据
    }

    /**
     * 轮播
     */
    private void requestLunBoData() {
        NetWork.getRetrofit()
                .create(ImpService.class)
                .HomeLunBo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HomeLunBoBean>() {
                    private HomeLunBoBean homeLunBoBean;

                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onCompleted() {
                        if ("0".equals(homeLunBoBean.getCode())) {
                            List<HomeLunBoBean.DataBean> data = homeLunBoBean.getData();
                            bannerAdapter = new BannerAdapter(context, data);
                            vp_content.setAdapter(bannerAdapter);
                            for (int i = 0; i < data.size(); i++) {
                                imageView = new ImageView(context);
                                imageView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.gray_point_shape));
                                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                lp.setMargins(5, 0, 5, 0);
                                imageView.setLayoutParams(lp);
                                ll_point.addView(imageView);
                            }
                            ll_point.getChildAt(0).setBackgroundDrawable(context.getResources().getDrawable(R.drawable.red_point_shape));

                            vp_content.setCurrentItem(0);
                            vp_content.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                @Override
                                public void onPageScrolled(final int i, float v, int i1) {//页面滚动时
                                    int childCount = ll_point.getChildCount();
                                    for (int j = 0; j < childCount; j++) {
                                        if (j == i) {
                                            ll_point.getChildAt(j).setBackgroundDrawable(context.getResources().getDrawable(R.drawable.red_point_shape));
                                        } else {
                                            ll_point.getChildAt(j).setBackgroundDrawable(context.getResources().getDrawable(R.drawable.gray_point_shape));
                                        }
                                    }
                                }

                                @Override
                                public void onPageSelected(int i) {//页面选中的时候


                                }

                                @Override
                                public void onPageScrollStateChanged(int i) {//状态改变时

                                }
                            });


                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(HomeLunBoBean homeLunBoBean) {
                        this.homeLunBoBean = homeLunBoBean;
                    }
                });
    }

    /**
     * 获取八宫格数据
     */
    private void requestClassifyData() {
        NetWork.getRetrofit()
                .create(ImpService.class)
                .HomeClassify()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HomeClassifyBean>() {
                    private HomeClassifyBean homeClassifyBean;

                    @Override
                    public void onCompleted() {
                        if ("0".equals(homeClassifyBean.getCode())) {
                            final List<HomeClassifyBean.DataBean> data = homeClassifyBean.getData();
                            final HomeClassifyAdapter adapter = new HomeClassifyAdapter(context, data);
                            rv_list.setAdapter(adapter);
                            adapter.setOnItemClickListener(new HomeClassifyAdapter.OnItemClickListener() {
                                @Override
                                public void onClick(int i) {
                                    if (IsLoginUtils.isloginFragment(context)) {
                                        ActivityPageManager instance = ActivityPageManager.getInstance();
                                        instance.finishAllActivity();
                                        context.startActivity(new Intent(context, LoginActivity.class));
                                    } else {
                                        HomeClassifyBean.DataBean dataBean = data.get(i);
                                        int id = dataBean.getId();
                                        Intent intent = new Intent(context, SearchDataActivity.class);
                                        intent.putExtra("gid", String.valueOf(id));
                                        if (!TextUtils.isEmpty(uid) && uid != null) {
                                            intent.putExtra("uid", uid);
                                        }
                                        context.startActivity(intent);
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(HomeClassifyBean homeClassifyBean) {

                        this.homeClassifyBean = homeClassifyBean;
                    }
                });
    }

    /**
     * RecyclerView 水平滑动的数据
     */
    private void requestHorizontalBannerData() {
        NetWork.getRetrofit()
                .create(ImpService.class)
                .HotGoodsData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HotGoodsBean>() {
                    private HotGoodsBean hotGoodsBean;

                    @Override
                    public void onCompleted() {
                        if ("0".equals(hotGoodsBean.getCode())) {
                            List<HotGoodsBean.DataBean> data = hotGoodsBean.getData();
                            HotGoddsAdapter adapter = new HotGoddsAdapter(context, data);
                            hsv_list.setAdapter(adapter);
                            adapter.setOnItemClickListener(new HotGoddsAdapter.OnItemClickListener() {
                                @Override
                                public void onClick(int i) {
                                    if (IsLoginUtils.isloginFragment(context)) {
                                        ActivityPageManager instance = ActivityPageManager.getInstance();
                                        instance.finishAllActivity();
                                        context.startActivity(new Intent(context, LoginActivity.class));
                                    } else {
//                                        if (!TextUtils.isEmpty(uid) && uid != null) {
//                                            intent.putExtra("uid", uid);
//                                        }
                                        Toast.makeText(context, "已登录", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(HotGoodsBean recommendBean) {
                        this.hotGoodsBean = recommendBean;
                    }
                });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.home_vp_banner_layout;
    }

    @Override
    protected void initView() {
        vp_content = getViewById(R.id.vp_content);
        //指示点
        ll_point = getViewById(R.id.ll_point);
        //八宫格
        rv_list = getViewById(R.id.rv_list);
        rv_list.setHasFixedSize(true);
        GridLayoutManager manager = new GridLayoutManager(context, 4);
        rv_list.setLayoutManager(manager);
        //水平
        hsv_list = getViewById(R.id.hsv_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        hsv_list.setLayoutManager(layoutManager);
    }
}
