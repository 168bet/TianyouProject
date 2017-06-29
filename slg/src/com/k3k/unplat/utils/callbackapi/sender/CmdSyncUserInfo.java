package com.k3k.unplat.utils.callbackapi.sender;

import com.k3k.unplat.utils.callbackapi.tool.Constants;

public class CmdSyncUserInfo extends PackerAbstract{
	
	private int userId;//同步 用户ID
	private int type;//信息类型
	
	private final int DATA_SIZE = 8;
	private final int PACK_SIZE = HEADER_SIZE + DATA_SIZE;
	
	public CmdSyncUserInfo(){
		createPacker(PACK_SIZE);
	}

	@Override
	public byte[] getByteArrayData() {
		// TODO Auto-generated method stub
		addHeader(PACK_SIZE, 2, Constants.S_MAIN_SYNC_USER_INFO, Constants.S_SUB_SYNC_USER_INFO);
		addInt(this.userId);
		addInt(this.type);
		return m_ByteArrayData;
	}

	@Override
	public boolean isLoginServer() {
		// TODO Auto-generated method stub
		return true;
	}
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public static void main(String[] args) {
		long l = 0x01;
		System.out.println(l);
	}
}
