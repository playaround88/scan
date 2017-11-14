package com.ai.scan.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ai.scan.core.IDealService;

public class ConsoleDealService implements IDealService{
	private static final Logger LOG=LoggerFactory.getLogger(ConsoleDealService.class);
	
	@Override
	public void deal(Object record) {
		LOG.info(JsonUtil.toJson(record));
	}
}
