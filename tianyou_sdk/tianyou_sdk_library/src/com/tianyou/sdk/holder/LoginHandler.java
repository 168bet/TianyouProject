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
		return getInstance();
	}

	public static LoginHandler getInstance(Activity activity, Handler handler) {
		mHandler = handler;
		return getInstance(activity);
	}
	
	// 1-1.账号密码登录接口
	public void doUserLogin(String username, String password, boolean isPhone) {
		SPHandler.putBoolean(mActivity, SPHandler.SP_IS_PHONE, isPhone);
		if (ConfigHolder.isUnion) {
			doUnionLogin(username, password, isPhone);
		} else {
			doCommonLogin(username, password, isPhone);
		}
    }
	
	private void doUnionLogin(String username, String password, boolean isPhone) {
		ProgressHandler.getInstance().openProgressDialog(mActivity);
    	Map<String,String> map = new HashMap<String, String>();
		map.put("username", username);
		map.put("password", password);
		map.put("appid", ConfigHolder.gameId);
		map.put("token", ConfigHolder.gameToken);
		map.put("channel", ConfigHolder.channelId);
		map.put("type", "android");
		map.put("imei", AppUtils.getPhoeIMEI(mActivity));
		map.put("sign", AppUtils.MD5(username + password + ConfigHolder.gameId + ConfigHolder.gameToken));
		map.put("signType", "md5");
		String url = isPhone ? URLHolder.URL_UNION_PHONE_LOGIN : URLHolder.URL_UNION_ACCOUNT_LOGIN;
		HttpUtils.post(mActivity, url, map, new HttpsCallback() {
			@Override
			public void onSuccess(String response) {
				onLoginProcess(new Gson().fromJson(response, LoginInfo.class));
			}
		});
	}
	
	private void doCommonLogin(String username, String password, boolean isPhone) {
		ProgressHandler.getInstance().openProgressDialog(mActivity);
    	Map<String,String> map = new HashMap<String, String>();
		map.put("username", username);
		map.put("verification", password);
		map.put("imei", AppUtils.getPhoeIMEI(mActivity));
		map.put("appID", ConfigHolder.gameId);
		map.put("type", "android");
		map.put("ispass", isPhone == true ? "0" : "1");
		map.put("ip", AppUtils.getIP());
		map.put("channel", ConfigHolder.channelId);
		Log.d("TAG", "login map= "+map);
		HttpUtils.post(mActivity, URLHolder.URL_CODE_LOGIN, map, new HttpsCallback() {
			@Override
			public void onSuccess(String response) {
				onLoginProcess(new Gson().fromJson(response, LoginInfo.class));
			}
		});
	}

	// 1-2.快速注册登陆接口
	public void doQuickRegister() {
		Map<String,String> map = new HashMap<String, String>();
		String phoeImei = AppUtils.getPhoeIMEI(mActivity);
		map.put("appID", ConfigHolder.gameId);
		map.put("imei", phoeImei);
		map.put("isgenerate", "1");
		map.put("channel", ConfigHolder.channelId);
		map.put("ip", AppUtils.getIP());
		map.put("type", "android");
		HttpUtils.post(mActivity, URLHolder.URL_LOGIN_QUICK, map, new HttpsCallback() {
			@Override
			public void onSuccess(String response) {
				LoginInfo info = new Gson().fromJson(response, LoginInfo.class);
				ResultBean result = info.getResult();
				if (result.getCode() == 200) {
					if (ConfigHolder.isOverseas || ConfigHolder.isUnion) {
						doUserLogin(result.getUsername(), result.getPassword(), false);
					} else {
						showTipDialog(result);
					}
				} else {
					ToastUtils.show(mActivity, result.getMsg());
				}
			}
		});
	}
	
	public void doQQLogin(Map<String, String> map) {
		if (ConfigHolder.isUnion) {
			doUnionQQLogin(map);
		} else {
			doCommonQQLogin(map);
		}
	}
	
	private void doUnionQQLogin(Map<String, String> map) {
		map.put("appid", ConfigHolder.gameId);
		map.put("token", ConfigHolder.gameToken);
		map.put("channel", ConfigHolder.channelId);
		map.put("type", "android");
		map.put("imei", AppUtils.getPhoeIMEI(mActivity));
		map.put("sign", AppUtils.MD5(map.get("openid") + ConfigHolder.gameId + ConfigHolder.gameToken));
		map.put("signtype", "md5");
		HttpUtils.post(mActivity, URLHolder.URL_UNION_QQ_LOGIN, map, new HttpsCallback() {
			@Override
			public void onSuccess(String response) {
				mActivity.finish();
				LoginInfo request = new Gson().fromJson(response, LoginInfo.class);
				onLoginProcess(request);
		}});
	}

	// 1-3.QQ登陆接口
	private void doCommonQQLogin(Map<String, String> map) {
		map.put("imei", AppUtils.getPhoeIMEI(mActivity));
		map.put("ip", AppUtils.getIP());
		map.put("appID", ConfigHolder.gameId);
		map.put("channel", ConfigHolder.channelId);
		map.put("type", "android");
		HttpUtils.post(mActivity, URLHolder.URL_QQ_LOGIN, map, new HttpsCallback() {
			@Override
			public void onSuccess(String response) {
				mActivity.finish();
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
					map.put("channel", ConfigHolder.channelId);
					map.put("appID", ConfigHolder.gameId);
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
	public void onLoginProcess(final LoginInfo request) {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if (request.getResult().getCode() == 200) {
					mResultBean = request.getResult();
					doHandlerLoginInfo();
				} else {
					ProgressHandler.getInstance().closeProgressDialog();
					ToastUtils.show(mActivity, request.getResult().getMsg());
					TianyouSdk.getInstance().mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, "");
				}
			} 
		}, 1500);
    }
	
	// 3.登陆成功数据处理
	private void doHandlerLoginInfo() {
		if ("qq".equals(mResultBean.getRegistertype()) && "0".equals(mResultBean.getIsperfect())) {	//	QQ登陆且没有完善账号信息
			mHandler.sendEmptyMessage(2);
		} else {
			doSaveUserInfo();
		}
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
		MobclickAgent.onProfileSignIn(ConfigHolder.userId);
		//保存到文件
		Map<String, String> info = new HashMap<String, String>();
		info.put(LoginInfoHandler.USER_ACCOUNT, mResultBean.getUsername());
		info.put(LoginInfoHandler.USER_NICKNAME, mResultBean.getNickname() == null ? "" : mResultBean.getNickname());
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
//  		ProgressBarHandler.getInstance().close();
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
  	
  	private void displayAnnouncement() {
  		if (ConfigHolder.isUnion) {
			unionAnnouncement();
		} else {
			commonAnnouncement();
		}
	}
  	
  	private void unionAnnouncement() {
   		Map<String, String> map = new HashMap<String, String>();
   		map.put("appid", ConfigHolder.gameId);
   		map.put("token", ConfigHolder.userToken);
   		map.put("type", "android");
   		map.put("imei", AppUtils.getPhoeIMEI(mActivity));
   		map.put("sign", AppUtils.MD5(ConfigHolder.gameId + ConfigHolder.userToken));
   		map.put("signtype", "md5");
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

	// 6.弹公告
   	private void commonAnnouncement() {
   		Map<String, String> map = new HashMap<String, String>();
   		map.put("appID", ConfigHolder.gameId);
   		map.put("usertoken", ConfigHolder.userToken);
   		HttpUtils.post(TianyouSdk.getInstance().mActivity, URLHolder.URL_ANNOUNCE, map, new HttpsCallback() {
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
 	public void doPerfectAccountInfo(String newName,final String newPwd) {
 		Map<String, String> map = new HashMap<String, String>();
 		map.put("password", newPwd);
 		map.put("newname", newName);
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
 						ConfigHolder.userName = result.getString("username");
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
