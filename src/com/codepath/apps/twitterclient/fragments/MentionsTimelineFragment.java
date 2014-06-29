package com.codepath.apps.twitterclient.fragments;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.twitterclient.EndlessScrollListener;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class MentionsTimelineFragment extends TweetsListFragment {

	private static final int REQUEST_CODE = 20;

//	private SharedPreferences prefs;
	private Long minId = Long.MAX_VALUE;
	private Long maxId = 1L;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// assign your view references
//		prefs = this.getSharedPreferences("com.codepath.twitterclient", Context.MODE_PRIVATE);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		
		setupHandlers();
		saveLoginUserProfileData();
//		fetchingSavedTweets();
		
		return v;
	}
	
	private void setupHandlers(){
		lvTweets.setOnRefreshListener(new OnRefreshListener(){

			@Override
			public void onRefresh() {
				refreshTimeline();
			}
			
		});
		
		lvTweets.setOnScrollListener(new EndlessScrollListener(){

			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				// Triggered only when new data needs to be appended to the list
				// Add whatever code is needed to append new items to your
				// AdapterView
				fetchMoreTimeline();
			}
			
		});

	}
	
	private void fetchingSavedTweets(){
		List<Tweet> savedTweets = Tweet.findAll();
		if (savedTweets != null || savedTweets.isEmpty()){
			for (Tweet tweet : savedTweets){
				User user = User.findById(tweet.getUserId());
				tweet.setUser(user);
			}
			aTweets.addAll(savedTweets);
		}
	}
	
	public void refreshTimeline(){
		if (aTweets.getCount() > 0){
			minId = aTweets.getItem(aTweets.getCount()-1).getUid(); 
			maxId = aTweets.getItem(0).getUid();
		}
		else{
			minId = Long.MAX_VALUE;
			maxId = 1L;
		}
		twitterClient.getMentionsTimeline(null, maxId, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, JSONArray json) {
				List<Tweet> newTweets = Tweet.fromJSONArray(json);
				//want to keep the new tweets at the front, so we do this swap
				saveTweets(newTweets);
				newTweets.addAll(tweets);
				tweets = newTweets;
				aTweets.notifyDataSetChanged();
			}
			
			@Override
			public void onFailure(Throwable e, String s) {
				super.onFailure(e, s);
			}
			
			@Override
			public void onFinish() {
                // ...the data has come back, finish populating listview...
                // Now we call onRefreshComplete to signify refresh has finished
                lvTweets.onRefreshComplete();
				super.onFinish();
			}
		});
	}

	public void fetchMoreTimeline(){
		if (aTweets.getCount() > 0){
			minId = aTweets.getItem(aTweets.getCount()-1).getUid(); 
			maxId = aTweets.getItem(0).getUid();
		}
		else{
			minId = Long.MAX_VALUE;
			maxId = 1L;
		}
		twitterClient.getMentionsTimeline(minId, null, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, JSONArray json) {
				List<Tweet> newTweets = Tweet.fromJSONArray(json);
				saveTweets(newTweets);
				aTweets.addAll(newTweets);
			}
			
			@Override
			public void onFailure(Throwable e, String s) {
				super.onFailure(e, s);
			}
			
			@Override
			public void onFinish() {
                // ...the data has come back, finish populating listview...
                // Now we call onRefreshComplete to signify refresh has finished
                lvTweets.onRefreshComplete();
				super.onFinish();
			}
		});
	}
	
    private void saveLoginUserProfileData(){
		twitterClient.getUserProfile(new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, JSONObject json) {
				User user = User.fromJSON(json);
//				prefs.edit().putLong("userId", user.getUid()).commit();
				user.save();
			}
			
			@Override
			public void onFailure(Throwable e, String s) {
				super.onFailure(e, s);
			}
			
		});
    }

	private void saveTweets(List<Tweet> tweets){
		Tweet.saveAll(tweets);
	}
	
	public void postTweet(String tweetStr){
		twitterClient.postTweet(tweetStr, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, JSONObject json) {
				Tweet tweet = Tweet.fromJSON(json);
				aTweets.insert(tweet, 0);
			}
			
			@Override
			public void onFailure(Throwable e, String s) {
				super.onFailure(e, s);
			}
		});
	}
}