package com.tianyou.sdk.bean;

/**
 * 客服信息
 * @author itstrong
 *
 */
public class ServerInfo {
	/**
     * code : 200
     * msg : 客服信息已获取
     * custominfo : {"call":{"imgurl":"http://api.tianyouxi.com/Public/Home/pay_phone.png","value":"010-57791788"},"qq":{"imgurl":"http://api.tianyouxi.com/Public/Home/pay_qq.png","value":"485613837","type":"1"},"wx":{"imgurl":"http://api.tianyouxi.com/Public/Home/pay_two_code.jpg","value":"天游戏"}}
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
         * call : {"imgurl":"http://api.tianyouxi.com/Public/Home/pay_phone.png","value":"010-57791788"}
         * qq : {"imgurl":"http://api.tianyouxi.com/Public/Home/pay_qq.png","value":"485613837","type":"1"}
         * wx : {"imgurl":"http://api.tianyouxi.com/Public/Home/pay_two_code.jpg","value":"天游戏"}
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
            /**
             * imgurl : http://api.tianyouxi.com/Public/Home/pay_phone.png
             * value : 010-57791788
             */

            private CallBean call;
            /**
             * imgurl : http://api.tianyouxi.com/Public/Home/pay_qq.png
             * value : 485613837
             * type : 1
             */

            private QqBean qq;
            /**
             * imgurl : http://api.tianyouxi.com/Public/Home/pay_two_code.jpg
             * value : 天游戏
             */

            private WxBean wx;

            public CallBean getCall() {
                return call;
            }

            public void setCall(CallBean call) {
                this.call = call;
            }

            public QqBean getQq() {
                return qq;
            }

            public void setQq(QqBean qq) {
                this.qq = qq;
            }

            public WxBean getWx() {
                return wx;
            }

            public void setWx(WxBean wx) {
                this.wx = wx;
            }

            public static class CallBean {
                private String imgurl;
                private String value;

                public String getImgurl() {
                    return imgurl;
                }

                public void setImgurl(String imgurl) {
                    this.imgurl = imgurl;
                }

                public String getValue() {
                    return value;
                }

                public void setValue(String value) {
                    this.value = value;
                }
            }

            public static class QqBean {
                private String imgurl;
                private String value;
                private String type;

                public String getImgurl() {
                    return imgurl;
                }

                public void setImgurl(String imgurl) {
                    this.imgurl = imgurl;
                }

                public String getValue() {
                    return value;
                }

                public void setValue(String value) {
                    this.value = value;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }
            }

            public static class WxBean {
                private String imgurl;
                private String value;

                public String getImgurl() {
                    return imgurl;
                }

                public void setImgurl(String imgurl) {
                    this.imgurl = imgurl;
                }

                public String getValue() {
                    return value;
                }

                public void setValue(String value) {
                    this.value = value;
                }
            }
        }
    }
}
