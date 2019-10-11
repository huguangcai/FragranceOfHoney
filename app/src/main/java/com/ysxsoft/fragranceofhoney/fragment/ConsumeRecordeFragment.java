package com.ysxsoft.fragranceofhoney.fragment;

import com.ysxsoft.fragranceofhoney.R;
import com.ysxsoft.fragranceofhoney.utils.BaseFragment;

/**
 * 消费记录的fragment
 */
public class ConsumeRecordeFragment extends BaseFragment {
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected int getLayoutResId() {
//        return R.layout.recharge_recorde_layout;
        return R.layout.consume_recorde_item_layout;
    }
}
