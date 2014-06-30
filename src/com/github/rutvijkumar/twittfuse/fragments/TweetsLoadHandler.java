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

import org.json.JSONArray;

import android.util.Log;

import com.github.rutvijkumar.twittfuse.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

/***
 * Response Handler for TimeLine Activity
 * 
 * @author Rutvijkumar Shah
 * 
 */
public class TweetsLoadHandler extends JsonHttpResponseHandler{
	private boolean onRefresh = false;
	private TweetListFragment tweetListFragment;
	
	public TweetsLoadHandler(boolean onRefresh,TweetListFragment tweetListFragment) {
		this.onRefresh = onRefresh;
		this.tweetListFragment=tweetListFragment;
		this.tweetListFragment.showWaiting();
		
	}

	@Override
	public void onFailure(Throwable e, String s) {
		Log.e("TwittFuse", "Error :"+s);
		e.printStackTrace();
	}


	@Override
	public void onFinish() {
		// TODO Auto-generated method stub
		if (onRefresh) {
			tweetListFragment.dataRefreshFinished();
		}
		this.tweetListFragment.doneWaiting();
		super.onFinish();
	}

	protected  void onTweetLoaded(Tweet tweet) {
		
	}
	@Override
	public void onSuccess(JSONArray jsArray) {
		ArrayList<Tweet> tweets = Tweet.fromJSONArray(jsArray);
		for (Tweet tweet : tweets) {
			onTweetLoaded(tweet);
			tweetListFragment.add(tweet);
		}

	}
}
