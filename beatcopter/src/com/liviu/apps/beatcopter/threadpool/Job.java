package com.liviu.apps.beatcopter.threadpool;
import java.util.concurrent.Callable;

public abstract class Job implements Callable<Object>{
	
	// Constants				
	private int mId;
	
	public Job(int pId) {
		mId = pId;		
	}

	public int getId() {
		return mId;
	}

}
