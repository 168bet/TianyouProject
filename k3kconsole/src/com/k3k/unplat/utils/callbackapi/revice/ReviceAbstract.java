package com.k3k.unplat.utils.callbackapi.revice;

import com.k3k.unplat.utils.callbackapi.tool.NetDataTypeTransform;

public abstract class ReviceAbstract
{
	protected int m_nPoint = 0;
	
	protected NetDataTypeTransform  NetDataTool = new NetDataTypeTransform();
	
	/**
	 * 获取INT
	 * @param b
	 * @return
	 */
	public int getInt(byte[] b)
	{
		if(null == b) return 0;
		if(b.length <= 4) return 0; 
		byte[] bVal = new byte[4];
		System.arraycopy(b, m_nPoint, bVal, 0, 4);
		m_nPoint+=4;
		return NetDataTool.ByteArrayToInt(bVal);
	}
	
	/**
	 * 获取String
	 * @param b
	 * @param size
	 * @return
	 */
	public String getString(byte[] b, int size)
	{
		if(null == b) return null;
		if(b.length <= size) return null; 
		byte[] bVal = new byte[size];
		System.arraycopy(b, m_nPoint, bVal, 0, size);
		m_nPoint+=size;
		return NetDataTool.ByteArraytoString(bVal, size);
	}
	
	/**
	 * 解析命令
	 * @param netData
	 * @return
	 */
	abstract public boolean ParseCommd(byte[] netData);
}
