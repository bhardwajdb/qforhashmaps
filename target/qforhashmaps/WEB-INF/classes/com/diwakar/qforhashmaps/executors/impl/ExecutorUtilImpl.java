package com.diwakar.qforhashmaps.executors.impl;

import com.diwakar.qforhashmaps.executors.ExecutorUtil;
import com.diwakar.qforhashmaps.util.QForHashmapsThreadFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import rx.Scheduler;
import rx.schedulers.Schedulers;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;

@Service
public class ExecutorUtilImpl implements ExecutorUtil{
	
	private Scheduler readScheduler;

	@Value("${read.thread.pool.size:50}")
	private int readThreads;

	private Scheduler writeScheduler;
	
	@Value("${write.thread.pool.size:50}")
	private int writeThreads; 

	@PostConstruct
	public void init(){
		readScheduler=Schedulers.from(Executors.newFixedThreadPool(readThreads,new QForHashmapsThreadFactory("Read-pool")));
		writeScheduler=Schedulers.from(Executors.newFixedThreadPool(writeThreads,new QForHashmapsThreadFactory("write-pool")));
	}
	public Scheduler getReadScheduler() {
		return readScheduler;
	}
	@Override
	public Scheduler getReadExecutors() {
		return readScheduler;
	}
	@Override
	public Scheduler getWriteExecutors() {
		return writeScheduler;
	}


}
