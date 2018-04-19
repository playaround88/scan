import java.io.IOException;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;

import com.ai.scan.bq.ScanLauncher;
import com.ai.scan.core.LineDealService;
import com.ai.scan.core.ScanConfig;
import com.ai.scan.ftp.FTPAuthConfig;
import com.ai.scan.ftp.FTPDealService;
import com.ai.scan.ftp.FTPScanService;

public class SimpleTest {
	private static String hostname="137.32.25.15";
	private static int port = 21;
	private static String username="kafka";
	private static String password="kafka123";
	private static String pathname="/data";
	
	public static void main(String[] args) {
		FTPAuthConfig authConfig=new FTPAuthConfig(hostname,port,username,password,pathname);
		
		//扫描服务定义
		FTPScanService scanService=new FTPScanService();
		scanService.setAuthConfig(authConfig);
		scanService.setFilter(new FTPFileFilter() {
			@Override
			public boolean accept(FTPFile file) {
				if(file.getName().endsWith(".txt")){
					return true;
				}
				return false;
			}
		});
		scanService.init();
		
		//处理服务定义
		FTPDealService dealService=new FTPDealService();
		dealService.setAuthConfig(authConfig);
		dealService.setLineDealService(new LineDealService() {
			@Override
			public void deal(String line) {
				System.out.println(line);
			}
		});
		dealService.init();
		
		//
		ScanConfig config=new ScanConfig("ftp-test", 2, 5, 5, 10, 5, 10, 
				scanService, dealService);
		
		//启动扫描
		ScanLauncher launcher=new ScanLauncher(config);
		launcher.init();
		
		//阻塞前台程序
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//关闭资源链接
		launcher.destroy();
		scanService.destroy();
		dealService.destroy();
	}
}
