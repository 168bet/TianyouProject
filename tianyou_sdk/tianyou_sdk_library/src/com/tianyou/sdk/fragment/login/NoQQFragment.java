package com.tianyou.sdk.fragment.login;

import com.tianyou.sdk.activity.WebViewAvtivity;
import com.tianyou.sdk.base.BaseLoginFragment;
import com.tianyou.sdk.holder.URLHolder;
import com.tianyou.sdk.utils.ResUtils;

import android.content.Intent;
import android.view.View;

/**
 * 没有找到QQ绑定页面
 * @author itstrong
 *
 */
public class NoQQFragment extends BaseLoginFragment {

	@Override
	protected String setContentView() {
		return "fragment_login_no_qq";
	}

	@Override
	protected void initView() {
		mContentView.findViewById(ResUtils.getResById(mActivity, "btn_no_qq_add", "id")).setOnClickListener(this);
	}

	@Override
	protected void initData() { mActivity.setFragmentTitle(ResUtils.getString(mActivity, "ty_qq_login2")); }

	@Override
	public void onClick(View arg0) {
		Intent intent = new Intent(mActivity, WebViewAvtivity.class);
		intent.putExtra("title", ResUtils.getString(mActivity, "ty_qq_login"));
		intent.putExtra("url", URLHolder.URL_QQ_WEB);
		startActivityForResult(intent, 100);
	}
}
