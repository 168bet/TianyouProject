package com.tianyou.sdk.fragment.pay;

import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.tianyou.sdk.activity.MenuActivity;
import com.tianyou.sdk.activity.PayActivity;
import com.tianyou.sdk.base.BaseFragment;
import com.tianyou.sdk.bean.PayParamInfo;
import com.tianyou.sdk.holder.ConfigHolder;
import com.tianyou.sdk.holder.SPHandler;
import com.tianyou.sdk.holder.URLHolder;
import com.tianyou.sdk.interfaces.Tianyouxi;
import com.tianyou.sdk.utils.AppUtils;
import com.tianyou.sdk.utils.ResUtils;

/**
 * 支付失败界面
 * Created by itstrong on 2016/7/1.
 */
public class FailedFragment extends BaseFragment {

	
	private TextView mTextOrder;
	private TextView mTextServer;
	private TextView mTextMoney;
	private TextView mTextQQ;
	private TextView mTextPhone;
	
	private static PayActivity activity;
	private static int ACTIVITY_FINISH = 1;
    
	@Override
	protected String setContentView() {
		if (ConfigHolder.isLandscape){
			return "fragment_pay_faliure_land";
		} else {
			return "fragment_pay_faliure";
		}
	}

	@Override
	protected void initView() {
		mTextOrder = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_failed_order", "id"));
		mTextServer = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_failed_server", "id"));
		mTextMoney = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_failed_money", "id"));
		mTextQQ = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_failed_service_qq", "id"));
		mTextPhone = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_failed_service_phone", "id"));
		
		mContentView.findViewById(ResUtils.getResById(mActivity, "btn_failed_backgame", "id")).setOnClickListener(this);
		mContentView.findViewById(ResUtils.getResById(mActivity, "btn_failed_pay_onplat", "id")).setOnClickListener(this);
		
		mTextQQ.setOnClickListener(this);
		mTextPhone.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		Tianyouxi.mPayCallback.onFailed("支付失败");
		activity = (PayActivity) getActivity();
		PayParamInfo payInfo = activity.mPayHandler.mPayInfo;
		if (activity.mPayHandler.mPayInfo.getOrderId().isEmpty()){
			mTextOrder.setVisibility(View.GONE);
		} else {
			mTextOrder.setText("充值订单：" + payInfo.getOrderId());
		}
		mTextServer.setText("充值区服："+payInfo.getServerName());
		mTextMoney.setText("充值金额："+Integer.parseInt(payInfo.getMoney()) * payInfo.getScale()+payInfo.getCurrency());
		mTextQQ.setText(SPHandler.getString(mActivity, SPHandler.SP_TEXT_QQ));
		mTextPhone.setText(SPHandler.getString(mActivity,SPHandler.SP_TEXT_PHONE));
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == ResUtils.getResById(mActivity, "btn_failed_backgame", "id")) {
			mActivity.finish();
		} else if (v.getId() == ResUtils.getResById(mActivity, "btn_failed_pay_onplat", "id")) {
			String url = URLHolder.URL_PAY_ONPLAT+"username="+ConfigHolder.USER_ACCOUNT+
					"&appID="+ConfigHolder.GAME_ID+"&sid="+activity.mPayHandler.mPayInfo.getServerId()+
					"&usertoken="+ConfigHolder.USER_TOKEN+"&type=sdk";
			Log.d("tianyouxi", "pay platform url= "+url);
			Intent intent = new Intent(mActivity,MenuActivity.class);
			intent.putExtra("menu_type", MenuActivity.POPUP_MENU_7);
			intent.putExtra("url", url);
			startActivityForResult(intent, ACTIVITY_FINISH);
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_failed_service_qq", "id")) {
			AppUtils.getQQTalk(mActivity);
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_failed_service_phone", "id")) {
			AppUtils.callServerPhone(mActivity);
		}
	}
	
	public static boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        	AppUtils.showFinishPayDialog(activity,false);
        }
        return true;
    }
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null) return;
		if (resultCode == MenuActivity.REQUEST_OK && requestCode == ACTIVITY_FINISH) {
			activity.finish();
		} 
		super.onActivityResult(requestCode, resultCode, data);
	}
}