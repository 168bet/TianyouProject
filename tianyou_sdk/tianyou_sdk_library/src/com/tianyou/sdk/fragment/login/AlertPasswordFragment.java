package com.tianyou.sdk.fragment.login;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.tianyou.sdk.activity.LoginActivity;
import com.tianyou.sdk.base.BaseFragment;
import com.tianyou.sdk.bean.LoginInfo;
import com.tianyou.sdk.holder.ConfigHolder;
import com.tianyou.sdk.holder.URLHolder;
import com.tianyou.sdk.utils.AppUtils;
import com.tianyou.sdk.utils.HttpUtils;
import com.tianyou.sdk.utils.HttpUtils.HttpsCallback;
import com.tianyou.sdk.utils.ResUtils;
import com.tianyou.sdk.utils.ToastUtils;

import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 修改密码页面
 * @author itstrong
 *
 */
public class AlertPasswordFragment extends BaseFragment {

	private TextView mTextTips;
	private TextView mTextPhone;
	private TextView mTextPhone2;
	private TextView mTextCode;
	private EditText mEditCode;
	private EditText mEditPassword;
	private EditText mEditAgain;
	private EditText mEditPassword1;
	private ImageView mImgSwitch;
	
	private int mStep;		//步骤
	private String mAccount;	//修改账号
	private boolean mIsOpenPassword;

	AlertPasswordFragment(int step, String account) {
		mStep = step;
		mAccount = account;
	}
	
	@Override
	protected String setContentView() {
		switch (mStep) {
		case 0:		//
			return "fragment_login_alert_password0";
		case 1:		//
			return "fragment_login_alert_password1";
		case 2:		//账号修改密码输入新密码
			return "fragment_login_alert_password2";
		case 3:
			return "fragment_login_alert_password3";
		}
		return "fragment_login_alert_password4";
	}

	@Override
	protected void initView() {
		switch (mStep) {
		case 0:
			mContentView.findViewById(ResUtils.getResById(mActivity, "layout_alert_context", "id")).setOnClickListener(this);
			mTextTips = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_alert_tips", "id"));
			mTextPhone = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_alert_phone", "id"));
			break;
		case 1:
			mEditPassword1 = (EditText) mContentView.findViewById(ResUtils.getResById(mActivity, "edit_alert_password", "id"));
			mImgSwitch = (ImageView) mContentView.findViewById(ResUtils.getResById(mActivity, "img_alert_switch", "id"));
			mContentView.findViewById(ResUtils.getResById(mActivity, "text_alert_confirm", "id")).setOnClickListener(this);
			mContentView.findViewById(ResUtils.getResById(mActivity, "text_alert_forget", "id")).setOnClickListener(this);
			mImgSwitch.setOnClickListener(this);
			break;
		case 2:
			mContentView.findViewById(ResUtils.getResById(mActivity, "text_alert_confirm2", "id")).setOnClickListener(this);
			break;
		case 3:
			break;
		case 4:
			mTextPhone2 = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_alert_phone", "id"));
			mEditCode = (EditText) mContentView.findViewById(ResUtils.getResById(mActivity, "edit_alert_code", "id"));
			mTextCode = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_alert_code", "id"));
			mContentView.findViewById(ResUtils.getResById(mActivity, "text_alert_confirm4", "id")).setOnClickListener(this);
			mTextCode.setOnClickListener(this);
			break;
		}
	}

	@Override
	protected void initData() {
		mActivity.setFragmentTitle("修改密码");
		((LoginActivity)mActivity).setBackBtnVisible(true);
		switch (mStep) {
		case 0:
			mTextTips.setText("您要修改的密码账号：" + mAccount);
			mTextPhone.setText(mAccount.substring(0, 3) + "****" + mAccount.substring(7, 11));
			break;
		case 1:
			break;
		case 2:
			mEditPassword = (EditText) mContentView.findViewById(ResUtils.getResById(mActivity, "edit_alert_password", "id"));
			mEditAgain = (EditText) mContentView.findViewById(ResUtils.getResById(mActivity, "edit_alert_again", "id"));
			break;
		case 3:
			break;
		case 4:
			mTextPhone2.setText(mAccount.substring(0, 3) + "****" + mAccount.substring(7, 11));
			break;
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == ResUtils.getResById(mActivity, "layout_alert_context", "id")) {
			mActivity.switchFragment(new AlertPasswordFragment(4, mAccount));
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_alert_confirm", "id")) {
			checkPassword();
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_alert_confirm2", "id")) {
			String password = mEditPassword.getText().toString();
			String again = mEditAgain.getText().toString();
			if (password.isEmpty()) {
				ToastUtils.show(mActivity, "密码不能为空");
			} else if (!again.equals(password)) {
				ToastUtils.show(mActivity, "两次输入密码不一致");
			}
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_alert_forget", "id")) {
			mActivity.switchFragment(new AlertPasswordFragment(3, mAccount));
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_alert_code", "id")) {
			getVerifiCode(mAccount, mTextCode);
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_alert_confirm4", "id")) {
			String code = mEditCode.getText().toString();
			if (code.isEmpty()) {
				ToastUtils.show(mActivity, "验证码不能为空");
			} else if (!code.equals(mVerifiCode)) {
				ToastUtils.show(mActivity, "验证码输入错误");
			} else if (mVerifiCode == null) {
				ToastUtils.show(mActivity, "请先获取验证码");
			} else {
				mActivity.switchFragment(new AlertPasswordFragment(2, mAccount));
			}
		} else if (v.getId() == ResUtils.getResById(mActivity, "img_alert_switch", "id")) {
			switchPassword();
		}
	}
	
	//检查密码是否正确
	private void checkPassword() {
		String password = mEditPassword1.getText().toString();
		if (password.isEmpty()) {
			ToastUtils.show(mActivity, "密码不能为空");
		} else if (password.length() < 6 || password.length() > 16) {
			ToastUtils.show(mActivity, "密码长度错误");
		} else {
			Map<String,String> map = new HashMap<String, String>();
			map.put("username", mAccount);
			map.put("password", password);
			map.put("channel", ConfigHolder.channelId);
			map.put("sign", AppUtils.MD5(mAccount + password + ConfigHolder.gameId + ConfigHolder.gameToken));
			HttpUtils.post(mActivity, URLHolder.URL_UNION_ACCOUNT_LOGIN, map, new HttpsCallback() {
				@Override
				public void onSuccess(String response) {
					LoginInfo loginInfo = new Gson().fromJson(response, LoginInfo.class);
					if (loginInfo.getResult().getCode() == 200) {
						mActivity.switchFragment(new AlertPasswordFragment(2, mAccount));
					} else {
						ToastUtils.show(mActivity, "密码错误");
					}
				}
			});
		}
	}

	private void switchPassword() {
		mIsOpenPassword = !mIsOpenPassword;
		mEditPassword1.setSingleLine();
		mEditPassword1.setInputType(mIsOpenPassword ? InputType.TYPE_CLASS_TEXT : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		mImgSwitch.setImageResource(ResUtils.getResById(mActivity, mIsOpenPassword ? "eye_open" : "eye_close", "drawable"));
	}
}
