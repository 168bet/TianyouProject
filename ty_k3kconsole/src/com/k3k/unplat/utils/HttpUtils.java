package com.k3k.unplat.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.lang.StringUtils;

public class HttpUtils {

	/**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param, String cookies) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = StringUtils.isNotBlank(param)? (url + "?" + param) : url;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(50000);
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            //设置cookie
            if(StringUtils.isNotBlank(cookies)){
            	connection.setRequestProperty("Cookie", cookies);
            }
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
//            Map<String, List<String>> map = connection.getHeaderFields();
//            // 遍历所有的响应头字段
//            for (String key : map.keySet()) {
//                System.out.println(key + "--->" + map.get(key));
//            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
//            System.out.println("发送GET请求出现异常！" + e);
        	System.out.println(e.getMessage());
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
               
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    } 
    
    public static String sendPostHTTPS(String requestURL, String param, String cookies){
		InputStream is = null;
		BufferedReader rd = null;
		try {
			URL url = new URL(requestURL);
			
			HttpsURLConnection httpsUrlConnection = (HttpsURLConnection) url.openConnection();
			// 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在
			// http正文内，因此需要设为true, 默认情况下是false;
			httpsUrlConnection.setDoOutput(true);
			httpsUrlConnection.setAllowUserInteraction(true);
			// POST请求不能使用缓存
			httpsUrlConnection.setUseCaches(false);
			// 超时设置
			httpsUrlConnection.setConnectTimeout(30000);
			httpsUrlConnection.setReadTimeout(50000);
			// 设定请求的方法为"POST"，默认是GET
			httpsUrlConnection.setRequestMethod("POST");
			
			//设置cookies
			if(StringUtils.isNotBlank(cookies)){
				httpsUrlConnection.addRequestProperty("Cookie", cookies);
			}
			
			PrintStream ps = new PrintStream(httpsUrlConnection.getOutputStream());  
			ps.print(param);  
			ps.close();  
			
			is = httpsUrlConnection.getInputStream();// <==注意，实际发送请求的代码段就在这里
			
			rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			return jsonText;
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				if(is!=null){
					is.close();
				}
				if(rd!=null){
					rd.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
    public static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}
    
    /**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String getLogFile(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = StringUtils.isNotBlank(param)? (url + "?" + param) : url;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(50000);
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            //设置cookie
//            if(StringUtils.isNotBlank(cookies)){
//            	connection.setRequestProperty("Cookie", cookies);
//            }
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
//            Map<String, List<String>> map = connection.getHeaderFields();
//            // 遍历所有的响应头字段
//            for (String key : map.keySet()) {
//                System.out.println(key + "--->" + map.get(key));
//            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(new GZIPInputStream(connection.getInputStream()), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
//            System.out.println("发送GET请求出现异常！" + e);
        	System.out.println(e.getMessage());
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
               
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    } 
    
    /**
     * 获取指定 url 的content length
     */
    public static long getFileLength(String url){
    	try {
    		URL realUrl = new URL(url);
    		// 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(50000);
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            return connection.getContentLength();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
    }
}
