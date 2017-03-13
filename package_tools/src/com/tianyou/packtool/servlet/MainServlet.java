package com.tianyou.packtool.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.mail.internet.NewsAddress;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.tianyou.packtool.domain.Channel;
import com.tianyou.packtool.util.CommandUtil;
import com.tianyou.packtool.util.DateUtil;
import com.tianyou.packtool.util.ExcelReaderUtil;
import com.tianyou.packtool.util.WriteUtil;

@SuppressWarnings("serial")
public class MainServlet extends BaseServlet {

	private String mainApkName;		//当前主包的文件名
	private Map<String, String> keystoreInfo;	//当前签名的文件名
	private String packName;		//当前要修改的包名
	private List<Channel> channelLists;		//已选渠道列表
	private String tempPath;
	private String packagePath;
	
	@Override
	public void doOperate(String op, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if ("packing".equals(op)) {
			startPacking(request, response);
		} else if ("upload_package".equals(op)) {
			uploadFile("upload_package", request, response);
		} else if ("index".equals(op)) {
			showIndex(request, response);
		} else if ("pkg_list".equals(op)) {
			displayApklist(request, response);
		} else if ("pkg_dir_list".equals(op)) {
			displayApkDirlist(request, response);
		} else if ("down_pkg_dir".equals(op)) {
			downloadAllPackage(request, response);
		} else if ("del_pkg_dir".equals(op)) {
			deleteAllPackage(request, response);
		} else if ("clean_channel".equals(op)) {
			channelLists = null;
			request.getSession().removeAttribute("select_channel");
			request.getRequestDispatcher("/index.jsp").forward(request, response);
		}
	}

