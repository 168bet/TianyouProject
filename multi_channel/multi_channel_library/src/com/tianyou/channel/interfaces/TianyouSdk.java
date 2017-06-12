package com.tianyou.channel.interfaces;

import com.tianyou.channel.bean.ChannelInfo;
import com.tianyou.channel.utils.ConfigHolder;
import com.tianyou.channel.utils.LogUtils;
import com.tianyou.channel.utils.ToastUtils;

import android.content.Context;

/**
 * 多渠道入口类
 * @author itstrong
 *
 */
public class TianyouSdk {

	private static BaseSdkService sdkService;
	
	private TianyouSdk() { }

	public static BaseSdkService getInstance(Context context) {
		if (sdkService != null) return sdkService;
		ChannelInfo channelInfo = ConfigHolder.getChannelInfo(context);
		LogUtils.d("channelInfo:" + channelInfo);
		if (channelInfo == null) {
			ToastUtils.show(context, "需打入渠道资源");
		} else {
			try {
				String className = ""; 
				if ("ty000".equals(channelInfo.getChannelId())) {
					className = "TianyouSdkService";
				} else if ("ty001".equals(channelInfo.getChannelId())) {
					className = "XiaoMiSdkService";
				} else if ("ty011".equals(channelInfo.getChannelId())) {
					className = "LeshiSpecialSdkService";
				} else if ("ty025".equals(channelInfo.getChannelId())) {
					className = "M4399SdkService";
				} else if ("ty066".equals(channelInfo.getChannelId())) {
					className = "HanfengService";
				}
				LogUtils.d("className:" + className);
				sdkService = (BaseSdkService) Class.forName("com.multi.channel." + className).newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return sdkService;
	}

	public static String getChannelName(Context context) {
		ChannelInfo channelInfo = ConfigHolder.getChannelInfo(context);
		return channelInfo == null ? "没有配置文件" : channelInfo.getChannelId();
	}
}
