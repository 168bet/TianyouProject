package com.tianyou.sdk.fragment.login;

import android.view.View;
import android.widget.EditText;

import com.tianyou.sdk.base.BaseLoginFragment;
import com.tianyou.sdk.utils.ResUtils;
import com.tianyou.sdk.utils.ToastUtils;

public class RegisterFragment extends BaseLoginFragment {

	private EditText mEditUsername;
	private EditText mEditPassword;
	private EditText mEditAgain;

	@Override
	protected String setContentView() { return "fragment_login_register"; }

	@Override
	protected void initView() {
		mEditUsername = (EditText) mContentView.findViewById(ResUtils.getResById(mActivity, "edit_register_username", "id"));
		mEditPassword = (EditText) mContentView.findViewById(ResUtils.getResById(mActivity, "edit_register_password", "id"));
		mEditAgain = (EditText) mContentView.findViewById(ResUtils.getResById(mActivity, "edit_register_again", "id"));
		mContentView.findViewById(ResUtils.getResById(mActivity, "btn_register_confirm", "id")).setOnClickListener(this);
	}

	@Override
	protected void initData() { mActivity.setFragmentTitle("工会注册"); }

	@Override
	public void onClick(View v) {
		String username = mEditUsername.getText().toString();
		String password = mEditPassword.getText().toString();
		String again = mEditAgain.getText().toString();
		if (username.isEmpty() || password.isEmpty()) {
			ToastUtils.show(mActivity, "用户名或密码不能为空");
		} else if (username.length() < 6 || username.length() > 18) {
			ToastUtils.show(mActivity, "请输入6-18位长度用户名");
		} else if (password.length() < 6 || password.length() > 18) {
			ToastUtils.show(mActivity, "请输入6-18位长度密码");
		} else if (!again.equals(password)) {
			ToastUtils.show(mActivity, "2次密码输入不一致");
		} else {
			ToastUtils.show(mActivity, "注册成功");
		}
	}
	
}
