package com.liviu.apps.beatcopter.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import com.liviu.apps.beatcopter.utils.Convertor;
import com.liviu.apps.beatcopter.utils.Utils;

public class DeviceDetails {
	public static final DeviceDetails DEFAULT = null;
	// Constants
	private final String TAG = "DeviceDetails";
	private final String KEY_DEVICE_ID = "key_device_id";
	private final String KEY_DEVICE_PUSH_ID = "key_device_push_id";
	private final String KEY_DEVICE_NAME = "key_device_name";
	
	// Data
	private static DeviceDetails mInstance;
	private String mDeviceId;
	private String mName;
	private String mDevicePushId;
	
	private SharedPreferences mPrefs;
	private SharedPreferences.Editor mPrefsEditor;
	
	private DeviceDetails(Context pContext) {		
		mPrefs = pContext.getSharedPreferences( Constants.PREFS_NAME, Context.MODE_PRIVATE);
		mDeviceId = mPrefs.getString(KEY_DEVICE_ID, null);
		mDevicePushId = mPrefs.getString(KEY_DEVICE_PUSH_ID, null);
		mName = mPrefs.getString(KEY_DEVICE_NAME, null);				
		mPrefsEditor = mPrefs.edit();		
		
		if(null == mName){
			setDeviceName(Build.MODEL);			
		}
		
		if(null == mDeviceId){
			setDeviceId(Utils.getDeviceId(pContext));
		}		
		commitChanges();
	}
	
	public static DeviceDetails getInstance(Context pContext){
		if(null == mInstance){
			return (mInstance = new DeviceDetails(pContext));
		} else {
			return mInstance;
		}
	}
	
	private DeviceDetails setDeviceId(String pId){
		mDeviceId = pId;
		mPrefsEditor.putString(KEY_DEVICE_ID, pId);		
		return this;
	}
	
	public String getDeviceId(){
		return mDeviceId;
	}
	
	public DeviceDetails setDevicePushId(String pId){
		mDevicePushId = pId;
		mPrefsEditor.putString(KEY_DEVICE_PUSH_ID, pId);
		return this;
	}
	
	public String getDevicePushId(){
		return mDevicePushId;
	}
	
	private DeviceDetails setDeviceName(String pName){
		mName = pName;
		mPrefsEditor.putString(KEY_DEVICE_NAME, pName);
		return this;
	}
	
	public String getDeviceName(){
		return mName;
	}
	
	@Override
	public String toString() {
		return Convertor.toString(this);
	}
	
	public DeviceDetails commitChanges(){
		mPrefsEditor.commit();
		return this;
	}
}
