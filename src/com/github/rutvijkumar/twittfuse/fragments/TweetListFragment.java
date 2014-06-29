/***

The MIT License (MIT)
Copyright © 2014 Rutvijkumar Shah
 
Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the “Software”), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute,
sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
 
The above copyright notice and this permission notice shall be included in all copies or substantial
portions of the Software.
 
THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

 ***/

package com.github.rutvijkumar.twittfuse.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.github.rutvijkumar.twittfuse.R;
import com.github.rutvijkumar.twittfuse.adapters.TweetArrayAdapter;
import com.github.rutvijkumar.twittfuse.helpers.EndlessScrollListener;
import com.github.rutvijkumar.twittfuse.models.Tweet;

import eu.erikw.PullToRefreshListView.OnRefreshListener;

public abstract class TweetListFragment extends Fragment {

	private ArrayList<Tweet> tweets = new ArrayList<Tweet>();
	private TweetArrayAdapter adapter;
	private eu.erikw.PullToRefreshListView tweetsListView;
	private ProgressBar progressBar;
	
	 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		adapter = new TweetArrayAdapter(getActivity(), tweets);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_tweets_list, container,
				false);
		tweetsListView = (eu.erikw.PullToRefreshListView) v
				.findViewById(R.id.lvTweets);
		
		progressBar=(ProgressBar)v.findViewById(R.id.progressBar);
		
		tweetsListView.setAdapter(adapter);

		return v;
	}

	protected abstract void populateTweets(long sinceId);

	protected abstract void populateAllTweets();

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		setupIintialViews();
	}

	private void setupIintialViews() {
		tweetsListView.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				long sinceId=-1;
				if (totalItemsCount > 0) {
					Tweet oldestTweet = getOldestTweet(totalItemsCount);
					if (oldestTweet != null) {
						sinceId=oldestTweet.getUid() - 1;
						populateTweets(sinceId);
					}
				}
				
			}

		});
		tweetsListView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				populateAllTweets();
			}
		});

	}
	private Tweet getOldestTweet(int totalItemsCount) {
		Tweet oldestTweet = (Tweet) tweetsListView
				.getItemAtPosition(totalItemsCount - 1);
		return oldestTweet;
	}

	


	public void dataRefreshFinished() {
		tweetsListView.onRefreshComplete();
	}

	public void addNewTweet(Tweet tweet) {
		adapter.insert(tweet, 0);
		adapter.notifyDataSetChanged();
		tweetsListView.setSelection(0);
	}


	public void add(Tweet tweet) {
		adapter.add(tweet);
	}
	public void addAll(List<Tweet> tweets) {
		this.adapter.addAll(tweets);
		this.adapter.notifyDataSetChanged();
	}

	public void clearAll() {
		this.adapter.clear();
		this.adapter.notifyDataSetInvalidated();
	}

	public void showWaiting() {
		// TODO Auto-generated method stub
		progressBar.setVisibility(View.VISIBLE);
		
	}

	public void doneWaiting() {
		progressBar.setVisibility(View.INVISIBLE);
	}

}
