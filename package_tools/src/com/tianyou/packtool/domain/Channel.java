package com.tianyou.packtool.domain;

import java.io.Serializable;
import java.util.List;

public class Channel implements Serializable {

	private String channelName;
	private String channelId;
	private String id;
	private List<Channel> childChannels;
	
	public List<Channel> getChildChannels() {
		return childChannels;
	}

	public void setChildChannels(List<Channel> childChannels) {
		this.childChannels = childChannels;
	}

	public String getId(){
		return id;
	}

	public void setId(String id){
		this.id = id;
	}
	
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


	@Override
	public String toString() {
		return "Channel [channelId=" + channelId + ", channelName=" + channelName+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((channelId == null) ? 0 : channelId.hashCode());
		result = prime * result
				+ ((channelName == null) ? 0 : channelName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Channel other = (Channel) obj;
		if (channelId == null) {
			if (other.channelId != null)
				return false;
		} else if (!channelId.equals(other.channelId))
			return false;
		if (channelName == null) {
			if (other.channelName != null)
				return false;
		} else if (!channelName.equals(other.channelName))
			return false;
		return true;
	}
}
