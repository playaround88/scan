package com.ai.scan.bq;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ai.scan.core.ScanConfig;
/**
 * 数据扫描任务的启动类
 * 
 * @author wutb
 *
 */
public class ScanLauncher {
	private static Logger LOG=LoggerFactory.getLogger(ScanLauncher.class);
	private ScanConfig config;
	private BlockingQueue<Object> queue;
	private List<ScanThread> scanThreads=new ArrayList<>();
	private List<DealThread> workers=new ArrayList<>();
	
	public ScanLauncher(ScanConfig config){
		this.config=config;
	}

	public void init(){
		LOG.info("启动数据扫描任务{}",config.getIdentifier());
		LOG.info("{}创建阻塞队列长度{}",config.getIdentifier(),config.getQueueSize());
		this.queue=new ArrayBlockingQueue<>(config.getQueueSize());
		
		LOG.info("{}启动扫描线程",config.getIdentifier());
		for(int i=0; i<config.getScanPoolSize(); i++){
			ScanThread scanThread=new ScanThread(config, queue);
			scanThread.start();
			this.scanThreads.add(scanThread);
		}
		
		LOG.info("{}启动处理线程池",config.getIdentifier());
		for(int i=0;i<config.getDealPoolSize();i++){
			DealThread worker=new DealThread(config, queue);
			worker.start();
			workers.add(worker);
		}
	}
	
	public void destroy(){
		LOG.info("关闭数据扫描任务{}",config.getIdentifier());
		
		LOG.info("关闭监听线程{}",config.getIdentifier());
		for(ScanThread scanThread : scanThreads){
			scanThread.shutdown();
		}
		
		LOG.info("等待队列数据处理完成或者超时{}",config.getIdentifier());
		try {
			for(int i=0;i<config.getBlockTimeout();i++){
				if(this.queue.isEmpty()){
					break;
				}else{
					Thread.sleep(1000);
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		LOG.info("关闭所有处理线程{}", config.getIdentifier());
		for(int i=0;i<workers.size();i++){
			workers.get(i).shutdown();
		}
		LOG.info("关闭数据扫描任务{}完成",config.getIdentifier());
	}
}
