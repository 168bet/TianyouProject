package com.tianyou.sdk.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tianyou.sdk.bean.LoginInfo;
import com.tianyou.sdk.holder.ConfigHolder;
import com.tianyou.sdk.holder.URLHolder;
import com.tianyou.sdk.utils.AppUtils;
import com.tianyou.sdk.utils.HttpUtils;
import com.tianyou.sdk.utils.HttpUtils.HttpsCallback;
import com.tianyou.sdk.utils.ResUtils;
import com.tianyou.sdk.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

public class WebViewAvtivity extends Activity implements OnClickListener {

	private Activity mActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(ResUtils.getResById(this, "activity_web_view", "layout"));
		mActivity = this;
//		PushAgent.getInstance(this).onAppStart();
		Intent intent = getIntent();
		findViewById(ResUtils.getResById(this, "img_web_back", "id")).setOnClickListener(this);
		TextView textTitle = (TextView) findViewById(ResUtils.getResById(this, "text_web_title", "id"));
		textTitle.setText(intent.getStringExtra("title"));
		WebView mWebView = (WebView) findViewById(ResUtils.getResById(this, "web_view", "id"));
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.loadUrl(intent.getStringExtra("url"));
		mWebView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				String[] split = url.split("&");
				String openid = split[1].split("=")[1];
				String access_token = split[3].split("=")[1];
				getNickName(openid, access_token);
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
			}
		});
	}
	
	// 获取QQ昵称和头像
	private void getNickName(final String openid, final String access_token) {
		String url = "https://graph.qq.com/user/get_user_info?access_token=" + access_token
				+ "&oauth_consumer_key=101322155&openid=" + openid + "&format=json";
		HttpUtils.get(mActivity, url, new HttpsCallback() {
			@Override
			public void onSuccess(String response) {
				try {
					JSONObject jsonObject = new JSONObject(response);
					int ret = jsonObject.getInt("ret");
					if (ret == 0) {
						String nickname = jsonObject.getString("nickname");
						String imgUrl = jsonObject.getString("figureurl_qq_1");
						login(openid, access_token, nickname, imgUrl);
					} else {
						ToastUtils.show(mActivity, "网络连接出错，请检查网络设置...");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private void login(final String openid, final String access_token, final String nickname, final String imgUrl) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("imei", AppUtils.getPhoeIMEI(mActivity));
		map.put("openid", openid);
		map.put("access_token", access_token);
		map.put("ip", AppUtils.getIP());
		map.put("appID", ConfigHolder.GAME_ID);
		map.put("channel", ConfigHolder.CHANNEL_ID);
		map.put("type", "android");
		map.put("nickname", nickname);
		map.put("headimg", imgUrl);
		HttpUtils.post(mActivity, URLHolder.URL_QQ_LOGIN, map, new HttpsCallback() {
			@Override
			public void onSuccess(String response) {
				LoginInfo request = new Gson().fromJson(response, LoginInfo.class);
				Intent intent = new Intent();
				intent.putExtra("login_result", request.getResult());
				setResult(100, intent);
				finish();
			}});
	}
	
	@Override
	public void onClick(View v) {
		finish();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		// 友盟统计
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		// 友盟统计
		MobclickAgent.onPause(this);
	}
}
