package com.sandeep.apps.twitterclient.activity;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.sandeep.apps.twitterclient.R;
import com.sandeep.apps.twitterclient.fragments.SearchFragment;
import com.sandeep.apps.twitterclient.models.Tweet;

public class SearchActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_search);
		
		List<Tweet> tweets = getIntent().getParcelableArrayListExtra("tweets");
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		
		ft.add(R.id.flSearch,new SearchFragment(tweets));
		ft.commit();
	}
	
}
