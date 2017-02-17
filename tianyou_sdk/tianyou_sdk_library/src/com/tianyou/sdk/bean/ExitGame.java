package com.tianyou.sdk.bean;

import java.util.List;

public class ExitGame {

	/**
     * code : 200
     * msg : 游戏信息已返回
     * productinfo : [{"id":"1009","name":"寻龙剑","filesize":"265","icon":"http://public.tianyouxi.com/Uploads/Picture/2016-07-29/579b3743cfbe0.png","download":"2227","path":"http://dl.tianyouxi.com/data/down/1009/XLJ_tyx.apk"},{"id":"1011","name":"剑与魔法","filesize":"280","icon":"http://public.tianyouxi.com/Uploads/Picture/2016-07-23/579358e466e7d.png","download":"46","path":"http://dl.tianyouxi.com/data/down/1011/JYMF_tyhd.apk"},{"id":"1028","name":"龙神捕鱼","filesize":"31","icon":"http://public.tianyouxi.com/Uploads/Picture/2016-09-19/57dfa56699dd2.png","download":"199","path":"http://dl.tianyouxi.com/data/down/1028/Fish_TYX.apk"},{"id":"1029","name":"降魔传奇","filesize":"266","icon":"http://public.tianyouxi.com/Uploads/Picture/2016-08-09/57a9a0a57eb74.png","download":"219","path":"http://dl.tianyouxi.com/data/down/1009/XLJ_ty_xmcq.apk"}]
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
         * id : 1009
         * name : 寻龙剑
         * filesize : 265
         * icon : http://public.tianyouxi.com/Uploads/Picture/2016-07-29/579b3743cfbe0.png
         * download : 2227
         * path : http://dl.tianyouxi.com/data/down/1009/XLJ_tyx.apk
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
            private String id;
            private String name;
            private String filesize;
            private String icon;
            private String download;
            private String path;

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

            public String getFilesize() {
                return filesize;
            }

            public void setFilesize(String filesize) {
                this.filesize = filesize;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getDownload() {
                return download;
            }

            public void setDownload(String download) {
                this.download = download;
            }

            public String getPath() {
                return path;
            }

            public void setPath(String path) {
                this.path = path;
            }
        }
    }
}
