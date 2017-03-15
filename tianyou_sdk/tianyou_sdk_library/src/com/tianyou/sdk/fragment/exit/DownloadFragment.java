package com.tianyou.sdk.fragment.exit;

import com.tianyou.sdk.base.BaseFragment;
import com.tianyou.sdk.utils.HttpUtils;
import com.tianyou.sdk.utils.ResUtils;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DownloadFragment extends BaseFragment {

	private TextView mTextName;
	private TextView mTextSize;
	private ImageView mImgIcon;

	public static Fragment getInstall(Bundle bundle) {
		DownloadFragment fragment = new DownloadFragment();
		fragment.setArguments(bundle);
		return fragment;
	}
	
	@Override
	protected String setContentView() {
		return "fragment_exit_download";
	}

	@Override
	protected void initView() {
		mContentView.findViewById(ResUtils.getResById(mActivity, "btn_download_game", "id")).setOnClickListener(this);
		mTextName = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_download_name", "id"));
		mTextSize = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_download_size", "id"));
		mImgIcon = (ImageView) mContentView.findViewById(ResUtils.getResById(mActivity, "img_download_icon", "id"));
	}

	@Override
	protected void initData() {
		mTextName.setText(getArguments().getString("game_name"));
		mTextSize.setText(getArguments().getString("game_size") + "M");
		HttpUtils.imageLoad(mActivity, getArguments().getString("game_icon"), mImgIcon);
	}

	@Override
	public void onClick(View arg0) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(getArguments().getString("game_url")));
		mActivity.startActivity(intent);
	}
}
