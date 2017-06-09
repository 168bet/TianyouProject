package com.tianyou.sdk.fragment.login;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.tianyou.sdk.activity.LoginActivity;
import com.tianyou.sdk.base.BaseFragment;
import com.tianyou.sdk.holder.ConfigHolder;
import com.tianyou.sdk.holder.LoginInfoHandler;
import com.tianyou.sdk.holder.URLHolder;
import com.tianyou.sdk.utils.AppUtils;
import com.tianyou.sdk.utils.HttpUtils;
import com.tianyou.sdk.utils.HttpUtils.HttpsCallback;
import com.tianyou.sdk.utils.ResUtils;
import com.tianyou.sdk.utils.ToastUtils;

import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 账号升级页面
 * @author itstrong
 *
 */
public class UpgradeFragment extends BaseFragment {

	private EditText mEditPhone;
	private EditText mEditCode;
	private EditText mEditPassword;
	private View mViewTip;
	private TextView mViewCode;
	
	private boolean mIsAccountUpgrade;	//是否是账号升级
	private TextView mTextAccount;

	@Override
	protected String setContentView() { return "fragment_login_upgrade"; }

	@Override
	protected void initView() {
		mContentView.findViewById(ResUtils.getResById(mActivity, "text_upgrade_confirm", "id")).setOnClickListener(this);
		mTextAccount = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_upgrade_account", "id"));
		mEditPhone = (EditText) mContentView.findViewById(ResUtils.getResById(mActivity, "edit_upgrade_phone", "id"));
		mEditCode = (EditText) mContentView.findViewById(ResUtils.getResById(mActivity, "edit_upgrade_code", "id"));
		if (!mIsAccountUpgrade) {
			mEditPassword = (EditText) mContentView.findViewById(ResUtils.getResById(mActivity, "edit_upgrade_password", "id"));
		}
		mViewTip = mContentView.findViewById(ResUtils.getResById(mActivity, "text_upgrade_tip", "id"));
		mViewCode = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_upgrade_code", "id"));
		mTextAccount.setOnClickListener(this);
		mViewCode.setOnClickListener(this);
		if (ConfigHolder.isOverseas) {
			mIsAccountUpgrade = false;
			switchUpgradeWay();
			mTextAccount.setVisibility(View.GONE);
		}
	}

