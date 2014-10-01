package com.sandeep.apps.twitterclient.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sandeep.apps.twitterclient.R;
import com.sandeep.apps.twitterclient.models.Tweet;

public class TweetDetailActivity extends Activity {

	private Tweet tweet;
	ImageView ivProfileImage;
	TextView name;
	TextView username;
	TextView tweetBody;
	ImageView ivMediaImage;
	TextView tvRetweetFavCount;
	ImageView ivFavorite;
	
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
		setContentView(R.layout.activity_tweet_detail);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		tweet = (Tweet)getIntent().getSerializableExtra("tweet");
		
		ivProfileImage = (ImageView)findViewById(R.id.ivDetailViewProfileImage);
		name = (TextView)findViewById(R.id.tvDetailViewName);
		username =  (TextView)findViewById(R.id.tvDetailViewUserName);
		tweetBody = (TextView)findViewById(R.id.tvDetailViewTweetBody);
		ivMediaImage = (ImageView)findViewById(R.id.ivDetailViewMedia);
		tvRetweetFavCount = (TextView)findViewById(R.id.tvRetweetFavCount);
	
		ivProfileImage.setImageResource(android.R.color.transparent);
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(tweet.user.profileImageURL, ivProfileImage);

		name.setText(tweet.user.name);
		username.setText("@"+tweet.user.username);
		tweetBody.setText(tweet.tweetBody);
		ivMediaImage.setImageResource(0);
		
		String retweetCountText = "<font color=\"#7e7e7e\"><b>"+tweet.retweetCount+"</b>  RETWEETS  <b>"+tweet.favoriteCount +"</b>  FAVORITES</font>";
		tvRetweetFavCount.setText(Html.fromHtml(retweetCountText));
		
		if (tweet.mediaUrl != null){
			// TODO - compute based on display 
			ivMediaImage.getLayoutParams().height=300;
			imageLoader.displayImage(tweet.mediaUrl,ivMediaImage);
		}else{
			// reset the height of the imageview
			ivMediaImage.getLayoutParams().height=0;
		}

	}
}

