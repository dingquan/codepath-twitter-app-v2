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

public class MentionsTimelineFragment extends TweetsListFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		page = getArguments().getInt("someInt", 0);
		title = getArguments().getString("someTitle");
	}
	
	// newInstance constructor for creating fragment with arguments
	public static MentionsTimelineFragment newInstance(int page, String title) {
		MentionsTimelineFragment mentionsFagment = new MentionsTimelineFragment();
		Bundle args = new Bundle();
		args.putInt("someInt", page);
		args.putString("someTitle", title);
		mentionsFagment.setArguments(args);
		return mentionsFagment;
	}
	
	@Override
	protected void refreshTimeline(){
		findMinMaxId();
		twitterClient.getMentionsTimeline(null, maxId, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, JSONArray json) {
				List<Tweet> newTweets = Tweet.fromJSONArray(json);
				//want to keep the new tweets at the front, so we do this swap
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
		twitterClient.getMentionsTimeline(minId, null, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, JSONArray json) {
				List<Tweet> newTweets = Tweet.fromJSONArray(json);
				aTweets.addAll(newTweets);
			}
			
			@Override
			public void onFailure(Throwable e, String s) {
				super.onFailure(e, s);
			}
			
			@Override
			public void onFinish() {
                pbLoading.setVisibility(ProgressBar.INVISIBLE);
				super.onFinish();
			}
		});
	}
	
}