	@Override
	protected void initData() {
		mActivity.setFragmentTitle(ConfigHolder.isOverseas?"Account upgrade":"账号升级");
		((LoginActivity)mActivity).setBackBtnVisible(true);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == ResUtils.getResById(mActivity, "text_upgrade_confirm", "id")) {
			accountUpgrade();
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_upgrade_account", "id")) {
			switchUpgradeWay();
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_upgrade_code", "id")) {
			verifiCode();
		}
	}

	private void verifiCode() {
		String phone = mEditPhone.getText().toString();
		if (phone.isEmpty()) {
			ToastUtils.show(mActivity, "手机号不能为空");
		} else if (!AppUtils.verifyPhoneNumber(phone)) {
			ToastUtils.show(mActivity, "手机号格式错误");
		} else {
			getVerifiCode(phone, mViewCode, SendType.SEND_TYPE_BIND_PHONE);
		}
	}

	private void accountUpgrade() {
		String editText0 = mEditPhone.getText().toString();
		String editText1 = mEditCode.getText().toString();
		String editText2 = mEditPassword.getText().toString();
		if (editText0.isEmpty()) {
			ToastUtils.show(mActivity, (mIsAccountUpgrade ?(ConfigHolder.isOverseas?"Account":"账号") : "手机号") + (ConfigHolder.isOverseas?"can't be empty":"不能为空"));
		} else if (!mIsAccountUpgrade && !AppUtils.verifyPhoneNumber(editText0)) {
			ToastUtils.show(mActivity, "手机号格式错误");
		} else if (mIsAccountUpgrade && (editText0.length() < 6 || editText0.length() > 16)) {
			ToastUtils.show(mActivity, ConfigHolder.isOverseas?"Account length error":"账号长度错误");
		} else if (mIsAccountUpgrade && (editText1.length() < 6 || editText1.length() > 16)) {
			ToastUtils.show(mActivity, ConfigHolder.isOverseas?"Password length error":"密码长度错误");
		} else if (editText1.isEmpty()) {
			ToastUtils.show(mActivity, (mIsAccountUpgrade ? (ConfigHolder.isOverseas?"Password":"密码"): "验证码") + (ConfigHolder.isOverseas?"can't be empty":"不能为空"));
		} else if (mIsAccountUpgrade && !editText2.equals(editText1)) {
			ToastUtils.show(mActivity, ConfigHolder.isOverseas?"Two passwords do not agree":"两次密码不一致");
		} else if (!mIsAccountUpgrade && editText2.isEmpty()) {
			ToastUtils.show(mActivity, "密码不能为空");
		} else {
			Map<String,String> map = new HashMap<String, String>();
			map.put("userid", ConfigHolder.userId);
			if (mIsAccountUpgrade) {
				map.put("newname", editText0);
				map.put("password", editText1);
			} else {
				map.put("mobile", editText0);
				map.put("mobilecode", editText1);
				map.put("password", editText2);
			}
			map.put("sign", AppUtils.MD5(ConfigHolder.gameId + ConfigHolder.gameToken + ConfigHolder.userId));
			String url = mIsAccountUpgrade ? URLHolder.URL_UPGRADE_ACCOUNT : URLHolder.URL_UPGRADE_PHONE;
			HttpUtils.post(mActivity, url, map, new HttpsCallback() {
				@Override
				public void onSuccess(String response) {
					try {
						doHandleResult(response);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}
	
	private void doHandleResult(String response) throws JSONException {
		JSONObject jsonObject = new JSONObject(response);
		JSONObject result = jsonObject.getJSONObject("result");
		ToastUtils.show(mActivity, result.getString("msg"));
		if (result.getInt("code") == 200) {
			String username = result.getString("username");
			String password = result.getString("password");
			//保存到文件
			Map<String, String> info = new HashMap<String, String>();
			info.put(LoginInfoHandler.USER_ACCOUNT, username);
			info.put(LoginInfoHandler.USER_NICKNAME, "");
			info.put(LoginInfoHandler.USER_PASSWORD, password);
			info.put(LoginInfoHandler.USER_SERVER, "最近登录：" + ConfigHolder.gameName);
			info.put(LoginInfoHandler.USER_LOGIN_WAY, "");
			//保存到账号登陆信息表
			LoginInfoHandler.putLoginInfo(LoginInfoHandler.LOGIN_INFO_ACCOUNT, info);
			LoginInfoHandler.deleteLoginInfo(LoginInfoHandler.LOGIN_INFO_ACCOUNT, ConfigHolder.userName);
			//保存到内存
			ConfigHolder.userId = result.getString("userid");
			ConfigHolder.userName = username;
			ConfigHolder.isPhone = !mIsAccountUpgrade;
			ConfigHolder.isTourist = false;
			if (ConfigHolder.isAuth) {
				mActivity.finish();
			} else {
				mActivity.switchFragment(new IdentifiFragment());
			}
		}
	}

	private void switchUpgradeWay() {
		mIsAccountUpgrade = !mIsAccountUpgrade;
		mEditPhone.setText("");
		mEditCode.setText("");
		mEditPassword.setText("");
		mEditPhone.setInputType(mIsAccountUpgrade ? InputType.TYPE_CLASS_TEXT : InputType.TYPE_CLASS_PHONE);
		mEditCode.setInputType(mIsAccountUpgrade ? InputType.TYPE_CLASS_TEXT : InputType.TYPE_CLASS_PHONE);
		mEditPhone.setHint(mIsAccountUpgrade ? (ConfigHolder.isOverseas?"Username: 6-16-bit":"账号：请输入6-16位字母或数字组合" ): "请输入11位手机号");
		mEditCode.setHint(mIsAccountUpgrade ? (ConfigHolder.isOverseas?"Password: 6-16-bit":"密码：请输入6-16位字母或数字组合" ): "请输入验证码");
		mEditPassword.setHint(mIsAccountUpgrade ?(ConfigHolder.isOverseas?"Please enter the password again":"请再次输入密码") : "密码：请输入6-16位字母或数字组合");
		mViewTip.setVisibility(mIsAccountUpgrade ? View.VISIBLE : View.GONE);
		mViewCode.setVisibility(mIsAccountUpgrade ? View.GONE : View.VISIBLE);
		mTextAccount.setText(mIsAccountUpgrade ? "升级手机号账号" : "升级用户名账号");
	}
}
