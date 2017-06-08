package com.tianyou.sdk.fragment.login;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tianyou.sdk.activity.LoginActivity;
import com.tianyou.sdk.base.BaseFragment;
import com.tianyou.sdk.bean.LoginInfo;
import com.tianyou.sdk.bean.PhoneCode;
import com.tianyou.sdk.holder.ConfigHolder;
import com.tianyou.sdk.holder.URLHolder;
import com.tianyou.sdk.utils.AppUtils;
import com.tianyou.sdk.utils.HttpUtils;
import com.tianyou.sdk.utils.HttpUtils.HttpsCallback;
import com.tianyou.sdk.utils.LogUtils;
import com.tianyou.sdk.utils.ResUtils;
import com.tianyou.sdk.utils.ToastUtils;

/**
 * 修改密码页面
 * @author itstrong
 *
 */
public class AlertPasswordFragment extends BaseFragment {

	public enum AlertType {
		ALERT_TYPE_PHONE_0,		//手机号修改密码显示密保手机
		ALERT_TYPE_PHONE_1,		//手机号修改密码输入验证码
		ALERT_TYPE_ACCOUNT,		//账号修改密码输入原密码
		ALERT_TYPE_NEW,			//手机号或账号修改密码输入新密码
	}
	
	private TextView mTextTips;
	private TextView mTextPhone;
	private TextView mTextCode;
	private EditText mEditCode;
	private EditText mEditPassword;
	private EditText mEditAgain;
	private ImageView mImgSwitch;
	private EditText mEditAccountPass;
	
	private AlertType mAlertType;
	private String mAccount;
	private String mBindPhone;
	private boolean mIsOpenPassword;
	
	AlertPasswordFragment(AlertType alertType, String account, String bindPhone) {
		mAlertType = alertType;
		mAccount = account;
		mBindPhone = bindPhone;
	}
	
	@Override
	protected String setContentView() {
		switch (mAlertType) {
		case ALERT_TYPE_PHONE_0:
			return "fragment_login_alert_phone_0";
		case ALERT_TYPE_PHONE_1:
			return "fragment_login_alert_phone_1";
		case ALERT_TYPE_ACCOUNT:		
			return "fragment_login_alert_account";
		default:
			break;
		}
		return "fragment_login_alert_new";
	}

	@Override
	protected void initView() {
		LogUtils.d("mAccount:" + mAccount);
		LogUtils.d("mBindPhone:" + mBindPhone);
		switch (mAlertType) {
		case ALERT_TYPE_PHONE_0:
			mContentView.findViewById(ResUtils.getResById(mActivity, "layout_alert_context", "id")).setOnClickListener(this);
			mTextTips = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_alert_tips", "id"));
			mTextPhone = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_alert_phone", "id"));
			break;
		case ALERT_TYPE_PHONE_1:
			mTextPhone = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_alert_phone", "id"));
			mEditCode = (EditText) mContentView.findViewById(ResUtils.getResById(mActivity, "edit_alert_code", "id"));
			mTextCode = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_alert_code", "id"));
			mContentView.findViewById(ResUtils.getResById(mActivity, "text_alert_confirm", "id")).setOnClickListener(this);
			mContentView.findViewById(ResUtils.getResById(mActivity, "text_alert_appeal", "id")).setOnClickListener(this);
			mTextCode.setOnClickListener(this);
			break;
		case ALERT_TYPE_ACCOUNT:
			mEditAccountPass = (EditText) mContentView.findViewById(ResUtils.getResById(mActivity, "edit_alert_account_password", "id"));
			mContentView.findViewById(ResUtils.getResById(mActivity, "text_alert_confirm", "id")).setOnClickListener(this);
			mImgSwitch = (ImageView) mContentView.findViewById(ResUtils.getResById(mActivity, "img_alert_switch", "id"));
			mContentView.findViewById(ResUtils.getResById(mActivity, "text_alert_forget", "id")).setOnClickListener(this);
			mContentView.findViewById(ResUtils.getResById(mActivity, "text_alert_confirm", "id")).setOnClickListener(this);
			mEditAccountPass.setOnClickListener(this);
			mImgSwitch.setOnClickListener(this);
			break;
		case ALERT_TYPE_NEW:
			mEditPassword = (EditText) mContentView.findViewById(ResUtils.getResById(mActivity, "edit_alert_password", "id"));
			mEditAgain = (EditText) mContentView.findViewById(ResUtils.getResById(mActivity, "edit_alert_again", "id"));
			mContentView.findViewById(ResUtils.getResById(mActivity, "text_alert_confirm", "id")).setOnClickListener(this);
			break;
		}
	}

