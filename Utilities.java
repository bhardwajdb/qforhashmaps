package com.diwakar.qforhashmaps.util;

import com.diwakar.qforhashmaps.domain.Response;
import com.diwakar.qforhashmaps.domain.WritablePacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.UUID;

/**
 * often used methods
 * @author diwakar
 *
 */
@Component
public class Utilities {
	
	@Value("${timeout.request.s:5}")
	private long requestTimeout;
	
	private static Logger logger=LoggerFactory.getLogger(Utilities.class);
	
	/**
	 * factory method
	 * @return an empty result with error scenarios prefilled with timeout
	 */
	public DeferredResult<Response<WritablePacket>> emptyResponseWithTimeout(){
		DeferredResult<Response<WritablePacket>> result=
				new DeferredResult<Response<WritablePacket>>(0L,
						new Response<WritablePacket>(new WritablePacket(),Constants.STATUS_TIMEOUT));
		result.onCompletion(() -> {
			//put something to do here in case of completion event
			//such as kafka lognew Response<WritablePacket>
			logger.debug("result received.");
		});
		result.onTimeout(() -> {
			//put something to do here in case of timeout
		});
		return result;
	}
	/**
	 * An empty response factory method
	 * @return an empty result without timeout
	 */
	public DeferredResult<Response<WritablePacket>> emptyResponse(){
		DeferredResult<Response<WritablePacket>> result=new DeferredResult<Response<WritablePacket>>();
		result.onCompletion(() -> {
			//put something to do here in case of completion event
			//such as kafka log
			logger.debug("result received.");
		}
		);
		return result;
	}
	public static final String generateUUID(){
		UUID u=UUID.randomUUID();
		long id=u.getLeastSignificantBits();
		return System.currentTimeMillis()+Constants.ID_SEPARATOR+Long.toString(id);
	}
}
