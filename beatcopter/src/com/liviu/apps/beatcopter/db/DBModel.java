package com.liviu.apps.beatcopter.db;

import com.liviu.apps.beatcopter.db.annotations.DbField;
import com.liviu.apps.beatcopter.utils.Convertor;

public class DBModel {
	
	// Data
	@DbField(autoincrement=true, primaryKey = true)
	protected long mId;
	
	@DbField(defaultValue="-1")
	protected long mParentId;
	
	public DBModel() {
		mId = DBConstants.INVALID_ID;
		mParentId = DBConstants.INVALID_ID;
	}
	
	public long getId(){
		return mId;
	}
	
	public long getParentId(){
		return mParentId;
	}
	
	public DBModel setId(long pId){
		mId = pId;
		return this;
	}
	
	public DBModel setParentId(long pParentId){
		mParentId = pParentId;
		return this;
	}
	
	@Override
	public String toString() {
		return "\nsuper: mId: " + mId + " mParentId: " + mParentId;
	}
	
}
