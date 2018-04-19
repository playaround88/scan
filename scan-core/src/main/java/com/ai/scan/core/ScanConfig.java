package com.ai.scan.core;
/**
 * 扫描参数配置
 * 
 * @author wutb
 *
 */
public class ScanConfig{
	//任务标识符
	private String identifier;
	//拉取数据线程数量
	private int scanPoolSize = 1;
	//扫描表，每次取数据的条数
	private int fetchSize = 100;
	//处理线程的个数
	private int dealPoolSize = 5;
	//队列长度
	private int queueSize = 500;
	//当扫描不到数据，或者得到的数据小于fetchSize时，休眠时间
	private long sleepTime = 10;
	//关闭时，等待处理线程关闭的最长时间
	private int blockTimeout = 10;
	
	//数据扫描和数据处理的具体实现
	private ScanService scanService;
	private DealService dealService;
	
	//方便spring直接配置
	public ScanConfig(String identifier, int scanPoolSize, int fetchSize, int dealPoolSize, int queueSize, long sleepTime, int blockTimeout,
			ScanService scanService, DealService dealService) {
		super();
		this.identifier = identifier;
		this.scanPoolSize = scanPoolSize;
		this.fetchSize = fetchSize;
		this.dealPoolSize = dealPoolSize;
		this.queueSize = queueSize;
		this.sleepTime = sleepTime;
		this.blockTimeout = blockTimeout;
		this.scanService = scanService;
		this.dealService = dealService;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public int getFetchSize() {
		return fetchSize;
	}
	public void setFetchSize(int fetchSize) {
		this.fetchSize = fetchSize;
	}
	public int getQueueSize() {
		return queueSize;
	}
	public void setQueueSize(int queueSize) {
		this.queueSize = queueSize;
	}
	public long getSleepTime() {
		return sleepTime;
	}
	public void setSleepTime(long sleepTime) {
		this.sleepTime = sleepTime;
	}

	public int getScanPoolSize() {
		return scanPoolSize;
	}

	public void setScanPoolSize(int scanPoolSize) {
		this.scanPoolSize = scanPoolSize;
	}

	public int getDealPoolSize() {
		return dealPoolSize;
	}

	public void setDealPoolSize(int dealPoolSize) {
		this.dealPoolSize = dealPoolSize;
	}

	public int getBlockTimeout() {
		return blockTimeout;
	}
	public void setBlockTimeout(int blockTimeout) {
		this.blockTimeout = blockTimeout;
	}
	public ScanService getScanService() {
		return scanService;
	}
	public void setScanService(ScanService scanService) {
		this.scanService = scanService;
	}
	public DealService getDealService() {
		return dealService;
	}
	public void setDealService(DealService dealService) {
		this.dealService = dealService;
	}

	@Override
	public String toString() {
		return "ScanConfig [identifier=" + identifier + ", scanPoolSize=" + scanPoolSize + ", fetchSize=" + fetchSize
				+ ", dealPoolSize=" + dealPoolSize + ", queueSize=" + queueSize + ", sleepTime=" + sleepTime
				+ ", blockTimeout=" + blockTimeout + ", scanService=" + scanService + ", dealService=" + dealService
				+ "]";
	}

}
