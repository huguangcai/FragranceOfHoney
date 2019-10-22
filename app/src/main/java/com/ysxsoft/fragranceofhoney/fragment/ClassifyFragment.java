package com.ysxsoft.fragranceofhoney.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ysxsoft.fragranceofhoney.R;
import com.ysxsoft.fragranceofhoney.adapter.ClassifyTitleAdapter;
import com.ysxsoft.fragranceofhoney.adapter.GridContentAdapter;
import com.ysxsoft.fragranceofhoney.impservice.ImpService;
import com.ysxsoft.fragranceofhoney.modle.ClassifyDataBean;
import com.ysxsoft.fragranceofhoney.utils.ActivityPageManager;
import com.ysxsoft.fragranceofhoney.utils.AppUtil;
import com.ysxsoft.fragranceofhoney.utils.BaseFragment;
import com.ysxsoft.fragranceofhoney.utils.IsLoginUtils;
import com.ysxsoft.fragranceofhoney.utils.NetWork;
import com.ysxsoft.fragranceofhoney.view.LoginActivity;
import com.ysxsoft.fragranceofhoney.view.SearchDataActivity;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 分类的Fragment
 */
public class ClassifyFragment extends Fragment implements View.OnClickListener {

    private RecyclerView rv_title, rv_content;
    private RelativeLayout rl_search;
    private TextView tv_title_right;
    private int stateBar;
    private EditText ed_title_search;
    private List<String> list = new ArrayList<>();
    private ClassifyTitleAdapter titleAdapter;
    private GridContentAdapter gridContentAdapter;
    private String uid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.classify_layout, null);
        stateBar = getStateBar();
        initView(view);
        if (list.size()!=0){
            list.clear();
        }
        requestData();
        initListener();
        return view;
    }

    /**
     * 获取数据
     */
    private void requestData() {
        NetWork.getRetrofit()
                .create(ImpService.class)
                .ClassifyData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ClassifyDataBean>() {
                    private ClassifyDataBean classifyDataBean;

                    @Override
                    public void onCompleted() {
                        if ("0".equals(classifyDataBean.getCode())) {
                            final List<ClassifyDataBean.DataBean> data = classifyDataBean.getData();
                            for (int i = 0; i < data.size(); i++) {
                                list.add(data.get(i).getCategory_name());
                            }
                            titleAdapter = new ClassifyTitleAdapter(getActivity(), list);
                            rv_title.setAdapter(titleAdapter);

                            gridContentAdapter = new GridContentAdapter(getActivity(), data.get(0).getOne());
                            rv_content.setAdapter(gridContentAdapter);

                            gridContentAdapter.setOnItemClickListener(new GridContentAdapter.OnItemClickListener() {
                                @Override
                                public void onClick(int i) {
                                    if (IsLoginUtils.isloginFragment(getActivity())) {
                                        ActivityPageManager instance = ActivityPageManager.getInstance();
                                        instance.finishAllActivity();
                                        getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                                    } else {
                                        int bid = gridContentAdapter.one.get(i).getBid();
                                        Intent intent = new Intent(getActivity(), SearchDataActivity.class);
                                        intent.putExtra("gid", String.valueOf(bid));
                                        intent.putExtra("uid", uid);
                                        getActivity().startActivity(intent);
                                    }
                                }
                            });
                            titleAdapter.setOnItemClickListener(new ClassifyTitleAdapter.OnItemClickListener() {
                                @Override
                                public void onClick(int i) {
                                    titleAdapter.setSelect(i);
                                    String category_name = data.get(i).getCategory_name();
                                    for (int j = 0; j < data.size(); j++) {
                                        String category_name1 = data.get(j).getCategory_name();
                                        if (category_name.equals(category_name1)) {
                                            final List<ClassifyDataBean.DataBean.OneBean> one = data.get(j).getOne();
                                            gridContentAdapter = new GridContentAdapter(getActivity(), one);
                                            rv_content.setAdapter(gridContentAdapter);
                                            gridContentAdapter.setOnItemClickListener(new GridContentAdapter.OnItemClickListener() {
                                                @Override
                                                public void onClick(int i) {
                                                    if (IsLoginUtils.isloginFragment(getActivity())) {
                                                        ActivityPageManager instance = ActivityPageManager.getInstance();
                                                        instance.finishAllActivity();
                                                        getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                                                    } else {
                                                        int bid = gridContentAdapter.one.get(i).getBid();
                                                        Intent intent = new Intent(getActivity(), SearchDataActivity.class);
                                                        intent.putExtra("gid", String.valueOf(bid));
                                                        intent.putExtra("uid", uid);
                                                        getActivity().startActivity(intent);
                                                    }
                                                }
                                            });
                                        }
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ClassifyDataBean classifyDataBean) {
                        this.classifyDataBean = classifyDataBean;
                    }
                });

    }

    private void initView(View view) {
        LinearLayout ll_title = view.findViewById(R.id.ll_title);
        ll_title.setPadding(0, stateBar, 0, 0);
        rl_search = view.findViewById(R.id.rl_search);
        tv_title_right = view.findViewById(R.id.tv_title_right);
        tv_title_right.setText("搜索");
        rv_title = view.findViewById(R.id.rv_title);
        rv_content = view.findViewById(R.id.rv_content);
        ed_title_search = view.findViewById(R.id.ed_title_search);
        ed_title_search.setFocusable(false);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        rv_title.setLayoutManager(manager);
//        rv_title.addItemDecoration(new DividerItemDecoration(getActivity(), OrientationHelper.VERTICAL));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        rv_content.setLayoutManager(gridLayoutManager);

    }

    private void initListener() {
        rl_search.setOnClickListener(this);
        tv_title_right.setOnClickListener(this);
        ed_title_search.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ed_title_search:
            case R.id.rl_search:
            case R.id.tv_title_right:
                if (IsLoginUtils.isloginFragment(getActivity())) {
                    ActivityPageManager instance = ActivityPageManager.getInstance();
                    instance.finishAllActivity();
                    getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                } else {
                    AppUtil.colsePhoneKeyboard(getActivity());
                    Intent intent = new Intent(getActivity(), SearchDataActivity.class);
                    intent.putExtra("uid", uid);
                    startActivity(intent);
                }
                break;
        }
    }

    protected void startActivity(Class clazz) {
        startActivity(new Intent(getActivity(), clazz));
    }

    protected int getStateBar() {
        int result = 0;
        int resourceId = this.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = this.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

}
