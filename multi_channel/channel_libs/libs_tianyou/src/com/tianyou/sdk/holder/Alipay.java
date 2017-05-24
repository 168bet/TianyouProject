package com.tianyou.sdk.holder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.alipay.sdk.app.PayTask;
import com.tianyou.sdk.bean.PayInfo;
import com.tianyou.sdk.utils.LogUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

/**
 * 支付宝支付接口
 * Created by itstrong on 2016/7/1.
 */
public class Alipay {

    private Activity mContext;
    private Handler mHandler;

    public String PARTNER;	  // 商户PID
    public String SELLER; 	  // 商户收款账号
    public String RSA_PRIVATE; // 商户私钥，pkcs8格式
    public String RSA_PUBLIC;  // 支付宝公钥
    private final int SDK_PAY_FLAG = 1;
    private PayInfo mPayInfo;

    public Alipay(Activity activity, Handler handler, PayInfo payInfo) {
        this.mContext = activity;
        this.mHandler = handler;
        this.mPayInfo = payInfo;
        this.PARTNER = mPayInfo.getPARTNER();
        this.SELLER = mPayInfo.getSELLER();
        this.RSA_PRIVATE = mPayInfo.getRSA_PRIVATE();
        this.RSA_PUBLIC = mPayInfo.getRSA_PUBLIC();
    }

    public void doAlipay() {
        if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE) || TextUtils.isEmpty(SELLER)) {
            new AlertDialog.Builder(mContext).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            mContext.finish();
                        }
                    }).show();
            return;
        }
        String orderInfo = getOrderInfo();
        LogUtils.d("orderInfo:" + orderInfo);

        /**
         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
         */
        String sign = sign(orderInfo);
        LogUtils.d("sign:" + sign);
        try {
            /**
             * 仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(sign, "UTF-8");
            LogUtils.d("sign:URLEncoder:" + sign);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        /**
         * 完整的符合支付宝参数规范的订单信息
         */
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

        LogUtils.d("payInfo:" + payInfo);
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(mContext);
                // 调用支付接口，获取支付结果
                final String result = alipay.pay(payInfo, true);
                
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
    
    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content
     *            待签名订单信息
     */
    private String sign(String content) {
        return com.tianyou.sdk.utils.SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     *
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }

    /**
     * create the order info. 创建订单信息
     *
     */
    private String getOrderInfo() {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + mPayInfo.getOrderId() + "\"";
        // 商品名称
        orderInfo += "&subject=" + "\"" + mPayInfo.getProductName() + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + mPayInfo.getProductName() + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + mPayInfo.getMoney() + "\"";
//        orderInfo += "&total_fee=" + "\"" + "0.01" + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + URLHolder.URL_NOTIFY_ALIPAY + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&notify_url=\"" + URLHolder.URL_NOTIFY_ALIPAY + "\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }
}
