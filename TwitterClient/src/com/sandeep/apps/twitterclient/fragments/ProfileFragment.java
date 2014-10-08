package com.sandeep.apps.twitterclient.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.sandeep.apps.twitterclient.R;
import com.sandeep.apps.twitterclient.activity.FollowingActivity;
import com.sandeep.apps.twitterclient.models.User;

public class ProfileFragment extends Fragment {
	
	public User user;
	private ImageView ivProfileImage; 
	private RelativeLayout rlProfile;
	private TextView tvName;
	private TextView tvUserName;
	private TextView tvTweetsCount;
	private TextView tvFollowingCount;
	private TextView tvFollowersCount;
	
	private LinearLayout llFollowing;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.fragment_profile, container, false);
		
		ivProfileImage = (ImageView)v.findViewById(R.id.ivProfile);
		
		rlProfile = (RelativeLayout)v.findViewById(R.id.rlProfile);
		
		tvName = (TextView)v.findViewById(R.id.tvName);
		tvUserName = (TextView)v.findViewById(R.id.tvUserName);
		
		tvTweetsCount = (TextView)v.findViewById(R.id.tvTweetsCount);
		tvFollowingCount= (TextView)v.findViewById(R.id.tvFollowingCount);
		tvFollowersCount = (TextView)v.findViewById(R.id.tvFollowersCount);

		llFollowing = (LinearLayout)v.findViewById(R.id.llFollowing);
		
		return v;
	}
	
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		tvName.setText(user.name);
		
		tvUserName.setText("@"+user.username);
		
		tvTweetsCount.setText(String.valueOf(user.tweetsCount));
		tvFollowingCount.setText(String.valueOf(user.followingCount));
		llFollowing.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(ProfileFragment.this.getActivity(), FollowingActivity.class);
				i.putExtra("screenName", user.username);
				startActivity(i);
			}
		});
		
		tvFollowersCount.setText(String.valueOf(user.followersCount));

		ImageLoader.getInstance().displayImage(user.profileImageURL, ivProfileImage);
		
		if (user.bannerImageURL != null){
			ImageLoader.getInstance().loadImage(user.bannerImageURL, new ImageLoadingListener() {
				
				@Override
				public void onLoadingStarted(String arg0, View arg1) {}
				
				@Override
				public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {}
				
				@Override
				public void onLoadingComplete(String uri, View view, Bitmap bitMap) {
					rlProfile.setBackground(new BitmapDrawable(rlProfile.getResources(),bitMap));
				}
				
				@Override
				public void onLoadingCancelled(String arg0, View arg1) {}
			});
		}
	}
	
}
