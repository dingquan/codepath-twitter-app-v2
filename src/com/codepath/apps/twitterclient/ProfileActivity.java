package com.codepath.apps.twitterclient;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileActivity extends FragmentActivity {

	private ImageView ivProfileImage;
	private TextView tvUserName;
	private TextView tvScreenName;
	private TextView tvTweetCount;
	private TextView tvFollowingCount;
	private TextView tvFollowerCount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		setupViews();
		
		populateProfileHeader();
	}

	private void setupViews() {
		ivProfileImage = (ImageView)findViewById(R.id.ivProfileImage);
		tvUserName = (TextView)findViewById(R.id.tvUserName);
		tvScreenName = (TextView)findViewById(R.id.tvScreenName);
		tvTweetCount = (TextView)findViewById(R.id.tvTweetCount);
		tvFollowingCount = (TextView)findViewById(R.id.tvFollowingCount);
		tvFollowerCount = (TextView)findViewById(R.id.tvFollowerCount);
	}
	
	private void populateProfileHeader(){
		TwitterApp.getRestClient().getUserProfile(new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, JSONObject json) {
				User user = User.fromJSON(json);
				getActionBar().setTitle("@"+user.getScreenName());
				ImageLoader.getInstance().displayImage(user.getProfileImageUrl(), ivProfileImage);
				tvUserName.setText(user.getName());
				tvScreenName.setText("@" + user.getScreenName());
				tvTweetCount.setText(user.getStatusesCount().toString());
				tvFollowingCount.setText(user.getFriendsCount().toString());
				tvFollowerCount.setText(user.getFollowersCount().toString());
			}
			
			@Override
			public void onFailure(Throwable e, String s) {
				super.onFailure(e, s);
			}
			
		});
	}
}
