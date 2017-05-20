package com.diwakar.qforhashmaps;

import com.diwakar.qforhashmaps.domain.Response;
import com.diwakar.qforhashmaps.domain.WritablePacket;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.concurrent.LinkedBlockingDeque;
/**
 * A Reactive message broker
 * @author diwakar
 *
 */
public abstract class QForHashmaps {
	

	protected LinkedBlockingDeque<TreeMap<Integer,ArrayList<Integer>>> queue= new LinkedBlockingDeque<TreeMap<Integer,ArrayList<Integer>>>();//has unread messages
	
	/**
	 * take the message out from the queue,
	 * push it into backlog and return it.
	 * If no messages are there, return null
	 * @return the packet to be read
	 */
	public abstract DeferredResult<Response<WritablePacket>> read();
	
	/**
	 * take the message out from the queue,
	 * push it into backlog and return it.
	 * If no messages are there, block until one becomes available until default timeout.
	 * @return the packet to be read
	 */
	public abstract DeferredResult<Response<WritablePacket>> readWithBlocking();
	/**
	 * calculate a UUID to give back to the writer, and push it into the queue at the tail with the UUID 
	 * @param packet the packet to be written
	 * @return the packet with status
	 */
	public abstract DeferredResult<Response<WritablePacket>> write(Integer key, ArrayList<Integer> value, Integer size);


}
