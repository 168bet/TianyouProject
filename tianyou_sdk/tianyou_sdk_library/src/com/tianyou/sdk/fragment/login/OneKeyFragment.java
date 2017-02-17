package com.tianyou.sdk.fragment.login;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.tianyou.sdk.activity.WebViewAvtivity;
import com.tianyou.sdk.base.BaseLoginFragment;
import com.tianyou.sdk.holder.ConfigHolder;
import com.tianyou.sdk.holder.LoginHandler;
import com.tianyou.sdk.holder.URLHolder;
import com.tianyou.sdk.utils.AppUtils;
import com.tianyou.sdk.utils.GoogleLogin;
import com.tianyou.sdk.utils.GoogleSignIn;
import com.tianyou.sdk.utils.HttpUtils;
import com.tianyou.sdk.utils.ToastUtils;
//import com.tianyou.sdk.utils.GoogleLogin;
//import com.tianyou.sdk.utils.GoogleLogin;
import com.tianyou.sdk.utils.LogUtils;
import com.tianyou.sdk.utils.ResUtils;

/**
 * 一键登录
 * @author itstrong
 *
 */
public class OneKeyFragment extends BaseLoginFragment {
	
//	private GoogleLogin mGoogleSignIn;
//	private GoogleSignIn mGoogleSignIn;

	@Override
	protected String setContentView() {
		return ConfigHolder.IS_OVERSEAS ? "fragment_login_one_key_overseas" : "fragment_login_one_key";
	}

	@Override
	protected void initView() {

		mContentView.findViewById(ResUtils.getResById(mActivity, "layout_one_key_quick", "id")).setOnClickListener(this);
		
		if (ConfigHolder.IS_OVERSEAS) {
			mContentView.findViewById(ResUtils.getResById(mActivity, "btn_facebook_login", "id")).setVisibility(View.VISIBLE);
			// 谷歌登录
			Button btnGoogle = (Button) mContentView.findViewById(ResUtils.getResById(mActivity, "btn_google_login", "id"));
			btnGoogle.setVisibility(View.VISIBLE);
//			mGoogleSignIn = new GoogleLogin(mActivity);//, new MyOnConnectionFailedListener());
			btnGoogle.setOnClickListener(this);
		} else {
			mContentView.findViewById(ResUtils.getResById(mActivity, "text_one_key_qq", "id")).setOnClickListener(this);
			mContentView.findViewById(ResUtils.getResById(mActivity, "text_one_key_account", "id")).setOnClickListener(this);
			mContentView.findViewById(ResUtils.getResById(mActivity, "text_one_key_server", "id")).setOnClickListener(this);
			
			View textMsm = mContentView.findViewById(ResUtils.getResById(mActivity, "text_one_key_msm", "id"));
			textMsm.setVisibility(View.VISIBLE);
			textMsm.setOnClickListener(this);
			
			View textQQ = mContentView.findViewById(ResUtils.getResById(mActivity, "text_one_key_qq", "id"));
			textQQ.setVisibility(View.VISIBLE);
			textQQ.setOnClickListener(this);
		}
		
//		LogUtils.d("isChineseLanguage:" + AppUtils.isChineseLanguage(mActivity));
//		if (AppUtils.isChineseLanguage(mActivity)) {
//			View textMsm = mContentView.findViewById(ResUtils.getResById(mActivity, "text_one_key_msm", "id"));
//			textMsm.setVisibility(View.VISIBLE);
//			textMsm.setOnClickListener(this);
//			
//			View textQQ = mContentView.findViewById(ResUtils.getResById(mActivity, "text_one_key_qq", "id"));
//			textQQ.setVisibility(View.VISIBLE);
//			textQQ.setOnClickListener(this);
//		} else {
//			mContentView.findViewById(ResUtils.getResById(mActivity, "btn_facebook_login", "id")).setVisibility(View.VISIBLE);
//			// 谷歌登录
//			Button btnGoogle = (Button) mContentView.findViewById(ResUtils.getResById(mActivity, "btn_google_login", "id"));
//			btnGoogle.setVisibility(View.VISIBLE);
//			mGoogleSignIn = new GoogleLogin(mActivity);//, new MyOnConnectionFailedListener());
//			btnGoogle.setOnClickListener(this);
//		}
	}

	@Override
	protected void initData() { ResUtils.getString(mActivity, "ty_one_key_login"); }

	@Override
	public void onClick(View v) {
		if (v.getId() == ResUtils.getResById(mActivity, "layout_one_key_quick", "id")) {
			doQuickRegister();
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_one_key_msm", "id")) {
			doOneKeyLogin();
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_one_key_qq", "id")) {
			Intent intent = new Intent(mActivity, WebViewAvtivity.class);
			intent.putExtra("title", ResUtils.getString(mActivity, "ty_qq_login"));
			intent.putExtra("url", URLHolder.URL_QQ_WEB);
			startActivityForResult(intent, 100);
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_one_key_account", "id")) {
			mActivity.switchFragment(new AccountFragment(), "AccountFragment");
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_one_key_server", "id")) {
			AppUtils.openServerTerms(mActivity);
		} else if (v.getId() == ResUtils.getResById(mActivity,"btn_google_login","id")) {
//			mGoogleSignIn.signIn();
			try {
				mActivity.getConnectionResult().startResolutionForResult(mActivity, 1);
			} catch (SendIntentException e) {
				if (mActivity.getGoogleApiClient() != null){ 
					mActivity.getGoogleApiClient().connect(); 
				} 
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void onStart() {
//		mGoogleSignIn.onStart();
		super.onStart();
	}
	
	@Override
	public void onStop() {
//		mGoogleSignIn.onStop();
		super.onStop();
	}
	
}
