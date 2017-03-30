package com.tianyou.sdk.fragment.login;

import java.util.List;
import java.util.Map;

import com.google.android.gms.common.ConnectionResult;
import com.google.gson.Gson;
import com.tianyou.sdk.activity.MenuActivity;
import com.tianyou.sdk.base.BaseLoginFragment;
import com.tianyou.sdk.base.LoginAdapter;
import com.tianyou.sdk.base.LoginAdapter.AdapterCallback;
import com.tianyou.sdk.bean.LoginWay;
import com.tianyou.sdk.bean.LoginWay.ResultBean;
import com.tianyou.sdk.bean.LoginWay.ResultBean.CustominfoBean;
import com.tianyou.sdk.holder.ConfigHolder;
import com.tianyou.sdk.holder.LoginInfoHandler;
import com.tianyou.sdk.holder.SPHandler;
import com.tianyou.sdk.utils.LogUtils;
import com.tianyou.sdk.utils.ResUtils;
import com.tianyou.sdk.utils.ToastUtils;

import android.app.Fragment;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

/**
 * 账号登录页面
 * @author itstrong
 *
 */
public class AccountFragment extends BaseLoginFragment {

	private EditText mEditAccount;
	private EditText mEditPassword;
	private View mImgWayQQ;
	private View mImgWayWechat;
	private ImageView mImgUserList;
	
	private PopupWindow mPopupWindow;
	private ListView mListView;
	private List<Map<String, String>> mLoginInfos;			//当前显示的登录信息
	
