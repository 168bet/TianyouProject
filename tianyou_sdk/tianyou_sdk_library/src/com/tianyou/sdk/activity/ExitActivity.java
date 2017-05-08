package com.tianyou.sdk.activity;

import com.tianyou.sdk.base.BaseActivity;
import com.tianyou.sdk.fragment.exit.HomeFragment;
import com.tianyou.sdk.interfaces.TianyouCallback;
import com.tianyou.sdk.interfaces.TianyouSdk;
import com.tianyou.sdk.utils.ResUtils;

import android.view.KeyEvent;
import android.view.View;

/**
 * 退出游戏Activity
 * @author itstrong
 *
 */
public class ExitActivity extends BaseActivity {

	@Override
	protected int setContentView() {
		return ResUtils.getResById(this, "activity_exit", "layout");
	}

	@Override
	protected void initView() {
		switchFragment(new HomeFragment());
		findViewById(ResUtils.getResById(this, "img_exit_last", "id")).setOnClickListener(this);
		findViewById(ResUtils.getResById(this, "img_exit_close", "id")).setOnClickListener(this);
	}

	@Override
	protected void initData() { }

	@Override
	public void onClick(View v) {
		if (v.getId() == ResUtils.getResById(this, "img_exit_last", "id")) {
			onBackPressed();
		} else if (v.getId() == ResUtils.getResById(this, "img_exit_close", "id")) {
			finish();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (mFragmentTag.equals("HomeFragment")) {
			TianyouSdk.getInstance().mTianyouCallback.onResult(TianyouCallback.CODE_QUIT_SUCCESS, "");
		}
		return super.onKeyDown(keyCode, event);
	}
}
