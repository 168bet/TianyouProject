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
				sdkService = (BaseSdkService) Class.forName("com.multi.channel.ChannelService").newInstance();
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
