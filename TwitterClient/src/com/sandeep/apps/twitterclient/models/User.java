package com.sandeep.apps.twitterclient.models;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "User")
public class User extends Model implements Parcelable {

	@Column( name = "username", index=true , unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	public String username;

	@Column(name = "name")
	public String name;

	@Column(name = "description")
	public String description;
	
	@Column(name = "profileImageURL")
	public String profileImageURL;
	
	@Column(name = "bannerImageURL")
	public String bannerImageURL;
	
	
	@Column(name = "tweetsCount")
	public int tweetsCount;
	
	@Column(name = "followersCount")
	public int followersCount;

	@Column(name = "followingCount")
	public int followingCount;
	
	public User(){
		
	}
	
	public static User fromJSON(JSONObject jsonObject){
		User user = new User();
		
		user.name = jsonObject.optString("name");
		user.username = jsonObject.optString("screen_name");
		user.description = jsonObject.optString("description");

		user.profileImageURL = jsonObject.optString("profile_image_url");
		user.bannerImageURL = jsonObject.optString("profile_banner_url");
		user.followersCount = jsonObject.optInt("followers_count");
		user.followingCount = jsonObject.optInt("friends_count");
		user.tweetsCount = jsonObject.optInt("statuses_count");
		
		
		User userExists = new Select().from(User.class).where("username = ?",user.username).executeSingle();
		
		if (userExists == null){
			// persist the user
			user.save();
		}
		
		
		return user;
	}
	
	public static List<User> fromJSONArray(JSONArray jsonArr) throws JSONException{
		List<User> users = new ArrayList<>();
		for (int i=0 ; i < jsonArr.length();i++){
			users.add(User.fromJSON(jsonArr.getJSONObject(i)));
		}
		
		return users;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(username);
		dest.writeString(name);
		dest.writeString(description);
		dest.writeString(profileImageURL);
		dest.writeString(bannerImageURL);
		dest.writeInt(tweetsCount);
		dest.writeInt(followersCount);
		dest.writeInt(followingCount);
	}
	
	public static final Parcelable.Creator<User> CREATOR
		    = new Parcelable.Creator<User>() {
					@Override
					public User createFromParcel(Parcel in) {
					    return new User(in);
					}
					
					@Override
					public User[] newArray(int size) {
					    return new User[size];
					}
		};
		
		private User(Parcel in) {
			username = in.readString();
			name = in.readString();
			description = in.readString();
			profileImageURL = in.readString();
			bannerImageURL = in.readString();
			tweetsCount = in.readInt();
			followersCount = in.readInt();
			followingCount = in.readInt();
		}
	
}
