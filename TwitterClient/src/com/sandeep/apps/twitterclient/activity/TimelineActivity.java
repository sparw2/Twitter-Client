package com.sandeep.apps.twitterclient.activity;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.sandeep.apps.twitterclient.R;
import com.sandeep.apps.twitterclient.TwitterClientApp;
import com.sandeep.apps.twitterclient.adapter.TimelinePagerAdapter;
import com.sandeep.apps.twitterclient.fragments.ComposeFragment;
import com.sandeep.apps.twitterclient.models.Tweet;
import com.viewpagerindicator.TabPageIndicator;

public class TimelineActivity extends ActionBarActivity  implements ComposeFragment.OnTweetPostedListener, OnQueryTextListener{

	private TimelinePagerAdapter adapterViewPager;
	private SearchView searchView;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu_timeline, menu);
	    MenuItem searchItem = menu.findItem(R.id.search);
	    
	    searchView = (SearchView) searchItem.getActionView();
	    if (searchView != null) {
	        searchView.setOnQueryTextListener(this);
	    }
	    
	    return super.onCreateOptionsMenu(menu);
	}    

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		
		ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
		adapterViewPager = new TimelinePagerAdapter(getSupportFragmentManager());
		vpPager.setAdapter(adapterViewPager);
		
		TabPageIndicator titleIndicator = (TabPageIndicator)findViewById(R.id.titles);
		titleIndicator.setViewPager(vpPager);
		
	}
	
/*	
	private void setupTabs() {
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);
		
		Tab tab1 = actionBar
		    .newTab()
		    .setText("Home")
		    .setTag("HomeTimelineFragment")
		    .setTabListener(new SupportFragmentTabListener<HomeTimelineFragment>(R.id.flContainer, this,
                        "hometimeline", HomeTimelineFragment.class));

		actionBar.addTab(tab1);
		actionBar.selectTab(tab1);
		
		Tab tab2 = actionBar
		    .newTab()
		    .setText("Mentions")
		    .setTag("MentionsTimelineFragment")
		    .setTabListener(new SupportFragmentTabListener<MentionTimelineFragment>(R.id.flContainer, this,
                        "mentions", MentionTimelineFragment.class));
		actionBar.addTab(tab2);
		
		
	}
*/	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		
			case R.id.profile:
				// navigate to current user's profile
				TwitterClientApp.getRestClient().getUserInfo(this, true, true, "");
				return true;
			case R.id.compose:
				 FragmentManager fm = getSupportFragmentManager();
				 ComposeFragment dialog = ComposeFragment.newInstance("","");
				 dialog.show(fm, "");
				 return true;
			}	 

		return super.onOptionsItemSelected(item);	 
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
		
		adapterViewPager.homeFragment.insertTweet(tweet, 0);
		
		// persist the tweet
		//tweet.new TweetStoreAsyncTask().execute(new Object[]{tweet});
   }



	@Override
	public void onFailure() {
		Toast.makeText(this,"Failure in posting the tweet", Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onQueryTextChange(String query) {
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		TwitterClientApp.getRestClient().searchTweets(this, query);
		return true;
	}
	
}
