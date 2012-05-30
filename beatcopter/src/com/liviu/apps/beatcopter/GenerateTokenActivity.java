package com.liviu.apps.beatcopter;

import java.io.IOException;

import com.liviu.apps.beatcopter.common.ActivityIdProvider;
import com.liviu.apps.beatcopter.common.Constants;
import com.liviu.apps.beatcopter.common.User;
import com.liviu.apps.beatcopter.common.UserManager;
import com.liviu.apps.beatcopter.interfaces.IUserAction;
import com.liviu.apps.beatcopter.utils.Console;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

public class GenerateTokenActivity extends com.liviu.apps.beatcopter.common.BaseActivity implements  IUserAction,
																	AccountManagerCallback{
	// Constants 
	private final String TAG = "GenerateTokenActivity";
	public static final int ACTIVITY_ID = ActivityIdProvider.getInstance().getNewId(GenerateTokenActivity.class);
	
	// Data
	private Account selectedAccount;
	private UserManager userMan;
	
	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.select_account_layout);		
		
		userMan = new UserManager( this );
		userMan.setUserActionNotifier(this);
		if(getIntent() == null){
			Toast.makeText(this, "No user selected", Toast.LENGTH_LONG).show();
			finish();
		}else if(getIntent().getParcelableExtra(Constants.KEY_CURRENT_ACCOUNT) == null){
			Toast.makeText(this, "No user selected1", Toast.LENGTH_LONG).show();
			finish();			
		}else{
			selectedAccount = (Account)getIntent().getParcelableExtra(Constants.KEY_CURRENT_ACCOUNT);
		}
		
		Console.debug(TAG, "selectedAccount: " + selectedAccount, Console.Liviu);
		AccountManager accMan = AccountManager.get( GenerateTokenActivity.this );
		AccountManagerFuture<Bundle> accountManagerFuture = accMan.getAuthToken(selectedAccount, "cp", true, this, null);		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		Intent intent = getIntent();
        AccountManager accountManager = AccountManager.get(getApplicationContext());
        Account account = (Account)intent.getExtras().get(Constants.KEY_CURRENT_ACCOUNT);
        accountManager.getAuthToken(account, "cp", false, this, null);
	}

	
	
	@Override
	public void onUserAction(int what, boolean isSuccess, Object resultData) {
		switch (what) {
			case IUserAction.SESSION_INITIALIZED:
				if(isSuccess){
					User userObj = (User)resultData;
					if(null != userObj){
						Toast.makeText(GenerateTokenActivity.this, "Welcome " + userObj.getName(), Toast.LENGTH_LONG).show();
						SharedPreferences prefs = GenerateTokenActivity.this.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
						SharedPreferences.Editor ed = prefs.edit();
						
						// save user details so other apps can use them later
						ed.putString(User.AUTH_NAME, userObj.getAuthName());
						ed.putLong(User.ID, userObj.getId());
						ed.putString(User.APP_KEY, userObj.getAppKey());
						ed.putString(User.NAME, userObj.getName());
						ed.putString(User.EMAIL, userObj.getEmail());
						
						// commit changes
						ed.commit();					
			            
						Intent registrationIntent = new Intent("com.google.android.c2dm.intent.REGISTER");
			            registrationIntent.putExtra("app", PendingIntent.getBroadcast(this, 0, new Intent(), 0));
			            registrationIntent.putExtra("sender", "skyduiapp@gmail.com");
			            startService(registrationIntent);
			            
						// we are done here, we should go to HomeScreen..which is not ready yet so..
						setResult(GenerateTokenActivity.RESULT_OK);						
					}else{
						Toast.makeText(GenerateTokenActivity.this, "Sorry, something went wrong", Toast.LENGTH_LONG).show();
						setResult(GenerateTokenActivity.RESULT_CANCELED);
						/*
						 *  maybe the account already exits
						 *  we should try to login in him but ask for password first
						 */					
					}
				} else {
					Toast.makeText(GenerateTokenActivity.this, "Sorry, something went wrong", Toast.LENGTH_LONG).show();
					setResult(GenerateTokenActivity.RESULT_CANCELED);
				}
				finish();
			break;	
			default:
				break;
		}
	}

	@Override
	public void run(AccountManagerFuture result) {
		Bundle bundle;		
		try {
			bundle = (Bundle) result.getResult();
			Intent intent = (Intent)bundle.get( AccountManager.KEY_INTENT );
			if( null != intent ){
				// user input required
				startActivity( intent );
			} else {
				String authToken = (String) bundle.get(AccountManager.KEY_AUTHTOKEN);				
				Console.debug(TAG, "authToken: " + authToken, Console.Liviu);
				if(null != authToken){					
					userMan.startSession(selectedAccount.name, authToken);
				}	
			}			
		} catch (OperationCanceledException e) {			
			e.printStackTrace();
		} catch (AuthenticatorException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
}

/*
	DQAAAMIAAAAgaD4-qe4MHyeeGPD4f_fu4yq9xnI1iVKcYT2zF28vf2RigW58AEwl3d3rIJ3i8WRveXsN6gjv8w0-ntMBWUUof6BgV9WbFvbIIZ4icfwxc14ZBnXtSzeXTcS4aNCL4L75dnd_wnJ4Q0tc5rKk8VwcNm86GpZV9sgf85TDhgoLbHUB3MDrocc172Z8b8c_JI4T6LDvdmDM65QYHGdqwBPAIR-5i2ihFBx5O2CtaPbVFD14qr_1WTPK8QTAd7MTWJwAFpmhPhQLmv7bdhV-w7K_
	DQAAAMMAAAC0FMUqkRcmWEcFEcnhwUi9kMVnFUF_kdQVXMEas09-Mmh8HpIweKakyo_zBMKzQ807RaVEXp9Z_WnbfWhDhNwSaW8yr9joWR5lq7TQz8rGdxhbJuMUv6OiiU4lNYpAB8p3swbx5_mje_sisPUKsfh3m3xReL1ZlhkZzMJROScxDGIwvBSD4Ne2C-Tb85PUAvNjH0gCVkuwhzorLY99-nAtTZsVAqfvYtjotr9RnI-aHQdXEq3XYSJUAuubtcA5_Z1vRB4AZlpnga87ZCBeMlpE
	DQAAAMIAAAAgaD4-qe4MHyeeGPD4f_fu4yq9xnI1iVKcYT2zF28vf2RigW58AEwl3d3rIJ3i8WRveXsN6gjv8w0-ntMBWUUof6BgV9WbFvbIIZ4icfwxc14ZBnXtSzeXTcS4aNCL4L75dnd_wnJ4Q0tc5rKk8VwcNm86GpZV9sgf85TDhgoLbHUB3MDrocc172Z8b8c_JI4T6LDvdmDM65QYHGdqwBPAIR-5i2ihFBx5O2CtaPbVFD14qr_1WTPK8QTAd7MTWJwAFpmhPhQLmv7bdhV-w7K_
	
	curl --header "Authorization: GoogleLogin auth=DQAAAMIAAAAgaD4-qe4MHyeeGPD4f_fu4yq9xnI1iVKcYT2zF28vf2RigW58AEwl3d3rIJ3i8WRveXsN6gjv8w0-ntMBWUUof6BgV9WbFvbIIZ4icfwxc14ZBnXtSzeXTcS4aNCL4L75dnd_wnJ4Q0tc5rKk8VwcNm86GpZV9sgf85TDhgoLbHUB3MDrocc172Z8b8c_JI4T6LDvdmDM65QYHGdqwBPAIR-5i2ihFBx5O2CtaPbVFD14qr_1WTPK8QTAd7MTWJwAFpmhPhQLmv7bdhV-w7K_" "https://www.google.com/m8/feeds/contacts/default/full" -k

*/
