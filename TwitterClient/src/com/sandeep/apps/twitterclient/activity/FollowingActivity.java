package com.sandeep.apps.twitterclient.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import com.sandeep.apps.twitterclient.R;
import com.sandeep.apps.twitterclient.TwitterClientApp;
import com.sandeep.apps.twitterclient.adapter.FollowingViewAdapter;
import com.sandeep.apps.twitterclient.models.User;

import eu.erikw.PullToRefreshListView;

public class FollowingActivity extends Activity {

	private FollowingViewAdapter adapter;
	private List<User> users;
	private PullToRefreshListView lvFollowing;
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	        finish();
	    }
	    return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_following);
		
		// enable the home navigation with up
		getActionBar().setDisplayHomeAsUpEnabled(true);

		lvFollowing = (PullToRefreshListView)findViewById(R.id.lvFollowing);
		
		users = new ArrayList<>();
		adapter = new FollowingViewAdapter(this, R.layout.item_following, users);
		lvFollowing.setAdapter(adapter);
		
		String screenName = getIntent().getExtras().getString("screenName");
		
		TwitterClientApp.getRestClient().getFollowingUsers(this, adapter, screenName);		
		
	}
}
