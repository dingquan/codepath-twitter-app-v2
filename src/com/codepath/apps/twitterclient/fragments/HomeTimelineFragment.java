package com.codepath.apps.twitterclient.fragments;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.codepath.apps.twitterclient.listeners.EndlessScrollListener;
import com.codepath.apps.twitterclient.models.Tweet;
import com.codepath.apps.twitterclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class HomeTimelineFragment extends TweetsListFragment {
	
	// newInstance constructor for creating fragment with arguments
	public static HomeTimelineFragment newInstance(int page, String title) {
		HomeTimelineFragment homeFragment = new HomeTimelineFragment();
		Bundle args = new Bundle();
		args.putInt("someInt", page);
		args.putString("someTitle", title);
		homeFragment.setArguments(args);
		return homeFragment;
	}
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		page = getArguments().getInt("someInt", 0);
		title = getArguments().getString("someTitle");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		fetchingSavedTweets();
		return v;
	}
	
	private void fetchingSavedTweets(){
		pbLoading.setVisibility(ProgressBar.VISIBLE);
		List<Tweet> savedTweets = Tweet.findAll();
		if (savedTweets != null && !savedTweets.isEmpty()){
			for (Tweet tweet : savedTweets){
				User user = User.findById(tweet.getUserId());
				tweet.setUser(user);
			}
			aTweets.addAll(savedTweets);
		}
		pbLoading.setVisibility(ProgressBar.INVISIBLE);
	}
	
	@Override
	protected void refreshTimeline(){
		findMinMaxId();
		twitterClient.getHomeTimeline(null, maxId, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, JSONArray json) {
				List<Tweet> newTweets = Tweet.fromJSONArray(json);
				//want to keep the new tweets at the front, so we do this swap
				Tweet.saveAll(newTweets);
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

	@Override
	protected void fetchMoreTimeline(){
		findMinMaxId();
		pbLoading.setVisibility(ProgressBar.VISIBLE);
		twitterClient.getHomeTimeline(minId, null, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, JSONArray json) {
				List<Tweet> newTweets = Tweet.fromJSONArray(json);
				Tweet.saveAll(newTweets);
				aTweets.addAll(newTweets);
			}
			
			@Override
			public void onFailure(Throwable e, String s) {
				super.onFailure(e, s);
			}
			
			@Override
			public void onFinish() {
                pbLoading.setVisibility(ProgressBar.INVISIBLE);;
				super.onFinish();
			}
		});
	}

	public void insertTweetToTop(Tweet tweet) {
		aTweets.insert(tweet, 0);
		lvTweets.smoothScrollToPosition(0);
	}
	
	public void updateTweet(int position, Tweet tweet){
		tweets.set(position, tweet);
		aTweets.notifyDataSetChanged();
	}
	
}
