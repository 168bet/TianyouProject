package com.tianyou.sdk.fragment.login;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.tianyou.sdk.activity.LoginActivity;
import com.tianyou.sdk.base.BaseFragment;
import com.tianyou.sdk.holder.ConfigHolder;
import com.tianyou.sdk.holder.URLHolder;
import com.tianyou.sdk.utils.AppUtils;
import com.tianyou.sdk.utils.HttpUtils;
import com.tianyou.sdk.utils.HttpUtils.HttpsCallback;
import com.tianyou.sdk.utils.ResUtils;
import com.tianyou.sdk.utils.ToastUtils;

import android.app.Fragment;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 用户注册页面
 * @author itstrong
 *
 */
public class RegisterFragment extends BaseFragment {

	private EditText mEditUsername;
	private EditText mEditCode;
	private EditText mEditPassword;
	private TextView mTextGetCode;
	private TextView mImgCode;
	
	private boolean mIsUserRegister;	//是否是用户注册
	
	public static Fragment getInstance(boolean isPhoneRegister) {
		Fragment fragment = new RegisterFragment();
		Bundle bundle = new Bundle();
		bundle.putBoolean("isPhoneRegister", isPhoneRegister);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	@Override
	protected String setContentView() { return "fragment_login_register"; }

	@Override
	protected void initView() {
		mEditUsername = (EditText) mContentView.findViewById(ResUtils.getResById(mActivity, "edit_register_username", "id"));
		mEditCode = (EditText) mContentView.findViewById(ResUtils.getResById(mActivity, "edit_register_code", "id"));
		mEditPassword = (EditText) mContentView.findViewById(ResUtils.getResById(mActivity, "edit_register_password", "id"));
		mTextGetCode = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_register_get_code", "id"));
		mImgCode = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "img_register_code", "id"));
		mContentView.findViewById(ResUtils.getResById(mActivity, "text_register_back", "id")).setOnClickListener(this);
		mContentView.findViewById(ResUtils.getResById(mActivity, "text_register_confirm", "id")).setOnClickListener(this);
		mTextGetCode.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		((LoginActivity)mActivity).setRegisterTitle(true);
		if (getArguments() != null) mIsUserRegister = !getArguments().getBoolean("isPhoneRegister");
		mEditUsername.setHint(mIsUserRegister ? "请输入账号" : "请输入手机号");
		mEditCode.setHint(mIsUserRegister ? "请输入密码" : "请输入验证码");
		mEditPassword.setHint(mIsUserRegister ? "请再次输入密码" : "密码：6-16位数字或字母组合");
		if (!mIsUserRegister) mEditUsername.setInputType(InputType.TYPE_CLASS_PHONE);
		mEditCode.setInputType((mIsUserRegister ? (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD) : InputType.TYPE_CLASS_NUMBER));
		mEditCode.setFilters((mIsUserRegister ? new InputFilter[]{new InputFilter.LengthFilter(16)} : new InputFilter[]{new InputFilter.LengthFilter(6)}));
		mTextGetCode.setVisibility(mIsUserRegister ? View.GONE : View.VISIBLE);
		mImgCode.setVisibility(mIsUserRegister ? View.VISIBLE : View.GONE);
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == ResUtils.getResById(mActivity, "text_register_back", "id")) {
			mActivity.switchFragment(new AccountFragment());
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_register_confirm", "id")) {
			registerAccount();
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_register_get_code", "id")) {
			getVerifiCode(mEditUsername.getText().toString(), mTextGetCode);
		}
	}
	
	// 立即注册
	private void registerAccount() {
		String username = mEditUsername.getText().toString();
		String code = mEditCode.getText().toString();
		String password = mEditPassword.getText().toString();
		if (username.isEmpty()) {
			ToastUtils.show(mActivity, "手机号不能为空");
		} else if (code.isEmpty()) {
			ToastUtils.show(mActivity, "验证码不能为空");
		} else if (password.isEmpty()) {
			ToastUtils.show(mActivity, "密码不能为空");
		} else if (!code.equals(mVerifiCode)) {
			ToastUtils.show(mActivity, "验证码输入错误");
		} else if (!AppUtils.verifyPhoneNumber(username)) {
			ToastUtils.show(mActivity, "手机号格式错误");
		} else {
			Map<String,String> map = new HashMap<String, String>();
			map.put("username", username);
			map.put("Mobilecode", code);
			map.put("password", password);
			map.put("channel", ConfigHolder.channelId);
			map.put("sign", AppUtils.MD5(username + password + ConfigHolder.gameId + ConfigHolder.gameToken));
			HttpUtils.post(mActivity, URLHolder.URL_REGISTER_PHONE, map, new HttpsCallback() {
				@Override
				public void onSuccess(String response) {
					try {
						JSONObject jsonObject = new JSONObject(response);
						JSONObject result = jsonObject.getJSONObject("result");
						ToastUtils.show(mActivity, result.getString("msg"));
						if (result.getInt("code") == 200) {
							mLoginHandler.doUserLogin(result.getString("username"), result.getString("password"), false);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}
}
