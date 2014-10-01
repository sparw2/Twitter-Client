package com.sandeep.apps.twitterclient;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sandeep.apps.twitterclient.adapter.TimelineViewAdapter;
import com.sandeep.apps.twitterclient.fragments.ComposeFragment.OnTweetPostedListener;
import com.sandeep.apps.twitterclient.helpers.NetworkConnectivityHelper;
import com.sandeep.apps.twitterclient.models.Tweet;
import com.sandeep.apps.twitterclient.models.User;

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
	public static final String REST_CONSUMER_KEY = "JwVQnVnQq7Dhc6ZiiAqOHZxp8";       // Change this
	public static final String REST_CONSUMER_SECRET = "qKi2aJBXgAiFel3g2nTmohgg5VXQCKyjh13Tac8A0T7HZVu5oZ"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://twitterclient"; // Change this (here and in manifest)

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	
	public void postStatusUpdate(final Activity activity, final String status, final String replyId, 
			final OnTweetPostedListener listener){
		
		// check if the network is available
		if(!NetworkConnectivityHelper.isNetworkAvailable(activity)){
			Toast.makeText(activity, "Failure in posting tweet - Network Unavailable", Toast.LENGTH_SHORT).show();
			return;
		}
		
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", status);
		
		if(replyId != null && !replyId.isEmpty()){
			params.put("in_reply_to_status_id", replyId);
		}
		
		client.post(apiUrl, params, new JsonHttpResponseHandler(){
			
			@Override
			public void onSuccess(int resStatus, JSONObject jsonObj) {
				listener.onPosted(jsonObj.optString("id"),status);
			}

			@Override
			protected void handleFailureMessage(Throwable t, String arg1) {
				t.printStackTrace();
				Toast.makeText(activity, "Failure in posting tweet", Toast.LENGTH_SHORT).show();
			}

		});
		
	}
	
	public void setFavorite(final Activity activity,String tweetId, boolean create){
		
		// check if the network is available
		if(!NetworkConnectivityHelper.isNetworkAvailable(activity)){
			Toast.makeText(activity, "Network Unavailable", Toast.LENGTH_SHORT).show();
			return;
		}
		
		String apiUrl = null;
		
		if(create){
			apiUrl = getApiUrl("favorites/create.json");
		}else{
			apiUrl = getApiUrl("favorites/destroy.json");
		}
		
		RequestParams params = new RequestParams();
		params.put("id", tweetId);
		
		client.post(apiUrl, params, new JsonHttpResponseHandler(){
			
			@Override
			public void onSuccess(int status, JSONObject jsonObj) {
				// 
			}

			@Override
			protected void handleFailureMessage(Throwable t, String arg1) {
				Toast.makeText(activity, "Unexpected failure", Toast.LENGTH_SHORT).show();
			}

		});
	}
	
	public void getUserInfo(final Activity activity){
		
		// check if the network is available
		if(!NetworkConnectivityHelper.isNetworkAvailable(activity)){
			Toast.makeText(activity, "Failure in getting user information - Network Unavailable", Toast.LENGTH_SHORT).show();
			return;
		}
		
		String apiUrl = getApiUrl("account/verify_credentials.json");
		
		client.get(apiUrl, null, new JsonHttpResponseHandler(){
			
			@Override
			public void onSuccess(int status, JSONObject jsonObj) {
				User user = User.fromJSON(jsonObj);
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
				Editor edit = prefs.edit();
				edit.putString("screenname", user.username);
				edit.putString("username", user.name);
				edit.putString("profileImage", user.profileImageURL);
				edit.commit();
			}

			@Override
			protected void handleFailureMessage(Throwable t, String arg1) {
				Toast.makeText(activity, "Failure to fetch user information", Toast.LENGTH_SHORT).show();
			}

		});
	}
	
	public void getHomeTimeline(final Activity activity,
			final TimelineViewAdapter adapter, 
			final List<Tweet> tweetList, 
			final boolean refesh, final boolean paginate){
		
		// check if the network is available
		if(!NetworkConnectivityHelper.isNetworkAvailable(activity)){
			Toast.makeText(activity, "Network Unavailable", Toast.LENGTH_SHORT).show();
			
			// load from local cache
			List<Tweet> tweetListCached = new Select().from(Tweet.class).orderBy("remoteId desc").execute();
			adapter.addAll(tweetListCached);
			return;
		}
		
		
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		
		if(refesh){
			apiUrl = apiUrl +"?since_id="+tweetList.get(0).id;	
		}else if (paginate){
			if (tweetList.size() == 0)
				// initial load hasn't completed yet
				return;
			else	
				apiUrl = apiUrl +"?max_id="+tweetList.get(tweetList.size()-1).id;
		}
		
		client.get(apiUrl, null, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(int status, JSONArray response) {
				try{
					if (refesh){
						// add in the front of the list
						tweetList.addAll(0, Tweet.fromJSONArray(response));
						adapter.notifyDataSetChanged();
					} else {
						// add at the end of the list
						tweetList.addAll(Tweet.fromJSONArray(response));
						adapter.notifyDataSetChanged();
					}
				}catch(JSONException e){
					e.printStackTrace();
					Toast.makeText(activity, "Failure to fetch user timeline", Toast.LENGTH_SHORT).show();
				}
			}
			
			@Override
			protected void handleFailureMessage(Throwable t, String arg1) {
				Toast.makeText(activity, "Failure to fetch user timeline", Toast.LENGTH_SHORT).show();
			}

		});
	}
	
	// CHANGE THIS
	// DEFINE METHODS for different API endpoints here
	public void getInterestingnessList(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("?nojsoncallback=1&method=flickr.interestingness.getList");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("format", "json");
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