package com.liviu.apps.beatcopter.objects;

import com.liviu.apps.beatcopter.db.DBModel;
import com.liviu.apps.beatcopter.db.annotations.DbField;
import com.liviu.apps.beatcopter.db.annotations.DbTable;
import com.liviu.apps.beatcopter.utils.Convertor;
import com.liviu.apps.beatcopter.utils.Utils;

@DbTable
public class Test extends DBModel{

	@DbField(unique=false)
	private String mTestField;
	
	public Test() {
		super();
		mTestField = "test " + Utils.now();
	}
	
	@Override
	public String toString() {
		return Convertor.toString(this);
	}
}
