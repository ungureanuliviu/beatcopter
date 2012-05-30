package com.liviu.apps.beatcopter.common;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;

/**
 * 
 * @author liviu
 * 
 * Generate an unique ID for every Activity passed.
 * You can pass this activity ID when you call {@link Activity#startActivityForResult(android.content.Intent, int)}
 */
public class ActivityIdProvider {

	// constants
	private final String TAG = "ActivityIDProvider";
	
	private static ActivityIdProvider 			instance;
	private static int				  			lastId;
	private static HashMap<Integer, Class<?>> 	activities;
	
	private ActivityIdProvider(){
		lastId 		= 0;
		activities 	= new HashMap<Integer, Class<?>>();
	}
	
	public static ActivityIdProvider getInstance(){
		if(instance == null){
			instance = new ActivityIdProvider();
			return instance;
		}
		else
			return instance;
	}
	
	/**
	 * Get a new unique ID for your activity
	 * <b>Do not store this ID in a data-store!</b>
	 * 
	 * @param activity
	 * @return
	 */
	public int getNewId(Class<?> activity){
		lastId++;		
		activities.put(lastId, activity);
		return lastId;
	}

	public static Class<?> getActivity(int activityId) {
		return activities.get(activityId);
	}
}
