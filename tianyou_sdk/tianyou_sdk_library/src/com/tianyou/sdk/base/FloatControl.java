package com.tianyou.sdk.base;

/**
 * Created by itstrong on 2017/1/5.
 */
public class FloatControl {

    /**
     * code : 200
     * status : 0
     * msg : 悬浮框开关已获取
     * frameinfo : {"account":1,"more":0,"gift":1,"bbs":0,"help":1,"logout":0}
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
        private int status;
        private String msg;
        /**
         * account : 1
         * more : 0
         * gift : 1
         * bbs : 0
         * help : 1
         * logout : 0
         */

        private FrameinfoBean frameinfo;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public FrameinfoBean getFrameinfo() {
            return frameinfo;
        }

        public void setFrameinfo(FrameinfoBean frameinfo) {
            this.frameinfo = frameinfo;
        }

        public static class FrameinfoBean {
            private int account;
            private int more;
            private int gift;
            private int bbs;
            private int help;
            private int logout;

            public int getAccount() {
                return account;
            }

            public void setAccount(int account) {
                this.account = account;
            }

            public int getMore() {
                return more;
            }

            public void setMore(int more) {
                this.more = more;
            }

            public int getGift() {
                return gift;
            }

            public void setGift(int gift) {
                this.gift = gift;
            }

            public int getBbs() {
                return bbs;
            }

            public void setBbs(int bbs) {
                this.bbs = bbs;
            }

            public int getHelp() {
                return help;
            }

            public void setHelp(int help) {
                this.help = help;
            }

            public int getLogout() {
                return logout;
            }

            public void setLogout(int logout) {
                this.logout = logout;
            }
        }
    }
}
