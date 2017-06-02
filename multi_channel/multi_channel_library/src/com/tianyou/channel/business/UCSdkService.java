package com.tianyou.channel.business;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.util.Log;
import cn.uc.gamesdk.UCGameSdk;
import cn.uc.gamesdk.even.SDKEventKey;
import cn.uc.gamesdk.even.SDKEventReceiver;
import cn.uc.gamesdk.even.Subscribe;
import cn.uc.gamesdk.exception.UCCallbackListenerNullException;
import cn.uc.gamesdk.exception.UCMissActivityException;
import cn.uc.gamesdk.open.GameParamInfo;
import cn.uc.gamesdk.open.OrderInfo;
import cn.uc.gamesdk.open.PaymentInfo;
import cn.uc.gamesdk.open.UCCallbackListener;
import cn.uc.gamesdk.open.UCGameSdkStatusCode;
import cn.uc.gamesdk.open.UCLogLevel;
import cn.uc.gamesdk.open.UCOrientation;

import com.tianyou.channel.bean.ChannelInfo;
import com.tianyou.channel.bean.LoginInfo;
import com.tianyou.channel.bean.PayInfo;
import com.tianyou.channel.bean.PayParam;
import com.tianyou.channel.bean.RoleInfo;
import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.tianyou.channel.utils.AppUtils;
import com.tianyou.channel.utils.ConfigHolder;
import com.tianyou.channel.utils.HttpUtils;
import com.tianyou.channel.utils.HttpUtils.HttpCallback;
import com.tianyou.channel.utils.ToastUtils;
import com.tianyou.channel.utils.URLHolder;

public class UCSdkService extends BaseSdkService {

	private int gameID;
	private String promotion;
	private String tyAppID;
	private String uid;
	
