package com.tianyou.sdk.fragment.login;

import com.tianyou.sdk.activity.LoginActivity;
import com.tianyou.sdk.base.BaseFragment;
import com.tianyou.sdk.utils.ResUtils;
import com.tianyou.sdk.utils.ToastUtils;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;

/**
 * 修改密码页面
 * @author itstrong
 *
 */
public class AlertPasswordFragment extends BaseFragment {

	public static Fragment getInstance(int step) {
		Fragment fragment = new AlertPasswordFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("step", step);
		fragment.setArguments(bundle);
		return fragment;
	}
	@Override
	protected String setContentView() {
		if (getArguments().getInt("step") == 0) {
			return "fragment_login_alert_password0";
		} else if (getArguments().getInt("step") == 1) {
			return "fragment_login_alert_password1";
		} else if (getArguments().getInt("step") == 2) {
			return "fragment_login_alert_password2";
		} else {
			return "fragment_login_alert_password3";
		}
	}

	@Override
	protected void initView() {
		mActivity.setFragmentTitle("修改密码");
		((LoginActivity)mActivity).setBackBtnVisible(true);
		if (getArguments().getInt("step") == 0) {
			mContentView.findViewById(ResUtils.getResById(mActivity, "layout_alert_context", "id")).setOnClickListener(this);
		} else if (getArguments().getInt("step") == 1) {
			mContentView.findViewById(ResUtils.getResById(mActivity, "text_alert_confirm", "id")).setOnClickListener(this);;
			mContentView.findViewById(ResUtils.getResById(mActivity, "text_alert_forget", "id")).setOnClickListener(this);;
		} else if (getArguments().getInt("step") == 2) {
			mContentView.findViewById(ResUtils.getResById(mActivity, "text_alert_confirm2", "id")).setOnClickListener(this);;
		}
	}

	@Override
	protected void initData() { }

	@Override
	public void onClick(View v) {
		if (v.getId() == ResUtils.getResById(mActivity, "layout_alert_context", "id")) {
			mActivity.switchFragment(AlertPasswordFragment.getInstance(1));
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_alert_confirm", "id")) {
			mActivity.switchFragment(AlertPasswordFragment.getInstance(2));
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_alert_confirm2", "id")) {
			ToastUtils.show(mActivity, "修改成功");
			mActivity.finish();
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_alert_forget", "id")) {
			mActivity.switchFragment(AlertPasswordFragment.getInstance(3));
		}
	}
}
