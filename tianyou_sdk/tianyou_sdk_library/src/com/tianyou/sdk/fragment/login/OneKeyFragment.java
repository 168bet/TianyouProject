package com.tianyou.sdk.fragment.login;

import com.tianyou.sdk.activity.LoginActivity;
import com.tianyou.sdk.base.BaseFragment;
import com.tianyou.sdk.holder.ConfigHolder;
import com.tianyou.sdk.utils.ResUtils;

import android.view.View;

/**
 * 一键登录页面
 * @author itstrongx
 *
 */
public class OneKeyFragment extends BaseFragment {
	
	@Override
	protected String setContentView() { return ConfigHolder.isOverseas ? "fragment_login_one_key_overseas" : "fragment_login_one_key"; }

	@Override
	protected void initView() {
		((LoginActivity)mActivity).setBackBtnVisible(false);
		((LoginActivity)mActivity).setBgHeight(true);
		mActivity.setFragmentTitle("一键登录");
		mContentView.findViewById(ResUtils.getResById(mActivity, "layout_one_key_tourist", "id")).setOnClickListener(this);
		mContentView.findViewById(ResUtils.getResById(mActivity, "layout_one_key_register", "id")).setOnClickListener(this);
		mContentView.findViewById(ResUtils.getResById(mActivity, "text_one_key_qq", "id")).setOnClickListener(this);
		mContentView.findViewById(ResUtils.getResById(mActivity, "text_one_key_account", "id")).setOnClickListener(this);
	}

	@Override
	protected void initData() { }

	@Override
	public void onClick(View v) {
		if (v.getId() == ResUtils.getResById(mActivity, "layout_one_key_tourist", "id")) {
			mLoginHandler.doQuickRegister();
		} else if (v.getId() == ResUtils.getResById(mActivity, "layout_one_key_register", "id")) {
			((LoginActivity)mActivity).switchFragment(new PhoneRegisterFragment());
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_one_key_qq", "id")) {
			doQQLogin();
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_one_key_account", "id")) {
			mActivity.switchFragment(new AccountFragment());
		}
	}
}
