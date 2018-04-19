package com.ai.scan.redis;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据扫描任务的启动类
 * 
 * @author wutb
 *
 */
public class ScanLauncher {
	private static Logger LOG=LoggerFactory.getLogger(ScanLauncher.class);
	private ScanConfig config;
	private List<ScanThread> scanThreads;
	private List<DealThread> workers;
	
	public ScanLauncher(ScanConfig config){
		this.config=config;
	}

	public void init(){
		LOG.info("启动数据扫描任务{}",config.getIdentifier());
		
		LOG.info("{}启动扫描线程",config.getIdentifier());
		for(int i=0; i<config.getScanPoolSize(); i++){
			ScanThread scanThread=new ScanThread(config);
			scanThread.start();
			this.scanThreads.add(scanThread);
		}
		
		LOG.info("{}启动处理线程池",config.getIdentifier());
		for(int i=0; i<config.getDealPoolSize(); i++){
			DealThread worker=new DealThread(config);
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
		
		LOG.info("关闭所有处理线程{}", config.getIdentifier());
		for(int i=0;i<workers.size();i++){
			workers.get(i).shutdown();
		}
		LOG.info("关闭数据扫描任务{}完成",config.getIdentifier());
	}
}
