package com.ai.scan.bq;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ai.scan.core.IScanService;
import com.ai.scan.core.ScanConfig;

public class ScanThread extends Thread{
	private static final Logger LOG = LoggerFactory.getLogger(ScanThread.class);
	private ScanConfig config;
	private BlockingQueue<Object> queue;
	private IScanService service;
	//主线程调用关闭循环，所以加volatile
	private volatile boolean loop=true;
	
	public ScanThread(ScanConfig config,BlockingQueue<Object> queue){
		this.config=config;
		this.service=config.getScanService();
		this.queue=queue;
	}

	@Override
	public void run() {
		super.run();
		LOG.info("启动扫描线程(start thread):{}", this.config.getIdentifier());
		while(loop){
			try{
				List<Object> records = service.scan(this.config.getFetchSize());
				if(records!=null){
					LOG.debug("{}扫描到数据{}条",config.getIdentifier(),records.size());
				}
				for (Object record : records) {
					//返回的列表可能含有null
					if(record==null) continue;
					// 更新状态
					int row = service.update(record);
					if (row == 1) {
						// 直接推动到队列
						queue.put(record);
					}
				}
				//如果扫描不到数据，或者扫描的数据小于fetchSize，则休眠一段时间
				if(records==null || records.size()<this.config.getFetchSize()){
					Thread.sleep(this.config.getSleepTime()*1000);
				}
			}catch (Exception e) {
				LOG.error("扫描数据异常(error scan data):{} \n {}"+this.config.getIdentifier(), e);
			}
		}
	}
	
	public void shutdown(){
		this.loop=false;
	}
}
