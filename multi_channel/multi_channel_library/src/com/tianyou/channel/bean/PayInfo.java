package com.tianyou.channel.bean;

/**
 * 游戏计费点信息
 * @author itstrong
 *
 */
public class PayInfo {

	private String id;
	private String money;
	private String productId;
	private String productName;
	private String productDesc;
	private String orderId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
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

	public String getProductDesc() {
		return productDesc;
	}

	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}

	@Override
	public String toString() {
		return "PayInfo [id=" + id + ", money=" + money + ", productId=" + productId + ", productName=" + productName
				+ ", productDesc=" + productDesc + "]";
	}
}
