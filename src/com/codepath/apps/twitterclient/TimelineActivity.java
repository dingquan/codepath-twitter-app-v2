package com.codepath.apps.twitterclient;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.codepath.apps.twitterclient.fragments.HomeTimelineFragment;
import com.codepath.apps.twitterclient.fragments.MentionsTimelineFragment;
import com.codepath.apps.twitterclient.listeners.FragmentTabListener;

public class TimelineActivity extends FragmentActivity {
	private static final int TWEET_REQUEST_CODE = 10;
//	private static final int PROFILE_REQUEST_CODE = 20;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		setupTabs();
	}
	
	private void setupTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);

		Tab tab1 = actionBar
			.newTab()
			.setText("Home")
			.setIcon(R.drawable.ic_action_home)
			.setTag("HomeTimelineFragment")
			.setTabListener(
				new FragmentTabListener<HomeTimelineFragment>(R.id.flContainer, this, "Home",
						HomeTimelineFragment.class));

		actionBar.addTab(tab1);
		actionBar.selectTab(tab1);

		Tab tab2 = actionBar
			.newTab()
			.setText("Mentions")
			.setIcon(R.drawable.ic_action_notification)
			.setTag("MentionsTimelineFragment")
			.setTabListener(
			    new FragmentTabListener<MentionsTimelineFragment>(R.id.flContainer, this, "Metions",
			    		MentionsTimelineFragment.class));

		actionBar.addTab(tab2);
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_timeline, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    
    public void composeTweet(MenuItem mi){
		Intent i = new Intent(TimelineActivity.this, ComposeActivity.class);
		startActivityForResult(i, TWEET_REQUEST_CODE);
    }
    
    public void onProfile(MenuItem mi){
		Intent i = new Intent(TimelineActivity.this, ProfileActivity.class);
		startActivity(i);
    }
    
    public void showProfile(View v){
    	Intent i = new Intent(TimelineActivity.this, ProfileActivity.class);
    	startActivity(i);
    }
    
}
