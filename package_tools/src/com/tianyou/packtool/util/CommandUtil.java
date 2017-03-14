package com.tianyou.packtool.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandUtil {

	public static void exceCmd(String toolsPath,String apkName, String apktoolsPath,String temp_path,String mainApkName){
		BufferedReader br = null;
		String cmd1 = "cmd /c "+toolsPath.substring(0,2);
		String cmd2 = "cd "+toolsPath;
		String cmd3 = "java -jar " + apktoolsPath + " d "+apkName;
		String cmd4 = "move " + toolsPath + File.separator + mainApkName.substring(0, mainApkName.lastIndexOf("."))+ " " + temp_path;
		String cmdStr = cmd1 +" && "+cmd2+" && "+cmd3+" && "+cmd4;
		System.out.println("cmdstr === "+cmdStr);
		try {
			Process process = Runtime.getRuntime().exec(cmdStr);
			br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null){
				sb.append(line+"\n");
			}
			System.out.println(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null){
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
