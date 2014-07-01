package com.github.rutvijkumar.twittfuse.activities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rutvijkumar.twittfuse.R;
import com.github.rutvijkumar.twittfuse.TwitterUtil;
import com.github.rutvijkumar.twittfuse.Util;
import com.github.rutvijkumar.twittfuse.api.TwitterClient;
import com.github.rutvijkumar.twittfuse.models.Tweet;
import com.github.rutvijkumar.twittfuse.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.twitter.Extractor;

public class TweetDetailsActivity extends FragmentActivity implements OnNewTweetListener{

	ImageView profileImage;
	TextView name;

	TextView screenName;

	TextView tweetbody;
	TextView timestamp;
	TextView rtCount;
	TextView favCount;

	ImageButton replyAction;
	ImageButton rtAction;
	ImageButton favAction;
	ImageButton shareAction;

	EditText replyEditText;
	TextView charLimit;
	TextView tweetIt;
	WebView webView;
	
	private Tweet tweet;
	private TwitterClient client;
	private TwitterUtil twUtil;
	
	static final DateFormat df = new SimpleDateFormat("h:mm a . dd MMM yy");

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
		
		webView=(WebView)findViewById(R.id.embdView);
		profileImage.setClickable(true);
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


	private void startShareIntent() {
		String userScreenName=tweet.getUser().getScreenName();
		String userInfo=tweet.getUser().getName()+" (@"+userScreenName+")";
		
		String twtUrl = "https://twitter.com/"+userScreenName+"/status/"+tweet.getUid();
		
		String content=userInfo+" tweeted at "+df.format(tweet.getCreatedAt())+" : \n "+tweet.getBody()+"\n ("+twtUrl+")";
		Intent shareIntent = new Intent();
	    shareIntent.setAction(Intent.ACTION_SEND);
	    shareIntent.putExtra(Intent.EXTRA_TEXT, content);
	    shareIntent.setType("text/plain");
	    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Tweeet from "+userInfo);
		
		
	    startActivity(Intent.createChooser(shareIntent, "Share Tweet"));		
	}	

	private void populateValues() {

		ImageLoader imageLoader = ImageLoader.getInstance();
		User user = tweet.getUser();
		imageLoader.displayImage(user.getProfileImageUrl(), profileImage);

		name.setText(user.getName());
		screenName.setText("@" + user.getScreenName());
		tweetbody.setText(tweet.getBody());
		timestamp.setText(df.format(tweet.getCreatedAt()));
		rtCount.setText(String.valueOf(tweet.getReTweetCount()));
		favCount.setText(String.valueOf(tweet.getFavouritesCount()));

		replyEditText.setHint("Reply to " + user.getName());// Localize Reply to
		twUtil=new TwitterUtil(this, client);
		twUtil.setFavView(tweet.isFavorited(),favAction);
		twUtil.setRTView(tweet.isRetweeted(),rtAction);
		
		Extractor extractor=new Extractor();
		List<String> urls = extractor.extractURLs(tweet.getBody());
		if(urls.size() > 0) {
			showUrlInWebView(urls.get(0));
		}
		
	}

	private void showUrlInWebView(String url) {
		webView.setVisibility(View.VISIBLE);
		WebViewClient webViewClient = new WebViewClient();
		
		WebSettings webSettings = webView.getSettings();
	    webSettings.setJavaScriptEnabled(true);
	    webSettings.setDomStorageEnabled(true);
	    
//	    webView.getSettings().setJavaScriptEnabled(true);
//	    webView.getSettings().setDomStorageEnabled(true);
		webView.setWebViewClient(webViewClient);
		webView.loadUrl(url);
	}
	
	public void doAction(View view) {
		final String action_code=view.getTag().toString();
		if("FAV".equals(action_code)) {
			tweet.setFavorited(!tweet.isFavorited());
			twUtil.markFavorite(tweet, favAction,favCount,false);
		}
		else if("SHARE".equals(action_code)) {
			startShareIntent();
		}
		else if("RT".equals(action_code)) {
			twUtil.confirmRetweet(tweet,rtAction,rtCount,false);
		}else if ("REPLY".equals(action_code)) {
			twUtil.postReply(tweet);
		}
		
		
	}
	
	
	private void setupListnersForUI() {
		
	profileImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent profileActivityIntent=new Intent(TweetDetailsActivity.this, ProfileViewActivity.class);
				profileActivityIntent.putExtra("PROFILE_EXTRA_USEROBJ", tweet.getUser());
				TweetDetailsActivity.this.startActivity(profileActivityIntent);

			}
		});

		tweetIt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				postTweet(replyEditText.getEditableText().toString());
			}
		});
		

		replyEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				int availableChar=0;
				String typedTweetText = replyEditText.getEditableText()
						.toString().trim();
				availableChar=CHARS_LIMIT - typedTweetText.length();
				String updatedRemainingChars = String
						.valueOf((CHARS_LIMIT - typedTweetText.length()));
				charLimit.setText(updatedRemainingChars);
				if(availableChar < 0) {
					tweetIt.setEnabled(false);
					tweetIt.setBackgroundColor(getResources().getColor(R.color.TwitterBlue_disabled));
					charLimit.setTextColor(getResources().getColor(R.color.Red));
				}else {
					tweetIt.setEnabled(true);
					tweetIt.setBackgroundColor(getResources().getColor(R.color.TwitterBlue));
					charLimit.setTextColor(getResources().getColor(android.R.color.black));

				}
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

	

	private void postTweet(String status) {
		
		if(!Util.isNetworkAvailable(this)) {
			Toast.makeText(this, "Network unavailable :Tweet will be saved and posted later ", Toast.LENGTH_LONG);
			Util.postTweetOffline(status, String.valueOf(tweet.getUid()));
			return;
		}
		
		client.postTweet(status, String.valueOf(tweet.getUid()),  new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONObject body) {
				Toast.makeText(TweetDetailsActivity.this, "Reply posted successfully", Toast.LENGTH_LONG).show();
				TweetDetailsActivity.this.setResult(RESULT_OK);
		        TweetDetailsActivity.this.finish();
			}

			public void onFailure(Throwable e, JSONObject error) {
				Log.d("DEBUG", error.toString());
				Log.e("ERROR", "Exception while posting tweet", e);
			}
		});
	}

	@Override
	public void onNewTweet(Tweet tweet) {
		// TODO Auto-generated method stub
		
	}
}
