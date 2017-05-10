package com.tianyou.sdk.fragment.login;

import com.tianyou.sdk.base.BaseFragment;
import com.tianyou.sdk.holder.ConfigHolder;
import com.tianyou.sdk.utils.ResUtils;

import android.view.View;
import android.widget.TextView;

/**
 * 个人中心页面
 * @author itstrong
 *
 */
public class PersonalCenterFragment extends BaseFragment {

	private View mLayoutTourist;
	private View mLayoutNotTourist;
	private TextView mTextAccount;

	@Override
	protected String setContentView() { return "fragment_login_personal_center"; }

	@Override
	protected void initView() {
		mActivity.setFragmentTitle("个人中心");
		mContentView.findViewById(ResUtils.getResById(mActivity, "text_center_logout", "id")).setOnClickListener(this);
		mContentView.findViewById(ResUtils.getResById(mActivity, "text_center_upgrade", "id")).setOnClickListener(this);
		mContentView.findViewById(ResUtils.getResById(mActivity, "text_center_alert", "id")).setOnClickListener(this);
		mContentView.findViewById(ResUtils.getResById(mActivity, "layout_center_identifi", "id")).setOnClickListener(this);
		mContentView.findViewById(ResUtils.getResById(mActivity, "layout_center_setting", "id")).setOnClickListener(this);
		
		mLayoutTourist = mContentView.findViewById(ResUtils.getResById(mActivity, "layout_center_tourist", "id"));
		mLayoutNotTourist = mContentView.findViewById(ResUtils.getResById(mActivity, "layout_center_not_tourist", "id"));
		
		mTextAccount = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_center_account", "id"));
	}

	@Override
	protected void initData() {
		mLayoutTourist.setVisibility(ConfigHolder.isTourist ? View.VISIBLE : View.GONE);
		mLayoutNotTourist.setVisibility(ConfigHolder.isTourist ? View.GONE : View.VISIBLE);
		mTextAccount.setText(ConfigHolder.isTourist ? "游客：" + ConfigHolder.userName : ConfigHolder.userName);
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == ResUtils.getResById(mActivity, "text_center_logout", "id")) {
			ConfigHolder.userIsLogin = false;
			mActivity.switchFragment(new AccountFragment());
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_center_upgrade", "id")) {
			mActivity.switchFragment(new UpgradeFragment());
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_center_alert", "id")) {
			mActivity.switchFragment(new AlertPasswordFragment(0, ConfigHolder.userName));
		} else if (v.getId() == ResUtils.getResById(mActivity, "layout_center_identifi", "id")) {
			mActivity.switchFragment(new IdentifiFragment());
		} else if (v.getId() == ResUtils.getResById(mActivity, "layout_center_setting", "id")) {
			mActivity.switchFragment(new SafetySettingFragment());
		}
	}
}
