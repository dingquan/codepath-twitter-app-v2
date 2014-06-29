package com.codepath.apps.twitterclient;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
    public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
    public static final String REST_CONSUMER_KEY = "YC69DFPYNxkoNDDiBDG5oLqrC";       // Change this
    public static final String REST_CONSUMER_SECRET = "5PbpQpKyEwbBPDOQBYIV1cT66dDZoYrcOzBt6IQMwyb3dBTpY7"; // Change this
    public static final String REST_CALLBACK_URL = "oauth://cpbasictwitterapp"; // Change this (here and in manifest)
    
    public TwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }
    
    public void getHomeTimeline(Long minId, Long maxId, AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();

		if (minId != null){
			if (!minId.equals(Long.MAX_VALUE)) {
				params.put("max_id", Long.valueOf(minId - 1).toString());
			}
			else{
				params.put("since_id", "1");
			}
		}
		if (maxId != null){
			params.put("since_id",  maxId.toString());
		}
		Log.d("DEBUG", "apiURl: " + apiUrl + "params: " + params.toString());
		client.get(apiUrl, params, handler);
    }
    
    public void getUserTimeline(Long userId, AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("statuses/user_timeline.json");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = null;
		
		if (userId != null && userId != -1L){
			params = new RequestParams();
			params.put("user_id", userId.toString());
		}
		client.get(apiUrl, params, handler);
    }
    
    public void getTweetById(String id, AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("statuses/show.json");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("id", id);
		client.get(apiUrl, params, handler);

    }
    
    public void postTweet(String tweet, AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("statuses/update.json");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("status", tweet);
		client.post(apiUrl, params, handler);
	}
    
    public void getUserProfile(Long userId, AsyncHttpResponseHandler handler){
    	
		String apiUrl = null;
		RequestParams params = null;
		
		if (userId == null || userId == -1L){
			apiUrl = getApiUrl("account/verify_credentials.json");
		}
		else{
			apiUrl = getApiUrl("users/show.json");
			params = new RequestParams();
			params.put("user_id", userId.toString());
		}
		client.get(apiUrl, params, handler);
	}
    
	public void getMentionsTimeline(Long minId, Long maxId, JsonHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/mentions_timeline.json");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();

		if (minId != null){
			if (!minId.equals(Long.MAX_VALUE)) {
				params.put("max_id", Long.valueOf(minId - 1).toString());
			}
			else{
				params.put("since_id", "1");
			}
		}
		if (maxId != null){
			params.put("since_id",  maxId.toString());
		}
		Log.d("DEBUG", "apiURl: " + apiUrl + "params: " + params.toString());
		client.get(apiUrl, params, handler);
	}
    
    /* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
     * 	  i.e getApiUrl("statuses/home_timeline.json");
     * 2. Define the parameters to pass to the request (query or body)
     *    i.e RequestParams params = new RequestParams("foo", "bar");
     * 3. Define the request method and make a call to the client
     *    i.e client.get(apiUrl, params, handler);
     *    i.e client.post(apiUrl, params, handler);
     */
}