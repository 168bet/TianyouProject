package com.tianyou.sdk.fragment.login;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.string;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.tianyou.sdk.activity.LoginActivity;
import com.tianyou.sdk.base.BaseFragment;
import com.tianyou.sdk.fragment.login.AlertPasswordFragment.AlertType;
import com.tianyou.sdk.holder.ConfigHolder;
import com.tianyou.sdk.holder.URLHolder;
import com.tianyou.sdk.utils.AppUtils;
import com.tianyou.sdk.utils.HttpUtils;
import com.tianyou.sdk.utils.HttpUtils.HttpsCallback;
import com.tianyou.sdk.utils.ResUtils;
import com.tianyou.sdk.utils.ToastUtils;

public class ForgetPasswordFragment extends BaseFragment {

	private EditText mEditUsername;

	@Override
	protected String setContentView() { return "fragment_login_forget"; }

	@Override
	protected void initView() {
		mEditUsername = (EditText) mContentView.findViewById(ResUtils.getResById(mActivity, "edit_forget_username", "id"));
		mContentView.findViewById(ResUtils.getResById(mActivity, "text_forget_confirm", "id")).setOnClickListener(this);
	}

	@Override
	protected void initData() {
		mActivity.setFragmentTitle(ResUtils.getString(mActivity, "ty_forget_password"));
		Bundle bundle = getArguments();  
		String username = bundle.getString("mEditUsername");  
		if(!username.isEmpty()){
			mEditUsername.setText(username);
		}
		((LoginActivity)mActivity).setBackBtnVisible(true);
	}

	@Override
	public void onClick(View v) {
		final String username = mEditUsername.getText().toString();
		if (username.isEmpty()) {
			ToastUtils.show(mActivity,!ConfigHolder.isOverseas?"账号不能为空":"The account cannot be empty");
		} else if (username.length() < 6 || username.length() > 16) {
			ToastUtils.show(mActivity,!ConfigHolder.isOverseas?"账号长度错误":"Length error");
		} else {
			Map<String,String> map = new HashMap<String, String>();
			map.put("username", username);
			map.put("sign", AppUtils.MD5(username + ConfigHolder.gameId));
			HttpUtils.post(mActivity, URLHolder.URL_CHECK_PHONE, map, new HttpsCallback() {
				@Override
				public void onSuccess(String response) {
					try {
						JSONObject jsonObject = new JSONObject(response);
						JSONObject result = jsonObject.getJSONObject("result");
						if (result.getInt("code") == 200) {
							int mobile = result.getInt("mobile");
							String phone = result.getString("mobileinfo");
							if (mobile == 1) {
								mActivity.switchFragment(new AlertPasswordFragment(AlertType.ALERT_TYPE_PHONE_0, username, phone));
							} else {
								mActivity.switchFragment(new AlertPasswordFragment(AlertType.ALERT_TYPE_ACCOUNT, username, phone));
							}
						} else {
							ToastUtils.show(mActivity, result.getString("msg"));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}
}
