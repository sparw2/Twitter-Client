package com.sandeep.apps.twitterclient.listeners;

import android.util.Log;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

public abstract class EndlessScrollListener implements OnScrollListener {

	private int visibleThreshold = 2;

	// The current offset index of data you have loaded
	private int currentPage = 0;
	
	// The total number of items in the dataset after the last load
	private int previousTotalItemCount = 0;
	
	// True if we are still waiting for the last set of data to load.
	private boolean loading = true;
	
	// Sets the starting page index
	private int startingPageIndex = 0;

	public EndlessScrollListener() {
	}

	public EndlessScrollListener(int visibleThreshold) {
		this.visibleThreshold = visibleThreshold;
	}

	public EndlessScrollListener(int visibleThreshold, int startPage) {
		this.visibleThreshold = visibleThreshold;
		this.startingPageIndex = startPage;
		this.currentPage = startPage;
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
		if (totalItemCount < previousTotalItemCount) {
			// If the total item count is zero and the previous isn't, assume the
			// list is invalidated and should be reset back to initial state
			this.currentPage = this.startingPageIndex;
			this.previousTotalItemCount = totalItemCount;
			if (totalItemCount == 0) { 
				this.loading = true; 
			} 
		}
		
		if (loading && (totalItemCount > previousTotalItemCount)) {
			// If it’s still loading, we check to see if the dataset count has
			// changed, if so we conclude it has finished loading and update the current page
			// number and total item count.
			loading = false;
			previousTotalItemCount = totalItemCount;
			currentPage++;
		}
		
		if (!loading && (totalItemCount - visibleItemCount)<=(firstVisibleItem + visibleThreshold)) {
			// If it isn’t currently loading, we check to see if we have breached
			// the visibleThreshold and need to reload more data.
			// If we do need to reload some more data, we execute onLoadMore to fetch the data.
			Log.d("SearchActivity currentPage+1:", String.valueOf(currentPage+1));
		    onLoadMore(currentPage+1, totalItemCount);
		   
		    loading = true;
		}
	}
	
	// Defines the process for actually loading more data based on page
	public abstract void onLoadMore(int page, int totalItemsCount);

}
