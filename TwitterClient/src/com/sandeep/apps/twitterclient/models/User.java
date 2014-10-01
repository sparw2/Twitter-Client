package com.sandeep.apps.twitterclient.models;

import java.io.Serializable;

import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "User")
public class User extends Model implements Serializable{

	private static final long serialVersionUID = 4495481175778740221L;

	@Column( name = "username", index=true , unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	public String username;

	@Column(name = "name")
	public String name;

	@Column(name = "profileImageURL")
	public String profileImageURL;

	public User(){
		
	}
	
	public static User fromJSON(JSONObject jsonObject){
		User user = new User();
		
		user.name = jsonObject.optString("name");
		user.username = jsonObject.optString("screen_name");
		user.profileImageURL = jsonObject.optString("profile_image_url");
		
		
		User userExists = new Select().from(User.class).where("username = ?",user.username).executeSingle();
		if (userExists == null){
			// persist the user
			user.save();
		}
		return user;
	}
}
