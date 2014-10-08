package com.sandeep.apps.twitterclient.adapter;

import com.sandeep.apps.twitterclient.fragments.HomeTimelineFragment;
import com.sandeep.apps.twitterclient.fragments.MentionTimelineFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TimelinePagerAdapter extends FragmentPagerAdapter {
	private static int NUM_ITEMS = 2;
	public HomeTimelineFragment homeFragment;
	public MentionTimelineFragment mentionFragment;
	
    public TimelinePagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }
    
    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
        case 0:
        	if (homeFragment == null)
        		homeFragment = new HomeTimelineFragment();
         return homeFragment; 
        case 1: 
        	if (mentionFragment == null)
        		mentionFragment =  new MentionTimelineFragment();
        	return mentionFragment;
        default:
        	return null;
        }
    }
    
    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
    	switch(position){
    		case 0:
    			return "HOME";
    		case 1:	
    	}		return "MENTIONS";
    }
    
}

