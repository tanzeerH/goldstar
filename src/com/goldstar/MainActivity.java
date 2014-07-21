package com.goldstar;

import java.util.HashMap;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;

import com.goldstar.fragment.ContactFragment;
import com.goldstar.fragment.FacebookFragment;

public class MainActivity extends FragmentActivity implements OnTabChangeListener {

	private TabHost mTabHost;
	private HashMap mapTabInfo = new HashMap();
	private TabInfo mLastTab = null;

	ActionBar actionBar;
	private String[] tabList = { "Facebook", "Contacts" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initialiseTabHost(savedInstanceState);
        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab")); //set the tab as per the saved state
        }
		/*actionBar = getActionBar();
		actionBar.setTitle("Find Your Friends");
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		for (String tab_name : tabList) {
			actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
		}
		actionBar.setSelectedNavigationItem(1);*/

	}
	private class TabInfo {
        private String tag;
        private Class clss;
        private Bundle args;
        private Fragment fragment;
        TabInfo(String tag, Class clazz, Bundle args) {
            this.tag = tag;
            this.clss = clazz;
            this.args = args;
        }

   }
	 private void initialiseTabHost(Bundle args) {
	        mTabHost = (TabHost)findViewById(android.R.id.tabhost);
	        mTabHost.setup();
	        TabInfo tabInfo = null;
	        MainActivity.addTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab1").setIndicator("Facebook"), ( tabInfo = new TabInfo("Tab1", FacebookFragment.class, args)));
	        this.mapTabInfo.put(tabInfo.tag, tabInfo);
	        MainActivity.addTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab2").setIndicator("Contacts"), ( tabInfo = new TabInfo("Tab2", ContactFragment.class, args)));
	        this.mapTabInfo.put(tabInfo.tag, tabInfo);
	       
	        // Default to first tab
	        this.onTabChanged("Tab1");
	        //
	        mTabHost.setOnTabChangedListener(this);
	    }
	 private static void addTab(MainActivity activity, TabHost tabHost, TabHost.TabSpec tabSpec, TabInfo tabInfo) {
	        // Attach a Tab view factory to the spec
	        tabSpec.setContent(activity.new TabFactory(activity));
	        String tag = tabSpec.getTag();
	 
	        // Check to see if we already have a fragment for this tab, probably
	        // from a previously saved state.  If so, deactivate it, because our
	        // initial state is that a tab isn't shown.
	        tabInfo.fragment = activity.getSupportFragmentManager().findFragmentByTag(tag);
	        if (tabInfo.fragment != null && !tabInfo.fragment.isDetached()) {
	            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
	            ft.detach(tabInfo.fragment);
	            ft.commit();
	            activity.getSupportFragmentManager().executePendingTransactions();
	        }
	 
	        tabHost.addTab(tabSpec);
	    }
	 class TabFactory implements TabContentFactory {
		 
	        private final Context mContext;
	 
	        /**
	         * @param context
	         */
	        public TabFactory(Context context) {
	            mContext = context;
	        }
	 
	        /** (non-Javadoc)
	         * @see android.widget.TabHost.TabContentFactory#createTabContent(java.lang.String)
	         */
	        public View createTabContent(String tag) {
	            View v = new View(mContext);
	            v.setMinimumWidth(0);
	            v.setMinimumHeight(0);
	            return v;
	        }
	 
	    }
	 public void onTabChanged(String tag) {
	        TabInfo newTab = (TabInfo)this.mapTabInfo.get(tag);
	        if (mLastTab != newTab) {
	            FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
	            if (mLastTab != null) {
	                if (mLastTab.fragment != null) {
	                    ft.detach(mLastTab.fragment);
	                }
	            }
	            if (newTab != null) {
	                if (newTab.fragment == null) {
	                    newTab.fragment = Fragment.instantiate(this,
	                            newTab.clss.getName(), newTab.args);
	                    ft.add(android.R.id.tabcontent, newTab.fragment, newTab.tag);
	                } else {
	                    ft.attach(newTab.fragment);
	                }
	            }
	 
	            mLastTab = newTab;
	            ft.commit();
	            this.getSupportFragmentManager().executePendingTransactions();
	        }
	    }
	/*@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		if (tab.getPosition() == 1) {
			ContactFragment contactFragment = new ContactFragment();
			ft.replace(android.R.id.content, contactFragment);
		}
		if (tab.getPosition() == 0) {
			FacebookFragment facebookFragment = new FacebookFragment();
			ft.replace(android.R.id.content, facebookFragment);
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}*/

}
