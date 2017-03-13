package com.tianyou.packtool.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;


public class DbcpUtils {

	private static DataSource ds;

	static {
		try {
			InputStream in = DbcpUtils.class.getClassLoader()
					.getResourceAsStream("dbcpconfig.properties");
			Properties props = new Properties();
			props.load(in);
			ds = BasicDataSourceFactory.createDataSource(props);
		} catch (Exception e) {
			System.out.println("dbcputils----"+e.toString());
//			throw new RuntimeException(e);
		}
	}

	public static DataSource getDataSource() {
		return ds;
	}

	public static Connection getConnection() {
		try {
			return ds.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