	public static Fragment getInstance(boolean isSwitchAccount) {
		Fragment fragment = new AccountFragment();
		Bundle bundle = new Bundle();
		bundle.putBoolean("isSwitchAccount", isSwitchAccount);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	@Override
	protected String setContentView() { return "fragment_login_account"; }

	@Override
	protected void initView() {
		mContentView.findViewById(ResUtils.getResById(mActivity, "btn_home_entry", "id")).setOnClickListener(this);
		mContentView.findViewById(ResUtils.getResById(mActivity, "text_home_quick", "id")).setOnClickListener(this);
		mContentView.findViewById(ResUtils.getResById(mActivity, "text_home_code", "id")).setOnClickListener(this);
		if (ConfigHolder.isOverseas) {
			mContentView.findViewById(ResUtils.getResById(mActivity, "text_home_code", "id")).setVisibility(View.GONE);
		}
		mContentView.findViewById(ResUtils.getResById(mActivity, "img_login_way_2", "id")).setOnClickListener(this);
		
		mEditAccount = (EditText)mContentView.findViewById(ResUtils.getResById(mActivity, "edit_home_phone", "id"));
		mEditPassword = (EditText)mContentView.findViewById(ResUtils.getResById(mActivity, "edit_home_code", "id"));
		mImgUserList = (ImageView) mContentView.findViewById(ResUtils.getResById(mActivity, "img_home_user_list", "id"));
		
		mImgWayQQ = mContentView.findViewById(ResUtils.getResById(mActivity, "img_login_way_0", "id"));
		mImgWayWechat = mContentView.findViewById(ResUtils.getResById(mActivity, "img_login_way_1", "id"));
		
		mImgUserList.setOnClickListener(this);
		mImgWayQQ.setOnClickListener(this);
		mImgWayWechat.setOnClickListener(this);
		
		if (ConfigHolder.isOverseas) {
			View loginWay2 = mContentView.findViewById(ResUtils.getResById(mActivity, "img_login_way_2", "id"));
			loginWay2.setVisibility(View.GONE);
			View loginWay3 = mContentView.findViewById(ResUtils.getResById(mActivity, "img_login_way_3", "id"));
			loginWay3.setVisibility(View.VISIBLE);
			loginWay3.setOnClickListener(this);
			View loginWay4 = mContentView.findViewById(ResUtils.getResById(mActivity, "img_login_way_4", "id"));
			loginWay4.setVisibility(View.VISIBLE);
			loginWay4.setOnClickListener(this);
		} else {
			showLoginWay();
		}
	}

	@Override
	protected void initData() {
		mActivity.setFragmentTitle(getResources().getString(ResUtils.getResById(mActivity, "ty_account_login2", "string")));
		
		mLoginInfos = LoginInfoHandler.getLoginInfo(LoginInfoHandler.LOGIN_INFO_ACCOUNT);
		if (mLoginInfos.size() == 0) {
			mImgUserList.setVisibility(View.GONE);
		} else {
			mImgUserList.setVisibility(View.VISIBLE);
			mEditAccount.setText(mLoginInfos.get(0).get(LoginInfoHandler.USER_ACCOUNT));
			mEditPassword.setText(mLoginInfos.get(0).get(LoginInfoHandler.USER_PASSWORD));
		}
        mListView = new ListView(mActivity);
        mListView.setBackgroundResource(ResUtils.getResById(mActivity, "listview_background", "drawable"));
        mListView.setAdapter(new LoginAdapter(mActivity, mLoginInfos, mAdapterCallback));
        
		List<Map<String, String>> loginInfo = LoginInfoHandler.getLoginInfo(LoginInfoHandler.LOGIN_INFO_ACCOUNT);
		if (loginInfo.size() != 0 && getArguments() != null && !getArguments().getBoolean("isSwitchAccount") && !mActivity.mIsLogout) {
			Map<String, String> map = loginInfo.get(0);
			String username = map.get(LoginInfoHandler.USER_ACCOUNT);
			String password = map.get(LoginInfoHandler.USER_PASSWORD);
			mLoginHandler.doUserLogin(username, password, false);
			return;
		}
		
        mEditPassword.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View arg0, boolean hasFocus) {
				if(hasFocus) mEditPassword.setText("");
			}
		});
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == ResUtils.getResById(mActivity, "img_login_way_0", "id")) {
			doQQLogin();
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_home_code", "id")) {
			forgetPassword();
		} else if (v.getId() == ResUtils.getResById(mActivity, "img_login_way_1", "id")) {
			// TODO	微信登陆
		} else if (v.getId() == ResUtils.getResById(mActivity, "img_login_way_2", "id")) {
			mActivity.switchFragment(new PhoneFragment(), "PhoneFragment");
		} else if (v.getId() == ResUtils.getResById(mActivity, "img_login_way_3", "id")) {
			mActivity.clickFacebook();
		} else if (v.getId() == ResUtils.getResById(mActivity, "img_login_way_4", "id") && !mActivity.getGoogleApiClient().isConnected()) {
			mActivity.setIsGoogleConnected(true);
			ConnectionResult connectionResult = mActivity.getConnectionResult();
			if (connectionResult == null) {
				LogUtils.d("connection == null");
			} else {
				try {
					connectionResult.startResolutionForResult(mActivity, 1);
				} catch (SendIntentException e) {
					connectionResult = null;
					mActivity.getGoogleApiClient().connect();
					e.printStackTrace();
				}
			}
//			try {
//				ConnectionResult connectionResult = mActivity.getConnectionResult();
//				if (mActivity.mConnectionResult != null) {
//					mActivity.mConnectionResult.startResolutionForResult(mActivity, 1);
//				} else {
//					if (mActivity.getGoogleApiClient() != null){ 
//						mActivity.getGoogleApiClient().connect(); 
//					}
//				}
//			} catch (SendIntentException e) {
//				if (mActivity.getGoogleApiClient() != null){ 
//					mActivity.getGoogleApiClient().connect(); 
//				}
//				e.printStackTrace();
//			}
		} else if (v.getId() == ResUtils.getResById(mActivity, "btn_home_entry", "id")) {
			doLogin();
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_home_quick", "id")) {
			quickRegisterSwitch();
		} else if (v.getId() == ResUtils.getResById(mActivity, "img_home_user_list", "id")) {
			showPopupWindow();
		}
	}

	private void showLoginWay() {
		String response = SPHandler.getString(mActivity, SPHandler.SP_LOGIN_WAY);
		LoginWay loginWay = new Gson().fromJson(response, LoginWay.class);
		ResultBean result = loginWay.getResult();
		if (result.getCode() == 200) {
			CustominfoBean custominfo = result.getCustominfo();
			mImgWayQQ.setVisibility(custominfo.getQq_quick() == 1 ? View.VISIBLE : View.GONE);
			mImgWayWechat.setVisibility(custominfo.getWx_quick() == 1 ? View.VISIBLE : View.GONE);
		} else {
			ToastUtils.show(mActivity, result.getMsg());
		}
	}
	
	private void doLogin() {
		String username = mEditAccount.getText().toString();
		String password = mEditPassword.getText().toString();
		if (username.isEmpty() || password.isEmpty()) {
			ToastUtils.show(mActivity, (ConfigHolder.isOverseas? "The user name or password cannot be empty":"用户名或密码不能为空"));
		} else {
			mLoginHandler.doUserLogin(username, password, false);
		}
	}

	AdapterCallback mAdapterCallback = new AdapterCallback() {
		@Override
		public List<Map<String, String>> userClick(Map<String, String> map) {
			mEditAccount.setText(map.get(LoginInfoHandler.USER_ACCOUNT));
			mEditPassword.setText(map.get(LoginInfoHandler.USER_PASSWORD));
			LoginInfoHandler.putLoginInfo(LoginInfoHandler.LOGIN_INFO_ACCOUNT, map);
			mLoginInfos = LoginInfoHandler.getLoginInfo(LoginInfoHandler.LOGIN_INFO_ACCOUNT);
			mPopupWindow.dismiss();
			return mLoginInfos;
		}
		
		@Override
		public void confirmDelete() {
			LoginInfoHandler.saveLoginInfo(LoginInfoHandler.LOGIN_INFO_ACCOUNT, mLoginInfos);
			if (mLoginInfos.size() == 0) {
				mImgUserList.setVisibility(View.GONE);
				mEditAccount.setText("");
				mEditPassword.setText("");
			} else {
				mEditAccount.setText(mLoginInfos.get(0).get(LoginInfoHandler.USER_ACCOUNT));
				mEditPassword.setText(mLoginInfos.get(0).get(LoginInfoHandler.USER_PASSWORD));
			}
			mPopupWindow.dismiss();
		}
		
		@Override
		public void cancelDelete() {
			mPopupWindow.dismiss();
		}
	};
	
	// 忘记密码
	private void forgetPassword() {
		Intent intent = new Intent(mActivity, MenuActivity.class);
		intent.putExtra("menu_type", MenuActivity.POPUP_MENU_6);
		mActivity.startActivity(intent);
	}

	// 用户登录下拉弹窗
	private void showPopupWindow() {
		if (mPopupWindow == null) {
			mPopupWindow = new PopupWindow(mListView, 0, 0);
			mPopupWindow.setWidth(mEditAccount.getWidth());
			mPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
			mPopupWindow.setContentView(mListView);
			mPopupWindow.setBackgroundDrawable(getResources().getDrawable(ResUtils.getResById(mActivity, "shape_btn_gray", "drawable")));
			mPopupWindow.setFocusable(true);
		}
		mPopupWindow.showAsDropDown(mEditAccount, 0, 0);
	}

	// 显示隐藏登录方式
//	private void showLoginWay() {
//		String response = SPHandler.getString(mActivity, SPHandler.SP_LOGIN_WAY);
//		LoginWay loginWay = new Gson().fromJson(response, LoginWay.class);
//		ResultBean result = loginWay.getResult();
//		if (result.getCode() == 200) {
//			CustominfoBean custominfo = result.getCustominfo();
//			mImgWayQQ.setVisibility(custominfo.getQq_quick() == 1 ? View.VISIBLE : View.GONE);
//			mImgWayWechat.setVisibility(custominfo.getWx_quick() == 1 ? View.VISIBLE : View.GONE);
//		} else {
//			ToastUtils.show(mActivity, result.getMsg());
//		}
//	}
}
