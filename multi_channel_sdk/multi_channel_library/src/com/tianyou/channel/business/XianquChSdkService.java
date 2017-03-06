package com.tianyou.channel.business;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import cn.joy2u.common.model.PayChannelType;
import cn.joy2u.common.service.OpenApiService;
import cn.joy2u.common.type.MoneyType;
import cn.joy2u.middleware.EnvironmentType;
import cn.joy2u.middleware.Joy2uCallback;
import cn.joy2u.middleware.Joy2uPlatform;
import cn.joy2u.middleware.model.LoginResult;
import cn.joy2u.middleware.model.PayResult;

import com.baidu.bdgame.sdk.obf.ml;
import com.tianyou.channel.bean.LoginInfo;
import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.bean.PayParam;
import com.tianyou.channel.bean.RoleInfo;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.tianyou.channel.utils.LogUtils;
import com.tianyou.channel.utils.ToastUtils;

public class XianquChSdkService extends BaseSdkService{
	
	private Joy2uPlatform joy2u = Joy2uPlatform.getInstance();
	
	@Override
	public void doApplicationCreate(Context context, boolean island) {
		super.doApplicationCreate(context, island);
		LogUtils.d("application oncreate------------------");
//		 try { 
//             Class<?> cls = Class.forName("com.snowfish.cn.ganga.helper.SFOnlineApplication");
//             Constructor<?> con = cls.getConstructor(Context.class);
//             con.newInstance(context.getApplicationContext());
//             Object obj = con.newInstance(context.getApplicationContext());
//             Method md = cls.getMethod("onCreate"); 
//             md.invoke(obj); 
//         } catch (Exception e) { 
//        	 e.printStackTrace(); 
//         } 
	}
	
	@Override
	public void doActivityInit(Activity activity,TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		LogUtils.d("activity oncreate---------------");
		
		joy2u.setEnvironment(EnvironmentType.PROD);//设置环境：TEST为测试环境，AWS:为亚马逊环境,PROD为国内生产环境
		joy2u.setInitCallback(new Joy2uCallback<String>() {

			@Override
			public void callback(String msg, boolean flag) {
				LogUtils.d("msg= "+msg+",flag= "+flag);
				if (flag) {
					mTianyouCallback.onResult(TianyouCallback.CODE_INIT, "SDK初始化完成");
				}
			}
		});	// 设置初始话运行环境回调
		joy2u.setLoginCallback(new MyLoginCallback());	//设置登录回调
		joy2u.setLogoutCallback(new Joy2uCallback<String>() {

			@Override
			public void callback(String msg, boolean flag) {
				if (flag) {
					mTianyouCallback.onResult(TianyouCallback.CODE_LOGOUT, msg);
				}
			}
		});//设置拿出回调
		joy2u.setPayCallback(new MyPayCallback());//设置支付回调
		
		joy2u.setExitCallback(new Joy2uCallback<String>() {

			@Override
			public void callback(String msg, boolean flag) {
				if (flag) {
					LogUtils.d("---------------1");
					mTianyouCallback.onResult(TianyouCallback.CODE_QUIT_SUCCESS, msg);
				} else {
					mTianyouCallback.onResult(TianyouCallback.CODE_QUIT_CANCEL, msg);
				}
			}
		});//设置退出应用回调
		
		joy2u.init(mActivity);
		
		if(joy2u.hasSplash()) {	// 如果渠道要求使用他们的闪屏
		    joy2u.splash();
		} else {
			// 自己实现闪屏
		} 
		if(joy2u.hasUserCenter()) {
			// 如果有用户中心，才显示进入用户中心的按钮
		}
	}
	
