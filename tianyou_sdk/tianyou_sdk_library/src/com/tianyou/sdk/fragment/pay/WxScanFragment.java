package com.tianyou.sdk.fragment.pay;

import com.tianyou.sdk.activity.MenuActivity;
import com.tianyou.sdk.activity.PayActivity;
import com.tianyou.sdk.base.BaseFragment;
import com.tianyou.sdk.bean.PayInfo;
import com.tianyou.sdk.holder.ConfigHolder;
import com.tianyou.sdk.holder.SPHandler;
import com.tianyou.sdk.holder.URLHolder;
import com.tianyou.sdk.utils.AppUtils;
import com.tianyou.sdk.utils.HttpUtils;
import com.tianyou.sdk.utils.ResUtils;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class WxScanFragment extends BaseFragment{

	private ImageView mImageView;
	private TextView mTextMoney;
	private TextView mTextServiceQQ;
	
	private TextView mTextOrderLand;
	private TextView mTextServerLand;
	private TextView mTextMoneyLand;
	
	private TextView mTextQQLand;
	private TextView mTextPhoneLand;
	
	private PayActivity activity;
	private boolean flag = false;
	
	@Override
	public void onClick(View v) {
		if (v.getId() == ResUtils.getResById(mActivity, "btn_wxscan_check_order", "id")){
			activity.mPayHandler.doQueryOrder();
		} else if (v.getId() == ResUtils.getResById(mActivity, "btn_wxscan_pay_onplat", "id")) {
			String url = URLHolder.URL_PAY_ONPLAT+"username="+ConfigHolder.userName+
					"&appID="+ConfigHolder.gameId+"&sid="+activity.mPayHandler.mPayInfo.getServerId()+
					"&usertoken="+ConfigHolder.userToken+"&type=sdk";
			Log.d("tianyouxi", "pay platform url= "+url);
			Intent intent = new Intent(mActivity,MenuActivity.class);
			intent.putExtra("menu_type", MenuActivity.POPUP_MENU_7);
			intent.putExtra("url", url);
			startActivity(intent);
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_wxscan_service_phone", "id")) {
			AppUtils.callServerPhone(mActivity);
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_wxscan_service_qq", "id")) {
			AppUtils.getQQTalk(mActivity);
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		flag = true;
	}

	@Override
	protected String setContentView() {
		if (ConfigHolder.isLandscape){
			return "fragment_pay_wxscan_land";
		} else {
			return "fragment_pay_wxscan";
		}
	}

	@Override
	protected void initView() {
		mImageView = (ImageView) mContentView.findViewById(ResUtils.getResById(mActivity, "image_wxscan_paycode", "id"));
		mContentView.findViewById(ResUtils.getResById(mActivity, "btn_wxscan_check_order", "id")).setOnClickListener(this);
		if (ConfigHolder.isLandscape){
			mTextMoneyLand = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_wxscan_money", "id"));
			mTextOrderLand = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_wxscan_order", "id"));
			mTextPhoneLand = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_wxscan_service_phone", "id"));
			mTextQQLand = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_wxscan_service_qq", "id"));
			mTextServerLand = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_wxscan_server", "id"));
			mContentView.findViewById(ResUtils.getResById(mActivity, "btn_wxscan_pay_onplat", "id")).setOnClickListener(this);
			
			mTextPhoneLand.setOnClickListener(this);
			mTextQQLand.setOnClickListener(this);
		} else {
			mTextMoney = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_wxscan_money", "id"));
			mTextServiceQQ = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_wxscan_service_qq", "id"));
		}
	}

	@Override
	protected void initData() {
		activity = (PayActivity) getActivity();
		PayInfo payInfo = activity.mPayHandler.mPayInfo;
		
		if (flag) {
			Log.d("TAG", "url= "+activity.mPayHandler.mPayInfo.getImgstr());
			HttpUtils.imageDown(mActivity, activity.mPayHandler.mPayInfo.getImgstr(), mImageView, "天游戏支付二维码"+AppUtils.getFormateTime());
		} else {
			HttpUtils.imageLoad(mActivity, activity.mPayHandler.mPayInfo.getImgstr(), mImageView);
		}
		
		if (ConfigHolder.isLandscape){
			mTextOrderLand.setText(ResUtils.getString(mActivity, "ty_pay_order2")+payInfo.getOrderId());
			mTextServerLand.setText(ResUtils.getString(mActivity, "ty_pay_server2")+payInfo.getServerName());
			mTextMoneyLand.setText(ResUtils.getString(mActivity, "ty_pay_money2")+Integer.parseInt(payInfo.getMoney()) * payInfo.getScale()+payInfo.getCurrency());
			
			mTextPhoneLand.setText(SPHandler.getString(mActivity, SPHandler.SP_TEXT_PHONE));
			mTextQQLand.setText(SPHandler.getString(mActivity, SPHandler.SP_TEXT_QQ));
			
		} else {
			mTextMoney.setText("创建订单成功,需支付金额: "+activity.mPayHandler.mPayInfo.getMoney()+"元");
			mTextServiceQQ.setText(SPHandler.getString(mActivity, SPHandler.SP_TEXT_QQ));
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		flag = false;
	}
}
