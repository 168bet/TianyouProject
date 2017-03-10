package com.tianyou.sdk.fragment.pay;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tianyou.sdk.activity.PayActivity;
import com.tianyou.sdk.base.BaseFragment;
import com.tianyou.sdk.bean.PayParamInfo;
import com.tianyou.sdk.holder.ConfigHolder;
import com.tianyou.sdk.holder.SPHandler;
import com.tianyou.sdk.interfaces.TianyouCallback;
import com.tianyou.sdk.interfaces.Tianyouxi;
import com.tianyou.sdk.utils.AppUtils;
import com.tianyou.sdk.utils.ResUtils;

/**
 * 支付成功界面
 * Created by itstrong on 2016/7/1.
 */
public class SuccessFragment extends BaseFragment {

	private TextView mTextServer;
	private TextView mTextMoney;
	private TextView mTextOrder;
	private TextView mTextQQ;
	private Button mBtnCallService;
	
    
	@Override
	protected String setContentView() {
		if (ConfigHolder.isLandscape) {
			return "fragment_pay_seccess_land";
		} else {
			return "fragment_pay_seccess";
		}
	}

	@Override
	protected void initView() {
		mTextServer = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_success_server", "id"));
    	mTextMoney = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_success_money", "id"));
    	mTextOrder = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_success_order", "id"));
    	mTextQQ = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_success_service_qq", "id"));
    	mContentView.findViewById(ResUtils.getResById(mActivity, "btn_success_backgame", "id")).setOnClickListener(this);
    	if (ConfigHolder.isLandscape) {
    		mBtnCallService = (Button) mContentView.findViewById(ResUtils.getResById(mActivity, "btn_success_call_service", "id"));
    		mBtnCallService.setText("客服电话："+SPHandler.getString(mActivity, SPHandler.SP_TEXT_PHONE));
    		mBtnCallService.setOnClickListener(this);
		}
    	mTextQQ.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		Tianyouxi.mTianyouCallback.onResult(TianyouCallback.CODE_PAY_SUCCESS, "");
		PayActivity activity = (PayActivity) getActivity();
		PayParamInfo payInfo = activity.mPayHandler.mPayInfo;
		if (activity.mPayHandler.mPayInfo.getOrderId().isEmpty()){
			mTextOrder.setVisibility(View.GONE);
		} else {
			mTextOrder.setText(ResUtils.getString(mActivity, "ty_pay_order2") + payInfo.getOrderId());
		}
    	mTextServer.setText(ResUtils.getString(mActivity, "ty_pay_server2") + payInfo.getServerName());
    	mTextMoney.setText(ResUtils.getString(mActivity, "ty_pay_money2") + Integer.parseInt(payInfo.getMoney()) * payInfo.getScale()+payInfo.getCurrency());
    	
    	mTextQQ.setText(SPHandler.getString(mActivity, SPHandler.SP_TEXT_QQ));
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == ResUtils.getResById(mActivity, "btn_success_backgame", "id")) {
			mActivity.finish();
		} else if (v.getId() == ResUtils.getResById(mActivity, "btn_success_call_service", "id")) {
			AppUtils.callServerPhone(mActivity);
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_success_service_qq", "id")) {
			AppUtils.getQQTalk(mActivity);
		} 
	}
}
