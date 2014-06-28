package com.codepath.apps.twitterclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterclient.models.User;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ComposeActivity extends Activity {

	private SharedPreferences prefs;
	private ImageView ivProfileImage;
	private TextView tvScreenName;
	private TextView tvUserName;
	private EditText etTweet;
	private Menu menu;
	
	private User user;
	
	private TwitterClient twitterClient;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose);
		
		prefs = this.getSharedPreferences("com.codepath.twitterclient", Context.MODE_PRIVATE);
		twitterClient = TwitterApp.getRestClient();
		setupviews();
		setupHandlers();
		loadLoginUserData();
	}
	
    private void setupviews() {
		ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
		tvScreenName = (TextView) findViewById(R.id.tvScreenName);
		tvUserName = (TextView) findViewById(R.id.tvUserName);
		etTweet = (EditText) findViewById(R.id.etTweet);
	}
    
    private void setupHandlers(){
		etTweet.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				//get remaining number of characters allowed and display it in the action bar
				Integer remainCount = 140 - s.length();
				MenuItem miCount = menu.findItem(R.id.remain_count);
				miCount.setTitle(remainCount.toString());
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
    }
    
    private void loadLoginUserData(){
		Long userId = prefs.getLong("userId", -1L);
		if (userId == null || userId.equals(-1L)){
			Log.e("ERROR", "profile data not availabel");
		}
		else{
			user = (User) User.findById(userId);
			if (user == null){
				Log.e("ERROR", "Can't find user with userId=" + userId);
			}
			ivProfileImage.setImageResource(android.R.color.transparent);
			ImageLoader imageLoader = ImageLoader.getInstance();
			imageLoader.displayImage(user.getProfileImageUrl(), ivProfileImage);
			
			tvScreenName.setText(user.getScreenName());
			tvUserName.setText("@" + user.getName());
		}
    }

    
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tweet, menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }
    
	public void postTweet(MenuItem mi) {
		String tweetBody = etTweet.getText().toString();
		// Prepare data intent
		Intent data = new Intent();
		// Pass relevant data back as a result
		data.putExtra("tweet", tweetBody);
		// Activity finished ok, return the data
		setResult(RESULT_OK, data); // set result code and bundle data for
									// response
		finish(); // closes the activity, pass data to parent
	}
}
