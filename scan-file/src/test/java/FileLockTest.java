import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class FileLockTest {
	private static final String path = "e:\\scan\\test.lock";

	public static void main(String[] args) {
		String identifier=args[0];
		
		File file = new File(path);
		
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		RandomAccessFile raf;
		try {
			raf = new RandomAccessFile(file, "rw");
			FileChannel fc = raf.getChannel();
			System.out.println(identifier+" try lock file");
			FileLock fl = fc.tryLock();
			if(fl!=null){
				System.out.println(identifier+" lock file success");
				Thread.sleep(60*1000);
				
				fl.release();
				System.out.println(identifier + " release the lock!");
			}else{
				System.out.println("connot lock");
			}
			raf.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}
