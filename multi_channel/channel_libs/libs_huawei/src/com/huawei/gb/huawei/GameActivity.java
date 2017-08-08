///*
//Copyright (C) Huawei Technologies Co., Ltd. 2015. All rights reserved.
//See LICENSE.txt for this sample's licensing information.
// */
//package com.huawei.gb.huawei;
//
//import java.text.DateFormat;
//import java.util.Date;
//import java.util.Locale;
//import java.util.Map;
//import java.util.regex.Pattern;
//
//import android.os.Bundle;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.huawei.gameservice.sdk.control.GameEventHandler;
//import com.huawei.gameservice.sdk.model.PayResult;
//import com.huawei.gameservice.sdk.model.Result;
//import com.huawei.gameservice.sdk.view.GameServiceBaseActivity;
//
//public class GameActivity extends GameServiceBaseActivity implements OnClickListener
//{
//        
//    private boolean isMainPage = true;
//    
//    
//    /**
//     * 支付回调handler
//     */
//    /**
//     * pay handler
//     */
//    private GameEventHandler payHandler = new GameEventHandler()
//    {
//		@Override
//		public String getGameSign(String appId, String cpId, String ts) {
//			return null;
//		}
//		
//        @Override
//        public void onResult(Result result)
//        {
// 
//            
//            Map<String, String> payResp = ((PayResult)result).getResultMap();
//            String pay = getString(R.string.pay_result_failed);
//            // 支付成功，进行验签
//            // payment successful, then check the response value
//            if ("0".equals(payResp.get("returnCode")))
//            {
//                if ("success".equals(payResp.get("errMsg")))
//                {
//                    // 支付成功，验证信息的安全性；待验签字符串中如果有isCheckReturnCode参数且为yes，则去除isCheckReturnCode参数
//                	// If the response value contain the param "isCheckReturnCode" and its value is yes, then remove the param "isCheckReturnCode".
//                	if (payResp.containsKey("isCheckReturnCode") && "yes".equals(payResp.get("isCheckReturnCode")))
//                    {
//                        payResp.remove("isCheckReturnCode");
//                        
//                    }
//                	// 支付成功，验证信息的安全性；待验签字符串中如果没有isCheckReturnCode参数活着不为yes，则去除isCheckReturnCode和returnCode参数
//                	// If the response value does not contain the param "isCheckReturnCode" and its value is yes, then remove the param "isCheckReturnCode".
//                	else
//                    {
//                        payResp.remove("isCheckReturnCode");
//                        payResp.remove("returnCode");
//                    }
//                    // 支付成功，验证信息的安全性；待验签字符串需要去除sign参数
//                	// remove the param "sign" from response
//                    String sign = payResp.remove("sign");
//                    
//                    String noSigna = GameBoxUtil.getSignData(payResp);
//                    
//                    // 使用公钥进行验签
//                    // check the sign using RSA public key
//                    boolean s = RSAUtil.doCheck(noSigna, sign, GlobalParam.PAY_RSA_PUBLIC);
//                    
//                    if (s)
//                    {
//                        pay = getString(R.string.pay_result_success);
//                    }
//                    else
//                    {
//                        pay = getString(R.string.pay_result_check_sign_failed);
//                    }
//                }
//               
//            }
//            else if ("30002".equals(payResp.get("returnCode")))
//            {
//                pay = getString(R.string.pay_result_timeout);
//            }
//            Toast.makeText(GameActivity.this, pay, Toast.LENGTH_SHORT).show();
//            
//            // 重新生成订单号，订单编号不能重复，所以使用时间的方式，CP可以根据实际情况进行修改，最长30字符
//            // generate the pay ID using the date format, and it can not be repeated. 
//            // CP can generate the pay ID according to the actual situation, a maximum of 30 characters
//            DateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd-hh-mm-ss-SSS", Locale.US);
//            String requestId = format.format(new Date());
//            ((TextView)findViewById(R.id.requestId)).setText(requestId);
//            
//
//        }
//    };
//    
//    @Override
//    protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        
//        requestWindowFeature(Window.FEATURE_NO_TITLE); 
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//     				WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        
//        
//        setContentView(R.layout.activity_game);
//        
//        findViewById(R.id.beginbuy).setOnClickListener(this);
//        findViewById(R.id.paymoney).setOnClickListener(this);
//
//    }
//    
//    @Override
//    public void onClick(View view)
//    {
//        int id = view.getId();
//        String requestId = null;
//        isMainPage = false;
//        switch (id)
//        {
//        	// 点击开始支付按钮，把支付选项显示出来
//        	// click the "In-game purchase" button
//            case R.id.beginbuy:
//                DateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd-hh-mm-ss-SSS", Locale.US);
//                requestId = format.format(new Date());
//                ((TextView)findViewById(R.id.requestId)).setText(requestId);
//                findViewById(R.id.beginbuy).setVisibility(View.GONE);
//                findViewById(R.id.buytips).setVisibility(View.VISIBLE);
//                findViewById(R.id.pricelinear).setVisibility(View.VISIBLE);
//                findViewById(R.id.namelinear).setVisibility(View.VISIBLE);
//                findViewById(R.id.desclinear).setVisibility(View.VISIBLE);
//                findViewById(R.id.requestLinear).setVisibility(View.VISIBLE);
//                findViewById(R.id.paymoney).setVisibility(View.VISIBLE);
//                ((TextView)findViewById(R.id.itemTips)).setText(R.string.item_tips);
//                findViewById(R.id.itemTips).setVisibility(View.VISIBLE);
//
//                break;
//            // 点击付款按钮
//            // click the "pay" button
//            case R.id.paymoney:
//                String price = ((EditText)findViewById(R.id.productPrice)).getText().toString().trim();
//                String productName = ((EditText)findViewById(R.id.productName)).getText().toString().trim();
//                String productDesc = ((EditText)findViewById(R.id.productDesc)).getText().toString().trim();
//                requestId = ((TextView)findViewById(R.id.requestId)).getText().toString().trim();
//                
//                // 价格必须精确到小数点后两位，使用正则进行匹配
//                // The price must be accurate to two decimal places
//                boolean priceChceckRet = Pattern.matches("^\\d+[.]\\d{2}$", price);
//                if (!priceChceckRet)
//                {
//                    Toast.makeText(getApplicationContext(), getString(R.string.msg_price_invalid), Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                
//                if ("".equals(productName))
//                {
//                    Toast.makeText(getApplicationContext(), getString(R.string.msg_product_name_null), Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                // 禁止输入：# " & / ? $ ^ *:) \ < > | , =
//                // the name can not input characters: # " & / ? $ ^ *:) \ < > | , =
//                else if (Pattern.matches(".*[#\\$\\^&*)=|\",/<>\\?:].*", productName))
//                {
//                    Toast.makeText(getApplicationContext(), getString(R.string.msg_product_name_invalid), Toast.LENGTH_LONG).show();
//                    return;
//                }
//                if ("".equals(productDesc))
//                {
//                    Toast.makeText(getApplicationContext(), getString(R.string.msg_product_description_null), Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                // 禁止输入：# " & / ? $ ^ *:) \ < > | , =
//                // the description can not input characters: # " & / ? $ ^ *:) \ < > | , =
//                else if (Pattern.matches(".*[#\\$\\^&*)=|\",/<>\\\\?\\^:].*", productDesc))
//                {
//                    Toast.makeText(getApplicationContext(), getString(R.string.msg_product_description_invalid), Toast.LENGTH_LONG).show();
//                    return;
//                }
//                
//                // 调用公共方法进行支付
//                // call the pay method
//                GameBoxUtil.startPay(GameActivity.this, price, productName, productDesc, requestId, payHandler);
//                break;
//          
//            default:
//                break;
//        }
//    }
//    
//    private void returnMainPage()
//    {
//        findViewById(R.id.beginbuy).setVisibility(View.VISIBLE);
//        findViewById(R.id.buytips).setVisibility(View.GONE);
//        findViewById(R.id.pricelinear).setVisibility(View.GONE);
//        findViewById(R.id.namelinear).setVisibility(View.GONE);
//        findViewById(R.id.desclinear).setVisibility(View.GONE);
//        findViewById(R.id.requestLinear).setVisibility(View.GONE);
//        findViewById(R.id.paymoney).setVisibility(View.GONE);
//        findViewById(R.id.itemTips).setVisibility(View.GONE);
//        
//        isMainPage = true;
//    }
//    
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event)
//    {
//        if (keyCode == KeyEvent.KEYCODE_BACK)
//        {
//            if (isMainPage)
//            {
//                return super.onKeyDown(keyCode, event);
//            }
//            else
//            {
//                returnMainPage();
//                return true;
//            }
//
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//    
//    
//
//}
