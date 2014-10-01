package com.sandeep.apps.twitterclient.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.sandeep.apps.twitterclient.R;
import com.sandeep.apps.twitterclient.TwitterClient;
import com.sandeep.apps.twitterclient.TwitterClientApp;

public class ComposeFragment  extends DialogFragment{

	private String userName;
	private String screenName;
	private String userProfileImageURL;
	
	private EditText etComposeTweet;
	public OnTweetPostedListener listener;
	public Activity activity;
	
	public interface OnTweetPostedListener {
		public void onPosted(String id, String status);
		public void onFailure();
	}
	
	public static ComposeFragment newInstance(String tweet, String id) {
		ComposeFragment frag = new ComposeFragment();
		
		Bundle args = new Bundle();
		args.putString("tweet", tweet);
		args.putString("id", id);
		frag.setArguments(args);
		return frag;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		this.activity = activity;
		if (activity instanceof OnTweetPostedListener) {
			listener = (OnTweetPostedListener) activity;
		} else {
			throw new ClassCastException(activity.toString()
					+ " must implement ComposeFragment.OnTweetPostedListener");
		}
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
		screenName = prefs.getString("screenname", "");
		userName = prefs.getString("username", "");
		userProfileImageURL = prefs.getString("profileImage", "");
	}
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NO_TITLE, DialogFragment.STYLE_NO_FRAME);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_tweet_compose, container);
		
		ImageView ivUserImage = (ImageView)view.findViewById(R.id.ivUserImage);
		ivUserImage.setImageResource(0);
		
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(userProfileImageURL, ivUserImage);

		TextView name = (TextView)view.findViewById(R.id.tvNameFragment);
		name.setText(userName);
		
		TextView username = (TextView)view.findViewById(R.id.tvUserNameFragment);
		username.setText("@"+screenName);
		
		final TextView tvTweetCharacterRemaining = (TextView)view.findViewById(R.id.tvTweetCharacterRemaining);

		final Button tweetButton = (Button) view.findViewById(R.id.btTweet);
		tweetButton.setEnabled(false);
		
		final String replyId = getArguments().getString("id");
	
		tweetButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try{
					TwitterClient client = TwitterClientApp.getRestClient();
					client.postStatusUpdate(activity,etComposeTweet.getText().toString(),replyId,listener);
				}catch(Exception e){
					e.printStackTrace();
					// notify the listener of failure
					listener.onFailure();
				}finally{
					// close the dialog
					getDialog().cancel();
				}
			}
		});
		
		etComposeTweet = (EditText)view.findViewById(R.id.etTweet);
		
		etComposeTweet.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				int textCount = etComposeTweet.getText().toString().length();
				
				if(textCount == 0){
					tweetButton.setEnabled(false);
					tvTweetCharacterRemaining.setText(String.valueOf(140));
					return;
				}
				
				int remainingChars = 140-textCount;
				tvTweetCharacterRemaining.setText(String.valueOf(remainingChars));
				
				if(remainingChars > 0){
					// enable the tweet button
					tweetButton.setEnabled(true);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		
		String tweetReply = getArguments().getString("tweet");
		if (tweetReply != null && !tweetReply.isEmpty()){
			etComposeTweet.setText(tweetReply);
			int pos = etComposeTweet.getText().length();
			etComposeTweet.setSelection(pos);
		}
		
		// Show soft keyboard automatically
		getDialog().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		
		return view;
	}
}
