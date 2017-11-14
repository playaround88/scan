package com.ai.scan.redis;

import com.ai.scan.core.IDealService;
import com.ai.scan.core.IScanService;

import redis.clients.jedis.JedisPool;

/**
 * 扫描参数配置
 * 
 * @author wutb
 *
 */
public class ScanConfig extends com.ai.scan.core.ScanConfig{
	private String queueName;
	private JedisPool jedisPool;
	
	//方便spring直接配置
	public ScanConfig(String identifier, int fetchSize, int poolSize, String queueName, long sleepTime, int blockTimeout,
			IScanService scanService, IDealService dealService, JedisPool jedisPool) {
		
		super(identifier, fetchSize, poolSize, 0, sleepTime, blockTimeout,
				scanService, dealService);
		
		this.queueName=queueName;
		this.jedisPool=jedisPool;
	}
	
	public String getQueueName() {
		return queueName;
	}
	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}
	public JedisPool getJedisPool() {
		return jedisPool;
	}
	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}
	
}
