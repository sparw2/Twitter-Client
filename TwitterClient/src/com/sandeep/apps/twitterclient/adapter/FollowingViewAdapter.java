package com.sandeep.apps.twitterclient.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sandeep.apps.twitterclient.R;
import com.sandeep.apps.twitterclient.TwitterClientApp;
import com.sandeep.apps.twitterclient.models.User;

public class FollowingViewAdapter extends ArrayAdapter<User> {

	private Activity activity;
	
	public FollowingViewAdapter(Context context, int resource,
			List<User> userList) {
		super(context, resource, userList);
		this.activity=(Activity)context;
	}

	class ViewHolder{
		ImageView ivProfileImage;
		TextView name;
		TextView username;
		TextView tvDescription;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		final User user = this.getItem(position);
		final ViewHolder holder;
		if (convertView == null){
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_following, parent, false);
			
			holder = new ViewHolder();
			convertView.setTag(holder);
			holder.ivProfileImage = (ImageView)convertView.findViewById(R.id.ivProfileImage);
			holder.name = (TextView)convertView.findViewById(R.id.tvName);
			holder.username =  (TextView)convertView.findViewById(R.id.tvUserName);
			holder.tvDescription = (TextView)convertView.findViewById(R.id.tvDescription);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		
		holder.ivProfileImage.setImageResource(android.R.color.transparent);
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(user.profileImageURL, holder.ivProfileImage);

		holder.name.setText(user.name);
		holder.username.setText("@"+user.username);
		
		holder.tvDescription.setText(user.description);
		
		holder.ivProfileImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TwitterClientApp.getRestClient().getUserInfo(activity, false, false, user.username);
			}
		});
		
		return convertView;
	}

	
}
