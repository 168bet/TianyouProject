package com.tianyou.sdk.fragment.login;

import android.R;
import android.content.IntentSender.SendIntentException;
import android.view.View;

import com.alipay.android.phone.mrpc.core.r;
import com.google.android.gms.common.ConnectionResult;
import com.tianyou.sdk.activity.LoginActivity;
import com.tianyou.sdk.base.BaseFragment;
import com.tianyou.sdk.holder.ConfigHolder;
import com.tianyou.sdk.utils.LogUtils;
import com.tianyou.sdk.utils.ResUtils;

/**
 * 一键登录页面
 * @author itstrongx
 *
 */
public class OneKeyFragment extends BaseFragment {
	
	@Override
	protected String setContentView() { return ConfigHolder.isOverseas ? "fragment_login_one_key_overseas" : "fragment_login_one_key"; }

	@Override
	protected void initView() {
		((LoginActivity)mActivity).setBackBtnVisible(false);
		((LoginActivity)mActivity).setBgHeight(true);
		mActivity.setFragmentTitle(ResUtils.getString(mActivity, "ty_one_key_login"));
		mContentView.findViewById(ResUtils.getResById(mActivity, "layout_one_key_tourist", "id")).setOnClickListener(this);
		if(ConfigHolder.isOverseas) {
			mContentView.findViewById(ResUtils.getResById(mActivity, "layout_one_key_simulate_facebook", "id")).setOnClickListener(this);
			mContentView.findViewById(ResUtils.getResById(mActivity, "text_one_key_account", "id")).setOnClickListener(this);
			mContentView.findViewById(ResUtils.getResById(mActivity, "layout_one_key_quicklogin", "id")).setOnClickListener(this);
			mContentView.findViewById(ResUtils.getResById(mActivity, "btn_facebook_login", "id")).setOnClickListener(this);
			mContentView.findViewById(ResUtils.getResById(mActivity, "btn_google_login", "id")).setOnClickListener(this);
		} else {
			mContentView.findViewById(ResUtils.getResById(mActivity, "layout_one_key_register", "id")).setOnClickListener(this);
			mContentView.findViewById(ResUtils.getResById(mActivity, "text_one_key_qq", "id")).setOnClickListener(this);
		}
	}

	@Override
	protected void initData() { }

	@Override
	public void onClick(View v) {
		if (v.getId() == ResUtils.getResById(mActivity, "layout_one_key_tourist", "id")) {
			mLoginHandler.doQuickRegister();
		} else if (v.getId() == ResUtils.getResById(mActivity, "layout_one_key_register", "id")) {
			((LoginActivity)mActivity).switchFragment(new PhoneRegisterFragment());
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_one_key_qq", "id")) {
			doQQLogin();
		} else if(v.getId() == ResUtils.getResById(mActivity, "layout_one_key_simulate_facebook", "id")){
			mContentView.findViewById(ResUtils.getResById(mActivity, "btn_facebook_login", "id")).performClick(); 
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_one_key_account", "id")) {
			mActivity.switchFragment(new AccountFragment());
		} else if (v.getId() == ResUtils.getResById(mActivity, "layout_one_key_quicklogin", "id")) {
			mActivity.switchFragment(new UserRegisterFragment());
		} else if (v.getId() == ResUtils.getResById(mActivity, "btn_google_login", "id")&& !((LoginActivity) mActivity).getGoogleApiClient().isConnected()) {
			LogUtils.d("into google");
			((LoginActivity) mActivity).setIsGoogleConnected(true);
			ConnectionResult connectionResult = ((LoginActivity) mActivity).getConnectionResult();
			if (connectionResult == null) {
				LogUtils.d("connection == null");
			} else {
				try {
					connectionResult.startResolutionForResult(mActivity, 1);
				} catch (SendIntentException e) {
					connectionResult = null;
					((LoginActivity) mActivity).getGoogleApiClient().connect();
					e.printStackTrace();
				}
			}
		}
	}
	@Override
	public void onStart() {
		super.onStart();
	}
	@Override
	public void onStop() {
		super.onStop();
	}
}
