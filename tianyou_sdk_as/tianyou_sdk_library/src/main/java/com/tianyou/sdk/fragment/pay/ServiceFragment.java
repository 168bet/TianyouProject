package com.tianyou.sdk.fragment.pay;

import com.tianyou.sdk.base.BaseFragment;
import com.tianyou.sdk.holder.ConfigHolder;
import com.tianyou.sdk.holder.SPHandler;
import com.tianyou.sdk.utils.HttpUtils;
import com.tianyou.sdk.utils.ResUtils;
import com.tianyou.sdk.utils.ToastUtils;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 客服中心页面
 * Created by itstrong on 2016/7/1.
 */
public class ServiceFragment extends BaseFragment {

	private TextView mTextNumber;
	private TextView mTextQQNumber;
	private ImageView mImgCall;
	private ImageView mImgQQ;
	private ImageView mImgCode;
	
	@Override
	protected String setContentView() {
		return ConfigHolder.isLandscape ? "fragment_pay_service_land" : "fragment_pay_service_port";
	}

	@Override
	protected void initView() {
		mContentView.findViewById(ResUtils.getResById(mActivity, "layout_server_call", "id")).setOnClickListener(this);
    	mContentView.findViewById(ResUtils.getResById(mActivity, "layout_server_qq", "id")).setOnClickListener(this);
    	mTextNumber = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_server_number", "id"));
    	mTextQQNumber = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_remit_qq_number", "id"));
		mImgCall = (ImageView) mContentView.findViewById(ResUtils.getResById(mActivity, "img_service_call", "id"));
		mImgQQ = (ImageView) mContentView.findViewById(ResUtils.getResById(mActivity, "img_service_qq", "id"));
		mImgCode = (ImageView) mContentView.findViewById(ResUtils.getResById(mActivity, "img_service_code", "id"));
	}

	@Override
	protected void initData() {
		HttpUtils.imageLoad(mActivity, SPHandler.getString(mActivity, SPHandler.SP_URL_PHONE), mImgCall);
		HttpUtils.imageLoad(mActivity, SPHandler.getString(mActivity, SPHandler.SP_URL_QQ),mImgQQ);
		HttpUtils.imageLoad(mActivity, SPHandler.getString(mActivity, SPHandler.SP_URL_WX), mImgCode);
		mTextNumber.setText(SPHandler.getString(mActivity, SPHandler.SP_TEXT_PHONE));
		mTextQQNumber.setText(SPHandler.getString(mActivity, SPHandler.SP_TEXT_QQ));
		Log.d("TAG", "service phone= "+SPHandler.getString(mActivity, SPHandler.SP_TEXT_PHONE));
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == ResUtils.getResById(mActivity, "layout_server_qq", "id")) {
			try {
				String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + mTextQQNumber.getText().toString();  
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
			} catch (Exception e) {
				ToastUtils.show(mActivity, "没有安装手机QQ");
			}
		} else {			
			String phoneNum = SPHandler.getString(mActivity, SPHandler.SP_TEXT_PHONE);
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum));
			startActivity(intent);
		}
	}
}
