package com.tianyou.sdk.holder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Handler;

import com.google.gson.Gson;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.tianyou.sdk.activity.PayActivity;
import com.tianyou.sdk.bean.CreateOrder;
import com.tianyou.sdk.bean.CreateOrder.ResultBean;
import com.tianyou.sdk.bean.CreateOrder.ResultBean.OrderinfoBean;
import com.tianyou.sdk.bean.PayInfo;
import com.tianyou.sdk.utils.AppUtils;
import com.tianyou.sdk.utils.AppUtils.DialogCallback;
import com.tianyou.sdk.utils.HttpUtils;
import com.tianyou.sdk.utils.HttpUtils.HttpCallback;
import com.tianyou.sdk.utils.HttpUtils.HttpsCallback;
import com.tianyou.sdk.utils.LogUtils;
import com.tianyou.sdk.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付逻辑处理
 * @author itstrong
 *
 */
public class PayHandler {

	public enum PayType {
		WECHAT, ALIPAY, QQPAY, UNION, REMIT, WALLET, WXSCAN, GOOGLE, PAYPAL;
	}
    
    private static PayHandler mPaymentHandler;
    private static Activity mActivity;
    private static Handler mHandler;
    
    public PayType mPayType;			//当前支付类型
    public PayInfo mPayInfo;			//支付参数集合
    public boolean PAY_FLAG;			//防止多次点击充值
    public boolean mIsShowChoose;		//是否有选择金额页面
    
    private PayHandler() {}
    
    public static PayHandler getInstance(Activity activity) {
		mActivity = activity;
		if (mPaymentHandler == null) {
			mPaymentHandler = new PayHandler();
		}
		return mPaymentHandler;
	}
    
    public static PayHandler getInstance(Activity activity, Handler handler) {
    	mHandler = handler;
		return getInstance(activity);
	}
    
    // 执行支付
    public void doPay(PayInfo payInfo, boolean isShowChooseMoney) {
    	mPayInfo = payInfo;
    	mIsShowChoose = isShowChooseMoney;
        if (checkoutPayInfo().equals("OK")) {
        	Intent intent = new Intent(mActivity, PayActivity.class);
    		mActivity.startActivity(intent);
        } else {
        	ToastUtils.show(mActivity, checkoutPayInfo() + (ConfigHolder.isOverseas?"connot be empty":"不能为空"));
		}
    }
    
	// 创建订单
    public void createOrder() {
        getPayWayName();
        Map<String, String> map = new HashMap<String, String>();
        map.put("userid", ConfigHolder.userId);
        map.put("serverid", mPayInfo.getServerId());
        map.put("servername", mPayInfo.getServerName());
        map.put("roleid", mPayInfo.getRoleId());
        map.put("productid", mPayInfo.getProductId());
        map.put("productname", mPayInfo.getProductName());
        map.put("money", mPayInfo.getMoney());
        map.put("way", mPayWayCode);
        map.put("custominfo", mPayInfo.getCustomInfo());
        map.put("sign", AppUtils.MD5(ConfigHolder.gameId + ConfigHolder.gameToken + ConfigHolder.userId + 
        		mPayInfo.getServerId() + mPayInfo.getRoleId() + mPayInfo.getMoney() + mPayInfo.getProductId()));
        HttpUtils.post(mActivity, URLHolder.URL_CREATE_ORDER, map, new HttpCallback() {
            @Override
            public void onSuccess(String response) {
                CreateOrder createOrder = new Gson().fromJson(response, CreateOrder.class);
                ResultBean result = createOrder.getResult();
                if (result.getCode() == 200) {
                    OrderinfoBean orderinfo = result.getOrderinfo();
                    mPayInfo.setOrderId(orderinfo.getOrderid());
                    mPayInfo.setProductName(orderinfo.getProductname());
                    mPayInfo.setPayMoney(orderinfo.getMoney());
                    if ("ALIPAY".equals(orderinfo.getWay())) {
                        mPayInfo.setSELLER(result.getPayinfo().getSELLER());
                        mPayInfo.setPARTNER(result.getPayinfo().getPARTNER());
                        mPayInfo.setRSA_PRIVATE(result.getPayinfo().getRSA_PRIVATE());
                        mPayInfo.setRSA_PUBLIC(result.getPayinfo().getRSA_PUBLIC());
                    } else if ("UNPAY".equals(orderinfo.getWay())) {
                        mPayInfo.setTnnumber(result.getPayinfo().getTnnumber());
                    } else if ("WXSCAN".equals(orderinfo.getWay())){
                        mPayInfo.setImgstr(result.getPayinfo().getImgstr());
                        mPayInfo.setQqmember(result.getPayinfo().getQqmember());
                    } else if ("GOOGLEPAY".equals(orderinfo.getWay())) {
                    	LogUtils.d("ggproductid= "+result.getPayinfo().getGGproduct_id());
                        mPayInfo.setGoogleProductID(result.getPayinfo().getGGproduct_id());
                    }
                    doStartPay();
                } else if (mPayType == PayType.WALLET) {
                    showWalletTip();
                } else {
                    ToastUtils.show(mActivity, result.getMsg());
                    mHandler.sendEmptyMessage(4);
                }
            }
            
            @Override
            public void onFailed() {
            	mHandler.sendEmptyMessage(5);
            }
        });
    }
    
