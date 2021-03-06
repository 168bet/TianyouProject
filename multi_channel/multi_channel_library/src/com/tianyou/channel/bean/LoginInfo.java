package com.tianyou.channel.bean;

public class LoginInfo {

	private String channelUserId; 	// 渠道用户id
	private String playId; 	// 渠道用户id
	private String tianyouUserId; 	// 天游用户id
	private String hanfengUid; 		// 汉风uid
	private String userToken;
	private String nickname;
	private String isGuest;
	private boolean isOverseas; // 是否是海外渠道
	private String yijieAppId;	// 易接appid

	public String getYijieAppId() {
		return yijieAppId;
	}

	public void setYijieAppId(String yijieAppId) {
		this.yijieAppId = yijieAppId;
	}

	public String getPlayId() {
		return playId;
	}

	public void setPlayId(String playId) {
		this.playId = playId;
	}

	public String getChannelUserId() {
		return channelUserId;
	}

	public String getHanfengUid() {
		return hanfengUid;
	}

	public void setHanfengUid(String hanfengUid) {
		this.hanfengUid = hanfengUid;
	}

	public void setChannelUserId(String channelUserId) {
		this.channelUserId = channelUserId;
	}

	public String getTianyouUserId() {
		return tianyouUserId;
	}

	public String getIsGuest() {
		return isGuest;
	}

	public void setIsGuest(String isGuest) {
		this.isGuest = isGuest;
	}

	public void setOverseas(boolean isOverseas) {
		this.isOverseas = isOverseas;
	}

	public void setTianyouUserId(String tianyouUserId) {
		this.tianyouUserId = tianyouUserId;
	}

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public boolean getIsOverseas() {
		return isOverseas;
	}

	public void setIsOverseas(boolean isOverseas) {
		this.isOverseas = isOverseas;
	}

	@Override
	public String toString() {
		return "LoginParam [channelUserId=" + channelUserId
				+ ", tianyouUserId=" + tianyouUserId + ", userToken="
				+ userToken + ", nickname=" + nickname + ", isOverseas="
				+ isOverseas + "]";
	}

}