	/**
	 * 开始打包
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void startPacking(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 获取表单数据
		boolean result = getFormData(request, response);
		if (!result) return;
		packagePath = PATH_PACKAGE + File.separator + new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
		new File(packagePath).mkdirs();
		// 批量打包
		new Thread(new Runnable() {
			@Override
			public void run() {
				batchPacking();
			}
		}).start();
		// 转向出包页面
		displayApkDirlist(request, response);
	}
	
	// 删除所有渠道包
	private void deleteAllPackage(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String delDir = request.getParameter("pkg_dir");
		System.out.println("delDir:" + delDir);
		String cmd1 = "cmd /c rd/s/q " + PATH_PACKAGE + File.separator + delDir;
		String cmd2 = "cmd /c del /f " + PATH_TOOL + File.separator + delDir + ".7z";
		String cmd = cmd1 + " && " + cmd2;
		System.out.println("删除所有渠道包cmd:" + cmd);
		Process exec = Runtime.getRuntime().exec(cmd);
		try {
			exec.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		displayApkDirlist(request, response);
	}
	
	// 下载所有渠道包
	private void downloadAllPackage(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String pkgDir = request.getParameter("pkg_dir");
		String zipPath = PATH_TOOL + File.separator + "7-Zip" + File.separator + "7z"; // 压缩工具路径
		String zipFile = PATH_TOOL + File.separator + pkgDir + ".7z "; // 压缩后的文件，存入临时文件目录
		String zipDir = PATH_PACKAGE + File.separator + pkgDir + File.separator + "*.*"; // 要压缩的目录
		try {
			// 1.先将要下载的批量包打包压缩，压缩命令：start E:\7z a E:\aaa.7z E:\aaa
			File temp = new File(zipFile);
			if (!temp.exists()) {
				String cmd = "cmd /c " + zipPath + " a " + zipFile + zipDir;
				System.out.println("压缩命令：" + cmd);
				Process exec = Runtime.getRuntime().exec(cmd);
				exec.waitFor();
			}
			// 2.然后下载压缩包
			OutputStream out = response.getOutputStream();
			File file = new File(zipFile);
			if (!file.exists()) {
				System.out.println("文件不存在！");
			} else {
				InputStream is = new FileInputStream(file);
				byte[] bs = new byte[1024];
				int b = 0;
				response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(pkgDir + ".7z", "UTF-8"));
				while ((b = is.read(bs)) != -1) {
					out.write(bs, 0, b);
				}
				out.close();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		request.getRequestDispatcher("/pkglist.jsp").forward(request, response);
	}
	
	// 显示首页
	private void showIndex(HttpServletRequest request, HttpServletResponse response) {
		File file = new File(PATH_APK);
		Map<String, String> map = new LinkedHashMap<String, String>();
		File[] fs = file.listFiles();
		if (fs != null) {
			for (File f : fs) {
				String name = f.getName();
				String uuid = UUID.randomUUID().toString();
				map.put(uuid, name);
			}
		}
		System.out.println("map:" + map);
		File file2 = new File(PATH_KEYSTORE);
		Map<String, String> map2 = new LinkedHashMap<String, String>();
		File[] fs2 = file2.listFiles();
		if (fs2 != null) {
			for (File f : fs2) {
				String name = f.getName();
				String uuid = UUID.randomUUID().toString();
				map2.put(uuid, name);
			}
		}
		request.getSession().setAttribute("apklist", map);
		request.getSession().setAttribute("keystonelist", map2);
		try {
			request.getRequestDispatcher("/index.jsp").forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 显示已完成的包目录列表
	private void displayApkDirlist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		File file = new File(PATH_PACKAGE);
		List<String> pkgDirList = new ArrayList<String>();
		File[] fs = file.listFiles();
		if (fs != null) {
			for (File f : fs) { 
				pkgDirList.add(f.getName());
			}
		}
		System.out.println("打包目录列表：" + pkgDirList);
		request.getSession().setAttribute("pkg_dir_list", pkgDirList);
		request.getRequestDispatcher("/pkgDirList.jsp").forward(request, response);
	}
	
	// 显示已完成的包目录列表
	private void displayApklist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String dirName = request.getParameter("dir_name");
		request.getSession().setAttribute("current_pkg_dir", dirName);
		System.out.println("dirName:" + dirName);
		File file = new File(PATH_PACKAGE + File.separator + dirName);
		List<String> pkgList = new ArrayList<String>();
		File[] fs = file.listFiles();
		if (fs != null) {
			for (File f : fs) {
				pkgList.add(f.getName());
			}
		}
		System.out.println("打包列表：" + pkgList);
		request.getSession().setAttribute("pkg_list", pkgList);
		request.getRequestDispatcher("/pkgList.jsp").forward(request, response);
	}
	
	//开始批量打包
	private void batchPacking() {
		tempPath = PATH_ROOT+File.separator+DateUtil.getCurrentTime();
		File file = new File(tempPath);
		System.out.println("tempPath:" + tempPath);
		if (!file.exists()) {
			System.out.println("mkdirs");
			file.mkdirs();
		}
		List<String> channelIds = new ArrayList<String>();
		List<String> channelNames = new ArrayList<String>();
		for (int i=0;i<channelLists.size();i++){
			Channel channel = channelLists.get(i);
			channelIds.add(channel.getChannelId());
			channelNames.add(channel.getChannelName());
		}
		// 解包
		String ApktoolsPath = PATH_TOOL + File.separator + "apktool.jar";
		CommandUtil.exceCmd(PATH_TOOL,PATH_APK+File.separator+mainApkName, ApktoolsPath,tempPath,mainApkName);
		
		for (int i=0;i<channelLists.size();i++){
			String channelId = channelIds.get(i);
			String channelName = channelNames.get(i);
			Map<String,String> map = new HashMap<String, String>();
			map.put("channel_name",channelName);
			map.put("channel_id",channelId);
			WriteUtil.writeXml(map.get("channel_id"),tempPath+File.separator+mainApkName.substring(0,mainApkName.lastIndexOf(".")),packName);
			try {
				buildPackage();
				signature(keystoreInfo,mainApkName);
				zipApk(map, mainApkName, packagePath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		try {
			Thread.sleep(1000);
			String cmd = "cmd /c del " + packagePath  + File.separator + mainApkName;
			String cmd_temp = "cmd /c rd/s/q " + tempPath;
			System.out.println("删除命令" + cmd);
			Process exec = Runtime.getRuntime().exec(cmd);
			exec.waitFor();
			Runtime.getRuntime().exec(cmd_temp).waitFor();
			deleteUnApkFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 读取表单数据
	private boolean getFormData(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();

		// 获取主包信息
		String apk = request.getParameter("apk");
		if (apk != null) {
			Map<String, String> map = (Map<String, String>) session.getAttribute("apklist");
			mainApkName = map.get(apk);
			System.out.println("主包为：" + mainApkName);
		} else {
			displayErrorTip(request, response, "没有选择主包!");
			return false;
		}
		
		// 获取签名信息
		String keystore = request.getParameter("keystore");
		if (keystore != null) {
			Map<String, String> map = (Map<String, String>) session.getAttribute("keystonelist");
			String keystoreName = map.get(keystore);
			System.out.println("keystoreName:" + keystoreName);
			if (ExcelReaderUtil.searchKeystone(PATH_KEYSTONE_INFO, keystoreName) != null) {
				keystoreInfo = ExcelReaderUtil.searchKeystone(PATH_KEYSTONE_INFO, keystoreName);
			}
			System.out.println("签名为：" + keystoreInfo);
		} else {
			displayErrorTip(request, response, "没有选择签名!");
			return false;
		}

		// 获取包名
		packName = request.getParameterValues("pack_name")[0].trim();
		System.out.println("包名：" + packName);
		
		// 获取渠道信息
		List<String> channels = (List<String>)session.getAttribute("select_channel");
		if (channelLists == null) channelLists = new ArrayList<Channel>();
		if (channels == null) {
			displayErrorTip(request, response, "没有选择渠道!");
			return false;
		} else {
			System.out.println("channels:" + channels);
			for (String string : channels) {
				Channel channel = new Channel();
				channel.setChannelName(string.split(":")[0]);
				channel.setChannelId(string.split(":")[1]);
				channelLists.add(channel);
			}
		}
		System.out.println("已选渠道信息：" + channelLists);

		return true;
	}
	
	// 显示错误提示
	private void displayErrorTip(HttpServletRequest request, HttpServletResponse response, String error) throws IOException {
		response.setHeader("refresh", "3;url=" + request.getContextPath() + "/index.jsp");
		response.getWriter().write(error + "3秒后返回主页...");
	}
	
	// 合包
	public void buildPackage() throws Exception {
		String cmd1 = "cmd /c " + PATH_TOOL.substring(0, 2);
		String cmd2 = "cd " + PATH_TOOL;
		String cmd3 = "java -jar " + apkToolPath + " b " + tempPath + File.separator + mainApkName.substring(0, mainApkName.lastIndexOf("."));
		String cmd4 = "move " + tempPath + File.separator + mainApkName.substring(0, mainApkName.lastIndexOf(".")) + File.separator + "dist" + File.separator + "*.*" + " " + packagePath;
		String cmd = cmd1 + " && " + cmd2 + " && " + cmd3 + " && " + cmd4;
		Process exec = Runtime.getRuntime().exec(cmd);
		System.out.println("合包命令:"+cmd);
		exec.waitFor();
	}
	
	// 签名
	public void signature(Map<String, String> keystone, String apkName) throws Exception {
		System.out.println("map------------"+keystone);
		String cmd1 = "cmd /c " + PATH_TOOL.substring(0, 2);
		System.out.println("keystone信息：" + keystone);
		String keystore = keystone.get("name");
		String alias = keystone.get("alias");
		String password = keystone.get("password");
		String signPath = PATH_KEYSTORE + File.separator + keystore;
		String path = packagePath + File.separator + apkName;
		String cmd2 = "cd " + PATH_TOOL;
		String cmd3 = "jarsigner -digestalg SHA1 -sigalg MD5withRSA -keystore " + signPath + 
				" -storepass " + password + " -keypass " + password + " " + path + " " + alias;
		String cmd = cmd1 + " && " + cmd2 + " && " + cmd3;
		System.out.println("签名命令：" + cmd);
		Process exec = Runtime.getRuntime().exec(cmd);
		exec.waitFor();
	}
	
	// 优化包
	public void zipApk(Map<String, String> map, String apkName, String fromPath)throws Exception {
		String cmd1 = "cmd /c " + PATH_TOOL.substring(0, 2);
		String channelID = map.get("channel_id");
		String channelName = map.get("channel_name");
		// 来源路径
		String srcPath = fromPath + File.separator + apkName;
		// 目标路径
		String desPath = fromPath + File.separator
				+ mainApkName.substring(0, mainApkName.lastIndexOf(".")) + "_"
				+ channelID + "_" + channelName + ".apk";
		// String cmd2 = "move " + PATH_PACKAGE + File.separator + apkName + " "
		// + pkgDatePath;
		String cmd3 = PATH_TOOL + File.separator + "zipalign -f -v 4 "
				+ srcPath + " " + desPath;
		String cmd = cmd1 + " && " + cmd3;
		System.out.println("优化包命令：" + cmd);
		Runtime.getRuntime().exec(cmd);
	}
	
	
	// 文件上传
	private void uploadFile(String type, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String flag = "上传成功！";
		try {
			File file = new File("upload_package".equals(type) ? PATH_APK : PATH_KEYSTORE);
			if (!file.exists()) file.mkdirs();
			DiskFileItemFactory dif = new DiskFileItemFactory(); // 使用默认的临时目录
			ServletFileUpload sfu = new ServletFileUpload(dif);
			sfu.setHeaderEncoding("utf-8");
			@SuppressWarnings("unchecked")
			List<FileItem> items = sfu.parseRequest(request);
			for (FileItem item : items) {
				if (item.isFormField()) { // 说明是普通字段
					String name = item.getFieldName(); // 拿到文本框的名字
					String value = item.getString("UTF-8"); // 拿到文本框中的内容
					System.out.println(name + "=" + value);
				} else { // 说明是上传字段
					String fileName = item.getName(); // 拿到文件名 ：不包括路径
					System.out.println("上传文件名；" + fileName);
					if ("upload_package".equals(type)) {						
						mainApkName = fileName;
					} else {
//						keystoreName = fileName;
					}
					File f = new File(("upload_package".equals(type) ? PATH_APK : PATH_KEYSTORE) + File.separator + fileName);
					item.write(f);
				}
			}
		} catch (Exception e) {
			flag = "上传失败！";
			e.printStackTrace();
		} finally {
			request.setAttribute("flag", flag);
			showIndex(request, response);
		}
	}
	
	// 删除解包临时文件
	public void deleteUnApkFile() throws Exception {
		String cmd = "cmd /c rd/s/q " + PATH_TOOL+File.separator+mainApkName.substring(0,mainApkName.lastIndexOf("."));
		System.out.println("删除命令" + cmd);
		Process exec = Runtime.getRuntime().exec(cmd);
		exec.waitFor();
	}
	
}
