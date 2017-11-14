package com.ai.scan.util;

import com.google.gson.Gson;

public class JsonUtil {
	private static final Gson gson=new Gson();
	
	public static <T> T fromJson(String json,Class<T> c){
		return gson.fromJson(json, c);
	}
	
	public static String toJson(Object obj){
		return gson.toJson(obj);
	}

}
