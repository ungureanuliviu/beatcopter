package com.liviu.apps.beatcopter;

import java.util.ArrayList;

import com.liviu.apps.beatcopter.db.DbManager;
import com.liviu.apps.beatcopter.objects.Beat;
import com.liviu.apps.beatcopter.objects.Test;
import com.liviu.apps.beatcopter.utils.Console;

import android.app.Activity;
import android.os.Bundle;

public class HomeActivity extends Activity {

	// Constants
	private final String TAG = "HomeActivity";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        DbManager dbMan = DbManager.getInstance(this);
        
        dbMan.registerTables(new Class<?>[]{Beat.class, Test.class});        
        
        Beat beat = new Beat();
        Console.debug(TAG, "newId: " + dbMan.put(beat), Console.Liviu);
        ArrayList<Beat> beats = (ArrayList<Beat>) dbMan.query(Beat.class, new String[]{"mTitle"}, null, null, null, null);        
        Console.debug(TAG, "results: " + beats , Console.Liviu);
        
        Test t = new Test();
        dbMan.put(t);
        Console.debug(TAG, "test results: " + dbMan.query(Test.class, new String[]{"mTestField"}, null, null, null, null), Console.Liviu);
        
        dbMan.put(new String("sss"));
    }
}