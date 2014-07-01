
Twittfuse 2 
===========
Twittfuse is simple twitter client with additional features than TwittFuse. Allowing users to do basic twitter functionality.
This application is developed with intent to learn android programming by making it as close to real twitter app as possbile in time frame.

Time spent: 30 hours spent in total.

Completed user stories:

 * [x] Required: All user stories from TwittFuse/Week 3 (Required,Optional,Advanced,Bonus)
 * [x] Required: User can sign in to Twitter using OAuth login.
 * [x] Required: User can view the tweets from their home timeline.
 * [x] Required: User should be able to see the username, name, body and timestamp for each tweet
 * [x] Required: User should be displayed the relative timestamp for a tweet "8m", "7h"
 * [x] Required:User can view more tweets as they scroll with infinite pagination
 * [x] Optional: Links in tweets are clickable and will launch the web browser (see autolink)
 * [x] Required: User can switch between Timeline and Mention views using tabs.
 * [x] Required: User can view their home timeline tweets. 
 * [x] Required: User can view the recent mentions of their username.
 * [x] Required: User can scroll to bottom of either of these lists and new tweets will load ("infinite scroll")
 * [x] Optional: Implement tabs in a gingerbread-compatible approach (Not applicable as Implemented View Pager)
 * [x] Required: User can navigate to view their own profile
 * [x] Required: User can see picture, tagline, # of followers, # of following, and tweets on their profile.
 * [x] Required: User can click on the profile image in any tweet to see another user's profile.
 * [x] Required: User can see picture, tagline, # of followers, # of following, and tweets of clicked user.
 * [x] Required: Profile view should include that user's timeline
 * [x] Required: User can view following / followers list through the profile

 * [x] Advanced: Robust error handling, check if internet is available, handle error cases, network failures.
 * [x] Advanced: When a network request is sent, user sees an indeterminate progress indicator
 * [x] Advanced: User can "reply" to any tweet on their home timeline
 * [x] The user that wrote the original tweet is automatically "@" replied in compose
 * [x] Advanced: User can click on a tweet to be taken to a "detail view" of that tweet
 * [x] Advanced: User can take favorite (and unfavorite) or reweet actions on a tweet
 * [x] Advanced: Improve the user interface and theme the app to feel twitter branded
 * [x] Advanced: User can search for tweets matching a particular query and see results
 * [x] Bonus: User can view their direct messages (or send new ones)

Other Additional goodies :

 *  Allow user to post Tweet when network is not available (offline) tweet.
 *  App posts offline Tweet  when network is available and shows notification to User.
 *  Reply to multiple handles: App parse tweet content and find all handles from tweet and making sure no duplicate handles in reply.
 *  Embeded view all any kind of url: App shows first url in full webView allowing it to show any url not just images.
 *  App does not make extra network call on device rotations (orientation change) instead it loads tweets from local db.
 *  Text tracking counter in minus to show user no of over typed characters.
 *  Shows Retweet information on top header for every tweet ( if tweet is retweeted) just like official twitter app.
 *  User can retweet a particular tweet from Time line activity (list of tweets)  & from Tweet details activity.
 *  User can mark favorite a particular tweet from Time line activity (list of tweets) & from Tweet details activity.
 *  Shows and update retweet & favorite view counters.
 *  Shows custom view in Toast when network is unavailable.
 *  Share Tweet in email or social meida with friends.


Know issue with Fling/Fast Scroll with PullToRefreshListView:
https://github.com/erikwt/PullToRefresh-ListView/issues/42 

Bonus: User can view their direct messages (or send new ones)




Third Party Utilities/source used for building this app:

 1. CodePath Rest Template (https://github.com/thecodepath/android-rest-client-template)
 2. AsyncHttpClient
 3. UniversalImageLoader
 4. Twitter-Text 
 5. Twitter blue color : http://www.colourlovers.com/color/4099FF/Twitter_Blue
 6. How to drow spearators : http://stackoverflow.com/questions/5049852/android-drawing-separator-divider-line-in-layout
