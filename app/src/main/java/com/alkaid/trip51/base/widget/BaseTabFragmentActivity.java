package com.alkaid.trip51.base.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import com.alkaid.base.common.LogUtil;
import com.alkaid.trip51.R;

import java.util.HashMap;

/**
 * Created by alkaid on 2015/10/29.
 */
public class BaseTabFragmentActivity extends BaseFragmentActivity{
    protected TabHost mTabHost;
    protected TabManager mTabManager;
    protected BaseFragment mLastFragment;

    public void addTab(final String label, int tabLayId, Class<?> paramClass, Bundle bundle)
    {
        if (label == null) {
            throw new IllegalArgumentException("title cann't be null!");
        }
        View tabView = new LabelIndicatorStrategy(this, label, tabLayId).createIndicatorView(this.mTabHost);
        this.mTabManager.addTab(this.mTabHost.newTabSpec(label).setIndicator(tabView), paramClass, bundle);
        tabView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if ((event.getAction() == MotionEvent.ACTION_UP) && (!label.equals(BaseTabFragmentActivity.this.mTabHost.getCurrentTabTag()))) {
                    BaseTabFragmentActivity.this.setGaPageNameByTitle(label);
//                    GAHelper.instance().contextStatisticsEvent(BaseTabFragmentActivity.this, "tab", label, 2147483647, "tap");
//                    GAHelper.instance().setGAPageName(BaseTabFragmentActivity.this.getGAPageName());
//                    GAHelper.instance().setRequestId(BaseTabFragmentActivity.this, UUID.randomUUID().toString(), null, false);
                }
                return false;
            }
        });
    }

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setOnContentView();
        this.mTabHost = ((TabHost)findViewById(android.R.id.tabhost));
        this.mTabHost.setup();
        this.mTabManager = new TabManager(this, this.mTabHost, R.id.realtabcontent);
        setTabWidgetBackground(0);
    }

    @Override
    protected void onRestoreInstanceState(Bundle paramBundle)
    {
        super.onRestoreInstanceState(paramBundle);
        this.mTabHost.setCurrentTabByTag(paramBundle.getString("tab"));
    }

    @Override
    protected void onSaveInstanceState(Bundle paramBundle)
    {
        super.onSaveInstanceState(paramBundle);
        paramBundle.putString("tab", this.mTabHost.getCurrentTabTag());
    }

    public void onTabChanged(String paramString) {}
    public void onTabChanged(Fragment fragment) {
        mLastFragment= (BaseFragment) fragment;
    }

    protected void setGaPageNameByTitle(String paramString) {}

    protected void setOnContentView()
    {
        super.setContentView(R.layout.fragment_tabs);
    }

    protected void setTabWidgetBackground(int paramInt)
    {
        if (paramInt > 0) {
            this.mTabHost.getTabWidget().setBackgroundResource(paramInt);
        }
    }

    public static class TabManager
            implements TabHost.OnTabChangeListener
    {
        private final BaseTabFragmentActivity mActivity;
        private final int mContainerId;
        TabInfo mLastTab;
        private final TabHost mTabHost;
        private final HashMap<String, TabInfo> mTabs = new HashMap();

        public TabManager(BaseTabFragmentActivity act, TabHost tabHost, int containerId)
        {
            this.mActivity = act;
            this.mTabHost = tabHost;
            this.mContainerId = containerId;
            this.mTabHost.setOnTabChangedListener(this);
        }

        public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle bundle)
        {
            tabSpec.setContent(new DummyTabFactory(this.mActivity));
            String tag = tabSpec.getTag();
            TabInfo info = new TabInfo(tag, clss, bundle);
            info.fragment = this.mActivity.getSupportFragmentManager().findFragmentByTag(tag);
            if ((info.fragment != null) && (!info.fragment.isHidden()) /*&& !info.fragment.isDetached()*/)
            {
                LogUtil.v("addTab "+tag+": fragment isn't null,will hide this fragment!");
                //TODO 当从订单Fragment进入其他界面APP崩溃后会自动重启(还不知道为什么)  重启后onCreat()里添加订单Fragment发现Fragment已存在会执行下面的隐藏Fragment的代码，导致白屏
                //1.为什么会自动重启 2.为什么Fragment已存在(猜测是Android恢复现场自动创建了Fragment)
//                FragmentTransaction transaction = this.mActivity.getSupportFragmentManager().beginTransaction();
////                transaction.detach(info.fragment);
//                transaction.hide(info.fragment);
//                transaction.commitAllowingStateLoss();
            }
            this.mTabs.put(tag, info);
            this.mTabHost.addTab(tabSpec);
        }

        public void onTabChanged(String tag)
        {
            LogUtil.v("onTabChanged tag="+tag);
            TabInfo newTab = (TabInfo)this.mTabs.get(tag);
            FragmentTransaction transaction;
            if (this.mLastTab != newTab)
            {
                transaction = this.mActivity.getSupportFragmentManager().beginTransaction();
                if(null==newTab.fragment) newTab.fragment = Fragment.instantiate(this.mActivity, newTab.clss.getName(), newTab.args);
                transaction.replace(this.mContainerId,newTab.fragment,newTab.tag).commit();
                this.mLastTab=newTab;
                //以下方法在Fragment嵌套时会出现多次切换后原一级Fragment无法显示的问题
//                if ((this.mLastTab != null) && (this.mLastTab.fragment != null)) {
//                    transaction.hide(this.mLastTab.fragment);
//                }
//                if (newTab == null) {
//                    LogUtil.i("onTabChanged with tabId:" + tag + ", newTab is null");
//                    return;
//                }
//                if (newTab.fragment != null) {
//                    transaction.show(newTab.fragment);
//                    LogUtil.i("onTabChanged with tabId:" + tag + ", show fragment success");
//                    return;
//                }
//                newTab.fragment = Fragment.instantiate(this.mActivity, newTab.clss.getName(), newTab.args);
//                transaction.add(this.mContainerId, newTab.fragment, newTab.tag);
//                LogUtil.i( "onTabChanged with tabId:" + tag + ", newTab.fragment is null, newTab.tag is " + newTab.tag);
//                this.mLastTab = newTab;
//                transaction.commitAllowingStateLoss();
//                this.mActivity.getSupportFragmentManager().executePendingTransactions();
                this.mActivity.onTabChanged(newTab.fragment);
                this.mActivity.onTabChanged(tag);
            }
        }

        static class DummyTabFactory
                implements TabHost.TabContentFactory
        {
            private final Context mContext;

            public DummyTabFactory(Context paramContext)
            {
                this.mContext = paramContext;
            }

            public View createTabContent(String tag)
            {
                View localView = new View(this.mContext);
                localView.setMinimumWidth(0);
                localView.setMinimumHeight(0);
                return localView;
            }
        }

        static final class TabInfo
        {
            final Bundle args;
            final Class<?> clss;
            Fragment fragment;
            final String tag;

            TabInfo(String tag, Class<?> clss, Bundle bundle)
            {
                this.tag = tag;
                this.clss = clss;
                this.args = bundle;
            }
        }
    }

    static class LabelIndicatorStrategy
    {
        private Context mContext;
        private int mIndicatorView;
        private final CharSequence mLabel;

        public LabelIndicatorStrategy(Context context, CharSequence label)
        {
            this.mContext = context;
            this.mLabel = label;
        }

        public LabelIndicatorStrategy(Context context, CharSequence label, int layId)
        {
            this(context, label);
            this.mIndicatorView = layId;
        }

        public View createIndicatorView(TabHost tabHost)
        {
//            if (this.mIndicatorView == 0) {
//                this.mIndicatorView = R.layout.tab_indicator_holo;
//            }
            View localView = LayoutInflater.from(mContext).inflate(this.mIndicatorView, tabHost.getTabWidget(), false);
            ((TextView)localView.findViewById(android.R.id.title)).setText(this.mLabel);
            return localView;
        }
    }
}
