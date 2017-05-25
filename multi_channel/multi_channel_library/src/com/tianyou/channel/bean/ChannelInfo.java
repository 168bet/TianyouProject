package com.tianyou.channel.bean;

/**
 * 渠道参数信息
 * 
 * @author itstrong
 * 
 */
public class ChannelInfo {

	//天游分配的渠道信息
	private String channelId;
	private String channelName;
	
	//渠道参数
	private String appId;
	private String appToken;
	
	//天游分配的游戏信息
	private String gameName;
	private String gameId;
	private String gameToken;
	
	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppToken() {
		return appToken;
	}

	public void setAppToken(String appToken) {
		this.appToken = appToken;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getGameToken() {
		return gameToken;
	}

	public void setGameToken(String gameToken) {
		this.gameToken = gameToken;
	}

	@Override
	public String toString() {
		return "ChannelInfo [channelId=" + channelId + ", channelName=" + channelName + ", appId=" + appId
				+ ", appToken=" + appToken + ", gameName=" + gameName + ", gameId=" + gameId + ", gameToken="
				+ gameToken + "]";
	}
}
