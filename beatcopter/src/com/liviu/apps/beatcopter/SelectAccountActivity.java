package com.liviu.apps.beatcopter;

import java.util.ArrayList;

import com.liviu.apps.beatcopter.adapters.AccountsAdapter;
import com.liviu.apps.beatcopter.common.ActivityIdProvider;
import com.liviu.apps.beatcopter.common.BaseActivity;
import com.liviu.apps.beatcopter.common.Constants;
import com.liviu.apps.beatcopter.utils.Console;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class SelectAccountActivity extends BaseActivity implements OnItemClickListener{

	// Constants
	private final String TAG = "SelectAccountActivity";
	public static final int ACTIVITY_ID = ActivityIdProvider.getInstance().getNewId(SelectAccountActivity.class);	
	
	// Data
	private AccountsAdapter adapter;
	
	// UI
	private ListView lstAccounts;			
	
	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.select_account_layout);
				
		lstAccounts = (ListView)findViewById(R.id.select_account_lst_accounts);
		adapter = new AccountsAdapter(this);
		
		ArrayList<Account> ret = new ArrayList<Account>();
		Account[] accounts = AccountManager.get(this).getAccounts();
		
		for (Account account : accounts){
			if(account.type.equals(Constants.ACCOUNT_TYPE_GOOGLE)){
				adapter.addItem(account);
			}
		}			
		
		lstAccounts.setAdapter(adapter);
		
		// set listeners
		lstAccounts.setOnItemClickListener(this);		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
		Console.debug(TAG, "selected account: " + adapter.getItem(position).name + " type: " + adapter.getItem(position).type, Console.Liviu);
		
		Intent generateToken = new Intent(SelectAccountActivity.this, GenerateTokenActivity.class);
			generateToken.putExtra(Constants.KEY_CURRENT_ACCOUNT, adapter.getItem(position));
			startActivityForResult(generateToken, GenerateTokenActivity.ACTIVITY_ID);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(GenerateTokenActivity.ACTIVITY_ID == requestCode){
			setResult(resultCode);
			finish();
		}
	}
}
