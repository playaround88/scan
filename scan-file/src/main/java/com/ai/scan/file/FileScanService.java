package com.ai.scan.file;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ai.scan.core.ScanService;

/**
 * 目录文件扫描实现类 
 * 1.扫描目录符合条件的文件。 
 * 2.修改文件名称添加一个~，标识文件正在被处理。
 * 
 * @author Administrator
 *
 */
public class FileScanService implements ScanService {
	private static Logger LOG = LoggerFactory.getLogger(FileScanService.class);
	private String path;
	private FileFilter filter;

	@Override
	public List<? extends Object> scan(int fetchSize) {
		File dir = new File(path);
		if (!dir.exists()) {
			throw new RuntimeException("Scan file path not exists!");
		}
		File[] files = dir.listFiles(filter);

		if (files == null || files.length == 0) {
			return null;
		}

		// 转换，返回文件的路径
		List<String> result = new ArrayList<String>();
		try {
			for (int i = 0; i < files.length; i++) {
				result.add(files[i].getCanonicalPath());
			}
		} catch (IOException e) {
			LOG.error("解析扫描到的文件路径发生错误:{}", e);
		}
		LOG.info("{}扫描到{}个文件", path, result.size());
		return result;
	}

	@Override
	public int updateStatus(Object record) {
		int rv=0;
		
		File lock = new File((String)record + ".lock");
		File file = new File((String) record);
		File dest = new File((String) record + "~");
		
		//获取锁，如果file文件还存在，则重命名
		RandomAccessFile raf=null;
		try {
			raf = new RandomAccessFile(lock, "rw");
			FileChannel fc = raf.getChannel();
			FileLock fl = fc.lock();
			
			// 文件可能已经被重命名
			if (file.exists()) {
				boolean result = file.renameTo(dest);
				LOG.info("{} rename result:{}", record, result);
				if (result) {
					rv=1;
				}
			}
			
			fl.release();
		} catch (Exception e) {
			LOG.error("锁定重命名文件异常:{}", e);
		}finally {
			//释放资源
			if(raf!=null){
				try {
					raf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			//删除锁文件
			LOG.info("删除锁文件：{}", (String)record + ".lock");
			lock.delete();
		}
		
		return rv;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public FileFilter getFilter() {
		return filter;
	}

	public void setFilter(FileFilter filter) {
		this.filter = filter;
	}

}
