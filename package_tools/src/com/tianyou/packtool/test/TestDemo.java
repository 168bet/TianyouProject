package com.tianyou.packtool.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.tianyou.packtool.util.CommandUtil;

public class TestDemo {
	
	private static String unApkPath = "D:\\work_space\\SVN\\TianYouPackTools\\WebRoot\\WEB-INF\\channel\\tools\\TianGameDemo";
	public static void main(String[] args) {
		String cmd1 = "cmd /c "+"D:\\work_space\\SVN\\TianYouPackTools\\WebRoot\\WEB-INF\\channel\\tools".substring(0, 2);
		String cmd2 = "cd "+"D:\\work_space\\SVN\\TianYouPackTools\\WebRoot\\WEB-INF\\channel\\tools";
		String cmd3 = "apktool d "+"D:\\work_space\\SVN\\TianYouPackTools\\WebRoot\\WEB-INF\\apk\\TianGameDemo.apk";
		String cmd = cmd1+"&&"+cmd2+"&&"+cmd3;
		
//		CommandUtil.exceCmd("D:\\install_package\\apache-tomcat-7.0.63-windows-x64\\apache-tomcat-7.0.63\\webapps\\packing\\WEB-INF\\tools"
//				,"D:\\install_package\\apache-tomcat-7.0.63-windows-x64\\apache-tomcat-7.0.63\\webapps\\packing\\WEB-INF\\apk\\TianGameDemo.apk", cmd3);
//		try {
//			String path = unApkPath+File.separator+ "assets" + File.separator + "TianGame.xml";
//			SAXReader reader = new SAXReader();
//			Document document = reader.read(new File(path));
//			Element element = document.getRootElement();
//			Element channelEle = element.element("channel");
//			channelEle.setText("1,2");
//			write(document, path);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	private static void write(Document document, String path) throws Exception {
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8");
		XMLWriter writer = new XMLWriter(new OutputStreamWriter(
				new FileOutputStream(new File(path)), "UTF-8"), format);
		writer.write(document);
		writer.flush();
		writer.close();
	}

}
