package com.asyx.issue;

import android.app.Activity;

import com.asyx.jdsdk.ASGameSDK;
import com.asyx.jdsdk.ExitGameListener;
import com.asyx.jdsdk.LoginResultListener;
import com.asyx.jdsdk.PayResultListener;
import com.asyx.jdsdk.RoleData;
import com.u8.sdk.InitResult;
import com.u8.sdk.PayParams;
import com.u8.sdk.PayResult;
import com.u8.sdk.SDKParams;
import com.u8.sdk.U8Code;
import com.u8.sdk.U8SDK;
import com.u8.sdk.UserExtraData;

public class ASYXSDK {
	private static ASYXSDK instance;

	private String cpID;
	private String md5Key;
	private String gameID;
	private String gameName;
	private String channelId;

	private String userId;
	private String username;

	public static ASYXSDK getInstance() {
		if (instance == null) {
			instance = new ASYXSDK();
		}
		return instance;
	}

	public void initSDK(SDKParams params) {
		this.parseSDKParams(params);
		this.initSDK();
	}

	private void initSDK() {
		// TODO::这里调用SDK初始化方法
		U8SDK.getInstance().onResult(U8Code.CODE_INIT_SUCCESS, "u8sdk init success!");
		U8SDK.getInstance().onInitResult(new InitResult(true, null));
	}

	private void parseSDKParams(SDKParams params) {

		this.cpID = params.getString("CpId");
		this.md5Key = params.getString("MD5Key");
		this.gameID = params.getString("GameId");
		this.gameName = params.getString("GameName");
		this.channelId = params.getString("ChannelId");
	}

	public void login(Activity context) {
		// TODO::这里调用爱上游戏的登录方法
		ASGameSDK.login(cpID, gameID, md5Key, context, new LoginResultListener() {

			@Override
			public void loginSuccess(String userID, String username) {
				// TODO 登录成功
				ASYXSDK.instance.userId = userID;
				ASYXSDK.instance.username = username;
				U8SDK.getInstance().onResult(U8Code.CODE_LOGIN_SUCCESS, userID);
				U8SDK.getInstance().onLoginResult(userID);
			}

			@Override
			public void loginFail() {
				// TODO 登录失败
				U8SDK.getInstance().onResult(U8Code.CODE_LOGIN_FAIL, "asyx sdk login failed");
			}
		});
	}

	public void switchLogin(Activity context) {
		// TODO::这里调用爱上游戏切换帐号的方法
		// 如果没有提供切换帐号的方法，那么切换帐号的逻辑就是[先登出，再登录]，也就是先调用logout，再调用login
		U8SDK.getInstance().onLogout();
	}

	public void logout() {
		// TODO::调用爱上游戏的登出方法
		U8SDK.getInstance().onLogout();
	}

	public void showUserCenter() {
		// TODO::调用爱上游戏显示个人中心的方法
		// 如果爱上游戏没有提供对应的接口，则不用实现该方法
	}

	public void exit(final Activity context) {
		// TODO::调用爱上游戏显示退出确认框接口
		// 如果爱上游戏没有提供对应的接口，则不用实现该方法
		ASGameSDK.exitGame(context, new ExitGameListener() {

			@Override
			public void exitGameSuccess() {
				// TODO Auto-generated method stub
				context.finish();
				System.exit(0);
			}

			@Override
			public void exitGameFail() {
				// TODO Auto-generated method stub

			}
		});
	}

	public void submitGameData(UserExtraData data) {
		// TODO::调用爱上游戏上报玩家数据接口
		RoleData role = new RoleData();
		if (data.getRoleID() != null)
			role.setRoleID(data.getRoleID());
		if (data.getRoleName() != null)
			role.setRoleName(data.getRoleName());
		if (data.getRoleLevel() != null)
			role.setRoleLevel(data.getRoleLevel());
		role.setServerID(data.getServerID() + "");
		if (data.getServerName() != null)
			role.setServerName(data.getServerName());
		role.setMoneyNum(data.getMoneyNum() + "");
		role.setRoleCreateTime(data.getRoleCreateTime() + "");
		role.setRoleLevelUpTime(data.getRoleLevelUpTime() + "");
		ASGameSDK.submitRoleMessage(cpID, gameID, md5Key, userId, role);
	}

	public void pay(Activity context, final PayParams data) {
		// TODO::调用爱上游戏充值接口
		if (userId == null || username == null) {
			U8SDK.getInstance().onResult(U8Code.CODE_PAY_FAIL, "请先登录，再支付！");
			return;
		}
		if (data.getRoleId() == null) {
			data.setRoleId("");
		}
		if (data.getRoleName() == null) {
			data.setRoleName("");
		}
		if (data.getServerId() == null) {
			data.setServerId("");
		}
		if (data.getServerName() == null) {
			data.setServerName("");
		}
		ASGameSDK.pay(cpID, gameID, md5Key, userId, username, String.valueOf(data.getPrice()), data.getProductName(), gameName, data.getProductDesc(), data.getRoleId(), data.getRoleName(), data.getServerId(), data.getServerName(), data.getOrderID() + "_" + channelId, context, new PayResultListener() {

			@Override
			public void paySuccess() {
				// TODO 支付成功
				U8SDK.getInstance().onResult(U8Code.CODE_PAY_SUCCESS, "asyx sdk pay success.");
				PayResult payResult = new PayResult();
				payResult.setProductID(data.getProductId());
				payResult.setProductName(data.getProductName());
				payResult.setExtension(data.getExtension());
				U8SDK.getInstance().onPayResult(payResult);
			}

			@Override
			public void payFail() {
				// TODO 支付失败
				U8SDK.getInstance().onResult(U8Code.CODE_PAY_FAIL, "asyx sdk pay failed.");
			}
		});
	}
}
