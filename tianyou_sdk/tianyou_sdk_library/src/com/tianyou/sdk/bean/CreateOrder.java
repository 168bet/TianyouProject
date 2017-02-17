package com.tianyou.sdk.bean;


public class CreateOrder {

	/**
     * code : 200
     * msg : 订单创建成功
     * orderinfo : {"moNey":"300元宝","serverName":"国内Android测试服","roleId":"13141654","sign":"84d80b227f4061eb3e726b3c8de33f55","appID":"1020","userId":"9986","Way":"ALIPAY","serverID":"99990","customInfo":"21689575c5284a334ca8f6630127915f9058","orderID":"2016101115435229296","product_name":"山口山战记-国内Android测试服"}
     * payinfo : {"SELLER":"2090640833@qq.com","PARTNER":"2088221284701274","RSA_PRIVATE":"MIICXAIBAAKBgQC2U4VhQ2dYFxqLuO6ETeTyht7e5Y7qz8cjkmSzpvVVoLcvG3Bs\nVyGIyqXzelWcTmuVKtxKaTq2DTqTqHbgxkEZVqP5x24tztr8ZDu4fysj7V+Y39Nt\nL8GsYTooBBAS3kSNJ8ziXLA90AlUWfHdO0UUGWC/KTWYj2S8uPmDDomrtwIDAQAB\nAoGABq1IVSeCwVcXQcaj9XRkzfzibIc7lJ3HTSYvSeTzVw9rpe37T/xg3gGN0jzI\npdC5X90R9CemXsRdQMsKj3CzMYqEPPiyvpZKIHJVT9zXtWaD0RuhK2r6V+W6HtrR\n0nuuZndNOskjZ0qRnaYRtiFSSjGDkvexSL5woN/Dyqk76QECQQDhYpXnQfymchBj\nFfh0iYBExeaest1EifsTskN4lqIfqJnY4lH15fnU4sZZHJYuGpdR3GlsGozfVgNl\nmGhyEP6NAkEAzxehpHg3BVjX5u7nEKaFIMpgZLlpE5Ah73LsjRvch+bQkRUCEaZh\nNe8QmMtxZQO2Znmd7HBVGpPqxu9aEzy0UwJBAM+rqPf2wzcSbqr9+XpXn8q+lqqv\noFWg1anmXWjlUujczzUaA+8RQku30I8XWgGNGtSgxLqJFDhM5sBb2BESO5kCQGVv\n2NA7xg+Lbe2C1ZF1Tz45gq+zlcFsVwL3kuCnZgT3TO/Tj3jfuzv0xndhJ0DoaZ6W\nMYGPoE/tZhFdSgykLlsCQG8Y+urvOIp7d06ouOALG7lD3dCm6B459MPOxfyrvBon\nFTIBCsC3tcgWnLRVyruGGNL4jGwn6ax7UKRdwtB+Ias=\n","RSA_PUBLIC":"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC2U4VhQ2dYFxqLuO6ETeTyht7e5Y7qz8cjkmSzpvVVoLcvG3BsVyGIyqXzelWcTmuVKtxKaTq2DTqTqHbgxkEZVqP5x24tztr8ZDu4fysj7V+Y39NtL8GsYTooBBAS3kSNJ8ziXLA90AlUWfHdO0UUGWC/KTWYj2S8uPmDDomrtwIDAQAB"}
     */

    private ResultBean result;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        private int code;
        private String msg;
        /**
         * moNey : 300元宝
         * serverName : 国内Android测试服
         * roleId : 13141654
         * sign : 84d80b227f4061eb3e726b3c8de33f55
         * appID : 1020
         * userId : 9986
         * Way : ALIPAY
         * serverID : 99990
         * customInfo : 21689575c5284a334ca8f6630127915f9058
         * orderID : 2016101115435229296
         * product_name : 山口山战记-国内Android测试服
         */

