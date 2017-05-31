package com.tianyou.channel.interfaces;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

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

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;

/**
 * 多渠道接口默认实现
 * @author itstrong
 *
 */
public class BaseSdkService implements SdkServiceInterface {

	protected Activity mActivity;
	protected TianyouCallback mTianyouCallback;
	
	protected PayInfo mPayInfo;						//支付相关参数
	protected RoleInfo mRoleInfo;					//游戏角色相关参数
	protected LoginInfo mLoginInfo;					//用户登录相关参数
	protected ChannelInfo mChannelInfo;				//渠道信息相关参数
	
	@Override
	public void doApplicationCreate(Context context, boolean island) {
		LogUtils.d("调用doApplicationCreate");
		mChannelInfo = ConfigHolder.getChannelInfo(context);
	}

	@Override
	public void doApplicationAttach(Context base) { LogUtils.d("调用doApplicationAttach"); }
	
	@Override
	public void doApplicationTerminate() { LogUtils.d("调用doApplicationTerminate"); }
	
	@Override
	public void doApplicationConfigurationChanged(Application application,Configuration newConfig) { 
		LogUtils.d("调用doApplicationConfigurationChanged"); 
	}

	@Override
	public void doActivityInit(Activity activity, TianyouCallback tianyouCallback) {
		LogUtils.d("调用初始化接口");
		mActivity = activity;
		mTianyouCallback = tianyouCallback;
		mLoginInfo = new LoginInfo();
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
	public void doCreateRole(RoleInfo roleInfo) { 
		LogUtils.d("调用创建角色接口" + roleInfo); 
		mRoleInfo = roleInfo; 
	}
	
	@Override
	public void doEntryGame() {
		LogUtils.d("调用进入游戏接口");
		if (mRoleInfo == null) {
			ToastUtils.show(mActivity, "请先上传角色信息");
			return;
		}
	}
	
	@Override
	public void doUpdateRoleInfo(RoleInfo roleInfo) { 
		LogUtils.d("调用更新角色信息接口：" + roleInfo.toString()); 
		mRoleInfo = roleInfo;
	}

	@Override
	public void doPay(PayParam payInfo) {
		LogUtils.d("调用支付接口:" + payInfo);
		if (mRoleInfo == null) {
			ToastUtils.show(mActivity, "请先上传角色信息");
			return;
		}
		mPayInfo = ConfigHolder.getPayInfo(mActivity, payInfo.getPayCode());
		if (mPayInfo == null) {
			ToastUtils.show(mActivity, "需打入渠道资源");
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
	
	// 查询登录信息
	protected void checkLogin() { checkLogin(null); }
	
	// 查询登录信息
	protected void checkLogin(final LoginCallback callback) {
		String gameId = mChannelInfo.getGameId();
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", mLoginInfo.getChannelUserId());
		map.put("session", mLoginInfo.getUserToken());
		map.put("imei", CommenUtil.getPhoeIMEI(mActivity));
		map.put("appid", gameId);
		map.put("playerid", mLoginInfo.getUserToken());
		map.put("nickname", mLoginInfo.getNickname());
		map.put("promotion", mChannelInfo.getChannelId());
		map.put("is_guest", mLoginInfo.getIsGuest());
		map.put("yijie_appid",mLoginInfo.getYijieAppId());
		map.put("signature", CommenUtil.MD5("session=" + mLoginInfo.getUserToken() + "&uid="
				+ mLoginInfo.getChannelUserId() + "&appid=" + gameId));
		String url = (mLoginInfo.getIsOverseas() ? URLHolder.URL_OVERSEAS : URLHolder.URL_BASE) + URLHolder.CHECK_LOGIN_URL;
		HttpUtils.post(mActivity, url, map, new HttpCallback() {
			@Override
			public void onSuccess(String data) {
				try {
					JSONObject jsonObject = new JSONObject(data);
					JSONObject result = (JSONObject) jsonObject.get("result");
					String code = result.getString("code");
					if ("200".equals(code)) {
						String userId = result.getString("uid");
						mLoginInfo.setHanfengUid(result.getString("channeluid"));
						LogUtils.d("userid= "+userId);
						LogUtils.d("tianyouuserId= "+mLoginInfo.getTianyouUserId());
						if (mLoginInfo.getTianyouUserId() != null && !userId.equals(mLoginInfo.getTianyouUserId())) {
							LogUtils.d("current uid= "+mLoginInfo.getTianyouUserId()+",new uid= "+userId);
							mLoginInfo.setTianyouUserId(userId);
							mTianyouCallback.onResult(TianyouCallback.CODE_LOGOUT, "");
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
	
	// 创建订单
	protected void createOrder(final PayParam payInfo) {
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
		String url = (mLoginInfo.getIsOverseas() ? URLHolder.URL_OVERSEAS : URLHolder.URL_BASE) + URLHolder.CREATE_ORDER_URL;
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
	
	// 查询订单
	protected void checkOrder(String orderId) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("orderID", orderId);
		param.put("userId", mLoginInfo.getTianyouUserId());
		param.put("promotion", mChannelInfo.getChannelId());
		String url = (mLoginInfo.getIsOverseas() ? URLHolder.URL_OVERSEAS : URLHolder.URL_BASE) + URLHolder.CHECK_ORDER_URL;
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
	public void doStop() { LogUtils.d("调用doStop接口..."); }

	@Override
	public void doRestart() { }

	@Override
	public void doNewIntent(Intent intent) { }
	
	@Override
	public void doChannelPay(PayParam payInfo, OrderinfoBean orderInfo) { }

	@Override
	public void doDestory() { LogUtils.d("调用doDestory接口..."); }

	@Override
	public void doActivityResult(int requestCode, int resultCode, Intent data) { }
	
	@Override
	public void doBackPressed() { LogUtils.d("调用doBackPressed接口..."); }

	@Override
	public void doRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) { }

	@Override
	public void doConfigurationChanged(Configuration newConfig) { }

	@Override
	public void doRegisterPhone() { LogUtils.d("调用手机号注册接口..."); }

	@Override
	public void doRegisterGenerate() { LogUtils.d("调用一键注册接口..."); }

	@Override
	public void doVerifiedInfo() { LogUtils.d("调用实名认证防沉迷接口..."); }
	
	//是否显示游戏的退出界面：true是显示，false不显示，默认不显示游戏的退出界面
	@Override
	public boolean isShowExitGame() { return false; }

	//是否显示游戏的注销按钮: true是显示，false不显示，默认不显示游戏的注销按钮
	@Override
	public boolean isShowLogout() { return false; }
	
	//登陆成功回调接口
	public interface LoginCallback {
		
		void onSuccess(String data); 
	}
}
