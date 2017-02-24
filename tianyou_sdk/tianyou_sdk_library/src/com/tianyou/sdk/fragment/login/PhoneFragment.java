package com.tianyou.sdk.fragment.login;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tianyou.sdk.base.BaseLoginFragment;
import com.tianyou.sdk.base.LoginAdapter;
import com.tianyou.sdk.base.LoginAdapter.AdapterCallback;
import com.tianyou.sdk.bean.LoginWay;
import com.tianyou.sdk.bean.LoginWay.ResultBean;
import com.tianyou.sdk.bean.LoginWay.ResultBean.CustominfoBean;
import com.tianyou.sdk.bean.PhoneCode;
import com.tianyou.sdk.holder.ConfigHolder;
import com.tianyou.sdk.holder.LoginInfoHandler;
import com.tianyou.sdk.holder.SPHandler;
import com.tianyou.sdk.holder.URLHolder;
import com.tianyou.sdk.utils.AppUtils;
import com.tianyou.sdk.utils.HttpUtils;
import com.tianyou.sdk.utils.HttpUtils.HttpsCallback;
import com.tianyou.sdk.utils.ResUtils;
import com.tianyou.sdk.utils.ToastUtils;

/**
 * 手机登录页面
 * @author itstrong
 *
 */
public class PhoneFragment extends BaseLoginFragment {

	private EditText mEditPhone;
	private EditText mEditCode;
	private View mImgWayQQ;
	private View mImgWayWechat;
	private ImageView mImgUserList;
	private TextView mTextCode;
	private View mLayoutQuick;
	
	private PopupWindow mPopupWindow;
	private ListView mListView;
	private String loginCode;								//验证码
	private List<Map<String, String>> mLoginInfos;			//当前显示的登录信息
	
