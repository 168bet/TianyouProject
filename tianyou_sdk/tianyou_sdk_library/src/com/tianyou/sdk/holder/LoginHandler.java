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
import com.tianyou.sdk.interfaces.Tianyouxi;
import com.tianyou.sdk.utils.AppUtils;
import com.tianyou.sdk.utils.HttpUtils;
import com.tianyou.sdk.utils.HttpUtils.HttpsCallback;
import com.tianyou.sdk.utils.LogUtils;
import com.tianyou.sdk.utils.ResUtils;
import com.tianyou.sdk.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
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
		if (mLoginHandler == null) {
			mLoginHandler = new LoginHandler();
		}
		return mLoginHandler;
	}

	public static LoginHandler getInstance(Activity activity, Handler handler) {
		mActivity = activity;
		mHandler = handler;
		if (mLoginHandler == null) {
			mLoginHandler = new LoginHandler();
		}
		return mLoginHandler;
	}
	
	// 1-1.账号密码登录接口
	public void doUserLogin(String userName, String userPass, boolean isPhone) {
		ProgressBarHandler.getInstance().open(mActivity);
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
		HttpUtils.post(mActivity, URLHolder.URL_CODE_LOGIN, map, new HttpsCallback() {
			@Override
			public void onSuccess(String response) {
				LoginInfo request = new Gson().fromJson(response, LoginInfo.class);
				onLoginProcess(request);
			}
		});
    }
	
	// 1-2.快速注册登陆接口
	public void doQuickRegister() {
		Map<String,String> map = new HashMap<String, String>();
		String phoeImei = AppUtils.getPhoeIMEI(mActivity);
		map.put("appID", ConfigHolder.GAME_ID);
		map.put("imei", phoeImei);
		map.put("isgenerate", "1");
		map.put("channel", ConfigHolder.CHANNEL_ID);
		map.put("ip", AppUtils.getIP());
		map.put("type", "android");
		String url = (ConfigHolder.IS_OVERSEAS ? URLHolder.URL_OVERSEAS : URLHolder.URL_BASE) + URLHolder.URL_LOGIN_QUICK;
		HttpUtils.post(mActivity, url, map, new HttpsCallback() {
			@Override
			public void onSuccess(String response) {
				LoginInfo info = new Gson().fromJson(response, LoginInfo.class);
				ResultBean result = info.getResult();
				if (result.getCode() == 200) {
					showTipDialog(result);
				} else {
					ToastUtils.show(mActivity, result.getMsg());
				}
			}
		});
	}
	
	// 1-3.QQ登陆接口
	public void doQQLogin(final Activity activity, final String openid, final String access_token, final String nickname, final String imgUrl) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("imei", AppUtils.getPhoeIMEI(mActivity));
		map.put("openid", openid);
		map.put("access_token", access_token);
		map.put("ip", AppUtils.getIP());
		map.put("appID", ConfigHolder.GAME_ID);
		map.put("channel", ConfigHolder.CHANNEL_ID);
		map.put("type", "android");
		map.put("nickname", nickname);
		map.put("headimg", imgUrl);
		HttpUtils.post(mActivity, URLHolder.URL_QQ_LOGIN, map, new HttpsCallback() {
			@Override
			public void onSuccess(String response) {
				activity.finish();
				LoginInfo request = new Gson().fromJson(response, LoginInfo.class);
				onLoginProcess(request);
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
					map.put("imei", AppUtils.getPhoeIMEI(mActivity));
					map.put("ip", AppUtils.getIP());
					map.put("channel", ConfigHolder.CHANNEL_ID);
					map.put("appID", ConfigHolder.GAME_ID);
					map.put("type", "android");
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
	public void onLoginProcess(LoginInfo request) {
		mResultBean = request.getResult();
    	if (mResultBean.getCode() == 200) {
    		mResultBean = request.getResult();
    		new Handler().postDelayed(new Runnable() {
    			@Override
    			public void run() {
    				doHandlerLoginInfo();
    			}
    		}, 1500);
		} else {
			ProgressBarHandler.getInstance().close();
			ToastUtils.show(mActivity, mResultBean.getMsg());
			Tianyouxi.mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, mResultBean.getMsg());
		}
    }
	
	// 3.登陆成功数据处理
	private void doHandlerLoginInfo() {
		if ("qq".equals(mResultBean.getRegistertype()) && "0".equals(mResultBean.getIsperfect())) {	//	QQ登陆且没有完善账号信息
			mHandler.sendEmptyMessage(2);
		} else {
			LogUtils.d("doSaveUserInfo----------------");
			doSaveUserInfo();
		}
	}
	
	// 4-1.保存登陆成功信息
    public void doSaveUserInfo() {
    	//保存到内存
		ConfigHolder.USER_ACCOUNT = mResultBean.getUsername();
		ConfigHolder.USER_ID = mResultBean.getUserid();
		ConfigHolder.USER_NICKNAME = mResultBean.getNickname();
		ConfigHolder.USER_TOKEN = mResultBean.getToken();
		ConfigHolder.USER_CODE = mResultBean.getVerification() + "";
		ConfigHolder.USER_PASS_WORD = mResultBean.getPassword();
		MobclickAgent.onProfileSignIn(ConfigHolder.USER_ID);
		//保存到文件
		Map<String, String> info = new HashMap<String, String>();
		info.put(LoginInfoHandler.USER_ACCOUNT, mResultBean.getUsername());
		info.put(LoginInfoHandler.USER_NICKNAME, mResultBean.getNickname() == null ? "" : mResultBean.getNickname());
		info.put(LoginInfoHandler.USER_PASSWORD, mResultBean.getPassword() == null ? mResultBean.getVerification() : mResultBean.getPassword());
		info.put(LoginInfoHandler.USER_SERVER, "最近登录：" + ConfigHolder.GAME_NAME);
		info.put(LoginInfoHandler.USER_LOGIN_WAY, mResultBean.getRegistertype() == null ? "" : mResultBean.getRegistertype());
		//保存到手机登陆信息表
		if ("1".equals(mResultBean.getIscode())) {
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
  		ProgressBarHandler.getInstance().close();
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
  	
  	// 6.弹公告
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
   						onNoticeLoginSuccess();
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
 	public void doPerfectAccountInfo() {
 		Map<String, String> map = new HashMap<String, String>();
 		map.put("password", mResultBean.getPassword());
 		map.put("newname", mResultBean.getNickname());
 		map.put("username", mResultBean.getUsername());
 		map.put("userid", mResultBean.getUserid());
 		HttpUtils.post(mActivity, URLHolder.URL_LOGIN_PERFECT, map, new HttpsCallback() {
 			@Override
 			public void onSuccess(String response) {
 				try {
 					JSONObject jsonObject = new JSONObject(response);
 					JSONObject result = jsonObject.getJSONObject("result");
 					ToastUtils.show(mActivity, result.getString("msg"));
 					if (result.getInt("code") == 200) {
 						ConfigHolder.USER_ACCOUNT = result.getString("username");
 						Map<String, String> info = new HashMap<String, String>();
 						info.put(LoginInfoHandler.USER_ACCOUNT, result.getString("username"));
 						info.put(LoginInfoHandler.USER_NICKNAME, result.getString("nickname"));
 						info.put(LoginInfoHandler.USER_PASSWORD, mResultBean.getPassword());
 						info.put(LoginInfoHandler.USER_SERVER, "最近登录：" + ConfigHolder.GAME_NAME);
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
  	
  	// 1-2-1
 	private void showTipDialog(final ResultBean result) {
 		View view = View.inflate(mActivity, ResUtils.getResById(mActivity, "dialog_login_quick", "layout"), null);
 		final AlertDialog dialog = new AlertDialog.Builder(mActivity).create();
 		dialog.setCanceledOnTouchOutside(false);
 		dialog.setView(view);
 		dialog.show();
 		setDialogWindowAttr(dialog, mActivity);
 		view.findViewById(ResUtils.getResById(mActivity, "text_dialog_menu_0", "id")).setOnClickListener(new OnClickListener() {
 			@Override
 			public void onClick(View arg0) {
 				mHandler.sendEmptyMessage(3);
 				dialog.dismiss();
 			}
 		});
 		view.findViewById(ResUtils.getResById(mActivity, "text_dialog_menu_1", "id")).setOnClickListener(new OnClickListener() {
 			@Override
 			public void onClick(View arg0) {
 				doUserLogin(result.getUsername(), result.getPassword(), false);
// 				doSaveUserInfo();
 				dialog.dismiss();
 			}
 		});
 	}
 	
 	// 1-2-2
 	private void setDialogWindowAttr(Dialog dlg,Context ctx){
         Window window = dlg.getWindow();
         WindowManager.LayoutParams lp = window.getAttributes();
         lp.gravity = Gravity.CENTER;
         lp.width = (int) ctx.getResources().getDimension(ResUtils.getResById(ctx, "dialog_login_tip", "dimen"));//宽高可设置具体大小
         lp.height = LayoutParams.WRAP_CONTENT;
         dlg.getWindow().setAttributes(lp);
     }
  	
  	// 通知游戏登录成功
  	public static void onNoticeLoginSuccess() {
  		ConfigHolder.IS_LOGIN = true;
		Tianyouxi.mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_SUCCESS, ConfigHolder.USER_ID);
  	}
  	
}
