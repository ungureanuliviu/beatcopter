package com.liviu.apps.beatcopter.common;

import com.liviu.apps.beatcopter.utils.Convertor;

import android.content.Context;
import android.content.SharedPreferences;

public class User {
	
	// Constants
	private final String TAG = "User";
	public static final String ID = "user_id";
	public static final String NAME	= "user_real_name";
	public static final String AUTH_NAME = "user_auth_name";
	public static final String EMAIL = "user_email";
	public static final String APP_KEY = "user_app_key";
	public static final String AUTH_TOKEN = "user_auth_token";
	public static final String GTALK_USER_NAME = "user_name";
	public static final String GTALK_USER_PASSWORD = "user_password";
	public static final long INVALID_ID = -1;
			
	// Data
	private long mId;
	private String mName;
	private String mAuthName;
	private String mEmail;
	private String mAppKey;
	private String mAuthToken;
	private static User mInstance;
	private DeviceDetails mDeviceDetails;
	private SharedPreferences.Editor mEditor;
	
	private User(Context pContext) {
		
		SharedPreferences prefs = pContext.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
		mEditor = prefs.edit();
		
		mId 			= prefs.getLong(ID, INVALID_ID);	
		mName 			= prefs.getString(NAME, "-");		
		mEmail 			= prefs.getString(EMAIL, null);
		mAuthName 		= prefs.getString(AUTH_NAME, null);
		mAppKey			= prefs.getString(APP_KEY, null);
		mAuthToken		= prefs.getString(AUTH_TOKEN, null);
		mDeviceDetails	= DeviceDetails.getInstance(pContext);
	}
	
	private User(){
		mId 			= INVALID_ID;
		mName 			= "unknown";		
		mEmail 			= "";
		mAuthName 		= "smartliviu@gmail.com";		
		mAppKey			= null;
		mAuthToken 		= null;
		mDeviceDetails	= DeviceDetails.DEFAULT;
	}
	
	public synchronized static User getInstance(Context pContext){
		if(mInstance != null)
			return mInstance;
		else
			return (mInstance = new User(pContext));
	}
	
	public User commitChanges(){
		mEditor.commit();
		return this;
	}
	
	public boolean isLoggedIn(){
		return mId != INVALID_ID;
	}
	
	public DeviceDetails getDeviceDetails(){
		return mDeviceDetails;
	}
	
	public String getName(){		
		return mName;
	}
	
	public User setName(String pName){
		mName = pName;
		mEditor.putString(NAME, mName);
		return this;
	}
	
	public String getAuthName(){
		return mAuthName;
	}
	
	public User setAuthName(String pAuthName){
		mAuthName = pAuthName;
		mEditor.putString(AUTH_NAME, mAuthName);
		return this;
	}
	
	public String getAppKey(){
		return mAppKey;
	}
	
	public User setAppKey(String pAppKey){
		mAppKey = pAppKey;
		mEditor.putString(APP_KEY, mAppKey);
		return this;
	}
	
	public String toString(){
		return Convertor.toString(this);	  
	}

	public User setEmail(String pEmail) { 
		mEmail = pEmail;
		mEditor.putString(EMAIL, mEmail);
		return this;
	}
	
	public String getEmail(){
		return mEmail;
	}

	public User setId(long pId) {
		mId = pId;
		mEditor.putLong(ID, mId);
		return this;
	}
	
	public long getId(){
		return mId;
	}

	public void logout() {
		mId = -1;
		mName = "unknown";
		mAppKey = null;
		mEmail = "";
		mAuthName = "unknown";
	}

	public User setAuthToken(String pAuthToken) {
		mAuthToken = pAuthToken;;
		mEditor.putString(AUTH_TOKEN, mAuthToken);
		return this;
	}

	public String getAuthToken() {
		return mAuthToken;
	}	
}
