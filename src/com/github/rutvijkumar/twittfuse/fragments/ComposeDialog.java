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
import java.util.HashSet;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.rutvijkumar.twittfuse.R;
import com.github.rutvijkumar.twittfuse.Util;
import com.github.rutvijkumar.twittfuse.api.TwitterClient;
import com.github.rutvijkumar.twittfuse.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ComposeDialog extends DialogFragment {

	private Activity activity;
	private TwitterClient client;
	private User currentUser;

	private ImageView currentUserProfilePic;
    private TextView  username;
    private TextView  userScreenName;
	private TextView  remainingChars;
	private EditText  tweetTextara;
	private Button    tweetIt;
			
	private HashSet<String> replyToScreenNames;//NOT Handles twitter usernames without '@'
	
	private String replyToTweetId;
	private final static int CHARS_LIMIT=140;
	
	public ComposeDialog() {
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public void setReplyTo(String screenName,String replyToTweetId) {
		this.replyToScreenNames=new HashSet<String>();
		this.replyToScreenNames.add(screenName);
		this.replyToTweetId=replyToTweetId;
	}
	
	public void setReplyTo(HashSet<String> replyToScreenNames,String replyToTweetId) {
		this.replyToScreenNames=replyToScreenNames;
		this.replyToTweetId=replyToTweetId;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Dialog dialog = getDialog();
		Window window = dialog.getWindow();
		View view = inflater.inflate(R.layout.compose_dialog, container);
		window.requestFeature(Window.FEATURE_NO_TITLE);
		window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		setUpUI(view);
		getUserAccountDetails();
		return view;
	}

	private void setUpUI(View view) {
		// Populate UI elements with currentUser Object
		currentUserProfilePic=(ImageView)view.findViewById(R.id.currentUserProfilePic);
		username=(TextView)view.findViewById(R.id.username);
		userScreenName=(TextView)view.findViewById(R.id.userscreenname);
		remainingChars=(TextView)view.findViewById(R.id.remainingChars);
		tweetTextara=(EditText)view.findViewById(R.id.tweetTextara);
		tweetIt=(Button)view.findViewById(R.id.tweetIt);
		
		
		setActionsListeners();
	}

	
	private void setActionsListeners() {
		
		tweetIt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String tweetBody = tweetTextara.getEditableText().toString();
				if(replyToTweetId !=null) {
					postReply(tweetBody, replyToTweetId);
				}else {
					postTweet(tweetBody);
				}
				
				
			}
		});
		tweetTextara.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				int availableChar=0;
				String typedTweetText = tweetTextara.getEditableText()
						.toString().trim();
				availableChar=CHARS_LIMIT - typedTweetText.length();
				String updatedRemainingChars = String
						.valueOf((CHARS_LIMIT - typedTweetText.length()));
				remainingChars.setText(updatedRemainingChars);
				if(availableChar < 0) {
					tweetIt.setEnabled(false);
					tweetIt.setBackgroundColor(getResources().getColor(R.color.TwitterBlue_disabled));
					remainingChars.setTextColor(getResources().getColor(R.color.Red));
				}else {
					tweetIt.setEnabled(true);
					tweetIt.setBackgroundColor(getResources().getColor(R.color.TwitterBlue));
					remainingChars.setTextColor(getResources().getColor(android.R.color.black));

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
	}

	public void postReply(String tweetBody,String replyToTweetId){
		client.postTweet(tweetBody,replyToTweetId, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject arg0) {
				// TODO Auto-generated method stub
				super.onSuccess(arg0);
			}
			@Override
			public void onFailure(Throwable e, JSONObject error) {
				Log.d("DEBUG", "POST Tweet Error :" + error.toString());
				Log.e("ERROR", "Exception while posting tweet", e);	
			}
		});
	}
	public void postTweet(String tweetBody) {
		 
		client.postTweet(tweetBody, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject arg0) {
				// TODO Auto-generated method stub
				super.onSuccess(arg0);
			}
			@Override
			public void onFailure(Throwable e, JSONObject error) {
				Log.d("DEBUG", "POST Tweet Error :" + error.toString());
				Log.e("ERROR", "Exception while posting tweet", e);	
			}
		});
	}
	private void populateData() {
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(currentUser.getProfileImageUrl(), currentUserProfilePic);
		StringBuilder mentions=new StringBuilder();
		username.setText(currentUser.getName());
		userScreenName.setText(currentUser.getScreenName());
		if(replyToScreenNames!=null && replyToScreenNames.size() > 0) {
			for(String screenName:replyToScreenNames) {
				mentions.append("@"+screenName);
				mentions.append(" ");
			}
			String metionStr=mentions.toString();
			tweetTextara.setText(metionStr);
			tweetTextara.setSelection(metionStr.length());
		}
		Util.showSoftKeyboard(tweetTextara, activity);
		
	}
	private void getUserAccountDetails() {
		client = new TwitterClient(this.activity);
		client.getUserAccountDetails(new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONObject json) {
				currentUser = User.fromJson(json);
				populateData();
			}

			@Override
			public void onFailure(Throwable e, JSONObject error) {
				Log.d("DEBUG", "getAccountDetails Error :" + error.toString());
				Log.e("ERROR", "Exception while getting user Profile", e);
			}
		});
	}

}
