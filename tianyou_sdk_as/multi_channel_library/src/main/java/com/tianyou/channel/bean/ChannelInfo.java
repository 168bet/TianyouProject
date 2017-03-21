package com.tianyou.channel.bean;

/**
 * 渠道参数信息
 * 
 * @author itstrong
 * 
 */
public class ChannelInfo {

	private String channelId;
	private String appId;
	private String appKey;
	private String gameId;
	private String cpId;
	private String payId;
	private String buoSecret;
	private String payRsaPrivate;
	private String payRsaPublic;
	private String merchantId;
	private String appSecret;
	private String platformId;

	public String getPlatformId() {
		return platformId;
	}

	public void setPlatformId(String platformId) {
		this.platformId = platformId;
	}

	public String getChannelId() {
		return channelId;
	}

	public String getBuoSecret() {
		return buoSecret;
	}

	public void setBuoSecret(String buoSecret) {
		this.buoSecret = buoSecret;
	}

	public String getPayRsaPrivate() {
		return payRsaPrivate;
	}

	public void setPayRsaPrivate(String payRsaPrivate) {
		this.payRsaPrivate = payRsaPrivate;
	}

	public String getPayRsaPublic() {
		return payRsaPublic;
	}

	public void setPayRsaPublic(String payRsaPublic) {
		this.payRsaPublic = payRsaPublic;
	}

	public String getGameId() {
		return gameId;
	}

	public String getPayId() {
		return payId;
	}

	public void setPayId(String payId) {
		this.payId = payId;
	}

	public String getCpId() {
		return cpId;
	}

	public void setCpId(String cpId) {
		this.cpId = cpId;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	@Override
	public String toString() {
		return "ChannelInfo [channelId=" + channelId + ", appId=" + appId
				+ ", appKey=" + appKey + ", gameId=" + gameId + ", cpId="
				+ cpId + ", payId=" + payId + ", buoSecret=" + buoSecret
				+ ", payRsaPrivate=" + payRsaPrivate + ", payRsaPublic="
				+ payRsaPublic + ", merchantId=" + merchantId + ", appSecret="
				+ appSecret + "]";
	}

}
