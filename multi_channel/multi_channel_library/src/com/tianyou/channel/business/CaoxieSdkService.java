package com.tianyou.channel.business;

import com.mchsdk.open.GPExitResult;
import com.mchsdk.open.GPSDKInitResult;
import com.mchsdk.open.GPUserResult;
import com.mchsdk.open.IGPExitObsv;
import com.mchsdk.open.IGPSDKInitObsv;
import com.mchsdk.open.IGPUserObsv;
import com.mchsdk.open.MCApiFactory;
import com.mchsdk.open.OrderInfo;
import com.mchsdk.open.PayCallback;
import com.tianyou.channel.bean.LoginInfo;
import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.bean.PayParam;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.tianyou.channel.utils.LogUtils;
import com.tianyou.channel.utils.ToastUtils;

import android.app.Activity;

public class CaoxieSdkService extends BaseSdkService {

	@Override
	public void doActivityInit(Activity activity, TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		MCApiFactory.getMCApi().initParams("0"// 渠道id
				, "自然注册"// 渠道名称
				, "60"// 游戏id
				, "龙之神域"// 游戏名称
				, "4D78838504730B105");// 游戏appid
		MCApiFactory.getMCApi().init(activity, new IGPSDKInitObsv() {
			@Override
			public void onInitFinish(int initResult) {
				switch (initResult) {
				case GPSDKInitResult.GPInitErrorCodeConfig:// 配置错误
	                ToastUtils.show(mActivity, "配置错误");
					break;
				case GPSDKInitResult.GPInitErrorCodeNet:// 网络不可用
					ToastUtils.show(mActivity, "网络不可用");
					break;
				case GPSDKInitResult.GPInitSuccess:// 初始化成功
					mTianyouCallback.onResult(TianyouCallback.CODE_INIT, "");
					break;
				}
			}
		});
	}
	
	@Override
	public void doLogin() {
		super.doLogin();
		MCApiFactory.getMCApi().startlogin(mActivity, mIgpUserObsv);
	}
	
	private IGPUserObsv mIgpUserObsv = new IGPUserObsv() {
		@Override
		public void onFinish(GPUserResult result) {
			switch (result.getmErrCode()) {
			case GPUserResult.USER_RESULT_LOGIN_FAIL://登录失败回调
				mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, "登录回调:登录失败");
				break;
			case GPUserResult.USER_RESULT_LOGIN_SUCC://登录成功回调
				/** 必须登录之后开启悬浮窗 */
				MCApiFactory.getMCApi().startFloating(mActivity);
				LoginInfo loginInfo = new LoginInfo();
				loginInfo.setChannelUserId(result.getAccountNo());
				loginInfo.setUserToken(result.getToken());
				checkLogin(loginInfo);
				break;
			}
		}
	};
	
	@Override
	public void doChannelPay(PayParam payInfo, final OrderinfoBean orderInfo) {
		super.doChannelPay(payInfo, orderInfo);
	    OrderInfo info = new OrderInfo();
		info.setAmount(Integer.parseInt(orderInfo.getMoNey()) * 100);/**切记== 物品价格,人民币单位（分）*/
		info.setExtendInfo(orderInfo.getOrderID()); /** 用于确认交易给玩家发送商品*/
		info.setProductDesc(orderInfo.getProductDesc()); /** 物品描述*/
		info.setProductName(orderInfo.getProductName());/** 物品名称*/
		/** 显示支付窗口 */
		MCApiFactory.getMCApi().pay(mActivity, info, new PayCallback() {
			@Override
			public void callback(String result) {
				LogUtils.d("result:" + result);
				try {
					result = result.substring(0, result.indexOf("."));
				} catch (Exception e) {
				}
				// 支付成功 100
				// 支付失败 -100
				// 支付取消 0
				// 未获取到支付结果 6004
				// 重复请求 5000
				// 正在确认支付结果 8000
				// 网络连接出错 6002
				// 认证被否决 -4
				// 发送失败 -3
				// 不支持错误 -5
				if (result.equals(100)) {
					checkOrder(orderInfo.getOrderID());
				}
			}
		});
	}
	
	@Override
	public boolean isShowExitGame() {
		return true;
	}
	
	@Override
	public void doExitGame() {
		MCApiFactory.getMCApi().exit(mActivity, new IGPExitObsv() {
			@Override
			public void onExitFinish(GPExitResult exitResult) {
				switch (exitResult.mResultCode) {
				case GPExitResult.GPSDKExitResultCodeError:
					mTianyouCallback.onResult(TianyouCallback.CODE_QUIT_CANCEL, "");
					break;
				case GPExitResult.GPSDKExitResultCodeExitGame:
					MCApiFactory.getMCApi().stopFloating(mActivity);
					mTianyouCallback.onResult(TianyouCallback.CODE_QUIT_SUCCESS, "");
				}
			}
		}, mIgpUserObsv);
	}
	
	@Override
	public void doResume() {
		super.doResume();
		/** 在此注册此接口，获取支付结果 */
		MCApiFactory.getMCApi().payResult(mActivity, new PayCallback() {
			@Override
			public void callback(String payResult) {
				LogUtils.d("payResult:" + payResult);
			}
		});
		/** 在此注册此接口，显示悬浮 */
		MCApiFactory.getMCApi().mcApiShowFloating();
		/**在此注册统计接口*/
		MCApiFactory.getMCApi().analyticsOnResume();
	}
	
	@Override
	public void doPause() {
		super.doPause();
		/** 在此注册此接口，隐藏悬浮 */
		MCApiFactory.getMCApi().mcApiHideFloating();
		/**在此注册统计接口*/
		MCApiFactory.getMCApi().analyticsOnPause();
	}
	
	@Override
	public void doDestory() {
		super.doDestory();
		MCApiFactory.getMCApi().stopFloating(mActivity);
	}
	
}
