package com.goldstar;

import com.goldstar.fragment.ContactFragment;
import com.goldstar.fragment.FacebookFragment;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends Activity implements TabListener {

	ActionBar actionBar;
	private String[] tabList = { "Facebook", "Contacts" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		actionBar = getActionBar();
		actionBar.setTitle("Find Your Friends");
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		for (String tab_name : tabList) {
			actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
		}
		actionBar.setSelectedNavigationItem(1);

	}

	@Override
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

	}

}
