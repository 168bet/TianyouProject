package com.k3k.unplat.utils.callbackapi.sender;

import com.k3k.unplat.utils.callbackapi.tool.Constants;

/**
 * 增加游戏类型
 * @author Binyo
 *
 */
public class CmdAddKind extends PackerAbstract
{

	private int unionId;
	private int typeId;
	private int kindId;
	private int sort;
	private int version;
	private String name;
	
	private final int DATA_SZE = 120;
	private final int PACK_SIZE = HEADER_SIZE+DATA_SZE;
	
	public CmdAddKind()
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
		 struct CmdAddKind
		{
			unsigned int		nUnionID;
			unsigned int		nTypeID;
			unsigned int		nSort;
			unsigned int		nKindID;
			unsigned int		nVersion;
			char					szName[100];
		};
		 */
		addHeader(PACK_SIZE, 2, Constants.S_MAIN_KIND_OP, Constants.S_SUB_ADD_KIND);
		addInt(this.unionId);
		addInt(this.typeId);
		addInt(this.sort);
		addInt(this.kindId);
		addInt(this.version);
		addString(this.name, 100);
		
		return m_ByteArrayData;
	}

	/**
	 * 设置联盟ID
	 * @param unionId 待设置联盟ID
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
	 * 游戏ID
	 * @param kindId 待设置的游戏ID
	 */
	public void setKindId(int kindId)
	{
		this.kindId = kindId;
	}

	/**
	 * 排序
	 * @param sort 待设置的排序
	 */
	public void setSort(int sort)
	{
		this.sort = sort;
	}

	/**
	 * 保留参数 请设置为0
	 * @param version
	 */
	public void setVersion(int version)
	{
		this.version = version;
	}

	/**
	 * 游戏名称
	 * @param name 待设置的游戏名称
	 */
	public void setName(String name)
	{
		this.name = name;
	}	
}
