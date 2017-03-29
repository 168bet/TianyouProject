package com.tianyou.sdk.activity;

import com.google.gson.Gson;
import com.tianyou.sdk.base.FloatControl;
import com.tianyou.sdk.base.FloatControl.ResultBean.CustominfoBean;
import com.tianyou.sdk.holder.ConfigHolder;
import com.tianyou.sdk.holder.SPHandler;
import com.tianyou.sdk.holder.URLHolder;
import com.tianyou.sdk.utils.LogUtils;
import com.tianyou.sdk.utils.ResUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 悬浮菜单Activity
 * @author itstrong
 * 
 */
public class MenuActivity extends Activity implements OnClickListener {

	public static final int POPUP_MENU_0 = 0;
	public static final int POPUP_MENU_1 = 1;
	public static final int POPUP_MENU_2 = 2;
	public static final int POPUP_MENU_3 = 3;
	public static final int POPUP_MENU_4 = 4;
	public static final int POPUP_MENU_5 = 5;
	public static final int POPUP_MENU_6 = 6;
	public static final int POPUP_MENU_7 = 7;
	
	public static int REQUEST_OK = 1;

	private TextView mTextTitle;
	private ProgressBar mProgressBar;
	private WebView mWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(ResUtils.getResById(this, "activity_menu", "layout"));
		PushAgent.getInstance(this).onAppStart();
		initFindViewById();
		showWebView();
	}
	
	private void initFindViewById() {
		findViewById(ResUtils.getResById(this, "img_menu_back", "id")).setOnClickListener(this);
		findViewById(ResUtils.getResById(this, "img_menu_close", "id")).setOnClickListener(this);
		mTextTitle = (TextView) findViewById(ResUtils.getResById(this, "text_menu_title", "id"));
		mProgressBar = (ProgressBar) findViewById(ResUtils.getResById(this, "pg_menu_loading", "id"));
		mWebView = (WebView) findViewById(ResUtils.getResById(this, "web_view_menu", "id"));
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.setWebViewClient(new MyWebViewClient());
	}
	
	class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			LogUtils.d("url:" + url);
			String substring = url.substring(url.length() - 3, url.length());
			LogUtils.d("substring:" + substring);
			if (!"apk".equals(substring)) {
				view.loadUrl(url);
			} else {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(url));
				startActivity(intent);
			}
			return true;
		}
		
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			mProgressBar.setVisibility(View.VISIBLE);
		}
		
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			mProgressBar.setVisibility(View.GONE);
		}
	}
	
	private void showWebView() {
		String response = SPHandler.getString(this, SPHandler.SP_FLOAT_CONTROL);
		FloatControl control = new Gson().fromJson(response, FloatControl.class);
		CustominfoBean info = control.getResult().getCustominfo();
		switch (getIntent().getIntExtra("menu_type", 0)) {
		case POPUP_MENU_0:
			mTextTitle.setText(ResUtils.getString(this, "ty_company_name") + " | " + ResUtils.getString(this, "ty_menu_index"));
			mWebView.loadUrl(getURL(info.getAccount().getUrl()));
			break;
		case POPUP_MENU_1:
			mTextTitle.setText(ResUtils.getString(this, "ty_company_name") + " | " + ResUtils.getString(this, "ty_menu_more"));
			mWebView.loadUrl(getURL(info.getMore().getUrl()));
			break;
		case POPUP_MENU_2:
			mTextTitle.setText(ResUtils.getString(this, "ty_company_name") + " | " + ResUtils.getString(this, "ty_menu_gift"));
			mWebView.loadUrl(getURL(info.getGift().getUrl()));
			break;
		case POPUP_MENU_3:
			mTextTitle.setText(ResUtils.getString(this, "ty_company_name") + " | " + ResUtils.getString(this, "ty_menu_bbs"));
			mWebView.loadUrl(getURL(info.getBbs().getUrl()));
			break;
		case POPUP_MENU_4:
			mTextTitle.setText(ResUtils.getString(this, "ty_company_name") + " | " + ResUtils.getString(this, "ty_menu_help"));
			mWebView.loadUrl(getURL(info.getHelp().getUrl()));
			break;
		case POPUP_MENU_5:
			mTextTitle.setText(ResUtils.getString(this, "ty_company_name") + " | " + ResUtils.getString(this, "ty_hot_game2"));
			mWebView.loadUrl(getURL(info.getMore().getUrl()));
			break;
		case POPUP_MENU_6:
			mTextTitle.setText(ResUtils.getString(this, "ty_company_name") + " | " + ResUtils.getString(this, "ty_forget_password"));
			mWebView.loadUrl(getURL(URLHolder.URL_FORGET_PASS));
			break;
		case POPUP_MENU_7:
			mTextTitle.setText(ResUtils.getString(this, "ty_company_name") + " | " + ResUtils.getString(this, "ty_platform_pay2"));
			String url = getIntent().getStringExtra("url");
			setResult(REQUEST_OK,getIntent());
			LogUtils.d("POPUP_MENU_7:" + url);
			mWebView.loadUrl(url);
			break;
		}
	}

	private String getURL(String url) {
		String getUrl = url + "&username=" + ConfigHolder.userName + "&appid=" + ConfigHolder.gameId
				+ "&token=" + ConfigHolder.gameToken + "&type=sdk" + "&uid=" + ConfigHolder.userId;
		LogUtils.d("url:" + getUrl);
		return getUrl;
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == ResUtils.getResById(this, "img_menu_back", "id")) {
			if (mWebView.canGoBack()) {
				mWebView.goBack();
			} else {
				finish();
			}
		} else if (view.getId() == ResUtils.getResById(this, "img_menu_close", "id")) {
			
			finish();
		}
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
