package com.liviu.apps.beatcopter.common;

import com.liviu.apps.beatcopter.SelectAccountActivity;
import com.liviu.apps.beatcopter.interfaces.IUserAction;
import com.liviu.apps.beatcopter.threadpool.IJobResultCallback;
import com.liviu.apps.beatcopter.threadpool.Job;
import com.liviu.apps.beatcopter.threadpool.ThreadPool;
import com.liviu.apps.beatcopter.utils.Console;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class UserManager implements IJobResultCallback{
	// Constants
	private final String TAG = "UserManager";
	
	// Data	
	private BaseAPI mApi;
	private ThreadPool mThreadPool;
	private Context mContext;
	
	// notifiers
	private IUserAction mUserAction; 
	
	public UserManager(Context pContext) {
		
		mApi = new BaseAPI(pContext);		
		mThreadPool = new ThreadPool(ThreadPool.FEW_THREADS);
		mContext = pContext;		
		
		mThreadPool.setCallback(this);	
	}
	
	public UserManager startSession(String pEmail, String pAuthToken){				
		final String cAuthToken	= pAuthToken;
		final String cEmail = pEmail;		
		
		mThreadPool.addJob(new Job(IUserAction.SESSION_INITIALIZED) {			
			@Override
			public Object call() throws Exception {
				return mApi.startSession(cEmail, cAuthToken);							
			}
		});
		return this;
	}	

	public UserManager setUserActionNotifier(IUserAction pNotifier) {
		mUserAction = pNotifier;
		return this;
	}

	@Override
	public void onJobDone(int jobId, Object result) {
		if( IUserAction.SESSION_INITIALIZED == jobId ){
			if( null == result ){
				// something went wrong when startSession()
				String errorMessage = mApi.getLastErrorMessage();
				Console.error(TAG, "errorMessage " + errorMessage, Console.Liviu);
				
				if( BaseAPI.MSG_INVALID_AUTH_TOKEN.equals( errorMessage) ){
					Toast.makeText( mContext, "The Google AuthToken is invalid. We will try to generate another one.", Toast.LENGTH_LONG).show();
					
					// the auth_token is invalid or expired.
					// Get a new one
					AccountManager accMan = AccountManager.get( mContext );
					Console.debug(TAG, "invalidate: " + User.getInstance(mContext).getAuthToken(), Console.Liviu);
					accMan.invalidateAuthToken(Constants.ACCOUNT_TYPE_GOOGLE, User.getInstance(mContext).getAuthToken());
					Intent toGenerateAuthToken = new Intent(mContext, SelectAccountActivity.class);
					mContext.startActivity( toGenerateAuthToken );
					
					// stop everything here
					return;				
				}
			}
		}
		if(null != mUserAction){
			mUserAction.onUserAction(jobId, (null != result), result);
		}
	}
}
