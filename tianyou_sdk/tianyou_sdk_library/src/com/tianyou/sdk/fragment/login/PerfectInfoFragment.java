package com.tianyou.sdk.fragment.login;

import com.tianyou.sdk.base.BaseFragment;
import com.tianyou.sdk.utils.ResUtils;
import com.tianyou.sdk.utils.ToastUtils;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 完善QQ登陆账号信息
 * @author itstrong
 *
 */
public class PerfectInfoFragment extends BaseFragment {

	private EditText mEditUsername;
	private EditText mEditPassword;
	private EditText mEditAgain;
	private TextView mTextTip;

	public static Fragment getInstance(String nickname) {
		Fragment fragment = new PerfectInfoFragment();
		Bundle bundle = new Bundle();
		bundle.putString("qq_nickname", nickname);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	@Override
	protected String setContentView() { return "fragment_login_perfect_info"; }

	@Override
	protected void initView() {
		mEditUsername = (EditText) mContentView.findViewById(ResUtils.getResById(mActivity, "edit_perfect_username", "id"));
		mEditPassword = (EditText) mContentView.findViewById(ResUtils.getResById(mActivity, "edit_perfect_password", "id"));
		mEditAgain = (EditText) mContentView.findViewById(ResUtils.getResById(mActivity, "edit_perfect_again", "id"));
		mTextTip = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_perfect_tip", "id"));
		mContentView.findViewById(ResUtils.getResById(mActivity, "text_perfect_confirm", "id")).setOnClickListener(this);;
	}

	@Override
	protected void initData() {
		mActivity.setFragmentTitle("完善账号信息");
		if (getArguments() != null) {
			String param = getArguments().getString("qq_nickname");
			String nickname = param == null ? "天游用户" : param;
			mTextTip.setText("亲爱的" + nickname + "，请完善以下账号信息");
		}
	}

	@Override
	public void onClick(View v) {
		String username = mEditUsername.getText().toString();
		String password = mEditPassword.getText().toString();
		String again = mEditAgain.getText().toString();
		if (username.isEmpty() || password.isEmpty()) {
			ToastUtils.show(mActivity, "用户名或密码不能为空");
		} else if (username.length() < 6 || username.length() > 16) {
			ToastUtils.show(mActivity, "用户名长度错误");
		} else if (password.length() < 6 || password.length() > 16) {
			ToastUtils.show(mActivity, "用户名长度错误");
		} else if (!password.equals(again)) {
			ToastUtils.show(mActivity, "两次输入密码不一致");
		} else {
			mLoginHandler.doPerfectAccountInfo(username, password);
		}
	}
}
