package com.tianyou.sdk.fragment.pay;

import com.tianyou.sdk.base.BaseFragment;
import com.tianyou.sdk.utils.HttpUtils;
import com.tianyou.sdk.utils.ResUtils;
import com.tianyou.sdk.utils.ToastUtils;

import android.app.ProgressDialog;
import android.os.Handler;
import android.view.View;

/**
 * Created by itstrong on 2016/7/2.
 */
public class NetworkFragment extends BaseFragment {

	@Override
	protected String setContentView() {
		return "fragment_pay_no_net";
	}

	@Override
	protected void initView() {
		mContentView.findViewById(ResUtils.getResById(mActivity, "btn_no_network_refresh", "id")).setOnClickListener(this);
	}

	@Override
	protected void initData() { }

	@Override
	public void onClick(View arg0) {
		final ProgressDialog mLoadingDialog = ProgressDialog.show(mActivity, "", "刷新中...", true);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				mLoadingDialog.dismiss();
				boolean isConnected = HttpUtils.isNetConnected(mActivity);
				ToastUtils.show(mActivity, isConnected ? "刷新成功！" : "刷新失败！");
				if (isConnected) {
					mActivity.onBackPressed();
				}
			}
		}, 3000);
	}
}
