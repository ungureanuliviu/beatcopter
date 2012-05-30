package com.liviu.apps.beatcopter.adapters;

import java.util.ArrayList;

import com.liviu.apps.beatcopter.R;

import android.accounts.Account;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AccountsAdapter extends BaseAdapter{

	private ArrayList<Account> mItems;
	private LayoutInflater mLf;
	
	public AccountsAdapter(Context pContext) {
		mItems = new ArrayList<Account>();
		mLf = (LayoutInflater)pContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public AccountsAdapter addItem(Account pItem){
		mItems.add(pItem);
		return this;
	}
	
	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public Account getItem(int position) {		
		return mItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if(null == convertView){
			convertView = (LinearLayout)mLf.inflate(R.layout.select_account_list_item, parent, false);
			vh = new ViewHolder();			
			vh.txtTitle = (TextView)convertView.findViewById(R.id.select_account_name);
			vh.txtType  = (TextView)convertView.findViewById(R.id.select_account_type);
			convertView.setTag(vh);
		}else{
			vh = (ViewHolder)convertView.getTag();
		}
		
		// update data
		vh.txtTitle.setText(mItems.get(position).name);
		vh.txtType.setText(mItems.get(position).type);
		
		return convertView;
	}

	private class ViewHolder{
		public TextView txtTitle;
		public TextView txtType;
	}
}
