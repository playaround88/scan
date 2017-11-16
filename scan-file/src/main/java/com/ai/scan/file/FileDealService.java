package com.ai.scan.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ai.scan.core.DealService;
import com.ai.scan.core.LineDealService;

/**
 * 文件处理
 * 1.入参是文件的原始绝对路径。
 * 2.添加~组成待处理文件的真是文件路径。
 * 3.逐行调用lineDealService。
 * 4.处理完成后，重命名文件为.bak。
 * 
 * @author wutb
 *
 */
public class FileDealService implements DealService{
	private static Logger LOG=LoggerFactory.getLogger(FileDealService.class);
	private LineDealService lineDealService;
	
	/**
	 * 这里获得的参数是文件的绝对路径
	 * 做读取操作的文件是“绝对路径+~”
	 * 读取完成后，修改文件名为.bak，标记处理完成
	 */
	@Override
	public void deal(Object record) {
		LOG.info("处理文件:{}", record);
		BufferedReader reader=null;
		try {
			File file=new File(record+"~");
			reader=new BufferedReader(new FileReader(file));
			
			//循环读取文件内容
			while(true){
				String line=reader.readLine();
				if(line==null){
					break;
				}
				lineDealService.deal(line);
			}
			
			//关闭reader，否则没法重命名
			reader.close();
			reader=null;
			
			//读取完成，修改文件名为.bak
			File dest=new File(record+".bak");
			boolean result=file.renameTo(dest);
			LOG.info("{}文件处理完成，重命名：{}",record,result);
			
		} catch (Exception e) {
			LOG.error("error on resolve file:{}", e);
		} finally {
			if(reader!=null){
				try{
					reader.close();
				}catch (Exception e) {
					LOG.error("error close file reader:{}", e);
				}
			}
		}
	}

	public LineDealService getLineDealService() {
		return lineDealService;
	}

	public void setLineDealService(LineDealService lineDealService) {
		this.lineDealService = lineDealService;
	}
	
}
