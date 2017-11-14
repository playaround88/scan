package com.ai.scan.redis;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ai.scan.core.IScanService;
import com.ai.scan.util.JsonUtil;

import redis.clients.jedis.Jedis;

public class ScanThread extends Thread{
	private static final Logger LOG = LoggerFactory.getLogger(ScanThread.class);
	private ScanConfig config;
	private IScanService service;
	//主线程调用关闭循环，所以加volatile
	private volatile boolean loop=true;
	
	public ScanThread(ScanConfig config){
		this.config=config;
		this.service=config.getScanService();
	}

	@Override
	public void run() {
		super.run();
		LOG.info("启动扫描线程(start thread):{}", this.config.getIdentifier());
		Jedis jedis=null;
		while(loop){
			try{
				//断线重连
				if (jedis==null || jedis.isConnected()==false) {
					if(jedis!=null) jedis.close();
					jedis = this.config.getJedisPool().getResource();
				}
				
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
						jedis.lpush(config.getQueueName(), JsonUtil.toJson(record));
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
		
		//关闭
		if(jedis!=null) jedis.close();
	}
	
	public void shutdown(){
		this.loop=false;
	}
}
