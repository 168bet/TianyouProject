package com.k3k.unplat.utils.callbackapi.sender;

import com.k3k.unplat.utils.callbackapi.tool.Constants;

/**
 * 删除游戏类型
 * @author Binyo
 *
 */
public class CmdDelKind extends PackerAbstract
{
	private int unionId;
	private int typeId;
	private int kindId;
	
	private final int DATA_SZE = 12;
	private final int PACK_SIZE = HEADER_SIZE+DATA_SZE;
	
	public CmdDelKind()
	{
		createPacker(PACK_SIZE);
	}
	
	@Override
	public boolean isLoginServer()
	{
		return true;
	}
	
	
	@Override
	public byte[] getByteArrayData()
	{
		/*
			//!< 删除游戏类型
			struct CmdDelKind
			{
				unsigned int		nUnionID;
				unsigned int		nTypeID;
				unsigned int		nKindID;
			};
		 */
		addHeader(PACK_SIZE, 2, Constants.S_MAIN_KIND_OP, Constants.S_SUB_DEL_KIND);
		addInt(unionId);
		addInt(typeId);
		addInt(this.kindId);
		
		return m_ByteArrayData;
	}

	/**
	 * 设置联盟ID
	 * @param unionId 待设置的联盟ID
	 */
	public void setUnionId(int unionId)
	{
		this.unionId = unionId;
	}

	/**
	 * 设置类型ID 
	 * @param typeId 待设置的类型ID
	 */
	public void setTypeId(int typeId)
	{
		this.typeId = typeId;
	}

	/**
	 * 设置游戏ID
	 * @param kindId 待设置的游戏ID
	 */
	public void setKindId(int kindId)
	{
		this.kindId = kindId;
	}

	
	
}
