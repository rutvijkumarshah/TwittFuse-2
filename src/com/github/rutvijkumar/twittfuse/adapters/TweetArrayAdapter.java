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

package com.github.rutvijkumar.twittfuse.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rutvijkumar.twittfuse.R;
import com.github.rutvijkumar.twittfuse.TwitterUtil;
import com.github.rutvijkumar.twittfuse.Util;
import com.github.rutvijkumar.twittfuse.activities.TweetDetailsActivity;
import com.github.rutvijkumar.twittfuse.api.TwitterClient;
import com.github.rutvijkumar.twittfuse.models.Tweet;
import com.github.rutvijkumar.twittfuse.models.User;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetArrayAdapter extends ArrayAdapter<Tweet> {

	private  static final int RETWEET_IMG_WIDTH=16;
	private  static final int RETWEET_IMG_HEIGHT=16;
	
	private Context context;
	
	private TwitterUtil twUtil;
	private TwitterClient client;
	public float getDP(int dp) {
		float dimension = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
		return dimension;
	}
	
	// View lookup cache
	private static class ViewHolder {
		TextView reTweetedByUserName;// @+id/retweetedTweetUserName
		TextView name;// @+id/tweetUserName
		TextView screenName; // @+id/tweetUserScreenName
		ImageView userProfilePic;// @+id/profileImg
		TextView timeStamp; // @+id/tweetTimeStampTxt
		TextView tweetBody; // @+id/tweetBodyTxt
		ImageView  reTweetImage;//@+id/retweetedTweet
		TextView retweetCountTv;
		TextView favCountTv;
		ImageButton favImage;
		ImageButton rtImage;
		ImageButton replyImage;
		
	}
//retweetedTweet
	public TweetArrayAdapter(Context context, ArrayList<Tweet> tweets) {
		super(context, R.layout.tweet_litem, tweets);
		this.context=context;
		this.client=new TwitterClient(context);
		this.twUtil=new TwitterUtil((FragmentActivity)context, client);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Get the data item for this position
		final Tweet tweet = getItem(position);
		// Check if an existing view is being reused, otherwise inflate the view
		ViewHolder viewHolder; // view lookup cache stored in tag
		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.tweet_litem, parent, false);

			viewHolder.reTweetedByUserName = (TextView) convertView
					.findViewById(R.id.retweetedTweetUserName);
			viewHolder.name = (TextView) convertView
					.findViewById(R.id.tweetUserName);
			viewHolder.screenName = (TextView) convertView
					.findViewById(R.id.tweetUserScreenName);
			viewHolder.timeStamp = (TextView) convertView
					.findViewById(R.id.tweetTimeStampTxt);
			viewHolder.tweetBody = (TextView) convertView
					.findViewById(R.id.tweetBodyTxt);
			viewHolder.userProfilePic = (ImageView) convertView
					.findViewById(R.id.profileImg);

			viewHolder.reTweetImage =(ImageView)convertView.findViewById(R.id.retweetedTweet);
			
			viewHolder.retweetCountTv=(TextView)convertView.findViewById(R.id.retweetCountTv);
			viewHolder.favCountTv=(TextView)convertView.findViewById(R.id.favCountTv);
			
			viewHolder.favImage=(ImageButton)convertView.findViewById(R.id.favIv);
			viewHolder.rtImage=(ImageButton)convertView.findViewById(R.id.retweetIv);
			viewHolder.replyImage=(ImageButton)convertView.findViewById(R.id.replyIv);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// Populate the data into the template view using the data object
		
		User user=null;
		Tweet displayTweet=null;
		long favouritesCount = tweet.getFavouritesCount();
		long reTweetCount=tweet.getReTweetCount();
		
		if(tweet.getReTweeted()!=null) {
			//Tweet is reTweeted
			viewHolder.reTweetedByUserName.setText(tweet.getUser().getName()+" retweeted");
			displayTweet=tweet.getReTweeted();
			//retweetedTweet
			viewRetweetInfo(View.VISIBLE, viewHolder.reTweetImage,getDP(16) , getDP(16));
			viewRetweetInfo(View.VISIBLE, viewHolder.reTweetedByUserName, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			
		}else {
			viewRetweetInfo(View.INVISIBLE, viewHolder.reTweetImage, 0, 0);
			viewRetweetInfo(View.INVISIBLE, viewHolder.reTweetedByUserName, 0, 0);
			displayTweet=tweet;
			
		}
		user=displayTweet.getUser();
		
		
		viewHolder.userProfilePic
				.setImageResource(android.R.color.transparent);
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(user.getProfileImageUrl(),
				viewHolder.userProfilePic);

		
		viewHolder.name.setText(user.getName());
		viewHolder.screenName.setText("@"+user.getScreenName());
		viewHolder.tweetBody.setText(displayTweet.getBody());
		viewHolder.timeStamp.setText(Util.getDuration(tweet.getCreatedAt()));
		viewHolder.retweetCountTv.setText(String.valueOf(reTweetCount));
	
		viewHolder.favImage.setTag(tweet);
		viewHolder.rtImage.setTag(tweet);
		viewHolder.replyImage.setTag(tweet);
		
		
		if(reTweetCount == 0) {
			viewHolder.retweetCountTv.setVisibility(View.INVISIBLE);
		}else {
			viewHolder.retweetCountTv.setVisibility(View.VISIBLE);
		}
		
		viewHolder.favCountTv.setText(String.valueOf(favouritesCount));
		if(favouritesCount == 0) {
			viewHolder.favCountTv.setVisibility(View.INVISIBLE);
		}else {
			viewHolder.favCountTv.setVisibility(View.VISIBLE);
		}
	
		
		//pullToRefreshListView setOnItem click listerner is not working because of auto link
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent detailsIntent = new Intent(getContext(), TweetDetailsActivity.class);
				detailsIntent.putExtra("tweet", tweet);
				getContext().startActivity(detailsIntent);
				
			}
		});
		
		twUtil.setFavView(tweet.isFavorited(), viewHolder.favImage);
		twUtil.setRTView(tweet.isRetweeted(), viewHolder.rtImage);
	
		
		final ImageButton favImage=viewHolder.favImage;
		viewHolder.favImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Tweet tw=(Tweet)v.getTag();
				//FLIP FAV
				tweet.setFavorited(!tw.isFavorited());
				twUtil.markFavorite(tw, favImage);
			}
		});
		
		final ImageButton reTweetImage=viewHolder.rtImage;
		viewHolder.rtImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Tweet tw=(Tweet)v.getTag();
				twUtil.confirmRetweet(tw, reTweetImage);
				
			}
		});
		
		
		viewHolder.replyImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Tweet tw=(Tweet)v.getTag();
				twUtil.postReply(tw);
				
			}
		});
		
		return convertView;
	}
	
	
	
	private void viewRetweetInfo( int visibility,View view,float width,float height) {
		LayoutParams layoutParams = view.getLayoutParams();
		layoutParams.height=(int)height;
		layoutParams.width=(int)width;
		view.setVisibility(visibility);
		view.setLayoutParams(layoutParams);
		
	}
}
