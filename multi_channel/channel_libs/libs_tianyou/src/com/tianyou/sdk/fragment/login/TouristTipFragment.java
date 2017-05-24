package com.tianyou.sdk.fragment.login;

import com.tianyou.sdk.activity.LoginActivity;
import com.tianyou.sdk.base.BaseFragment;
import com.tianyou.sdk.holder.LoginHandler;
import com.tianyou.sdk.utils.ResUtils;

import android.view.View;

/**
 * 游客提示页面
 * @author itstrong
 *
 */
public class TouristTipFragment extends BaseFragment {

	@Override
	protected String setContentView() { return "fragment_login_tourist_tips"; }

	@Override
	protected void initView() {
		mContentView.findViewById(ResUtils.getResById(mActivity, "text_tourist_account", "id")).setOnClickListener(this);;
		mContentView.findViewById(ResUtils.getResById(mActivity, "text_tourist_upgrade", "id")).setOnClickListener(this);;
	}

	@Override
	protected void initData() {
		mActivity.setFragmentTitle("");
		((LoginActivity)mActivity).setBgHeight(true);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == ResUtils.getResById(mActivity, "text_tourist_account", "id")) {
			LoginHandler.onNoticeLoginSuccess();
			mActivity.finish();
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_tourist_upgrade", "id")) {
			mActivity.switchFragment(new UpgradeFragment());
		}
	}
}
