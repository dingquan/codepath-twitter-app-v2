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

		Long userId = getArguments().getLong("user_id");
		twitterClient.getUserTimeline(userId, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, JSONArray json) {
				List<Tweet> newTweets = Tweet.fromJSONArray(json);
//				Tweet.saveAll(newTweets);
				aTweets.addAll(newTweets);
			}
		});
	}
	
    // Creates a new fragment given an int and title
    // DemoFragment.newInstance(5, "Hello");
    public static UserTimelineFragment newInstance(Long userId) {
    	UserTimelineFragment fragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putLong("user_id", userId);
        fragment.setArguments(args);
        return fragment;
    }
}
