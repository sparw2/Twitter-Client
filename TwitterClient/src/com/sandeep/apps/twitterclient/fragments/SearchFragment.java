package com.sandeep.apps.twitterclient.fragments;

import java.util.List;

import android.app.Activity;

import com.sandeep.apps.twitterclient.adapter.TimelineViewAdapter;
import com.sandeep.apps.twitterclient.models.Tweet;

public class SearchFragment extends TimelineFragment {

	public SearchFragment(List<Tweet> list){
		super(list);
	}
	
	@Override
	protected void populateTimeline(Activity activity,
			TimelineViewAdapter adapter, List<Tweet> tweets, boolean isRefresh,
			boolean isPaginate) {
		if (adapter.isEmpty())
			adapter.addAll(tweets);
	}

}
