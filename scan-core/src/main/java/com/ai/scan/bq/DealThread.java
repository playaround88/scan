package com.ai.scan.bq;

import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ai.scan.core.DealService;
import com.ai.scan.core.ScanConfig;

public class DealThread extends Thread {
	private static final Logger LOG = LoggerFactory.getLogger(DealThread.class);
	private ScanConfig config;
	private BlockingQueue<Object> queue;
	private DealService dealService;
	// 主线程调用关闭循环，所以加volatile
	private volatile boolean loop = true;

	public DealThread(ScanConfig config, BlockingQueue<Object> queue) {
		this.config = config;
		this.queue = queue;
		this.dealService = config.getDealService();
	}

	@Override
	public void run() {
		LOG.info("启动处理线程(start deal thread):{}",config.getIdentifier());
		super.run();
		while (loop) {
			try {
				Object record = this.queue.take();
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
