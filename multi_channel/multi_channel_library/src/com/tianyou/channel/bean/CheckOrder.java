package com.tianyou.channel.bean;

public class CheckOrder {

	/**
	 * code : 200 msg : 订单创建成功 orderinfo :
	 * {"result":{"code":"200","msg":"\u9a8c\u8bc1\u6210\u529f\uff0c\u8bf7\u767b\u5f55\u6e38\u620f","uid":"2250401","channeluid":"58390015"}}
	 * 
	 */

	private ResultBean result;

	public ResultBean getResult() {
		return result;
	}

	public void setResult(ResultBean result) {
		this.result = result;
	}

	public static class ResultBean {
		private String code;
		private String msg;

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}
	}
}
