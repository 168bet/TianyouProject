package com.tianyou.sdk.fragment.pay;

import com.tianyou.sdk.activity.MenuActivity;
import com.tianyou.sdk.activity.PayActivity;
import com.tianyou.sdk.base.BaseFragment;
import com.tianyou.sdk.bean.PayInfo;
import com.tianyou.sdk.holder.ConfigHolder;
import com.tianyou.sdk.holder.SPHandler;
import com.tianyou.sdk.holder.URLHolder;
import com.tianyou.sdk.interfaces.TianyouxiCallback;
import com.tianyou.sdk.interfaces.TianyouxiSdk;
import com.tianyou.sdk.utils.AppUtils;
import com.tianyou.sdk.utils.ResUtils;

import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

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
	
	public FailedFragment(){
		TianyouxiSdk.getInstance().mTianyouCallback.onResult(TianyouxiCallback.CODE_PAY_FAILED, "");
	}

	@Override
	protected void initView() {
		mTextOrder = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_failed_order", "id"));
		mTextServer = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_failed_server", "id"));
		mTextMoney = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_failed_money", "id"));
		if (ConfigHolder.isOverseas) {
			mContentView.findViewById(ResUtils.getResById(mActivity, "tv_wish_payfaile", "id")).setVisibility(View.GONE);
			mContentView.findViewById(ResUtils.getResById(mActivity, "lv_servcieqq_payfaile", "id")).setVisibility(View.GONE);
			mContentView.findViewById(ResUtils.getResById(mActivity, "lv_servicephone_payfaile", "id")).setVisibility(View.GONE);
			mContentView.findViewById(ResUtils.getResById(mActivity, "lv_servicewechat_payfaile", "id")).setVisibility(View.GONE);
			mContentView.findViewById(ResUtils.getResById(mActivity, "rl_payplat_payfaile", "id")).setVisibility(View.GONE);
		} else {
			mTextQQ = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_failed_service_qq", "id"));
			mTextPhone = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_failed_service_phone", "id"));
			
			mContentView.findViewById(ResUtils.getResById(mActivity, "btn_failed_backgame", "id")).setOnClickListener(this);
			mContentView.findViewById(ResUtils.getResById(mActivity, "btn_failed_pay_onplat", "id")).setOnClickListener(this);
			
			mTextQQ.setOnClickListener(this);
			mTextPhone.setOnClickListener(this);
		}
		
	}

	@Override
	protected void initData() {
		activity = (PayActivity) getActivity();
		PayInfo payInfo = activity.mPayHandler.mPayInfo;
		if (activity.mPayHandler.mPayInfo.getOrderId().isEmpty()){
			mTextOrder.setVisibility(View.GONE);
		} else {
			mTextOrder.setText(ResUtils.getString(mActivity, "ty_pay_order2") + payInfo.getOrderId());
		}
		mTextServer.setText(ResUtils.getString(mActivity, "ty_pay_server2")+payInfo.getServerName());
		mTextMoney.setText(ResUtils.getString(mActivity, "ty_pay_money2")+Integer.parseInt(payInfo.getMoney()) * payInfo.getScale()+payInfo.getCurrency());
		if (!ConfigHolder.isOverseas) {
			mTextQQ.setText(SPHandler.getString(mActivity, SPHandler.SP_TEXT_QQ));
			mTextPhone.setText(SPHandler.getString(mActivity,SPHandler.SP_TEXT_PHONE));
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == ResUtils.getResById(mActivity, "btn_failed_backgame", "id")) {
			mActivity.finish();
		} else if (v.getId() == ResUtils.getResById(mActivity, "btn_failed_pay_onplat", "id")) {
			String url = URLHolder.URL_PAY_ONPLAT+"username="+ConfigHolder.userName+
					"&appID="+ConfigHolder.gameId+"&sid="+activity.mPayHandler.mPayInfo.getServerId()+
					"&usertoken="+ConfigHolder.userToken+"&type=sdk";
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
        	if (!ConfigHolder.isOverseas) {
        		AppUtils.showFinishPayDialog(activity,false);
        	}
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