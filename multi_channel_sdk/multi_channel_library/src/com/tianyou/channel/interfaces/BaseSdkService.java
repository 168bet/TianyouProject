package com.tianyou.channel.interfaces;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.tianyou.channel.bean.ChannelInfo;
import com.tianyou.channel.bean.LoginInfo;
import com.tianyou.channel.bean.OrderInfo;
import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.bean.PayInfo;
import com.tianyou.channel.bean.PayParam;
import com.tianyou.channel.bean.RoleInfo;
import com.tianyou.channel.utils.CommenUtil;
import com.tianyou.channel.utils.ConfigHolder;
import com.tianyou.channel.utils.HttpUtils;
import com.tianyou.channel.utils.HttpUtils.HttpCallback;
import com.tianyou.channel.utils.LogUtils;
import com.tianyou.channel.utils.ToastUtils;
import com.tianyou.channel.utils.URLHolder;

public class BaseSdkService implements SdkServiceInterface {

	protected Activity mActivity;
	protected PayInfo mPayInfo;
	protected RoleInfo mRoleInfo;
	protected LoginInfo mLoginInfo;
	protected ChannelInfo mChannelInfo;
	protected TianyouCallback mTianyouCallback;
	protected boolean mIsOverseas;
	
	@Override
	public void doApplicationCreate(Context context, boolean island) { }

	@Override
	public void doApplicationAttach(Context base) { }
	
	@Override
	public void doApplicationTerminate() { }
	
	@Override
	public void doApplicationConfigurationChanged(Application application,Configuration newConfig) { }

	@Override
	public void doActivityInit(Activity activity, TianyouCallback tianyouCallback) {
		LogUtils.d("调用初始化接口");
		mIsOverseas = false;
		mActivity = activity;
		mTianyouCallback = tianyouCallback;
		mLoginInfo = new LoginInfo();
		mChannelInfo = ConfigHolder.getChannelInfo(activity);
	}
	
	@Override
	public void doLogin() { LogUtils.d("调用登录接口"); }
	
	@Override
	public void doLoginWechat() { LogUtils.d("调用微信登录接口"); }

	@Override
	public void doLogout() { LogUtils.d("调用注销接口"); }
	
	@Override
	public void doCustomerService() { LogUtils.d("调用客户服务接口"); }
	
	@Override
	public void doUploadRoleInfo(RoleInfo roleInfo) {
		LogUtils.d("调用上传角色信息接口：" + roleInfo);
		mRoleInfo = roleInfo;
	}
	
	@Override
	public void doCreateRole(RoleInfo roleInfo) { mRoleInfo = roleInfo; LogUtils.d("调用创建角色接口"); }
	
	@Override
	public void doEntryGame() {
		LogUtils.d("调用进入游戏接口");
		if (mRoleInfo == null) {
			ToastUtils.showToast(mActivity, "请先上传角色信息");
			return;
		}
	}
	
	@Override
	public void doUpdateRoleInfo(RoleInfo roleInfo) { 
		mRoleInfo = roleInfo;
		LogUtils.d("调用更新角色信息接口：" + roleInfo.toString()); 
	}

	@Override
	public void doPay(PayParam payInfo) {
		LogUtils.d("调用支付接口:" + payInfo);
		if (mRoleInfo == null) {
			ToastUtils.showToast(mActivity, "请先上传角色信息");
			return;
		}
		mPayInfo = ConfigHolder.getPayInfo(mActivity, payInfo.getPayCode());
		if (mPayInfo == null) {
			ToastUtils.showToast(mActivity, "需打入渠道资源");
		} else {
			createOrder(payInfo);
		}
	}
	
	@Override
	public void doExitGame() {
		LogUtils.d("调用退出游戏接口");
		mTianyouCallback.onResult(TianyouCallback.CODE_QUIT_SUCCESS, "");
	}
	
	@Override
	public void doOpenNaverCafe() { LogUtils.d("调用doOpenNaverCafe接口"); }
	
	/**
	 * 查询登录信息
	 * @param param
	 */
	protected void checkLogin(LoginInfo param) {
		checkLogin(param, null);
	}
	
	/**
	 * 查询登录信息
	 * @param param
	 * @param callback
	 */
	protected void checkLogin(LoginInfo param, final LoginCallback callback) {
		mLoginInfo = param;
		String userId = param.getChannelUserId();
		String userToken = param.getUserToken();
		String gameId = mChannelInfo.getGameId();
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", userId);
		map.put("session", userToken);
		map.put("imei", CommenUtil.getPhoeIMEI(mActivity));
		map.put("appid", gameId);
		map.put("playerid", userToken);
		map.put("nickname", param.getNickname());
		map.put("promotion", mChannelInfo.getChannelId());
		map.put("is_guest", param.getIsGuest());
		map.put("signature", CommenUtil.MD5("session=" + userToken + "&uid=" + userId + "&appid=" + gameId));
		String url = (mIsOverseas ? URLHolder.URL_OVERSEAS : URLHolder.URL_BASE) + URLHolder.CHECK_LOGIN_URL;
		HttpUtils.post(mActivity, url, map, new HttpCallback() {
			@Override
			public void onSuccess(String data) {
				try {
					JSONObject jsonObject = new JSONObject(data);
					JSONObject result = (JSONObject) jsonObject.get("result");
					String code = result.getString("code");
					if ("200".equals(code)) {
						String userId = result.getString("uid");
						LogUtils.d("userid= "+userId);
						LogUtils.d("tianyouuserId= "+mLoginInfo.getTianyouUserId());
						if (mLoginInfo.getTianyouUserId() != null && !userId.equals(mLoginInfo.getTianyouUserId())) {
							LogUtils.d("current uid= "+mLoginInfo.getTianyouUserId()+",new uid= "+userId);
							mLoginInfo.setTianyouUserId(userId);
							mTianyouCallback.onResult(TianyouCallback.CODE_LOGOUT, "");
							LogUtils.d("onlogout -------------------------");
						} else {
							LogUtils.d("uid= "+userId);
							mLoginInfo.setTianyouUserId(userId);
							LogUtils.d("CODE_LOGIN_SUCCESS");
							mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_SUCCESS, userId);
						}
						if (callback != null) callback.onSuccess("");
					} else {
						mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, "登录失败");
					}
				} catch (JSONException e) {
					e.printStackTrace();
					mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, "登录失败");
				}
			}
			
