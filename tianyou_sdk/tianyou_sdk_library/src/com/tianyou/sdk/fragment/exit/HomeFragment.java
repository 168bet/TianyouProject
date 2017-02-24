package com.tianyou.sdk.fragment.exit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tianyou.sdk.activity.MenuActivity;
import com.tianyou.sdk.base.BaseFragment;
import com.tianyou.sdk.bean.ExitGame;
import com.tianyou.sdk.bean.ExitGame.ResultBean;
import com.tianyou.sdk.bean.ExitGame.ResultBean.ProductinfoBean;
import com.tianyou.sdk.holder.ConfigHolder;
import com.tianyou.sdk.holder.URLHolder;
import com.tianyou.sdk.interfaces.TianyouCallback;
import com.tianyou.sdk.interfaces.Tianyouxi;
import com.tianyou.sdk.utils.HttpUtils;
import com.tianyou.sdk.utils.HttpUtils.HttpsCallback;
import com.tianyou.sdk.utils.ResUtils;
import com.umeng.analytics.MobclickAgent;

public class HomeFragment extends BaseFragment {

	private LinearLayout mLayoutList;
	private Bundle bundle;

	@Override
	protected String setContentView() {
		return "fragment_exit_home";
	}

	@Override
	protected void initView() {
		mContentView.findViewById(ResUtils.getResById(mActivity, "text_home_recommend", "id")).setOnClickListener(this);
		mContentView.findViewById(ResUtils.getResById(mActivity, "text_home_exit_game", "id")).setOnClickListener(this);
		mLayoutList = (LinearLayout) mContentView.findViewById(ResUtils.getResById(mActivity, "layout_exit_game_list", "id"));
	}

	@Override
	protected void initData() {
		bundle = new Bundle();
		Map<String, String> map = new HashMap<String, String>();
    	map.put("appID", ConfigHolder.GAME_ID);
    	map.put("usertoken", ConfigHolder.GAME_TOKEN);
		HttpUtils.post(mActivity, URLHolder.URL_EXIT_MORE, map, new HttpsCallback() {
			@Override
			public void onSuccess(String response) {
				ExitGame exitGame = new Gson().fromJson(response, ExitGame.class);
				ResultBean result = exitGame.getResult();
				if (result.getCode() == 200) {
					List<ProductinfoBean> gameInfo = result.getProductinfo();
					for (ProductinfoBean game : gameInfo) {
						View view =  View.inflate(mActivity, ResUtils.getResById(mActivity, "item_exit_game", "layout"), null);
						ImageView imgIcon = (ImageView) view.findViewById(ResUtils.getResById(mActivity, "img_exit_icon", "id"));
						TextView textName = (TextView) view.findViewById(ResUtils.getResById(mActivity, "text_exit_name", "id"));
						TextView textCount = (TextView) view.findViewById(ResUtils.getResById(mActivity, "text_exit_count", "id"));
						HttpUtils.imageLoad(mActivity, game.getIcon(), imgIcon);
						imgIcon.setOnClickListener(new ItemClickListener(game));
						textName.setText(game.getName());
						textCount.setText("下载量:" + game.getDownload());
						mLayoutList.addView(view);
					}
				}
			}
		});
	}
	
	class ItemClickListener implements OnClickListener {

		private ProductinfoBean mGameInfo;
		
		public ItemClickListener(ProductinfoBean game) {
			mGameInfo = game;
		}
		
		@Override
		public void onClick(View arg0) {
			bundle.putString("game_name", mGameInfo.getName());
			bundle.putString("game_size", mGameInfo.getFilesize());
			bundle.putString("game_icon", mGameInfo.getIcon());
			bundle.putString("game_url", mGameInfo.getPath());
			mActivity.switchFragment(DownloadFragment.getInstall(bundle), "DownloadFragment");
		}
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == ResUtils.getResById(mActivity, "text_home_recommend", "id")) {
			Intent intent = new Intent(mActivity, MenuActivity.class);
			intent.putExtra("menu_type", MenuActivity.POPUP_MENU_5);
			mActivity.startActivity(intent);
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_home_exit_game", "id")) {
			MobclickAgent.onProfileSignOff();
			MobclickAgent.onKillProcess(mActivity);
			mActivity.finish();
			Tianyouxi.mTianyouCallback.onResult(TianyouCallback.CODE_QUIT_SUCCESS, "");
		} else if (v.getId() == ResUtils.getResById(mActivity, "img_exit_icon", "id")) {
			mActivity.switchFragment(DownloadFragment.getInstall(bundle), "DownloadFragment");
		}
	}
}
