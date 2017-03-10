package com.tianyou.sdk.holder;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.tianyou.sdk.bean.CreateOrder;
import com.tianyou.sdk.bean.CreateOrder.ResultBean;
import com.tianyou.sdk.bean.CreateOrder.ResultBean.OrderinfoBean;
import com.tianyou.sdk.bean.PayParamInfo;
import com.tianyou.sdk.utils.AppUtils;
import com.tianyou.sdk.utils.AppUtils.DialogCallback;
import com.tianyou.sdk.utils.HttpUtils;
import com.tianyou.sdk.utils.HttpUtils.HttpCallback;
import com.tianyou.sdk.utils.HttpUtils.HttpsCallback;
import com.tianyou.sdk.utils.LogUtils;
import com.tianyou.sdk.utils.ToastUtils;

/**
 * 支付逻辑处理
 * @author itstrong
 *
 */
public class PaymentHandler {

    public static final int PAY_TYPE_WECHAT = 0;
    public static final int PAY_TYPE_ALIPAY = 1;
    public static final int PAY_TYPE_QQPAY = 2;
    public static final int PAY_TYPE_UNION = 3;
    public static final int PAY_TYPE_REMIT = 4;
    public static final int PAY_TYPE_WALLET = 5;
    public static final int PAY_TYPE_WXSCAN = 6;
    public static final int PAY_TYPE_GOOGLE = 7;
    public static final int PAY_TYPE_PAYPAL = 8;
    
    public int mPayType;				//当前支付类型
    public PayParamInfo mPayInfo;		//支付参数集
    public boolean PAY_FLAG;			//防止多次点击充值
    public boolean mIsShowChoose;		//是否有选择金额页面
    
    private static PaymentHandler mPaymentHandler;
    private static Activity mActivity;
    private static Handler mHandler;
    
    private PaymentHandler() {}
    
    public static PaymentHandler getInstance(Activity activity, Handler handler) {
    	mHandler = handler;
		mActivity = activity;
		if (mPaymentHandler == null) {
			mPaymentHandler = new PaymentHandler();
		}
		return mPaymentHandler;
	}
    
