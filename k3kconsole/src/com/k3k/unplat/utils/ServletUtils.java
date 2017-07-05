/**
 * Copyright (c) 2005-2011 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * 
 * $Id: ServletUtils.java 1595 2011-05-11 16:41:16Z calvinxiu $
 */
package com.k3k.unplat.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;


/**
 * Http与Servlet工具类.
 * 
 * @author calvin
 */
@SuppressWarnings("unchecked")
public abstract class ServletUtils {

	//-- Content Type 定义 --//
	public static final String EXCEL_TYPE = "application/vnd.ms-excel";
	public static final String HTML_TYPE = "text/html";
	public static final String JS_TYPE = "text/javascript";
	public static final String JSON_TYPE = "application/json";
	public static final String XML_TYPE = "text/xml";
	public static final String TEXT_TYPE = "text/plain";

	//-- Header 定义 --//
	public static final String AUTHENTICATION_HEADER = "Authorization";

	//-- 常用数值定义 --//
	public static final long ONE_YEAR_SECONDS = 60 * 60 * 24 * 365;

	/**
	 * 设置客户端缓存过期时间 的Header.
	 */
	public static void setExpiresHeader(HttpServletResponse response, long expiresSeconds) {
		//Http 1.0 header
		response.setDateHeader("Expires", System.currentTimeMillis() + expiresSeconds * 1000);
		//Http 1.1 header
		response.setHeader("Cache-Control", "private, max-age=" + expiresSeconds);
	}

	/**
	 * 设置禁止客户端缓存的Header.
	 */
	public static void setDisableCacheHeader(HttpServletResponse response) {
		//Http 1.0 header
		response.setDateHeader("Expires", 1L);
		response.addHeader("Pragma", "no-cache");
		//Http 1.1 header
		response.setHeader("Cache-Control", "no-cache, no-store, max-age=0");
	}

	/**
	 * 设置LastModified Header.
	 */
	public static void setLastModifiedHeader(HttpServletResponse response, long lastModifiedDate) {
		response.setDateHeader("Last-Modified", lastModifiedDate);
	}

	/**
	 * 设置Etag Header.
	 */
	public static void setEtag(HttpServletResponse response, String etag) {
		response.setHeader("ETag", etag);
	}

	/**
	 * 根据浏览器If-Modified-Since Header, 计算文件是否已被修改.
	 * 
	 * 如果无修改, checkIfModify返回false ,设置304 not modify status.
	 * 
	 * @param lastModified 内容的最后修改时间.
	 */
	public static boolean checkIfModifiedSince(HttpServletRequest request, HttpServletResponse response,
			long lastModified) {
		long ifModifiedSince = request.getDateHeader("If-Modified-Since");
		if ((ifModifiedSince != -1) && (lastModified < ifModifiedSince + 1000)) {
			response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
			return false;
		}
		return true;
	}

	/**
	 * 根据浏览器 If-None-Match Header, 计算Etag是否已无效.
	 * 
	 * 如果Etag有效, checkIfNoneMatch返回false, 设置304 not modify status.
	 * 
	 * @param etag 内容的ETag.
	 */
	public static boolean checkIfNoneMatchEtag(HttpServletRequest request, HttpServletResponse response, String etag) {
		String headerValue = request.getHeader("If-None-Match");
		if (headerValue != null) {
			boolean conditionSatisfied = false;
			if (!"*".equals(headerValue)) {
				StringTokenizer commaTokenizer = new StringTokenizer(headerValue, ",");

				while (!conditionSatisfied && commaTokenizer.hasMoreTokens()) {
					String currentToken = commaTokenizer.nextToken();
					if (currentToken.trim().equals(etag)) {
						conditionSatisfied = true;
					}
				}
			} else {
				conditionSatisfied = true;
			}

			if (conditionSatisfied) {
				response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
				response.setHeader("ETag", etag);
				return false;
			}
		}
		return true;
	}

