package com.k3k.unplat.utils.callbackapi.sender;

import com.k3k.unplat.utils.callbackapi.tool.Constants;

/**
 * 增加大类型
 * @author Binyo
 *
 */
public class CmdAddType extends PackerAbstract
{
	private int unionId;
	private int typeId;
	private int sort;
	private String name;
	
	private final int DATA_SZE = 62;
	private final int PACK_SIZE = HEADER_SIZE+DATA_SZE;
	
	public CmdAddType()
	{
		createPacker(PACK_SIZE);
	}
	
	@Override
	public boolean isLoginServer()
	{
		return true;
	}
	
	
	/**
	 * 设置联盟ID
	 * @param unionId 联盟ID
	 */
	public void setUnionId(int unionId)
	{
		this.unionId = unionId;
	}
	
	/**
	 * 设置类型ID
	 * @param typeId 类型ID
	 */
	public void setTypeId(int typeId)
	{
		this.typeId = typeId;
	}
	
	/**
	 * 设置排序ID
	 * @param sort
	 */
	public void setSort(int sort)
	{
		this.sort = sort;
	}
	
	/**
	 * 设置名称
	 * @param name
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public byte[] getByteArrayData()
	{
		/*
		 * //!< 增加类型
			struct tagCmdAddType
			{
				unsigned int		nUnionID;
				unsigned int		nTypeID;
				unsigned int		nSort;
				char					szName[50];
			};
		 */
		addHeader(PACK_SIZE, 2, Constants.S_MAIN_TYPE_OP, Constants.S_SUB_ADD_TYPE);
		addInt(unionId);
		addInt(typeId);
		addInt(sort);
		addString(name,50);
		
		return m_ByteArrayData;
	} 


	
}
