package com.tianyou.sdk.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.tianyou.sdk.fragment.login.IdentifiFragment;
import com.tianyou.sdk.fragment.login.OneKeyFragment;
import com.tianyou.sdk.fragment.login.PersonalCenterFragment;
import com.tianyou.sdk.fragment.login.PhoneRegisterFragment;
import com.tianyou.sdk.fragment.login.TouristTipFragment;
import com.tianyou.sdk.fragment.login.UserRegisterFragment;
import com.tianyou.sdk.holder.ConfigHolder;
import com.tianyou.sdk.holder.LoginHandler;
import com.tianyou.sdk.holder.LoginInfoHandler;
import com.tianyou.sdk.holder.ProgressHandler;
import com.tianyou.sdk.holder.URLHolder;
import com.tianyou.sdk.utils.AppUtils;
import com.tianyou.sdk.utils.HttpUtils;
import com.tianyou.sdk.utils.LogUtils;
import com.tianyou.sdk.utils.ResUtils;
import com.tianyou.sdk.utils.ToastUtils;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 登录Activity
 * @author itstrong
 * 
 */
public class LoginActivity extends BaseActivity implements ConnectionCallbacks,OnConnectionFailedListener{

	private View mLayoutRegisterTitle;
	private View mLayoutTitle;
	private View mViewBack;
	private View mLayoutBg;
	private TextView mTextPhone;
	private TextView mTextAccount;
	private ImageView mImgClose;
	
	private static GoogleApiClient mApiClient;
	public ConnectionResult mConnectionResult;
	private static boolean isGoogleConnected = false;
	private CallbackManager callbackManager;
	
	private boolean mIsAccountRegister;
	private ConnectionCallbacks mConnectionCallbacks;
	private OnConnectionFailedListener mOnConnectionFailedListener;
	
	protected int setContentView() {
		return ResUtils.getResById(this, ConfigHolder.isOverseas ? "activity_login2" : "activity_login", "layout");
	}

	@Override
	protected void initView() {
		
		
		mImgClose = (ImageView) findViewById(ResUtils.getResById(mActivity, "img_login_close", "id"));
		
		mLayoutRegisterTitle = findViewById(ResUtils.getResById(mActivity, "layout_login_register_title", "id"));
		mLayoutTitle = findViewById(ResUtils.getResById(mActivity, "layout_login_title", "id"));
		mViewBack = findViewById(ResUtils.getResById(mActivity, "img_login_back", "id"));
		mLayoutBg = findViewById(ResUtils.getResById(mActivity, "layout_login_bg", "id"));
		
		mTextPhone = (TextView) findViewById(ResUtils.getResById(mActivity, "text_login_register_phone", "id"));
		mTextAccount = (TextView) findViewById(ResUtils.getResById(mActivity, "text_login_register_account", "id"));
		
		mImgClose.setOnClickListener(this);
		mViewBack.setOnClickListener(this);
		mTextPhone.setOnClickListener(this);
		mTextAccount.setOnClickListener(this);
		
		if (ConfigHolder.isOverseas) {
			mConnectionResult = null;
			mApiClient = null;
			mApiClient = new GoogleApiClient.Builder(this)
			.addApi(Plus.API,Plus.PlusOptions.builder()
					.setServerClientId(AppUtils.getMetaDataValue(LoginActivity.this, "google_client_id"))//"775358139434-v3h256aimo98rno1colkjevmqo6966kp.apps.googleusercontent.com")
					.build())
					.addScope(Plus.SCOPE_PLUS_LOGIN).addConnectionCallbacks(mConnectionCallbacks).addOnConnectionFailedListener(mOnConnectionFailedListener)
					.build();
			facebookLogin();
		}
		googleInit();
	}
	
	@Override
	protected void initData() {
		mIsLogout = getIntent().getBooleanExtra("is_logout", false);
		if (getIntent().getBooleanExtra("show_tourist_tip", false)) {
			if (!ConfigHolder.isUnion && ConfigHolder.isTourist) {
				switchFragment(new TouristTipFragment());
			} else if(!ConfigHolder.isAuth) {
				switchFragment(new IdentifiFragment());
			}
		} else if (getIntent().getIntExtra("login_type", 0) == 1) {
			switchFragment(new PersonalCenterFragment());
		} else {
			if (ConfigHolder.isUnion) {
				switchFragment(new AccountFragment());
			} else {
				List<Map<String, String>> info1 = LoginInfoHandler.getLoginInfo(LoginInfoHandler.LOGIN_INFO_ACCOUNT);
//				List<Map<String, String>> info2 = LoginInfoHandler.getLoginInfo(LoginInfoHandler.LOGIN_INFO_PHONE);
				if (info1.size() == 0) {
					switchFragment(new OneKeyFragment());
				} else {
					switchFragment(new AccountFragment());
				}
			}
		}
	}
	
