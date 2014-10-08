package com.sandeep.apps.twitterclient.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sandeep.apps.twitterclient.R;
import com.sandeep.apps.twitterclient.TwitterClient;
import com.sandeep.apps.twitterclient.TwitterClientApp;
import com.sandeep.apps.twitterclient.adapter.TimelineViewAdapter;
import com.sandeep.apps.twitterclient.listeners.EndlessScrollListener;
import com.sandeep.apps.twitterclient.models.Tweet;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public abstract class TimelineFragment extends Fragment {
	
	protected TwitterClient client;
	
	private TimelineViewAdapter adapter;
	private List<Tweet> timeline;
	private PullToRefreshListView lvTimeline;
	
	private boolean isRefresh=false;
	private boolean isPaginate=false;
	
	public TimelineFragment(){
		
	}
	
	public TimelineFragment(List<Tweet> timeline){
		this.timeline = timeline;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (timeline == null)
			timeline = new ArrayList<>();
		
		client = TwitterClientApp.getRestClient();
		
		// fetch and cache the user info
		client.getUserInfo(this.getActivity(), false, true, null);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	
		View v = inflater.inflate(R.layout.fragment_timeline, container, false);
		
		lvTimeline = (PullToRefreshListView)v.findViewById(R.id.lvTimeline);
		adapter = new TimelineViewAdapter(this.getActivity(), R.layout.fragment_timeline, timeline);
		lvTimeline.setAdapter(adapter);

		// Set a listener to be invoked when the list should be refreshed.
		lvTimeline.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
             	isRefresh = true;
            	isPaginate = false;		
            	populateTimeline(TimelineFragment.this.getActivity(),adapter,timeline,isRefresh,isPaginate);
        		lvTimeline.onRefreshComplete();
            }
        });
		
		// Set a listener for scrolling 
		lvTimeline.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				isRefresh = false;
            	isPaginate = true;
            	populateTimeline(TimelineFragment.this.getActivity(),adapter,timeline,isRefresh,isPaginate);
        		lvTimeline.onRefreshComplete();
			}
		});
	
		return v;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// populate the timeline
		isRefresh=false;
		isPaginate=false;
		populateTimeline(TimelineFragment.this.getActivity(),adapter,timeline,isRefresh,isPaginate);
	}
	
	protected abstract void populateTimeline(Activity activity, TimelineViewAdapter adapter, List<Tweet> tweets, boolean isRefresh,
			boolean isPaginate);
	
	public void insertTweet(Tweet tweet, int position){
		adapter.insert(tweet, 0);
	}
}
