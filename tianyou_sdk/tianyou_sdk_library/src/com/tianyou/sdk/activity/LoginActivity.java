package com.tianyou.sdk.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.gson.Gson;
import com.tianyou.sdk.base.BaseActivity;
import com.tianyou.sdk.bean.FacebookLogin;
import com.tianyou.sdk.bean.FacebookLogin.ResultBean;
import com.tianyou.sdk.fragment.login.AccountFragment;
import com.tianyou.sdk.fragment.login.BindingFragment;
import com.tianyou.sdk.fragment.login.OneKeyFragment;
import com.tianyou.sdk.fragment.login.PerfectFragment;
import com.tianyou.sdk.fragment.login.PhoneFragment;
import com.tianyou.sdk.holder.ConfigHolder;
import com.tianyou.sdk.holder.LoginHandler;
import com.tianyou.sdk.holder.LoginInfoHandler;
import com.tianyou.sdk.holder.SPHandler;
import com.tianyou.sdk.holder.URLHolder;
import com.tianyou.sdk.interfaces.TianyouCallback;
import com.tianyou.sdk.interfaces.Tianyouxi;
import com.tianyou.sdk.utils.AppUtils;
import com.tianyou.sdk.utils.HttpUtils;
import com.tianyou.sdk.utils.LogUtils;
import com.tianyou.sdk.utils.ResUtils;
import com.tianyou.sdk.utils.ToastUtils;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * 登录Activity
 * @author itstrong
 * 
 */
public class LoginActivity extends BaseActivity implements ConnectionCallbacks, OnConnectionFailedListener  {

	private TextView mTextTitle2;
	private CallbackManager callbackManager;
	
	private GoogleApiClient mApiClient;
	private ConnectionResult mConnectionResult;
	private LoginHandler mLoginHandler;
	
