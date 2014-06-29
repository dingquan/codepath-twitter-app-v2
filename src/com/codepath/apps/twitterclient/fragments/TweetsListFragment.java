package com.codepath.apps.twitterclient.fragments;

import java.util.ArrayList;
import java.util.List;

import com.codepath.apps.twitterclient.EndlessScrollListener;
import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.TwitterApp;
import com.codepath.apps.twitterclient.TwitterClient;
import com.codepath.apps.twitterclient.adapter.TweetArrayAdapter;
import com.codepath.apps.twitterclient.models.Tweet;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class TweetsListFragment extends Fragment {

	protected List<Tweet> tweets;
	protected ArrayAdapter<Tweet> aTweets;
	protected PullToRefreshListView lvTweets;
	
	protected TwitterClient twitterClient;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//inflate layout
		View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
		
		lvTweets = (PullToRefreshListView)v.findViewById(R.id.lvTweets);
		lvTweets.setAdapter(aTweets);

		//return view
		return v;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		tweets = new ArrayList<Tweet>();
		aTweets = new TweetArrayAdapter(getActivity(), tweets);

		twitterClient = TwitterApp.getRestClient();
	}

	public void adAll(ArrayList<Tweet> tweets){
		aTweets.addAll(tweets);
	}

}
