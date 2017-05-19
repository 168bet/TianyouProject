//package com.tianyou.sdk.fragment.login;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import com.tianyou.sdk.activity.LoginActivity;
//import com.tianyou.sdk.base.BaseFragment;
//import com.tianyou.sdk.holder.ConfigHolder;
//import com.tianyou.sdk.holder.URLHolder;
//import com.tianyou.sdk.utils.AppUtils;
//import com.tianyou.sdk.utils.HttpUtils;
//import com.tianyou.sdk.utils.LogUtils;
//import com.tianyou.sdk.utils.HttpUtils.HttpsCallback;
//import com.tianyou.sdk.utils.ResUtils;
//import com.tianyou.sdk.utils.ToastUtils;
//
//import android.app.Fragment;
//import android.os.Bundle;
//import android.text.InputFilter;
//import android.text.InputType;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//
///**
// * 用户注册页面
// * @author itstrong
// *
// */
//public class RegisterFragment extends BaseFragment {
//
//	private EditText mEditText0;
//	private EditText mEditText1;
//	private EditText mEditText2;
//	private TextView mTextGetCode;
//	private ImageView mImgCode;
//	private ImageView mImgRadio;
//	
//	private boolean mIsUserRegister;	//是否是用户注册
//	private String mImgRandom;
//	
//	public static Fragment getInstance(boolean isPhoneRegister) {
//		Fragment fragment = new RegisterFragment();
//		Bundle bundle = new Bundle();
//		bundle.putBoolean("isPhoneRegister", isPhoneRegister);
//		fragment.setArguments(bundle);
//		return fragment;
//	}
//	
//	@Override
//	protected String setContentView() { return "fragment_login_register"; }
//
//	@Override
//	protected void initView() {
//		mEditText0 = (EditText) mContentView.findViewById(ResUtils.getResById(mActivity, "edit_register_username", "id"));
//		mEditText1 = (EditText) mContentView.findViewById(ResUtils.getResById(mActivity, "edit_register_code", "id"));
//		mEditText2 = (EditText) mContentView.findViewById(ResUtils.getResById(mActivity, "edit_register_password", "id"));
//		mTextGetCode = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_register_get_code", "id"));
//		mImgCode = (ImageView) mContentView.findViewById(ResUtils.getResById(mActivity, "img_register_code", "id"));
//		mImgRadio = (ImageView) mContentView.findViewById(ResUtils.getResById(mActivity, "img_register_radio", "id"));
//		mContentView.findViewById(ResUtils.getResById(mActivity, "text_register_back", "id")).setOnClickListener(this);
//		mTextConfirm = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_register_confirm", "id"));
//		mContentView.findViewById(ResUtils.getResById(mActivity, "text_register_protocol", "id")).setOnClickListener(this);
//		mTextConfirm.setOnClickListener(this);
//		mTextGetCode.setOnClickListener(this);
//		mImgRadio.setOnClickListener(this);
//		mImgCode.setOnClickListener(this);
//	}
//
//	@Override
//	protected void initData() {
////		((LoginActivity)mActivity).setRegisterTitle(true);
////		((LoginActivity)mActivity).switchTitleState();
//		((LoginActivity)mActivity).mIsAccountRegister = false;
//		if (getArguments() != null) mIsUserRegister = !getArguments().getBoolean("isPhoneRegister");
//		mEditText0.setHint(mIsUserRegister ? "账号：请输入6-16位字母或数字组合" : "请输入11位手机号");
//		mEditText1.setHint(mIsUserRegister ? "密码：请输入6-16位字母或数字组合" : "请输入6位验证码");
//		mEditText2.setHint(mIsUserRegister ? "请输入4位验证码" : "密码：请输入6-16位字母或数字组合");
//		mEditText2.setInputType(mIsUserRegister ? InputType.TYPE_CLASS_TEXT : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//		/**
//		 * 账号输入框
//		 * 1.选择手机号注册，输入格式为phone;选择用户注册，输入格式为密码（字母+数字）
//		 * 2.选择手机号注册，字符长度控制在11位;选择用户注册，输入长度控制在16位
//		 */
//		if (!mIsUserRegister) mEditText0.setInputType(InputType.TYPE_CLASS_PHONE);
//		mEditText0.setFilters((mIsUserRegister ? new InputFilter[]{new InputFilter.LengthFilter(16)} : new InputFilter[]{new InputFilter.LengthFilter(11)}));
//		/**
//		 * 密码和手机验证码输入框
//		 * 1.选择手机注册时，输入格式为数字;选择用户注册时，输入格式为密码（字母+数字）
//		 * 2.选择手机注册时，输入长度控制在6位;选择用户注册时，输入长度控制在16位
//		 */
//		mEditText1.setInputType((mIsUserRegister ? (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD) : InputType.TYPE_CLASS_NUMBER));
//		mEditText1.setFilters((mIsUserRegister ? new InputFilter[]{new InputFilter.LengthFilter(16)} : new InputFilter[]{new InputFilter.LengthFilter(6)}));
//		/**
//		 * 密码和图片验证码输入框
//		 * 1.选择手机注册时，输入格式为密码（字母+数字）;选择用户注册时，输入格式为字母+数字
//		 * 2.选择手机注册时，输入长度控制在16位;选择用户注册时，输入长度控制在4位
//		 */
//		if (!mIsUserRegister) mEditText2.setInputType((InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD));
//		mEditText2.setFilters((mIsUserRegister ? new InputFilter[]{new InputFilter.LengthFilter(4)} : new InputFilter[]{new InputFilter.LengthFilter(16)}));
//
//		mTextGetCode.setVisibility(mIsUserRegister ? View.GONE : View.VISIBLE);
//		mImgCode.setVisibility(mIsUserRegister ? View.VISIBLE : View.GONE);
//		mImgRandom = UUID.randomUUID().toString();
//		HttpUtils.imageLoad(mActivity, URLHolder.URL_IMG_VERIFI + "/random/" + mImgRandom, mImgCode);
//	}
//	
//	@Override
//	public void onClick(View v) {
//		if (v.getId() == ResUtils.getResById(mActivity, "text_register_back", "id")) {
//			mActivity.switchFragment(new AccountFragment());
//		} else if (v.getId() == ResUtils.getResById(mActivity, "text_register_confirm", "id")) {
//			registerAccount();
//		} else if (v.getId() == ResUtils.getResById(mActivity, "text_register_get_code", "id")) {
//			getVerifiCode(mEditText0.getText().toString(), mTextGetCode, SendType.SEND_TYPE_REGISTER);
//		} else if (v.getId() == ResUtils.getResById(mActivity, "img_register_code", "id")) {
//			mImgRandom = UUID.randomUUID().toString();
//			LogUtils.d("mImgRandom:" + mImgRandom);
//			HttpUtils.imageLoad(mActivity, URLHolder.URL_IMG_VERIFI + "/random/" + mImgRandom, mImgCode);
//		} else if (v.getId() == ResUtils.getResById(mActivity, "text_register_protocol", "id")) {
//			mActivity.switchFragment(new ProtocolFragment());
//		} else if (v.getId() == ResUtils.getResById(mActivity, "img_register_radio", "id")) {
//			switchRadioState();
//		}
//	}
//	
//	private boolean isShowRadio;	//false选中
//	private TextView mTextConfirm;
//	
//	private void switchRadioState() {
//		isShowRadio = !isShowRadio;
//		mImgRadio.setImageResource(ResUtils.getResById(mActivity, isShowRadio ? "ty2_gou_1" : "ty2_gou_0", "drawable"));
//		mTextConfirm.setBackgroundResource(ResUtils.getResById(mActivity, isShowRadio ? "shape_btn_gray_fill" : "shape_bg_jacinth_fill", "drawable"));
//		mTextConfirm.setClickable(!isShowRadio);
//	}
//
//	// 立即注册
//	private void registerAccount() {
//		String editText0 = mEditText0.getText().toString();
//		String editText1 = mEditText1.getText().toString();
//		String editText2 = mEditText2.getText().toString();
//		if (editText0.isEmpty()) {
//			ToastUtils.show(mActivity, (mIsUserRegister ? "账号" : "手机号") + "不能为空");
//		} else if (editText1.isEmpty()) {
//			ToastUtils.show(mActivity, (mIsUserRegister ? "密码" : "验证码") + "不能为空");
//		} else if (editText2.isEmpty()) {
//			ToastUtils.show(mActivity, (mIsUserRegister ? "验证码" : "密码") + "不能为空");
//		} else if (mIsUserRegister && (editText0.length() < 6 || editText0.length() > 16)) {
//			ToastUtils.show(mActivity, "账号长度错误");
//		} else if (mIsUserRegister && (editText1.length() < 6 || editText1.length() > 16)) {
//			ToastUtils.show(mActivity, "密码长度错误");
//		} else if (!mIsUserRegister && (editText2.length() < 6 || editText2.length() > 16)) {
//			ToastUtils.show(mActivity, "密码长度错误");
//		} else if (!mIsUserRegister && !AppUtils.verifyPhoneNumber(editText0)) {
//			ToastUtils.show(mActivity, "手机号格式错误");
//		} else {
//			Map<String,String> map = new HashMap<String, String>();
//			map.put("username", editText0);
//			map.put("Mobilecode", editText1);
//			map.put("password", mIsUserRegister ? editText1 : editText2);
//			map.put("channel", ConfigHolder.channelId);
//			if(mIsUserRegister) {
//				map.put("verify", editText2);
//				map.put("random", mImgRandom);
//			}
//			map.put("sign", AppUtils.MD5(editText0 + editText2 + ConfigHolder.gameId + ConfigHolder.gameToken));
//			String url = mIsUserRegister ? URLHolder.URL_USER_REGISTER : URLHolder.URL_REGISTER_PHONE;
//			HttpUtils.post(mActivity, url, map, new HttpsCallback() {
//				@Override
//				public void onSuccess(String response) {
//					try {
//						JSONObject jsonObject = new JSONObject(response);
//						JSONObject result = jsonObject.getJSONObject("result");
//						ToastUtils.show(mActivity, result.getString("msg"));
//						if (result.getInt("code") == 200) {
//							mLoginHandler.doUserLogin(result.getString("username"), result.getString("password"), false);
//						}
//					} catch (JSONException e) {
//						e.printStackTrace();
//					}
//				}
//			});
//		}
//	}
//}
