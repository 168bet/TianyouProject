package com.multi.channel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import android.app.Activity;
import android.content.Intent;

import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.bean.PayParam;
import com.tianyou.channel.bean.RoleInfo;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.youxun.sdk.app.YouxunProxy;
import com.youxun.sdk.app.YouxunXF;
import com.youxun.sdk.app.model.MessageEvent;

public class ChannelService extends BaseSdkService {

	@Override
	public void doActivityInit(Activity activity,TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		//注册EventBus
		EventBus.getDefault().register(this);
		//初始化 game key
		YouxunProxy.init(mChannelInfo.getGameName(), mChannelInfo.getAppId());
		
		doNoticeGame(TianyouCallback.CODE_INIT, "初始化成功");
	}
	
	@Override
	public void doDestory() {
		super.doDestory();
		YouxunXF.onDestroy();
	}
	//方式一、通过EventBus接收返回的数据
	@Subscribe
    public void msgEventBus(MessageEvent message) {
		int code = message.getCode();
		Intent data = message.getIntent();
		//登录注册状态
		if (code == 0) {
			if (data.getStringExtra("data").equals("success")) {
				//登入成功
				String userid = data.getStringExtra("userid");
				mLoginInfo.setChannelUserId(userid);
				checkLogin(new LoginCallback() {
					@Override
					public void onSuccess() {
						//创建悬浮图标          悬浮图标占屏幕的比例  0.4=>竖屏的十分之四处 
						YouxunXF.onCreate(mActivity, 0.4f);
					}
				});
				//检测版本
				YouxunProxy.updateDialog(mActivity, data);
				
				//提示用户账号信息
				YouxunXF.hintUserInfo(mActivity);
			}else {
				//登入失败
				doNoticeGame(TianyouCallback.CODE_LOGIN_FAILED, "");
			}
		}
		
		//支付状态……
		if (code == 1) {
			if (data.getStringExtra("data").equals("success")) {
				//支付成功
				checkOrder(mPayInfo.getOrderId());
			}else {
				//支付失败
				doNoticeGame(TianyouCallback.CODE_PAY_FAILED, "");
			}
		}
		
		//切换账号……
		if (code == 2) {
			doNoticeGame(TianyouCallback.CODE_LOGOUT, "");
			//销毁悬浮图标
//				YouxunXF.onDestroy();
			//启动登录
			YouxunProxy.startLogin(mActivity);
		}
    }
		
	@Override
	public void doLogin() {
		super.doLogin();
		//启动登录
		YouxunProxy.startLogin(mActivity);
	}

	@Override
	public void doUploadRoleInfo(RoleInfo roleInfo) {
		super.doUploadRoleInfo(roleInfo);
//		server 角色所在区服id标识
//		role 角色名称
//		level 角色等级
//		less 角色注册时间戳
//      gid 角色id
//      area 区服名称
		YouxunProxy.uploadRole(mActivity, roleInfo.getServerId(), roleInfo.getRoleName(), roleInfo.getRoleLevel(), roleInfo.getCreateTime(), roleInfo.getRoleId(), roleInfo.getServerName());
	}

	@Override
	public void doChannelPay(PayParam payInfo, final OrderinfoBean orderInfo) {
		super.doChannelPay(payInfo, orderInfo);
		YouxunProxy.startPay(mActivity, orderInfo.getProduct_name(), orderInfo.getMoNey(),   orderInfo.getOrderID(), orderInfo.getServerID());
	}
	@Override
	public void doUpdateRoleInfo(RoleInfo roleInfo) {
		super.doUpdateRoleInfo(roleInfo);
		YouxunProxy.uploadRole(mActivity, roleInfo.getServerId(), roleInfo.getRoleName(), 
				roleInfo.getRoleLevel(), roleInfo.getCreateTime(), roleInfo.getRoleId(), roleInfo.getServerName());
	}
	@Override
	public void doExitGame() {
		YouxunProxy.exitLogin(mActivity);
	}
	
	@Override
	public boolean isShowExitGame() {
		return !super.isShowExitGame();
	}
}
