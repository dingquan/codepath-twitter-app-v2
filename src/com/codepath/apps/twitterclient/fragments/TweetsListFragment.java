package com.codepath.apps.twitterclient.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.TwitterApp;
import com.codepath.apps.twitterclient.TwitterClient;
import com.codepath.apps.twitterclient.adapter.TweetArrayAdapter;
import com.codepath.apps.twitterclient.listeners.EndlessScrollListener;
import com.codepath.apps.twitterclient.models.Tweet;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class TweetsListFragment extends Fragment {

	protected List<Tweet> tweets;
	protected ArrayAdapter<Tweet> aTweets;
	protected PullToRefreshListView lvTweets;
	
	protected TwitterClient twitterClient;
	
	protected Long minId = Long.MAX_VALUE;
	protected Long maxId = 1L;
	
	// Store instance variables
	protected String title;
	protected int page;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//inflate layout
		View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
		
		lvTweets = (PullToRefreshListView)v.findViewById(R.id.lvTweets);
		lvTweets.setAdapter(aTweets);

		setupHandlers();
		
		//return view
		return v;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		tweets = new ArrayList<Tweet>();
		aTweets = new TweetArrayAdapter(getActivity(), tweets);

		twitterClient = TwitterApp.getRestClient();
		
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

	protected void fetchMoreTimeline() {
		// TODO Auto-generated method stub
		
	}

	protected void refreshTimeline() {
		// TODO Auto-generated method stub
		
	}
	
	protected void findMinMaxId(){
		minId = Long.MAX_VALUE;
		maxId = 1L;

		if (aTweets.getCount() > 0){
			for (Tweet tweet : tweets){
				Long uid = tweet.getUid();
				if (uid < minId){
					minId = uid;
				}
				if (uid > maxId){
					maxId = uid;
				}
			}
		}
	}
}
