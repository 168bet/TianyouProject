package com.tianyou.sdk.holder;

import java.io.File;

import android.os.Environment;

/**
 * 常量池
 * @author itstrong
 *
 */
public class ConstantHolder {

	public static String PATH_ROOT = Environment.getExternalStorageDirectory().getPath() + File.separator + "tianyouxi";
	
	public static String PATH_CONFIG = PATH_ROOT + File.separator + "config";
	
	public static String PATH_IMAGE = PATH_ROOT + File.separator + "image";
}
