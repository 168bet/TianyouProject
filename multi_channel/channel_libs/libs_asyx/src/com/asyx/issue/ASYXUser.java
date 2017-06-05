package com.asyx.issue;

import android.app.Activity;

import com.u8.sdk.IUser;
import com.u8.sdk.U8SDK;
import com.u8.sdk.UserExtraData;
import com.u8.sdk.utils.Arrays;

public class ASYXUser implements IUser {
	private Activity context;

	private String[] supportedMethods = { "login", "switchLogin", "submitExtraData", "logout", "exit" };

	/**
	 * 必须定义包含一个Activity参数的构造函数，否则实例化的时候，会失败
	 * 
	 * 一般SDK初始化方法的调用也在这里调用。除非SDK要求要在Application对应的方法调用。
	 * 
	 * @param context
	 */
	public ASYXUser(Activity context) {
		this.context = context;
		ASYXSDK.getInstance().initSDK(U8SDK.getInstance().getSDKParams());
	}

	/**
	 * 判断当前插件是否支持接口中定义的方法
	 */
	@Override
	public boolean isSupportMethod(String methodName) {
		// TODO Auto-generated method stub
		return Arrays.contain(supportedMethods, methodName);
	}

	@Override
	public void login() {
		// TODO Auto-generated method stub
		ASYXSDK.getInstance().login(context);
	}

	@Override
	public void loginCustom(String customData) {
		// TODO 此方法弃用

	}

	@Override
	public void switchLogin() {
		// TODO Auto-generated method stub
		ASYXSDK.getInstance().switchLogin(context);
	}

	@Override
	public boolean showAccountCenter() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void logout() {
		// TODO Auto-generated method stub
		ASYXSDK.getInstance().logout();
	}

	@Override
	public void submitExtraData(UserExtraData extraData) {
		// TODO Auto-generated method stub
		ASYXSDK.getInstance().submitGameData(extraData);
	}

	@Override
	public void exit() {
		// TODO Auto-generated method stub
		ASYXSDK.getInstance().exit(context);
	}

	@Override
	public void postGiftCode(String code) {
		// TODO Auto-generated method stub

	}

	@Override
	public void realNameRegister() {
		// TODO Auto-generated method stub

	}

	@Override
	public void queryAntiAddiction() {
		// TODO Auto-generated method stub

	}

}
