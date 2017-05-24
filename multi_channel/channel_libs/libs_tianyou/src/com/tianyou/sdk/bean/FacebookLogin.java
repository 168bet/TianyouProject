package com.tianyou.sdk.bean;

public class FacebookLogin {

	/**
     * code : 200
     * msg : Login succesfully
     * username : f5803449489
     * password : 78378659
     * userid : 2368838
     * token : 3806734b256c27e41ec2c6bffa26d9e7
     * isphone : 0
     * registertype : facebook
     * isperfect : 0
     * nickname : 刘奉强
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
        private String username;
        private int password;
        private String userid;
        private String token;
        private int isphone;
        private String registertype;
        private String isperfect;
        private String nickname;

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

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public int getPassword() {
            return password;
        }

        public void setPassword(int password) {
            this.password = password;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public int getIsphone() {
            return isphone;
        }

        public void setIsphone(int isphone) {
            this.isphone = isphone;
        }

        public String getRegistertype() {
            return registertype;
        }

        public void setRegistertype(String registertype) {
            this.registertype = registertype;
        }

        public String getIsperfect() {
            return isperfect;
        }

        public void setIsperfect(String isperfect) {
            this.isperfect = isperfect;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }
}
