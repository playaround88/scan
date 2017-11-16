package com.ai.scan.util;

import java.io.File;

public class FilePathUtil {
	/**
	 * 文件路径组装
	 * 
	 * @param path
	 * @param fileName
	 * @return
	 */
	public static String joinPath(String path, String fileName){
		if(path.endsWith(File.separator)){
			return path+fileName;
		}
		return path+File.pathSeparator+fileName;
	}
}
