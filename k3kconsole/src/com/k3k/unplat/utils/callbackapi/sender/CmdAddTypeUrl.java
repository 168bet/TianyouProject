package com.k3k.unplat.utils.callbackapi.sender;

import com.k3k.unplat.utils.callbackapi.tool.Constants;

/**
 * 增加大类型URL
 * @author Binyo
 *
 */
public class CmdAddTypeUrl extends PackerAbstract
{

	private int unionId;
	private int typeId;
	private int sort;
	private String target;
	private String name;
	private String url;
	
	private final int DATA_SZE = 422;
	private final int PACK_SIZE = HEADER_SIZE+DATA_SZE;
	
	public CmdAddTypeUrl()
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
		 //!< 增加类型URL
		 struct CmdAddTypeUrl
		{
			unsigned int		nUnionID;
			unsigned int		nTypeID;
			unsigned int		nSort;
			char					szTarget[10];
			char					szName[100];
			char					szUrl[300];
		};
		 */
		addHeader(PACK_SIZE, 2, Constants.S_MAIN_TYPE_OP, Constants.S_SUB_ADD_TYPE_URL);
		addInt(this.unionId);
		addInt(this.typeId);
		addInt(this.sort);
		addString(this.target, 10);
		addString(this.name, 100);
		addString(this.url, 300);
		
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
	 * 设置类型ID, <br/>
	 *  取值范围为 2000 以上 3000 以下
	 * @param typeId 待设置的类型ID
	 */
	public void setTypeId(int typeId)
	{
		this.typeId = typeId;
	}

	/**
	 * 设置排序
	 * @param sort 待设置排序
	 */
	public void setSort(int sort)
	{
		this.sort = sort;
	}

	/**
	 * 设置打开方式 new 或者 plaza <br/>
	 * 		new 为新开窗口 <br/>
	 * 		plaza 在大厅内打开 <br/>
	 * @param target 待设置的参数
	 */
	public void setTarget(String target)
	{
		this.target = target;
	}

	/**
	 * 设置名称
	 * @param name 待设置的名称
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * 开启的URL地址
	 * @param url 待设置URL
	 */
	public void setUrl(String url)
	{
		this.url = url;
	}

	
	
}
