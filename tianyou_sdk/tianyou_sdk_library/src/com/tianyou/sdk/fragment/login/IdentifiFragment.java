package com.tianyou.sdk.fragment.login;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.tianyou.sdk.base.BaseFragment;
import com.tianyou.sdk.bean.Identifi;
import com.tianyou.sdk.holder.ConfigHolder;
import com.tianyou.sdk.holder.LoginHandler;
import com.tianyou.sdk.holder.URLHolder;
import com.tianyou.sdk.utils.AppUtils;
import com.tianyou.sdk.utils.HttpUtils;
import com.tianyou.sdk.utils.HttpUtils.HttpsCallback;
import com.tianyou.sdk.utils.ResUtils;
import com.tianyou.sdk.utils.ToastUtils;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
	private View mLayoutIsPhone;
	private TextView mTextCode;

	@Override
	protected String setContentView() { return "fragment_login_identifi"; }

	@Override
	protected void initView() {
		mTextCode = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_identifi_code", "id"));
		mEditPhone = (EditText) mContentView.findViewById(ResUtils.getResById(mActivity, "edit_identifi_phone", "id"));
		mEditCode = (EditText) mContentView.findViewById(ResUtils.getResById(mActivity, "edit_identifi_code", "id"));
		mEditName = (EditText) mContentView.findViewById(ResUtils.getResById(mActivity, "edit_identifi_name", "id"));
		mEditIdCard = (EditText) mContentView.findViewById(ResUtils.getResById(mActivity, "edit_identifi_id_card", "id"));
		mLayoutIsPhone = mContentView.findViewById(ResUtils.getResById(mActivity, "layout_identifi_is_phone", "id"));
		mContentView.findViewById(ResUtils.getResById(mActivity, "text_identifi_confirm", "id")).setOnClickListener(this);
		mTextCode.setOnClickListener(this);
		mEditName.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }
			
			@Override
			public void afterTextChanged(Editable text) {
				String value = text.toString();
				for (int i = 0; i < value.length(); i++) {
					if (!isChinese(value.charAt(i))) {
						mEditName.setText(value.substring(0, i) + value.substring(i + 1, value.length()));
					}
				}
			}
		});
	}
	
    public  boolean isChinese(char c) {
	    Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
	    if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
	         || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
	        || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
	        || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
	        || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
	        || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
	        return true;
	    }
	    return false;
    }

	@Override
	protected void initData() {
		mActivity.setFragmentTitle("安全实名认证");
		mLayoutIsPhone.setVisibility(ConfigHolder.isPhone ? View.GONE : View.VISIBLE);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == ResUtils.getResById(mActivity, "text_identifi_confirm", "id")) {
			submitInfo();
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_identifi_code", "id")) {
			getPhoneCode();
		}
	}

	private void getPhoneCode() {
		String phone = mEditPhone.getText().toString();
		if (phone.isEmpty()) {
			ToastUtils.show(mActivity, "手机号不能为空");
		} else if (!AppUtils.verifyPhoneNumber(phone)) {
			ToastUtils.show(mActivity, "手机号格式错误");
		} else {
			getVerifiCode(phone, mTextCode, SendType.SEND_TYPE_IDENTITY);
		}
	}

	private void submitInfo() {
		String phone = ConfigHolder.isPhone ? ConfigHolder.userName : mEditPhone.getText().toString();
		String code = mEditCode.getText().toString();
		String name = mEditName.getText().toString();
		String idCard = mEditIdCard.getText().toString();
		if (!ConfigHolder.isPhone && phone.isEmpty()) {
			ToastUtils.show(mActivity, "手机号不能为空");
		} else if (!ConfigHolder.isPhone && !AppUtils.verifyPhoneNumber(phone)) {
			ToastUtils.show(mActivity, "手机号格式错误");
		} else if (!ConfigHolder.isPhone && code.isEmpty()) {
			ToastUtils.show(mActivity, "验证码不能为空");
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
			if (!ConfigHolder.isPhone) map.put("mobilecode", code);
			map.put("sign", AppUtils.MD5(ConfigHolder.gameId + ConfigHolder.gameToken + ConfigHolder.userId));
			HttpUtils.post(mActivity, URLHolder.URL_IDENTIFI, map, new HttpsCallback() {
				@Override
				public void onSuccess(String response) {
					Identifi identifi = new Gson().fromJson(response, Identifi.class);
					ToastUtils.show(mActivity, identifi.getResult().getMsg());
					if (identifi.getResult().getCode() == 200) {
						ConfigHolder.isAuth = true;
						ConfigHolder.isPhone = true;
						LoginHandler.onNoticeLoginSuccess();
						mActivity.finish();
					}
				}
			});
		}
	}
}
