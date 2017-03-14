package com.tianyou.packtool.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class WriteUtil {

	public static void writeXml(String channel,String unApkPath,String packName){
		try {
			
			// 修改渠道号
			String path = unApkPath+File.separator+ "assets" + File.separator + "TianGame.xml";
			SAXReader reader = new SAXReader();  
			System.out.println("path:" + path);
			Document document = reader.read(new File(path));
			Element element = document.getRootElement();
			Element channelEle = element.element("channel");
			channelEle.setText(channel);
			write(document, path);
			// 修改包名
			if (!("".equals(packName))){
				String path_packName = unApkPath+File.separator+"AndroidManifest.xml";
				SAXReader reader2 = new SAXReader();
				Document document_packName = reader2.read(new File(path_packName));
				Element element_packName = document_packName.getRootElement();
				Attribute attribute = element_packName.attribute("package");
				attribute.setText(packName);
				write(document_packName, path_packName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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
