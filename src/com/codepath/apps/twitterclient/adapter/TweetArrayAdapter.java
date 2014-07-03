package com.codepath.apps.twitterclient.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
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
    public int getViewTypeCount() {
		return Tweet.TYPE.values().length;
	}
	
	@Override
	public int getItemViewType(int position) {
		return getItem(position).getType().ordinal();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Tweet tweet = getItem(position);
		
		View v;
		if (convertView == null){
			int type = getItemViewType(position);
			v = getInflatedLayoutForType(type, parent);
		}
		else{
			v = convertView;
		}
		
		ImageView ivProfileImage = (ImageView)v.findViewById(R.id.ivProfileImage);
		TextView tvUserName = (TextView)v.findViewById(R.id.tvUserName);
		TextView tvScreenName = (TextView)v.findViewById(R.id.tvScreenName);
		TextView tvBody = (TextView)v.findViewById(R.id.tvBody);
		TextView tvTimeStamp = (TextView)v.findViewById(R.id.tvTimeStamp);
		TextView tvRetweetCnt = (TextView)v.findViewById(R.id.tvRetweetCnt);
		TextView tvFavoriteCnt = (TextView)v.findViewById(R.id.tvFavoriteCnt);
		ImageView ivFavorite = (ImageView)v.findViewById(R.id.ivFavorite);
		ImageView ivRetweet = (ImageView)v.findViewById(R.id.ivRetweet);
		ImageView ivReply = (ImageView)v.findViewById(R.id.ivReply);
		
		ivProfileImage.setImageResource(android.R.color.transparent);
		ivProfileImage.setTag(tweet.getUserId());
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(tweet.getUser().getProfileImageUrl(), ivProfileImage);
		tvUserName.setText("@" + tweet.getUser().getScreenName());
		tvScreenName.setText(tweet.getUser().getScreenName());
		tvBody.setText(tweet.getBody());
		tvTimeStamp.setText(tweet.getRelativeTimeAgo());
		tvRetweetCnt.setText(tweet.getRetweetCount() != null ? tweet.getRetweetCount().toString() : "");
		tvFavoriteCnt.setText(tweet.getFavoriteCount() != null ? tweet.getFavoriteCount().toString() : "");
		if (tweet.getFavorated() == true){
			ivFavorite.setImageResource(R.drawable.ic_unfavorite);
		}
		else{
			ivFavorite.setImageResource(R.drawable.ic_favorite);
		}
		//hack, need to pass multiple values through the tag
		Object[] tagObjs = new Object[]{Integer.valueOf(position), tweet};
		ivFavorite.setTag(tagObjs);
		ivRetweet.setTag(tagObjs);
		ivReply.setTag(tweet);
		
		if (tweet.getType().equals(Tweet.TYPE.IMAGE)){
			ImageView ivTweetImage = (ImageView)v.findViewById(R.id.ivTweetImage);
			imageLoader.displayImage(tweet.getImageUrl(), ivTweetImage);
		}
		
		return v;
	}
	
	// Given the item type, responsible for returning the correct inflated XML
	// layout file
	private View getInflatedLayoutForType(int type, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		Log.d("DEBUG", "### adapter tweet type: " + type);
		if (type == Tweet.TYPE.TEXT.ordinal()) {
			return inflater.inflate(R.layout.tweet_item, parent, false);
		} else if (type == Tweet.TYPE.IMAGE.ordinal()) {
			return inflater.inflate(R.layout.tweet_item_image, parent, false);
		} else if (type == Tweet.TYPE.VIDEO.ordinal()) {
			return inflater.inflate(R.layout.tweet_item_video, parent, false);
		} else {
			return null;
		}
}
}
