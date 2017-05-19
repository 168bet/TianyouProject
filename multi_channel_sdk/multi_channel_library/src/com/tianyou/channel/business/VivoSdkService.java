package com.tianyou.channel.business;

import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.bean.RoleInfo;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.vivo.unionsdk.open.VivoExitCallback;
import com.vivo.unionsdk.open.VivoUnionSDK;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

public class VivoSdkService extends BaseSdkService {

	@Override
	public void doActivityInit(Activity activity, TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		VivoUnionSDK.initSdk(mActivity, mChannelInfo.getAppId(), false);
		
	}

	@Override
	public void doExitGame() {
		VivoUnionSDK.exit(mActivity, new VivoExitCallback() {
			@Override
			public void onExitConfirm() {
				mTianyouCallback.onResult(TianyouCallback.CODE_QUIT_SUCCESS, "");
			}
			
			@Override
			public void onExitCancel() {
				mTianyouCallback.onResult(TianyouCallback.CODE_QUIT_CANCEL, "");
			}
		});
	}

	@Override
	public void doUploadRoleInfo(RoleInfo roleInfo) {
		super.doUploadRoleInfo(roleInfo);
		VivoUnionManager.vivoAccountreportRoleInfo(mRoleInfo.getRoleId(), mRoleInfo.getRoleLevel(),
				mRoleInfo.getServerId(), mRoleInfo.getRoleName(), mActivity, mRoleInfo.getServerName());
	}

	@Override
	public void doLogin() {
		mUnionManager.startLogin(appID);
	}

	@Override
	public void doChannelPay(OrderinfoBean orderInfo) {
		super.doChannelPay(orderInfo);
		String orderID = orderInfo.getOrderID();
		mPayReslutListener = new MyPayReslutListener(orderID);
		mUnionManager.initVivoPaymentAndRecharge(mActivity, mPayReslutListener);
		Bundle localBundle = new Bundle();
		localBundle.putString("transNo", orderInfo.getOrderNumber()); // 订单推送接口返回的vivo订单号
		localBundle.putString("accessKey", orderInfo.getAccessKey()); // 订单推送接口返回的accessKey
		localBundle.putString("appId", appID); // 在vivo开发者平台注册应用后获取到的appId
		localBundle.putString("productName", mPayInfo.getProductName()); // 商品名称
		localBundle.putString("productDes", mPayInfo.getProductDesc());// 商品描述
		localBundle.putLong("price", Long.parseLong(orderInfo.getMoNey()));// 商品价格，单位为分（1000即10.00元）
		Log.d("TAG", "vivo pay param= " + localBundle);

		// 以下为可选参数，能收集到务必填写，如未填写，掉单、用户密码找回等问题可能无法解决。
		localBundle.putString("blance", mRoleInfo.getBalance());
		localBundle.putString("vip", mRoleInfo.getVipLevel());
		localBundle.putInt("level", Integer.parseInt(mRoleInfo.getRoleLevel()));
		localBundle.putString("party", mRoleInfo.getParty());
		localBundle.putString("roleId", mRoleInfo.getRoleId());
		localBundle.putString("roleName", mRoleInfo.getRoleName());
		localBundle.putString("serverName", mRoleInfo.getServerName());
		localBundle.putString("extInfo", mRoleInfo.getCustomInfo());
		localBundle.putBoolean("logOnOff", false);
		mUnionManager.payment(mActivity, localBundle);
	}

	@Override
	public void doResume() {
		mUnionManager.showVivoAssitView(mActivity);
	}

	@Override
	public void doDestory() {
		mUnionManager.unRegistVivoAccountChangeListener(mAccountListener);
		if (mPayReslutListener != null) {
			mUnionManager.cancelVivoPaymentAndRecharge(mPayReslutListener);
		}
		mUnionManager.hideVivoAssitView(mActivity);
	}

	// 监听登录的接口
	private class MyAccoutChangeListener implements OnVivoAccountChangedListener {

		@Override
		public void onAccountLogin(String name, String openid, String authtoken) {

			checkLogin(openid, authtoken);

			mActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					mUnionManager.showVivoAssitView(mActivity);
				} // 登录成功后显示悬浮框
			});
		}

		@Override
		public void onAccountLoginCancled() {
			mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_CANCEL, "取消登录");
		}

		@Override
		public void onAccountRemove(boolean isRemoved) {
			Log.d("TAG", "account remove-------------");
		}
	}

	// 监听支付的接口
	private class MyPayReslutListener implements OnVivoPayResultListener {

		private String orderID;

		public MyPayReslutListener(String orderID) {
			this.orderID = orderID;
		}

		@Override
		public void payResult(String transNo, boolean pay_result, String result_code, String pay_msg) {

			String msg = "payResult: transNo= " + transNo + ",pay_result= " + pay_result + ",result_code= "
					+ result_code + ",pay_msg= " + pay_msg;
			if ("9000".equals(result_code)) {
				checkOrder(orderID);
			}

			else if ("6001".equals(result_code)) {
				mTianyouCallback.onResult(TianyouCallback.CODE_PAY_CANCEL, "支付取消");
			}

			else {
				mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, "支付失败");
			}
		}

		/**
		 * 注：cp方不需要使用此回调方法
		 */
		@Override
		public void rechargeResult(String openid, boolean pay_result, String result_code, String pay_msg) {
			Log.d("TAG", "rechargeResult: openid= " + openid + ",pay_result= " + pay_result + ",result_code= "
					+ result_code + ",pay_msg= " + pay_msg);
		}
	}

}
