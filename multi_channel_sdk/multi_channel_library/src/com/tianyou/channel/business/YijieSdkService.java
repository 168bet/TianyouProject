package com.tianyou.channel.business;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.util.Log;

import com.snowfish.cn.ganga.helper.SFOnlineExitListener;
import com.snowfish.cn.ganga.helper.SFOnlineHelper;
import com.snowfish.cn.ganga.helper.SFOnlineInitListener;
import com.snowfish.cn.ganga.helper.SFOnlineLoginListener;
import com.snowfish.cn.ganga.helper.SFOnlinePayResultListener;
import com.snowfish.cn.ganga.helper.SFOnlineUser;
import com.tianyou.channel.bean.LoginInfo;
import com.tianyou.channel.bean.PayParam;
import com.tianyou.channel.bean.RoleInfo;
import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.tianyou.channel.utils.LogUtils;

public class YijieSdkService extends BaseSdkService{

	// Activity初始化
	@Override
	public void doActivityInit(Activity activity,
			TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		
		SFOnlineHelper.onCreate(mActivity,new SFOnlineInitListener() {
			
			@Override
			public void onResponse(String tag, String value) {
				if (tag.equalsIgnoreCase("success")) {
					// 初始化成功
					mTianyouCallback.onResult(TianyouCallback.CODE_INIT, value);
					LogUtils.d("init success value= "+value);
				} else if (tag.equalsIgnoreCase("fail")) {
					// 初始化失败
					LogUtils.d("init failed value= "+value);
					
				}
			}
		});
		
		SFOnlineHelper.setLoginListener(mActivity, new SFOnlineLoginListener() {
			
			@Override
			public void onLoginSuccess(SFOnlineUser user, Object customParams) {
				// 登录成功
//				mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_SUCCESS, "登录成功");
				LoginInfo loginParam = new LoginInfo();
				loginParam.setChannelUserId(user.getChannelUserId());
				loginParam.setUserToken(user.getToken());
				loginParam.setIsGuest(user.getChannelId());
				loginParam.setNickname(user.getUserName());
				checkLogin(loginParam);
				LogUtils.d("login success id= "+user.getId()+",channelId= "+user.getChannelId()+
						",channelUserId= "+user.getChannelUserId()+",productCode= "+user.getProductCode()+
						",token= "+user.getToken()+",userName= "+user.getUserName()+",custom= "+customParams);
			}
			
			@Override
			public void onLoginFailed(String msg, Object customParams) {
				// 登录失败
				mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, "登录失败");
				LogUtils.d("loign failed msg= "+msg+",custom= "+customParams);
			}
			
			@Override
			public void onLogout(Object customParams) {
				// 退出回调
				mTianyouCallback.onResult(TianyouCallback.CODE_LOGOUT, "退出登录");
				LogUtils.d("logout custom= "+customParams);
			}
		});
	}
	
	// 登录
	@Override
	public void doLogin() {
		super.doLogin();
		SFOnlineHelper.login(mActivity, "Login");
	}
	
	// 登出
	@Override
	public void doLogout() {
		super.doLogout();
		SFOnlineHelper.logout(mActivity, "LoginOut");
	}
	
	// 进入游戏
	@Override
	public void doEntryGame() {
		super.doEntryGame();
//		SFOnlineHelper.setRoleData(mActivity, mRoleInfo.getRoleId(), mRoleInfo.getRoleName(), mRoleInfo.getRoleLevel(), 
//				mRoleInfo.getServerId(), mRoleInfo.getServerName());
		SFOnlineHelper.setData(mActivity, "enterServer", getDataValue(mRoleInfo));
	}
	
	// 上传角色信息
	@Override
	public void doUploadRoleInfo(RoleInfo roleInfo) {
		super.doUploadRoleInfo(roleInfo);
		SFOnlineHelper.setRoleData(mActivity, mRoleInfo.getRoleId(), mRoleInfo.getRoleName(), mRoleInfo.getRoleLevel(), 
				mRoleInfo.getServerId(), mRoleInfo.getServerName());
	}
	
	// 退出游戏
	@Override
	public void doExitGame() {
		super.doExitGame();
		SFOnlineHelper.exit(mActivity, new SFOnlineExitListener() {
			
			@Override
			public void onSDKExit(boolean flag) {
				if (flag) {
					// 退出游戏回调
					mTianyouCallback.onResult(TianyouCallback.CODE_QUIT_SUCCESS, "退出游戏");
					LogUtils.d("exit success -----------");
				} else {
					mTianyouCallback.onResult(TianyouCallback.CODE_QUIT_CANCEL, "退出游戏失败");
					LogUtils.d("exit failed----------------");
				}
			}
			
			@Override
			public void onNoExiterProvide() {
				// SDK没有退出界面时，这里通知游戏
				LogUtils.d("on No Exiter Provide------------");
				mTianyouCallback.onResult(TianyouCallback.CODE_QUIT_SUCCESS, "退出游戏——无界面");
			}
		});
	}
	
	// 支付
	@Override
	public void doChannelPay(PayParam payInfo, final OrderinfoBean orderInfo) {
		super.doChannelPay(payInfo, orderInfo);
		SFOnlineHelper.pay(mActivity, Integer.parseInt(orderInfo.getMoNey()), orderInfo.getProduct_name(), 1, orderInfo.getOrderID(), orderInfo.getNotifyurl(), 
				new SFOnlinePayResultListener() {
					
					@Override
					public void onSuccess(String msg) {
						// 支付成功
//						mTianyouCallback.onResult(TianyouCallback.CODE_PAY_SUCCESS, "支付成功");
						checkOrder(orderInfo.getOrderID());
						LogUtils.d("pay success msg= "+msg);
					}
					
					@Override
					public void onOderNo(String msg) {
						LogUtils.d("on order no= "+msg);
					}
					
					@Override
					public void onFailed(String msg) {
						// 支付失败
						mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, "支付失败");
						LogUtils.d("pay failed msg= "+msg);
					}
				});
	}
	
	// 创建角色
	@Override
	public void doCreateRole(RoleInfo roleInfo) {
		super.doCreateRole(roleInfo);
		SFOnlineHelper.setData(mActivity, "createrole", getDataValue(mRoleInfo));
	}
	
	// 角色升级
	@Override
	public void doUpdateRoleInfo(RoleInfo roleInfo) {
		super.doUpdateRoleInfo(roleInfo);
		SFOnlineHelper.setData(mActivity, "levelup", getDataValue(mRoleInfo));
	}
	
	private String getDataValue(RoleInfo info){
		JSONObject data = new JSONObject();
		try {
			data.put("roleId", info.getRoleId());
			data.put("roleName", info.getRoleName());
			data.put("roleLevel", info.getRoleLevel());
			data.put("zoneId", info.getServerId());
			data.put("zoneName", info.getServerName());
			data.put("balance", info.getBalance());
			data.put("vip", info.getVipLevel());
			data.put("partyName", info.getParty());
			data.put("roleCTime", info.getCreateTime());
			data.put("roleLevelMTime", info.getRoleLevelUpTime());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		LogUtils.d("getDataValue= "+data.toString());
		return data.toString();
		
	}
	
	@Override
	public void doStop() {
		super.doStop();
		SFOnlineHelper.onStop(mActivity);
	}
	
	@Override
	public void doResume() {
		super.doResume();
		SFOnlineHelper.onResume(mActivity);
	}
	
	@Override
	public void doPause() {
		super.doPause();
		SFOnlineHelper.onPause(mActivity);
	}
	
	@Override
	public void doRestart() {
		super.doRestart();
		SFOnlineHelper.onRestart(mActivity);
	}
	
	@Override
	public void doDestory() {
		super.doDestory();
		SFOnlineHelper.onDestroy(mActivity);
	}
	
}
