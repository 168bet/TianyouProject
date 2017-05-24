package com.tianyou.sdk.holder;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.switfpass.pay.MainApplication;
import com.switfpass.pay.activity.PayPlugin;
import com.switfpass.pay.bean.RequestMsg;
import com.switfpass.pay.utils.MD5;
import com.switfpass.pay.utils.SignUtils;
import com.switfpass.pay.utils.Util;
import com.switfpass.pay.utils.XmlUtils;
import com.tianyou.sdk.bean.PayInfo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * 微信支付接口
 * Created by itstrong on 2016/7/1.
 */
public class WechatPay {

    private Activity mActivity;
    private PayInfo mPayInfo;
    private PayHandler mPayHandler;
    private String mch_id_wx = "6522000068";
    private String secret_key_wx = "149b9410afd8e8eafed59d3c104a5159";
    
    private String mch_id_qq = "101540000278";
    private String secret_key_qq = "38ec112f5ae62d57c7919d09829373a5";
    private boolean isWechatPay;

    public WechatPay(Activity context,boolean isWechatPay, PayInfo payInfo,PayHandler payHandler) {
        this.mActivity = context;
        this.isWechatPay = isWechatPay;
        this.mPayInfo = payInfo;
        this.mPayHandler = payHandler;
    }

    public void doWeChatPay() {
        new GetPrepayIdTask().execute();
    }

    private class GetPrepayIdTask extends AsyncTask<Void, Void, Map<String, String>> {

        private ProgressDialog dialog;

        public GetPrepayIdTask() { }

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(mActivity, "提示", "正在获取预支付订单...");
        }

        @Override
        protected void onPostExecute(Map<String, String> result) {
            if (dialog != null) {
                dialog.dismiss();
            }
            if (result == null) {
                Toast.makeText(mActivity, "获取prepayid失败！", Toast.LENGTH_LONG).show();
            } else {
                if (result.get("status").equalsIgnoreCase("0")) {// 成功
                    RequestMsg msg = new RequestMsg();
                    if (isWechatPay){
                    	msg.setTokenId(result.get("token_id"));
                    	// 微信wap支付
                    	msg.setTradeType(MainApplication.PAY_WX_WAP);
                    	PayPlugin.unifiedH5Pay(mActivity, msg);
                    } else {
						// 手Q wap支付
                    	msg.setTokenId(result.get("token_id"));
						msg.setTradeType(MainApplication.PAY_QQ_WAP);
						PayPlugin.unifiedH5Pay(mActivity, msg);
                    }
                } else {
                    Toast.makeText(mActivity, "支付失败！", Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @SuppressWarnings("unchecked")
		@Override
        protected Map<String, String> doInBackground(Void... params) {
            // 统一预下单接口
            String url = "https://pay.swiftpass.cn/pay/gateway";
            String entity = getParams();
            byte[] buf = Util.httpPost(url, entity);
            if (buf == null || buf.length == 0) {
                return null;
            }
            String content = new String(buf);
            try {
                return XmlUtils.parse(content);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally{
            	mPayHandler.PAY_FLAG = false;
            }
        }
    }

    /**
     * 组装参数
     * <功能详细描述>
     * @return
     * @see [类、类#方法、类#成员]
     */
    private String getParams() {
        Map<String, String> params = new HashMap<String, String>();
        String sign = "";
        params.put("device_info", "AND_WAP"); // 商品名称
        params.put("mch_app_name", "天游戏官网"); // 商品名称
        params.put("mch_app_id", "http://www.tianyouxi.com"); // 商品名称
        params.put("body", mPayInfo.getProductName()); // 商品名称
        params.put("service", "unified.trade.pay"); // 支付类型
        params.put("version", "1.0"); // 版本
        params.put("notify_url", URLHolder.URL_NOTIFY_WECHAT); // 后台通知url
        params.put("nonce_str", genNonceStr()); // 随机数
        params.put("out_trade_no", mPayInfo.getOrderId()); //订单号
        params.put("mch_create_ip", "127.0.0.1"); // 机器ip地址
        params.put("total_fee", Integer.parseInt(mPayInfo.getMoney()) * 100 + ""); // 总金额
//        params.put("total_fee", 1 + ""); // 总金额
        params.put("limit_credit_pay", "0"); // 是否限制信用卡支付， 0：不限制（默认），1：限制
        if (isWechatPay){
        	params.put("mch_id", mch_id_wx); // 威富通商户号
        	sign = createSign(secret_key_wx, params); // 9d101c97133837e13dde2d32a5054abb 威富通密钥
        } else {
        	params.put("mch_id", mch_id_qq); // 威富通商户号
        	sign = createSign(secret_key_qq, params);
        }
        params.put("sign", sign); // sign签名
        return XmlUtils.toXml(params);
    }

    public String createSign(String signKey, Map<String, String> params) {
        StringBuilder buf = new StringBuilder((params.size() + 1) * 10);
        SignUtils.buildPayParams(buf, params, false);
        buf.append("&key=").append(signKey);
        String preStr = buf.toString();
        String sign = "";
        sign = MD5.md5s(preStr).toUpperCase();
        return sign;
    }

    private String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }
}
