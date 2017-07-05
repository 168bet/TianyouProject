package com.k3k.unplat.common;

import java.util.Map;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class MultipleDataSource extends AbstractRoutingDataSource {
	
	private static final ThreadLocal<String> dataSourceKey = new InheritableThreadLocal<String>();

	public static void setDataSourceKey(String dataSource){
		dataSourceKey.set(dataSource);
	}
	
	@Override
	protected Object determineCurrentLookupKey() {
		return dataSourceKey.get();
	}
	
	@Override 
    public void setTargetDataSources(Map<Object, Object> targetDataSources) { 
        super.setTargetDataSources(targetDataSources); 
    } 

}
