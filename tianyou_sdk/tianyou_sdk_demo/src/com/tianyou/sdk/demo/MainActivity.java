package com.tianyou.sdk.demo;

import org.json.JSONException;
import org.json.JSONObject;

import com.tianyou.sdk.holder.ConfigHolder;
import com.tianyou.sdk.interfaces.TianyouCallback;
import com.tianyou.sdk.interfaces.Tianyouxi;
import com.tianyou.sdk.utils.AppUtils;
import com.tianyou.sdk.utils.ToastUtils;
import com.tianyouxi.lszg.bm.R;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener {

	private Activity mActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mActivity = this;
		findViewById(R.id.btn_login).setOnClickListener(this);
		findViewById(R.id.btn_pay).setOnClickListener(this);
		findViewById(R.id.btn_pay_1).setOnClickListener(this);
		findViewById(R.id.btn_entry_game).setOnClickListener(this);
		findViewById(R.id.btn_create_role).setOnClickListener(this);
		findViewById(R.id.btn_switch).setOnClickListener(this);
		Tianyouxi.doInitActivity(this, mTianyouCallback);
	}
	
	private TianyouCallback mTianyouCallback = new TianyouCallback() {
		@Override
		public void onResult(int code, String msg) {
			switch (code) {
			case TianyouCallback.CODE_INIT:
				ToastUtils.show(mActivity, "初始化：" + msg);
				break;
			case TianyouCallback.CODE_LOGIN_SUCCESS:
				ToastUtils.show(mActivity, "登录成功：uid=" + msg);
				break;
			case TianyouCallback.CODE_LOGIN_FAILED:
				ToastUtils.show(mActivity, "登录失败：" + msg);
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			doLogin();
			break;
		case R.id.btn_pay:
			Tianyouxi.pay(getPayParam());
			break;
		case R.id.btn_pay_1:
			Tianyouxi.pay(getPayParam(), 15, "超值大礼包");
			break;
		case R.id.btn_entry_game:
			doEntryGame();
			break;
		case R.id.btn_create_role:
			doCreateRole();
			break;
		case R.id.btn_switch:
			ConfigHolder.isLandscape = !ConfigHolder.isLandscape;
			setRequestedOrientation(ConfigHolder.isLandscape ? ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE 
					: ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
			break;
		}
	}
	
	private String getPayParam() {
		try {
			JSONObject payInfo = new JSONObject();
			String roleId = "13141654";
			String serverId = "10281";
			payInfo.put("roleId", roleId);
			payInfo.put("serverId", serverId);
			payInfo.put("serverName", "国内Android测试服");
			payInfo.put("customInfo", "21689575c5284a334ca8f6630127915f9058");
			payInfo.put("productId", "scom.tianyouxi.skszj.p1");
			payInfo.put("productName", "60金钻");
			payInfo.put("gameName", "寻龙剑");
			payInfo.put("sign", AppUtils.MD5(roleId + serverId));
			payInfo.put("signType", "MD5");
			return payInfo.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void doCreateRole() {
		try {
			JSONObject roleInfo = new JSONObject();
			roleInfo.put("roleId", "1000");
			roleInfo.put("roleName", "tom");
			roleInfo.put("serverId", "1000");
			roleInfo.put("serverName", "sName");
			roleInfo.put("profession", "剑圣");
			Tianyouxi.createRole(mActivity, roleInfo.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void doEntryGame() {
		try {
			JSONObject roleInfo = new JSONObject();
			roleInfo.put("roleId", "1000");
			roleInfo.put("roleLevel", "100");
			roleInfo.put("serverId", "1000");
			roleInfo.put("serverName", "sName");
			roleInfo.put("vipLevel", "100");
			Tianyouxi.enterGame(mActivity, roleInfo.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private void doLogin() {
		Tianyouxi.login("龙神捕鱼");
	}
	
	@Override
	public void onBackPressed() {
		Tianyouxi.exitGame();
	}
}
