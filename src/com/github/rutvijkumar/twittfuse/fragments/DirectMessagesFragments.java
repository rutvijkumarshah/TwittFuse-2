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

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.rutvijkumar.twittfuse.TwitterApplication;
import com.github.rutvijkumar.twittfuse.Util;
import com.github.rutvijkumar.twittfuse.api.TwitterClient;
import com.github.rutvijkumar.twittfuse.models.Tweet;

public class DirectMessagesFragments extends TweetListFragment{

	private TwitterClient client;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		client = TwitterApplication.getRestClient();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		populateAllTweets();
	}


	public void populateTweets(long maxId) {
		// Util.showProgressBar(this);
		if (Util.isNetworkAvailable(getActivity())) {

			if (maxId > 0) {
				client.getDirectMessages(new DMessagesLoadHandler(false,this), maxId);
			} else {
				// When refresh to Pull or Getting metions first time after
				// launch of app
				clearAll();
				client.getDirectMessages(new DMessagesLoadHandler(true,this));
			}
		} else {
			Util.showNetworkUnavailable(getActivity());
		}
	}

	@Override
	protected void populateAllTweets() {
		// TODO Auto-generated method stub
		populateTweets(-1);
	}

	 public static DirectMessagesFragments newInstance(int page, String title) {
		 DirectMessagesFragments fragment = new DirectMessagesFragments();
	        Bundle args = new Bundle();
	        args.putInt("page", page);
	        args.putString("title", title);
	        fragment.setArguments(args);
	        return fragment;
	}
	 class DMessagesLoadHandler extends TweetsLoadHandler {


		 	
			@Override
		protected Object parseResponse(String arg0) throws JSONException {
			Log.d("Direct Messages", arg0);
			return super.parseResponse(arg0);
		}

			public DMessagesLoadHandler(boolean onRefresh,
					TweetListFragment tweetListFragment) {
				super(onRefresh, tweetListFragment);
			}

			@Override
			protected void onTweetLoaded(Tweet tweet) {
				tweet.persist();
			}
		}
 
}
