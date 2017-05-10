package com.tianyou.sdk.activity;

import java.util.List;
import java.util.Map;

import com.tianyou.sdk.base.BaseActivity;
import com.tianyou.sdk.fragment.login.AccountFragment;
import com.tianyou.sdk.fragment.login.OneKeyFragment;
import com.tianyou.sdk.fragment.login.PersonalCenterFragment;
import com.tianyou.sdk.fragment.login.RegisterFragment;
import com.tianyou.sdk.fragment.login.TouristTipFragment;
import com.tianyou.sdk.holder.LoginHandler;
import com.tianyou.sdk.holder.LoginInfoHandler;
import com.tianyou.sdk.utils.ResUtils;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

/**
 * 登录Activity
 * @author itstrong
 * 
 */
public class LoginActivity extends BaseActivity {

	private View mLayoutRegisterTitle;
	private View mLayoutTitle;
	private View mViewBack;
	private TextView mTextPhone;
	private TextView mTextAccount;
	
	protected int setContentView() { return ResUtils.getResById(this, "activity_login", "layout"); }

	@Override
	protected void initView() {
		findViewById(ResUtils.getResById(mActivity, "img_login_close", "id")).setOnClickListener(this);
		
		mLayoutRegisterTitle = findViewById(ResUtils.getResById(mActivity, "layout_login_register_title", "id"));
		mLayoutTitle = findViewById(ResUtils.getResById(mActivity, "layout_login_title", "id"));
		mViewBack = findViewById(ResUtils.getResById(mActivity, "img_login_back", "id"));
		
		mTextPhone = (TextView) findViewById(ResUtils.getResById(mActivity, "text_login_register_phone", "id"));
		mTextAccount = (TextView) findViewById(ResUtils.getResById(mActivity, "text_login_register_account", "id"));
		
		mViewBack.setOnClickListener(this);
		mTextPhone.setOnClickListener(this);
		mTextAccount.setOnClickListener(this);
	}
	
	@Override
	protected void initData() {
		mIsLogout = getIntent().getBooleanExtra("is_logout", false);
		if (getIntent().getBooleanExtra("show_tourist_tip", false)) {
			switchFragment(new TouristTipFragment());
		} else if (getIntent().getIntExtra("login_type", 0) == 1) {
			switchFragment(new PersonalCenterFragment());
		} else {
			List<Map<String, String>> info1 = LoginInfoHandler.getLoginInfo(LoginInfoHandler.LOGIN_INFO_ACCOUNT);
			List<Map<String, String>> info2 = LoginInfoHandler.getLoginInfo(LoginInfoHandler.LOGIN_INFO_PHONE);
			if (info1.size() == 0 && info2.size() == 0) {
				switchFragment(new OneKeyFragment());
			} else {
				switchFragment(new AccountFragment());
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == ResUtils.getResById(mActivity, "img_login_close", "id")) {
			closeLoginActivity();
		} else if (v.getId() == ResUtils.getResById(mActivity, "img_login_back", "id")) {
			onBackPressed();
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_login_register_phone", "id")) {
			switchTitleState(true);
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_login_register_account", "id")) {
			switchTitleState(false);
		}
	}
	
	private void closeLoginActivity() {
		if (mFragmentTag.equals("TouristTipFragment")) {
			LoginHandler.onNoticeLoginSuccess();
		}
		finish();
	}

	private void switchTitleState(boolean isPhoneRegister) {
		mTextPhone.setTextColor(Color.parseColor(isPhoneRegister ? "#666666" : "#FFFFFF"));
		mTextAccount.setTextColor(Color.parseColor(isPhoneRegister ? "#FFFFFF" : "#666666"));
		mTextPhone.setBackgroundColor(Color.parseColor(isPhoneRegister ? "#FFFFFF" : "#999999"));
		mTextAccount.setBackgroundColor(Color.parseColor(isPhoneRegister ? "#999999" : "#FFFFFF"));
		switchFragment(RegisterFragment.getInstance(isPhoneRegister));
	}
	
	public void setRegisterTitle(boolean flag) {
		mLayoutTitle.setVisibility(flag ? View.GONE : View.VISIBLE);
		mLayoutRegisterTitle.setVisibility(flag ? View.VISIBLE : View.GONE);
	}
	
	public void setBackBtnVisible(boolean flag) {
		mViewBack.setVisibility(flag ? View.VISIBLE : View.INVISIBLE);
	}
}