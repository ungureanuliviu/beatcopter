package com.liviu.apps.beatcopter.common;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONResponse extends JSONObject{
	public JSONResponse(String jsonString) throws JSONException {
		super(jsonString);
	}

	public JSONResponse() {	
		super();
		try {
			put("is_success", 0);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public boolean isSuccess(){
		try {
			if(getInt("is_success") == 1)
				return true;
			else
				return false;
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
	}
}
