package com.tianyou.sdk.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.tianyou.sdk.activity.MenuActivity;
import com.tianyou.sdk.activity.PayActivity;
import com.tianyou.sdk.activity.WebViewAvtivity;
import com.tianyou.sdk.holder.ConfigHolder;
import com.tianyou.sdk.holder.SPHandler;
import com.tianyou.sdk.holder.URLHolder;

public class AppUtils {

	/**
	 * 获取电话号码
	 * @param context
	 * @return
	 */
	public static String getPhoneNumber(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String number = telephonyManager.getLine1Number();
		if (number == null) {
			return "";
		} else {
			return number.contains("+86") ? number.substring(3) : number;
		}
	}

	/**
	 * 验证手机格式
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		String telRegex = "[1][358]\\d{9}";
		if (TextUtils.isEmpty(mobiles))
			return false;
		else
			return mobiles.matches(telRegex);
	}
	
	/**
	 * MD5加密
	 * @param pwd
	 * @return
	 */
	public static String MD5(String pwd) {
		// 用于加密的字符
		char md5String[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			// 使用平台的默认字符集将此 String 编码为 byte序列，并将结果存储到一个新的 byte数组中
			byte[] btInput = pwd.getBytes();
			// 信息摘要是安全的单向哈希函数，它接收任意大小的数据，并输出固定长度的哈希值。
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// MessageDigest对象通过使用 update方法处理数据， 使用指定的byte数组更新摘要
			mdInst.update(btInput);
			// 摘要更新之后，通过调用digest（）执行哈希计算，获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) { // i = 0
				byte byte0 = md[i]; // 95
				str[k++] = md5String[byte0 >>> 4 & 0xf]; // 5
				str[k++] = md5String[byte0 & 0xf]; // F
			}
			// 返回经过加密后的字符串
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 获取设备IMEI号的方法
	 * @param context
	 * @return
	 */
    public static String getPhoeIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        if (imei == null) imei = SPHandler.getString(context, SPHandler.SP_IMEI);
        return imei;
    }
    
    /**
     * 获取设备IP的方法
     * @return
     */
    public static String getIP() {
        String IP = null;
        StringBuilder IPStringBuilder = new StringBuilder();
        try {
            Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaceEnumeration.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaceEnumeration.nextElement();
                Enumeration<InetAddress> inetAddressEnumeration = networkInterface.getInetAddresses();
                while (inetAddressEnumeration.hasMoreElements()) {
                    InetAddress inetAddress = inetAddressEnumeration.nextElement();
                    if (!inetAddress.isLoopbackAddress()&&
                            !inetAddress.isLinkLocalAddress()&&
                            inetAddress.isSiteLocalAddress()) {
                        IPStringBuilder.append(inetAddress.getHostAddress().toString()+"\n");
                    }
                }
            }
        } catch (SocketException ex) {
        	ex.printStackTrace();
        }
        IP = IPStringBuilder.toString();
        return IP;
    }
    
    /**
     * 打开天游戏服务条款
     * @param activity
     */
    public static void openServerTerms(Activity activity) {
    	Intent intent = new Intent(activity, WebViewAvtivity.class);
		intent.putExtra("title", ResUtils.getString(activity, "ty_server_info2"));
		intent.putExtra("url", "file:///android_asset/agreement.html");
		activity.startActivity(intent);
    }
    
    /**
	 * 拨打客服电话
	 * @param context
	 */
	public static void callServerPhone(Activity context) {
		Intent intent = new Intent(Intent.ACTION_CALL);
		Uri data = Uri.parse("tel:" + SPHandler.getString(context, SPHandler.SP_TEXT_PHONE));
		intent.setData(data);
		context.startActivity(intent);
	}
	
	/**
	 * 联系客服qq
	 */
	public static void getQQTalk(Activity activity){
		try {
			String url="mqqwpa://im/chat?chat_type=wpa&uin="+SPHandler.getString(activity, SPHandler.SP_TEXT_QQ);  
			activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
		} catch (Exception e) {
			ToastUtils.show(activity, "请先安装手机QQ");
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
	
	public static String getFormateTime(){
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		String currentTime = sf.format(new Date());
		return currentTime;
	}
	
	public static void showProgressDialog(final Activity mActivity, String title, String message, final DialogCallback callback) {
		final ProgressDialog dialog = new ProgressDialog(mActivity);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setIndeterminate(false);
		dialog.setMax(100);
		dialog.incrementProgressBy(30);
		dialog.incrementSecondaryProgressBy(70);
		dialog.setCancelable(false);
		dialog.show();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				dialog.dismiss();
				callback.onDismiss();
			}
		}, 2000);
	}
	
	public interface DialogCallback {
		void onDismiss();
	}
	
	
	public static void showFinishPayDialog(final PayActivity activity,final boolean isFinish){
		View dialogView = View.inflate(activity, ResUtils.getResById(activity, "dialog_exit_pay", "layout"), null);
		final AlertDialog dialog = new AlertDialog.Builder(activity)
        .create();
		dialog.setView(dialogView);
		dialog.show();
		
		dialogView.findViewById(ResUtils.getResById(activity, "btn_pay_confirm_exit", "id")).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (isFinish){
					activity.finish();
				} else {
					activity.onBackPressed();
				}
				dialog.dismiss();
			}
		});
		
		dialogView.findViewById(ResUtils.getResById(activity, "btn_pay_pay_onplat", "id")).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String url = URLHolder.URL_PAY_ONPLAT+"username="+ConfigHolder.USER_ACCOUNT+
						"&appID="+ConfigHolder.GAME_ID+"&sid="+activity.mPayHandler.mPayInfo.getServerId()+
						"&usertoken="+ConfigHolder.USER_TOKEN+"&type=sdk";
				Log.d("tianyouxi", "pay platform url= "+url);
				Intent intent = new Intent(activity,MenuActivity.class);
				intent.putExtra("menu_type", MenuActivity.POPUP_MENU_7);
				intent.putExtra("url", url);
				activity.startActivityForResult(intent,activity.ACTIVITY_FINISH);
				dialog.dismiss();
			}
		});
	}
	
	/**
	 * 判断当前系统语言是否为中文
	 * @param context
	 * @return
	 */
	public static boolean isChineseLanguage(Context context) {
		Locale locale = context.getResources().getConfiguration().locale;
		return "zh".equals(locale.getLanguage()) ? true : false;
	}
	
	/**
	 * 判断当前系统语言简称
	 * @param context
	 * @return
	 */
	public static String getLanguageSort(Context context) {
		return context.getResources().getConfiguration().locale.getLanguage();
	}
}
