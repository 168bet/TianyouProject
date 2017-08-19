package com.tianyou.channel.bean;

public class OrderInfo {

	/**
     * code : 200
     * msg : 订单创建成功
     * orderinfo : {"moNey":"8","serverName":"国内Android测试服","roleId":"13141654","appID":"1021","productName":"80钻石","userId":"2251210","productDesc":"购买此商品获得80钻石","productId":"tyxmulti_qihoo_02","serverID":"99990","promotion":"ty006","customInfo":"13465897","create_time":1480338010,"create_date":"2016-11-28 21:00:10","orderID":"2016112821001048922","product_name":"龙神之光-国内Android测试服","signinfo":"058cab36b1c4168e755e7e465966fe5d","total_price":""}
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
        /**
         * moNey : 8
         * serverName : 国内Android测试服
         * roleId : 13141654
         * appID : 1021
         * productName : 80钻石
         * userId : 2251210
         * productDesc : 购买此商品获得80钻石
         * productId : tyxmulti_qihoo_02
         * serverID : 99990
         * promotion : ty006
         * customInfo : 13465897
         * create_time : 1480338010
         * create_date : 2016-11-28 21:00:10
         * orderID : 2016112821001048922
         * product_name : 龙神之光-国内Android测试服
         * signinfo : 058cab36b1c4168e755e7e465966fe5d
         * total_price : 
         */

        private OrderinfoBean orderinfo;

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

        public OrderinfoBean getOrderinfo() {
            return orderinfo;
        }

        public void setOrderinfo(OrderinfoBean orderinfo) {
            this.orderinfo = orderinfo;
        }

        public static class OrderinfoBean {
            private String moNey;
            private String serverName;
            private String roleId;
            private String appID;
            private String productName;
            private String userId;
            private String channeluid;
            private String productDesc;
            private String productId;
            private String serverID;
            private String promotion;
            private String customInfo;
            private int create_time;
            private String create_date;
            private String orderID;
            private String product_name;
            private String signinfo;
            private String total_price;
            private String notifyurl;
            private int ratio;
            private String out_order_no;
            private String api_key;
            private String submit_time;
            private String deadline;
            private String orderNumber;
            private String accessKey;
            private String productNum;
            private String waresid;
            private String currency;
            private String rate;
            private String sign;
            
            public String getSign() {
				return sign;
			}

			public void setSign(String sign) {
				this.sign = sign;
			}

			public String getChanneluid() {
				return channeluid;
			}

			public void setChanneluid(String channeluid) {
				this.channeluid = channeluid;
			}

			public String getCurrency() {
				return currency;
			}

			public void setCurrency(String currency) {
				this.currency = currency;
			}

			public String getRate() {
				return rate;
			}

			public void setRate(String rate) {
				this.rate = rate;
			}

			public String getProductNum() {
				return productNum;
			}

			public void setProductNum(String productNum) {
				this.productNum = productNum;
			}

			public String getWaresid() {
				return waresid;
			}

			public void setWaresid(String waresid) {
				this.waresid = waresid;
			}

			public String getAccessKey() {
				return accessKey;
			}

			public void setAccessKey(String accessKey) {
				this.accessKey = accessKey;
			}

			public String getOrderNumber() {
				return orderNumber;
			}

			public void setOrderNumber(String orderNumber) {
				this.orderNumber = orderNumber;
			}

			public String getDeadline() {
				return deadline;
			}

			public void setDeadline(String deadline) {
				this.deadline = deadline;
			}

			public String getOut_order_no() {
				return out_order_no;
			}

			public void setOut_order_no(String out_order_no) {
				this.out_order_no = out_order_no;
			}

			public String getApi_key() {
				return api_key;
			}

			public void setApi_key(String api_key) {
				this.api_key = api_key;
			}

			public String getSubmit_time() {
				return submit_time;
			}

			public void setSubmit_time(String submit_time) {
				this.submit_time = submit_time;
			}

			public int getRatio() {
				return ratio;
			}

			public void setRatio(int ratio) {
				this.ratio = ratio;
			}

			public String getNotifyurl() {
				return notifyurl;
			}

			public void setNotifyurl(String notifyurl) {
				this.notifyurl = notifyurl;
			}

			public String getMoNey() {
                return moNey;
            }

            public void setMoNey(String moNey) {
                this.moNey = moNey;
            }

            public String getServerName() {
                return serverName;
            }

            public void setServerName(String serverName) {
                this.serverName = serverName;
            }

            public String getRoleId() {
                return roleId;
            }

            public void setRoleId(String roleId) {
                this.roleId = roleId;
            }

            public String getAppID() {
                return appID;
            }

            public void setAppID(String appID) {
                this.appID = appID;
            }

            public String getProductName() {
                return productName;
            }

            public void setProductName(String productName) {
                this.productName = productName;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getProductDesc() {
                return productDesc;
            }

            public void setProductDesc(String productDesc) {
                this.productDesc = productDesc;
            }

            public String getProductId() {
                return productId;
            }

            public void setProductId(String productId) {
                this.productId = productId;
            }

            public String getServerID() {
                return serverID;
            }

            public void setServerID(String serverID) {
                this.serverID = serverID;
            }

            public String getPromotion() {
                return promotion;
            }

            public void setPromotion(String promotion) {
                this.promotion = promotion;
            }

            public String getCustomInfo() {
                return customInfo;
            }

            public void setCustomInfo(String customInfo) {
                this.customInfo = customInfo;
            }

            public int getCreate_time() {
                return create_time;
            }

            public void setCreate_time(int create_time) {
                this.create_time = create_time;
            }

            public String getCreate_date() {
                return create_date;
            }

            public void setCreate_date(String create_date) {
                this.create_date = create_date;
            }

            public String getOrderID() {
                return orderID;
            }

            public void setOrderID(String orderID) {
                this.orderID = orderID;
            }

            public String getProduct_name() {
                return product_name;
            }

            public void setProduct_name(String product_name) {
                this.product_name = product_name;
            }

            public String getSigninfo() {
                return signinfo;
            }

            public void setSigninfo(String signinfo) {
                this.signinfo = signinfo;
            }

            public String getTotal_price() {
                return total_price;
            }

            public void setTotal_price(String total_price) {
                this.total_price = total_price;
            }
        }
    }

}