    // 创建订单
    public void createOrder() {
        LogUtils.d("mPayInfo:" + mPayInfo);
        String payWay = "";
        if (mPayType == PAY_TYPE_WECHAT) {
            payWay = "WXPAY";
        } else if (mPayType == PAY_TYPE_ALIPAY) {
            payWay = "ALIPAY";
        } else if (mPayType == PAY_TYPE_QQPAY) {
            payWay = "HANDQ";
        } else if (mPayType == PAY_TYPE_UNION) {
            payWay = "UNPAY";
        } else if (mPayType == PAY_TYPE_REMIT) {
            payWay = "BANK_PAY";
        } else if (mPayType == PAY_TYPE_WALLET) {
            payWay = "QBPAY";
        } else if (mPayType == PAY_TYPE_WXSCAN) {
            payWay = "WXSCAN";
        } else if (mPayType == PAY_TYPE_GOOGLE) {
            payWay = "GOOGLEPAY";
        } else if (mPayType == PAY_TYPE_PAYPAL) {
        	payWay = "PAYPALPAY";
        }
        String userId = ConfigHolder.userId;
        String appID = ConfigHolder.gameId;
        String serverID = mPayInfo.getServerId();
        String createOrderUrl = "";
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId", userId);
        map.put("roleId", mPayInfo.getRoleId());
        map.put("appID", appID);
        map.put("serverID", serverID);
        map.put("customInfo", mPayInfo.getCustomInfo());
        map.put("serverName", mPayInfo.getServerName());
        map.put("moNey", mPayInfo.getMoney());
        map.put("Way", payWay);
        map.put("productId",mPayInfo.getProductId());
        map.put("sign", AppUtils.MD5(userId + serverID + serverID));
        if (mPayType == PaymentHandler.PAY_TYPE_GOOGLE || mPayType == PaymentHandler.PAY_TYPE_PAYPAL) {
        	createOrderUrl = URLHolder.URL_CREATE_ORDER_OVERSEAS;
        } else {
        	createOrderUrl = URLHolder.URL_CREATE_ORDER;
        }
        HttpUtils.post(mActivity, createOrderUrl, map, new HttpCallback() {
            @Override
            public void onSuccess(String response) {
                CreateOrder createOrder = new Gson().fromJson(response, CreateOrder.class);
                ResultBean result = createOrder.getResult();
                if (result.getCode() == 200) {
                    OrderinfoBean orderinfo = result.getOrderinfo();
                    mPayInfo.setOrderId(orderinfo.getOrderID());
                    mPayInfo.setProductName(orderinfo.getProduct_name());
                    mPayInfo.setPayMoney(orderinfo.getMoNey());
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
                        Log.d("TAG", "111111111111111111111111111111");
                    }
                    doPay();
                } else if (mPayType == PAY_TYPE_WALLET) {
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
    
    private void showWalletTip() {
        AlertDialog.Builder builder = new Builder(mActivity);
        builder.setMessage("天游币余额不足，是否给天游币充值？");  
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                mHandler.sendEmptyMessage(2);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new OnClickListener() {
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
    
    // 获取支付方式名称
    public String getPayWayName() {
        String payWayName = "微信支付";
        switch (mPayType) {
        case PAY_TYPE_WECHAT:
            payWayName = "微信支付";
            break;
        case PAY_TYPE_ALIPAY:
            payWayName = "支付宝";
            break;
        case PAY_TYPE_UNION:
            payWayName = "银联支付";
            break;
        case PAY_TYPE_REMIT:
            payWayName = "汇款";
            break;
        case PAY_TYPE_WALLET:
            payWayName = "钱包支付";
            break;
        case PAY_TYPE_QQPAY:
            payWayName = "QQ钱包支付";
            break;
        case PAY_TYPE_WXSCAN:
            payWayName = "微信扫码支付方式";
            break;
        case PAY_TYPE_GOOGLE:
        	payWayName = "Google Payment";
        	break;
        case PAY_TYPE_PAYPAL:
        	payWayName = "Paypal Payment";
        	break;
        }
        return payWayName;
    }
    
    // 创建钱包订单
    public void createWalletOrder() {
        String payWay = "";
        if (mPayType == PAY_TYPE_WECHAT) {
            payWay = "WXPAY";
        } else if (mPayType == PAY_TYPE_ALIPAY) {
            payWay = "ALIPAY";
        } else if (mPayType == PAY_TYPE_QQPAY) {
            payWay = "HANDQ";
        } else if (mPayType == PAY_TYPE_UNION) {
            payWay = "UNPAY";
        } else if (mPayType == PAY_TYPE_REMIT) {
            payWay = "BANK_PAY";
        } else if (mPayType == PAY_TYPE_WALLET) {
            payWay = "QBPAY";
        } else if (mPayType == PAY_TYPE_WXSCAN) {
            payWay = "WXSCAN";
        }
        String userId = ConfigHolder.userId;
        String appID = ConfigHolder.gameId;
        String serverID = mPayInfo.getServerId();
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId", userId);
        map.put("roleId", mPayInfo.getRoleId());
        map.put("appID", appID);
        map.put("serverID", serverID);
        map.put("customInfo", mPayInfo.getCustomInfo());
        map.put("serverName", mPayInfo.getServerName());
        map.put("moNey", mPayInfo.getMoney());
        map.put("Way", payWay);
        map.put("sign", AppUtils.MD5(userId + serverID + serverID));
        HttpUtils.post(mActivity, URLHolder.URL_PAY_WALLET, map, new HttpCallback() {
            @Override
            public void onSuccess(String response) {
                CreateOrder createOrder = new Gson().fromJson(response, CreateOrder.class);
                ResultBean result = createOrder.getResult();
                if (result.getCode() == 200) {
                    OrderinfoBean orderinfo = result.getOrderinfo();
                    mPayInfo.setOrderId(orderinfo.getOrderID());
                    mPayInfo.setProductName(orderinfo.getProduct_name());
                    mPayInfo.setPayMoney(orderinfo.getMoNey());
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
                    doPay();
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
    private void doPay() {
        switch (mPayType) {
            case PAY_TYPE_WECHAT:
                new WechatPay(mActivity, true, mPayInfo,this).doWeChatPay();
                break;
            case PAY_TYPE_QQPAY:
                new WechatPay(mActivity, false, mPayInfo,this).doWeChatPay();
                break;
            case PAY_TYPE_ALIPAY:
                new Alipay(mActivity, mHandler, mPayInfo).doAlipay();
                break;
            case PAY_TYPE_UNION:
                new UnionPay(mActivity, mPayInfo.getTnnumber()).doUnionPay();
                break;
            case PAY_TYPE_REMIT:
            	mHandler.sendEmptyMessage(6);
                break;
            case PAY_TYPE_WALLET:
            	mHandler.sendEmptyMessage(3);
                break;
            case PAY_TYPE_WXSCAN:
            	mHandler.sendEmptyMessage(7);
                break;
            case PAY_TYPE_GOOGLE:
            	Log.d("TAG", "2222222222222222222222");
            	mHandler.sendEmptyMessage(8);
            	Log.d("TAG", "3333333333333333333333");
                break;
            case PAY_TYPE_PAYPAL:
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
    public void doQueryOrder(){
        AppUtils.showProgressDialog(mActivity, "查询订单", "正在查询订单，请稍后...", new DialogCallback() {
            public void onDismiss() {
                Map<String, String> checkParam = new HashMap<String, String>();
                checkParam.put("orderID", mPayInfo.getOrderId());
                checkParam.put("appID",ConfigHolder.gameId);
                checkParam.put("Token",ConfigHolder.gameToken);
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

    public void receivePayParam(String paramInfo) {
        mPayInfo = new Gson().fromJson(paramInfo, PayParamInfo.class);
        LogUtils.d("支付参数信息：" + mPayInfo);
        LogUtils.d("mPayInfo.getMoney().isEmpty():" + mPayInfo.getMoney().isEmpty());
//		mIsShowChoose = mPayInfo.getMoney().isEmpty();
        String result = payParamInfoCheckout();
        if (!result.equals("OK")) {
            ToastUtils.show(mActivity, result + "不能为空");
        }
    }
    
    // 支付参数校验
    private String payParamInfoCheckout() {
        if (mPayInfo.getRoleId() == null || mPayInfo.getRoleId().isEmpty()) {
            return "roleId";
        } else if (mPayInfo.getServerId() == null || mPayInfo.getServerId().isEmpty()) {
            return "serverId";
        } else if (mPayInfo.getGameName() == null || mPayInfo.getGameName().isEmpty()) {
            return "gameName";
        } else if (mPayInfo.getServerName() == null || mPayInfo.getServerName().isEmpty()) {
            return "serverName";
        } else if (mPayInfo.getSign() == null || mPayInfo.getSign().isEmpty()) {
            return "sign";
        } else if (mPayInfo.getSignType() == null || mPayInfo.getSignType().isEmpty())
            return "signType";
        else if (mPayInfo.getProductId() == null || mPayInfo.getProductId().isEmpty()) {	// customInfo验证
            return "productId";
        }
        return "OK";
    }
}