	@Override
	public void doLogin() {
		super.doLogin();
		mActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					joy2u.login();
				} catch (Exception e) {
					LogUtils.d(e.getMessage());
				}
			}
		});
	}
	
	@Override
	public void doLogout() {
		super.doLogout();
		joy2u.logout();
	}
	
	@Override
	public void doChannelPay(PayParam payInfo, OrderinfoBean orderInfo) {
		super.doChannelPay(payInfo, orderInfo);
		joy2u.pay(orderInfo.getServerID(), orderInfo.getServerName(), orderInfo.getOrderID(),
				Integer.parseInt(orderInfo.getMoNey()), MoneyType.CNY, Integer.parseInt(orderInfo.getRate()), 
				orderInfo.getProduct_name(), orderInfo.getRoleId(), mRoleInfo.getRoleName(), 
				mRoleInfo.getRoleLevel(), mRoleInfo.getVipLevel(), 
				mRoleInfo.getParty(), mRoleInfo.getBalance(), null);
		Log.d("TAG", "serverID= "+orderInfo.getServerID()+",serverName= "+orderInfo.getServerName()+",orderID= "+orderInfo.getOrderID()
				+",money= "+orderInfo.getMoNey()+",rate= "+orderInfo.getRate()+",productName= "+orderInfo.getProduct_name()
				+"roleID= "+orderInfo.getRoleId()+",roleName= "+mRoleInfo.getRoleName()+",roleLevel= "+mRoleInfo.getRoleLevel()
				+",vipLevel= "+mRoleInfo.getVipLevel()+",party= "+mRoleInfo.getParty()+",balance= "+mRoleInfo.getBalance());
	}
	
	@Override
	public void doEntryGame() {
		super.doEntryGame();
		
		joy2u.loginGameServer(mRoleInfo.getRoleId(), mRoleInfo.getRoleName(), mRoleInfo.getRoleLevel(), 
				mRoleInfo.getServerId(), mRoleInfo.getServerName(), mRoleInfo.getBalance(), mRoleInfo.getVipLevel(),
				mRoleInfo.getParty(), mRoleInfo.getCreateTime(), mRoleInfo.getRoleLevelUpTime());
		
		try {
			OpenApiService.getInstance(mActivity).loginGameServer(Long.parseLong(mRoleInfo.getServerId()), mRoleInfo.getServerName(),
					"http://channel.tianyouxi.com/index.php/AndroidReceive/GetPayInfo");
		} catch (Exception e) {
			e.printStackTrace();
			LogUtils.d("e= "+e.getMessage());
		}
	}
	
	@Override
	public void doUploadRoleInfo(RoleInfo roleInfo) {
		super.doUploadRoleInfo(roleInfo);
		
		joy2u.uploadGameData(mRoleInfo.getServerId(), mRoleInfo.getServerName(), mRoleInfo.getRoleId(), 
				mRoleInfo.getRoleName(), mRoleInfo.getRoleLevel(), mRoleInfo.getParty(), 
				mRoleInfo.getVipLevel(), mRoleInfo.getBalance(), null);
		
//		joy2u.uploadGameData(mRoleInfo.getServerId(), mRoleInfo.getServerName(), mRoleInfo.getRoleId(), 
//				mRoleInfo.getRoleName(), mRoleInfo.getRoleLevel(), mRoleInfo.getParty(), mRoleInfo.getVipLevel(), 
//				mRoleInfo.getBalance(), null);
	}
	
	@Override
	public void doCreateRole(RoleInfo roleInfo) {
		super.doCreateRole(roleInfo);
		joy2u.createRole(mRoleInfo.getRoleId(), mRoleInfo.getRoleName(), mRoleInfo.getRoleLevel(), 
				mRoleInfo.getServerId(), mRoleInfo.getServerName(), mRoleInfo.getBalance(), mRoleInfo.getVipLevel(),
				mRoleInfo.getParty(), mRoleInfo.getCreateTime(), mRoleInfo.getRoleLevelUpTime());
	}
	
	@Override
	public void doUpdateRoleInfo(RoleInfo roleInfo) {
		super.doUpdateRoleInfo(roleInfo);
		joy2u.levelUpRole(mRoleInfo.getRoleId(), mRoleInfo.getRoleName(), mRoleInfo.getRoleLevel(), 
				mRoleInfo.getServerId(), mRoleInfo.getServerName(), mRoleInfo.getBalance(), mRoleInfo.getVipLevel(),
				mRoleInfo.getParty(), mRoleInfo.getCreateTime(), mRoleInfo. getRoleLevelUpTime());
	}
	
	@Override
	public boolean isShowExitGame() {
		return true;
	}
	
	@Override
	public void doExitGame() {
		LogUtils.d("doExitGame-----------------");
		joy2u.exit();
//		super.doExitGame();
	}
	
	@Override
	public void doPause() {
		super.doPause();
		LogUtils.d("onPause----------------");
		joy2u.onPause();
	}
	
	@Override
	public void doResume() {
		super.doResume();
		LogUtils.d("onResume---------------");
		joy2u.onResume();
	}
	
	@Override
	public void doRestart() {
		super.doRestart();
		LogUtils.d("onRestart-----------------");
		joy2u.onRestart();
	}
	
	@Override
	public void doBackPressed() {
		LogUtils.d("onBackPressed---------------");
		joy2u.onBackPressed(); 
	}
	
	@Override
	public void doNewIntent(Intent intent) {
		super.doNewIntent(intent);
		LogUtils.d("onNewIntent--------------------");
		joy2u.onNewIntent(intent);
	}
	
	@Override
	public void doStop() {
		super.doStop();
		LogUtils.d("onStop-----------------");
		joy2u.onStop();
	}
	
	@Override
	public void doDestory() {
		super.doDestory();
		LogUtils.d("onDestroy-----------------");
		joy2u.onDestroy();
//		System.exit(0);
//		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	@Override
	public void doActivityResult(int requestCode, int resultCode, Intent data) {
		super.doActivityResult(requestCode, resultCode, data);
		LogUtils.d("onActivityResult--------------");
		LogUtils.d("data= "+data);
		joy2u.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public void doConfigurationChanged(Configuration newConfig) {
		super.doConfigurationChanged(newConfig);
		joy2u.onConfigurationChanged(newConfig);
	}
	
	
	private class MyInitCallback implements Joy2uCallback<String> {
		@Override
		public void callback(String msg, boolean flag) {
			Log.d("TAG", "msg= "+msg+",flag= "+flag);
			ToastUtils.showToast(mActivity, "msg= "+msg+",flag= "+flag);
		}
	}
	
	private class MyLoginCallback implements Joy2uCallback<LoginResult> {
		
		@Override
		public void callback(LoginResult result, boolean flag) {
			Log.d("TAG", "ext= "+result.getExt()+",msg= "+result.getMsg()+",ticket= "+result.getTicket()+",userid= "+result.getUserId()
					+",flag= "+flag);
			if (flag) {
				mLoginInfo.setChannelUserId(result.getUserId()+"");
				mLoginInfo.setUserToken(result.getTicket());
				checkLogin(mLoginInfo);
			}
		}
	}
	
	private class MyPayCallback implements Joy2uCallback<PayResult> {

		@Override
		public void callback(PayResult payResult, boolean flag) {
			String orderID = payResult.getOrderId();
			PayChannelType payChannel = payResult.getPayChannel();
			if (flag) {
				checkOrder(orderID);
			} else {
				mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, "支付失败");
			}
		}
		
	}

}
