package com.tianyou.sdk.fragment.login;

import com.tianyou.sdk.base.BaseFragment;
import com.tianyou.sdk.fragment.login.AlertPasswordFragment.AlertType;
import com.tianyou.sdk.holder.ConfigHolder;
import com.tianyou.sdk.interfaces.TianyouxiCallback;
import com.tianyou.sdk.interfaces.TianyouxiSdk;
import com.tianyou.sdk.utils.LogUtils;
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
	private View mViewPoint0;
	private View mViewPoint1;
	private TextView mTextAccount;
	private TextView mTextSet;
	private TextView mTextAuth;

	@Override
	protected String setContentView() { return "fragment_login_personal_center"; }

	@Override
	protected void initView() {
		mActivity.setFragmentTitle("个人中心");
		mContentView.findViewById(ResUtils.getResById(mActivity, "text_center_logout", "id")).setOnClickListener(this);
		mContentView.findViewById(ResUtils.getResById(mActivity, "text_center_upgrade", "id")).setOnClickListener(this);
		mContentView.findViewById(ResUtils.getResById(mActivity, "text_center_alert", "id")).setOnClickListener(this);
		mContentView.findViewById(ResUtils.getResById(mActivity, "layout_center_setting", "id")).setOnClickListener(this);
		if (!ConfigHolder.isAuth) {
			mContentView.findViewById(ResUtils.getResById(mActivity, "layout_center_identifi", "id")).setOnClickListener(this);
		}
		mLayoutTourist = mContentView.findViewById(ResUtils.getResById(mActivity, "layout_center_tourist", "id"));
		mLayoutNotTourist = mContentView.findViewById(ResUtils.getResById(mActivity, "layout_center_not_tourist", "id"));
		mViewPoint0 = mContentView.findViewById(ResUtils.getResById(mActivity, "img_center_point0", "id"));
		mViewPoint1 = mContentView.findViewById(ResUtils.getResById(mActivity, "img_center_point1", "id"));
		
		mTextAccount = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_center_account", "id"));
		mTextSet = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_center_set", "id"));
		mTextAuth = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_center_auth", "id"));
	}

	@Override
	protected void initData() {
		mLayoutTourist.setVisibility(ConfigHolder.isTourist ? View.VISIBLE : View.GONE);
		mLayoutNotTourist.setVisibility(ConfigHolder.isTourist ? View.GONE : View.VISIBLE);
		mTextAccount.setText(ConfigHolder.isTourist ? "游客：" + ConfigHolder.userName : ConfigHolder.userName);
		mTextSet.setText(ConfigHolder.isPhone ? "已设置" : "未设置");
		mTextAuth.setText(ConfigHolder.isAuth ? "已认证" : "未认证");
		mViewPoint0.setVisibility(ConfigHolder.isPhone ? View.GONE : View.VISIBLE);
		mViewPoint1.setVisibility(ConfigHolder.isAuth ? View.GONE : View.VISIBLE);
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == ResUtils.getResById(mActivity, "text_center_logout", "id")) {
			mLoginHandler.doLogout();
			mActivity.switchFragment(new AccountFragment());
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_center_upgrade", "id")) {
			mActivity.switchFragment(new UpgradeFragment());
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_center_alert", "id")) {
			AlertType type = ConfigHolder.isPhone ? AlertType.ALERT_TYPE_PHONE_0 : AlertType.ALERT_TYPE_ACCOUNT;
			mActivity.switchFragment(new AlertPasswordFragment(type, ConfigHolder.userName, ConfigHolder.userPhone));
		} else if (v.getId() == ResUtils.getResById(mActivity, "layout_center_identifi", "id")) {
			mActivity.switchFragment(new IdentifiFragment());
		} else if (v.getId() == ResUtils.getResById(mActivity, "layout_center_setting", "id")) {
			mActivity.switchFragment(new SafetySettingFragment(ConfigHolder.isPhone));
		}
	}
}