	/**
	 * 设置让浏览器弹出下载对话框的Header.
	 * 
	 * @param fileName 下载后的文件名.
	 */
	public static void setFileDownloadHeader(HttpServletResponse response, String fileName) {
		try {
			//中文文件名支持
			String encodedfileName = new String(fileName.getBytes(), "ISO8859-1");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedfileName + "\"");
		} catch (UnsupportedEncodingException e) {
		}
	}

	/**
	 * 取得带相同前缀的Request Parameters.
	 * 
	 * 返回的结果的Parameter名已去除前缀.
	 */
	public static Map<String, Object> getParametersStartingWith(ServletRequest request, String prefix) {
		AssertUtils.notNull(request, "Request must not be null");
		Enumeration paramNames = request.getParameterNames();
		Map<String, Object> params = new TreeMap<String, Object>();
		if (prefix == null) {
			prefix = "";
		}
		while (paramNames != null && paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();
			if ("".equals(prefix) || paramName.startsWith(prefix)) {
				String unprefixed = paramName.substring(prefix.length());
				String[] values = request.getParameterValues(paramName);
				if (values == null || values.length == 0) {
					// Do nothing, no values found at all.
				} else if (values.length > 1) {
					params.put(unprefixed, values);
				} else {
					params.put(unprefixed, values[0]);
				}
			}
		}
		return params;
	}

	/**
	 * 客户端对Http Basic验证的 Header进行编码.
	 */
	public static String encodeHttpBasic(String userName, String password) {
		String encode = userName + ":" + password;
		return "Basic " + EncodeUtils.encodeBase64(encode.getBytes());
	}

	/**
	 * 向客户端输出json
	 * @param response
	 * @param object
	 */
	public static void responseJson(HttpServletResponse response, Object object) {
		JsonMapper jsonMapper = JsonMapper.buildNormalMapper();
		String jsonStr = jsonMapper.toJson(object);
		try {
			response.setContentType("text/plain");
			response.setCharacterEncoding("UTF-8");
//			System.out.println(jsonStr);
			response.getWriter().write(jsonStr);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
		}
	}
	
	public static void responseJsonForCollection(HttpServletResponse response, Collection<?> col){
		try {
			response.setContentType("text/plain");
			response.setCharacterEncoding("UTF-8");
			JSONArray jsonArr = new JSONArray();
			for(Object obj : col){
				JSONObject jsonObj = new JSONObject();
				Field[] fields = obj.getClass().getDeclaredFields();
				for(Field field : fields){
					field.setAccessible(true);
//					System.out.println(field.getType().toString() + " " + field.getName());
					if(field.getType().toString().endsWith("String") || field.getType().toString().endsWith("int")
							|| field.getType().toString().endsWith("BigDecimal")){
						jsonObj.put(field.getName(), field.get(obj));
					}
				}
				jsonArr.add(jsonObj);				
			}
//			System.out.println(jsonArr.toString());
			response.getWriter().write(jsonArr.toString());
			response.getWriter().flush();
			response.getWriter().close();
		} catch (Exception e) {
		}
	}

	/**
	 * 向客户端输出普通文本
	 * @param response
	 * @param object
	 */
	public static void responseText(HttpServletResponse response, String text) {
		try {
			response.getWriter().write(text);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
		}
	}

	/**
	 * 向客户端输出xml格式文本
	 * @param response
	 * @param object
	 */
	public static void responseXml(HttpServletResponse response, String xml) {
		try {
			response.setContentType("text/xml");
			response.getWriter().write(xml);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
		}
	}

	public static String getStringParameter(HttpServletRequest request, String name) {
		return request.getParameter(name);
	}

	public static Integer getIntegerParameter(HttpServletRequest request, String name) {
		String value = getStringParameter(request, name);
		if (StringUtils.isNumeric(value))
			return new Integer(value);
		else
			return null;
	}

	public static Boolean getBooleanParameter(HttpServletRequest request, String name) {
		String value = getStringParameter(request, name);

		if (StringUtils.isNotBlank(value))
			return Boolean.parseBoolean(value);
		else
			return Boolean.FALSE;
	}

	public static Date getDateTimeParameter(HttpServletRequest request, String name) {
		String value = getStringParameter(request, name);
		try {
			if (StringUtils.isNotBlank(value))
				return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date getDateParameter(HttpServletRequest request, String name) {
		String value = getStringParameter(request, name);
		try {
			if (StringUtils.isNotBlank(value))
				return new SimpleDateFormat("yyyy-MM-dd").parse(value);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 取得Request Parameter的简化方法.
	 */
	public static List<String> getListParameter(HttpServletRequest request, String name) {
		String[] values = request.getParameterValues(name);
		return values == null ? Collections.EMPTY_LIST : Arrays.asList(values);
	}
}
