package com.tianyou.channel.bean;

public class PayParam {

	private String payCode;
	private String customInfo;
	private String amount;

	public String getPayCode() {
		return payCode;
	}

	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}

	public String getCustomInfo() {
		return customInfo;
	}

	public void setCustomInfo(String customInfo) {
		this.customInfo = customInfo;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return "PayParam [payCode=" + payCode + ", customInfo=" + customInfo + ", amount=" + amount + "]";
	}
}
