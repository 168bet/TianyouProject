package com.tianyou.channel.business;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import com.tianyou.channel.bean.ChannelInfo;
import com.tianyou.channel.bean.RoleInfo;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.MultiChannelCallback.ResultCallback;
import com.tianyou.channel.interfaces.MultiChannelCallback.ResultCancelCallback;
import com.tianyou.channel.utils.AppUtils;
import com.tianyou.channel.utils.ConfigHolder;
import com.tianyou.channel.utils.HttpUtils;
import com.wett.cooperation.container.SdkCallback;
import com.wett.cooperation.container.TTSDKV2;
import com.wett.cooperation.container.bean.GameInfo;

public class TTSdkService extends BaseSdkService {
	
	private static ResultCallback logoutCallback;
	private RoleInfo mRoleInfo;
	
	private static String tyAppID;
	private static String promotion;

	@Override
	public void doApplicationCreate() {
		TTSDKV2.getInstance().prepare(mContext);
	}
	
	@Override
	public void doActivityInit(final ResultCallback callback) {
		
		ChannelInfo channelInfo = ConfigHolder.getChannelInfo(mActivity);
		tyAppID = channelInfo.getGameId();
		promotion = channelInfo.getChannelId();
		
		GameInfo gameInfo = new GameInfo();
		TTSDKV2.getInstance().init(mActivity, gameInfo, false, Configuration.ORIENTATION_PORTRAIT, new SdkCallback<String>() {
			@Override
			protected boolean onResult(int status, String msg) {
				if (status == 0) {
					callback.onSuccess(msg);
					
					TTSDKV2.getInstance().setLogoutListener(new SdkCallback<String>() {
						@Override
						protected boolean onResult(int logouStatus, String logoutMsg) {
							if (logouStatus == 0) {
								TTSDKV2.getInstance().showFloatView(mActivity);
								logoutCallback.onSuccess(logoutMsg);
							} else {
								logoutCallback.onFailed(logoutMsg);
							}
							return false;
						}
					});
				} else {
					callback.onFailed(msg);
				}
				return false;
			}
		});
	}
	
	@Override
	public void doLogin(ResultCallback callback) {
		TTSDKV2.getInstance().login(mActivity, new SdkCallback<String>() {

			@Override
			protected boolean onResult(int status, String msg) {
				if (status == 0){
					Log.d("TAG", "login success msg= "+msg);
					String session = TTSDKV2.getInstance().getSession();
					String sid = TTSDKV2.getInstance().getUid();
					
					Log.d("TAG", "sid= "+sid+",sesion= "+session);
					String phoneIMEI = AppUtils.getPhoeIMEI(mActivity);
					String mdSignature = AppUtils.MD5("session="+session+"&uid="+sid+"&appid="+tyAppID);
					Map<String, String> loginParam = new HashMap<String, String>();
					loginParam.put("uid",sid);
					loginParam.put("session",session);
					loginParam.put("imei",phoneIMEI);
					loginParam.put("appid",tyAppID);
					loginParam.put("promotion",promotion);
					loginParam.put("signature", mdSignature);
					Log.d("TAG","tt login param= "+loginParam);
//					HttpUtils.post(path, data, callback)
				} else {
					Log.d("TAG", "login failed msg= "+msg);
				}
				return false;
			}
		});
	}
	
	@Override
	public void doUploadRoleInfo(RoleInfo roleInfo) {
		mRoleInfo = roleInfo;
	}
	
	@Override
	public void doLogout(ResultCallback callback) {
		logoutCallback = callback;
		TTSDKV2.getInstance().logout(mActivity);
	}
	
	@Override
	public void doPay(ResultCancelCallback callback, String payCode) {
		
	}
	
	@Override
	public void doResume() {
		 TTSDKV2.getInstance().onResume(mActivity);
		 if(TTSDKV2.getInstance().isLogin()){
		    TTSDKV2.getInstance().showFloatView(mActivity);
		 }
	}
	
	@Override
	public void doPause() {
		TTSDKV2.getInstance().onPause(mActivity);
		TTSDKV2.getInstance().hideFloatView(mActivity);
	}
	

}