			@Override
			public void onFailed(String code) {
				mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, "登录失败" + code);
			}
		});
	}
	
	/**
	 * 创建订单
	 * @param payInfo
	 */
	protected void createOrder(final PayParam payInfo) {
		LogUtils.d("createOrder---------------");
		Map<String, String> param = new HashMap<String, String>();
		param.put("userId", mLoginInfo.getTianyouUserId());
		param.put("appID", mChannelInfo.getGameId());
		param.put("roleId", mRoleInfo.getRoleId());
		param.put("serverID", mRoleInfo.getServerId());
		param.put("serverName", mRoleInfo.getServerName());
		param.put("customInfo", payInfo.getCustomInfo());
		param.put("productId", mPayInfo.getProductId());
		param.put("productName", mPayInfo.getProductName());
		param.put("productDesc", mPayInfo.getProductDesc());
		param.put("moNey", mPayInfo.getMoney());
		param.put("promotion", mChannelInfo.getChannelId());
		param.put("playerid", mLoginInfo.getChannelUserId());
		param.put("roleName", mRoleInfo.getRoleName());
		String url = (mIsOverseas ? URLHolder.URL_OVERSEAS : URLHolder.URL_BASE) + URLHolder.CREATE_ORDER_URL;
		LogUtils.d("url= "+url);
		HttpUtils.post(mActivity, url, param, new HttpCallback() {
			@Override
			public void onSuccess(String data) {
				OrderInfo orderInfo = new Gson().fromJson(data, OrderInfo.class);
				if ("200".equals(orderInfo.getResult().getCode())) {
					doChannelPay(payInfo, orderInfo.getResult().getOrderinfo());
				} else {
					mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, "创建订单失败");
				}
			}
			
			@Override
			public void onFailed(String code) {
				mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, "创建订单失败");
			}
		});
	}
	
	/**
	 * 查询订单
	 * @param orderId
	 * @param callback
	 */
	protected void checkOrder(String orderId) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("orderID", orderId);
		param.put("userId", mLoginInfo.getTianyouUserId());
		param.put("promotion", mChannelInfo.getChannelId());
		String url = (mIsOverseas ? URLHolder.URL_OVERSEAS : URLHolder.URL_BASE) + URLHolder.CHECK_ORDER_URL;
		HttpUtils.post(mActivity, url, param, new HttpCallback() {
			@Override
			public void onSuccess(String data) {
				try {
					JSONObject jsonObject = new JSONObject(data);
					JSONObject result = jsonObject.getJSONObject("result");
					String code = result.getString("code");
					String msg = result.getString("msg");
					if ("200".equals(code)) {
						LogUtils.d("支付成功");
						mTianyouCallback.onResult(TianyouCallback.CODE_PAY_SUCCESS, msg);
					} else {
						mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, data);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, e.getMessage());
				}
			}
			
			@Override
			public void onFailed(String code) {
				mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, code);
			}
		});
	}
	
	@Override
	public void doGoogleAchieve(String achieve) { LogUtils.d("完成谷歌成就：" + achieve); }

	@Override
	public void doGoogleAchieveActivity() { LogUtils.d("打开谷歌成就界面"); }
	
	@Override
	public void doDataStatistics(String content) { }
	
	@Override
	public void doPushSwitch(boolean isOpen) { }

	@Override
	public void doResume() { }

	@Override
	public void doStart() { }

	@Override
	public void doPause() { }

	@Override
	public void doStop() { }

	@Override
	public void doRestart() { }

	@Override
	public void doNewIntent(Intent intent) { }
	
	@Override
	public void doChannelPay(PayParam payInfo, OrderinfoBean orderInfo) { }

	@Override
	public void doDestory() { }

	@Override
	public void doActivityResult(int requestCode, int resultCode, Intent data) { }
	
	@Override
	public void doBackPressed() { }

	@Override
	public boolean isShowExitGame() { return false; }

	@Override
	public boolean isShowLogout() { return true; }

	public interface LoginCallback { void onSuccess(String data); }

	@Override
	public void doRequestPermissionsResult(int requestCode,
			@NonNull String[] permissions, @NonNull int[] grantResults) {
		
	}

	@Override
	public void doConfigurationChanged(Configuration newConfig) { }

}