        private OrderinfoBean orderinfo;
        /**
         * SELLER : 2090640833@qq.com
         * PARTNER : 2088221284701274
         * RSA_PRIVATE : MIICXAIBAAKBgQC2U4VhQ2dYFxqLuO6ETeTyht7e5Y7qz8cjkmSzpvVVoLcvG3Bs
         VyGIyqXzelWcTmuVKtxKaTq2DTqTqHbgxkEZVqP5x24tztr8ZDu4fysj7V+Y39Nt
         L8GsYTooBBAS3kSNJ8ziXLA90AlUWfHdO0UUGWC/KTWYj2S8uPmDDomrtwIDAQAB
         AoGABq1IVSeCwVcXQcaj9XRkzfzibIc7lJ3HTSYvSeTzVw9rpe37T/xg3gGN0jzI
         pdC5X90R9CemXsRdQMsKj3CzMYqEPPiyvpZKIHJVT9zXtWaD0RuhK2r6V+W6HtrR
         0nuuZndNOskjZ0qRnaYRtiFSSjGDkvexSL5woN/Dyqk76QECQQDhYpXnQfymchBj
         Ffh0iYBExeaest1EifsTskN4lqIfqJnY4lH15fnU4sZZHJYuGpdR3GlsGozfVgNl
         mGhyEP6NAkEAzxehpHg3BVjX5u7nEKaFIMpgZLlpE5Ah73LsjRvch+bQkRUCEaZh
         Ne8QmMtxZQO2Znmd7HBVGpPqxu9aEzy0UwJBAM+rqPf2wzcSbqr9+XpXn8q+lqqv
         oFWg1anmXWjlUujczzUaA+8RQku30I8XWgGNGtSgxLqJFDhM5sBb2BESO5kCQGVv
         2NA7xg+Lbe2C1ZF1Tz45gq+zlcFsVwL3kuCnZgT3TO/Tj3jfuzv0xndhJ0DoaZ6W
         MYGPoE/tZhFdSgykLlsCQG8Y+urvOIp7d06ouOALG7lD3dCm6B459MPOxfyrvBon
         FTIBCsC3tcgWnLRVyruGGNL4jGwn6ax7UKRdwtB+Ias=

         * RSA_PUBLIC : MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC2U4VhQ2dYFxqLuO6ETeTyht7e5Y7qz8cjkmSzpvVVoLcvG3BsVyGIyqXzelWcTmuVKtxKaTq2DTqTqHbgxkEZVqP5x24tztr8ZDu4fysj7V+Y39NtL8GsYTooBBAS3kSNJ8ziXLA90AlUWfHdO0UUGWC/KTWYj2S8uPmDDomrtwIDAQAB
         */

        private PayinfoBean payinfo;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public OrderinfoBean getOrderinfo() {
            return orderinfo;
        }

        public void setOrderinfo(OrderinfoBean orderinfo) {
            this.orderinfo = orderinfo;
        }

        public PayinfoBean getPayinfo() {
            return payinfo;
        }

        public void setPayinfo(PayinfoBean payinfo) {
            this.payinfo = payinfo;
        }

        public static class OrderinfoBean {
            private String moNey;
            private String serverName;
            private String roleId;
            private String sign;
            private String appID;
            private String userId;
            private String Way;
            private String serverID;
            private String customInfo;
            private String orderID;
            private String product_name;

            public String getMoNey() {
                return moNey;
            }

            public void setMoNey(String moNey) {
                this.moNey = moNey;
            }

            public String getServerName() {
                return serverName;
            }

            public void setServerName(String serverName) {
                this.serverName = serverName;
            }

            public String getRoleId() {
                return roleId;
            }

            public void setRoleId(String roleId) {
                this.roleId = roleId;
            }

            public String getSign() {
                return sign;
            }

            public void setSign(String sign) {
                this.sign = sign;
            }

            public String getAppID() {
                return appID;
            }

            public void setAppID(String appID) {
                this.appID = appID;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getWay() {
                return Way;
            }

            public void setWay(String Way) {
                this.Way = Way;
            }

            public String getServerID() {
                return serverID;
            }

            public void setServerID(String serverID) {
                this.serverID = serverID;
            }

            public String getCustomInfo() {
                return customInfo;
            }

            public void setCustomInfo(String customInfo) {
                this.customInfo = customInfo;
            }

            public String getOrderID() {
                return orderID;
            }

            public void setOrderID(String orderID) {
                this.orderID = orderID;
            }

            public String getProduct_name() {
                return product_name;
            }

            public void setProduct_name(String product_name) {
                this.product_name = product_name;
            }
        }

        public static class PayinfoBean {
            private String SELLER;
            private String PARTNER;
            private String RSA_PRIVATE;
            private String RSA_PUBLIC;
            private String tnnumber;
            private String imgstr;
            private String qqmember;
            private String GGproduct_id;

            public String getGGproduct_id (){ return GGproduct_id; }

            public void setGGproduct_id(String GGproduct_id) { this.GGproduct_id = GGproduct_id; }
            
            public String getImgstr(){
            	return imgstr;
            }
            
            public void setImgstr(String imgstr){
            	this.imgstr = imgstr;
            }
            
            public String getQqmember(){
            	return qqmember;
            }
            
            public void setQqmember(String qqmember){
            	this.qqmember = qqmember;
            }

            public String getSELLER() {
                return SELLER;
            }

            public void setSELLER(String SELLER) {
                this.SELLER = SELLER;
            }

            public String getPARTNER() {
                return PARTNER;
            }

            public void setPARTNER(String PARTNER) {
                this.PARTNER = PARTNER;
            }

            public String getRSA_PRIVATE() {
                return RSA_PRIVATE;
            }

            public void setRSA_PRIVATE(String RSA_PRIVATE) {
                this.RSA_PRIVATE = RSA_PRIVATE;
            }

            public String getRSA_PUBLIC() {
                return RSA_PUBLIC;
            }

            public void setRSA_PUBLIC(String RSA_PUBLIC) {
                this.RSA_PUBLIC = RSA_PUBLIC;
            }
            
            public String getTnnumber() {
                return tnnumber;
            }

            public void setTnnumber(String tnnumber) {
                this.tnnumber = tnnumber;
            }
        }
    }
}
