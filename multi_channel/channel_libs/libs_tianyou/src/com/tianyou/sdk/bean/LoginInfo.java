package com.tianyou.sdk.bean;

import java.io.Serializable;

public class LoginInfo {

	/**
	 * code : 200 msg : 登陆成功 userid : 9985 token :
	 * 65cc2c8205a05d7379fa3a6386f710e1 isphone : 1 registertype : phone
	 * password : 12749081
	 */
	private ResultBean result;

	public ResultBean getResult() {
		return result;
	}

	public void setResult(ResultBean result) {
		this.result = result;
	}

	@SuppressWarnings("serial")
	public static class ResultBean implements Serializable {
		private int code;
		private String msg;
		private String userid;
		private String username;
		private String nickname;
		private String token;
		private int isphone;
		private String registertype;
		private String password;
		private String verification;
		private String isperfect;
		private int istourist;
		private int isauth;
		private String iscode;
		private String mobile;

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public int getIsauth() {
			return isauth;
		}

		public void setIsauth(int isauth) {
			this.isauth = isauth;
		}

		public String getMobile() {
			return mobile;
		}

		public void setMobile(String mobile) {
			this.mobile = mobile;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public int getIstourist() {
			return istourist;
		}

		public void setIstourist(int istourist) {
			this.istourist = istourist;
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

		public String getIscode() {
			return iscode;
		}

		public void setIscode(String iscode) {
			this.iscode = iscode;
		}

		public String getNickname() {
			return nickname;
		}

		public void setNickname(String nickname) {
			this.nickname = nickname;
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

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getVerification() {
			return verification;
		}

		public void setVerification(String verification) {
			this.verification = verification;
		}

		public String getIsperfect() {
			return isperfect;
		}

		public void setIsperfect(String isperfect) {
			this.isperfect = isperfect;
		}

		@Override
		public String toString() {
			return "ResultBean [code=" + code + ", msg=" + msg + ", userid="
					+ userid + ", username=" + username + ", token=" + token
					+ ", isphone=" + isphone + ", registertype=" + registertype
					+ ", password=" + password + ", verification="
					+ verification + ", isperfect=" + isperfect + "]";
		}

	}
}
