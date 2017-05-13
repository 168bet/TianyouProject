package com.tianyou.sdk.fragment.login;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.tianyou.sdk.base.BaseFragment;
import com.tianyou.sdk.holder.ConfigHolder;
import com.tianyou.sdk.holder.URLHolder;
import com.tianyou.sdk.utils.AppUtils;
import com.tianyou.sdk.utils.HttpUtils;
import com.tianyou.sdk.utils.HttpUtils.HttpsCallback;
import com.tianyou.sdk.utils.ResUtils;
import com.tianyou.sdk.utils.ToastUtils;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 账号安全设置页面
 * @author itstrong
 * @param <V>
 *
 */
public class SafetySettingFragment extends BaseFragment {

	private EditText mEditPhone;
	private EditText mEditCode;
	private TextView mTextCode;
	private TextView mTextPhone;
	
	private boolean mIsPhone;	//是否是已绑定手机页面

	public SafetySettingFragment(boolean isPhone) {
		mIsPhone = isPhone;
	}
	
	@Override
	protected String setContentView() { return mIsPhone ? "fragment_login_safety_setting0" : "fragment_login_safety_setting1"; }

	@Override
	protected void initView() {
		mContentView.findViewById(ResUtils.getResById(mActivity, "text_safety_confirm", "id")).setOnClickListener(this);
		mTextCode = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_setting_code", "id"));
		mEditCode = (EditText) mContentView.findViewById(ResUtils.getResById(mActivity, "edit_setting_code", "id"));
		if (mIsPhone) {
			mTextPhone = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_setting_phone", "id"));
		} else {
			mEditPhone = (EditText) mContentView.findViewById(ResUtils.getResById(mActivity, "edit_setting_phone", "id"));
		}
		mTextCode.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		mActivity.setFragmentTitle("账号安全设置");
		String phone = ConfigHolder.userPhone;
		if (mIsPhone) mTextPhone.setText(phone.substring(0, 3) + "****" + phone.substring(7, 11));
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == ResUtils.getResById(mActivity, "text_safety_confirm", "id")) {
			if (mIsPhone) {
				updatePhone();
			} else {
				bindingPhone();
			}
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_setting_code", "id")) {
			if (mIsPhone) {
				getVerifiCode(ConfigHolder.userPhone, mTextCode, SendType.SEND_TYPE_BIND_PHONE);
			} else {
				getVerifiCode(mEditPhone, mTextCode, SendType.SEND_TYPE_BIND_PHONE);
			}
		}
	}

	private void updatePhone() {
		String code = mEditCode.getText().toString();
		if (code.isEmpty()) {
			ToastUtils.show(mActivity, "验证码不能为空");
		} else if (!code.equals(mVerifiCode)) {
			ToastUtils.show(mActivity, "验证码输入错误");
		} else {
			mActivity.switchFragment(new SafetySettingFragment(false));
		}
	}

	private void bindingPhone() {
		final String phone = mEditPhone.getText().toString();
		String code = mEditCode.getText().toString();
		if (phone.isEmpty()) {
			ToastUtils.show(mActivity, "手机号不能为空");
		} else if (!AppUtils.verifyPhoneNumber(phone)) {
			ToastUtils.show(mActivity, "手机号格式错误");
		} else if(code.isEmpty()) {
			ToastUtils.show(mActivity, "验证码不能为空");
		} else {
			Map<String,String> map = new HashMap<String, String>();
			map.put("userid", ConfigHolder.userId);
			map.put("mobile", phone);
			map.put("mobile_code", code);
			map.put("sign", AppUtils.MD5(ConfigHolder.gameId + ConfigHolder.gameToken + phone));
			HttpUtils.post(mActivity, URLHolder.URL_BINDING_PHONE, map, new HttpsCallback() {
				@Override
				public void onSuccess(String response) {
					try {
						JSONObject jsonObject = new JSONObject(response);
						JSONObject result = jsonObject.getJSONObject("result");
						ToastUtils.show(mActivity, result.getString("msg"));
						if (result.getInt("code") == 200) {
							ConfigHolder.isPhone = true;
							ConfigHolder.userName = phone;
							mActivity.finish();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}
}
