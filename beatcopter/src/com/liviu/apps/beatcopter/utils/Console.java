package com.liviu.apps.beatcopter.utils;

import android.util.Log;
/**  
 * @author liviu
 * 
 * Console class provide a simple way to 
 * log your messages and remove them when the code
 * is released or committed
 * 
 * Set isDev variable to <b>FALSE</b> and all logs won't 
 * be displayed.
 */

public class Console {
	/* It is nice to show just your logs and not everyone's log. 
	 * For doing this just create a new constant with you name and
	 * use it like Console.debug(TAG, "super message", Console.YOUR_NAME).
	 * Don't forget to update the CURRENT_DEVELOPER constant with your name!
	 */
	public final static String Liviu = "liviu";
	// create a new constant with your name as above
	private final static String CURRENT_DEVELOPER = "liviu"; 
	
	/** Flag to specify if the logs should be displayed or not */
	private static boolean isDev = true;
	
	/**
	 * Log an error message
	 * @param TAG 		activity's tag
	 * @param msg 		error message
	 * @param pAuthor	who wants to log the message
	 */
	public static void error(String TAG, String msg, String pAuthor){
		if(isDev && pAuthor.equals(CURRENT_DEVELOPER)){			
			Log.e(TAG, msg);
		}
	}
	
	/**
	 * Log an warning message
	 * 
	 * @param TAG activity's tag
	 * @param msg warning message
	 */
	public static void warning(String TAG, String msg, String pAuthor){
		if(isDev && pAuthor.equals(CURRENT_DEVELOPER)){			
			Log.w(TAG, msg);			
		}
	}	
	
	/**
	 * Log an debug message
	 * @param TAG	activity's tag
	 * @param msg	debug message
	 */
	public static void debug(String TAG, String msg, String pAuthor){
		if(isDev && pAuthor.equals(CURRENT_DEVELOPER)){			
			Log.d(TAG, msg);
		}
	}
}
