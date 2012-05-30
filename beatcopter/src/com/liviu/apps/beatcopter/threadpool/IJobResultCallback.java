package com.liviu.apps.beatcopter.threadpool;

public interface IJobResultCallback {
	public void onJobDone(int jobId, Object result);
}
