package com.tianyou.sdk.bean;

public class PayType {
	/**
     * code : 200
     * msg : 支付方式已获取
     * custominfo : {"wx_pay":1,"zfb_pay":1,"wy_pay":1,"dk_pay":1,"hk_pay":1,"qb_pay":1}
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
         * wx_pay : 1
         * zfb_pay : 1
         * wy_pay : 1
         * dk_pay : 1
         * hk_pay : 1
         * qb_pay : 1
         */

        private CustominfoBean custominfo;

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

        public CustominfoBean getCustominfo() {
            return custominfo;
        }

        public void setCustominfo(CustominfoBean custominfo) {
            this.custominfo = custominfo;
        }

        public static class CustominfoBean {
            private int wx_pay;
            private int zfb_pay;
            private int wy_pay;
            private int dk_pay;
            private int hk_pay;
            private int qb_pay;

            public int getWx_pay() {
                return wx_pay;
            }

            public void setWx_pay(int wx_pay) {
                this.wx_pay = wx_pay;
            }

            public int getZfb_pay() {
                return zfb_pay;
            }

            public void setZfb_pay(int zfb_pay) {
                this.zfb_pay = zfb_pay;
            }

            public int getWy_pay() {
                return wy_pay;
            }

            public void setWy_pay(int wy_pay) {
                this.wy_pay = wy_pay;
            }

            public int getDk_pay() {
                return dk_pay;
            }

            public void setDk_pay(int dk_pay) {
                this.dk_pay = dk_pay;
            }

            public int getHk_pay() {
                return hk_pay;
            }

            public void setHk_pay(int hk_pay) {
                this.hk_pay = hk_pay;
            }

            public int getQb_pay() {
                return qb_pay;
            }

            public void setQb_pay(int qb_pay) {
                this.qb_pay = qb_pay;
            }
        }
    }
}