    //余额不足
    private void showWalletTip() {
        AlertDialog.Builder builder = new Builder(mActivity);
        builder.setMessage(ConfigHolder.isOverseas?"The balance of the currency is not enough,whether top-up?":"天游币余额不足，是否给天游币充值?");  
        builder.setTitle(ConfigHolder.isOverseas?"Prompt":"提示");
        builder.setPositiveButton(ConfigHolder.isOverseas?"Confirm":"确认", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                mHandler.sendEmptyMessage(2);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(ConfigHolder.isOverseas?"Cancel":"取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                PAY_FLAG = false;
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    
    // 获取金额对应的货币值
    public String getCurrencyValue(String money) {
        return Integer.parseInt(money) * mPayInfo.getScale() + mPayInfo.getCurrency();
    }
    
    // 获取金额对应的货币值
    public String getCurrencyValue(int money) {
        return money * mPayInfo.getScale() + mPayInfo.getCurrency();
    }
    
    private String mPayWayCode = "WXPAY";
    public String mPayWayName = "微信支付";
    
    // 获取支付方式名称
    public void getPayWayName() {
        switch (mPayType) {
        case WECHAT:
        	mPayWayCode = "WXPAY";
        	mPayWayName = "微信支付";
            break;
        case ALIPAY:
        	mPayWayCode = "ALIPAY";
        	mPayWayName = "支付宝";
            break;
        case UNION:
        	mPayWayCode = "UNPAY";
        	mPayWayName = "银联支付";
            break;
        case REMIT:
        	mPayWayCode = "BANK_PAY";
        	mPayWayName = "汇款";
            break;
        case WALLET:
        	mPayWayCode = "QBPAY";
        	mPayWayName = "钱包支付";
            break;
        case QQPAY:
        	mPayWayCode = "HANDQ";
        	mPayWayName = "QQ钱包支付";
            break;
        case WXSCAN:
        	mPayWayCode = "WXSCAN";
        	mPayWayName = "微信扫码支付方式";
            break;
        case GOOGLE:
        	mPayWayCode = "GOOGLEPAY";
        	mPayWayName = "Google Payment";
        	break;
        case PAYPAL:
        	mPayWayCode = "PAYPALPAY";
        	mPayWayName = "Paypal Payment";
        	break;
        }
    }
    
    // 创建钱包订单
    public void createWalletOrder() {
    		getPayWayName();
        Map<String, String> map = new HashMap<String, String>();
        map.put("userid", ConfigHolder.userId);
        map.put("productname", mPayInfo.getProductName());
        map.put("money", mPayInfo.getMoney());
        map.put("way", mPayWayCode);
        map.put("custominfo", mPayInfo.getCustomInfo());
        map.put("sign", ConfigHolder.gameId + ConfigHolder.gameToken + ConfigHolder.userId + mPayInfo.getMoney());
        HttpUtils.post(mActivity, URLHolder.URL_PAY_WALLET, map, new HttpCallback() {
            @Override
            public void onSuccess(String response) {
                CreateOrder createOrder = new Gson().fromJson(response, CreateOrder.class);
                ResultBean result = createOrder.getResult();
                if (result.getCode() == 200) {
                    OrderinfoBean orderinfo = result.getOrderinfo();
                    mPayInfo.setOrderId(orderinfo.getOrderid());
                    mPayInfo.setProductName(orderinfo.getProductname());
                    mPayInfo.setPayMoney(orderinfo.getMoney());
                    if ("ALIPAY".equals(orderinfo.getWay())) {
                        mPayInfo.setSELLER(result.getPayinfo().getSELLER());
                        mPayInfo.setPARTNER(result.getPayinfo().getPARTNER());
                        mPayInfo.setRSA_PRIVATE(result.getPayinfo().getRSA_PRIVATE());
                        mPayInfo.setRSA_PUBLIC(result.getPayinfo().getRSA_PUBLIC());
                    } else if ("UNPAY".equals(orderinfo.getWay())) {
                        mPayInfo.setTnnumber(result.getPayinfo().getTnnumber());
                    } else if ("WXSCAN".equals(orderinfo.getWay())){
                        mPayInfo.setImgstr(result.getPayinfo().getImgstr());
                        mPayInfo.setQqmember(result.getPayinfo().getQqmember());
                    }
                    doStartPay();
                } else {
                    ToastUtils.show(mActivity, result.getMsg());
                    mHandler.sendEmptyMessage(4);
                }
            }
            
            @Override
            public void onFailed() {
            		mHandler.sendEmptyMessage(5);
            }
        });
    }
    
    // 开始支付
    private void doStartPay() {
    	LogUtils.d("mPayInfo:" + mPayInfo);
        switch (mPayType) {
            case WECHAT:
                new WechatPay(mActivity, true, mPayInfo,this).doWeChatPay();
                break;
            case QQPAY:
                new WechatPay(mActivity, false, mPayInfo,this).doWeChatPay();
                break;
            case ALIPAY:
                new Alipay(mActivity, mHandler, mPayInfo).doAlipay();
                break;
            case UNION:
                new UnionPay(mActivity, mPayInfo.getTnnumber()).doUnionPay();
                break;
            case REMIT:
            	mHandler.sendEmptyMessage(6);
                break;
            case WALLET:
            	mHandler.sendEmptyMessage(3);
                break;
            case WXSCAN:
            	mHandler.sendEmptyMessage(7);
                break;
            case GOOGLE:
            	mHandler.sendEmptyMessage(8);
                break;
            case PAYPAL:
            	PayPalPayment payment = new PayPalPayment(new BigDecimal(mPayInfo.getMoney()), "USD", mPayInfo.getProductName(),
                        PayPalPayment.PAYMENT_INTENT_SALE);
                Intent intent = new Intent(mActivity, PaymentActivity.class);
                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
                mPaymentHandler.PAY_FLAG = false;
                mActivity.startActivityForResult(intent, 0);
                break;
        }
    }
    
    // 查询订单
    public void doQueryOrder() {
        AppUtils.showProgressDialog(mActivity, "查询订单", "正在查询订单，请稍后...", new DialogCallback() {
            public void onDismiss() {
            	Map<String, String> checkParam = new HashMap<String, String>();
                checkParam.put("orderid", mPayInfo.getOrderId());
                checkParam.put("userid", ConfigHolder.userId);
                checkParam.put("sign", AppUtils.MD5(mPayInfo.getOrderId() + 
                		ConfigHolder.gameId + ConfigHolder.gameToken + ConfigHolder.userId));
                HttpUtils.post(mActivity, URLHolder.URL_QUERY_ORDER, checkParam, new HttpsCallback() {
                    @Override
                    public void onSuccess(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject result = jsonObject.getJSONObject("result");
                            if ("200".equals(result.getString("code"))) {
                                mHandler.sendEmptyMessage(3);
                            } else {
                                mHandler.sendEmptyMessage(4);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
    
    // 支付参数校验
    private String checkoutPayInfo() {
        if (mPayInfo.getRoleId() == null || mPayInfo.getRoleId().isEmpty()) {
            return "roleId";
        } else if (mPayInfo.getServerId() == null || mPayInfo.getServerId().isEmpty()) {
            return "serverId";
        } else if (mPayInfo.getGameName() == null || mPayInfo.getGameName().isEmpty()) {
            return "gameName";
        } else if (mPayInfo.getServerName() == null || mPayInfo.getServerName().isEmpty()) {
            return "serverName";
        }
        return "OK";
    }
    
}
