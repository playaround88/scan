import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import com.ai.scan.bq.ScanLauncher;
import com.ai.scan.core.LineDealService;
import com.ai.scan.core.ScanConfig;
import com.ai.scan.file.FileDealService;
import com.ai.scan.file.FileScanService;

public class SimpleTest {
	private static final String sanPath="e:\\scan";

	public static void main(String[] args) {
		//扫描服务
		FileScanService scanService=new FileScanService();
		scanService.setPath(sanPath);
		scanService.setFilter(new MyFileFilter());
		
		//处理服务
		LineDealService lineDealService=new LineDealService() {
			@Override
			public void deal(String line) {
				System.out.println(line);
			}
		};
		
		FileDealService dealService=new FileDealService();
		dealService.setLineDealService(lineDealService);
		
		//启动配置
		ScanConfig config=new ScanConfig("scan-test", 5, 2, 10, 10, 10, 
				scanService, dealService);
		
		//启动扫描任务
		ScanLauncher launcher=new ScanLauncher(config);
		launcher.init();
		
		//阻塞主线程
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//关闭扫描任务
		launcher.destroy();
	}
	
	static class MyFileFilter implements FileFilter{

		@Override
		public boolean accept(File pathname) {
			try {
				if(pathname.getCanonicalPath().endsWith(".txt")){
					return true;
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}
		
	}
}
