package com.tianyou.sdk.fragment.login;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.view.View;
import android.webkit.WebView;

import com.tianyou.sdk.activity.LoginActivity;
import com.tianyou.sdk.base.BaseFragment;
import com.tianyou.sdk.holder.ConfigHolder;
import com.tianyou.sdk.holder.URLHolder;
import com.tianyou.sdk.utils.HttpUtils;
import com.tianyou.sdk.utils.HttpUtils.HttpCallback;
import com.tianyou.sdk.utils.ResUtils;

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
		mActivity.setFragmentTitle(ConfigHolder.isOverseas?"User registration agreement":"用户注册协议");
		((LoginActivity)mActivity).setBackBtnVisible(true);
		Map<String,String> map = new HashMap<String, String>();
		HttpUtils.post(mActivity, URLHolder.URL_USER_AGREEMENT, map, new HttpCallback() {
			
			@Override
			public void onSuccess(String response) {
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(response);
					JSONObject result = jsonObject.getJSONObject("result");
					if (result.getInt("code") == 200) {
						mWebView.loadUrl(result.getString("agreeurl"));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFailed() {
				
			}
		});
	}

	@Override
	public void onClick(View arg0) { }
}
