package com.liviu.apps.beatcopter.interfaces;

public interface IUserAction {
	public static final int USER_CREATED = 1;
	public static final int SESSION_INITIALIZED = 2;
	
	public void onUserAction(int what, boolean isSuccess, Object resultData);
}
