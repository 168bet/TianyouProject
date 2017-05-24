package com.tianyou.sdk.fragment.login;

import com.tianyou.sdk.activity.LoginActivity;
import com.tianyou.sdk.base.BaseFragment;
import com.tianyou.sdk.utils.ResUtils;

import android.view.View;
import android.webkit.WebView;

/**
 * 用户注册协议
 * @author itstrong
 *
 */
public class ProtocolFragment extends BaseFragment {

	private WebView mWebView;

	@Override
	protected String setContentView() { return "fragment_login_protocol"; }

	@Override
	protected void initView() {
		mWebView = (WebView) mContentView.findViewById(ResUtils.getResById(mActivity, "web_view_protocol", "id"));
	}

	@Override
	protected void initData() {
		mActivity.setFragmentTitle("用户注册协议");
		((LoginActivity)mActivity).setBackBtnVisible(true);
		mWebView.loadUrl("file:///android_asset/agreement.html");
	}

	@Override
	public void onClick(View arg0) { }
}
