package com.liviu.apps.beatcopter.objects;


import com.liviu.apps.beatcopter.common.Constants;
import com.liviu.apps.beatcopter.db.annotations.DbField;
import com.liviu.apps.beatcopter.db.annotations.DbTable;
import com.liviu.apps.beatcopter.utils.Convertor;

@DbTable
public class ContentItem {

	// Constants
	private final String TAG = "ContentItem";
	private final String TYPE_IMAGE = "image";
	private final String TYPE_VIDEO = "video";
	private final String TYPE_SOUND = "sound";
	private final String TYPE_TEXT = "text";
	private final String TYPE_UNKNOWN = "unknown";
	
	
	// Data
	@DbField(autoincrement=true, canBeNull=false, primaryKey=true)
	private long mLocalId;
	
	@DbField(defaultValue="-1", canBeNull=false)
	private long mRemoteId;
	
	@DbField(defaultValue="-1", canBeNull=false)
	private long mParentId;
	
	@DbField(defaultValue=TYPE_UNKNOWN, canBeNull=false)
	private String mType; 

	/* the path of the item */
	@DbField(canBeNull=false)
	private String mPath;	
	
	public ContentItem() {
		mLocalId = Constants.INVALID_ID;
		mRemoteId = Constants.INVALID_ID;
		mParentId = Constants.INVALID_ID;
		mType = TYPE_UNKNOWN;		
		mPath = "fake path";
	}
	
	public long getLocalId(){
		return mLocalId;
	}
	
	public ContentItem setLocalId(long pLocalId){
		mLocalId = pLocalId;
		return this;
	}
	
	public long getRemoteId(){
		return mRemoteId;
	}
	
	public ContentItem setRemoteId(long pRemote){
		mRemoteId = pRemote;
		return this;
	}
	
	public long getParentId(){
		return mParentId;
	}
	
	public ContentItem setParentId(long pParentId){
		mParentId = pParentId;
		return this;
	}
	
	public String getType(){
		return mType;
	}
	
	public ContentItem setType(String pType){
		if(TYPE_IMAGE.equals(pType) 	|| 
		   TYPE_VIDEO.equals(pType)		||
		   TYPE_SOUND.equals(pType)		||
		   TYPE_TEXT.equals(pType)
		  ){
			mType = pType;
		}else{
			mType = TYPE_UNKNOWN;
		}
		return this;		
	}
	
	public String getPath(){
		return mPath;
	}
	
	public ContentItem setPath(String pPath){
		mPath = pPath;
		return this;
	}
	
	@Override
	public String toString() {	
		return Convertor.toString(this);
	}
}
