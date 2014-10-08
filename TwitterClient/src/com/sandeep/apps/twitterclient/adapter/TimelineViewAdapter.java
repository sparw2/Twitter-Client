package com.sandeep.apps.twitterclient.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.sandeep.apps.twitterclient.R;
import com.sandeep.apps.twitterclient.TwitterClientApp;
import com.sandeep.apps.twitterclient.activity.TweetDetailActivity;
import com.sandeep.apps.twitterclient.fragments.ComposeFragment;
import com.sandeep.apps.twitterclient.helpers.DateUtil;
import com.sandeep.apps.twitterclient.models.Tweet;

public class TimelineViewAdapter extends ArrayAdapter<Tweet> {

	private FragmentActivity activity;
	
	public TimelineViewAdapter(Context context, int resource,
			List<Tweet> timelineList) {
		super(context, resource, timelineList);
		this.activity=(FragmentActivity)context;
	}

	class ViewHolder{
		ImageView ivProfileImage;
		TextView name;
		TextView username;
		TextView tweetBody;
		ImageView ivMediaImage;
		TextView tvCreationTime;
		TextView retweetCount;
		TextView reFavCount;
		ImageView ivReply;
		ImageView ivFavorite;
		ProgressBar pb;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		final Tweet tweet = this.getItem(position);
		final ViewHolder holder;
		if (convertView == null){
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
			
			holder = new ViewHolder();
			convertView.setTag(holder);
			holder.ivProfileImage = (ImageView)convertView.findViewById(R.id.ivProfileImage);
			holder.name = (TextView)convertView.findViewById(R.id.tvName);
			holder.username =  (TextView)convertView.findViewById(R.id.tvUserName);
			holder.tweetBody = (TextView)convertView.findViewById(R.id.tvTweetBody);
			holder.ivMediaImage = (ImageView)convertView.findViewById(R.id.ivMedia);
			holder.tvCreationTime =(TextView)convertView.findViewById(R.id.tvRelativeTimestamp);
			holder.retweetCount =  (TextView)convertView.findViewById(R.id.tvRetweetCount);
			holder.reFavCount = (TextView)convertView.findViewById(R.id.tvFavoriteCount);
			holder.ivReply = (ImageView)convertView.findViewById(R.id.ivReply);
			holder.ivFavorite = (ImageView)convertView.findViewById(R.id.ivFavorite);
			holder.pb = (ProgressBar) convertView.findViewById(R.id.pbLoading);
			
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		
		holder.ivProfileImage.setImageResource(android.R.color.transparent);
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(tweet.user.profileImageURL, holder.ivProfileImage);

		
		 
		holder.name.setText(tweet.user.name);
		holder.username.setText("@"+tweet.user.username);
		
		holder.tweetBody.setText(tweet.tweetBody);
		holder.tweetBody.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(activity,TweetDetailActivity.class);
				i.putExtra("tweet", tweet);
				activity.startActivity(i);
			}
		});
		
		
		holder.ivMediaImage.setImageResource(0);
		
		if (tweet.mediaUrl != null){
			// TODO - compute based on display 
			holder.ivMediaImage.getLayoutParams().height=300;
			imageLoader.displayImage(tweet.mediaUrl,holder.ivMediaImage, new ImageLoadingListener() {
				
				@Override
				public void onLoadingStarted(String arg0, View arg1) {
					holder.pb.setVisibility(ProgressBar.VISIBLE);
				}
				
				@Override
				public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				}
				
				@Override
				public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
					holder.pb.setVisibility(ProgressBar.INVISIBLE);
				}
				
				@Override
				public void onLoadingCancelled(String arg0, View arg1) {
		
				}
			});
		}else{
			// reset the height of the imageview
			holder.ivMediaImage.getLayoutParams().height=0;
			holder.pb.setVisibility(ProgressBar.GONE);
		}
		
		if(tweet.created_at != null){ 
			String relativeTime = DateUtil.getRelativeTimeAgo(tweet.created_at);
			holder.tvCreationTime.setText(relativeTime);
		}else{
			// just created
			holder.tvCreationTime.setText("0s");
		}
		
		
		if (tweet.retweetCount > 0){
			holder.retweetCount.setText(String.valueOf(tweet.retweetCount));
		}else{
			holder.retweetCount.setText("");
		}
			
		
		if (tweet.favoriteCount > 0)
			holder.reFavCount.setText(String.valueOf(tweet.favoriteCount));
		else
			holder.reFavCount.setText("");
		
		holder.ivProfileImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TwitterClientApp.getRestClient().getUserInfo(activity, false, false, tweet.user.username);
			}
		});
		
		
		holder.ivReply.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				 FragmentManager fm = activity.getSupportFragmentManager();
				 
				 ComposeFragment dialog = ComposeFragment.newInstance(holder.username.getText().toString()+" ", tweet.id);
				 dialog.show(fm, "");
			}
		});
		
		holder.ivFavorite.setSelected(tweet.favorited);
		holder.ivFavorite.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (holder.ivFavorite.isSelected()){
					TwitterClientApp.getRestClient().setFavorite(activity, tweet.id,false);
					holder.ivFavorite.setSelected(false);
					tweet.favorited=false;
				}else{	
					TwitterClientApp.getRestClient().setFavorite(activity, tweet.id,true);
					holder.ivFavorite.setSelected(true);
					tweet.favorited=true;
				}	
				
			}
		});
		
		return convertView;
	}

	
}
