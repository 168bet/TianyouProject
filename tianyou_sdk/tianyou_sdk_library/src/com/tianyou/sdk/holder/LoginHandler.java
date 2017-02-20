package com.tianyou.sdk.holder;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tianyou.channel.utils.LogUtils;
import com.tianyou.sdk.activity.LoginActivity;
import com.tianyou.sdk.activity.NotifyActivity;
import com.tianyou.sdk.bean.LoginInfo;
import com.tianyou.sdk.bean.LoginInfo.ResultBean;
import com.tianyou.sdk.fragment.login.PerfectFragment;
import com.tianyou.sdk.interfaces.Tianyouxi;
import com.tianyou.sdk.utils.AppUtils;
import com.tianyou.sdk.utils.HttpUtils;
import com.tianyou.sdk.utils.HttpUtils.HttpsCallback;
import com.tianyou.sdk.utils.ResUtils;
import com.tianyou.sdk.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * 登录逻辑处理
 * @author itstrong
 *
 */
public class LoginHandler {
	
	private static LoginHandler mLoginHandler;
	private static Activity mActivity;
	private static Handler mHandler;
	
	private LoginHandler() { }

	public static LoginHandler getInstance(Activity activity,Handler handler) {
		mActivity = activity;
		mHandler = handler;
		if (mLoginHandler == null) {
			mLoginHandler = new LoginHandler();
		}
		return mLoginHandler;
	}
	
	// 登录接口
	public void onUserLogin(String userName, String userPass, boolean isPhone) {
    	Map<String,String> map = new HashMap<String, String>();
		map.put("username", userName);
		map.put("verification", userPass);
		map.put("imei", AppUtils.getPhoeIMEI(mActivity));
		map.put("appID", ConfigHolder.GAME_ID);
		map.put("type", "android");
		map.put("ispass", isPhone == true ? "0" : "1");
		map.put("ip", AppUtils.getIP());
		map.put("channel", ConfigHolder.CHANNEL_ID);
		Log.d("TAG", "login map= "+map);
//		String url = (ConfigHolder.IS_OVERSEAS ? URLHolder.URL_OVERSEAS: URLHolder.URL_BASE) + URLHolder.URL_CODE_LOGIN;
		HttpUtils.post(mActivity, URLHolder.URL_CODE_LOGIN, map, new HttpsCallback() {
			@Override
			public void onSuccess(String response) {
				LoginInfo info = new Gson().fromJson(response, LoginInfo.class);
				onLoginProcess(info.getResult());
			}
		});
    }
	
	// 登录逻辑处理
	public void onLoginProcess(ResultBean result) {
    	if (result.getCode() == 200) {
    		mActivity.finish();
    		showWelComePopup(result);
		} else {
			ToastUtils.show(mActivity, result.getMsg());
			Tianyouxi.mLoginCallback.onFailed(result.getMsg());
		}
    }
	
