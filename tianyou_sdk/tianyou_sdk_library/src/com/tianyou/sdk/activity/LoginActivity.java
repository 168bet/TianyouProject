package com.tianyou.sdk.activity;

import java.util.List;
import java.util.Map;

import com.tianyou.sdk.base.BaseActivity;
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
import com.tianyou.sdk.utils.LogUtils;
import com.tianyou.sdk.utils.ResUtils;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
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
	private View mLayoutBg;
	private TextView mTextPhone;
	private TextView mTextAccount;
	private ImageView mImgClose;
	
	private boolean mIsAccountRegister;
	
	protected int setContentView() { return ResUtils.getResById(this, "activity_login", "layout"); }

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
				List<Map<String, String>> info2 = LoginInfoHandler.getLoginInfo(LoginInfoHandler.LOGIN_INFO_PHONE);
				if (info1.size() == 0 && info2.size() == 0) {
					switchFragment(new OneKeyFragment());
				} else {
					switchFragment(new AccountFragment());
				}
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
			switchTitleState(false);
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_login_register_account", "id")) {
			switchTitleState(true);
		}
	}
	
	public void setCloseViw(boolean visibility) {
		mImgClose.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
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
			LoginHandler.onNoticeLoginSuccess();
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
}