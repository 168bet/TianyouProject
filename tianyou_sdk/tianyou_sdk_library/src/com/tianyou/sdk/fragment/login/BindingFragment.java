package com.tianyou.sdk.fragment.login;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tianyou.sdk.base.BaseLoginFragment;
import com.tianyou.sdk.bean.PhoneCode;
import com.tianyou.sdk.holder.ConfigHolder;
import com.tianyou.sdk.holder.URLHolder;
import com.tianyou.sdk.utils.AppUtils;
import com.tianyou.sdk.utils.HttpUtils;
import com.tianyou.sdk.utils.LogUtils;
import com.tianyou.sdk.utils.HttpUtils.HttpsCallback;
import com.tianyou.sdk.utils.ResUtils;
import com.tianyou.sdk.utils.ToastUtils;

public class BindingFragment extends BaseLoginFragment {

	private EditText mEditPhone;
	private EditText mEditCode;
	private TextView mTextCode;
	private String mLoginCode;								//验证码
	
	public static Fragment getInstance(String userId, String username, String password, String usertoken) {
		Fragment fragment = new BindingFragment();
		Bundle bundle = new Bundle();
		bundle.putString("userId", userId);
		bundle.putString("username", username);
		bundle.putString("password", password);
		bundle.putString("usertoken", usertoken);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	@Override
	protected String setContentView() {
		return "fragment_login_binding";
	}

	@Override
	protected void initView() {
		mEditPhone = (EditText)mContentView.findViewById(ResUtils.getResById(mActivity, "edit_home_phone", "id"));
		mTextCode = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_home_code", "id"));
		mEditCode = (EditText)mContentView.findViewById(ResUtils.getResById(mActivity, "edit_home_code", "id"));
		mContentView.findViewById(ResUtils.getResById(mActivity, "btn_home_entry", "id")).setOnClickListener(this);
		mContentView.findViewById(ResUtils.getResById(mActivity, "text_quick_switch", "id")).setOnClickListener(this);
		mContentView.findViewById(ResUtils.getResById(mActivity, "text_quick_server", "id")).setOnClickListener(this);
		mTextCode.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		LogUtils.d("userId:" + getArguments().getString("userId") + "===" + getArguments().getString("usertoken"));
		mActivity.setFragmentTitle(mActivity.getString(ResUtils.getResById(mActivity, "ty_binding_phone", "string")));
        mEditCode.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View arg0, boolean hasFocus) {
				if(hasFocus) mEditCode.setText("");
			}
		});
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == ResUtils.getResById(mActivity, "btn_home_entry", "id")) {
			doEntryGame();
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_home_code", "id")) {
			getVerificationCode();
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_quick_switch", "id")) {
			mActivity.switchFragment(new AccountFragment(), "AccountFragment");
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_quick_server", "id")) {
			AppUtils.openServerTerms(mActivity);
		}
	}
	
	// 登录游戏
	private void doEntryGame() {
		String phone = mEditPhone.getText().toString();
		if (!AppUtils.isMobileNO(phone)) {
			ToastUtils.show(mActivity, "手机号格式错误");
			return;
		}
		String code = mEditCode.getText().toString();
		if (code.isEmpty()) {
			ToastUtils.show(mActivity, "请输入验证码");
		} else if (mLoginCode == null || !code.equals(mLoginCode)) {
			ToastUtils.show(mActivity, "验证码输入有误");
		} else {
			Map<String,String> map = new HashMap<String, String>();
			map.put("mobile", phone);
			map.put("userid", getArguments().getString("userId"));
			map.put("mobile_code", mLoginCode);
			map.put("usertoken", getArguments().getString("usertoken"));
			map.put("appID", ConfigHolder.GAME_ID);
			HttpUtils.post(mActivity, URLHolder.URL_BINDING_PHONE, map, new HttpsCallback() {
				@Override
				public void onSuccess(String response) {
					try {
						JSONObject jsonObject = new JSONObject(response);
						JSONObject result = jsonObject.getJSONObject("result");
						ToastUtils.show(mActivity, result.getString("msg"));
						if (result.getInt("code") == 200) {
							mLoginHandler.onUserLogin(getArguments().getString("username"), getArguments().getString("password"), false);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}
	
	// 获取验证码
	private void getVerificationCode() {
		String phone = mEditPhone.getText().toString();
		if (phone.isEmpty()) {
			ToastUtils.show(mActivity, "手机号不能为空");
		} else if (!AppUtils.isMobileNO(phone)) {
			ToastUtils.show(mActivity, "手机号格式错误");
		} else {
			Map<String, String> map = new HashMap<String, String>();
            map.put("mobile", phone);
            map.put("send_code", AppUtils.MD5(phone));
            map.put("send_type", "verification");
            map.put("appID", ConfigHolder.GAME_ID);
			HttpUtils.post(mActivity, URLHolder.URL_GET_CODE, map, new HttpsCallback() {
				@Override
				public void onSuccess(String response) {
					PhoneCode code = new Gson().fromJson(response, PhoneCode.class);
					if (code.getResult().getCode() == 200) {
						mLoginCode = code.getResult().getMobile_code();
						mTextCode.setBackgroundColor(Color.parseColor("#666666"));
						mTextCode.setTextColor(Color.WHITE);
						mTextCode.setClickable(false);
						createDelayed();
						mEditCode.setText("");
					}
					ToastUtils.show(mActivity, code.getResult().getMsg());
				}
			});
		}
	}
	
	private Handler handler;
	private int time;
	
	// 创建定时器
	private void createDelayed() {
		time = 60;
		handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (time != 0) {
					mTextCode.setText("重新发送(" + time-- + ")");
					handler.postDelayed(this, 1000);
				} else {
					mTextCode.setText("获取验证码");
					mTextCode.setTextColor(Color.WHITE);
					mTextCode.setClickable(true);
					mTextCode.setBackgroundColor(Color.parseColor("#36C6DA"));
					handler.removeCallbacks(this);
				}
			}
		}, 1000);
	}
}
