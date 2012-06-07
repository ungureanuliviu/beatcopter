package com.liviu.apps.beatcopter.objects;

import java.util.ArrayList;

import com.liviu.apps.beatcopter.db.DBConstants;
import com.liviu.apps.beatcopter.db.DBModel;
import com.liviu.apps.beatcopter.db.annotations.DbField;
import com.liviu.apps.beatcopter.db.annotations.DbTable;
import com.liviu.apps.beatcopter.utils.Convertor;
import com.liviu.apps.beatcopter.utils.Utils;

@DbTable
public class Beat extends DBModel{
	
	// Constants 
	private final String TAG = "Beat";
	
	// Data						
	@DbField(defaultValue = "-1")	
	private long mRemoteId;			
	
	@DbField(canBeNull = false)
	private String mTitle;
	
	@DbField
	private long mIndex; // the index of this beat in counter 0
	
	@DbField(defaultValue = DBConstants.DEFAULT_NOW)
	private long mCreateDate;
	
	@DbField
	private long mUserId;
	
	@DbField
	private ContentItem mImage;
	
	@DbField
	private ContentItem mImage2;	
	
	@DbField(canBeNull=true, defaultValue="null")
	private Test mTest;
	
	public Beat(){		
		super();
		mRemoteId = 33;
		mTitle = "Beat";
		mImage = new ContentItem();
		mImage2 = new ContentItem();
		mUserId = 1;
		mTest = new Test();				
	}
	
	public Beat(String pTitle, String p1, String p2, String t1){
		mRemoteId = 33;
		mTitle = pTitle;
		mImage = new ContentItem(p1);
		mImage2 = new ContentItem(p2);
		mUserId = 1;
		mTest = new Test(t1);		
	}
	
	@Override
	public String toString() {	
		return  Convertor.toString(this);
	}

	public ContentItem getContent() {
		return mImage;
	}
}
