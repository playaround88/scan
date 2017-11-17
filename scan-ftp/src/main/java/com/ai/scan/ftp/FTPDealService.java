package com.ai.scan.ftp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ai.scan.core.DealService;
import com.ai.scan.core.LineDealService;

public class FTPDealService implements DealService {
	private static Logger LOG = LoggerFactory.getLogger(FTPDealService.class);
	private FTPClientConfig config;
	private FTPAuthConfig authConfig;
	private LineDealService lineDealService;
	// FTP客户端连接
	private FTPClient ftp;

	public void init() {
		LOG.info("初始化FtpScanService");
		LOG.debug("auth config:{}", authConfig);
		ftp = new FTPClient();
		// 特殊配置
		if (config != null) {
			ftp.configure(config);
		}
		// 初始化连接
		connect();
	}

	public void destroy() {
		if (ftp != null) {
			try {
				ftp.quit();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean connect() {
		LOG.info("FTPclient connect server...");
		boolean result = false;
		try {
			ftp.connect(authConfig.getHostname(), authConfig.getPort());
			result = ftp.login(authConfig.getUsername(), authConfig.getPassword());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public void deal(Object record) {
		String filename = (String) record + "~";
		LOG.info("开始处理ftp文件:{}", filename);

		BufferedReader reader = null;
		try {
			// 如果未连接服务器，则调用登录
			if (!ftp.isConnected()) {
				connect();
			}

			ftp.changeWorkingDirectory(authConfig.getPathname());
			LOG.debug("before retrieve file(pwd):{}",ftp.printWorkingDirectory());
			
			InputStream in = ftp.retrieveFileStream(filename);
			reader = new BufferedReader(new InputStreamReader(in));
			// 循环读取文件内容
			while (true) {
				String line = reader.readLine();
				if (line == null) {
					break;
				}
				lineDealService.deal(line);
			}

			ftp.completePendingCommand();
			// 关闭reader，否则没法重命名
			reader.close();
			reader = null;
			
			// 读取完成，修改文件名为.bak
			boolean result = ftp.rename(filename, (String) record + ".bak");
			LOG.info("ftp文件{}处理完成，重命名：{}", filename, result);

		} catch (Exception e) {
			LOG.error("error on resolve ftp file:{}", e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
					reader=null;
				} catch (Exception e) {
					LOG.error("error close ftp buffered reader:{}", e);
				}
			}
		}
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
	public LineDealService getLineDealService() {
		return lineDealService;
	}
	public void setLineDealService(LineDealService lineDealService) {
		this.lineDealService = lineDealService;
	}
}
