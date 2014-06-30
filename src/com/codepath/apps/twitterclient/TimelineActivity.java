package com.codepath.apps.twitterclient;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.codepath.apps.twitterclient.fragments.HomeTimelineFragment;
import com.codepath.apps.twitterclient.fragments.MentionsTimelineFragment;
import com.codepath.apps.twitterclient.listeners.FragmentTabListener;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimelineActivity extends FragmentActivity {
	private static final int REQUEST_CODE = 10;
	
	protected TwitterClient twitterClient;
	protected HomeTimelineFragment homeTimelineFragment;
	protected MentionsTimelineFragment mentionsTimelineFragment;
	
	private SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		
		prefs = this.getSharedPreferences("com.codepath.twitterclient", Context.MODE_PRIVATE);
		setupTabs();
		twitterClient = TwitterApp.getRestClient();
		saveLoginUserProfileData();
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
		startActivityForResult(i, REQUEST_CODE);
    }
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
			String tweetStr = data.getExtras().getString("tweet");
			postTweet(tweetStr);
		}
	}
    
    public void onProfile(MenuItem mi){
		Intent i = new Intent(TimelineActivity.this, ProfileActivity.class);
		startActivity(i);
    }
    
    public void showProfile(View v){
    	Long userId = (Long)((ImageView)v).getTag();
    	Intent i = new Intent(TimelineActivity.this, ProfileActivity.class);
    	i.putExtra("user_id", userId);
    	startActivity(i);
    }
    
	private void saveLoginUserProfileData() {
		Long userId = prefs.getLong("userId", -1L);
		if (userId != -1L) //saved before, no need to fetch again
			return;
		
		twitterClient.getUserProfile(null, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, JSONObject json) {
				User user = User.fromJSON(json);
				prefs.edit().putLong("userId", user.getUid()).commit();
				user.save();
			}

			@Override
			public void onFailure(Throwable e, String s) {
				super.onFailure(e, s);
			}

		});
	}
	
	public void postTweet(String tweetStr){
		twitterClient.postTweet(tweetStr, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, JSONObject json) {
				Tweet tweet = Tweet.fromJSON(json);
				findFragments();
				homeTimelineFragment.insertTweetToTop(tweet);
			}
			
			@Override
			public void onFailure(Throwable e, String s) {
				super.onFailure(e, s);
			}
		});
	}
	
	private void findFragments(){
		if (homeTimelineFragment == null){
			FragmentManager fm = getSupportFragmentManager();
			fm.executePendingTransactions();
			homeTimelineFragment = (HomeTimelineFragment) fm.findFragmentByTag("Home");
		}
	}
}
