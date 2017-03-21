package com.tianyou.sdk.base;

/**
 * Created by itstrong on 2017/1/5.
 */
public class FloatControl {

	/**
	 * result :
	 * {"code":200,"msg":"登录方式已获取","custominfo":{"lockstatus":1,"account":{"status":1,"url":"www.baidu.com"},"more":{"status":1,"url":"www.baidu.com"},"gift":{"status":1,"url":"www.baidu.com"},"bbs":{"status":1,"url":"www.baidu.com"},"help":{"status":1,"url":"www.baidu.com"},"logout":{"status":1,"url":"www.baidu.com"}}}
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
		 * code : 200 msg : 登录方式已获取 custominfo :
		 * {"lockstatus":1,"account":{"status":1,"url":"www.baidu.com"},"more":{"status":1,"url":"www.baidu.com"},"gift":{"status":1,"url":"www.baidu.com"},"bbs":{"status":1,"url":"www.baidu.com"},"help":{"status":1,"url":"www.baidu.com"},"logout":{"status":1,"url":"www.baidu.com"}}
		 */

		private int code;
		private String msg;
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
			 * lockstatus : 1 account : {"status":1,"url":"www.baidu.com"} more
			 * : {"status":1,"url":"www.baidu.com"} gift :
			 * {"status":1,"url":"www.baidu.com"} bbs :
			 * {"status":1,"url":"www.baidu.com"} help :
			 * {"status":1,"url":"www.baidu.com"} logout :
			 * {"status":1,"url":"www.baidu.com"}
			 */

			private int lockstatus;
			private AccountBean account;
			private MoreBean more;
			private GiftBean gift;
			private BbsBean bbs;
			private HelpBean help;
			private LogoutBean logout;

			public int getLockstatus() {
				return lockstatus;
			}

			public void setLockstatus(int lockstatus) {
				this.lockstatus = lockstatus;
			}

			public AccountBean getAccount() {
				return account;
			}

			public void setAccount(AccountBean account) {
				this.account = account;
			}

			public MoreBean getMore() {
				return more;
			}

			public void setMore(MoreBean more) {
				this.more = more;
			}

			public GiftBean getGift() {
				return gift;
			}

			public void setGift(GiftBean gift) {
				this.gift = gift;
			}

			public BbsBean getBbs() {
				return bbs;
			}

			public void setBbs(BbsBean bbs) {
				this.bbs = bbs;
			}

			public HelpBean getHelp() {
				return help;
			}

			public void setHelp(HelpBean help) {
				this.help = help;
			}

			public LogoutBean getLogout() {
				return logout;
			}

			public void setLogout(LogoutBean logout) {
				this.logout = logout;
			}

			public static class AccountBean {
				/**
				 * status : 1 url : www.baidu.com
				 */

				private int status;
				private String url;

				public int getStatus() {
					return status;
				}

				public void setStatus(int status) {
					this.status = status;
				}

				public String getUrl() {
					return url;
				}

				public void setUrl(String url) {
					this.url = url;
				}
			}

			public static class MoreBean {
				/**
				 * status : 1 url : www.baidu.com
				 */

				private int status;
				private String url;

				public int getStatus() {
					return status;
				}

				public void setStatus(int status) {
					this.status = status;
				}

				public String getUrl() {
					return url;
				}

				public void setUrl(String url) {
					this.url = url;
				}
			}

			public static class GiftBean {
				/**
				 * status : 1 url : www.baidu.com
				 */

				private int status;
				private String url;

				public int getStatus() {
					return status;
				}

				public void setStatus(int status) {
					this.status = status;
				}

				public String getUrl() {
					return url;
				}

				public void setUrl(String url) {
					this.url = url;
				}
			}

			public static class BbsBean {
				/**
				 * status : 1 url : www.baidu.com
				 */

				private int status;
				private String url;

				public int getStatus() {
					return status;
				}

				public void setStatus(int status) {
					this.status = status;
				}

				public String getUrl() {
					return url;
				}

				public void setUrl(String url) {
					this.url = url;
				}
			}

			public static class HelpBean {
				/**
				 * status : 1 url : www.baidu.com
				 */

				private int status;
				private String url;

				public int getStatus() {
					return status;
				}

				public void setStatus(int status) {
					this.status = status;
				}

				public String getUrl() {
					return url;
				}

				public void setUrl(String url) {
					this.url = url;
				}
			}

			public static class LogoutBean {
				/**
				 * status : 1 url : www.baidu.com
				 */

				private int status;
				private String url;

				public int getStatus() {
					return status;
				}

				public void setStatus(int status) {
					this.status = status;
				}

				public String getUrl() {
					return url;
				}

				public void setUrl(String url) {
					this.url = url;
				}
			}
		}
	}

}
