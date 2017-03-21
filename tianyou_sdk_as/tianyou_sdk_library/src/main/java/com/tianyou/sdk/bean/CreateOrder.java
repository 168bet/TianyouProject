package com.tianyou.sdk.bean;

public class CreateOrder {
	/**
	 * result :
	 * {"code":200,"msg":"订单创建成功","orderinfo":{"roleid":"13141654","custominfo":"","userid":"47","way":"WXPAY","appid":"1021","servername":"国内Android测试服","sign":"e6c2996c5193926148fb3f595fd760af","serverid":"10281","productid":"","token":"0768281a05da9f27df178b5c39a51263","money":"10","language":"ch","signtype":"md5","productname":"asdfasf-","orderid":"gh0120170316130540eZeOWh"},"payinfo":{"dd":"ddd"}}
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
		 * code : 200 msg : 订单创建成功 orderinfo :
		 * {"roleid":"13141654","custominfo":"","userid":"47","way":"WXPAY","appid":"1021","servername":"国内Android测试服","sign":"e6c2996c5193926148fb3f595fd760af","serverid":"10281","productid":"","token":"0768281a05da9f27df178b5c39a51263","money":"10","language":"ch","signtype":"md5","productname":"asdfasf-","orderid":"gh0120170316130540eZeOWh"}
		 * payinfo : {"dd":"ddd"}
		 */

		private int code;
		private String msg;
		private OrderinfoBean orderinfo;
		private PayinfoBean payinfo;

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

		public OrderinfoBean getOrderinfo() {
			return orderinfo;
		}

		public void setOrderinfo(OrderinfoBean orderinfo) {
			this.orderinfo = orderinfo;
		}

		public PayinfoBean getPayinfo() {
			return payinfo;
		}

		public void setPayinfo(PayinfoBean payinfo) {
			this.payinfo = payinfo;
		}

		public static class OrderinfoBean {
			/**
			 * roleid : 13141654 custominfo : userid : 47 way : WXPAY appid :
			 * 1021 servername : 国内Android测试服 sign :
			 * e6c2996c5193926148fb3f595fd760af serverid : 10281 productid :
			 * token : 0768281a05da9f27df178b5c39a51263 money : 10 language : ch
			 * signtype : md5 productname : asdfasf- orderid :
			 * gh0120170316130540eZeOWh
			 */

			private String roleid;
			private String custominfo;
			private String userid;
			private String way;
			private String appid;
			private String servername;
			private String sign;
			private String serverid;
			private String productid;
			private String token;
			private String money;
			private String language;
			private String signtype;
			private String productname;
			private String orderid;

			public String getRoleid() {
				return roleid;
			}

			public void setRoleid(String roleid) {
				this.roleid = roleid;
			}

			public String getCustominfo() {
				return custominfo;
			}

			public void setCustominfo(String custominfo) {
				this.custominfo = custominfo;
			}

			public String getUserid() {
				return userid;
			}

			public void setUserid(String userid) {
				this.userid = userid;
			}

			public String getWay() {
				return way;
			}

			public void setWay(String way) {
				this.way = way;
			}

			public String getAppid() {
				return appid;
			}

			public void setAppid(String appid) {
				this.appid = appid;
			}

			public String getServername() {
				return servername;
			}

			public void setServername(String servername) {
				this.servername = servername;
			}

			public String getSign() {
				return sign;
			}

			public void setSign(String sign) {
				this.sign = sign;
			}

			public String getServerid() {
				return serverid;
			}

			public void setServerid(String serverid) {
				this.serverid = serverid;
			}

			public String getProductid() {
				return productid;
			}

			public void setProductid(String productid) {
				this.productid = productid;
			}

			public String getToken() {
				return token;
			}

			public void setToken(String token) {
				this.token = token;
			}

			public String getMoney() {
				return money;
			}

			public void setMoney(String money) {
				this.money = money;
			}

			public String getLanguage() {
				return language;
			}

			public void setLanguage(String language) {
				this.language = language;
			}

			public String getSigntype() {
				return signtype;
			}

			public void setSigntype(String signtype) {
				this.signtype = signtype;
			}

			public String getProductname() {
				return productname;
			}

			public void setProductname(String productname) {
				this.productname = productname;
			}

			public String getOrderid() {
				return orderid;
			}

			public void setOrderid(String orderid) {
				this.orderid = orderid;
			}
		}

		public static class PayinfoBean {
			/**
			 * dd : ddd
			 */

			private String dd;
			private String SELLER;
			private String PARTNER;
			private String RSA_PRIVATE;
			private String RSA_PUBLIC;
			private String tnnumber;
			private String imgstr;
			private String qqmember;
			private String GGproduct_id;

			public String getDd() {
				return dd;
			}

			public void setDd(String dd) {
				this.dd = dd;
			}

			public String getSELLER() {
				return SELLER;
			}

			public void setSELLER(String sELLER) {
				SELLER = sELLER;
			}

			public String getPARTNER() {
				return PARTNER;
			}

			public void setPARTNER(String pARTNER) {
				PARTNER = pARTNER;
			}

			public String getRSA_PRIVATE() {
				return RSA_PRIVATE;
			}

			public void setRSA_PRIVATE(String rSA_PRIVATE) {
				RSA_PRIVATE = rSA_PRIVATE;
			}

			public String getRSA_PUBLIC() {
				return RSA_PUBLIC;
			}

			public void setRSA_PUBLIC(String rSA_PUBLIC) {
				RSA_PUBLIC = rSA_PUBLIC;
			}

			public String getTnnumber() {
				return tnnumber;
			}

			public void setTnnumber(String tnnumber) {
				this.tnnumber = tnnumber;
			}

			public String getImgstr() {
				return imgstr;
			}

			public void setImgstr(String imgstr) {
				this.imgstr = imgstr;
			}

			public String getQqmember() {
				return qqmember;
			}

			public void setQqmember(String qqmember) {
				this.qqmember = qqmember;
			}

			public String getGGproduct_id() {
				return GGproduct_id;
			}

			public void setGGproduct_id(String gGproduct_id) {
				this.GGproduct_id = gGproduct_id;
			}
			
		}
	}

}