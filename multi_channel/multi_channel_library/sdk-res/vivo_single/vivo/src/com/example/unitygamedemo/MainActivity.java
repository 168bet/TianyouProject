package com.example.unitygamedemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

import com.tianyou.channel.bean.UserRoleInfo;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.tianyou.channel.interfaces.TianyouSdk;
import com.tianyou.channel.utils.LogUtils;
import com.tianyou.channel.utils.ToastUtils;
import com.tianyouxi.lszg.vivo.R;

public class MainActivity extends Activity implements OnClickListener {

	private Activity mActivity;
	private BaseSdkService mTianyouSdk;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		this.mActivity = this;
		findViewById(R.id.btn_main_login).setOnClickListener(this);
		findViewById(R.id.btn_main_logout).setOnClickListener(this);
		findViewById(R.id.btn_main_pay).setOnClickListener(this);
		findViewById(R.id.btn_main_pay_1).setOnClickListener(this);
		findViewById(R.id.btn_main_get_user_info).setOnClickListener(this);
		findViewById(R.id.btn_main_send_role_info).setOnClickListener(this);
		findViewById(R.id.btn_main_exit).setOnClickListener(this);
		findViewById(R.id.btn_main_submit_extend_data).setOnClickListener(this);
		mTianyouSdk = TianyouSdk.getInstance(mActivity);
		mTianyouSdk.doActivityInit(mActivity, mTianyouCallback);
		String channelName = TianyouSdk.getChannelName(this);
		LogUtils.d("平台名称：" + channelName);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_main_login:
			mTianyouSdk.doLogin();
			break;
		case R.id.btn_main_logout:
			mTianyouSdk.doLogout();
			break;
		case R.id.btn_main_pay:
			sendRoleInfo();
			mTianyouSdk.doPay();
			break;
		case R.id.btn_main_pay_1:
			sendRoleInfo();
			mTianyouSdk.doPay("tyxmulti_qihoo_01");
			break;
		case R.id.btn_main_get_user_info:
			mTianyouSdk.doGetTokenInfo();
			break;
		case R.id.btn_main_send_role_info:
			sendRoleInfo();
			break;
		case R.id.btn_main_exit:
			mTianyouSdk.doExitGame();
			break;
		}
	}
	
	private TianyouCallback mTianyouCallback = new TianyouCallback() {
		@Override
		public void onResult(int code, String msg) {
			switch (code) {
			case TianyouCallback.CODE_INIT:
				ToastUtils.showToast(mActivity, "初始化：" + msg);
				break;
			case TianyouCallback.CODE_LOGIN_SUCCESS:
				ToastUtils.showToast(mActivity, "登录成功：uid=" + msg);
				break;
			case TianyouCallback.CODE_LOGIN_FAILED:
				ToastUtils.showToast(mActivity, "登录失败：" + msg);
				break;
			case TianyouCallback.CODE_LOGIN_CANCEL:
				ToastUtils.showToast(mActivity, "登录取消：" + msg);
				break;
			case TianyouCallback.CODE_LOGOUT:
				ToastUtils.showToast(mActivity, "注销：" + msg);
				break;
			case TianyouCallback.CODE_PAY_SUCCESS:
				ToastUtils.showToast(mActivity, "支付成功：" + msg);
				break;
			case TianyouCallback.CODE_PAY_FAILED:
				ToastUtils.showToast(mActivity, "支付失败：" + msg);
				break;
			case TianyouCallback.CODE_PAY_CANCEL:
				ToastUtils.showToast(mActivity, "支付取消：" + msg);
				break;
			case TianyouCallback.CODE_QUIT_SUCCESS:
				ToastUtils.showToast(mActivity, "退出游戏：" + msg);
				break;
			case TianyouCallback.CODE_QUIT_CANCEL:
				ToastUtils.showToast(mActivity, "退出游戏取消：" + msg);
				break;
			}
		}
	};
	
	private void sendRoleInfo() {
		UserRoleInfo roleInfo = new UserRoleInfo();
		roleInfo.setBalance("100");
		roleInfo.setProfession("法师");
		roleInfo.setRoleId("13141654");
		roleInfo.setServerId("1");
		roleInfo.setServerName("国内Android测试服");
		roleInfo.setCustomInfo("21689575c5284a334ca8f6630127915f9058");
		roleInfo.setGameName("剑与魔法");
		roleInfo.setParty("五号特工组");
		roleInfo.setRoleLevel("80");
		roleInfo.setRoleName("卢锡安");
		roleInfo.setVipLevel("12");
		roleInfo.setCustomInfo("13465897");
		roleInfo.setBuyAmount("1");
		mTianyouSdk.doUploadRoleInfo(roleInfo);
	}

	@Override
	protected void onResume() {
		mTianyouSdk.doResume();
		super.onResume();
	};

	@Override
	protected void onPause() {
		mTianyouSdk.doPause();
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		mTianyouSdk.doStop();
		super.onStop();
	}
	
	@Override
	protected void onRestart() {
		mTianyouSdk.doRestart();
		super.onRestart();
	}
	
	@Override
	protected void onStart() {
		mTianyouSdk.doStart();
		super.onStart();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		mTianyouSdk.doNewIntent(intent);
		super.onNewIntent(intent);
	}
	
	@Override
	protected void onDestroy() {
		mTianyouSdk.doDestory();
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mTianyouSdk.doActivityResult(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}
}
