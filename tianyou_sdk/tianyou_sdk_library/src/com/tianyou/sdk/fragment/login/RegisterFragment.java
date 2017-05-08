package com.tianyou.sdk.fragment.login;

import com.tianyou.sdk.activity.LoginActivity;
import com.tianyou.sdk.base.BaseFragment;
import com.tianyou.sdk.utils.ResUtils;
import com.tianyou.sdk.utils.ToastUtils;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;

/**
 * 用户注册页面
 * @author itstrong
 *
 */
public class RegisterFragment extends BaseFragment {

	public static Fragment getInstance(boolean isPhoneRegister) {
		Fragment fragment = new RegisterFragment();
		Bundle bundle = new Bundle();
		bundle.putBoolean("isPhoneRegister", isPhoneRegister);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	@Override
	protected String setContentView() {
		if (getArguments() == null) {
			return "fragment_login_register_phone";
		} else {
			return getArguments().getBoolean("isPhoneRegister") ? "fragment_login_register_phone" : "fragment_login_register_account"; 
		}
	}

	@Override
	protected void initView() {
		((LoginActivity)mActivity).setRegisterTitle(true);
		mContentView.findViewById(ResUtils.getResById(mActivity, "text_register_back", "id")).setOnClickListener(this);;
		mContentView.findViewById(ResUtils.getResById(mActivity, "text_register_confirm", "id")).setOnClickListener(this);;
	}

	@Override
	protected void initData() {  }

	@Override
	public void onClick(View v) {
		if (v.getId() == ResUtils.getResById(mActivity, "text_register_back", "id")) {
			mActivity.switchFragment(new AccountFragment());
		}  else if (v.getId() == ResUtils.getResById(mActivity, "text_register_confirm", "id")) {
			ToastUtils.show(mActivity, "注册成功");
		}
	}
}
