package com.tianyou.sdk.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tianyou.sdk.bean.ExitGame;
import com.tianyou.sdk.bean.ExitGame.ResultBean;
import com.tianyou.sdk.bean.ExitGame.ResultBean.ProductinfoBean;
import com.tianyou.sdk.holder.ConfigHolder;
import com.tianyou.sdk.holder.LoginHandler;
import com.tianyou.sdk.holder.URLHolder;
import com.tianyou.sdk.interfaces.TianyouCallback;
import com.tianyou.sdk.interfaces.Tianyouxi;
import com.tianyou.sdk.utils.HttpUtils;
import com.tianyou.sdk.utils.HttpUtils.HttpsCallback;
import com.tianyou.sdk.utils.ResUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * 公告Activity
 * @author itstrong
 *
 */
public class NotifyActivity extends Activity implements OnClickListener {

	private WebView mWebView;
	private TextView mTextName;
	private TextView mTextGame;
	private View mViewLine0;
	private View mViewLine1;
	private GridView mGridView;
	private ImageView mImgIcon;
	
	private Activity mActivity;
	private String content;
	private List<ProductinfoBean> mGameInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = this;
		setContentView(ResUtils.getResById(this, "activity_notify", "layout"));
		setRequestedOrientation(ConfigHolder.isLandscape ? ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE 
				: ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
//		PushAgent.getInstance(this).onAppStart();
		content = getIntent().getStringExtra("content");
		initFindViewById();
		initData();
	}
	
	private void initFindViewById() {
		findViewById(ResUtils.getResById(mActivity, "img_announce_close", "id")).setOnClickListener(this);
		findViewById(ResUtils.getResById(mActivity, "layout_announce_menu_0", "id")).setOnClickListener(this);
		findViewById(ResUtils.getResById(mActivity, "layout_announce_menu_1", "id")).setOnClickListener(this);
		
		mImgIcon = (ImageView) findViewById(ResUtils.getResById(mActivity, "img_announce_icon", "id"));
		mTextName = (TextView) findViewById(ResUtils.getResById(mActivity, "text_announce_name", "id"));
		mTextGame = (TextView) findViewById(ResUtils.getResById(mActivity, "layout_announce_game", "id"));
		mViewLine0 = findViewById(ResUtils.getResById(mActivity, "view_announce_line_0", "id"));
		mViewLine1 = findViewById(ResUtils.getResById(mActivity, "view_announce_line_1", "id"));
		if (ConfigHolder.isLandscape) {
			mGridView = (GridView) findViewById(ResUtils.getResById(mActivity, "grid_view_announce_land", "id"));
		} else {
			mGridView = (GridView) findViewById(ResUtils.getResById(mActivity, "grid_view_announce", "id"));
		}
		mWebView = (WebView) findViewById(ResUtils.getResById(mActivity, "web_view_announce", "id"));
		mWebView.loadDataWithBaseURL("about:blank", content, "text/html", "utf-8", null);
		mWebView.setWebViewClient(new WebViewClient() {
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
	
	private void initData() {
		mGameInfo = new ArrayList<ProductinfoBean>();
		MyAdadpter mMyAdadpter = new MyAdadpter();
		mGridView.setAdapter(mMyAdadpter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(mGameInfo.get(position).getPath()));
				mActivity.startActivity(intent);
			}
		});
		Map<String, String> map = new HashMap<String, String>();
    	map.put("appID", ConfigHolder.GAME_ID);
    	map.put("usertoken", ConfigHolder.GAME_TOKEN);
		HttpUtils.post(mActivity, URLHolder.URL_EXIT_MORE, map, new HttpsCallback() {
			@Override
			public void onSuccess(String response) {
				ExitGame exitGame = new Gson().fromJson(response, ExitGame.class);
				ResultBean result = exitGame.getResult();
				if (result.getCode() == 200) {
					mGameInfo = result.getProductinfo();
					for (final ProductinfoBean game : mGameInfo) {
						View view =  View.inflate(mActivity, ResUtils.getResById(mActivity, "item_exit_game", "layout"), null);
						ImageView imgIcon = (ImageView) view.findViewById(ResUtils.getResById(mActivity, "img_exit_icon", "id"));
						TextView textName = (TextView) view.findViewById(ResUtils.getResById(mActivity, "text_exit_name", "id"));
						TextView textCount = (TextView) view.findViewById(ResUtils.getResById(mActivity, "text_exit_count", "id"));
						HttpUtils.imageLoad(mActivity, game.getIcon(), imgIcon);
						imgIcon.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								Intent intent = new Intent(Intent.ACTION_VIEW);
								intent.setData(Uri.parse(game.getPath()));
								mActivity.startActivity(intent);
							}
						});
						textName.setText(game.getName());
						textCount.setText("下载量:" + game.getDownload());
					}
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == ResUtils.getResById(mActivity, "img_announce_close", "id")) {
			startEntryGame();
		} else if (v.getId() == ResUtils.getResById(mActivity, "layout_announce_menu_0", "id")) {
			mTextName.setTextColor(Color.parseColor("#FE623F"));
			mTextGame.setTextColor(Color.parseColor("#333333"));
			mImgIcon.setImageResource(ResUtils.getResById(mActivity, "ty_game_icon_2", "drawable"));
			mViewLine0.setVisibility(View.INVISIBLE);
			mViewLine1.setVisibility(View.VISIBLE);
			mWebView.setVisibility(View.VISIBLE);
			mGridView.setVisibility(View.GONE);
		} else if (v.getId() == ResUtils.getResById(mActivity, "layout_announce_menu_1", "id")) {
			mTextName.setTextColor(Color.parseColor("#333333"));
			mTextGame.setTextColor(Color.parseColor("#FE623F"));
			mImgIcon.setImageResource(ResUtils.getResById(mActivity, "ty_game_icon", "drawable"));
			mViewLine0.setVisibility(View.VISIBLE);
			mViewLine1.setVisibility(View.INVISIBLE);
			mWebView.setVisibility(View.GONE);
			mGridView.setVisibility(View.VISIBLE);
		}
	}
	
