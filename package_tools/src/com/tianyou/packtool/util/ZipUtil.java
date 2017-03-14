package com.tianyou.packtool.util;

public class ZipUtil {

	private static final int BUFFER = 1024;
	private static final String BASE_DIR = "";
	// '/'用来作为目录标识判断符
	private static final String PATH = "/";
	
	// 签名目录
	private static final String SIGN_PATH_NAME = "META-INF";
	// 修改文件目录
	private static final String UPDATE_PATH_NAME = "\\res\\raw\\channel";
	// 解压源文件目录
	private static final String SOURCE_PATH_NAME = "\\source\\";
	 /**打包目录*/  
    private static final String TARGET_PATH_NAME = "\\target\\";  
    /**签名目录*/  
    private static final String RESULT_PATH_NAME = "\\result\\";  
    /**JDK BIN 目录*/  
    private static final String JDK_BIN_PATH = "C:\\Program Files\\Java\\jdk1.6.0_26\\bin";  
    /**密钥 目录*/  
    private static final String SECRET_KEY_PATH = "F:\\document\\APK\\";  
    /**密钥 名称*/  
    private static final String SECRET_KEY_NAME = "sdk.keystore";  
}
