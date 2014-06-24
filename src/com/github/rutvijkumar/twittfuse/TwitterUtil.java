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

package com.github.rutvijkumar.twittfuse;

import java.util.HashSet;
import java.util.List;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.rutvijkumar.twittfuse.api.TwitterClient;
import com.github.rutvijkumar.twittfuse.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

@SuppressLint("DefaultLocale")
public class TwitterUtil {

	private FragmentActivity activity;
	private TwitterClient client;

	public TwitterUtil(FragmentActivity activity, TwitterClient client) {
		this.activity = activity;
		this.client = client;
	}

	public void markFavorite(final Tweet tweet, final ImageButton favAction,final TextView countView) {
		if(!Util.isNetworkAvailable(activity)) {
			Util.showNetworkUnavailable(activity);
		}
		client.markTweetFavorite(tweet.isFavorited(),
				String.valueOf(tweet.getUid()), new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject body) {
						boolean isFav = tweet.isFavorited();
						long newFavCount=tweet.getFavouritesCount();
						tweet.save();
						if(isFav) {
							newFavCount++;
						}else {
							newFavCount--;
						}
						tweet.setFavouritesCount(newFavCount);
						tweet.save();
						
						countView.setText(String.valueOf(tweet.getFavouritesCount()));
						setFavView(tweet.isFavorited(), favAction);
					}

					public void onFailure(Throwable e, JSONObject error) {
						Log.d("DEBUG", error.toString());
						Log.e("ERROR", "Exception while marking fav", e);
					}
				});
	}

	public void setFavView(boolean isFav, ImageButton favAction) {
		if (isFav) {
			favAction.setImageResource(R.drawable.ic_action_fav_selected);
		} else {
			favAction.setImageResource(R.drawable.ic_action_fav_dark);
		}
	}

	public void setRTView(boolean isRetweeted, ImageButton rtAction) {
		if (isRetweeted) {
			rtAction.setImageResource(R.drawable.ic_rt_action_selected);
		} else {
			rtAction.setImageResource(R.drawable.ic_rt_action_dark);
		}
	}

	private void doReTweet(final Tweet tweet, final ImageButton rtAction,final TextView rtCount) {
		
		client.postRT(String.valueOf(tweet.getUid()),
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject body) {
						final long newRTCount=tweet.getReTweetCount()+1;
						
						tweet.setRetweeted(true);
						tweet.setReTweetCount(tweet.getReTweetCount()+1);
						tweet.save();
						setRTView(true, rtAction);
						rtCount.setText(String.valueOf(newRTCount));
					}

					public void onFailure(Throwable e, JSONObject error) {
						Log.d("DEBUG", error.toString());
						Log.e("ERROR", "Exception while doing RT", e);
					}
				});

	}

	public void confirmRetweet(final Tweet tweet, final ImageButton rtAction, final TextView rtCount) {
		
		if (!tweet.isRetweeted()) {

			AlertDialog.Builder builder = new AlertDialog.Builder(activity);
			builder.setTitle("Retweet this to your followers?");
			builder.setPositiveButton("Retweet",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int whichButton) {
							if(!Util.isNetworkAvailable(activity)) {
								Util.showNetworkUnavailable(activity);
							}
							doReTweet(tweet, rtAction,rtCount);

						}

					});
			builder.setNegativeButton("Cancel", null);
			builder.create().show();
		}
	}
	
	@SuppressLint("DefaultLocale")
	public void postReply(final Tweet tweet) {
		HashSet<String> handles=new HashSet<String>();
		
		List<String> list = Util.testExtract(tweet.getBody());
		for (String l:list) {
			handles.add(l.toLowerCase());
		}
		handles.add(tweet.getUser().getScreenName().toLowerCase());
		Tweet retweetedTweeet=tweet.getReTweeted();
		if(retweetedTweeet!=null) {
			handles.add(retweetedTweeet.getUser().getScreenName().toLowerCase());
		}
		
		Util.onReply(activity, handles, String.valueOf(tweet.getUid()));
		
	}
}
