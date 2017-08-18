package com.tianyou.channel.utils;

import java.security.MessageDigest;

import org.json.JSONException;
import org.json.JSONObject;

import com.tianyou.channel.utils.HttpUtils.HttpCallback;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.telephony.TelephonyManager;

public class AppUtils {

//	 获取设备IMEI号的方法
    public static String getPhoeIMEI(Context context) {
    	if (Build.VERSION.SDK_INT >= 23) {
    		return SPHandler.getIMEI(context, SPHandler.SP_IMEI);
    	} else {
    		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    		String imei = telephonyManager.getDeviceId();
    		if (imei == null || imei.isEmpty()) imei = SPHandler.getIMEI(context, SPHandler.SP_IMEI);
    		return imei;
    	}
    }
    
    //MD5加密
    public  static String MD5(String pwd) {
        //用于加密的字符
        char md5String[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            //使用平台的默认字符集将此 String 编码为 byte序列，并将结果存储到一个新的 byte数组中
            byte[] btInput = pwd.getBytes();

            //信息摘要是安全的单向哈希函数，它接收任意大小的数据，并输出固定长度的哈希值。
            MessageDigest mdInst = MessageDigest.getInstance("MD5");

            //MessageDigest对象通过使用 update方法处理数据， 使用指定的byte数组更新摘要
            mdInst.update(btInput);

            // 摘要更新之后，通过调用digest（）执行哈希计算，获得密文
            byte[] md = mdInst.digest();

            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {   //  i = 0
                byte byte0 = md[i];  //95
                str[k++] = md5String[byte0 >>> 4 & 0xf];    //    5
                str[k++] = md5String[byte0 & 0xf];   //   F
            }

            //返回经过加密后的字符串
            return new String(str);

        } catch (Exception e) {
            return null;
        }
    }
    
    /**
	 * 获取Manifest中meta-data的值
	 * @param context
	 * @param name
	 * @return
	 */
	public static String getMetaDataValue(Context context, String name) {
		ApplicationInfo appInfo = null;
		try {
			appInfo = context.getPackageManager().getApplicationInfo(
					context.getPackageName(), PackageManager.GET_META_DATA);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		String msg = appInfo.metaData.getString(name);
		return msg;
	}
    
	/**
	 * 获取系统时间戳(10位)
	 */
	public static String getSystemTime(){
		return System.currentTimeMillis()/1000+"";
	}
	
    public static String time;
    public static String getCurrentTime (Activity activity,final boolean isTimeStr){
    	
    	HttpUtils.post(activity, URLHolder.GET_TIME, null, new HttpCallback() {
			@Override
			public void onSuccess(String data) {
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(data);
					JSONObject result = jsonObject.getJSONObject("result");
					JSONObject channelInfo = result.getJSONObject("channelinfo");
					if (isTimeStr) {
						time = channelInfo.getString("timestr");
					} else {
						time = channelInfo.getString("datastr");
					}
				} catch (JSONException e) {
					e.printStackTrace();
					time = null;
				}
			}
			
			@Override
			public void onFailed(String code) {
				time = null;
			}
		});
    	
    	return time;
    }
}