	@Override
	protected void initData() {
		mActivity.setFragmentTitle("修改密码");
		((LoginActivity)mActivity).setBackBtnVisible(true);
		switch (mAlertType) {
		case ALERT_TYPE_PHONE_0:
			mTextTips.setText("您要修改的密码账号：" + mAccount);
			mTextPhone.setText(mBindPhone.substring(0, 3) + "****" + mBindPhone.substring(7, 11));
			break;
		case ALERT_TYPE_PHONE_1:
			mTextPhone.setText(mBindPhone.substring(0, 3) + "****" + mBindPhone.substring(7, 11));
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (mAlertType) {
		case ALERT_TYPE_PHONE_0:
			mActivity.switchFragment(new AlertPasswordFragment(AlertType.ALERT_TYPE_PHONE_1, mAccount, mBindPhone));
			break;
		case ALERT_TYPE_PHONE_1:
			if (v.getId() == ResUtils.getResById(mActivity, "text_alert_code", "id")) {
				getVerifiCode(mBindPhone, mTextCode, SendType.SEND_TYPE_UPDATE_PHONE);
			} else if (v.getId() == ResUtils.getResById(mActivity, "text_alert_confirm", "id")) {
				phoneAlertPassword();
			} else if (v.getId() == ResUtils.getResById(mActivity, "text_alert_appeal", "id")) {
				mActivity.switchFragment(new ServerFragment(!mBindPhone.isEmpty(), mAccount));
			}
			break;
		case ALERT_TYPE_ACCOUNT:
			if (v.getId() == ResUtils.getResById(mActivity, "text_alert_confirm", "id")) {
				verifiPassword();
			} else if (v.getId() == ResUtils.getResById(mActivity, "img_alert_switch", "id")) {
				switchPassword();
			} else if (v.getId() == ResUtils.getResById(mActivity, "text_alert_forget", "id")) {
				mActivity.switchFragment(new ServerFragment(!mBindPhone.isEmpty(), mAccount));
			}
			break;
		case ALERT_TYPE_NEW:
			alertPassword();
			break;
		default:
			break;
		}
	}
	
	private void phoneAlertPassword() {
		String code = mEditCode.getText().toString();
		if (code.isEmpty()) {
			ToastUtils.show(mActivity, "验证码不能为空");
		} else {
			Map<String, String> map = new HashMap<String, String>();
			map.put("username", mBindPhone);
	        map.put("verify", code);
	        map.put("type", "android");
	        map.put("imei", AppUtils.getPhoeIMEI(mActivity));
	        map.put("sign", AppUtils.MD5(mBindPhone + ConfigHolder.gameId));
			HttpUtils.post(mActivity, URLHolder.URL_VERIFY_CODE, map, new HttpsCallback() {
				@Override
				public void onSuccess(String response) {
					PhoneCode code = new Gson().fromJson(response, PhoneCode.class);
					if (code.getResult().getCode() == 200) {
						mTextCode.setClickable(false);
						mActivity.switchFragment(new AlertPasswordFragment(AlertType.ALERT_TYPE_NEW, mAccount, mBindPhone));
					} else {
						ToastUtils.show(mActivity, code.getResult().getMsg());
					}
				}
			});
			if (mAlertType == AlertType.ALERT_TYPE_ACCOUNT) {
				ConfigHolder.oldPassword = mEditAccountPass.getText().toString();
			}
		}
	}

	private void alertPassword() {
		String password = mEditPassword.getText().toString();
		String again = mEditAgain.getText().toString();
		if (password.isEmpty()) {
			ToastUtils.show(mActivity, "密码不能为空");
		} else if (password.length() < 6 || password.length() > 16) {
			ToastUtils.show(mActivity, "密码长度错误");
		} else if (!again.equals(password)) {
			ToastUtils.show(mActivity, "两次输入密码不一致");
		} else {
			postAlertPassword(password);
		}
	}
	
	//修改密码
	private void postAlertPassword(final String password) {
		Map<String,String> map = new HashMap<String, String>();
		map.put("username", mAccount);
		map.put("channel", ConfigHolder.channelId);
		if (!mBindPhone.isEmpty()) {
			map.put("mobile", mBindPhone);
			map.put("password", password);
		} else {
			map.put("password", ConfigHolder.oldPassword);
			map.put("newpassword", password);
		}
		map.put("sign", AppUtils.MD5(ConfigHolder.userName + password + ConfigHolder.gameId));
		String url = mBindPhone.isEmpty() ? URLHolder.URL_ALERT_ACCOUNT : URLHolder.URL_ALERT_PHONE;
		HttpUtils.post(mActivity, url, map, new HttpsCallback() {
			@Override
			public void onSuccess(String response) {
				try {
					JSONObject jsonObject = new JSONObject(response);
					JSONObject result = jsonObject.getJSONObject("result");	
					ToastUtils.show(mActivity, result.getString("msg"));
					if (result.getInt("code") == 200) {
						mLoginHandler.doUserLogin(mAccount, password, false);
//						Map<String, String> info = new HashMap<String, String>();
//						info.put(LoginInfoHandler.USER_ACCOUNT, mAccount);
//						info.put(LoginInfoHandler.USER_NICKNAME, "");
//						info.put(LoginInfoHandler.USER_PASSWORD, password);
//						info.put(LoginInfoHandler.USER_SERVER, "最近登录：" + ConfigHolder.gameName);
//						info.put(LoginInfoHandler.USER_LOGIN_WAY, "");
//						//保存到账号登陆信息表
//						LoginInfoHandler.putLoginInfo(LoginInfoHandler.LOGIN_INFO_ACCOUNT, info);
//						ConfigHolder.userPassword = password;
//						mActivity.finish();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void verifiPassword() {
		String password = mEditAccountPass.getText().toString();
		if (password.isEmpty()) {
			ToastUtils.show(mActivity, "密码不能为空");
		} else if (password.length() < 6 || password.length() > 16) {
			ToastUtils.show(mActivity, "密码长度错误");
		} else {
			checkPassword(password);
		}
	}

	//检查密码是否正确
	private void checkPassword(final String password) {
		Map<String,String> map = new HashMap<String, String>();
		map.put("username", mAccount);
		map.put("password", password);
		map.put("channel", ConfigHolder.channelId);
		map.put("sign", AppUtils.MD5(ConfigHolder.userName + password + ConfigHolder.gameId + ConfigHolder.gameToken));
		HttpUtils.post(mActivity, URLHolder.URL_UNION_ACCOUNT_LOGIN, map, new HttpsCallback() {
			@Override
			public void onSuccess(String response) {
				LoginInfo loginInfo = new Gson().fromJson(response, LoginInfo.class);
				if (loginInfo.getResult().getCode() == 200) {
					ConfigHolder.oldPassword = password;
					mActivity.switchFragment(new AlertPasswordFragment(AlertType.ALERT_TYPE_NEW, mAccount, mBindPhone));
				} else {
					ToastUtils.show(mActivity, "密码错误");
				}
			}
		});
	}

	private void switchPassword() {
		mIsOpenPassword = !mIsOpenPassword;
		mEditAccountPass.setSingleLine();
		mEditAccountPass.setInputType(mIsOpenPassword ? InputType.TYPE_CLASS_TEXT : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		mImgSwitch.setImageResource(ResUtils.getResById(mActivity, mIsOpenPassword ? "ty2_eye_open" : "ty2_eye_close", "drawable"));
	}
}
