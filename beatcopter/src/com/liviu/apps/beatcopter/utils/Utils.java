package com.liviu.apps.beatcopter.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.view.Window;
import android.view.WindowManager;

public class Utils {
	
	public static String md5PhpCompatible(String input) throws NoSuchAlgorithmException {
	    String result = input;
	    if(input != null) {
	        MessageDigest md = MessageDigest.getInstance("MD5"); //or "SHA-1"
	        md.update(input.getBytes());
	        BigInteger hash = new BigInteger(1, md.digest());
	        result = hash.toString(16);
	        while(result.length() < 32) {
	            result = "0" + result;
	        }
	    }
	    return result;
	}
	
	public static String md5(String s){		
		if( null == s )
			return "";				
		try{
			// Create MD5 Hash
			MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++){
				hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
			}
			return hexString.toString();
		}
		catch (NoSuchAlgorithmException e){
			e.printStackTrace();
		}
		return "";
	}
	
	public static void makeFullscreen(Activity pActivity){
		if (pActivity != null){
			try{
				pActivity.requestWindowFeature(Window.FEATURE_NO_TITLE);				
			}
			catch (Exception e){
				e.printStackTrace();
			}
			Window w = pActivity.getWindow();
			if (w != null){
				w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
			}
		}
	}
	
	/**
	 * Get a unique id for current device
	 * @param pContext
	 * @return an unique id
	 */
	public static String getDeviceId(Context pContext){
		final TelephonyManager tm = (TelephonyManager) pContext.getSystemService(Context.TELEPHONY_SERVICE);

	    final String tmDevice, tmSerial, androidId;
	    tmDevice = "" + tm.getDeviceId();
	    tmSerial = "" + tm.getSimSerialNumber();
	    androidId = "" + android.provider.Settings.Secure.getString(pContext.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
	    
	    UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
	    return deviceUuid.toString();
	}

	public static Long now() {
	    Date d = new Date();	    
	    return d.getTime();	        
	}			
}
