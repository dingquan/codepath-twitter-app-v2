package com.codepath.apps.twitterclient.fragments;

import java.util.List;

import org.json.JSONArray;

import android.os.Bundle;

import com.codepath.apps.twitterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class UserTimelineFragment extends TweetsListFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		twitterClient.getUserTimeline(new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, JSONArray json) {
				List<Tweet> newTweets = Tweet.fromJSONArray(json);
				Tweet.saveAll(newTweets);
				aTweets.addAll(newTweets);
			}
		});
	}
}
