package com.diwakar.qforhashmaps.impl;

import com.diwakar.qforhashmaps.QForHashmaps;
import com.diwakar.qforhashmaps.UnacknowledgedPackets;
import com.diwakar.qforhashmaps.domain.Response;
import com.diwakar.qforhashmaps.domain.WritablePacket;
import com.diwakar.qforhashmaps.executors.ExecutorUtil;
import com.diwakar.qforhashmaps.util.Constants;
import com.diwakar.qforhashmaps.util.Utilities;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;
import rx.Observable;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@Service
public class QForHashmapsImpl extends QForHashmaps {
	private static Logger logger = LoggerFactory.getLogger(QForHashmapsImpl.class);
	
	/**
	 * This is a lookup table for mappping UUId with read timestamps
	 */
	private ConcurrentHashMap<String, Long> readTimestamps=new ConcurrentHashMap<String, Long>();

	@Autowired
	protected UnacknowledgedPackets backlog=new UnacknowledgedPacketsImpl();

	@Autowired
	private Utilities util;
	
	@Autowired
	private ExecutorUtil executorUtil;
	
	@Override
	public DeferredResult<Response<WritablePacket>> read() {
		Observable<Supplier<WritablePacket>> res=Observable.just(() -> {
			logger.debug("polling the head of the queue");
			List<Integer> q = queue.pollFirst().get(1);
			return new WritablePacket(1,q,q.size(),"");
		});

		DeferredResult<Response<WritablePacket>> result=util.emptyResponse();
		Observable<Supplier<WritablePacket>> response=res.subscribeOn(executorUtil.getReadExecutors());
		response.subscribe(
				(packet) -> {
					logger.debug("reading the queue");
					WritablePacket payload=packet.get();
					if(payload!=null){
						readTimestamps.put(payload.getUuid(), System.currentTimeMillis());
						logger.debug("setting the result");
						result.setResult(new Response<WritablePacket>(payload));
					}else
					result.setResult(new Response<WritablePacket>(new WritablePacket(-1, null, 0,""),Constants.STATUS_EMPTY));
				},
				(exception) -> {
					result.setErrorResult(new Response<WritablePacket>(new WritablePacket(-1, null, 0,""),Constants.STATUS_ERROR));
					logger.debug("Oops: {}",exception);
				}
		);
		return result;		}

	@Override
	public DeferredResult<Response<WritablePacket>> write(Integer key, ArrayList<Integer> value, Integer size) {
		ArrayList<Integer> lst = new ArrayList<Integer>();
		lst.add(key);
		lst.addAll(value);
		lst.add(size);
		Observable<WritablePacket> transformed=Observable.just(lst).flatMap(
			(a) ->  {
				String uuid=Utilities.generateUUID();
				WritablePacket packet= new WritablePacket(lst.get(0),lst.subList(1,lst.size()-1),lst.get(lst.size()-1),uuid);
				Boolean status=false;
				try {
					logger.debug("Writing the packet {}",packet);
					status=queue.offer(packet.toMap());
				} catch (Exception e) {
					logger.error("Oops:{}",e);
					throw new RuntimeException(e); // this will be caught later in the subscriber
				}
				if(status)
					return Observable.just(packet);
				else
					return Observable.just(new WritablePacket());
			}
		);
		
		DeferredResult<Response<WritablePacket>> result=util.emptyResponseWithTimeout();
		Observable<WritablePacket> response=transformed.subscribeOn(executorUtil.getWriteExecutors());
		response.subscribe(
			(output) -> {
				logger.debug("got result {}", output);
				if(!StringUtils.isEmpty(output.getUuid())){
					result.setResult(new Response<WritablePacket>(output,Constants.STATUS_OK));
				}else
					result.setResult(new Response<WritablePacket>(null,Constants.STATUS_ERROR));	
			},
			(exception) -> {
				result.setErrorResult(new Response<WritablePacket>(null,Constants.STATUS_ERROR));
				logger.error("Oops:{}",exception);					
			}
		);
		return result;
	}

	@Override
	public DeferredResult<Response<WritablePacket>> readWithBlocking(){
		Observable<Supplier<WritablePacket>> res=Observable.just(() -> {
			try {
				TreeMap<Integer,ArrayList<Integer>> p = queue.take();
				return new WritablePacket(p.lastKey(),p.get(p.lastKey()),p.get(p.lastKey()).size(),"");
			} catch (Exception e) {
				logger.error("Oops:{}",e);
				throw new RuntimeException(e);
			}
		});
		DeferredResult<Response<WritablePacket>> result=util.emptyResponseWithTimeout();
		Observable<Supplier<WritablePacket>> response=res.subscribeOn(executorUtil.getReadExecutors());
		response.subscribe(
				(packet) -> {
					WritablePacket payload=packet.get();
					if(payload!=null){
						readTimestamps.put(payload.getUuid(), System.currentTimeMillis());
						result.setResult(new Response<WritablePacket>(payload));
					}
				},
				(exception) -> {
					result.setErrorResult(new Response<WritablePacket>(new WritablePacket(-1, null, 0,""),Constants.STATUS_ERROR));
					logger.debug("Oops: {}",exception);
				}
		);
		return result;
	}

	/**
	 * Getter method
	 * @return the map with uuid-timestamp mappings
	 */
	public ConcurrentHashMap<String, Long> getReadTimestamps() {
		return readTimestamps;
	}
	/**
	 * Setter method
	 * @param readTimestamps the map to set
	 */
	public void setReadTimestamps(ConcurrentHashMap<String, Long> readTimestamps) {
		this.readTimestamps = readTimestamps;
	}
}
