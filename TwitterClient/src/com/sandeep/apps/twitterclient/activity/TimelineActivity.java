package com.sandeep.apps.twitterclient.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.sandeep.apps.twitterclient.R;
import com.sandeep.apps.twitterclient.TwitterClient;
import com.sandeep.apps.twitterclient.TwitterClientApp;
import com.sandeep.apps.twitterclient.adapter.TimelineViewAdapter;
import com.sandeep.apps.twitterclient.fragments.ComposeFragment;
import com.sandeep.apps.twitterclient.listeners.EndlessScrollListener;
import com.sandeep.apps.twitterclient.models.Tweet;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class TimelineActivity extends FragmentActivity implements ComposeFragment.OnTweetPostedListener{

	private TwitterClient client;
	private TimelineViewAdapter adapter;
	private List<Tweet> timeline;
	private PullToRefreshListView lvTimeline;
	
	private boolean isRefresh=false;
	private boolean isPaginate=false;
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_timeline, menu);
		return super.onCreateOptionsMenu(menu);
	}



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);

		lvTimeline = (PullToRefreshListView)findViewById(R.id.lvTimeline);
		
		timeline = new ArrayList<>();
		adapter = new TimelineViewAdapter(this, R.layout.activity_timeline, timeline);
		lvTimeline.setAdapter(adapter);
		
		client = TwitterClientApp.getRestClient();
		
		client.getHomeTimeline(this,adapter,timeline,isRefresh,isPaginate);

		// Set a listener to be invoked when the list should be refreshed.
		lvTimeline.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
             	isRefresh = true;
            	isPaginate = false;		
        		client.getHomeTimeline(TimelineActivity.this,adapter,timeline,isRefresh,isPaginate);
        		lvTimeline.onRefreshComplete();
            }
        });
		
		// Set a listener for scrolling 
		lvTimeline.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				isRefresh = false;
            	isPaginate = true;		
        		client.getHomeTimeline(TimelineActivity.this,adapter,timeline,isRefresh,isPaginate);
        		lvTimeline.onRefreshComplete();
			}
		});
		
		// fetch and cache the user info
		client.getUserInfo(this);
	}
	
	public void composeTweet(MenuItem item){
		 
		 FragmentManager fm = getSupportFragmentManager();
		 
		 ComposeFragment dialog = ComposeFragment.newInstance("","");
		 dialog.show(fm, "");
		
	}


	@Override
	public void onPosted(String id, String status) {
		Toast.makeText(this,"Tweet Posted", Toast.LENGTH_SHORT).show();
		
		Tweet tweet = new Tweet();
		tweet.tweetBody= status;
		tweet.id = id;
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		tweet.user.name = prefs.getString("username", "");
		tweet.user.profileImageURL = prefs.getString("profileImage", "");
		tweet.user.username = prefs.getString("screenname", "");
		
		adapter.insert(tweet, 0);
		adapter.notifyDataSetChanged();
		
		// persist the tweet
		tweet.new TweetStoreAsyncTask().execute(new Object[]{tweet});
		
   }



	@Override
	public void onFailure() {
		Toast.makeText(this,"Failure in posting the tweet", Toast.LENGTH_SHORT).show();
	}
	
}
