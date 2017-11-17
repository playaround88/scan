package com.ai.scan.ftp;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ai.scan.core.ScanService;

public class FTPScanService implements ScanService{
	private static Logger LOG=LoggerFactory.getLogger(FTPScanService.class);
	private FTPClientConfig config;
	private FTPAuthConfig authConfig;
	private FTPFileFilter filter;
	//FTP客户端连接
	private FTPClient ftp;
	
	public void init(){
		LOG.info("初始化FtpScanService");
		LOG.debug("auth config:{}",authConfig);
		ftp=new FTPClient();
		//特殊配置
		if(config!=null){
			ftp.configure(config);
		}
		//初始化连接
		connect();
	}
	
	public void destroy(){
		if(ftp!=null){
			try {
				ftp.quit();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private boolean connect() {
		LOG.info("FTPclient connect server...");
		boolean result=false;
		try {
			ftp.connect(authConfig.getHostname(),authConfig.getPort());
			result=ftp.login(authConfig.getUsername(), authConfig.getPassword());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<? extends Object> scan(int fetchSize) {
		List<String> result=new ArrayList<>();
		try {
			//如果未连接服务器，则调用登录
			if(!ftp.isConnected()){
					connect();
			}
			
			//扫描符合条件的文件
			FTPFile[] files=ftp.listFiles(authConfig.getPathname(), filter);
			
			if(files!=null){
				LOG.info("扫描到了{}个文件",files.length);
				//转换为文件名列表
				for(int i=0;i<files.length;i++){
					result.add(files[i].getName());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
			
		return result;
	}

	@Override
	public int updateStatus(Object record) {
		try {
			//如果未连接服务器，则调用登录
			if(!ftp.isConnected()){
					connect();
			}
			
			//修改工作目录
			ftp.changeWorkingDirectory(authConfig.getPathname());
			
			//重命名成功返回1
			boolean rt=ftp.rename((String)record, (record)+"~");
			if(rt){
				return 1;
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public FTPClientConfig getConfig() {
		return config;
	}
	public void setConfig(FTPClientConfig config) {
		this.config = config;
	}
	public FTPAuthConfig getAuthConfig() {
		return authConfig;
	}
	public void setAuthConfig(FTPAuthConfig authConfig) {
		this.authConfig = authConfig;
	}
	public FTPFileFilter getFilter() {
		return filter;
	}
	public void setFilter(FTPFileFilter filter) {
		this.filter = filter;
	}
}
