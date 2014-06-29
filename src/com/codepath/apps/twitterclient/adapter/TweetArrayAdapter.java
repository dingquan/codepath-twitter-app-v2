package com.codepath.apps.twitterclient.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclient.R;
import com.codepath.apps.twitterclient.R.id;
import com.codepath.apps.twitterclient.R.layout;
import com.codepath.apps.twitterclient.models.Tweet;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetArrayAdapter extends ArrayAdapter<Tweet> {

	public TweetArrayAdapter(Context context, List<Tweet> objects) {
		super(context, 0, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Tweet tweet = getItem(position);
		
		View v;
		if (convertView == null){
			LayoutInflater inflater = LayoutInflater.from(getContext());
			v = inflater.inflate(R.layout.tweet_item, parent, false);
		}
		else{
			v = convertView;
		}
		
		ImageView ivProfileImage = (ImageView)v.findViewById(R.id.ivProfileImage);
		TextView tvUserName = (TextView)v.findViewById(R.id.tvUserName);
		TextView tvScreenName = (TextView)v.findViewById(R.id.tvScreenName);
		TextView tvBody = (TextView)v.findViewById(R.id.tvBody);
		TextView tvTimeStamp = (TextView)v.findViewById(R.id.tvTimeStamp);
		ivProfileImage.setImageResource(android.R.color.transparent);
		ImageLoader imageLoader = ImageLoader.getInstance();
		
		imageLoader.displayImage(tweet.getUser().getProfileImageUrl(), ivProfileImage);
		tvUserName.setText("@" + tweet.getUser().getScreenName());
		tvScreenName.setText(tweet.getUser().getScreenName());
		tvBody.setText(tweet.getBody());
		tvTimeStamp.setText(tweet.getRelativeTimeAgo());
		
		return v;
	}
}
