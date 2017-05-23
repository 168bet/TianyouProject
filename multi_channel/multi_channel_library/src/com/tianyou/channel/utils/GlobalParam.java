/*
Copyright (C) Huawei Technologies Co., Ltd. 2015. All rights reserved.
See LICENSE.txt for this sample's licensing information.
*/
package com.tianyou.channel.utils;

public class GlobalParam
{
    /**
     * 联盟为应用分配的应用ID
     */
    /**
     * APP ID
     */
    public static final String APP_ID = "10172150";
    
    /**
     * 浮标密钥，CP必须存储在服务端，然后通过安全网络（如https）获取下来，存储到内存中，否则存在私钥泄露风险
     */
    /**
     * private key for buoy, the CP need to save the key value on the server for security
     */
    public static String BUO_SECRET = "";
    
    /**
     * CPID
     */
    public static final String CP_ID = "900086000000103770";
    
    /**
     * 支付ID
     */
	/**
     * Pay ID
     */
    public static final String PAY_ID = "900086000000103770";
    
    /**
     * 支付私钥，CP必须存储在服务端，然后通过安全网络（如https）获取下来，存储到内存中，否则存在私钥泄露风险
     */
    /**
     * private key for pay, the CP need to save the key value on the server for security
     */
    public static String PAY_RSA_PRIVATE = "";
    
    /**
     * 支付公钥
     */
    /**
     * public key for pay
     */
    public static final String PAY_RSA_PUBLIC = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAI3XiRc0OXxWQ6SCsn+Z+FKYlfmqJpmdwdOkgF19FPj8LEOvPlp2aRZe2DztWMyaBROUriGDjOlMdSHdL1Wdt88CAwEAAQ==";

    /*
     * 支付页面横竖屏参数：1表示竖屏，2表示横屏，默认竖屏
     */
    // portrait view for pay UI
	public static final int PAY_ORI = 1;
	// landscape view for pay UI
	public static final int PAY_ORI_LAND = 2;
    

    
    /**
     * Demo校验accessToken的地址，此地址为华为服务端Demo的地址，CP不能使用，需要自己实现服务端代码并部署，然后修改地址为自己的URL
     */
	/**
	 * the server url for getting the access token.The CP need to modify the value for the real server.
	 */

    /**
     * 生成签名时需要使用RSA的私钥，安全考虑，必须放到服务端，通过此接口使用安全通道获取
     */
    /**
 	 * the server url for getting the pay private key.The CP need to modify the value for the real server.
 	 */
     public static final String GET_PAY_PRIVATE_KEY = "https://ip:port/HuaweiServerDemo/getPayPrivate";
    
    /**
     * 调用浮标时需要使用浮标的私钥，安全考虑，必须放到服务端，通过此接口使用安全通道获取
     */
    /**
  	 * the server url for getting the buoy private key.The CP need to modify the value for the real server.
  	 */
     public static final String GET_BUOY_PRIVATE_KEY = "https://ip:port/HuaweiServerDemo/getBuoyPrivate";
    
    public interface PayParams
    {
        public static final String USER_ID = "userID";
        
        public static final String APPLICATION_ID = "applicationID";
        
        public static final String AMOUNT = "amount";
        
        public static final String PRODUCT_NAME = "productName";
        
        public static final String PRODUCT_DESC = "productDesc";
        
        public static final String REQUEST_ID = "requestId";
        
        public static final String USER_NAME = "userName";
        
        public static final String SIGN = "sign";
        
        public static final String NOTIFY_URL = "notifyUrl";
        
        public static final String SERVICE_CATALOG = "serviceCatalog";
        
        public static final String SHOW_LOG = "showLog";
        
        public static final String SCREENT_ORIENT = "screentOrient";
        
        public static final String SDK_CHANNEL = "sdkChannel";
        
        public static final String URL_VER = "urlver";
    }
    
}