	@Override
	public void doActivityInit(Activity activity,
			TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		
		gameID = Integer.parseInt(mChannelInfo.getAppId());
		promotion = mChannelInfo.getChannelId();
		tyAppID = mChannelInfo.getGameId();
		// 设置相关参数
		GameParamInfo info = new GameParamInfo();
		info.setGameId(gameID);
		info.setEnablePayHistory(true);
		info.setServerId(0);
		info.setEnableUserChange(true);
		info.setOrientation(UCOrientation.LANDSCAPE);
		
		UCGameSdk.defaultSdk().registerSDKEventReceiver(new SDKEventReceiver(){
			@Subscribe(event = SDKEventKey.ON_INIT_SUCC) 
			private void onInitSucc(){
				mTianyouCallback.onResult(TianyouCallback.CODE_INIT, "SDK初始化完成");
			}
			
		});
		
		try {
			UCGameSdk.defaultSdk().initSdk(mActivity, UCLogLevel.DEBUG, false, info, new UCCallbackListener<String>() {
						@Override
						public void callback(int code, String msg) {
//							initCallback.onSuccess("初始化成功");
							code = UCGameSdkStatusCode.LOGIN_EXIT;
//							ToastUtils.showToast(mActivity, "code=" + code + ",msg=" + msg);
							mTianyouCallback.onResult(TianyouCallback.CODE_INIT, "SDK初始化完成");
							// 创建悬浮框
							UCGameSdk.defaultSdk().createFloatButton(mActivity);
							try {
								UCGameSdk.defaultSdk().setLogoutNotifyListener(logoutListener);
							} catch (UCCallbackListenerNullException e) {
								e.printStackTrace();
							}
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	UCCallbackListener<String> logoutListener = new UCCallbackListener<String>() {
		@Override
		public void callback(int statuscode, String data) {
			Log.d("TAG","退出账号: statuscode= "+statuscode+",data= "+data);
		}
	};
	
	

	@Override
	public void doLogin() {
		Log.d("TAG", "--------------------------");
		try {
			UCGameSdk.defaultSdk().login(new UCCallbackListener<String>() {
				@Override
				public void callback(int code, String msg) {
					Log.d("TAG", "code= "+code+",msg= "+msg);
					if (code == UCGameSdkStatusCode.SUCCESS) {
						// 显示悬浮框
						UCGameSdk.defaultSdk().showFloatButton(mActivity, 100,50);
						// 获取sid
						String sid = UCGameSdk.defaultSdk().getSid();
						Log.d("TAG","uc login sid= "+sid);
						LoginInfo loginParam = new LoginInfo();
						loginParam.setChannelUserId(sid);
						loginParam.setUserToken(sid);
						checkLogin(loginParam);
					}

					if (code == UCGameSdkStatusCode.LOGIN_EXIT) {
						// 登录界面关闭，游戏需判断此时是否已登录成功进行相应操作
						Log.d("TAG", "登录界面关闭: code= "+code+",msg= "+msg);
					}

					if (code == UCGameSdkStatusCode.NO_INIT) {
						// 没有初始化就进行登录调用，需要游戏调用SDK初始化方法
						Log.d("TAG", "未初始化进行登录: code= "+code+",msg= "+msg);
					}

					if (code == UCGameSdkStatusCode.NO_LOGIN) {
						// 未登录成功，需要游戏重新调登录方法
//						callback.onFailed(msg);
						mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, "登录失败");
						Log.d("TAG","登录失败: code="+code+",msg= "+msg);
					}
				}
			});
		} catch (UCCallbackListenerNullException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void doUploadRoleInfo(RoleInfo roleInfo) {
		super.doUploadRoleInfo(roleInfo);
//		http://192.168.1.169/morechannel/index.php/MoreChannel/gettimeinfo
		HttpUtils.post(mActivity, URLHolder.GET_TIME,null, new HttpCallback() {
					@Override
					public void onSuccess(String data) {
						Log.d("TAG", "get time success data= "+data);
						JSONObject jsonObject;
						try {
							jsonObject = new JSONObject(data);
							JSONObject result = jsonObject.getJSONObject("result");
							JSONObject channelInfo = result.getJSONObject("channelinfo");
							String timeStr = channelInfo.getString("timestr");
							String dataStr = channelInfo.getString("datastr");
							String roleLevel = mRoleInfo.getRoleLevel();
							roleLevel = roleLevel.substring(0, roleLevel.length()-1);
							Log.d("TAG", "roleLevel= "+roleLevel);
							
							JSONObject loginExData = new JSONObject(); 
							loginExData.put("roleId", mRoleInfo.getRoleId());//同一区服角色ID需保持唯一性 
							loginExData.put("roleName", mRoleInfo.getRoleName());//未取名时传默认值，不可传空 
							loginExData.put("roleLevel", roleLevel);//如游戏存在转生，转职等，等级需累加 
							loginExData.put("zoneId", Integer.parseInt(mRoleInfo.getServerId())); 
							loginExData.put("zoneName", mRoleInfo.getServerName());//服务器名必须与界面展示的名称 保持一致 
							loginExData.put("roleCTime", mRoleInfo.getCreateTime());
							//获取服务器存储的角色创建时间，不可 用本地手机时间，同一角色创建时间不可变 
							loginExData.put("roleLevelMTime", timeStr);//可选字段
							Log.d("TAG", "login exdata= "+loginExData.toString());
							UCGameSdk.defaultSdk().submitExtendData("loginGameRole", loginExData);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					@Override
					public void onFailed(String code) {
						Log.d("TAG", "get time failed code= "+code);
					}
				});
	}
	
	@Override
	public void doChannelPay(PayParam payInfo, OrderinfoBean orderInfo) {
		super.doChannelPay(payInfo, orderInfo);
		MyPaymentInfo payMentInfo = new MyPaymentInfo();
		payMentInfo.setAmount(Float.parseFloat(orderInfo.getMoNey()));
		payMentInfo.setCustomInfo(payInfo.getCustomInfo());
		payMentInfo.setRoleId(mRoleInfo.getRoleId());
		payMentInfo.setRoleName(mRoleInfo.getRoleName());
		payMentInfo.setGrade(mRoleInfo.getRoleLevel());
		payMentInfo.setServerId(0);
		payMentInfo.setNotifyUrl(orderInfo.getNotifyurl());
		payMentInfo.setTransactionNumCP(orderInfo.getOrderID());
		Log.d("TAG","uc pay detail= "+payMentInfo.getDetail());
		try {
			UCGameSdk.defaultSdk().pay(payMentInfo,new UCCallbackListener<OrderInfo>() {
				
				@Override
				public void callback(int statuscode, OrderInfo orderInfo) {

					if (statuscode == UCGameSdkStatusCode.NO_INIT) {
						// 没有初始化就进行登录调用，需要游戏调用SDK初始化方法
						Log.e("TAG", "未初始化进行充值: statuscode= "+statuscode+",orderInfo= "+orderInfo.toString());
					}

					if (statuscode == UCGameSdkStatusCode.SUCCESS) {
						Log.d("TAG","uc pay success");
						// 订单生成生成，非充值成功，充值结果由服务端回调判断,请勿显示充值成 功的弹窗或toast
						//	callback.onSuccess(orderInfo.toString());
						if (orderInfo != null) { 
							checkOrder(orderInfo.getOrderId());
						} else {
//							callback.onFailed("充值失败");
							mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED,"支付失败");
						}
					}
					if (statuscode == UCGameSdkStatusCode.PAY_USER_EXIT) {
						if (orderInfo != null) {
							String orderId = orderInfo.getOrderId();
							float orderAmount = orderInfo.getOrderAmount();
							int payWay = orderInfo.getPayWay();
							String payWayName = orderInfo.getPayWayName();
							Log.d("TAG","ucpay exit------"+orderInfo.toString());
						} else {
							Log.d("TAG","uc pay exit orderInfo is null");
						}
						// 用户退出充值界面
						Log.e("TAG", "PAY_USER_EXIT");
					}
				}
			});
		}	catch (UCCallbackListenerNullException e) {
			Log.d("TAG",e.toString());
			e.printStackTrace();
		}		
		
	}
	
	
	private class MyPaymentInfo extends PaymentInfo{
		private String getDetail(){
			String detail = "{ amount= "+getAmount()+",customInfo= "+getCustomInfo()
					+",roleid= "+getRoleId()+",rolename= "+getRoleName()+",grade= "+getGrade()
					+",notifyurl= "+getNotifyUrl()+",transaction= "+getTransactionNumCP()+" }";
			return detail;
		}
	}
	
	@Override
	public void doExitGame() {
		try {
			UCGameSdk.defaultSdk().exitSDK(mActivity, new UCCallbackListener<String>() {
				@Override
				public void callback(int arg0, String arg1) {
					switch (arg0) {
					case UCGameSdkStatusCode.SDK_EXIT:
//						callback.onSuccess("退出成功");
						mTianyouCallback.onResult(TianyouCallback.CODE_QUIT_SUCCESS, "用户退出成功");
						break;
					case UCGameSdkStatusCode.SDK_EXIT_CONTINUE:
//						callback.onFailed("退出失败");
						mTianyouCallback.onResult(TianyouCallback.CODE_QUIT_CANCEL, "用户取消退出");
						break;
					case UCGameSdkStatusCode.INIT_FAIL:
						ToastUtils.show(mActivity, "初始化失败");
						break;
					}
				}
			});
		} catch (UCCallbackListenerNullException e) {
			e.printStackTrace();
		} catch (UCMissActivityException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean isShowExitGame() {
		return super.isShowExitGame();
	}
	
//	@Override
//	public void doLogout() {
//		UCGameSdk.defaultSdk().logout();
//	}

	@Override
	public void doDestory() {
		// 销毁悬浮框
		UCGameSdk.defaultSdk().destoryFloatButton(mActivity);
		// 退出sdk
		try {
			UCGameSdk.defaultSdk().exitSDK(mActivity, new UCCallbackListener<String>() {

				@Override
				public void callback(int statuscode, String data) {
					 switch (statuscode) {                     
					 case UCGameSdkStatusCode.SDK_EXIT:                            
						 // 退出程序 
						 break;
					 case UCGameSdkStatusCode.SDK_EXIT_CONTINUE:                            
						// 继续游戏 
						 break;
					 case UCGameSdkStatusCode.NO_INIT:                         
						// 没有初始化 
						 break;
					 }
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
