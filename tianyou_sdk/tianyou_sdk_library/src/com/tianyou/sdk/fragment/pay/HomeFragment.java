package com.tianyou.sdk.fragment.pay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.tianyou.sdk.activity.PayActivity;
import com.tianyou.sdk.base.BaseFragment;
import com.tianyou.sdk.bean.PayParamInfo;
import com.tianyou.sdk.bean.PayWayControl;
import com.tianyou.sdk.bean.PayWayControl.ResultBean;
import com.tianyou.sdk.bean.PayWayControl.ResultBean.CustominfoBean;
import com.tianyou.sdk.holder.ConfigHolder;
import com.tianyou.sdk.holder.PayHandler;
import com.tianyou.sdk.holder.PayHandler.PayType;
import com.tianyou.sdk.holder.SPHandler;
import com.tianyou.sdk.holder.URLHolder;
import com.tianyou.sdk.utils.HttpUtils;
import com.tianyou.sdk.utils.HttpUtils.HttpCallback;
import com.tianyou.sdk.utils.LogUtils;
import com.tianyou.sdk.utils.ResUtils;
import com.tianyou.sdk.utils.ToastUtils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

/**
 * 充值首页
 * Created by itstrong on 2016/7/1.
 */
public class HomeFragment extends BaseFragment {

	private TextView mTextAccount;
	private TextView mTextServer;
	private TextView mTextScale;
	private TextView mTextMoney;
	private EditText mEditOther;
	private PopupWindow mPopupWindow;
	private TextView mTextWalletMoney;
	private View mLayoutWallet;
	private View mLayoutMenu0;
	private View mLayoutMenu1;
	private TextView mLayoutMenu2;
	private View mLayoutMenu3;
	private TextView mTextPayMoney;
	private View mPayWay0;
	private View mPayWay1;
	private View mPayWay2;
	private View mPayWay3;
	private View mPayWay4;
	private View mPayWay5;
	private View mPayWay6;
	private View mPayWay7;
	private View mPayWay8;
	
	private List<TextView> mPayWayList;		//支付方式集合
	private List<TextView> mPayMoneyList;	//支付金额集合
	private List<ImageView> mPayHeadList;	//支付金额集合
	private List<Integer> mMoneyList;   	//供选择充值的金额集合
	private int mMoneyIndex; 				//当前选中的金额
	private PayHandler mPayHandler;
	private PayParamInfo mPaymentInfo;
	
	@Override
	protected String setContentView() {
		return ConfigHolder.isLandscape ? "fragment_pay_home_land" : "fragment_pay_home_port";
	}
	
