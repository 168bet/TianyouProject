package com.tianyou.packtool.domain;


import java.util.List;

/**
 * Created by MrV on 2016/8/3.
 */
public class MainChannelBean {

    /**
     * code : 200
     * msg : 产品信息已返回
     * productinfo : [{"channelid":"0","id":"1","name":"百度"},{"channelid":"0","id":"2","name":"天游"}]
     * status : 1
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
        private int status;
        /**
         * channelid : 0
         * id : 1
         * name : 百度
         */

        private List<ProductinfoBean> productinfo;

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

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public List<ProductinfoBean> getProductinfo() {
            return productinfo;
        }

        public void setProductinfo(List<ProductinfoBean> productinfo) {
            this.productinfo = productinfo;
        }

        public static class ProductinfoBean {
            private String channelid;
            private String id;
            private String name;
//            private List childChannel;
//            
//            public List getChildChannel(){
//            	return childChannel;
//            }
//            
//            public void setChildChannel(List childChannel){
//            	this.childChannel = childChannel;
//            }

            public String getChannelid() {
                return channelid;
            }

            public void setChannelid(String channelid) {
                this.channelid = channelid;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
