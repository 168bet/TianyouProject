package com.k3k.unplat.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class UnionUtils {
	
	private static final String IP_TAOBAO = "http://ip.taobao.com/service/getIpInfo.php";
	
	public static String getValue(String file, String key){
		Properties p = loadProperties(file);
		return p.getProperty(key);
	}
	
	private static Properties loadProperties(String file){
		InputStream is = null;
		Properties p = null;
		try {
			is = Thread.currentThread().getContextClassLoader().getResource(file).openStream();
			p = new Properties();
			p.load(is);
			is.close();
		} catch (IOException e) {
		}
		return p;
	}
	
	/**
     * 根据ip获取运营商
     */
    @SuppressWarnings("static-access")
	public static String getIsp(String ip){
    	if(StringUtils.isBlank(ip))
    		return null;
    	String result = null;
    	for(int i=0;i<3;i++){
    		try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    		result = sendGet(IP_TAOBAO, "ip=" + ip);
    		if(StringUtils.isNotBlank(result))
    			break;
    	}
    	JSONObject json = JSONObject.fromObject(result);
    	
    	if(json.getInt("code") == 0){
    		JSONObject data = JSONObject.fromObject(json.getString("data"));
    		return data.getString("isp");
    	}else{
    		return null;
    	}
    		
    }
	
	public static String getRemoteHost(HttpServletRequest request){
	    String ip = request.getHeader("x-forwarded-for");
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getHeader("Proxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getHeader("WL-Proxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getRemoteAddr();
	    }
	    return ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
	}


	
	public static String getScreen(String screenWidth, String screenHeight, String devicePixel, boolean isMobile){
    	String screen = "";
		if(StringUtils.isBlank(devicePixel) || "undefined".equals(devicePixel) || !isMobile){
			screen = screenWidth + "*" + screenHeight;
		}else{
			try {
				int width = Integer.parseInt(screenWidth);
				int height = Integer.parseInt(screenHeight);
				float pix = Float.parseFloat(devicePixel);
				screen = ((int)(width*pix)) + "*" + ((int)(height*pix));
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return screen;
    }
	
	public static List<Date> findDates(Calendar begin, Calendar end) {
		List<Date> dates = new ArrayList<Date>();
		// 测试此日期是否在指定日期之后
		while (end.after(begin)) {
			// 根据日历的规则，为给定的日历字段添加或减去指定的时间量
			begin.add(Calendar.DAY_OF_MONTH, 1);
			dates.add(begin.getTime());
		}
		return dates;
	}
	
	public static JSONArray buildJSON(Collection<?> col){
		JSONArray jsonArr = new JSONArray();
		try {
			for(Object obj : col){
				JSONObject jsonObj = new JSONObject();
				Field[] fields = obj.getClass().getDeclaredFields();
				for(Field field : fields){
					field.setAccessible(true);
//				System.out.println(field.getType().toString() + " " + field.getName());
					if(field.getType().toString().endsWith("String") || field.getType().toString().endsWith("int")){
						jsonObj.put(field.getName(), field.get(obj));
					}
				}
				jsonArr.add(jsonObj);				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return jsonArr;
	}
		
	public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), Charset.forName("GBK")));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
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
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置超时
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
//            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
//            for (String key : map.keySet()) {
//                System.out.println(key + "--->" + map.get(key));
//            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("GBK")));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
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
    
    
	public static void main(String[] args) {
		String ip = "223.155.209.65";
		String country = UnionUtils.sendGet("http://int.dpool.sina.com.cn/iplookup/iplookup.php?", "format=js&ip="+ip);
		System.out.println(country);
		String[] strArr = country.split("	");
		//for(String s : strArr){
			System.out.println(strArr[5]);
		//}
	}
    
}
