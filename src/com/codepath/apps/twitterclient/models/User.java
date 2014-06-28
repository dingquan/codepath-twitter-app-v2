package com.codepath.apps.twitterclient.models;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "users")
public class User extends Model {
	@Column
	private String name;
	@Column(unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private long uid;
	@Column
	private String screenName;
	@Column
	private String profileImageUrl;

	public static User fromJSON(JSONObject json) {
		User user = new User();
		try{
			user.name = json.getString("name");
			user.uid = json.getLong("id");
			user.screenName = json.getString("screen_name");
			user.profileImageUrl = json.getString("profile_image_url");
		}catch(JSONException e){
			e.printStackTrace();
			return null;
		}
		return user;
	}

	public String getName() {
		return name;
	}

	public long getUid() {
		return uid;
	}

	public String getScreenName() {
		return screenName;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public static User findById(Long id){
		return new Select().from(User.class).where("uid = ?", id).executeSingle();
	}
	
	public static List<User> findAll(){
		return new Select().from(User.class).orderBy("uid").execute();
	}

}
