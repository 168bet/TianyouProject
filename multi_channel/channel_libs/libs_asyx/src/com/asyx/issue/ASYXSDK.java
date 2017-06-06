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
		// TODO::�������SDK��ʼ������
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
		// TODO::������ð�����Ϸ�ĵ�¼����
		ASGameSDK.login(cpID, gameID, md5Key, context, new LoginResultListener() {

			@Override
			public void loginSuccess(String userID, String username) {
				// TODO ��¼�ɹ�
				ASYXSDK.instance.userId = userID;
				ASYXSDK.instance.username = username;
				U8SDK.getInstance().onResult(U8Code.CODE_LOGIN_SUCCESS, userID);
				U8SDK.getInstance().onLoginResult(userID);
			}

			@Override
			public void loginFail() {
				// TODO ��¼ʧ��
				U8SDK.getInstance().onResult(U8Code.CODE_LOGIN_FAIL, "asyx sdk login failed");
			}
		});
	}

	public void switchLogin(Activity context) {
		// TODO::������ð�����Ϸ�л��ʺŵķ���
		// ���û���ṩ�л��ʺŵķ�������ô�л��ʺŵ��߼�����[�ȵǳ����ٵ�¼]��Ҳ�����ȵ���logout���ٵ���login
		U8SDK.getInstance().onLogout();
	}

	public void logout() {
		// TODO::���ð�����Ϸ�ĵǳ�����
		U8SDK.getInstance().onLogout();
	}

	public void showUserCenter() {
		// TODO::���ð�����Ϸ��ʾ�������ĵķ���
		// ���������Ϸû���ṩ��Ӧ�Ľӿڣ�����ʵ�ָ÷���
	}

	public void exit(final Activity context) {
		// TODO::���ð�����Ϸ��ʾ�˳�ȷ�Ͽ�ӿ�
		// ���������Ϸû���ṩ��Ӧ�Ľӿڣ�����ʵ�ָ÷���
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
		// TODO::���ð�����Ϸ�ϱ�������ݽӿ�
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
		// TODO::���ð�����Ϸ��ֵ�ӿ�
		if (userId == null || username == null) {
			U8SDK.getInstance().onResult(U8Code.CODE_PAY_FAIL, "���ȵ�¼����֧����");
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
				// TODO ֧���ɹ�
				U8SDK.getInstance().onResult(U8Code.CODE_PAY_SUCCESS, "asyx sdk pay success.");
				PayResult payResult = new PayResult();
				payResult.setProductID(data.getProductId());
				payResult.setProductName(data.getProductName());
				payResult.setExtension(data.getExtension());
				U8SDK.getInstance().onPayResult(payResult);
			}

			@Override
			public void payFail() {
				// TODO ֧��ʧ��
				U8SDK.getInstance().onResult(U8Code.CODE_PAY_FAIL, "asyx sdk pay failed.");
			}
		});
	}
}