	private void googleInit() {
		LogUtils.d("是否是海外报：" + ConfigHolder.isOverseas);
		if (ConfigHolder.isOverseas) {
			mConnectionCallbacks = new ConnectionCallbacks() {
				@Override
				public void onConnectionSuspended(int arg0) {
					mApiClient.connect();
				}
				
				@Override
				public void onConnected(Bundle arg0) {
					if (isGoogleConnected) {
						isGoogleConnected = false;
						final String accountName = Plus.AccountApi.getAccountName(mApiClient);
						Person person = Plus.PeopleApi.getCurrentPerson(mApiClient);
						final String nickname = person.getDisplayName();
						final String id = person.getId();
//						final String nickname = person.getNickname();
						LogUtils.d("id= "+id+",accountName= "+accountName+",displayname= "+nickname);
						new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									String token = GoogleAuthUtil.getToken(mActivity, accountName, "audience:server:client_id:"+AppUtils.getMetaDataValue(mActivity, "google_client_id"));//775358139434-v3h256aimo98rno1colkjevmqo6966kp.apps.googleusercontent.com");
									LogUtils.d("token= "+token);
				//					checkGoogleLogin(id,token);
									Bundle bundle = new Bundle();
									bundle.putString("id", id);
									bundle.putString("token", token);
									bundle.putString("nickname", nickname);
									Message msg = new Message();
									msg.what = 1;
									msg.setData(bundle);
									mHandler.sendMessage(msg);
								} catch (Exception e) {
									LogUtils.d("Exception= "+e.getMessage());
								}
							}
						}).start();
					}
				}
			};
			
			mOnConnectionFailedListener = new OnConnectionFailedListener() {
				@Override
				public void onConnectionFailed(ConnectionResult result) {
					LogUtils.d("onConnecton failed-------------");
					if (result == null) {
						LogUtils.d("----------------");
					} else {
						LogUtils.d("=============");
					}
					mConnectionResult = result;
				}
			};
		}
	}
	@Override
	public void onClick(View v) {
		if (v.getId() == ResUtils.getResById(mActivity, "img_login_close", "id")) {
			closeLoginActivity();
		} else if (v.getId() == ResUtils.getResById(mActivity, "img_login_back", "id")) {
			onBackPressed();
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_login_register_phone", "id")) {
			switchTitleState(false);
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_login_register_account", "id")) {
			switchTitleState(true);
		}
	}
	
	public void setCloseViw(boolean visibility) {
		mImgClose.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
	}
	
	private LoginButton btnLogin;
	
	public void clickFacebook() {
		btnLogin.performClick();
	}

	//facebook登录
	private void facebookLogin() {
		FacebookSdk.sdkInitialize(this);
		callbackManager = CallbackManager.Factory.create();
		btnLogin = (LoginButton) findViewById(ResUtils.getResById(mActivity, "btn_facebook_login", "id"));
		btnLogin.setReadPermissions("email");
		btnLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
			@Override
			public void onSuccess(LoginResult loginResult) {
				ToastUtils.show(mActivity, (ConfigHolder.isOverseas? "Facebook login successfully" : "Facebook登陆成功"));
				String userId = loginResult.getAccessToken().getUserId();
				Map<String, String> map = new HashMap<String, String>();
				map.put("uid", userId);
				map.put("usertoken", loginResult.getAccessToken().getToken());
				map.put("channel", ConfigHolder.channelId);
				map.put("sign", AppUtils.MD5(userId + ConfigHolder.gameId + ConfigHolder.gameToken));
				HttpUtils.post(mActivity, URLHolder.URL_PAY_FACEBOOK, map, new HttpUtils.HttpsCallback() {
					@Override
					public void onSuccess(String response) {
						FacebookLogin login = new Gson().fromJson(response, FacebookLogin.class);
						ResultBean result = login.getResult();
						if (result.getCode() == 200) {
							String username = result.getUsername();
							int password = result.getPassword();
							LoginHandler.getInstance(mActivity, mHandler).doUserLogin(username, password + "", false);
						} else {
							ToastUtils.show(mActivity, result.getMsg());
						}
					}
				});
			}

			@Override
			public void onCancel() { 
				LogUtils.d("onCancel:"); }

			@Override
			public void onError(FacebookException e) { 
				LogUtils.d("onError:"); }
		});
	}
	
	private int mBgHeight;
	
	public void setBgHeight(boolean flag) {
		LayoutParams params = mLayoutBg.getLayoutParams();
		if (mBgHeight == 0) mBgHeight = params.height;
		params.height = flag ? LayoutParams.WRAP_CONTENT : mBgHeight;
		mLayoutBg.setLayoutParams(params);
	}
	
	private void closeLoginActivity() {
		LogUtils.d("mFragmentTag:" + mFragmentTag);
		if (mFragmentTag.equals("PerfectInfoFragment")) {
			return;
		} else if (mFragmentTag.equals("TouristTipFragment") || 
				(mFragmentTag.equals("IdentifiFragment")  && !ConfigHolder.isNoticeGame)) {
			LoginHandler.displayAnnouncement();
		} else if (mFragmentTag.equals("UpgradeFragment")) {
			if(!ConfigHolder.isAuth) {
				switchFragment(new IdentifiFragment());
			} else {
				LoginHandler.onNoticeLoginSuccess();
			}
		} else if (mFragmentTag.equals("ProtocolFragment") || mFragmentTag.equals("ServerFragment")) {
			onBackPressed();
			return;
		}
		finish();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		LogUtils.d("requestCode, resultCode, data");
		if (ConfigHolder.isOverseas) {
			callbackManager.onActivityResult(requestCode, resultCode, data);
//			if (requestCode == REQUEST_CODE_SIGN_IN|| requestCode == REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES) {
//	            if (resultCode == mActivity.RESULT_CANCELED) {
//	            } else if (resultCode == mActivity.RESULT_OK && !mApiClient.isConnected()
//	                    && !mApiClient.isConnecting()) {
//	            	mApiClient.connect();
//	            }
//	        }
		}
	}
	
	//设置注册类型
	private void switchTitleState(boolean isAccountRegister) {
		if (mIsAccountRegister == isAccountRegister) return;
		mIsAccountRegister = isAccountRegister;
		mTextPhone.setTextColor(Color.parseColor(!mIsAccountRegister ? "#333333" : "#FFFFFF"));
		mTextAccount.setTextColor(Color.parseColor(!mIsAccountRegister ? "#FFFFFF" : "#333333"));
		mTextPhone.setBackgroundResource(ResUtils.getResById(mActivity, 
				!mIsAccountRegister ? "shape_bg_dialog" : "shape_bg_gray_fill", "drawable"));
		mTextAccount.setBackgroundResource(ResUtils.getResById(mActivity, 
				!mIsAccountRegister ? "shape_bg_gray_fill" : "shape_bg_dialog", "drawable"));
		switchFragment(isAccountRegister ? new UserRegisterFragment() : new PhoneRegisterFragment());
	}
	
	public void setRegisterTitle(boolean flag) {
		mLayoutTitle.setVisibility(flag ? View.GONE : View.VISIBLE);
		mLayoutRegisterTitle.setVisibility(flag ? View.VISIBLE : View.GONE);
	}
	
	public void setBackBtnVisible(boolean flag) {
		mViewBack.setVisibility(flag ? View.VISIBLE : View.INVISIBLE);
	}

	public void resetRegisterTitle() {
		mIsAccountRegister = false;
		mTextPhone.setTextColor(Color.parseColor(!mIsAccountRegister ? "#333333" : "#FFFFFF"));
		mTextAccount.setTextColor(Color.parseColor(!mIsAccountRegister ? "#FFFFFF" : "#333333"));
		mTextPhone.setBackgroundResource(ResUtils.getResById(mActivity, 
				!mIsAccountRegister ? "shape_bg_dialog" : "shape_bg_gray_fill", "drawable"));
		mTextAccount.setBackgroundResource(ResUtils.getResById(mActivity, 
				!mIsAccountRegister ? "shape_bg_gray_fill" : "shape_bg_dialog", "drawable"));
	}
	
	@Override
	protected void onStart() {
		LogUtils.d("onstart-----------");
		if (ConfigHolder.isOverseas) {
			mApiClient.connect();
		}
		super.onStart();
	}
	
	@Override
	protected void onStop() {
		LogUtils.d("onstop-----------");
		if (ConfigHolder.isOverseas) {
			if (mApiClient.isConnected()) {
				LogUtils.d("disconnect-----------");
				Plus.AccountApi.clearDefaultAccount(mApiClient);
				mApiClient.disconnect();
				mApiClient.connect();
			}
		}
		super.onStop();
	}
	@Override
	protected void onDestroy() {
		ProgressHandler.getInstance().closeProgressDialog();
		super.onDestroy();
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
	
	
	public boolean getIsGoogleConnected (){
		return isGoogleConnected;
	}
	
	public void setIsGoogleConnected (boolean isGoogleConnected) {
		isGoogleConnected= isGoogleConnected;
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		LogUtils.d("onConnecton failed-------------");
		if (result == null) {
			LogUtils.d("----------------");
		} else {
			LogUtils.d("=============");
		}
		mConnectionResult = result;
	}

	@Override
	public void onConnected(Bundle arg0) {
		if (isGoogleConnected) {
			isGoogleConnected = false;
			final String accountName = Plus.AccountApi.getAccountName(mApiClient);
			Person person = Plus.PeopleApi.getCurrentPerson(mApiClient);
			final String nickname = person.getDisplayName();
			final String id = person.getId();
//			final String nickname = person.getNickname();
			LogUtils.d("id= "+id+",accountName= "+accountName+",displayname= "+nickname);
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						String token = GoogleAuthUtil.getToken(mActivity, accountName, "audience:server:client_id:"+AppUtils.getMetaDataValue(mActivity, "google_client_id"));//775358139434-v3h256aimo98rno1colkjevmqo6966kp.apps.googleusercontent.com");
						LogUtils.d("token= "+token);
	//					checkGoogleLogin(id,token);
						Bundle bundle = new Bundle();
						bundle.putString("id", id);
						bundle.putString("token", token);
						bundle.putString("nickname", nickname);
						Message msg = new Message();
						msg.what = 1;
						msg.setData(bundle);
						mHandler.sendMessage(msg);
					} catch (Exception e) {
						LogUtils.d("Exception= "+e.getMessage());
					}
				}
			}).start();
		}
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		mApiClient.connect();
	}
}