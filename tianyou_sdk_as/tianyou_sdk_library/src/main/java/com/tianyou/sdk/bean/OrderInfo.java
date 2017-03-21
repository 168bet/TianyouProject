package com.tianyou.sdk.bean;

/**
 * Created by itstrong on 2016/7/2.
 */
public class OrderInfo {

	private String orderID;
	private String product_name;
	private String moNey;

	/** 支付宝独有 */
	private String SELLER;
	private String PARTNER;
	private String RSA_PRIVATE;
	private String RSA_PUBLIC;

	/** 银联独有 */
	private String tnnumber;

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

	public String getMoNey() {
		return moNey;
	}

	public void setMoNey(String moNey) {
		this.moNey = moNey;
	}

	public String getSELLER() {
		return SELLER;
	}

	public void setSELLER(String SELLER) {
		this.SELLER = SELLER;
	}

	public String getPARTNER() {
		return PARTNER;
	}

	public void setPARTNER(String PARTNER) {
		this.PARTNER = PARTNER;
	}

	public String getRSA_PRIVATE() {
		return RSA_PRIVATE;
	}

	public void setRSA_PRIVATE(String RSA_PRIVATE) {
		this.RSA_PRIVATE = RSA_PRIVATE;
	}

	public String getRSA_PUBLIC() {
		return RSA_PUBLIC;
	}

	public void setRSA_PUBLIC(String RSA_PUBLIC) {
		this.RSA_PUBLIC = RSA_PUBLIC;
	}

	public String getTnnumber() {
		return tnnumber;
	}

	public void setTnnumber(String tnnumber) {
		this.tnnumber = tnnumber;
	}

	@Override
	public String toString() {
		return "OrderInfo [orderID=" + orderID + ", product_name="
				+ product_name + ", moNey=" + moNey + ", SELLER=" + SELLER
				+ ", PARTNER=" + PARTNER + ", RSA_PRIVATE=" + RSA_PRIVATE
				+ ", RSA_PUBLIC=" + RSA_PUBLIC + ", tnnumber=" + tnnumber + "]";
	}
}
