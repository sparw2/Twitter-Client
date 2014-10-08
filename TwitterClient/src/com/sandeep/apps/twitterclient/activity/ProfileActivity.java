package com.sandeep.apps.twitterclient.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.Toast;

import com.sandeep.apps.twitterclient.R;
import com.sandeep.apps.twitterclient.fragments.ComposeFragment;
import com.sandeep.apps.twitterclient.fragments.ProfileFragment;
import com.sandeep.apps.twitterclient.fragments.UserTimelineFragment;
import com.sandeep.apps.twitterclient.models.Tweet;
import com.sandeep.apps.twitterclient.models.User;

public class ProfileActivity extends FragmentActivity implements ComposeFragment.OnTweetPostedListener{

	private UserTimelineFragment userTimeline;
	private User user;
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	        NavUtils.navigateUpFromSameTask(this);
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		// enable the home navigation with up
		getActionBar().setDisplayHomeAsUpEnabled(true);

		user = (User)getIntent().getParcelableExtra("user");

		userTimeline = (UserTimelineFragment)getSupportFragmentManager().findFragmentById(R.id.fgUserTimeline);
		userTimeline.screenName = user.username;
		
		ProfileFragment userProfile = (ProfileFragment)getSupportFragmentManager().findFragmentById(R.id.fgUserProfile);
		userProfile.user = user;

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
		
		if (tweet.user.username.equals(userTimeline.screenName)){
			// update it if we are in profile view of same user
			userTimeline.insertTweet(tweet, 0);
		}	
   }

	@Override
	public void onFailure() {
		Toast.makeText(this,"Failure in posting the tweet", Toast.LENGTH_SHORT).show();
	}
	
	
}
