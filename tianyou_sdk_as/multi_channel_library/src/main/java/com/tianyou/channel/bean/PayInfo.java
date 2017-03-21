package com.tianyou.channel.bean;

public class PayInfo {

	private String id;
	private String money;
	private String productId;
	private String productName;
	private String productDesc;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
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

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@Override
	public String toString() {
		return "PayInfo [id=" + id + ", money=" + money + ", productId="
				+ productId + ", productName=" + productName + ", productDesc="
				+ productDesc + "]";
	}

}
