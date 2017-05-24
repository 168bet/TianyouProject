package com.tianyou.sdk.holder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;

/**
 * 读写用户登录信息
 * @author itstrong
 *
 */
public class LoginInfoHandler {
	
	public static final String USER_ACCOUNT = "user_account";
	public static final String USER_NICKNAME = "user_nickname";
	public static final String USER_PASSWORD = "user_password";
	public static final String USER_SERVER = "user_server";
	public static final String USER_LOGIN_WAY = "user_login_way";
	
	public static final String LOGIN_INFO_ACCOUNT = "user_login_info";
	public static final String LOGIN_INFO_PHONE = "phone_login_info";
	public static final String LOGIN_INFO_QQ = "qq_login_info";
	
	// 读取登录信息
	public static List<Map<String, String>> getLoginInfo(String loginType) {
		File file = new File(ConstantHolder.PATH_CONFIG, loginType);
		if (!file.exists()) return new ArrayList<Map<String,String>>();
		List<Map<String, String>> infos = new ArrayList<Map<String, String>>();
		try {
			FileInputStream input=  new FileInputStream(file);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
		    DocumentBuilder builder = factory.newDocumentBuilder();
		    Document document = builder.parse(input);
		    Element root = document.getDocumentElement();
		    NodeList infosNode = root.getChildNodes();
		    for (int i = 0; i < infosNode.getLength(); i++) {
		    	Node node1 = infosNode.item(i);
		    	Map<String, String> map = new HashMap<String, String>();
		    	NodeList infoNode = node1.getChildNodes();
		    	for (int j = 0; j < infoNode.getLength(); j++) {
		    		Node node2 = infoNode.item(j);
		    		map.put(node2.getNodeName(), node2.getTextContent());
				}
		    	infos.add(map);
			}
		    Collections.reverse(infos);
		    return infos;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<Map<String,String>>();
	}
	
	// 保存用户登录信息
	public static void putLoginInfo(String loginType, Map<String, String> info) {
		List<Map<String, String>> infos = getLoginInfo(loginType);
		if (infos == null) {
			infos = new ArrayList<Map<String, String>>();
		} else {
			for (int i = 0; i < infos.size(); i++) {
				if (info.get(USER_ACCOUNT).equals(infos.get(i).get(USER_ACCOUNT))) {
					infos.remove(i);
					break;
				}
			}
		}
		infos.add(info);
		saveLoginInfo(loginType, infos);
	}
	
	// 保存用户登录信息
	public static void deleteLoginInfo(String loginType, String username) {
		List<Map<String, String>> infos = getLoginInfo(loginType);
		for (int i = 0; i < infos.size(); i++) {
			if (username.equals(infos.get(i).get(USER_ACCOUNT))) {
				infos.remove(i);
				break;
			}
		}
	}
	
	// 写入用户登录信息
	public static void saveLoginInfo(String loginType, List<Map<String, String>> infos) {
		try {
			XmlSerializer serializer = Xml.newSerializer();
			StringWriter writer = new StringWriter();
			serializer.setOutput(writer);
			serializer.startDocument("utf-8", true);
			serializer.startTag("", "infos");
			for (Map<String, String> info : infos) {
				serializer.startTag("", "info");

				serializer.startTag("", USER_ACCOUNT);
				serializer.text(info.get(USER_ACCOUNT));
				serializer.endTag("", USER_ACCOUNT);

				serializer.startTag("", USER_NICKNAME);
				serializer.text(info.get(USER_NICKNAME));
				serializer.endTag("", USER_NICKNAME);
				
				serializer.startTag("", USER_PASSWORD);
				serializer.text(info.get(USER_PASSWORD));
				serializer.endTag("", USER_PASSWORD);
				
				serializer.startTag("", USER_SERVER);
				serializer.text(info.get(USER_SERVER));
				serializer.endTag("", USER_SERVER);
				
				serializer.startTag("", USER_LOGIN_WAY);
				serializer.text(info.get(USER_LOGIN_WAY));
				serializer.endTag("", USER_LOGIN_WAY);

				serializer.endTag("", "info");
			}
			serializer.endTag("", "infos");
			serializer.endDocument();
			String xmlString = writer.toString();

			File dir = new File(ConstantHolder.PATH_CONFIG);
			if (!dir.exists() && !dir.isDirectory()) dir.mkdirs();
			File file = new File(ConstantHolder.PATH_CONFIG, loginType);
			if (!file.exists()) file.createNewFile();
			FileOutputStream output = new FileOutputStream(file);
			output.write(xmlString.getBytes());
			output.close();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
