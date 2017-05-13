package com.tianyou.sdk.holder;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.tianyou.sdk.activity.LoginActivity;
import com.tianyou.sdk.activity.NotifyActivity;
import com.tianyou.sdk.bean.LoginInfo;
import com.tianyou.sdk.bean.LoginInfo.ResultBean;
import com.tianyou.sdk.interfaces.TianyouCallback;
import com.tianyou.sdk.interfaces.TianyouSdk;
import com.tianyou.sdk.utils.AppUtils;
import com.tianyou.sdk.utils.HttpUtils;
import com.tianyou.sdk.utils.HttpUtils.HttpsCallback;
import com.tianyou.sdk.utils.LogUtils;
import com.tianyou.sdk.utils.ResUtils;
import com.tianyou.sdk.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * 登录逻辑处理
 * @author itstrong
 *
 */
public class LoginHandler {
	
	private static LoginHandler mLoginHandler;
	private static Activity mActivity;
	private static Handler mHandler;
	public ResultBean mResultBean;
	
	private LoginHandler() { }
	
	public static LoginHandler getInstance() {
		if (mLoginHandler == null) {
			mLoginHandler = new LoginHandler();
		}
		return mLoginHandler;
	}
	
	public static LoginHandler getInstance(Activity activity) {
		mActivity = activity;
		return getInstance();
	}

	public static LoginHandler getInstance(Activity activity, Handler handler) {
		mHandler = handler;
		return getInstance(activity);
	}
	
	// 1-1.账号密码登录接口
	public void doUserLogin(String username, String password, boolean isPhone) {
		SPHandler.putBoolean(mActivity, SPHandler.SP_IS_PHONE, isPhone);
		ProgressHandler.getInstance().openProgressDialog(mActivity);
    	Map<String,String> map = new HashMap<String, String>();
		map.put("username", username);
		map.put("password", password);
		map.put("channel", ConfigHolder.channelId);
		map.put("sign", AppUtils.MD5(username + password + ConfigHolder.gameId + ConfigHolder.gameToken));
		String url = isPhone ? URLHolder.URL_UNION_PHONE_LOGIN : URLHolder.URL_UNION_ACCOUNT_LOGIN;
		HttpUtils.post(mActivity, url, map, new HttpsCallback() {
			@Override
			public void onSuccess(String response) {
				onLoginProcess(new Gson().fromJson(response, LoginInfo.class));
			}
		});
    }
	
	// 1-2.快速注册登陆接口
	public void doQuickRegister() {
		Map<String,String> map = new HashMap<String, String>();
		map.put("channel", ConfigHolder.channelId);
		map.put("sign", AppUtils.MD5(ConfigHolder.gameId + ConfigHolder.gameToken));
		HttpUtils.post(mActivity, URLHolder.URL_LOGIN_QUICK, map, new HttpsCallback() {
			@Override
			public void onSuccess(String response) {
				LoginInfo info = new Gson().fromJson(response, LoginInfo.class);
				ResultBean result = info.getResult();
				if (result.getCode() == 200) {
					doUserLogin(result.getUsername(), result.getPassword(), false);
				} else {
					ToastUtils.show(mActivity, result.getMsg());
				}
			}
		});
	}
	
	// 1-3.QQ登陆接口
	public void doQQLogin(final Activity activity, final Map<String, String> map) {
		SPHandler.putBoolean(mActivity, SPHandler.SP_IS_PHONE, false);
		map.put("channel", ConfigHolder.channelId);
		map.put("sign", AppUtils.MD5(map.get("openid") + ConfigHolder.gameId + ConfigHolder.gameToken));
		HttpUtils.post(mActivity, URLHolder.URL_UNION_QQ_LOGIN, map, new HttpsCallback() {
			@Override
			public void onSuccess(String response) {
				activity.finish();
				LoginInfo request = new Gson().fromJson(response, LoginInfo.class);
				request.getResult().setNickname(map.get("nickname"));
				if ("0".equals(request.getResult().getIsperfect())) {	//	QQ登陆且没有完善账号信息
					mResultBean = request.getResult();
					Message msg = new Message();
					msg.what = 2;
					msg.obj = map.get("nickname");
					mHandler.sendMessage(msg);
				} else {
					onLoginProcess(request);
				}
		}});
	}
	
