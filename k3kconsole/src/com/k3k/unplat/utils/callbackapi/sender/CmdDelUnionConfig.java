package com.k3k.unplat.utils.callbackapi.sender;

import com.k3k.unplat.utils.callbackapi.tool.Constants;

/**
 * 删除联盟配置类型,暂时不支持
 * @author Binyo
 *
 */
public class CmdDelUnionConfig extends PackerAbstract
{
	private int unionId;
	
	private final int DATA_SZE = 4;
	private final int PACK_SIZE = HEADER_SIZE+DATA_SZE;
	
	public CmdDelUnionConfig()
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
		addHeader(PACK_SIZE, 2, Constants.S_MAIN_UNION_OP, Constants.S_SUB_DEL_UNION);
		addInt(this.unionId);
		
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
	
}
