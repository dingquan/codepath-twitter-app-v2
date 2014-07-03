package com.codepath.apps.twitterclient.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.format.DateUtils;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Column.ForeignKeyAction;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name="tweets")
public class Tweet extends Model{
	public enum TYPE {TEXT, IMAGE, VIDEO};
	
	@Column
	private String body;
	@Column(unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private Long uid;
	@Column
	private String createdAt;
	@Column(name = "User", onUpdate = ForeignKeyAction.CASCADE, onDelete = ForeignKeyAction.CASCADE)
	private User user;
	@Column
	private Long userId;
	@Column
	private Long retweetCount;
	@Column
	private Boolean favorated;
	@Column
	private Long favoriteCount;
	@Column
	private TYPE type = TYPE.TEXT;
	@Column
	private String imageUrl;

	public static Tweet fromJSON(JSONObject json){
		Tweet tweet = new Tweet();
		try{
			tweet.body = json.getString("text");
			tweet.uid = json.getLong("id");
			tweet.createdAt = json.getString("created_at");
			tweet.user = User.fromJSON(json.getJSONObject("user"));
			tweet.userId = tweet.user.getUid();
			tweet.retweetCount = json.getLong("retweet_count");
			tweet.favorated = json.getBoolean("favorited");
			tweet.favoriteCount = json.getLong("favorite_count");
			tweet.imageUrl = getMediaUrl(json);
			if (!tweet.imageUrl.isEmpty()){
				tweet.type = TYPE.IMAGE;
			}
			Log.d("DEBUG", "### Tweet, imageUrl: " + tweet.imageUrl + ", type: " + tweet.getType());
		}catch(JSONException e){
			e.printStackTrace();
			return null;
		}
		return tweet;
	}
	
	public static List<Tweet> fromJSONArray(JSONArray json){
		List<Tweet> tweets = new ArrayList<Tweet>();
		for (int i = 0; i < json.length(); i++){
			JSONObject tweetJson = null;
			try{
				tweetJson = json.getJSONObject(i);
			}
			catch(JSONException e){
				e.printStackTrace();
				continue;
			}
			
			Tweet tweet = Tweet.fromJSON(tweetJson);
			if (tweet != null){
				tweets.add(tweet);
			}
		}
		return tweets;
	}
	
	private static String getMediaUrl(JSONObject json){
		try {
			JSONObject entities = json.getJSONObject("entities");
			if (entities.has("media")){
				JSONObject media = entities.getJSONArray("media").getJSONObject(0);
				if (media.has("media_url")){
					return media.getString("media_url");
				}
			}
		} catch (JSONException e) {
			Log.e("Tweet", e.getMessage(), e);
		}
		return "";
	}

	public String toString(){
		return body;
	}

	public String getRelativeTimeAgo() {
		String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
		SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
		sf.setLenient(true);
	 
		String relativeDate = "";
		try {
			long dateMillis = sf.parse(createdAt).getTime();
			relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
					System.currentTimeMillis(), DateUtils.FORMAT_ABBREV_ALL).toString();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	 
		return relativeDate;
	}
	
	public static Tweet findById(Long id){
		return new Select().from(Tweet.class).where("uid = ?", id).executeSingle();
	}
	
	public static List<Tweet> findAll(){
		return new Select().from(Tweet.class).orderBy("uid DESC").execute();
	}
	
	public static void saveAll(List<Tweet> tweets){
		ActiveAndroid.beginTransaction();
		try {
			for (Tweet tweet: tweets) {
				tweet.user.save();
				tweet.save();
			}
			ActiveAndroid.setTransactionSuccessful();
		} finally {
			ActiveAndroid.endTransaction();
		}
	}

	public String getBody() {
		return body;
	}

	public Long getUid() {
		return uid;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public User getUser() {
		return user;
	}
	
	public void setUser(User user){
		this.user = user;
	}
	
	public Long getUserId(){
		return userId;
	}
	
	public Long getRetweetCount() {
		return retweetCount;
	}

	public Boolean getFavorated() {
		return favorated;
	}
	
	public void setFavorited(Boolean favorited){
		this.favorated = favorited;
	}

	public Long getFavoriteCount() {
		return favoriteCount;
	}
	
	public void setFavoriteCount(Long favoriteCount){
		this.favoriteCount = favoriteCount;
	}

	public TYPE getType() {
		return type;
	}

	public String getImageUrl() {
		return imageUrl;
	}
}
