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
			sdkService = new BaseSdkService();
		} else {
			try {
				String className = ""; 
				if ("ty000".equals(channelInfo.getChannelId())) {
					className = "TianyouSdkService";
				} else if ("ty000".equals(channelInfo.getChannelId())) {
					className = "TianyouSdkService";
				} else if ("ty001".equals(channelInfo.getChannelId())) {
					className = "XiaoMiSdkService";
				} else if ("ty007".equals(channelInfo.getChannelId())) {
					className = "DownJoySdkService";
				} else if ("ty009".equals(channelInfo.getChannelId())) {
					className = "JinliSdkService";
				} else if ("ty011".equals(channelInfo.getChannelId())) {
//					className = "LeshiSpecialSdkService";
					className = "LeshiSdkService";
				} else if ("ty025".equals(channelInfo.getChannelId())) {
					className = "M4399SdkService";
				} else if ("ty066".equals(channelInfo.getChannelId())) {
					className = "HanfengService";
				} else if ("ty067".equals(channelInfo.getChannelId())) {
					className = "LeyouSdkService";
				} else if ("ty069".equals(channelInfo.getChannelId())) {
					className = "LeshiSpecialSdkService";
				} else if ("ty073".equals(channelInfo.getChannelId())) {
					className = "SamsungSdkService";
				} else if ("bm105".equals(channelInfo.getChannelId())) {
					className = "YilingSdkService";
				}
				LogUtils.d("className:" + className);
				sdkService = (BaseSdkService) Class.forName("com.multi.channel." + className).newInstance();
			} catch (Exception e) {
				e.printStackTrace();
				ToastUtils.show(context, "渠道类没有找到");
				sdkService = new BaseSdkService();
			}
		}
		return sdkService;
	}

	public static String getChannelName(Context context) {
		ChannelInfo channelInfo = ConfigHolder.getChannelInfo(context);
		return channelInfo == null ? "没有配置文件" : channelInfo.getChannelId();
	}
}