	@Override
	protected void initView() {
		mTextAccount = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_pay_account", "id"));
		mTextServer = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_pay_server", "id"));
		mTextScale = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_pay_scale", "id"));
		mTextMoney = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_pay_money", "id"));
		mEditOther = (EditText)mContentView.findViewById(ResUtils.getResById(mActivity, "edit_choose_other", "id"));
		mLayoutWallet = mContentView.findViewById(ResUtils.getResById(mActivity, "layout_home_wallet", "id"));
		mTextWalletMoney = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_pay_wallet", "id"));
		mEditOther.addTextChangedListener(mTextWatcher);
		mEditOther.setOnClickListener(this);
		mEditOther.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(hasFocus) mEditOther.setText("");
				setPayMoneyState(-1);
			}
		});
		if (ConfigHolder.isLandscape) {
			mContentView.findViewById(ResUtils.getResById(mActivity, "text_home_dopay", "id")).setOnClickListener(this);
			initViewData(mContentView);
		} else {
			mContentView.findViewById(ResUtils.getResById(mActivity, "text_home_switch", "id")).setOnClickListener(this);
			mContentView.findViewById(ResUtils.getResById(mActivity, "layout_pay_way_0", "id")).setOnClickListener(this);
			mContentView.findViewById(ResUtils.getResById(mActivity, "layout_pay_way_1", "id")).setOnClickListener(this);
			mContentView.findViewById(ResUtils.getResById(mActivity, "layout_pay_way_2", "id")).setOnClickListener(this);
		}
		
		mEditOther.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent ev) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					InputMethodManager im = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
					if (im.isActive()) {  
						im.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);  
                    }  
					return true;
				}
				return false;
			}
		});
		
		mPayMoneyList = new ArrayList<TextView>();
		mPayMoneyList.add((TextView)mContentView.findViewById(ResUtils.getResById(mActivity, "text_home_money_0", "id")));
		mPayMoneyList.add((TextView)mContentView.findViewById(ResUtils.getResById(mActivity, "text_home_money_1", "id")));
		mPayMoneyList.add((TextView)mContentView.findViewById(ResUtils.getResById(mActivity, "text_home_money_2", "id")));
		mPayMoneyList.add((TextView)mContentView.findViewById(ResUtils.getResById(mActivity, "text_home_money_3", "id")));
		mPayMoneyList.add((TextView)mContentView.findViewById(ResUtils.getResById(mActivity, "text_home_money_4", "id")));
		mPayMoneyList.add((TextView)mContentView.findViewById(ResUtils.getResById(mActivity, "text_home_money_5", "id")));
		mPayMoneyList.add((TextView)mContentView.findViewById(ResUtils.getResById(mActivity, "text_home_money_6", "id")));
		for (TextView textView: mPayMoneyList) textView.setOnClickListener(this);
		mLayoutMenu0 = mContentView.findViewById(ResUtils.getResById(mActivity, "layout_pay_menu_0", "id"));
		mLayoutMenu1 = mContentView.findViewById(ResUtils.getResById(mActivity, "layout_pay_menu_1", "id"));
		mLayoutMenu2 = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "layout_pay_menu_2", "id"));
		mLayoutMenu3 = mContentView.findViewById(ResUtils.getResById(mActivity, "layout_pay_menu_3", "id"));
		mTextPayMoney = (TextView) mContentView.findViewById(ResUtils.getResById(mActivity, "text_pay_home_money", "id"));
	}
	
	@Override
	protected void initData() {
		PayActivity activity = (PayActivity) getActivity();
		mPayHandler = activity.mPayHandler;
		mPaymentInfo = mPayHandler.mPayInfo;
		mPayHandler.PAY_FLAG = false;
		LogUtils.d("mPayHandler.mIsShowChoose:" + mPayHandler.mIsShowChoose);
		mLayoutMenu0.setVisibility(mPayHandler.mIsShowChoose ? View.VISIBLE : View.GONE);
		mLayoutMenu1.setVisibility(!mPayHandler.mIsShowChoose ? View.VISIBLE : View.GONE);
		mLayoutMenu2.setVisibility(!mPayHandler.mIsShowChoose ? View.VISIBLE : View.GONE);
		mLayoutMenu3.setVisibility(mPayHandler.mIsShowChoose ? View.VISIBLE : View.GONE);
		mLayoutMenu2.setText(mPaymentInfo.getProductDesc());
		mTextPayMoney.setText(mPaymentInfo.getMoney() + ResUtils.getString(mActivity,"ty_currency"));
		mMoneyList = new ArrayList<Integer>();
		mTextAccount.setText(getResources().getString(ResUtils.getResById(mActivity, "ty_account", "string")) + ConfigHolder.userName);
		mTextServer.setText(getResources().getString(ResUtils.getResById(mActivity, "ty_server", "string")) + mPaymentInfo.getServerName());
		setPayWayState(mPayHandler.mPayType);
		getPayMoneyValue();
		getWalletMoney();
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == ResUtils.getResById(mActivity, "text_home_dopay", "id")) {
			startPayment();
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_home_way_0", "id")) {
			doPortPay(PayType.WECHAT);
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_home_way_1", "id")) {
			doPortPay(PayType.ALIPAY);
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_home_way_2", "id")) {
			doPortPay(PayType.QQPAY);
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_home_way_3", "id")) {
			doPortPay(PayType.UNION);
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_home_way_4", "id")) {
			doPortPay(PayType.REMIT);
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_home_way_5", "id")) {
			doPortPay(PayType.WALLET);
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_home_way_6", "id")) {
			doPortPay(PayType.WXSCAN);
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_home_way_7", "id")) {
			doPortPay(PayType.GOOGLE);
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_home_way_8", "id")) {
			doPortPay(PayType.PAYPAL);
		} else if (v.getId() == ResUtils.getResById(mActivity, "edit_choose_other", "id")) {
			mEditOther.setText("");
			setPayMoneyState(-1);
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_home_money_0", "id")) {
			setPayMoneyState(0);
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_home_money_1", "id")) {
			setPayMoneyState(1);
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_home_money_2", "id")) {
			setPayMoneyState(2);
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_home_money_3", "id")) {
			setPayMoneyState(3);
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_home_money_4", "id")) {
			setPayMoneyState(4);
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_home_money_5", "id")) {
			setPayMoneyState(5);
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_home_money_6", "id")) {
			setPayMoneyState(6);
		} else if (v.getId() == ResUtils.getResById(mActivity, "text_home_switch", "id")) {
			showPayWayPupup();
		} else if (v.getId() == ResUtils.getResById(mActivity, "img_popup_close", "id")) {
			mPopupWindow.dismiss();
		} else if (v.getId() == ResUtils.getResById(mActivity, "layout_pay_way_0", "id")) {
			doPortPay(PayType.WECHAT);
		} else if (v.getId() == ResUtils.getResById(mActivity, "layout_pay_way_1", "id")) {
			doPortPay(PayType.ALIPAY);
		} else if (v.getId() == ResUtils.getResById(mActivity, "layout_pay_way_2", "id")) {
			doPortPay(PayType.QQPAY);
		}
	}
	
	private TextWatcher mTextWatcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }
		
		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }
		
		@Override
		public void afterTextChanged(Editable text) {
			String value = text.toString();
			if (value.matches("\\d+") && !value.isEmpty()) {
				LogUtils.d("2" + mPaymentInfo.getMoney());
				mTextMoney.setText("( " + ResUtils.getString(mActivity, "ty_get") + mPayHandler.getCurrencyValue(value) + ")");
				mPaymentInfo.setPayMoney(Integer.parseInt(value) * mPayHandler.mPayInfo.getScale() + mPayHandler.mPayInfo.getCurrency());
			} else {
				mTextMoney.setText("(" + ResUtils.getString(mActivity, "ty_get") + "0" + mPayHandler.mPayInfo.getCurrency() + ")");
			}
		}
	};

	private void doPortPay(PayType type) {
		setPayWayState(type);
		if (ConfigHolder.isLandscape && mPayHandler.mPayType != PayType.REMIT) return;
		if (mPopupWindow != null) mPopupWindow.dismiss();
		mPayHandler.mPayType = type;
		startPayment();
	}
	
	// 获取钱包余额
	private void getWalletMoney() {
		Map<String,String> map = new HashMap<String, String>();
    	map.put("appID", ConfigHolder.gameId);
		map.put("usertoken", ConfigHolder.userToken);
		map.put("userid", ConfigHolder.userId);
    	HttpUtils.post(mActivity, URLHolder.URL_PAY_WALLET_REMAIN, map, new HttpCallback() {
			@Override
			public void onSuccess(String response) {
				try {
					JSONObject jsonObject = new JSONObject(response);
					JSONObject result = jsonObject.getJSONObject("result");
					if (result.getInt("code") == 200) {
						mTextWalletMoney.setText(result.getString("money") + "天游币");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFailed() {
				mActivity.switchFragment(new NetworkFragment(), "NoNetworkFragment");
			}
		});
	}
	
	private void showPayWayPupup() {
		if (mPopupWindow == null) {
			mPopupWindow = new PopupWindow();
			View popupView = mActivity.getLayoutInflater().inflate(ResUtils.getResById(mActivity, "pupup_pay_way", "layout"), null);
			mPopupWindow.setContentView(popupView);
			mPopupWindow.setWidth(LayoutParams.MATCH_PARENT);
			mPopupWindow.setHeight(LayoutParams.WRAP_CONTENT);
			mPopupWindow.setFocusable(true);
			mPopupWindow.setAnimationStyle(ResUtils.getResById(mActivity, "style_popup_pay", "style"));
			mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x09000000));
			WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();  
	        lp.alpha = 0.5f;  
	        mActivity.getWindow().setAttributes(lp);  
	        mPopupWindow.setOnDismissListener(new OnDismissListener() {  
	            @Override  
	            public void onDismiss() {  
	                WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();  
	                lp.alpha = 1f;  
	                mActivity.getWindow().setAttributes(lp);  
	            }  
	        });
	        initViewData(popupView);
	        popupView.findViewById(ResUtils.getResById(mActivity, "img_popup_close", "id")).setOnClickListener(this);
		}
		mPopupWindow.showAtLocation(mTextMoney, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
	}

	private void initViewData(View popupView) {
		mPayWayList = new ArrayList<TextView>();
        mPayWayList.add((TextView) popupView.findViewById(ResUtils.getResById(mActivity, "text_home_way_0", "id")));
        mPayWayList.add((TextView) popupView.findViewById(ResUtils.getResById(mActivity, "text_home_way_1", "id")));
        mPayWayList.add((TextView) popupView.findViewById(ResUtils.getResById(mActivity, "text_home_way_2", "id")));
        mPayWayList.add((TextView) popupView.findViewById(ResUtils.getResById(mActivity, "text_home_way_3", "id")));
        mPayWayList.add((TextView) popupView.findViewById(ResUtils.getResById(mActivity, "text_home_way_4", "id")));
        mPayWayList.add((TextView) popupView.findViewById(ResUtils.getResById(mActivity, "text_home_way_5", "id")));
        mPayWayList.add((TextView) popupView.findViewById(ResUtils.getResById(mActivity, "text_home_way_6", "id")));
        mPayWayList.add((TextView) popupView.findViewById(ResUtils.getResById(mActivity, "text_home_way_7", "id")));
        mPayWayList.add((TextView) popupView.findViewById(ResUtils.getResById(mActivity, "text_home_way_8", "id")));
        for (View view : mPayWayList) view.setOnClickListener(this);
        mPayHeadList = new ArrayList<ImageView>();
		mPayHeadList.add((ImageView) popupView.findViewById(ResUtils.getResById(mActivity, "img_home_head_0", "id")));
		mPayHeadList.add((ImageView) popupView.findViewById(ResUtils.getResById(mActivity, "img_home_head_1", "id")));
		mPayHeadList.add((ImageView) popupView.findViewById(ResUtils.getResById(mActivity, "img_home_head_2", "id")));
		mPayHeadList.add((ImageView) popupView.findViewById(ResUtils.getResById(mActivity, "img_home_head_3", "id")));
		mPayHeadList.add((ImageView) popupView.findViewById(ResUtils.getResById(mActivity, "img_home_head_4", "id")));
		mPayHeadList.add((ImageView) popupView.findViewById(ResUtils.getResById(mActivity, "img_home_head_5", "id")));
		mPayHeadList.add((ImageView) popupView.findViewById(ResUtils.getResById(mActivity, "img_home_head_6", "id")));
		mPayHeadList.add((ImageView) popupView.findViewById(ResUtils.getResById(mActivity, "img_home_head_7", "id")));
		mPayHeadList.add((ImageView) popupView.findViewById(ResUtils.getResById(mActivity, "img_home_head_8", "id")));
		mPayWay0 = popupView.findViewById(ResUtils.getResById(mActivity, "layout_pay_way_0", "id"));
		mPayWay1 = popupView.findViewById(ResUtils.getResById(mActivity, "layout_pay_way_1", "id"));
		mPayWay2 = popupView.findViewById(ResUtils.getResById(mActivity, "layout_pay_way_2", "id"));
		mPayWay3 = popupView.findViewById(ResUtils.getResById(mActivity, "layout_pay_way_3", "id"));
		mPayWay4 = popupView.findViewById(ResUtils.getResById(mActivity, "layout_pay_way_4", "id"));
		mPayWay5 = popupView.findViewById(ResUtils.getResById(mActivity, "layout_pay_way_5", "id"));
		mPayWay6 = popupView.findViewById(ResUtils.getResById(mActivity, "layout_pay_way_6", "id"));
		mPayWay7 = popupView.findViewById(ResUtils.getResById(mActivity, "layout_pay_way_7", "id"));
		mPayWay8 = popupView.findViewById(ResUtils.getResById(mActivity, "layout_pay_way_8", "id"));
		getPayWayControl();
	}
	
	private void getPayWayControl() {
		
		PayWayControl control = new Gson().fromJson(SPHandler.getString(mActivity, SPHandler.SP_PAY_WAY), PayWayControl.class);
		ResultBean result = control.getResult();
		if (result.getCode() == 200) {
			CustominfoBean customInfo = result.getCustominfo();
			mPayWay0.setVisibility(customInfo.getWx_pay() == 1 ? View.VISIBLE : View.GONE);
			mPayWay1.setVisibility(customInfo.getZfb_pay() == 1 ? View.VISIBLE : View.GONE);
			mPayWay2.setVisibility(customInfo.getQq_pay() == 1 ? View.VISIBLE : View.GONE);
			mPayWay3.setVisibility(customInfo.getWy_pay() == 1 ? View.VISIBLE : View.GONE);
			mPayWay4.setVisibility(customInfo.getHk_pay() == 1 ? View.VISIBLE : View.GONE);
			mPayWay5.setVisibility(customInfo.getQb_pay() == 1 ? View.VISIBLE : View.GONE);
			mPayWay6.setVisibility(customInfo.getScan_pay() == 1 ? View.VISIBLE : View.GONE);
			mPayWay7.setVisibility(customInfo.getGoogle_pay() == 1 ? View.VISIBLE : View.GONE);
			mPayWay8.setVisibility(customInfo.getPaypal_pay() == 1 ? View.VISIBLE : View.GONE);
		}
	}

	// 设置支付方式状态
	private void setPayWayState(PayType type) {
		mLayoutWallet.setVisibility(type == PayType.WALLET ? View.VISIBLE : View.GONE);
		mPayHandler.mPayType = type;
		if (mPayWayList != null) {
			for (TextView view : mPayWayList) {
				view.setBackgroundColor(Color.parseColor("#FFFFFF"));
				view.setTextColor(Color.parseColor("#666666"));
			}
			for (ImageView view : mPayHeadList) {
				view.setImageResource(ResUtils.getResById(mActivity, "ty_pay_right", "drawable"));
			}
			mPayHeadList.get(mPayHandler.mPayType.ordinal()).setImageResource(ResUtils.getResById(mActivity, "ty_pay_right_click", "drawable"));
			mPayWayList.get(mPayHandler.mPayType.ordinal()).setBackgroundColor(Color.parseColor("#FE623F"));
			mPayWayList.get(mPayHandler.mPayType.ordinal()).setTextColor(Color.WHITE);
		}
	}
	
	// 设置支付金额状态
	private void setPayMoneyState(int index) {
		mMoneyIndex = index;
		for (TextView view : mPayMoneyList) {
			view.setTextColor(Color.parseColor("#333333"));
			view.setBackgroundResource(ResUtils.getResById(mActivity, "shape_bg_gray_2", "drawable"));
		}
		if (mMoneyIndex == -1) {
			mEditOther.setBackgroundResource(ResUtils.getResById(mActivity, "shape_bg_jacinth", "drawable"));
			mTextMoney.setText("(" + ResUtils.getString(mActivity, "ty_get") + "0" + mPayHandler.mPayInfo.getCurrency() + ")");
		} else {
			mPaymentInfo.setPayMoney(mMoneyList.get(mMoneyIndex) * mPayHandler.mPayInfo.getScale() + mPayHandler.mPayInfo.getCurrency());
			mEditOther.setBackgroundResource(ResUtils.getResById(mActivity, "shape_bg_gray_2", "drawable"));
			mEditOther.setText("");
			mPayMoneyList.get(mMoneyIndex).setTextColor(Color.parseColor("#FFFFFF"));
			mPayMoneyList.get(mMoneyIndex).setBackgroundColor(Color.parseColor("#FE623F"));
			mTextMoney.setText("(" + ResUtils.getString(mActivity, "ty_get") + mPayHandler.getCurrencyValue(mMoneyList.get(mMoneyIndex)) + ")");
			LogUtils.d("tyget= "+ResUtils.getResById(mActivity, "ty_get", "string"));
			LogUtils.d("getCurrencyValue= "+mPayHandler.getCurrencyValue(mMoneyList.get(mMoneyIndex)));
			LogUtils.d("TextMoney= "+mTextMoney.getText().toString());
		}
	}

	// 立即支付
	private void startPayment() {
		String other = mEditOther.getText().toString();
		if (mMoneyIndex == -1 && other.isEmpty()) {
			ToastUtils.show(mActivity, "充值金额不能为0");
			return;
		}
		if (mPayHandler.mIsShowChoose) {
			mPaymentInfo.setMoney(mMoneyIndex == -1 ? other : mMoneyList.get(mMoneyIndex) + "");
			LogUtils.d("1" + mPaymentInfo.getMoney());
		}
		mPaymentInfo.setPayMoney((mMoneyIndex == -1 ? Integer.parseInt(other) : mMoneyList.get(mMoneyIndex))
				* mPayHandler.mPayInfo.getScale() + mPayHandler.mPayInfo.getCurrency());
		if (mPayHandler.mPayType == PayType.WECHAT && Integer.parseInt(mPaymentInfo.getMoney()) > 50000) {
			ToastUtils.show(mActivity, "充值金额不能大于50000");
		} else if (mPayHandler.mPayType == PayType.QQPAY && Integer.parseInt(mPaymentInfo.getMoney()) > 3000) {
			ToastUtils.show(mActivity, "充值金额不能大于3000");
		} else if (mMoneyIndex == -1 && Integer.parseInt(mPaymentInfo.getMoney()) < 10) {
			ToastUtils.show(mActivity, "充值金额不能低于10元");
		} else {
			if (!mPayHandler.PAY_FLAG) {
				mPayHandler.PAY_FLAG = true;
				mPayHandler.createOrder();
			}
		}
	}
	
	// 充值金额数值
    private void getPayMoneyValue() {
    	Map<String, String> map = new HashMap<String, String>();
    	map.put("appID", ConfigHolder.gameId);
		map.put("usertoken", ConfigHolder.gameToken);
        HttpUtils.post(mActivity, URLHolder.URL_MONEY_VALUE, map, new HttpCallback() {
			@Override
			public void onSuccess(String response) {
				try {
                    JSONObject root = new JSONObject(response);
                    JSONObject result = root.getJSONObject("result");
                    int code = result.getInt("code");
                    if (code == 200) {
                        JSONArray list = result.getJSONArray("list");
                        for (int i = 0; i < list.length(); i++)
                        	mMoneyList.add(list.getInt(i));
                        if (mPayHandler.mIsShowChoose) {
                        	mPaymentInfo.setMoney(mMoneyList.get(0) + "");
                        	LogUtils.d("2" + mPaymentInfo.getMoney());
                        }
                        mPayHandler.mPayInfo.setScale(result.getInt("exchange"));
                        mPayHandler.mPayInfo.setCurrency(result.getString("currency"));
                        for (int i = 0; i < mPayMoneyList.size(); i++) {
                        	mPayMoneyList.get(i).setText(mMoneyList.get(i) + 
                        			getResources().getString(ResUtils.getResById(mActivity, "ty_money_sign", "string")));
                        }
                        mTextScale.setText(mActivity.getString(ResUtils.getResById(mActivity, "ty_scale", "string")) + "1:" + mPayHandler.mPayInfo.getScale());
                        setPayMoneyState(mMoneyIndex);
                        String payMoney = mPayHandler.mPayInfo.getPayMoney();
                        if (!payMoney.isEmpty()) {
                        	LogUtils.d("payMoney:" + payMoney);
                        	mTextMoney.setText("(" + mActivity.getString(ResUtils.getResById(mActivity, "ty_get", "string")) + payMoney + ")");
						} else {
							mTextMoney.setText("(" + mActivity.getString(ResUtils.getResById(mActivity, "ty_get", "string")) + mPayHandler.getCurrencyValue(mMoneyList.get(0)) + ")");
						}
                    } else {
                        LogUtils.e("创建订单失败！");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
			}
			
			@Override
			public void onFailed() {
				mActivity.switchFragment(new FailedFragment(), "PayfailedFragment");
			}
		});
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    }
}
