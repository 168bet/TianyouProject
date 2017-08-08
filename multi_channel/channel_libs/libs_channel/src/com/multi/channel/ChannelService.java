package com.multi.channel;

import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;

import android.app.Activity;

public class ChannelService extends BaseSdkService{
	
	@Override
	public void doActivityInit(Activity activity, TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		doNoticeGame(TianyouCallback.CODE_INIT, "");
	}
}