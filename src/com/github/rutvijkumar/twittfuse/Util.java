/***

The MIT License (MIT)
Copyright � 2014 Rutvijkumar Shah
 
Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the �Software�), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute,
sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
 
The above copyright notice and this permission notice shall be included in all copies or substantial
portions of the Software.
 
THE SOFTWARE IS PROVIDED �AS IS�, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

 ***/

package com.github.rutvijkumar.twittfuse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.util.TypedValue;

import com.github.rutvijkumar.twittfuse.models.Tweet;

public class Util {

	private static final String TWITTER = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";

	

	public static long getMaxSinceId(ArrayList<Tweet> tweets) {
		long maxSinceId = 1L;
		long tweetUid = 0L;
		for (Tweet tweet : tweets) {
			tweetUid = tweet.getUid();
			if (tweetUid > maxSinceId) {
				maxSinceId = tweet.getUid();
			}
		}
		return maxSinceId;
	}

	public static String fromNow(Date date) {
		String fromNow = null;
		Date today = new Date();
		long now = today.getTime();

		long then = date.getTime();

		long diff = (now - then) / 1000;
		if (diff < 1) {
			fromNow = "0 s";
		} else {
			if (diff > 60) {
				diff = diff / 60;
				if (diff <= 0) {
					fromNow = "1 m";
				} else if (diff < 60) {
					fromNow = diff + " m";
				} else {
					diff = (diff / 60);
					if (diff <= 0) {
						fromNow = "1 h";
					} else if (diff < 24) {
						fromNow = diff + " h";
					} else {
						diff = (diff / 24);
						fromNow = diff + " d";
					}
				}
			} else {
				fromNow = diff + " s";
			}
		}
		return fromNow;
	}

	public static String getDuration(String timeStamp) throws ParseException {
		Date date = getTwitterDate(timeStamp);
		return fromNow(date);
	}

	public static Date getTwitterDate(String date) {
		Date dt=new Date();
		SimpleDateFormat sf = new SimpleDateFormat(TWITTER);
		sf.setLenient(true);
		try {
			dt= sf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dt;
	}

	public static String getDuration(Date createdAt) {
		return fromNow(createdAt);
	}
}
