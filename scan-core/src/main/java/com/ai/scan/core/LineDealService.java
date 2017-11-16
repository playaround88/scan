package com.ai.scan.core;
/**
 * 用于文件处理、流处理的行处理服务
 * 
 * @author wutb
 *
 */
public interface LineDealService {
	/**
	 * 单行数据的处理
	 * 
	 * @param line
	 */
	public void deal(String line);
}
