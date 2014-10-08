Extended Twitter client with tab interface and fragments.

Time Spent: 25 hours

Completed user stories:
<ol>
<li> Includes all required user stories from Week 3 Twitter Client </li>
<li> User can switch between Timeline and Mention views using tabs. </li>
<li> User can view their home timeline tweets </li>
<li> User can view the recent mentions of their username. </li>
<li> User can scroll to bottom of either of these lists and new tweets will load ("infinite scroll") </li>
<li> Optional: Implement tabs in a gingerbread-compatible approach </li>
<li> User can navigate to view their own profile </li>
<li> User can see picture, tagline, # of followers, # of following, and tweets on their profile. </li>
<li> User can click on the profile image in any tweet to see another user's profile. </li>
<li> User can see picture, tagline, # of followers, # of following, and tweets of clicked user. </li>
<li> Profile view should include that user's timeline </li>
<li> Optional: User can view following list through the profile </li>
<li> Advanced: Robust error handling, check if internet is available, handle error cases, network failures </li>
<li> Advanced: When a network request is sent, user sees an indeterminate progress indicator </li>
<li> Advanced: User can "reply" to any tweet on their home timeline </li>
<li> The user that wrote the original tweet is automatically "@" replied in compose </li>
<li> Advanced: User can click on a tweet to be taken to a "detail view" of that tweet </li>
<li> Advanced: User can take favorite (and unfavorite) on a tweet </li>
<li> Advanced: Improve the user interface and theme the app to feel twitter branded </li>
<li> Advanced: User can search for tweets matching a particular query and see results </li>
</ol>


Walk-through of all user stories:
[link](http://youtu.be/AR23q12zAUE) 


	



Simple Twitter client that supports viewing a Twitter timeline and composing a new tweet.

Time spent: 22 hours 

Completed user stories:
<ol>
<li> User can sign in to Twitter using OAuth login </li>
<li> User should be displayed the username, name, and body for each tweet </li>
<li> User should be displayed the username, name, and body for each tweet </li>
<li> User should be displayed the relative timestamp for each tweet "8m", "7h" </li>
<li> Optional: Links in tweets are clickable and will launch the web browser (see autolink) </li>
<li> User can compose a new tweet </li>
<li> User can click a “Compose” icon in the Action Bar on the top right </li>
<li> User can then enter a new tweet and post this to twitter </li>
<li> User is taken back to home timeline with new tweet visible in timeline </li>
<li> Optional: User can see a counter with total number of characters left for tweet </li>
<li> Advanced: User can refresh tweets timeline by pulling down to refresh (i.e pull-to-refresh) </li>
<li> Advanced: User can open the twitter app offline and see last loaded tweets </li>
<li> Tweets are persisted into sqlite and can be displayed from the local DB </li>
<li> Bonus: Compose activity is replaced with a modal overlay </li>
</ol>

The following libraries are used to make this possible:

 * [scribe-java](https://github.com/fernandezpablo85/scribe-java) - Simple OAuth library for handling the authentication flow.
 * [Android Async HTTP](https://github.com/loopj/android-async-http) - Simple asynchronous HTTP requests with JSON parsing
 * [codepath-oauth](https://github.com/thecodepath/android-oauth-handler) - Custom-built library for managing OAuth authentication and signing of requests
 * [UniversalImageLoader](https://github.com/nostra13/Android-Universal-Image-Loader) - Used for async image loading and caching them in memory and on disk.
 * [ActiveAndroid](https://github.com/pardom/ActiveAndroid) - Simple ORM for persisting a local SQLite database on the Android device

Walkthrough of all user stories:
[link](http://youtu.be/aLPFM_ZeAlI) or [here](https://vimeo.com/107717509)

