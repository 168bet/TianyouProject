package com.tianyou.sdk.bean;

public class PayInfo {

	private String gameName = "";
	private String roleId = "";
	private String serverId = "";
	private String serverName = "";
	private String productId = "";
	private String productName = "";
	private String productDesc = "";
	private String customInfo = "";
	private String sign = "";
	private String signType = "";
	private int scale; // 支付比例
	private String currency = ""; // 游戏货币名

	private String userName = "";
	private String money = ""; // 支付金额 格式 2(元)
	private String payMoney = ""; // 支付金额 格式 20元宝
	private String orderId = "";

	private String SELLER = "";
	private String PARTNER = "";
	private String RSA_PRIVATE = "";
	private String RSA_PUBLIC = "";

	private String imgstr;
	private String qqmember;
	private String tnnumber;
	private String googleProductID;

	public String getGoogleProductID() {
		return googleProductID;
	}

	public void setGoogleProductID(String googleProductID) {
		this.googleProductID = googleProductID;
	}

	public String getImgstr() {
		return imgstr;
	}

	public void setImgstr(String imgstr) {
		this.imgstr = imgstr;
	}

	public int getScale() {
		return scale;
	}

	public String getProductDesc() {
		return productDesc;
	}

	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getQqmember() {
		return qqmember;
	}

	public void setQqmember(String qqmember) {
		this.qqmember = qqmember;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getCustomInfo() {
		return customInfo;
	}

	public void setCustomInfo(String customInfo) {
		this.customInfo = customInfo;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(String payMoney) {
		this.payMoney = payMoney;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
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

	@Override
	public String toString() {
		return "PayParamInfo [gameName=" + gameName + ", roleId=" + roleId + ", serverId=" + serverId + ", serverName="
				+ serverName + ", productId=" + productId + ", productName=" + productName + ", customInfo="
				+ customInfo + ", sign=" + sign + ", signType=" + signType + ", userName=" + userName + ", money="
				+ money + ", payMoney=" + payMoney + ", orderId=" + orderId + ", SELLER=" + SELLER + ", PARTNER="
				+ PARTNER + ", RSA_PRIVATE=" + RSA_PRIVATE + ", RSA_PUBLIC=" + RSA_PUBLIC + ", tnnumber=" + tnnumber
				+ "]";
	}

}
