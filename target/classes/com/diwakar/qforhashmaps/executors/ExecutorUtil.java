package com.diwakar.qforhashmaps.executors;

import rx.Scheduler;

public interface ExecutorUtil {

	public Scheduler getReadExecutors();

	public Scheduler getWriteExecutors();

}