	// 用户登录欢迎pupup
	public void showWelComePopup() {
		LogUtils.d("showWelcomePopup-------------");
		View mView = new View(Tianyouxi.mActivity);
		FrameLayout layout = new FrameLayout(Tianyouxi.mActivity);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layout.addView(mView);
		Tianyouxi.mActivity.addContentView(layout, params);
		View view = View.inflate(Tianyouxi.mActivity, ResUtils.getResById(Tianyouxi.mActivity, "popup_welcome", "layout"), null);
		TextView textUser = (TextView) view.findViewById(ResUtils.getResById(Tianyouxi.mActivity, "text_welcome_user", "id"));
		TextView textSwitch = (TextView) view.findViewById(ResUtils.getResById(Tianyouxi.mActivity, "text_welcome_switch", "id"));
		if (ConfigHolder.USER_NICKNAME == null || ConfigHolder.USER_NICKNAME.isEmpty()) {
			textUser.setText(ResUtils.getString(mActivity, "ty_tianyou") + 
					ConfigHolder.USER_ACCOUNT + ResUtils.getString(mActivity, "ty_welcome_back2"));
		} else {
			textUser.setText(ConfigHolder.USER_NICKNAME + ResUtils.getString(mActivity, "ty_welcome_back2"));
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
				ConfigHolder.IS_LOGIN = true;
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
				Intent intent = new Intent(Tianyouxi.mActivity, LoginActivity.class);
				intent.putExtra("is_switch_account", true);
				Tianyouxi.mActivity.startActivity(intent);
			}
		});
	}
    
    // 用户登录欢迎pupup
  	public void showWelComePopup(final ResultBean result) {
  		LogUtils.d("showWelcomePopup result-------------");
  		mActivity.finish();
  		ConfigHolder.USER_NICKNAME = result.getNickname();
  		ConfigHolder.USER_ACCOUNT = result.getUsername();
  		View mView = new View(Tianyouxi.mActivity);
  		FrameLayout layout = new FrameLayout(Tianyouxi.mActivity);
  		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
  		layout.addView(mView);
  		Tianyouxi.mActivity.addContentView(layout, params);
  		View view = View.inflate(Tianyouxi.mActivity, ResUtils.getResById(Tianyouxi.mActivity, "popup_welcome", "layout"), null);
  		TextView textUser = (TextView) view.findViewById(ResUtils.getResById(Tianyouxi.mActivity, "text_welcome_user", "id"));
  		TextView textSwitch = (TextView) view.findViewById(ResUtils.getResById(Tianyouxi.mActivity, "text_welcome_switch", "id"));
  		LogUtils.d("nickName= "+ConfigHolder.USER_NICKNAME+",account= "+ConfigHolder.USER_ACCOUNT);
  		if (ConfigHolder.USER_NICKNAME == null || ConfigHolder.USER_NICKNAME.isEmpty()) {
  			textUser.setText(ResUtils.getString(mActivity, "ty_tianyou") + 
   					ConfigHolder.USER_ACCOUNT + ResUtils.getString(mActivity, "ty_welcome_back2"));
 		} else {
 			textUser.setText(ConfigHolder.USER_NICKNAME + ResUtils.getString(mActivity, "ty_welcome_back2"));
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
  				onLoginSuccess(result);
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
  				Intent intent = new Intent(Tianyouxi.mActivity, LoginActivity.class);
  				intent.putExtra("is_switch_account", true);
  				Tianyouxi.mActivity.startActivity(intent);
  			}
  		});
  	}
  	
  	// 弹公告
  	private void displayAnnouncement() {
  		Map<String, String> map = new HashMap<String, String>();
  		map.put("appID", ConfigHolder.GAME_ID);
  		map.put("usertoken", ConfigHolder.USER_TOKEN);
  		HttpUtils.post(Tianyouxi.mActivity, URLHolder.URL_ANNOUNCE, map, new HttpsCallback() {
  			@Override
  			public void onSuccess(String response) {
  				try {
  					JSONObject jsonObject = new JSONObject(response);
  					JSONObject result = jsonObject.getJSONObject("result");
  					if (result.getInt("code") == 200) {
  						String custominfo = result.getString("custominfo");
  						custominfo = URLDecoder.decode(custominfo, "utf-8");
  						Intent intent = new Intent(Tianyouxi.mActivity, NotifyActivity.class);
  						intent.putExtra("content", custominfo);
  						Tianyouxi.mActivity.startActivity(intent);
  					} else {
  						Tianyouxi.mLoginCallback.onSuccess(ConfigHolder.USER_ID, ConfigHolder.USER_TOKEN);
  					}
  				} catch (JSONException e) {
  					e.printStackTrace();
  				} catch (UnsupportedEncodingException e) {
  					e.printStackTrace();
  				}
  			}
  		});
  	}
  	
  	// 登录成功
    public void onLoginSuccess(ResultBean result) {
    	LogUtils.d("onLoginSuccess---------------------");
    	ConfigHolder.IS_LOGIN = true;
		ConfigHolder.USER_ACCOUNT = result.getUsername();
		ConfigHolder.USER_ID = result.getUserid();
		ConfigHolder.USER_NICKNAME = result.getNickname();
		ConfigHolder.USER_TOKEN = result.getToken();
		ConfigHolder.USER_CODE = result.getVerification() + "";
		ConfigHolder.USER_PASS_WORD = result.getPassword();
		MobclickAgent.onProfileSignIn(ConfigHolder.USER_ID);
		Map<String, String> info = new HashMap<String, String>();
		info.put(LoginInfoHandler.USER_ACCOUNT, result.getUsername());
		info.put(LoginInfoHandler.USER_NICKNAME, result.getNickname() == null ? "" : result.getNickname());
		info.put(LoginInfoHandler.USER_PASSWORD, result.getPassword() == null ? result.getVerification() : result.getPassword());
		info.put(LoginInfoHandler.USER_SERVER, "最近登录：" + ConfigHolder.GAME_NAME);
		info.put(LoginInfoHandler.USER_LOGIN_WAY, result.getRegistertype() == null ? "" : result.getRegistertype());
		if ("1".equals(result.getIscode())) {
			LoginInfoHandler.putLoginInfo(LoginInfoHandler.LOGIN_INFO_PHONE, info);
			SPHandler.putBoolean(mActivity, SPHandler.SP_IS_PHONE_LOGIN, true);
		} else {
			LoginInfoHandler.putLoginInfo(LoginInfoHandler.LOGIN_INFO_ACCOUNT, info);
			SPHandler.putBoolean(mActivity, SPHandler.SP_IS_PHONE_LOGIN, false);
		}
		if ("qq".equals(result.getRegistertype())) {
			if ("0".equals(result.getIsperfect())) {
				
				Message msg = new Message();
				Bundle data = new Bundle();
				data.putString("username", result.getUsername());
				data.putString("password", result.getPassword());
				data.putString("userid", result.getUserid());
				data.putString("nickname", result.getNickname());
				msg.setData(data);
				msg.what = 2;
				mHandler.sendMessage(msg);
			} else {
				LoginInfoHandler.putLoginInfo(LoginInfoHandler.LOGIN_INFO_QQ, info);
			}
		}
    }
}
