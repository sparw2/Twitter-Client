package com.sandeep.apps.twitterclient.fragments;

import java.util.List;

import android.app.Activity;

import com.sandeep.apps.twitterclient.TwitterClientApp;
import com.sandeep.apps.twitterclient.adapter.TimelineViewAdapter;
import com.sandeep.apps.twitterclient.models.Tweet;

public class UserTimelineFragment extends TimelineFragment {

	public String screenName;
	
	@Override
	protected void populateTimeline(Activity activity,
			TimelineViewAdapter adapter, List<Tweet> tweets, boolean isRefresh,
			boolean isPaginate) {

		TwitterClientApp.getRestClient().getUserTimeline(activity, adapter,tweets, 
				isRefresh,isPaginate,screenName);

	}

}
