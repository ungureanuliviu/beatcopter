package com.liviu.apps.beatcopter.threadpool;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Simple thread pool
 * It accept jobs and once a job is done, a callback {@link IJobResultCallback} will be called
 * 
 * 
 * Example:<br />
 * 		ThreadPool mPool = new ThreadPool(10);		
		mPool.registerCallback(new IJobResultCallback() {			
			public void onJobDone(long jobId, Object result) {
				Log.e(TAG, "onJobDone: " + jobId + " result: " + result);
			}
		});
		
		Job jb1 = new Job(1) {			
			public Object call() throws Exception {				
				return "job 1 done";
			}
		};
		
		Job jb2 = new Job(2) {			
			public Object call() throws Exception {				
				return "job 2 done";
			}
		};
		
		
		mPool.addJob(jb1);
		try {
			Thread.sleep(10000);
			mPool.addJob(jb2);
		} catch (InterruptedException e) {		
			e.printStackTrace();
		}		
		
 * @author liviu
 *
 */
public class ThreadPool {	

	// Constants
	private final String TAG = "ThreadPool";
	public static final int DEFAULT_THREADS_COUNT = 10;
	public static final int FEW_THREADS = 5;
	
	// Data	
	private ExecutorService mExecutor;
	private ThreadPool 		mInstance;
	private Handler 		mHandler;	
	private IJobResultCallback mCallback;
	
	public ThreadPool(int numThreads) {
		mExecutor 	= Executors.newFixedThreadPool(numThreads);		
		mHandler	= new Handler(){
			@Override
			public void handleMessage(Message msg) {				
				if( null != mCallback ){					
						mCallback.onJobDone( msg.what, msg.obj );
				}			
			}
		};
	}
	
	public ThreadPool setCallback(IJobResultCallback pJobResultCallback) {		
		mCallback = pJobResultCallback;		
		return this;
	}
		
	
	public void unregisterCallback(){		
		mCallback = null;		
	}
	
	public boolean isTerminated(){
		return mExecutor.isTerminated();
	}
	
	public boolean shutdown(){		
		return mExecutor.isShutdown();		
	}
	
	/**
	 * Submit a job to work queue
	 * Once it will be done, the callback {@link IJobResultCallback} will be called 
	 */
	public void addJob(Job pJob){
		final Job cJob = pJob;		
		mExecutor.execute(new Runnable() {		
			public void run() {
				Message msg = new Message();
				msg.what = cJob.getId();					
				try {
					msg.obj = cJob.call();					
					Log.e(TAG, "job " + msg.what + " is done");
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				mHandler.sendMessage(msg);				
			}
		});
	}
}
