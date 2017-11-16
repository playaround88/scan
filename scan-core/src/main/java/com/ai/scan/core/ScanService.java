package com.ai.scan.core;

import java.util.List;

/**
 * 数据扫描服务
 * 
 * @author wutb
 *
 */
public interface ScanService {
	/**
	 * 获取指定条数的数据
	 * 
	 * @param fetchSize 期望获取的数据条数
	 * @return
	 */
	List<? extends Object> scan(int fetchSize);
	/**
	 * 更新数据处理状态，更新成功返回1，其他为失败。
	 * 只有更新成功的数据，监听线程才会调度给处理线程处理。
	 * <b>数据扫描的并发情况下，依赖此方法避免数据重复加载。</b>
	 * 
	 * @param record
	 * @return
	 */
	int updateStatus(Object record);
}
