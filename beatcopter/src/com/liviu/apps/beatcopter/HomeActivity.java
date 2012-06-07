package com.liviu.apps.beatcopter;

import java.util.ArrayList;

import com.liviu.apps.beatcopter.db.DbManager;
import com.liviu.apps.beatcopter.objects.Beat;
import com.liviu.apps.beatcopter.objects.ContentItem;
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
        
        dbMan.registerTables(new Class<?>[]{Beat.class, Test.class, ContentItem.class});        
        
        Beat beat = new Beat("test3", "img4", "img32", "tst3");
        Beat beat2 = new Beat("test4", "img4", "img42", "tst4");
        
        Console.debug(TAG, "newId: " + dbMan.put(beat, -1), Console.Liviu);
        Console.debug(TAG, "newId: " + dbMan.put(beat2, -1), Console.Liviu);
        ArrayList<Beat> beats = (ArrayList<Beat>) dbMan.query(Beat.class, new String[]{"*"}, null, null, null, null);        
        Console.debug(TAG, "results: " + beats , Console.Liviu);        
    }
}