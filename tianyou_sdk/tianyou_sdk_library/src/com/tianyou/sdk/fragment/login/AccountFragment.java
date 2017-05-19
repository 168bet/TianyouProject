package com.tianyou.sdk.fragment.login;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.tianyou.sdk.activity.LoginActivity;
import com.tianyou.sdk.base.BaseFragment;
import com.tianyou.sdk.base.LoginAdapter;
import com.tianyou.sdk.base.LoginAdapter.AdapterCallback;
import com.tianyou.sdk.bean.LoginWay;
import com.tianyou.sdk.bean.LoginWay.ResultBean;
import com.tianyou.sdk.holder.ConfigHolder;
import com.tianyou.sdk.holder.LoginInfoHandler;
import com.tianyou.sdk.holder.SPHandler;
import com.tianyou.sdk.utils.ResUtils;
import com.tianyou.sdk.utils.ToastUtils;

import android.app.Fragment;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
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
public class AccountFragment extends BaseFragment {

	private EditText mEditUsername;
	private EditText mEditPassword;
	private ImageView mImgSwitch;
	private View mImgPull;
	private View mViewLogin;
	
	private List<Map<String, String>> mLoginInfos;			//当前显示的登录信息
	private PopupWindow mPopupWindow;
	private ListView mListView;
	private boolean mIsOpenPassword;
	
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
		mEditUsername = (EditText) mContentView.findViewById(ResUtils.getResById(mActivity, "edit_account_username", "id"));
		mEditPassword = (EditText) mContentView.findViewById(ResUtils.getResById(mActivity, "edit_account_password", "id"));
		mImgSwitch = (ImageView) mContentView.findViewById(ResUtils.getResById(mActivity, "img_account_switch", "id"));
		
		mViewLogin = mContentView.findViewById(ResUtils.getResById(mActivity, "btn_account_login", "id"));
		mImgPull = mContentView.findViewById(ResUtils.getResById(mActivity, "img_account_pull", "id"));
		mViewTourist = mContentView.findViewById(ResUtils.getResById(mActivity, "text_account_tourist", "id"));
		
		mContentView.findViewById(ResUtils.getResById(mActivity, "layout_account_quick", "id")).setOnClickListener(this);
		mContentView.findViewById(ResUtils.getResById(mActivity, "layout_account_qq", "id")).setOnClickListener(this);
		mContentView.findViewById(ResUtils.getResById(mActivity, "text_account_forget", "id")).setOnClickListener(this);
		
		mImgPull.setOnClickListener(this);
		mViewLogin.setOnClickListener(this);
		mImgSwitch.setOnClickListener(this);
		mViewTourist.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		mActivity.setFragmentTitle("账号登录");
		((LoginActivity)mActivity).setCloseViw(false);
		mViewTourist.setVisibility(ConfigHolder.isUnion ? View.GONE : View.VISIBLE);
		mLoginInfos = LoginInfoHandler.getLoginInfo(LoginInfoHandler.LOGIN_INFO_ACCOUNT);
		if (mLoginInfos.size() == 0) {
			mImgPull.setVisibility(View.GONE);
		} else {
			mImgPull.setVisibility(View.VISIBLE);
			mEditUsername.setText(mLoginInfos.get(0).get(LoginInfoHandler.USER_ACCOUNT));
			mEditPassword.setText(mLoginInfos.get(0).get(LoginInfoHandler.USER_PASSWORD));
		}
		mListView = new ListView(mActivity);
        mListView.setBackgroundResource(ResUtils.getResById(mActivity, "listview_background", "drawable")); 
        mListView.setAdapter(new LoginAdapter(mActivity, mLoginInfos, mAdapterCallback));
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == ResUtils.getResById(mActivity, "img_account_pull", "id")) {
			showPopupWindow();
		} else if (v.getId() == ResUtils.getResById(mActivity, "btn_account_login", "id")) {
			doLogin();
		} else if (v.getId() == ResUtils.getResById(mActivity, "layout_account_quick", "id")) {
			doRegister();
		} else if (v.getId() == ResUtils.getResById(mActivity, "layout_account_qq", "id")) {
			doQQLogin();
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_account_tourist", "id")) {
			mLoginHandler.doQuickRegister();
		} else if (v.getId() == ResUtils.getResById(mActivity, "img_account_switch", "id")) {
			switchPassword();
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_account_forget", "id")) {
			mActivity.switchFragment(new ForgetPasswordFragment());
		}
	}

	private void doRegister() {
		String response = SPHandler.getString(mActivity, SPHandler.SP_LOGIN_WAY);
		LoginWay loginWay = new Gson().fromJson(response, LoginWay.class);
		ResultBean result = loginWay.getResult();
		if (result.getCode() == 200 && result.getCustominfo().getReg_quick() == 0) {
			ToastUtils.show(mActivity, "暂未开放");
		} else {
			((LoginActivity)mActivity).switchFragment(new PhoneRegisterFragment());
//			mActivity.switchFragment(new RegisterFragment());
		}
	}

	private void doLogin() {
		String username = mEditUsername.getText().toString();
		String password = mEditPassword.getText().toString();
		if (username.isEmpty() || password.isEmpty()) {
			ToastUtils.show(mActivity, "用户名或密码不能为空");
		} else if (username.length() < 6 || username.length() > 16) {
			ToastUtils.show(mActivity, "用户名长度错误");
		} else if (password.length() < 6 || password.length() > 16) {
			ToastUtils.show(mActivity, "密码长度错误");
		} else {
			mLoginHandler.doUserLogin(mEditUsername.getText().toString(), mEditPassword.getText().toString(), false);
		}
	}

	private void switchPassword() {
		mIsOpenPassword = !mIsOpenPassword;
		mEditPassword.setSingleLine();
		mEditPassword.setInputType(mIsOpenPassword ? InputType.TYPE_CLASS_TEXT : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		mImgSwitch.setImageResource(ResUtils.getResById(mActivity, mIsOpenPassword ? "ty2_eye_open" : "ty2_eye_close", "drawable"));
	}

	// 用户登录下拉弹窗
	private void showPopupWindow() {
		if (mPopupWindow == null) {
			mPopupWindow = new PopupWindow(mListView, 0, 0);
			mPopupWindow.setWidth(mViewLogin.getWidth());
			mPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
			mPopupWindow.setContentView(mListView);
			mPopupWindow.setBackgroundDrawable(getResources().getDrawable(ResUtils.getResById(mActivity, "shape_btn_gray", "drawable")));
			mPopupWindow.setFocusable(true);
		}
		mPopupWindow.showAsDropDown(mEditUsername, 0, 0);
	}
	
	AdapterCallback mAdapterCallback = new AdapterCallback() {
		@Override
		public List<Map<String, String>> userClick(Map<String, String> map) {
			mEditUsername.setText(map.get(LoginInfoHandler.USER_ACCOUNT));
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
				mImgPull.setVisibility(View.GONE);
				mEditUsername.setText("");
				mEditPassword.setText("");
			} else {
				mEditUsername.setText(mLoginInfos.get(0).get(LoginInfoHandler.USER_ACCOUNT));
				mEditPassword.setText(mLoginInfos.get(0).get(LoginInfoHandler.USER_PASSWORD));
			}
			mPopupWindow.dismiss();
		}
		
		@Override
		public void cancelDelete() {
			mPopupWindow.dismiss();
		}
	};
	private View mViewTourist;
}
