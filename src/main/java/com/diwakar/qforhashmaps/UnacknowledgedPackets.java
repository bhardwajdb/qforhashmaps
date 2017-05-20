package com.diwakar.qforhashmaps;

import com.diwakar.qforhashmaps.domain.WritablePacket;

import java.util.concurrent.ConcurrentHashMap;
/**
 * 
 * Has those messages which are yet to be acknowledged	
 * @author diwakar
 *
 */
public abstract class UnacknowledgedPackets {
	
	protected ConcurrentHashMap<String ,WritablePacket> unacknowledgedPackets=new ConcurrentHashMap<String,WritablePacket>();

	/**
	 * delete the message from the map
	 * @param messageId the message id you want to delete
	 */
	public abstract void acknowledgePacket(String messageId);

}
