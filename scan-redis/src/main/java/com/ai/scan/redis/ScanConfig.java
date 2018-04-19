package com.ai.scan.redis;

import com.ai.scan.core.DealService;
import com.ai.scan.core.ScanService;

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
	public ScanConfig(String identifier, int scanPoolSize, int fetchSize, int dealPoolSize, String queueName, long sleepTime, int blockTimeout,
			ScanService scanService, DealService dealService, JedisPool jedisPool) {
		
		super(identifier,scanPoolSize, fetchSize, dealPoolSize, 0, sleepTime, blockTimeout,
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
