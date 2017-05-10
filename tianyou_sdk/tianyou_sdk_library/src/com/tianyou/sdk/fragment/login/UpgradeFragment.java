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
	private View mViewTip;
	private View mViewCode;
	
	private boolean mIsAccountUpgrade;	//是否是账号等级
	private TextView mTextAccount;

	@Override
	protected String setContentView() { return "fragment_login_upgrade"; }

	@Override
	protected void initView() {
		mActivity.setFragmentTitle("账号升级");
		((LoginActivity)mActivity).setBackBtnVisible(true);
		mContentView.findViewById(ResUtils.getResById(mActivity, "text_upgrade_confirm", "id")).setOnClickListener(this);
		mTextAccount = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_upgrade_account", "id"));
		mEditPhone = (EditText) mContentView.findViewById(ResUtils.getResById(mActivity, "edit_upgrade_phone", "id"));
		mEditCode = (EditText) mContentView.findViewById(ResUtils.getResById(mActivity, "edit_upgrade_code", "id"));
		mViewTip = mContentView.findViewById(ResUtils.getResById(mActivity, "text_upgrade_tip", "id"));
		mViewCode = mContentView.findViewById(ResUtils.getResById(mActivity, "text_upgrade_code", "id"));
		mTextAccount.setOnClickListener(this);
	}

	@Override
	protected void initData() { }

	@Override
	public void onClick(View v) {
		if (v.getId() == ResUtils.getResById(mActivity, "text_upgrade_confirm", "id")) {
			accountUpgrade();
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_upgrade_account", "id")) {
			switchUpgradeWay();
		}
	}

	private void accountUpgrade() {
		String phone = mEditPhone.getText().toString();
		String code = mEditCode.getText().toString();
		if (phone.isEmpty()) {
			ToastUtils.show(mActivity, "手机号不能为空");
		} else if (!AppUtils.verifyPhoneNumber(phone)) {
			ToastUtils.show(mActivity, "手机号格式错误");
		} else if (code.isEmpty()) {
			ToastUtils.show(mActivity, "验证码不能为空");
		} else {
			Map<String,String> map = new HashMap<String, String>();
			map.put("userid", ConfigHolder.userId);
			map.put("mobile", phone);
			map.put("mobilecode", code);
			if (mIsAccountUpgrade) {
				map.put("newname", phone);
				map.put("password", code);
			}
			map.put("sign", AppUtils.MD5(ConfigHolder.gameId + ConfigHolder.gameToken + ConfigHolder.userId));
			String url = mIsAccountUpgrade ? URLHolder.URL_UPGRADE_ACCOUNT : URLHolder.URL_UPGRADE_PHONE;
			HttpUtils.post(mActivity, url, map, new HttpsCallback() {
				@Override
				public void onSuccess(String response) {
					try {
						JSONObject jsonObject = new JSONObject(response);
						JSONObject result = jsonObject.getJSONObject("result");
						ToastUtils.show(mActivity, result.getString("msg"));
						if (result.getInt("code") == 200) {
							mActivity.finish();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	private void switchUpgradeWay() {
		mIsAccountUpgrade = !mIsAccountUpgrade;
		mEditPhone.setText("");
		mEditCode.setText("");
		mEditPhone.setHint(mIsAccountUpgrade ? "用户名:4-14位,数字字母下划线组合" : "请输入手机号");
		mEditCode.setHint(mIsAccountUpgrade ? "密码：6-16位数字字母组合" : "请输入验证码");
		mViewTip.setVisibility(mIsAccountUpgrade ? View.VISIBLE : View.GONE);
		mViewCode.setVisibility(mIsAccountUpgrade ? View.GONE : View.VISIBLE);
		mTextAccount.setText(mIsAccountUpgrade ? "升级手机号账号" : "升级用户名账号");
	}
}
