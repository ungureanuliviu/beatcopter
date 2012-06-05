package com.liviu.apps.beatcopter.objects;

import com.liviu.apps.beatcopter.db.DBConstants;
import com.liviu.apps.beatcopter.db.annotations.DbField;
import com.liviu.apps.beatcopter.db.annotations.DbTable;
import com.liviu.apps.beatcopter.interfaces.IDb;
import com.liviu.apps.beatcopter.utils.Convertor;
import com.liviu.apps.beatcopter.utils.Utils;

@DbTable
public class 
Beat implements IDb{
	
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
	private long mIndex; // the index of this beat in counter 0
	
	@DbField(defaultValue = DBConstants.DEFAULT_NOW)
	private long mCreateDate;
	
	@DbField
	private long mParentId;
	
	@DbField
	private long mUserId;
	
	@DbField(canBeNull=true, defaultValue="null")
	private ContentItem mImage;
	
	@DbField(canBeNull=true, defaultValue="null")
	private Test mTest;
		
	public Beat(){
		mLocalId = 1;
		mRemoteId = 33;
		mTitle = "Beat " + Utils.now();
		mImage = new ContentItem();
		mUserId = 1;
		mTest = new Test();				
	}
	
	@Override
	public String toString() {	
		return "\n================================= BEAT ==================================\n " + Convertor.toString(this) + "\nImage: " +  mImage + "\nTest: " + mTest;
	}

	@Override
	public long getLocalId() {
		return mLocalId;
	}

	public ContentItem getContent() {
		return mImage;
	}
}
