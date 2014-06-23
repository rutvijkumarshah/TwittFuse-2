package com.github.rutvijkumar.twittfuse.activities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.rutvijkumar.twittfuse.R;
import com.github.rutvijkumar.twittfuse.Util;
import com.github.rutvijkumar.twittfuse.models.Tweet;
import com.github.rutvijkumar.twittfuse.models.User;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetDetailsActivity extends RoboActivity {

	@InjectView(R.id.img_profilepic)
	ImageView profileImage;
	@InjectView(R.id.tv_username)
	TextView name;
	@InjectView(R.id.tv_screenname)
	TextView screenName;
	@InjectView(R.id.tv_tweetbody)
	TextView tweetbody;
	@InjectView(R.id.tv_timestamp)
	TextView timestamp;
	@InjectView(R.id.tv_RTcount)
	TextView rtCount;
	@InjectView(R.id.tv_FVcount)
	TextView favCount;
	@InjectView(R.id.im_reply)
	ImageView replyAction;
	@InjectView(R.id.im_retweet)
	ImageView rtAction;
	@InjectView(R.id.im_fav)
	ImageView favAction;
	@InjectView(R.id.im_share)
	ImageView shareAction;
	@InjectView(R.id.et_reply)
	EditText replyEditText;
	@InjectView(R.id.tv_charlimit)
	TextView charLimit;
	@InjectView(R.id.tv_tweetAction)
	TextView tweetIt;

	private Tweet tweet;
	private static final int CHARS_LIMIT=140;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tweet_details);
		tweet = (Tweet) getIntent().getSerializableExtra("tweet");
		populateValues();
		setupListnersForUI();
	}

	private void populateValues() {
		ImageLoader imageLoader = ImageLoader.getInstance();
		User user = tweet.getUser();
		imageLoader.displayImage(user.getProfileImageUrl(), profileImage);

		name.setText(user.getName());
		screenName.setText("@" + user.getScreenName());

		tweetbody.setText(tweet.getBody());
		DateFormat df = new SimpleDateFormat("h:mm a . dd MMM yy");

		timestamp.setText(df.format(tweet.getCreatedAt()));
		rtCount.setText(String.valueOf(tweet.getReTweetCount()));
		favCount.setText(String.valueOf(tweet.getFavouritesCount()));

		replyEditText.setHint("Reply to " + user.getName());// Localize Reply to

	}

	private void setupListnersForUI() {
		
		replyEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String typedTweetText=replyEditText.getEditableText().toString().trim();
				String remainingCharacters= String.valueOf(( CHARS_LIMIT - typedTweetText.length()));
				charLimit.setText(remainingCharacters);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		replyEditText.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) { 
					if(replyEditText.getText().toString().isEmpty()) {
						String initialText="@"+tweet.getUser().getScreenName();
						replyEditText.setText(initialText);
						replyEditText.setSelection(initialText.length());
					
					}
					Util.showSoftKeyboard(v, TweetDetailsActivity.this);
					//Twitter way to Reply. Putting screenname/handle first
				}else{
					Util.hideSoftKeyboard(v, TweetDetailsActivity.this);
				}

			}
		});
	}
}
