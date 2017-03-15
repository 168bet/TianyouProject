package com.tianyou.sdk.fragment.login;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.tianyou.sdk.base.BaseLoginFragment;
import com.tianyou.sdk.bean.LoginInfo;
import com.tianyou.sdk.holder.ConfigHolder;
import com.tianyou.sdk.holder.URLHolder;
import com.tianyou.sdk.utils.AppUtils;
import com.tianyou.sdk.utils.HttpUtils;
import com.tianyou.sdk.utils.HttpUtils.HttpsCallback;
import com.tianyou.sdk.utils.ResUtils;
import com.tianyou.sdk.utils.ToastUtils;

import android.view.View;
import android.widget.EditText;

/**
 * 工会注册页面
 * @author itstrong
 *
 */
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
			Map<String,String> map = new HashMap<String, String>();
			map.put("username", username);
			map.put("password", password);
			map.put("repassword", password);
			map.put("appid", ConfigHolder.gameId);
			map.put("token", ConfigHolder.gameToken);
			map.put("channel", ConfigHolder.channelId);
			map.put("type", "android");
			map.put("imei", AppUtils.getPhoeIMEI(mActivity));
			map.put("sign", AppUtils.MD5(username + password + ConfigHolder.gameId + ConfigHolder.gameToken));
			map.put("signtype", "md5");
			HttpUtils.post(mActivity, URLHolder.URL_UNION_REGISTER, map, new HttpsCallback() {
				@Override
				public void onSuccess(String response) {
					LoginInfo request = new Gson().fromJson(response, LoginInfo.class);
					mLoginHandler.onLoginProcess(request);
				}
			});
		}
	}
	
}
