package com.diwakar.qforhashmaps.impl;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rx.Observable;

import com.diwakar.qforhashmaps.UnacknowledgedPackets;
import com.diwakar.qforhashmaps.domain.WritablePacket;
import com.diwakar.qforhashmaps.executors.ExecutorUtil;

@Service
public class UnacknowledgedPacketsImpl extends UnacknowledgedPackets{

	@Autowired 
	private QForHashmapsImpl queue;
	
	private static Logger logger = LoggerFactory.getLogger(UnacknowledgedPacketsImpl.class);
	
	@Autowired
	private ExecutorUtil executorUtil;

	private ReentrantReadWriteLock lock= new ReentrantReadWriteLock();
	
	@Override
	public void acknowledgePacket(String messageId) {
		logger.debug("deleting from backlog {}",messageId);
		long timestamp=queue.getReadTimestamps().get(messageId);	//get read timestamp
		lock.writeLock().lock();
		unacknowledgedPackets.remove(messageId);
		queue.getReadTimestamps().remove(timestamp);
		logger.debug("removed packet from backlog {} wth read timestamp {}",messageId, timestamp);
		lock.writeLock().unlock();
	}
}