	// 登录成功
	private void startEntryGame() {
		LoginHandler.onNoticeLoginSuccess();
		mActivity.finish();
	}
	
	@Override
	public void onBackPressed() {
		LoginHandler.onNoticeLoginSuccess();
		super.onBackPressed();
	}
	
	class MyAdadpter extends BaseAdapter {

		@Override
		public int getCount() {
			return mGameInfo.size();
		}

		@Override
		public Object getItem(int item) {
			return item;
		}

		@Override
		public long getItemId(int id) {
			return id;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
            if (convertView == null) {
            	holder = new ViewHolder();
            	convertView = View.inflate(mActivity, ResUtils.getResById(mActivity, "item_exit_game", "layout"), null);
            	holder.imgIcon = (ImageView) convertView.findViewById(ResUtils.getResById(mActivity, "img_exit_icon", "id"));
            	holder.textName = (TextView) convertView.findViewById(ResUtils.getResById(mActivity, "text_exit_name", "id"));
            	holder.textCount = (TextView) convertView.findViewById(ResUtils.getResById(mActivity, "text_exit_count", "id"));
            	convertView.setTag(holder);
            } else {
            	holder = (ViewHolder) convertView.getTag();
            }
            ProductinfoBean info = mGameInfo.get(position);
            HttpUtils.imageLoad(mActivity, info.getIcon(), holder.imgIcon);
            holder.textName.setText(info.getName());
            holder.textCount.setText("下载量：" + info.getDownload());
            return convertView;
		}
	}
	
	class ViewHolder {
    	ImageView imgIcon;
    	TextView textName;
    	TextView textCount;
    }
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(mActivity);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(mActivity);
	}
}
