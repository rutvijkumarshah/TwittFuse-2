package com.github.rutvijkumar.twittfuse.activities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.util.Log;
import com.github.rutvijkumar.twittfuse.R;
import com.github.rutvijkumar.twittfuse.Util;
import com.github.rutvijkumar.twittfuse.api.TwitterClient;
import com.github.rutvijkumar.twittfuse.models.Tweet;
import com.github.rutvijkumar.twittfuse.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetDetailsActivity extends Activity {

	ImageView profileImage;
	TextView name;

	TextView screenName;

	TextView tweetbody;
	TextView timestamp;
	TextView rtCount;
	TextView favCount;

	ImageButton replyAction;
	ImageButton rtAction;
	ImageView favAction;
	ImageButton shareAction;

	EditText replyEditText;
	TextView charLimit;
	TextView tweetIt;

	private Tweet tweet;
	private TwitterClient client;

	private static final int CHARS_LIMIT = 140;

	private void setupUI() {
		profileImage = (ImageView) findViewById(R.id.img_profilepic);
		name = (TextView) findViewById(R.id.tv_username);
		screenName = (TextView) findViewById(R.id.tv_screenname);
		tweetbody = (TextView) findViewById(R.id.tv_tweetbody);
		timestamp = (TextView) findViewById(R.id.tv_timestamp);
		rtCount = (TextView) findViewById(R.id.tv_RTcount);
		favCount = (TextView) findViewById(R.id.tv_FVcount);

		replyAction = (ImageButton) findViewById(R.id.im_reply);
		rtAction = (ImageButton) findViewById(R.id.im_retweet);
		favAction = (ImageButton) findViewById(R.id.im_fav);
		shareAction = (ImageButton) findViewById(R.id.im_share);

		replyEditText = (EditText) findViewById(R.id.et_reply);

		charLimit = (TextView) findViewById(R.id.tv_charlimit);
		tweetIt = (TextView) findViewById(R.id.tv_tweetAction);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tweet_details);
		tweet = (Tweet) getIntent().getSerializableExtra("tweet");
		client = new TwitterClient(this);
		setupUI();
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

	public void doAction(View view) {
		final String action_code=view.getTag().toString();
		if("FAV".equals(action_code)) {
			markFavorite();
		}
		
		
	}
	private void setupListnersForUI() {

	
		replyEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String typedTweetText = replyEditText.getEditableText()
						.toString().trim();
				String remainingCharacters = String
						.valueOf((CHARS_LIMIT - typedTweetText.length()));
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
					if (replyEditText.getText().toString().isEmpty()) {
						String initialText = "@"
								+ tweet.getUser().getScreenName();
						replyEditText.setText(initialText);
						replyEditText.setSelection(initialText.length());

					}
					Util.showSoftKeyboard(v, TweetDetailsActivity.this);
					// Twitter way to Reply. Putting screenname/handle first
				} else {
					Util.hideSoftKeyboard(v, TweetDetailsActivity.this);
				}

			}
		});
	}

	private  void markFavorite() {

		client.markTweetFavorite(tweet.isFavorited(),
				String.valueOf(tweet.getUid()), new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject body) {
						// finishActivity();
						if (!tweet.isFavorited()) {
							tweet.setFavorited(true);

						} else {
							tweet.setFavorited(false);

						}
						tweet.save();
						super.onSuccess(body);
					}

					public void onFailure(Throwable e, JSONObject error) {

					}
				});
	}
}
