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
import com.github.rutvijkumar.twittfuse.TwitterApplication;
import com.github.rutvijkumar.twittfuse.Util;
import com.github.rutvijkumar.twittfuse.adapters.UserArrayAdapter;
import com.github.rutvijkumar.twittfuse.api.TwitterClient;
import com.github.rutvijkumar.twittfuse.helpers.EndlessScrollListener;
import com.github.rutvijkumar.twittfuse.models.User;

import eu.erikw.PullToRefreshListView.OnRefreshListener;

public  class UserListFragment extends Fragment {

	private ArrayList<User> users = new ArrayList<User>();
	private UserArrayAdapter adapter;
	private eu.erikw.PullToRefreshListView userListView;
	private ProgressBar progressBar;
	private String nextCursor;
	private TwitterClient client;
	private boolean isFollowersList;
	private String userScreenName;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		adapter = new UserArrayAdapter(getActivity(), users);
		client=TwitterApplication.getRestClient();
		
		isFollowersList=getArguments().getBoolean("isFollowersList");
		userScreenName=getArguments().getString("userScreenName");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_tweets_list, container,
				false);
		userListView = (eu.erikw.PullToRefreshListView) v
				.findViewById(R.id.lvTweets);
		
		progressBar=(ProgressBar)v.findViewById(R.id.progressBar);
		userListView.setAdapter(adapter);

		return v;
	}



	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		setupIintialViews();
	}

	private void setupIintialViews() {
		userListView.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				if (totalItemsCount > 0) {
					loadUsers();
				}
				
			}

		});
		userListView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				nextCursor=null;
				loadUsers();
			}
		});

	}
	
	
	public void loadUsers() {
		// Util.showProgressBar(this);
		if (Util.isNetworkAvailable(getActivity())) {

			boolean isOnRefresh=false;
			if (nextCursor ==null) {
				clearAll();
				isOnRefresh=true;
			}
			client.getUserList(isFollowersList, userScreenName, nextCursor, new UsersLoadHandler(isOnRefresh, this));	
			
		} else {
			Util.showNetworkUnavailable(getActivity());
		}
	}

	


	public void dataRefreshFinished() {
		userListView.onRefreshComplete();
	}

	

	public void addAll(List<User> users) {
		this.adapter.addAll(users);
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

	public void setNextCursor(String nextCursor) {
		// TODO Auto-generated method stub
		this.nextCursor=nextCursor;
		
	}
	
	 public static UserListFragment newInstance(int page, String title,String screenName,boolean isFollowersList) {
		 UserListFragment fragment = new UserListFragment();
	        Bundle args = new Bundle();
	        args.putInt("page", page);
	        args.putString("title", title);
	        
	        args.putBoolean("isFollowersList", isFollowersList);
	        args.putString("userScreenName", screenName);
	        
	        fragment.setArguments(args);
	        return fragment;
	}
}
