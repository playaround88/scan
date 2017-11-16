package com.ai.scan.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ai.scan.core.DealService;

import redis.clients.jedis.Jedis;

public class DealThread extends Thread {
	private static final Logger LOG = LoggerFactory.getLogger(DealThread.class);
	private ScanConfig config;
	private DealService dealService;
	// 主线程调用关闭循环，所以加volatile
	private volatile boolean loop = true;

	public DealThread(ScanConfig config) {
		this.config = config;
		this.dealService = config.getDealService();
	}

	@Override
	public void run() {
		LOG.info("启动处理线程(start deal thread):{}",config.getIdentifier());
		super.run();
		Jedis jedis=null;
		while (loop) {
			try {
				//断线重连
				if (jedis==null || jedis.isConnected()==false) {
					if(jedis!=null) jedis.close();
					jedis = this.config.getJedisPool().getResource();
				}
				String record = jedis.rpop(config.getQueueName());
				LOG.debug("{}处理线程接受到数据:{}",config.getIdentifier(), record);
				if (record != null) {
					dealService.deal(record);
				}
			} catch (Exception e) {
				LOG.error("处理过程异常(error deal data):{} \n {}", config.getIdentifier(),e);
			}
		}
	}

	public void shutdown() {
		this.loop = false;
	}

}
