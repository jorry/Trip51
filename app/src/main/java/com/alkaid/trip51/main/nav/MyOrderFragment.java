package com.alkaid.trip51.main.nav;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.alkaid.trip51.R;
import com.alkaid.trip51.base.widget.BaseTabPageViewFragment;
import com.alkaid.trip51.model.NetDataConstants;
import com.alkaid.trip51.shop.OrderListFragment;

/**
 * Created by alkaid on 2015/10/31.
 */
public class MyOrderFragment extends BaseTabPageViewFragment {
    private View slideshowView,layMainMenu,layOrder;

    @Override
    protected ViewGroup creatContetView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.main_myorder_fragment,container,false);
        initTitleBar(v);
        return v;
    }

    @Override
    protected void addTabs(TabHost mTabHost, TabsAdapter mTabsAdapter) {
        Bundle data=new Bundle();
        data.putInt(OrderListFragment.BUNDLE_KEY_CONDITION_ORDER_STATUS, NetDataConstants.CONDITION_ORDER_STATUS_NORMAL);
        mTabsAdapter.addTab(mTabHost.newTabSpec("可使用").setIndicator("可使用"),
                OrderListFragment.class, data);
        data=new Bundle();
        data.putInt(OrderListFragment.BUNDLE_KEY_CONDITION_ORDER_STATUS, NetDataConstants.CONDITION_ORDER_STATUS_UNPAY);
        mTabsAdapter.addTab(mTabHost.newTabSpec("待付款").setIndicator("待付款"),
                OrderListFragment.class, data);
        data=new Bundle();
        data.putInt(OrderListFragment.BUNDLE_KEY_CONDITION_ORDER_STATUS, NetDataConstants.CONDITION_ORDER_STATUS_REFUND);
        mTabsAdapter.addTab(mTabHost.newTabSpec("退款单").setIndicator("退款单"),
                OrderListFragment.class, data);
    }
    private void initTitleBar(View v){
        View layTitleBar=v.findViewById(R.id.title_bar);
        TextView tvTitle= (TextView) v.findViewById(R.id.tvTitle);
        View btnLeft=v.findViewById(R.id.btn_back_wx);
        View btnRight=v.findViewById(R.id.notify);
        tvTitle.setText("订单");
        btnRight.setVisibility(View.GONE);
        btnLeft.setVisibility(View.INVISIBLE);
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();
            }
        });
    }

}