	private static final int REQUEST_CODE_SIGN_IN = 1;
	private static final int DIALOG_GET_GOOGLE_PLAY_SERVICES = 1;
	private static final int REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES = 2;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			
			switch (msg.what) {
				case 1:
					Bundle data = msg.getData();
					checkGoogleLogin(data.getString("id"), data.getString("token"));
					break;
				case 2:
					switchFragment(new PerfectFragment(), "PerfectFragment");
					break;
				case 3:
					switchFragment(BindingFragment.getInstance(mLoginHandler.mResultBean.getUserid(), mLoginHandler.mResultBean.getUsername(), 
							mLoginHandler.mResultBean.getPassword(), mLoginHandler.mResultBean.getToken()), "BandingFragment");
					break;
				case 4:
					switchFragment(new PhoneFragment(), "PhoneFragment");
					break;
			}
		};
	};
	
	protected int setContentView() {
		return ResUtils.getResById(this, ConfigHolder.isOverseas ? "activity_login_overseas" : "activity_login", "layout");
	}

	@Override
	protected void initView() {
		mTextTitle2 = (TextView) findViewById(ResUtils.getResById(mActivity, "text_title_2", "id"));
		if (ConfigHolder.isOverseas) {
			facebookLogin();
		}
		
		mApiClient = new GoogleApiClient.Builder(this)
		.addApi(Plus.API,Plus.PlusOptions.builder()
				.setServerClientId("775358139434-v3h256aimo98rno1colkjevmqo6966kp.apps.googleusercontent.com").build())
		.addScope(Plus.SCOPE_PLUS_LOGIN).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
	}

	//facebook登录
	private void facebookLogin() {
		FacebookSdk.sdkInitialize(this);
		callbackManager = CallbackManager.Factory.create();
		LoginButton btnLogin = (LoginButton) findViewById(ResUtils.getResById(mActivity, "btn_facebook_login", "id"));
		btnLogin.setReadPermissions("email");
		btnLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
			@Override
			public void onSuccess(LoginResult loginResult) {
				final Map<String, String> map = new HashMap<String, String>();
				map.put("uid", loginResult.getAccessToken().getUserId());
				map.put("usertoken", loginResult.getAccessToken().getToken());
				map.put("appID", ConfigHolder.gameId);
				map.put("imei", AppUtils.getPhoeIMEI(mActivity));
				HttpUtils.post(mActivity, URLHolder.URL_FACEBOOK_LOGIN, map, new HttpUtils.HttpsCallback() {
					@Override
					public void onSuccess(String response) {
						FacebookLogin login = new Gson().fromJson(response, FacebookLogin.class);
						ResultBean result = login.getResult();
						if (result.getCode() == 200) {
							String username = result.getUsername();
							int password = result.getPassword();
							mLoginHandler.doUserLogin(username, password + "", false);
						} else {
							ToastUtils.show(mActivity, result.getMsg());
						}
					}
				});
			}

			@Override
			public void onCancel() { LogUtils.d("onCancel:"); }

			@Override
			public void onError(FacebookException e) { LogUtils.d("onError:"); }
		});
	}

	@Override
	protected void initData() {
		mLoginHandler = LoginHandler.getInstance(mActivity,mHandler);
		List<Map<String, String>> info1 = LoginInfoHandler.getLoginInfo(LoginInfoHandler.LOGIN_INFO_ACCOUNT);
		List<Map<String, String>> info2 = LoginInfoHandler.getLoginInfo(LoginInfoHandler.LOGIN_INFO_PHONE);
		if (info1.size() == 0 && info2.size() == 0) {
			switchFragment(new OneKeyFragment(), "OneKeyFragment");
		} else {
			boolean isSwitchAccount = getIntent().getBooleanExtra("is_switch_account", false);
			if (SPHandler.getBoolean(mActivity, SPHandler.SP_IS_PHONE_LOGIN)) {
				switchFragment(PhoneFragment.getInstance(isSwitchAccount), "PhoneFragment");
			} else {
				switchFragment(AccountFragment.getInstance(isSwitchAccount), "AccountFragment");
			}
		}
	}
	
	@Override
	public void onClick(View v) { }
	
	@Override
	public void onBackPressed() {
		if (mFragmentTag.equals("NoQQFragment")) {
			finish();
		} if (mFragmentTag.equals("AccountFragment")) {
			if (!ConfigHolder.userIsLogin) {
				ToastUtils.show(mActivity, "登录失败");
				Tianyouxi.mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, "");
			}
			finish();
		} if (mFragmentTag.equals("PhoneFragment")) {
			if (!ConfigHolder.userIsLogin) {
				ToastUtils.show(mActivity, "登录失败");
				Tianyouxi.mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, "");
			}
			finish();
		} else if (mFragmentTag.equals("PerfectFragment")) {
			mLoginHandler.doSaveUserInfo();
		}
		super.onBackPressed();
	}
	
	@Override
	public void setFragmentTitle(String title) {
		if (mFragmentTag.equals("PhoneFragment")) {
			mTextTitle2.setVisibility(View.VISIBLE);
		} else {
			mTextTitle2.setVisibility(View.GONE);
		}
		super.setFragmentTitle(title);
	}
	
	@Override
	protected void onStart() {
		mApiClient.connect();
		super.onStart();
	}
	
	@Override
	protected void onStop() {
		if (mApiClient.isConnected()) {
			mApiClient.disconnect();
		}
		super.onStop();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (ConfigHolder.isOverseas) {
			callbackManager.onActivityResult(requestCode, resultCode, data);
		}
		
		if (requestCode == REQUEST_CODE_SIGN_IN|| requestCode == REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES) {
            if (resultCode == mActivity.RESULT_CANCELED) {
            } else if (resultCode == mActivity.RESULT_OK && !mApiClient.isConnected()
                    && !mApiClient.isConnecting()) {
            	mApiClient.connect();
            }
        }
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		mConnectionResult = result;
	}
	
	@Override
	public void onConnected(Bundle connectionHint) {
		LogUtils.d("onConnected------------");
		final String accountName = Plus.AccountApi.getAccountName(mApiClient);
		Person person = Plus.PeopleApi.getCurrentPerson(mApiClient);
		final String id = person.getId();
		Log.d("TAG", "id= "+id+",accountName= "+accountName);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String token = GoogleAuthUtil.getToken(mActivity, accountName, "audience:server:client_id:775358139434-v3h256aimo98rno1colkjevmqo6966kp.apps.googleusercontent.com");
					Log.d("TAG", ",token= "+token);
//					checkGoogleLogin(id,token);
					Bundle bundle = new Bundle();
					bundle.putString("id", id);
					bundle.putString("token", token);
					Message msg = new Message();
					msg.what = 1;
					msg.setData(bundle);
					mHandler.sendMessage(msg);
				} catch (Exception e) {
					Log.d("TAG", "Exception= "+e.getMessage());
				}
			}
		}).start();
	}
	
	private void checkGoogleLogin(String id,String token) {
		Map<String,String> googleParam = new HashMap<String, String>();
		googleParam.put("id_token",token);
		googleParam.put("id",id);
		googleParam.put("GGappid", AppUtils.getMetaDataValue(mActivity,"google_client_id"));
		googleParam.put("appID",ConfigHolder.gameId);
		googleParam.put("imei",AppUtils.getPhoeIMEI(mActivity));
		HttpUtils.post(mActivity, URLHolder.URL_GOOGLE_LOGIN, googleParam, new HttpUtils.HttpCallback() {
			@Override
			public void onSuccess(String response) {
				LogUtils.d("login success response= "+response);
				try {
					JSONObject jsonObject = new JSONObject(response);
					int code = jsonObject.getInt("code");
					if (code == 200) {
						String userName = jsonObject.getString("username");
						String userPass = jsonObject.getString("truepass");
						mLoginHandler.doUserLogin(userName, userPass, false);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					ToastUtils.show(mActivity, "服务器好像开小差了...");
					LogUtils.d(e.getMessage());
				}
			}

			@Override
			public void onFailed() {
				LogUtils.d("login failed-------------");
				ToastUtils.show(mActivity, "网络连接出错,请检查您的网络设置...");
			}
		});

	}

	@Override
	public void onConnectionSuspended(int cause) {
		mApiClient.connect();
	}
	
	public void setConnectionResult(ConnectionResult result) {
		mConnectionResult = result;
	}
	
	public ConnectionResult getConnectionResult (){
		return mConnectionResult;
	}
	
	public void setGoogleApiClient (GoogleApiClient apiClient) {
		mApiClient = apiClient;
	}
	
	public GoogleApiClient getGoogleApiClient (){
		return mApiClient;
	}
	
	public Handler getHandler(){
		return mHandler;
	}
}