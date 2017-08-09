package com.multi.channel;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.tianyou.channel.bean.ChannelInfo;
import com.tianyou.channel.bean.PayParam;
import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.tianyou.channel.utils.ConfigHolder;
import com.tianyou.channel.utils.LogUtils;
import com.xiaomi.gamecenter.sdk.GameInfoField;
import com.xiaomi.gamecenter.sdk.MiCommplatform;
import com.xiaomi.gamecenter.sdk.MiErrorCode;
import com.xiaomi.gamecenter.sdk.OnLoginProcessListener;
import com.xiaomi.gamecenter.sdk.OnPayProcessListener;
import com.xiaomi.gamecenter.sdk.entry.MiAccountInfo;
import com.xiaomi.gamecenter.sdk.entry.MiAppInfo;
import com.xiaomi.gamecenter.sdk.entry.MiAppType;
import com.xiaomi.gamecenter.sdk.entry.MiBuyInfoOnline;

public class ChannelService extends BaseSdkService{

	@Override
	public void doApplicationCreate(Context context, boolean island) {
		super.doApplicationCreate(context, island);
		ChannelInfo channelInfo = ConfigHolder.getChannelInfo(context);
		MiAppInfo appInfo = new MiAppInfo();
		appInfo.setAppId(channelInfo.getAppId());
		appInfo.setAppKey(channelInfo.getAppKey());
		appInfo.setAppType(MiAppType.online); // 网游
		MiCommplatform.Init(context, appInfo);
	}
	
	@Override
	public void doActivityInit(Activity activity, TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		doNoticeGame(TianyouCallback.CODE_INIT, "");
	}
	
	@Override
	public void doLogin() {
		super.doLogin();
		MiCommplatform.getInstance().miLogin(mActivity, new MyOnLoginProcessListener());
	}

	@Override
	public void doChannelPay(PayParam payInfo, OrderinfoBean orderInfo) {
		super.doChannelPay(payInfo,orderInfo);
		String orderID = orderInfo.getOrderID();
		MiBuyInfoOnline online = new MiBuyInfoOnline();
		online.setCpOrderId(orderID); //订单号唯一(不为空)
		online.setCpUserInfo(orderInfo.getCustomInfo()); //此参数在用户支付成功后会透传给CP的服务器 
		online.setMiBi(Integer.parseInt(orderInfo.getMoNey())); //必须是大于1的整数, 10代表10米币,即10元人民币(不为空)

		//用户信息※必填※
		Bundle mBundle = new Bundle();
		LogUtils.d("xiaomi pay mRoleInfo= "+mRoleInfo.toString());
		mBundle.putString( GameInfoField.GAME_USER_BALANCE, mRoleInfo.getBalance());  //用户余额
		mBundle.putString( GameInfoField.GAME_USER_GAMER_VIP, mRoleInfo.getVipLevel() );  //vip 等级
		mBundle.putString( GameInfoField.GAME_USER_LV, mRoleInfo.getRoleLevel() );          //角色等级
		mBundle.putString( GameInfoField.GAME_USER_PARTY_NAME, mRoleInfo.getParty() );  //工会，帮派
		mBundle.putString( GameInfoField.GAME_USER_ROLE_NAME, mRoleInfo.getRoleName() ); //角色名称
		mBundle.putString( GameInfoField.GAME_USER_ROLEID, mRoleInfo.getRoleId());   //角色id
		mBundle.putString( GameInfoField.GAME_USER_SERVER_NAME, mRoleInfo.getServerName() );  //所在服务器
		Log.d("TAG","bundle= "+mBundle.toString()+"/// online= "+online.toString()+"/// orderID= "+orderID);
		MiCommplatform.getInstance().miUniPayOnline(mActivity,online, mBundle,new MyOnPayListener(orderID));
	}

	// 监听登录的接口
	private class MyOnLoginProcessListener implements OnLoginProcessListener{
		@Override
		public void finishLoginProcess(int code, final MiAccountInfo info) {
			switch(code) {
	        case MiErrorCode.MI_XIAOMI_GAMECENTER_SUCCESS: 
	            // 登陆成功
	            //获取用户的登陆后的 UID(即用户唯一标识)
	            long sid = info.getUid();
	            //获取用户的登陆的 Session(请参考 3.3用户session验证接口) 
	            String session = info.getSessionId();//若没有登录返回 null
	            mLoginInfo.setChannelUserId(sid+"");
	            mLoginInfo.setUserToken(session);
	            checkLogin();
	            break;
	        case MiErrorCode.MI_XIAOMI_GAMECENTER_ERROR_LOGIN_FAIL: 
	            // 登陆失败
	        	doNoticeGame(TianyouCallback.CODE_LOGIN_FAILED, "登录失败");
	            break;
	        case MiErrorCode.MI_XIAOMI_GAMECENTER_ERROR_CANCEL:
	            // 取消登录 
	        	doNoticeGame(TianyouCallback.CODE_LOGIN_CANCEL, "登录取消");
	            break;
	        case MiErrorCode.MI_XIAOMI_GAMECENTER_ERROR_ACTION_EXECUTED:
	            // 登录操作正在进行中 
	            break;
	        default:
	            // 登录失败
	        	doNoticeGame(TianyouCallback.CODE_LOGIN_FAILED, "登录失败");
	            break;
			}
		}
	}
	
	@Override
	public boolean isShowLogout() {
		LogUtils.d("isShowLogout return true");
		return true;
	}
	
	@Override
	public boolean isShowExitGame() {
		LogUtils.d("isShowExitGame return true");
		return true;
	}

	// 监听支付的接口
	private class MyOnPayListener implements OnPayProcessListener{

		private String orderID;
		public MyOnPayListener(String orderId){
			this.orderID = orderId;
		}

		@Override
		public void finishPayProcess(int code) {
			LogUtils.d("finishPayProcess Code= "+code);
			switch(code) {
	        case MiErrorCode.MI_XIAOMI_GAMECENTER_SUCCESS: 
	             // 购买成功
	        	checkOrder(orderID);
	             break;
	        case MiErrorCode.MI_XIAOMI_GAMECENTER_ERROR_PAY_CANCEL:
	             // 取消购买 
	        	doNoticeGame(TianyouCallback.CODE_PAY_CANCEL, "支付取消");
	             break;
	        case MiErrorCode.MI_XIAOMI_GAMECENTER_ERROR_PAY_FAILURE: 
	             // 购买失败
	        	doNoticeGame(TianyouCallback.CODE_PAY_FAILED, "支付失败");
	             break;
	        case MiErrorCode.MI_XIAOMI_GAMECENTER_ERROR_ACTION_EXECUTED:
	             //操作正在进行中 
	             break;
	        default:
	             // 购买失败
	        	doNoticeGame(TianyouCallback.CODE_PAY_FAILED, "支付失败");
	             break; 
	        }			
		}
	}
}
