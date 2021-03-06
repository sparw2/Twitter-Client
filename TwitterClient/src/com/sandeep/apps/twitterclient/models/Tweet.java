package com.sandeep.apps.twitterclient.models;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Column.ForeignKeyAction;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "Tweet")
public class Tweet extends Model implements Parcelable{

	@Column(name = "remoteId", index=true, unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	public String id;
	
	@Column(name = "User", onUpdate = ForeignKeyAction.CASCADE, onDelete = ForeignKeyAction.CASCADE)
	public User user;
	
	@Column(name = "body")
	public String tweetBody;
	
	@Column(name = "createdAt")
	public String created_at;
	
	@Column(name = "favorited")
	public boolean favorited;
	
	@Column(name = "favoriteCount")
	public int favoriteCount;
	
	@Column(name = "retweetCount")
	public int retweetCount;
	
	@Column(name = "retweeted")
	public boolean retweeted;
	
	@Column(name = "mediaUrl")
	public String mediaUrl;
	
	@Column(name = "replyId")
	public String replyId;

	
	public Tweet(){
		user = new User();
	}
	
	public Tweet(JSONObject jsonObj) throws JSONException{
		
		id= jsonObj.optString("id");
		created_at= jsonObj.optString("created_at");
		tweetBody= jsonObj.optString("text");
		retweetCount= jsonObj.optInt("retweet_count");
		retweeted = jsonObj.getBoolean("retweeted");
		
		favoriteCount= jsonObj.optInt("favorite_count");
		favorited = jsonObj.getBoolean("favorited");
		
		replyId = jsonObj.optString("in_reply_to_status_id");
		
		JSONObject userJson = jsonObj.getJSONObject("user");
		if (userJson != null){
			user = new User();
			user.name= userJson.optString("name");
			user.profileImageURL	= userJson.optString("profile_image_url").replace("_normal", "_bigger");
			user.username= userJson.optString("screen_name");
			
			
		}
		
		JSONArray mediaArr = jsonObj.getJSONObject("entities").optJSONArray("media");
		if (mediaArr != null){
			mediaUrl = mediaArr.getJSONObject(0).getString("media_url");
		}

		// store the tweet in background thread
		new TweetStoreAsyncTask().execute(new Object[]{this});
	}
	
	
	public static List<Tweet> fromJSONArray(JSONArray arr) throws JSONException{
		List<Tweet> list = new ArrayList<>();

		for (int i=0; i < arr.length(); i++){
			list.add(new Tweet(arr.getJSONObject(i)));
		}
		
		return list;
	}

	
	public class TweetStoreAsyncTask extends AsyncTask<Object, Void, Tweet>{

		@Override
		protected Tweet doInBackground(Object... params) {

			Tweet tweet = (Tweet)params[0];
			User userExists = new Select().from(User.class).where("username = ?",tweet.user.username).executeSingle();
			if (userExists == null){
				// persist the user
				tweet.user.save();
			}else{
				tweet.user = userExists;
			}
			
			Tweet tweetExists = new Select().from(Tweet.class).where("remoteId = ?",tweet.id).executeSingle();
			if (tweetExists == null){
				// persist the tweet
				tweet.save();
			}
			
			return tweetExists;
		}
		
	}


	public static final Parcelable.Creator<Tweet> CREATOR
	    = new Parcelable.Creator<Tweet>() {
				@Override
				public Tweet createFromParcel(Parcel in) {
				    return new Tweet(in);
				}
				
				@Override
				public Tweet[] newArray(int size) {
				    return new Tweet[size];
				}
		};

	private Tweet(Parcel in) {
		id = in.readString();
		user = in.readParcelable(User.class.getClassLoader());
		tweetBody = in.readString();
		created_at = in.readString();
		favorited = in.readByte() != 0;
		favoriteCount = in.readInt();
		retweeted = in.readByte() != 0;
		retweetCount = in.readInt();
		mediaUrl = in.readString();
		replyId = in.readString();
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeParcelable(user, flags);
		dest.writeString(tweetBody);
		dest.writeString(created_at);
		dest.writeByte((byte) (favorited ? 1 : 0));
		dest.writeInt(favoriteCount);
		dest.writeByte((byte) (retweeted ? 1 : 0));
		dest.writeInt(retweetCount);
		dest.writeString(mediaUrl);
		dest.writeString(replyId);
	}
	
}
