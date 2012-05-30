package com.liviu.apps.beatcopter.common;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

import com.liviu.apps.beatcopter.utils.Utils;

public abstract class BaseActivity extends Activity{
	@Override
	protected void onCreate(Bundle icicle){
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	
		super.onCreate(icicle);		
		Utils.makeFullscreen(this);
	}
}
