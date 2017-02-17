package com.tianyou.sdk.bean;

/**
 * 手机验证码
 * @author itstrong
 *
 */
public class PhoneCode {

	/**
     * code : 200
     * msg : 验证码已发送
     * mobile_code : 416209
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
        private String mobile_code;

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

        public String getMobile_code() {
            return mobile_code;
        }

        public void setMobile_code(String mobile_code) {
            this.mobile_code = mobile_code;
        }
    }
}
