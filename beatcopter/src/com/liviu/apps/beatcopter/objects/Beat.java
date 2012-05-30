package com.liviu.apps.beatcopter.objects;

import com.liviu.apps.beatcopter.db.DBConstants;
import com.liviu.apps.beatcopter.db.annotations.DbField;
import com.liviu.apps.beatcopter.db.annotations.DbTable;
import com.liviu.apps.beatcopter.utils.Convertor;
import com.liviu.apps.beatcopter.utils.Utils;

@DbTable
public class Beat {
	
	// Constants 
	private final String TAG = "Beat";
	
	// Data				
	@DbField(autoincrement=true, primaryKey = true)
	private long mLocalId;
	
	@DbField(defaultValue = "-1")	
	private long mRemoteId;			
	
	@DbField(canBeNull = false)
	private String mTitle;
	
	@DbField
	private long mIndex; // the index of this beat in counter 	
	
	@DbField(defaultValue = DBConstants.DEFAULT_NOW)
	private long mCreateDate;
	
	@DbField
	private long mParentId;
	
	@DbField
	private long mUserId;
	
	@DbField
	private ContentItem mImage;
		
	public Beat(){
		mLocalId = 1;
		mRemoteId = 33;
		mTitle = "Beat " + Utils.now();
		mImage = new ContentItem();
	}
	
	@Override
	public String toString() {	
		return Convertor.toString(this);
	}
}
