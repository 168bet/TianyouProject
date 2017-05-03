package com.tianyou.channel.business;

import android.app.Activity;
import android.widget.Toast;

import cn.beecloud.dankal.Lequ;
import cn.beecloud.dankal.callback.LoginCallback;
import cn.beecloud.dankal.callback.RechargeResultCallBack;
import cn.beecloud.dankal.callback.RegisterGenerateCallBack;
import cn.beecloud.dankal.callback.RegisterPhoneCallBack;
import cn.beecloud.dankal.callback.ValidateAppIdCallBack;
import cn.beecloud.dankal.model.AccountInfo;
import cn.beecloud.dankal.model.GameInfo;
import cn.beecloud.dankal.model.PayInfo;

import com.tianyou.channel.bean.LoginInfo;
import com.tianyou.channel.bean.PayParam;
import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.tianyou.channel.utils.LogUtils;

public class DouquSdkService extends BaseSdkService{

	@Override
	public void doActivityInit(Activity activity,
			TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		
		GameInfo gameInfo = new GameInfo();
		gameInfo.setGameId("10874");
//		gameInfo.setGameKey("");
//		gameInfo.setPartnerId("");
		Lequ.initSdk(gameInfo, mActivity, new ValidateAppIdCallBack() {
			
			@Override
			public void validateSuccess(GameInfo info) {
				LogUtils.d("SDK初始化完成");
				mTianyouCallback.onResult(TianyouCallback.CODE_INIT, "SDK初始化完成");
			}
			
			@Override
			public void validateFail(int errorCode, String msg) {
				LogUtils.d("SDK初始化失败...");
			}
		});
	}
	
	@Override
	public void doLogin() {
		super.doLogin();
		Lequ.login(new cn.beecloud.dankal.callback.LoginCallback() {
			
			@Override
			public void loginSuccess(final AccountInfo accountInfo) {
				LogUtils.d("login success account= "+accountInfo.getAccount()+",accountId= "+accountInfo.getAccountId()+
						",fromId= "+accountInfo.getFromid()+",key= "+accountInfo.getKey()+",nickName= "+accountInfo.getNickName()+
						",pwd= "+accountInfo.getPassword()+",phone= "+accountInfo.getPhone());
				LoginInfo param = new LoginInfo();
				param.setChannelUserId(accountInfo.getAccountId());
				param.setUserToken(accountInfo.getAccountId());
				checkLogin(param);
			}
			
			@Override
			public void loginFail(int errorCode, String msg) {
				LogUtils.d("login failed errorCode= "+errorCode+",msg= "+msg);
				mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, msg);
			}
		}, mActivity);
	}
	
	@Override
	public void doChannelPay(PayParam payInfo, final OrderinfoBean orderInfo) {
		super.doChannelPay(payInfo, orderInfo);
		PayInfo lqPayInfo = new PayInfo();
		lqPayInfo.setOrderName(orderInfo.getProduct_name());
		lqPayInfo.setOrderFee(Float.parseFloat(orderInfo.getMoNey()));
		lqPayInfo.setOrderNo(orderInfo.getOrderID());
		Lequ.recharge(lqPayInfo, mActivity, new RechargeResultCallBack() {
			
			@Override
			public void onUserCancel(PayInfo info) {
				LogUtils.d("pay cancle orderName= "+info.getOrderName()+",orderFee= "+info.getOrderFee()+",orderNo= "+info.getOrderNo());
				mTianyouCallback.onResult(TianyouCallback.CODE_PAY_CANCEL, "支付取消");
			}
			
			@Override
			public void onSuccess(PayInfo info) {
				LogUtils.d("pay success orderName= "+info.getOrderName()+",orderFee= "+info.getOrderFee()+",orderNo= "+info.getOrderNo());
				checkOrder(orderInfo.getOrderID());
			}
			
			@Override
			public void onFailure(PayInfo info) {
				LogUtils.d("pay failed orderName= "+info.getOrderName()+",orderFee= "+info.getOrderFee()+",orderNo= "+info.getOrderNo());
				mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, "支付失败");
			}
		}, orderInfo.getOrderID());
	}
	
	@Override
	public void doRegisterGenerate() {
		super.doRegisterGenerate();
		Lequ.register_generate(mActivity, new RegisterGenerateCallBack() {
			
			@Override
			public void registerGenerateSuccess() {
				LogUtils.d("register genetate success---------");
				doLogin();
			}
			
			@Override
			public void registerGenerateFail(String errorCode, String msg) {
				LogUtils.d("register generate failed errorCode= "+errorCode+",msg= "+msg);
				mActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(mActivity, "一键注册失败", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}
	
	@Override
	public void doRegisterPhone() {
		super.doRegisterPhone();
		Lequ.register_phone(mActivity, new RegisterPhoneCallBack() {
			
			@Override
			public void sendSMSOrRegisterPhoneSuccess(String phone, String password, String type) {
				LogUtils.d("register phone success phone= "+phone+",pwd= "+password+",type= "+type);
				doLogin();
			}
			
			@Override
			public void sendSMSOrRegisterPhoneFail(String errorCode, String msg) {
				LogUtils.d("register phone faield errorCode= "+errorCode+",msg= "+msg);
				mActivity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						Toast.makeText(mActivity, "手机号注册失败", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}
}
