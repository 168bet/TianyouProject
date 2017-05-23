package com.tianyou.channel.business;

import java.util.HashMap;

import com.le.accountoauth.utils.LeUserInfo;
import com.le.legamesdk.LeGameSDK;
import com.le.legamesdk.LeGameSDK.ActionCallBack;
import com.le.legamesdk.LeGameSDK.ExitCallback;
import com.le.legamesdk.LeGameSDK.PayCallback;
import com.letv.lepaysdk.smart.LePayInfo;
import com.tianyou.channel.bean.LoginInfo;
import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.bean.PayParam;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.tianyou.channel.utils.LogUtils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

public class LeshiSdkService extends BaseSdkService{
	
	private LeGameSDK mLeGameSDK;
	private String sid;
	private String session;
	
	@Override
	public void doApplicationCreate(Context context, boolean island) {
		super.doApplicationCreate(context, island);
		LeGameSDK.init(context);
	}
	
	@Override
	public void doActivityInit(Activity activity, TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		mLeGameSDK = LeGameSDK.getInstance();
		mLeGameSDK.onCreate(mActivity, new ActionCallBack() {
			@Override
			public void onExitApplication() {mActivity.finish();}
		});
		mTianyouCallback.onResult(TianyouCallback.CODE_INIT, "SDK初始化完成");
	}

	@Override
	public void doLogin() {
		mLeGameSDK.doLogin(mActivity, mLeLoginCallback,false);
	}
	
	@Override
	public void doLogout() {
		super.doLogout();
		mTianyouCallback.onResult(TianyouCallback.CODE_LOGOUT, "");
		mLeGameSDK.doLogin(mActivity, mLeLoginCallback, true);
	}
	
	@Override
	public void doChannelPay(PayParam payInfo, final OrderinfoBean orderInfo) {
		super.doChannelPay(payInfo, orderInfo);
		final LePayInfo lePayInfo = new LePayInfo();
		lePayInfo.setLetv_user_access_token(session);
		LogUtils.d("sid:" + sid);
		lePayInfo.setLetv_user_id(sid);// 乐视集团用户id
		lePayInfo.setNotify_url(orderInfo.getNotifyurl());// 支付结果回调地址
		Log.d("TAG", "orderID= "+orderInfo.getOrderID());
		lePayInfo.setCooperator_order_no(orderInfo.getOrderID());// 商户订单号,生成随机数，由cp定义，唯一；
		lePayInfo.setPrice("0.01");// 产品价格
		lePayInfo.setProduct_name(mPayInfo.getProductName());// 商品名称
		lePayInfo.setProduct_desc(mPayInfo.getProductDesc());// 商品描述
		lePayInfo.setPay_expire(orderInfo.getDeadline());// 支付结束期限
		LogUtils.d("orderInfo.getDeadline():" + orderInfo.getDeadline());
		lePayInfo.setProduct_id(mPayInfo.getProductId());// 商品id
		lePayInfo.setCurrency("RMB");// 货币种类
		lePayInfo.setExtro_info(payInfo.getCustomInfo());// cp 自定义参数
		Log.d("TAG", "Letv_access_token= "+lePayInfo.getLetv_user_access_token()+",Letv_userid= "+lePayInfo.getLetv_user_id()+
				",notify_url= "+lePayInfo.getNotify_url()+",cooperator_order_no= "+lePayInfo.getCooperator_order_no()
				+",price= "+lePayInfo.getPrice()+",product_name= "+lePayInfo.getProduct_name()+",product_desc= "+lePayInfo.getProduct_desc()
				+",pay_expire= "+lePayInfo.getPay_expire()+",product_id= "+lePayInfo.getProduct_id()+",currency= "+lePayInfo.getCurrency()
				+",extro_info= "+lePayInfo.getExtro_info());
		mActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mLeGameSDK.doPay(mActivity, lePayInfo, new PayCallback() {
					@Override
					public void onPayResult(String status, String msg) {
						if (status.equals("SUCCESS") || status.equals("PAYED")) {
							checkOrder(orderInfo.getOrderID());
						} else if (status.equals("CANCEL")) {
							mTianyouCallback.onResult(TianyouCallback.CODE_PAY_CANCEL, "");
						} else {
							mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, "");
						}
					}
				});
			}
		});
	}
	
	private com.le.legamesdk.LeGameSDK.LoginCallback mLeLoginCallback = new com.le.legamesdk.LeGameSDK.LoginCallback(){

		@Override
		public void onLoginSuccess(LeUserInfo leUserInfo) {
			if (leUserInfo != null) {
				LoginInfo loginParam = new LoginInfo();
				loginParam.setChannelUserId(leUserInfo.getUserId());
				loginParam.setUserToken(leUserInfo.getAccessToken());
				checkLogin(loginParam);
			} else {
				mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED,"登录失败");
			}
		}
		
		@Override
		public void onLoginFailure(int errorCode, String errorMsg) {
			mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, "登录失败");
		}
		
		@Override
		public void onLoginCancel() {
			mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_CANCEL, "取消登录");
		}
	};
	
	@Override
	public void doResume() {
		mLeGameSDK.onResume(mActivity);
	}
	
	@Override
	public void doPause() {
		mLeGameSDK.onPause(mActivity);
	}
	
	@Override
	public void doDestory() {
		mLeGameSDK.onDestory(mActivity);
		mLeGameSDK = null;
	}
	
	@Override
	public void doExitGame() {
		mLeGameSDK.onExit(mActivity,new ExitCallback() {
			@Override
			public void onSdkExitConfirmed() {
				mTianyouCallback.onResult(TianyouCallback.CODE_QUIT_SUCCESS, "用户退出游戏成功");
			}
			
			@Override
			public void onSdkExitCanceled() {
				mTianyouCallback.onResult(TianyouCallback.CODE_QUIT_CANCEL, "用户退出游戏成功");
			}
		});
	}
	
}