	// 1-4.一键登录接口
 	public void doOneKeyLogin() {
		final ProgressDialog dialog = new ProgressDialog(mActivity);
		dialog.setTitle("一键登录");
		dialog.setMessage("正在发送短信...");
		dialog.setIndeterminate(false);
		dialog.setMax(100);
		dialog.incrementProgressBy(30);
		dialog.incrementSecondaryProgressBy(70);
		dialog.setCancelable(false);
		dialog.show();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				dialog.dismiss();
				String number = AppUtils.getPhoneNumber(mActivity);
				if (number.isEmpty()) {
					ToastUtils.show(mActivity, "验证失败，请使用其他方式登录！");
					mHandler.sendEmptyMessage(4);
				} else {
					Map<String,String> map = new HashMap<String, String>();
					map.put("username", number);
					map.put("channel", ConfigHolder.channelId);
					map.put("sign", number + ConfigHolder.gameId + ConfigHolder.gameToken);
					HttpUtils.post(mActivity, URLHolder.URL_KEY_LOGIN, map, new HttpsCallback() {
						@Override
						public void onSuccess(String response) {
							LoginInfo info = new Gson().fromJson(response, LoginInfo.class);
							mResultBean = info.getResult();
							if (info.getResult().getCode() == 200) {
								doSaveUserInfo();
							} else {
								ToastUtils.show(mActivity, info.getResult().getMsg());
							}
						}
					});
				}
			}
		}, 1000);
	}
	
	// 2.登录逻辑处理
	public void onLoginProcess(final LoginInfo request) {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if (request.getResult().getCode() == 200) {
					mResultBean = request.getResult();
					doSaveUserInfo();
				} else {
					ProgressHandler.getInstance().closeProgressDialog();
					ToastUtils.show(mActivity, request.getResult().getMsg());
					TianyouSdk.getInstance().mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, "");
				}
			} 
		}, 1500);
    }
	
	// 4-1.保存登陆成功信息
    public void doSaveUserInfo() {
    	//保存到内存
		ConfigHolder.userName = mResultBean.getUsername();
		ConfigHolder.userId = mResultBean.getUserid();
		ConfigHolder.userNickname = mResultBean.getNickname();
		ConfigHolder.userToken = mResultBean.getToken();
		ConfigHolder.userCode = mResultBean.getVerification() + "";
		ConfigHolder.userPassword = mResultBean.getPassword();
		ConfigHolder.isAuth = mResultBean.getIsauth() == 1;
		ConfigHolder.isTourist = mResultBean.getIstourist() == 1;
		ConfigHolder.isPhone = mResultBean.getIsphone() == 1;
		ConfigHolder.userPhone = mResultBean.getMobile();
		MobclickAgent.onProfileSignIn(ConfigHolder.userId);
		//保存到文件
		Map<String, String> info = new HashMap<String, String>();
		info.put(LoginInfoHandler.USER_ACCOUNT, mResultBean.getUsername());
		info.put(LoginInfoHandler.USER_NICKNAME, (mResultBean.getNickname() == null || mResultBean.getNickname().isEmpty()) ? "" : mResultBean.getNickname());
		info.put(LoginInfoHandler.USER_PASSWORD, mResultBean.getPassword() == null ? mResultBean.getVerification() : mResultBean.getPassword());
		info.put(LoginInfoHandler.USER_SERVER, "最近登录：" + ConfigHolder.gameName);
		info.put(LoginInfoHandler.USER_LOGIN_WAY, mResultBean.getRegistertype() == null ? "" : mResultBean.getRegistertype());
		//保存到手机登陆信息表
		if (SPHandler.getBoolean(mActivity, SPHandler.SP_IS_PHONE)) {
			LoginInfoHandler.putLoginInfo(LoginInfoHandler.LOGIN_INFO_PHONE, info);
			SPHandler.putBoolean(mActivity, SPHandler.SP_IS_PHONE_LOGIN, true);
		//保存到账号登陆信息表
		} else {
			LoginInfoHandler.putLoginInfo(LoginInfoHandler.LOGIN_INFO_ACCOUNT, info);
			SPHandler.putBoolean(mActivity, SPHandler.SP_IS_PHONE_LOGIN, false);
		}
		//保存到QQ登陆信息表
		if ("qq".equals(mResultBean.getRegistertype())) {
			LoginInfoHandler.putLoginInfo(LoginInfoHandler.LOGIN_INFO_QQ, info);
		}
		showWelComePopup();
    }
	
    // 5.显示用户登录欢迎pupup
  	public void showWelComePopup() {
  		mActivity.finish();
  		View mView = new View(TianyouSdk.getInstance().mActivity);
  		FrameLayout layout = new FrameLayout(TianyouSdk.getInstance().mActivity);
  		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
  		layout.addView(mView);
  		TianyouSdk.getInstance().mActivity.addContentView(layout, params);
  		View view = View.inflate(TianyouSdk.getInstance().mActivity, ResUtils.getResById(TianyouSdk.getInstance().mActivity, "popup_welcome", "layout"), null);
  		TextView textUser = (TextView) view.findViewById(ResUtils.getResById(TianyouSdk.getInstance().mActivity, "text_welcome_user", "id"));
  		TextView textSwitch = (TextView) view.findViewById(ResUtils.getResById(TianyouSdk.getInstance().mActivity, "text_welcome_switch", "id"));
  		LogUtils.d("nickName= "+ConfigHolder.userNickname+",account= "+ConfigHolder.userName);
  		if (ConfigHolder.userNickname == null || ConfigHolder.userNickname.isEmpty()) {
  			textUser.setText(ResUtils.getString(mActivity, "ty_tianyou") + 
   					ConfigHolder.userName + ResUtils.getString(mActivity, "ty_welcome_back2"));
 		} else {
 			textUser.setText(ConfigHolder.userNickname + ResUtils.getString(mActivity, "ty_welcome_back2"));
 		}
  		final PopupWindow popupWindow = new PopupWindow(view, 0, 0);
  		popupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
  		popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
  		popupWindow.setFocusable(true);
  		popupWindow.showAtLocation(mView, Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
  		final Handler handler = new Handler();
  		final Runnable runnable = new Runnable() {
  			@Override
  			public void run() {
  				displayAnnouncement();
  				popupWindow.dismiss();
  			}
  		};
  		handler.postDelayed(runnable, 1500);
  		textSwitch.setOnClickListener(new OnClickListener() {
  			@Override
  			public void onClick(View arg0) {
  				handler.removeCallbacks(runnable);
  				popupWindow.dismiss();
				Intent intent = new Intent(TianyouSdk.getInstance().mActivity, LoginActivity.class);
				intent.putExtra("is_switch_account", true);
				TianyouSdk.getInstance().mActivity.startActivity(intent);
  			}
  		});
  	}
  	
  	public interface LogoutCallback {
		void onSuccess(String response);
	}
  	
  	// 6.弹公告
  	private void displayAnnouncement() {
   		Map<String, String> map = new HashMap<String, String>();
   		map.put("sign", AppUtils.MD5(ConfigHolder.gameId + ConfigHolder.userToken));
   		HttpUtils.post(TianyouSdk.getInstance().mActivity, URLHolder.URL_UNION_ANNOUNCE, map, new HttpsCallback() {
   			@Override
   			public void onSuccess(String response) {
   				try {
   					JSONObject jsonObject = new JSONObject(response);
   					JSONObject result = jsonObject.getJSONObject("result");
   					if (result.getInt("code") == 200) {
   						String custominfo = result.getString("custominfo");
   						custominfo = URLDecoder.decode(custominfo, "utf-8");
   						Intent intent = new Intent(TianyouSdk.getInstance().mActivity, NotifyActivity.class);
   						intent.putExtra("content", custominfo);
   						TianyouSdk.getInstance().mActivity.startActivity(intent);
   					} else {
   						if (ConfigHolder.isTourist || !ConfigHolder.isAuth || !ConfigHolder.isPhone) {
   							if (SPHandler.getBoolean(mActivity, SPHandler.SP_TOURIST)) {
   								Intent intent = new Intent(TianyouSdk.getInstance().mActivity, LoginActivity.class);
   	   				  			intent.putExtra("show_tourist_tip", true);
   	   							TianyouSdk.getInstance().mActivity.startActivity(intent);
							} else {
								SPHandler.putBoolean(mActivity, SPHandler.SP_TOURIST, true);
								onNoticeLoginSuccess();
							}
   						} else {
   							onNoticeLoginSuccess();
   						}
   					}
   				} catch (JSONException e) {
   					e.printStackTrace();
   				} catch (UnsupportedEncodingException e) {
   					e.printStackTrace();
   				}
   			}
   		});
   	}

  	// 1-3-1.完善QQ账号信息
 	public void doPerfectAccountInfo(String newName,final String newPwd) {
 		Map<String, String> map = new HashMap<String, String>();
		map.put("userid", mResultBean.getUserid());
		map.put("newname", newName);
		map.put("password", newPwd);
		map.put("username", mResultBean.getUsername());
		map.put("sign", AppUtils.MD5(ConfigHolder.gameId + 
				ConfigHolder.gameToken + mResultBean.getUserid() + newName));
 		HttpUtils.post(mActivity, URLHolder.URL_UNION_PERFECT, map, new HttpsCallback() {
 			@Override
 			public void onSuccess(String response) {
 				try {
 					JSONObject jsonObject = new JSONObject(response);
 					JSONObject result = jsonObject.getJSONObject("result");
 					ToastUtils.show(mActivity, result.getString("msg"));
 					if (result.getInt("code") == 200) {
 						ConfigHolder.userName = result.getString("username");
 						ConfigHolder.userId = result.getString("userid");
 						ConfigHolder.userToken = result.getString("token");
 						ConfigHolder.userPassword = result.getString("password");
 						ConfigHolder.userNickname = result.getString("nickname");
 						MobclickAgent.onProfileSignIn(ConfigHolder.userId);
 						
 						Map<String, String> info = new HashMap<String, String>();
 						info.put(LoginInfoHandler.USER_ACCOUNT, result.getString("username"));
 						info.put(LoginInfoHandler.USER_NICKNAME, result.getString("nickname"));
 						info.put(LoginInfoHandler.USER_PASSWORD, newPwd);
 						info.put(LoginInfoHandler.USER_SERVER, "Recently landed: " + ConfigHolder.gameName);
 						info.put(LoginInfoHandler.USER_LOGIN_WAY, "qq");
 						SPHandler.putBoolean(mActivity, SPHandler.SP_IS_PHONE_LOGIN, false);
 						LoginInfoHandler.putLoginInfo(LoginInfoHandler.LOGIN_INFO_ACCOUNT, info);
 						LoginInfoHandler.putLoginInfo(LoginInfoHandler.LOGIN_INFO_QQ, info);
 						mActivity.finish();
 						showWelComePopup();
 					}
 				} catch (JSONException e) {
 					e.printStackTrace();
 				}
 			}
 		});
 	}
  	
  	// 7.通知游戏登录成功
  	public static void onNoticeLoginSuccess() {
  		ConfigHolder.userIsLogin = true;
  		JSONObject jsonObject = new JSONObject();
  		try {
			jsonObject.put("uid", ConfigHolder.userId);
			jsonObject.put("userToken", ConfigHolder.userToken);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		TianyouSdk.getInstance().mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_SUCCESS, jsonObject.toString());
  	}
}
