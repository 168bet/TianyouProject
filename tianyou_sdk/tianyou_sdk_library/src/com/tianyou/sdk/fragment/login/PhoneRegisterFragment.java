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
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 手机注册页面
 * @author itstrong
 *
 */
public class PhoneRegisterFragment extends BaseFragment {

	private EditText mEditText0;
	private EditText mEditText1;
	private EditText mEditText2;
	private TextView mTextGetCode;
	private ImageView mImgRadio;
	
	@Override
	protected String setContentView() { return "fragment_login_register_phone"; }

	@Override
	protected void initView() {
		mEditText0 = (EditText) mContentView.findViewById(ResUtils.getResById(mActivity, "edit_register_username", "id"));
		mEditText1 = (EditText) mContentView.findViewById(ResUtils.getResById(mActivity, "edit_register_code", "id"));
		mEditText2 = (EditText) mContentView.findViewById(ResUtils.getResById(mActivity, "edit_register_password", "id"));
		mEditText0.requestFocus();
		mTextGetCode = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_register_get_code", "id"));
		mImgRadio = (ImageView) mContentView.findViewById(ResUtils.getResById(mActivity, "img_register_radio", "id"));
		mContentView.findViewById(ResUtils.getResById(mActivity, "text_register_back", "id")).setOnClickListener(this);
		mTextConfirm = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_register_confirm", "id"));
		mContentView.findViewById(ResUtils.getResById(mActivity, "text_register_protocol", "id")).setOnClickListener(this);
		mTextConfirm.setOnClickListener(this);
		mTextGetCode.setOnClickListener(this);
		mImgRadio.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		((LoginActivity)mActivity).setRegisterTitle(true);
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == ResUtils.getResById(mActivity, "text_register_back", "id")) {
			((LoginActivity)mActivity).resetRegisterTitle();
			mActivity.switchFragment(new AccountFragment());
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_register_confirm", "id")) {
			registerAccount();
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_register_get_code", "id")) {
			getVerifiCode(mEditText0.getText().toString(), mTextGetCode, SendType.SEND_TYPE_REGISTER);
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_register_protocol", "id")) {
			mActivity.switchFragment(new ProtocolFragment());
		} else if (v.getId() == ResUtils.getResById(mActivity, "img_register_radio", "id")) {
			switchRadioState();
		}
	}
	
	
	private boolean isShowRadio;	//false选中
	private TextView mTextConfirm;
	
	private void switchRadioState() {
		isShowRadio = !isShowRadio;
		mImgRadio.setImageResource(ResUtils.getResById(mActivity, isShowRadio ? "ty2_gou_1" : "ty2_gou_0", "drawable"));
		mTextConfirm.setBackgroundResource(ResUtils.getResById(mActivity, isShowRadio ? "shape_btn_gray_fill" : "shape_bg_jacinth_fill", "drawable"));
		mTextConfirm.setClickable(!isShowRadio);
	}

	// 立即注册
	private void registerAccount() {
		String editText0 = mEditText0.getText().toString();
		String editText1 = mEditText1.getText().toString();
		String editText2 = mEditText2.getText().toString();
		if (editText0.isEmpty()) {
			ToastUtils.show(mActivity, "手机号不能为空");
		} else if (editText1.isEmpty()) {
			ToastUtils.show(mActivity, "验证码不能为空");
		} else if (editText2.isEmpty()) {
			ToastUtils.show(mActivity, "密码不能为空");
		} else if ((editText2.length() < 6 || editText2.length() > 16)) {
			ToastUtils.show(mActivity, "密码长度错误");
		} else if (!AppUtils.verifyPhoneNumber(editText0)) {
			ToastUtils.show(mActivity, "手机号格式错误");
		} else {
			Map<String,String> map = new HashMap<String, String>();
			map.put("username", editText0);
			map.put("Mobilecode", editText1);
			map.put("password", editText2);
			map.put("channel", ConfigHolder.channelId);
			map.put("sign", AppUtils.MD5(editText0 + editText2 + ConfigHolder.gameId + ConfigHolder.gameToken));
			HttpUtils.post(mActivity, URLHolder.URL_REGISTER_PHONE, map, new HttpsCallback() {
				@Override
				public void onSuccess(String response) {
					try {
						JSONObject jsonObject = new JSONObject(response);
						JSONObject result = jsonObject.getJSONObject("result");
						ToastUtils.show(mActivity, result.getString("msg"));
						if (result.getInt("code") == 200) {
							mLoginHandler.doUserLogin(result.getString("username"), result.getString("password"), false);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}
}
