package com.k3k.unplat.utils.callbackapi.sender;

import com.k3k.unplat.utils.callbackapi.tool.Constants;

/**
 * 删除游戏类型URL
 * @author Binyo
 *
 */
public class CmdDelKindUrl extends PackerAbstract
{
	private int unionId;
	private int kindId;
	private int serverId;
	
	private final int DATA_SZE = 12;
	private final int PACK_SIZE = HEADER_SIZE+DATA_SZE;
	
	public CmdDelKindUrl()
	{
		createPacker(PACK_SIZE);
	}
	
	@Override
	public boolean isLoginServer()
	{
		return false;
	}
	
	
	@Override
	public byte[] getByteArrayData()
	{
		
	/*
		//!< 删除Kind Url
		struct CmdDelKindUrl
		{
			unsigned int		nUnionID;				//!< 联盟ID
			unsigned int		nKindID;				//!< 游戏ID
			unsigned int		nServerID;				//!< 服务编号
		};
	 */
		addHeader(PACK_SIZE, 2, Constants.S_MAIN_KIND_OP, Constants.S_SUB_DEL_KIND_URL);
		addInt(this.unionId);
		addInt(this.kindId);
		addInt(this.serverId);
		return m_ByteArrayData;
	}

	/**
	 * 设置联盟ID
	 * @param unionId
	 */
	public void setUnionId(int unionId)
	{
		this.unionId = unionId;
	}

	/**
	 * 设置KindID
	 * @param kindId
	 */
	public void setKindId(int kindId)
	{
		this.kindId = kindId;
	}

	/**
	 * 设置ServerID
	 * @param serverId
	 */
	public void setServerId(int serverId)
	{
		this.serverId = serverId;
	}
	
	
	
}
