package com.tianyou.sdk.fragment.login;

import com.tianyou.sdk.base.BaseFragment;
import com.tianyou.sdk.utils.ResUtils;
import com.tianyou.sdk.utils.ToastUtils;

import android.view.View;

/**
 * 账号安全设置页面
 * @author itstrong
 *
 */
public class SafetySettingFragment extends BaseFragment {

	@Override
	protected String setContentView() { return "fragment_login_safety_setting"; }

	@Override
	protected void initView() {
		mContentView.findViewById(ResUtils.getResById(mActivity, "text_safety_confirm", "id")).setOnClickListener(this);
	}

	@Override
	protected void initData() { }

	@Override
	public void onClick(View v) {
		ToastUtils.show(mActivity, "修改成功");
		mActivity.finish();
	}
}
