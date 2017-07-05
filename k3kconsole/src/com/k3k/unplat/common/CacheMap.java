package com.k3k.unplat.common;

import java.util.HashMap;

public class CacheMap {
	
	private static HashMap<String, Object> cacheMap;
	
	private CacheMap(){}
	
	public static HashMap<String, Object> getCacheMap(){
		if(cacheMap == null){
			cacheMap = new HashMap<String, Object>();
		}
		return cacheMap;
	}

}
