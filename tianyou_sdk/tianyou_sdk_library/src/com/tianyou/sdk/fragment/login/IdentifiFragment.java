package com.tianyou.sdk.fragment.login;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.tianyou.sdk.activity.LoginActivity;
import com.tianyou.sdk.base.BaseFragment;
import com.tianyou.sdk.bean.Identifi;
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
 * 实名认证页面
 * @author itstrong
 *
 */
public class IdentifiFragment extends BaseFragment {

	private EditText mEditPhone;
	private EditText mEditCode;
	private EditText mEditName;
	private EditText mEditIdCard;

	@Override
	protected String setContentView() { return "fragment_login_identifi"; }

	@Override
	protected void initView() {
		mActivity.setFragmentTitle("安全实名认证");
		((LoginActivity)mActivity).setBackBtnVisible(true);
		mContentView.findViewById(ResUtils.getResById(mActivity, "text_identifi_confirm", "id")).setOnClickListener(this);
		mContentView.findViewById(ResUtils.getResById(mActivity, "text_identifi_code", "id")).setOnClickListener(this);
		
		mEditPhone = (EditText) mContentView.findViewById(ResUtils.getResById(mActivity, "edit_identifi_phone", "id"));
		mEditCode = (EditText) mContentView.findViewById(ResUtils.getResById(mActivity, "edit_identifi_code", "id"));
		mEditName = (EditText) mContentView.findViewById(ResUtils.getResById(mActivity, "edit_identifi_name", "id"));
		mEditIdCard = (EditText) mContentView.findViewById(ResUtils.getResById(mActivity, "edit_identifi_id_card", "id"));
	}

	@Override
	protected void initData() { }

	@Override
	public void onClick(View v) {
		if (v.getId() == ResUtils.getResById(mActivity, "text_identifi_confirm", "id")) {
			submitInfo();
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_identifi_code", "id")) {
			getPhoneCode();
		}
	}

	private void getPhoneCode() { }

	private void submitInfo() {
		String phone = mEditPhone.getText().toString();
		String code = mEditCode.getText().toString();
		String name = mEditName.getText().toString();
		String idCard = mEditIdCard.getText().toString();
		if (phone.isEmpty()) {
			ToastUtils.show(mActivity, "手机号不能为空");
		} else if (!AppUtils.verifyPhoneNumber(phone)) {
			ToastUtils.show(mActivity, "手机号格式错误");
		} else if (code.isEmpty()) {
			ToastUtils.show(mActivity, "验证码不能为空");
		} else if (code.length() != 6) {
			ToastUtils.show(mActivity, "验证码长度错误");
		} else if (name.isEmpty()) {
			ToastUtils.show(mActivity, "真实姓名不能为空");
		} else if (name.length() < 2 || name.length() > 5) {
			ToastUtils.show(mActivity, "真实姓名长度错误");
		} else if (idCard.isEmpty()) {
			ToastUtils.show(mActivity, "身份证号码不能为空");
		} else if (idCard.length() != 18) {
			ToastUtils.show(mActivity, "身份证号码长度错误");
		} else {
			Map<String,String> map = new HashMap<String, String>();
			map.put("userid", ConfigHolder.userId);
			map.put("realname", name);
			map.put("cardnumber", idCard);
			map.put("mobile", phone);
			map.put("mobilecode", code);
			map.put("sign", AppUtils.MD5(ConfigHolder.gameId + ConfigHolder.gameToken + ConfigHolder.userId));
			HttpUtils.post(mActivity, URLHolder.URL_IDENTIFI, map, new HttpsCallback() {
				@Override
				public void onSuccess(String response) {
					Identifi identifi = new Gson().fromJson(response, Identifi.class);
					ToastUtils.show(mActivity, identifi.getResult().getMsg());
					if (identifi.getResult().getCode() == 200) {
						mActivity.finish();
					}
				}
			});
		}
	}
}
