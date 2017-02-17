//package com.tianyou.sdk.fragment.login;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//import android.text.InputType;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.google.gson.Gson;
//import com.tianyou.sdk.base.BaseLoginFragment;
//import com.tianyou.sdk.bean.LoginInfo;
//import com.tianyou.sdk.bean.LoginInfo.ResultBean;
//import com.tianyou.sdk.bean.LoginWay;
//import com.tianyou.sdk.bean.LoginWay.ResultBean.CustominfoBean;
//import com.tianyou.sdk.holder.ConfigHolder;
//import com.tianyou.sdk.holder.LoginInfoHandler;
//import com.tianyou.sdk.holder.SPHandler;
//import com.tianyou.sdk.holder.URLHolder;
//import com.tianyou.sdk.utils.AppUtils;
//import com.tianyou.sdk.utils.HttpUtils;
//import com.tianyou.sdk.utils.HttpUtils.HttpsCallback;
//import com.tianyou.sdk.utils.ResUtils;
//import com.tianyou.sdk.utils.ToastUtils;
//
///**
// * 快速注册
// * @author itstrong
// *
// */
//public class QuickFragment extends BaseLoginFragment {
//
//	private EditText mEditPass;
//	private TextView mTextSwitch;
//	private ImageView mImgPassState;
//	private View mLayoutWay;
//	private View mImgWayQQ;
//	private View mImgWayWechat;
//	private boolean mIsPassState;
//	private boolean mIsShowIcon;
//	
//	private String mUserName;
//
//	@Override
//	protected String setContentView() { return "fragment_login_quick"; }
//
//	@Override
//	protected void initView() {
//		mContentView.findViewById(ResUtils.getResById(mActivity, "text_quick_server", "id")).setOnClickListener(this);
//		mContentView.findViewById(ResUtils.getResById(mActivity, "text_quick_login", "id")).setOnClickListener(this);
//		mTextSwitch = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_quick_switch", "id"));
//		mTextSwitch.setOnClickListener(this);
//		mEditPass = (EditText) mContentView.findViewById(ResUtils.getResById(mActivity, "edit_quick_pass", "id"));
//		mImgPassState = (ImageView) mContentView.findViewById(ResUtils.getResById(mActivity, "img_quick_pass", "id"));
//		mImgWayQQ = mContentView.findViewById(ResUtils.getResById(mActivity, "img_login_way_0", "id"));
//		mImgWayWechat = mContentView.findViewById(ResUtils.getResById(mActivity, "img_login_way_1", "id"));
//		mContentView.findViewById(ResUtils.getResById(mActivity, "img_login_way_2", "id")).setOnClickListener(this);
//		mContentView.findViewById(ResUtils.getResById(mActivity, "img_login_way_3", "id")).setOnClickListener(this);
//		mLayoutWay = mContentView.findViewById(ResUtils.getResById(mActivity, "layout_quick_way", "id"));
//		mImgPassState.setOnClickListener(this);
//		mImgWayQQ.setOnClickListener(this);
//		mImgWayWechat.setOnClickListener(this);
//	}
//
//	@Override
//	protected void initData() {
//		mIsShowIcon = false;
//		showLoginWay();
//		Map<String,String> map = new HashMap<String, String>();
//		String phoeImei = AppUtils.getPhoeIMEI(mActivity);
//		if (phoeImei.isEmpty()) {
//			String imei = SPHandler.getString(mActivity, SPHandler.SP_IMEI);
//			if (imei.isEmpty()) SPHandler.putString(mActivity, SPHandler.SP_IMEI, UUID.randomUUID().toString());
//			phoeImei = SPHandler.getString(mActivity, SPHandler.SP_IMEI);
//		}
//		map.put("appID", ConfigHolder.GAME_ID);
//		map.put("imei", phoeImei);
//		map.put("channel", ConfigHolder.CHANNEL_ID);
//		map.put("ip", AppUtils.getIP());
//		map.put("type", "android");
//		HttpUtils.post(mActivity, URLHolder.URL_LOGIN_QUICK, map, new HttpsCallback() {
//			@Override
//			public void onSuccess(String response) {
//				LoginInfo info = new Gson().fromJson(response, LoginInfo.class);
//				ResultBean result = info.getResult();
//				if (result.getCode() == 200) {
//					mEditPass.setText(result.getPassword() == null ? "" : result.getPassword());
//					mUserName = result.getUsername();
//				}
//			}
//		});
//	}
//
//	@Override
//	public void onClick(View v) {
//		if (v.getId() == ResUtils.getResById(mActivity, "text_quick_server", "id")) {
//			AppUtils.openServerTerms(mActivity);
//		} else if (v.getId() == ResUtils.getResById(mActivity, "img_quick_pass", "id")) {
//			switchPassState();
//		} else if (v.getId() == ResUtils.getResById(mActivity, "text_quick_switch", "id")) {
//			mIsShowIcon = !mIsShowIcon;
//			if (mIsShowIcon) {
//				mLayoutWay.setVisibility(View.VISIBLE);
//				mTextSwitch.setText("");
//			} else {
//				mLayoutWay.setVisibility(View.GONE);
//				mTextSwitch.setText("切换登录");
//			}
//		} else if (v.getId() == ResUtils.getResById(mActivity, "text_quick_login", "id")) {
//			doLogin();
//		} else if (v.getId() == ResUtils.getResById(mActivity, "img_login_way_0", "id")) {
//			if (LoginInfoHandler.getLoginInfo(LoginInfoHandler.LOGIN_INFO_QQ).size() == 0) {
//				mActivity.switchFragment(new NoQQFragment(), "NoQQFragment");
//			} else {
//				mActivity.switchFragment(new QQBindingFragment(), "QQBindingFragment");
//			}
//		} else if (v.getId() == ResUtils.getResById(mActivity, "img_login_way_3", "id")) {
//			mActivity.switchFragment(new AccountFragment(), "AccountFragment");
//		} else if (v.getId() == ResUtils.getResById(mActivity, "img_login_way_2", "id")) {
//			mActivity.switchFragment(new PhoneFragment(), "PhoneFragment");
//		}
//	}
//	
//	// 显示隐藏登录方式
//	private void showLoginWay() {
//		Map<String,String> map = new HashMap<String, String>();
//		map.put("appID", ConfigHolder.GAME_ID);
//		map.put("usertoken", ConfigHolder.GAME_TOKEN);
//		HttpUtils.post(mActivity, URLHolder.URL_LOGIN_WAY, map, new HttpsCallback() {
//			@Override
//			public void onSuccess(String response) {
//				LoginWay loginWay = new Gson().fromJson(response, LoginWay.class);
//				com.tianyou.sdk.bean.LoginWay.ResultBean result = loginWay.getResult();
//				if (result.getCode() == 200) {
//					CustominfoBean custominfo = result.getCustominfo();
//					mImgWayQQ.setVisibility(custominfo.getQq_quick() == 1 ? View.VISIBLE : View.GONE);
//					mImgWayWechat.setVisibility(custominfo.getWx_quick() == 1 ? View.VISIBLE : View.GONE);
//				} else {
//					ToastUtils.show(mActivity, result.getMsg());
//				}
//			}
//		});
//	}
//
//	private void doLogin() {
//		String password = mEditPass.getText().toString();
//		if (password.isEmpty()) {
//			ToastUtils.show(mActivity, "密码不能为空");
//		} else {
//			onUserLogin(mUserName, password, false);
//		}
//	}
//
//	private void switchPassState() {
//		if (mIsPassState) {
//			mEditPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
//		} else {
//			mEditPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//		}
//		mImgPassState.setImageResource(ResUtils.getResById(mActivity, mIsPassState ? "ty_pass_1" : "ty_pass_0", "drawable"));
//		mIsPassState = mIsPassState ? false : true;
//	}
//}
