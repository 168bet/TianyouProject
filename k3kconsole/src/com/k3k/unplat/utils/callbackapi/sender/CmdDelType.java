package com.k3k.unplat.utils.callbackapi.sender;

import com.k3k.unplat.utils.callbackapi.tool.Constants;

/**
 * 删除大类型
 * @author Binyo
 *
 */
public class CmdDelType extends PackerAbstract
{
	private int unionId;
	private int typeId;
	
	private final int DATA_SZE = 8;
	private final int PACK_SIZE = HEADER_SIZE+DATA_SZE;
	
	public CmdDelType()
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
	 * //!< 删除类型
		struct CmdDelType
		{
			unsigned int		nUnionID;
			unsigned int		nTypeID;
		};	
	 */
		addHeader(PACK_SIZE, 2, Constants.S_MAIN_TYPE_OP, Constants.S_SUB_DEL_TYPE);
		addInt(unionId);
		addInt(typeId);
		return m_ByteArrayData;
	}
	
	/**
	 * 设置联盟ID
	 * @param unionId 待设置到联盟ID
	 */
	public void setUnionId(int unionId)
	{
		this.unionId = unionId;
	}
	
	/**
	 * 设置类型ID
	 * @param typeId 待设置类型ID
	 */
	public void setTypeId(int typeId)
	{
		this.typeId = typeId;
	}

}
