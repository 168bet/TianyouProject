package com.tianyou.sdk.fragment.pay;

import com.tianyou.sdk.base.BaseFragment;
import com.tianyou.sdk.holder.ConfigHolder;
import com.tianyou.sdk.holder.SPHandler;
import com.tianyou.sdk.holder.URLHolder;
import com.tianyou.sdk.utils.AppUtils;
import com.tianyou.sdk.utils.HttpUtils;
import com.tianyou.sdk.utils.ResUtils;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 汇款页面
 * Created by itstrong on 2016/7/3.
 */
public class RemitFragment extends BaseFragment {

	private TextView mTextNumber;
	private ImageView mImageView;
	private TextView mTextQQ;
	
	private LinearLayout mLayoutPhone;
	
	private boolean flag = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		flag = true;
	}
	
	@Override
	protected String setContentView() {
		if (ConfigHolder.isLandscape){
			return "fragment_pay_remit_land";
		} else {
			return "fragment_pay_remit";
		}
	}

	@Override
	protected void initView() {
		// 初始化视图
		mTextNumber = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_remit_phone", "id"));
		mImageView = (ImageView) mContentView.findViewById(ResUtils.getResById(mActivity, "imag_remit_code", "id"));
		mTextQQ = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_remit_service_qq", "id"));
		if (ConfigHolder.isLandscape){
			mLayoutPhone = (LinearLayout) mContentView.findViewById(ResUtils.getResById(mActivity, "ll_remit_phone","id"));
			mLayoutPhone.setOnClickListener(this);
		}
		if (flag){
			HttpUtils.imageDown(mActivity, URLHolder.URL_GET_REMIT_CODE, mImageView, "天游戏汇款二维码");
		} else {
			HttpUtils.imageLoad(mActivity, URLHolder.URL_GET_REMIT_CODE, mImageView);
		}
		if (ConfigHolder.isLandscape){
			mTextQQ.setText("客服QQ："+SPHandler.getString(mActivity, SPHandler.SP_TEXT_QQ));
			mTextNumber.setText(SPHandler.getString(mActivity, SPHandler.SP_TEXT_PHONE));
		} else {
			mTextQQ.setText(SPHandler.getString(mActivity, SPHandler.SP_TEXT_QQ));
			mTextNumber.setText("天游戏客服电话："+SPHandler.getString(mActivity, SPHandler.SP_TEXT_PHONE));
		}
		mTextNumber.setOnClickListener(this);
	}

	@Override
	protected void initData() { }

	@Override
	public void onClick(View v) {
		if (v.getId() == ResUtils.getResById(mActivity, "text_remit_phone", "id")) {
			AppUtils.callServerPhone(mActivity);
		} else if (v.getId() == ResUtils.getResById(mActivity, "ll_remit_phone", "id")) {
			AppUtils.callServerPhone(mActivity);
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		flag = false;
	}
}
