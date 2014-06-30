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

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

import com.github.rutvijkumar.twittfuse.TwitterApplication;
import com.github.rutvijkumar.twittfuse.api.TwitterClient;

public class SearchTweetsFragment extends MentionsFragment{

	private String searchQuery;
	private TwitterClient client;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		searchQuery = getArguments().getString("query");
		client=TwitterApplication.getRestClient();
	}
	
	private class TweetSearchResponseHandler extends TweetsLoadHandler {

		public TweetSearchResponseHandler(boolean onRefresh,
				TweetListFragment tweetListFragment) {
			super(onRefresh, tweetListFragment);
			// TODO Auto-generated constructor stub
		}
		@Override
		public void onSuccess(JSONObject response) {
			try {
				onSuccess(response.getJSONArray("statuses"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	@Override
	protected void doTwitterAPICall() {
		
		client.searchTweets(searchQuery,new TweetSearchResponseHandler(true,this));
	}
	@Override
	protected void doTwitterAPICall(long maxId) {
		
		client.searchTweets(searchQuery, maxId, new TweetSearchResponseHandler(false,this));
	}
	
	public static SearchTweetsFragment newInstance(int page, String title,String query) {
		 SearchTweetsFragment fragment = new SearchTweetsFragment();
	        Bundle args = new Bundle();
	        args.putInt("page", page);
	        args.putString("title", title);
	        args.putString("query", query);
	        fragment.setArguments(args);
	        return fragment;
	}
}
