package com.tianyou.packtool.servlet;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public abstract class BaseServlet extends HttpServlet {

	protected static String PATH_ROOT;
	protected static String PATH_APK;
	protected static String PATH_KEYSTORE;
	protected static String PATH_CHANNEL;
	protected static String PATH_TOOL;
	protected static String apkToolPath;
	protected static String PATH_PACKAGE;
	protected static String PATH_KEYSTONE_INFO;
//	protected static String unApkPath;// 文件夹名
//	protected static String PATH_UNPACKAGE;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		PATH_ROOT = getServletContext().getRealPath("/WEB-INF");
		PATH_APK = PATH_ROOT + File.separator + "apk";
		PATH_KEYSTORE = PATH_ROOT + File.separator + "keystore";
		PATH_CHANNEL = PATH_ROOT + File.separator + "channel" + File.separator + "channel_info.xls";
		PATH_TOOL = PATH_ROOT+File.separator+"tools";
		apkToolPath = PATH_TOOL+File.separator+"apktool.jar";
		PATH_PACKAGE = PATH_ROOT + File.separator + "package";
		PATH_KEYSTONE_INFO = PATH_ROOT+File.separator+"keystone_info"+File.separator+"keystone_info.xls";
//		PATH_UNPACKAGE = PATH_ROOT+File.separator+"unpackage";
		String op = request.getParameter("op");
		System.out.println("op:" + op);
		doOperate(op, request, response);
	}
	public abstract void doOperate(String op, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
