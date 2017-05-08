package com.tianyou.sdk.bean;

public class Identifi {

	/**
     * result : {"code":200,"msg":"完善成功","userid":"2237638","username":"2139356457","isphone":1,"token":"d41d8cd98f00b204e9800998ecf8427e","password":null,"nickname":"2139356457","registertype":"qq"}
     */

    private ResultBean result;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * code : 200
         * msg : 完善成功
         * userid : 2237638
         * username : 2139356457
         * isphone : 1
         * token : d41d8cd98f00b204e9800998ecf8427e
         * password : null
         * nickname : 2139356457
         * registertype : qq
         */

        private int code;
        private String msg;
        private String userid;
        private String username;
        private int isphone;
        private String token;
        private Object password;
        private String nickname;
        private String registertype;

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

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public int getIsphone() {
            return isphone;
        }

        public void setIsphone(int isphone) {
            this.isphone = isphone;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public Object getPassword() {
            return password;
        }

        public void setPassword(Object password) {
            this.password = password;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getRegistertype() {
            return registertype;
        }

        public void setRegistertype(String registertype) {
            this.registertype = registertype;
        }
    }
}
