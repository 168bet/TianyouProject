package com.tianyou.channel.bean;

/**
 * 用户角色信息
 * 
 * @author itstrong
 * 
 */
public class RoleInfo {

	private String roleId;
	private String roleName;
	private String serverId;
	private String serverName;
	private String gameName;
	private String balance; // 用户余额
	private String vipLevel;
	private String roleLevel;
	private String party; // 工会，帮派
	private String profession; // 职业
	private String createTime; // 角色创建时间
	private String roleLevelUpTime;// 角色升级时间

	public String getRoleLevelUpTime() {
		return roleLevelUpTime;
	}

	public void setRoleLevelUpTime(String roleLevelUpTime) {
		this.roleLevelUpTime = roleLevelUpTime;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
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

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(String vipLevel) {
		this.vipLevel = vipLevel;
	}

	public String getRoleLevel() {
		return roleLevel;
	}

	public void setRoleLevel(String roleLevel) {
		this.roleLevel = roleLevel;
	}

	public String getParty() {
		return party;
	}

	public void setParty(String party) {
		this.party = party;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	@Override
	public String toString() {
		return "RoleInfo [roleId=" + roleId + ", roleName=" + roleName
				+ ", serverId=" + serverId + ", serverName=" + serverName
				+ ", gameName=" + gameName + ", balance=" + balance
				+ ", vipLevel=" + vipLevel + ", roleLevel=" + roleLevel
				+ ", party=" + party + ", profession=" + profession
				+ ", createTime=" + createTime + ", roleLevelUpTime="
				+ roleLevelUpTime + "]";
	}

}
