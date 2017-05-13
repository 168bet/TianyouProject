package com.tianyou.sdk.fragment.login;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.tianyou.sdk.base.BaseFragment;
import com.tianyou.sdk.fragment.login.AlertPasswordFragment.AlertType;
import com.tianyou.sdk.holder.ConfigHolder;
import com.tianyou.sdk.holder.URLHolder;
import com.tianyou.sdk.utils.AppUtils;
import com.tianyou.sdk.utils.HttpUtils;
import com.tianyou.sdk.utils.HttpUtils.HttpsCallback;
import com.tianyou.sdk.utils.ResUtils;
import com.tianyou.sdk.utils.ToastUtils;

import android.view.View;
import android.widget.EditText;

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
		mActivity.setFragmentTitle("忘记密码");
	}

	@Override
	public void onClick(View v) {
		final String username = mEditUsername.getText().toString();
		if (username.isEmpty()) {
			ToastUtils.show(mActivity, "账号不能为空");
		} else if (username.length() < 6 || username.length() > 16) {
			ToastUtils.show(mActivity, "账号长度错误");
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
