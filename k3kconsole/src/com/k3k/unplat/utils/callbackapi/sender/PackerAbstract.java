package com.k3k.unplat.utils.callbackapi.sender;

import com.k3k.unplat.utils.callbackapi.tool.Constants;
import com.k3k.unplat.utils.callbackapi.tool.NetDataTypeTransform;

public  abstract  class PackerAbstract
{
	/**
	 * 包头大小
	 */
	public final int	 HEADER_SIZE = 48;
	
	/**
	 * 字节序转换
	 */
	public NetDataTypeTransform m_DataTypeTransform=new NetDataTypeTransform();
	
	/**
	 * 当前复制位置
	 */
	public int m_nNowPoint = 0;
	
	/**
	 * 缓冲数据
	 */
	public byte[] m_ByteArrayData	= null;
	
	 /**
	 * @param 数据包大小
	 */
	public void createPacker(int nSize)
	{
		m_ByteArrayData = new byte[nSize];
	}	
	
	/**
	 * 添加头部消息
	 * @param nPackSize 包大小 头大小+数据大小
	 * @param nType 类型 1=验证 2 消息
	 * @param nMainCmd 主ID
	 * @param nSubCmd 子ID
	 */
	void addHeader(int nPackSize,int nType,int nMainCmd,int nSubCmd)
	{
		addInt(nPackSize);
		addInt(nType);
		addInt(nMainCmd);
		addInt(nSubCmd);
		addString(Constants.NET_API_PASSWORD,32);
	}
	
	/**
	 * 添加int类型数据
	 * @param nVal 待添加到数据
	 */
	void addInt(int nVal)
	{
		byte[] bVal = m_DataTypeTransform.IntToByteArray(nVal);
		System.arraycopy(bVal,0, m_ByteArrayData, m_nNowPoint, bVal.length);  
		m_nNowPoint+=bVal.length;
	}
	
//	/**
//	 * 添加long类型数据
//	 * @param lVal 待添加数据
//	 */
//	void addLong(long lVal) {
//		byte[] b = m_DataTypeTransform.LongToByteArray(lVal);
//		System.out.println(b.length);
//		System.out.println(m_ByteArrayData.length);
//		System.out.println(m_nNowPoint);
//		System.arraycopy(b, 0, m_ByteArrayData, m_nNowPoint, b.length);
//		m_nNowPoint += b.length;
//	}
	
	/**
	 * 增加字符串类型数据
	 * @param strVal 待添加数据
	 * @param nSize 字符串大小,针对实际发送数据, 如果增加 " java"字符串 如果是 nSize==10 则内存表述为 j|a|v|a|\0|0|0|0|0|0
	 */
	void addString(String strVal,int nSize)
	{
		strVal+='\0';
		byte[] bVal = m_DataTypeTransform.StringToByteArray(strVal);  
		System.arraycopy(bVal,0, m_ByteArrayData, m_nNowPoint, bVal.length);  
		m_nNowPoint+=nSize;
	}
	
	/**
	 * 获取byte数据
	 * @return byte数据
	 */
	abstract public byte[] getByteArrayData();
	
	/**
	 * 查询是否为登录服务器命令
	 * @return 为登录服务器命令返回true,否则返回false,为List服务器
	 */
	abstract public boolean isLoginServer();
}
