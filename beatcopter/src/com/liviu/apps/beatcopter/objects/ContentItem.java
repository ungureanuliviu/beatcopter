package com.liviu.apps.beatcopter.objects;


import com.liviu.apps.beatcopter.common.Constants;
import com.liviu.apps.beatcopter.db.DBModel;
import com.liviu.apps.beatcopter.db.annotations.DbField;
import com.liviu.apps.beatcopter.db.annotations.DbTable;
import com.liviu.apps.beatcopter.utils.Convertor;
import com.liviu.apps.beatcopter.utils.Utils;

@DbTable
public class ContentItem extends DBModel{

	// Constants
	private final String TAG = "ContentItem";
	private final String TYPE_IMAGE = "image";
	private final String TYPE_VIDEO = "video";
	private final String TYPE_SOUND = "sound";
	private final String TYPE_TEXT = "text";
	private final String TYPE_UNKNOWN = "unknown";
	
	
	// Data	
	@DbField(defaultValue="-1", canBeNull=false)
	private long mRemoteId;
	
	@DbField(defaultValue=TYPE_UNKNOWN, canBeNull=false)
	private String mType; 

	/* the path of the item */
	@DbField(canBeNull=false)
	private String mPath;	
	
	@DbField(canBeNull=true, defaultValue="null")
	private Test mTest;
	
	public ContentItem() {
		super();
		long now = Utils.now();		
		mRemoteId = Constants.INVALID_ID;		
		mType = TYPE_UNKNOWN;		
		mPath = "fake path_" + now;
		mTest = new Test();
	}
	
	public ContentItem(String p2) {
		super();
		long now = Utils.now();		
		mRemoteId = Constants.INVALID_ID;		
		mType = TYPE_UNKNOWN;		
		mPath = p2;
		mTest = new Test();
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
