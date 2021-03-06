package com.tianyou.sdk.demo;

import com.tianyou.sdk.bean.PayInfo;
import com.tianyou.sdk.bean.RoleInfo;
import com.tianyou.sdk.holder.ConfigHolder;
import com.tianyou.sdk.interfaces.TianyouCallback;
import com.tianyou.sdk.interfaces.TianyouSdk;
import com.tianyou.sdk.utils.LogUtils;
import com.tianyou.sdk.utils.ToastUtils;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener {

	private Activity mActivity;
	private TianyouCallback mTianyouCallback = new TianyouCallback() {
		@Override
		public void onResult(int code, String msg) {
			switch (code) {
			case TianyouCallback.CODE_LOGIN_SUCCESS:
				// 登陆成功返回uid+token的json串，解析即可获取uid和token数据，格式：{"uid":47814,"userToken":"fhadklfa234"}
				ToastUtils.show(mActivity, "登录成功：uid=" + msg);
				break;
			case TianyouCallback.CODE_LOGIN_FAILED:
				LogUtils.d("登录失败：" + msg);
				break;
			case TianyouCallback.CODE_LOGIN_CANCEL:
				ToastUtils.show(mActivity, "登录取消：" + msg);
				break;
			case TianyouCallback.CODE_LOGOUT:
				ToastUtils.show(mActivity, "注销：" + msg);
				break;
			case TianyouCallback.CODE_PAY_SUCCESS:
				ToastUtils.show(mActivity, "支付成功：" + msg);
				break;
			case TianyouCallback.CODE_PAY_FAILED:
				ToastUtils.show(mActivity, "支付失败：" + msg);
				break;
			case TianyouCallback.CODE_PAY_CANCEL:
				ToastUtils.show(mActivity, "支付取消：" + msg);
				break;
			case TianyouCallback.CODE_QUIT_SUCCESS:
				finish();
				android.os.Process.killProcess(android.os.Process.myPid());
				break;
			case TianyouCallback.CODE_QUIT_CANCEL:
				ToastUtils.show(mActivity, "退出游戏取消：" + msg);
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mActivity = this;
		findViewById(R.id.btn_login).setOnClickListener(this);
		findViewById(R.id.btn_pay).setOnClickListener(this);
		findViewById(R.id.btn_pay_1).setOnClickListener(this);
		findViewById(R.id.btn_create_role).setOnClickListener(this);
		findViewById(R.id.btn_switch).setOnClickListener(this);
		findViewById(R.id.btn_update_role_info).setOnClickListener(this);
		findViewById(R.id.btn_exit_game).setOnClickListener(this);
		TianyouSdk.getInstance().activityInit(this, mTianyouCallback);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			TianyouSdk.getInstance().login();
			break;
		case R.id.btn_create_role:
			doCreateRoleInfo();
			break;
		case R.id.btn_update_role_info:
			doUpdateRoleInfo();
			break;
		case R.id.btn_pay:
			TianyouSdk.getInstance().pay(getPayInfo(), true);
			break;
		case R.id.btn_pay_1:
			TianyouSdk.getInstance().pay(getPayInfo());
			break;
		case R.id.btn_switch:
			ConfigHolder.isLandscape = !ConfigHolder.isLandscape;
			setRequestedOrientation(ConfigHolder.isLandscape ? ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE 
					: ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
			break;
		case R.id.btn_exit_game:
			TianyouSdk.getInstance().exitGame();
			break;
		}
	}
	
	private void doUpdateRoleInfo() {
		RoleInfo roleInfo = new RoleInfo();
		roleInfo.setServerId("99990");
		roleInfo.setServerName("测试一区");
		roleInfo.setRoleId("13141654");
		roleInfo.setRoleName("Jack");
		roleInfo.setProfession("法师");
		roleInfo.setLevel("50");
		roleInfo.setSociaty("阴阳寮");
		roleInfo.setVipLevel("3");
		roleInfo.setAmount("1");
		TianyouSdk.getInstance().updateRoleInfo(roleInfo);
	}

	private void doCreateRoleInfo() {
		RoleInfo roleInfo = new RoleInfo();
		roleInfo.setRoleId("13141654");
		roleInfo.setRoleName("Jack");
		roleInfo.setServerId("99990");
		roleInfo.setServerName("测试一区");
		roleInfo.setProfession("法师");
		roleInfo.setLevel("50");
		roleInfo.setSociaty("阴阳寮");
		TianyouSdk.getInstance().createRole(roleInfo);
	}

	private PayInfo getPayInfo() {
		PayInfo payInfo = new PayInfo();
		payInfo.setRoleId("13141654");
		payInfo.setMoney("1");
		payInfo.setServerId("10281");
		payInfo.setServerName("测试一区");
		payInfo.setProductId("scom.tianyouxi.skszj.p1");
		payInfo.setProductName("10金钻");
		payInfo.setCustomInfo("21689575c5284a334ca8f6630127915f9058");
		payInfo.setGameName("寻龙剑");
		return payInfo;
	}

	@Override
	public void onBackPressed() {
		TianyouSdk.getInstance().exitGame();
	}
}