	public static Fragment getInstance(boolean isSwitchAccount) {
		Fragment fragment = new PhoneFragment();
		Bundle bundle = new Bundle();
		bundle.putBoolean("isSwitchAccount", isSwitchAccount);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	@Override
	protected String setContentView() {
		return "fragment_login_phone";
	}
	
	@Override
	protected void initView() {
		mContentView.findViewById(ResUtils.getResById(mActivity, "btn_home_entry", "id")).setOnClickListener(this);
		mContentView.findViewById(ResUtils.getResById(mActivity, "text_home_quick", "id")).setOnClickListener(this);
		mContentView.findViewById(ResUtils.getResById(mActivity, "img_login_way_2", "id")).setOnClickListener(this);
		
		mEditPhone = (EditText)mContentView.findViewById(ResUtils.getResById(mActivity, "edit_home_phone", "id"));
		mEditCode = (EditText)mContentView.findViewById(ResUtils.getResById(mActivity, "edit_home_code", "id"));
		mTextCode = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_home_code", "id"));
		mImgUserList = (ImageView) mContentView.findViewById(ResUtils.getResById(mActivity, "img_home_user_list", "id"));
		
		mLayoutQuick = mContentView.findViewById(ResUtils.getResById(mActivity, "layout_login_quick", "id"));
		mImgWayQQ = mContentView.findViewById(ResUtils.getResById(mActivity, "img_login_way_0", "id"));
		mImgWayWechat = mContentView.findViewById(ResUtils.getResById(mActivity, "img_login_way_1", "id"));
		
		mLayoutQuick.setOnClickListener(this);
		mImgUserList.setOnClickListener(this);
		mTextCode.setOnClickListener(this);
		mImgWayQQ.setOnClickListener(this);
		mImgWayWechat.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		List<Map<String, String>> loginInfo = LoginInfoHandler.getLoginInfo(LoginInfoHandler.LOGIN_INFO_PHONE);
		if (loginInfo.size() != 0 && getArguments() != null && !getArguments().getBoolean("isSwitchAccount")) {
			Map<String, String> map = loginInfo.get(0);
			String userName = map.get(LoginInfoHandler.USER_ACCOUNT);
			String userPass = map.get(LoginInfoHandler.USER_PASSWORD);
			mLoginHandler.onUserLogin(userName, userPass, true);
			return;
		}
		showLoginWay();
		mActivity.setFragmentTitle(getResources().getString(ResUtils.getResById(mActivity, "ty_phone_login", "string")));
		mLayoutQuick.setVisibility(SPHandler.getBoolean(mActivity, SPHandler.SP_IS_SHOW_KEY) ? View.GONE : View.VISIBLE);
		mLoginInfos = LoginInfoHandler.getLoginInfo(LoginInfoHandler.LOGIN_INFO_PHONE);
		if (mLoginInfos.size() == 0) {
			mImgUserList.setVisibility(View.GONE);
		} else {
			mImgUserList.setVisibility(View.VISIBLE);
			mEditPhone.setText(mLoginInfos.get(0).get(LoginInfoHandler.USER_ACCOUNT));
			mEditCode.setText(mLoginInfos.get(0).get(LoginInfoHandler.USER_PASSWORD));
		}
        mListView = new ListView(mActivity);
        mListView.setBackgroundResource(ResUtils.getResById(mActivity, "listview_background", "drawable"));
        mListView.setAdapter(new LoginAdapter(mActivity, mLoginInfos, mAdapterCallback));
        mEditCode.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View arg0, boolean hasFocus) {
				if(hasFocus) mEditCode.setText("");
			}
		});
	}

	private void showLoginWay() {
		String response = SPHandler.getString(mActivity, SPHandler.SP_LOGIN_WAY);
		LoginWay loginWay = new Gson().fromJson(response, LoginWay.class);
		ResultBean result = loginWay.getResult();
		if (result.getCode() == 200) {
			CustominfoBean custominfo = result.getCustominfo();
			mImgWayQQ.setVisibility(custominfo.getQq_quick() == 1 ? View.VISIBLE : View.GONE);
			mImgWayWechat.setVisibility(custominfo.getWx_quick() == 1 ? View.VISIBLE : View.GONE);
		} else {
			ToastUtils.show(mActivity, result.getMsg());
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == ResUtils.getResById(mActivity, "img_login_way_0", "id")) {
			doQQLogin();
		} else if (v.getId() == ResUtils.getResById(mActivity, "img_login_way_1", "id")) {
			// TODO	微信登陆
		} else if (v.getId() == ResUtils.getResById(mActivity, "img_login_way_2", "id")) {
			mActivity.switchFragment(new AccountFragment(), "AccountFragment");
		} else if (v.getId() == ResUtils.getResById(mActivity, "layout_login_quick", "id")) {
			doOneKeyLogin();
			SPHandler.putBoolean(mActivity, SPHandler.SP_IS_SHOW_KEY, true);
			mLayoutQuick.setVisibility(View.GONE);
		} else if (v.getId() == ResUtils.getResById(mActivity, "btn_home_entry", "id")) {
			doEntryGame();
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_home_code", "id")) {
			getVerificationCode();
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_home_quick", "id")) {
			doQuickRegister();
		} else if (v.getId() == ResUtils.getResById(mActivity, "img_home_user_list", "id")) {
			showPopupWindow();
		}
	}
	
	AdapterCallback mAdapterCallback = new AdapterCallback() {
		@Override
		public List<Map<String, String>> userClick(Map<String, String> map) {
			mEditPhone.setText(map.get(LoginInfoHandler.USER_ACCOUNT));
			mEditCode.setText(map.get(LoginInfoHandler.USER_PASSWORD));
			LoginInfoHandler.putLoginInfo(LoginInfoHandler.LOGIN_INFO_PHONE, map);
			mLoginInfos = LoginInfoHandler.getLoginInfo(LoginInfoHandler.LOGIN_INFO_PHONE);
			mPopupWindow.dismiss();
			return mLoginInfos;
		}
		
		@Override
		public void confirmDelete() {
			LoginInfoHandler.saveLoginInfo(LoginInfoHandler.LOGIN_INFO_PHONE, mLoginInfos);
			if (mLoginInfos.size() == 0) {
				mImgUserList.setVisibility(View.GONE);
				mEditPhone.setText("");
				mEditCode.setText("");
			} else {
				mEditPhone.setText(mLoginInfos.get(0).get(LoginInfoHandler.USER_ACCOUNT));
				mEditCode.setText(mLoginInfos.get(0).get(LoginInfoHandler.USER_PASSWORD));
			}
			mPopupWindow.dismiss();
		}
		
		@Override
		public void cancelDelete() {
			mPopupWindow.dismiss();
		}
	};
	
	// 用户登录下拉弹窗
	private void showPopupWindow() {
		if (mPopupWindow == null) {
			mPopupWindow = new PopupWindow(mListView, 0, 0);
			mPopupWindow.setWidth(mEditPhone.getWidth());
			mPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
			mPopupWindow.setContentView(mListView);
			mPopupWindow.setBackgroundDrawable(getResources().getDrawable(ResUtils.getResById(mActivity, "shape_btn_gray", "drawable")));
			mPopupWindow.setFocusable(true);
		}
		mPopupWindow.showAsDropDown(mEditPhone, 0, 0);
	}

	// 登录游戏
	private void doEntryGame() {
		if (!AppUtils.isMobileNO(mEditPhone.getText().toString())) {
			ToastUtils.show(mActivity, "手机号格式错误");
			return;
		}
		String code = mEditCode.getText().toString();
		if (code.isEmpty()) {
			ToastUtils.show(mActivity, "请输入验证码");
		} else if (loginCode != null && !code.equals(loginCode)) {
			ToastUtils.show(mActivity, "验证码输入有误");
		} else {
			mLoginHandler.onUserLogin(mEditPhone.getText().toString(), mEditCode.getText().toString(), true);
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
					ToastUtils.show(mActivity, code.getResult().getMsg());
					if (code.getResult().getCode() == 200) {
						loginCode = code.getResult().getMobile_code();
						mTextCode.setBackgroundResource(ResUtils.getResById(mActivity, "shape_btn_gray_fill", "drawable"));
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
					mTextCode.setTextColor(Color.parseColor("#FE623F"));
					mTextCode.setClickable(true);
					mTextCode.setBackgroundResource(ResUtils.getResById(mActivity, "shape_bg_jacinth_2", "drawable"));
					handler.removeCallbacks(this);
				}
			}
		}, 1000);
	}
}
