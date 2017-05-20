package com.diwakar.qforhashmaps.controllers;

import com.diwakar.qforhashmaps.QForHashmaps;
import com.diwakar.qforhashmaps.domain.QRequest;
import com.diwakar.qforhashmaps.domain.Response;
import com.diwakar.qforhashmaps.domain.WritablePacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

@Controller
public class MainController {

	private final static Logger logger=LoggerFactory.getLogger(MainController.class);
	
	@Autowired
	private QForHashmaps queue;
	
	@ResponseBody
	@RequestMapping(value = "/read", method = RequestMethod.GET)
	public DeferredResult<Response<WritablePacket>> read(ModelMap model) {
		return queue.read();
	}

	@ResponseBody
	@RequestMapping(value = "/readWithBlocking", method = RequestMethod.GET)
	public DeferredResult<Response<WritablePacket>> readWithBlocking(ModelMap model){
		return queue.readWithBlocking();
	}

	@ResponseBody
	@RequestMapping(value = "/write", method = RequestMethod.POST)
	public DeferredResult<Response<WritablePacket>> write(ModelMap model,@RequestBody QRequest payload){
		logger.debug("Writing payload:{}",payload);
		return queue.write(payload.getKey(),payload.getValue(), payload.getSize());
	}
}
