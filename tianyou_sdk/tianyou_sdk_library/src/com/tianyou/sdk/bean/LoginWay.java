package com.tianyou.sdk.bean;

public class LoginWay {
	/**
	 * code : 200 msg : 登录方式已获取 custominfo :
	 * {"qq_quick":1,"wx_quick":0,"mb_quick":1,"sm_quick":1}
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
		 * qq_quick : 1 wx_quick : 0 mb_quick : 1 sm_quick : 1
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
			private int qq_quick;
			private int wx_quick;
			private int mb_quick;
			private int sm_quick;
			private int reg_quick;

			public int getQq_quick() {
				return qq_quick;
			}

			public void setQq_quick(int qq_quick) {
				this.qq_quick = qq_quick;
			}

			public int getWx_quick() {
				return wx_quick;
			}

			public void setWx_quick(int wx_quick) {
				this.wx_quick = wx_quick;
			}

			public int getMb_quick() {
				return mb_quick;
			}

			public void setMb_quick(int mb_quick) {
				this.mb_quick = mb_quick;
			}

			public int getSm_quick() {
				return sm_quick;
			}

			public void setSm_quick(int sm_quick) {
				this.sm_quick = sm_quick;
			}

			public int getReg_quick() {
				return reg_quick;
			}

			public void setReg_quick(int reg_quick) {
				this.reg_quick = reg_quick;
			}
		}
	}
}
