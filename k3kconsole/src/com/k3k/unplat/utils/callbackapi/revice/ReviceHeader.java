package com.k3k.unplat.utils.callbackapi.revice;

import com.k3k.unplat.utils.callbackapi.tool.Constants;

public class ReviceHeader extends ReviceAbstract
{
	private int size;
	private int type;
	private int mainCmd;
	private int subCmd;
	private String checkCode;

	@Override
	public boolean ParseCommd(byte[] netData)
	{
		if(null == netData) return false;
		if(netData.length < Constants.NET_DATA_HEADER_SIZE) return false;
		
		/**
		 	unsigned int		nSize;					//!< 大小
			unsigned int		nType;					//!< 类型			//1=VERIFY 2=MSG
			unsigned int		nMainCmd;			//!< 主命令
			unsigned int		nSubCmd;				//!< 子命令
			char					szCheckCode[32];	//!< API密码
		 */
		this.size = getInt(netData);
		this.type = getInt(netData);
		this.mainCmd = getInt(netData);
		this.subCmd = getInt(netData);
		this.checkCode = getString(netData, 32);
		
		return true;
	}
	
	public int getSize()
	{
		return size;
	}
	
	public int getType()
	{
		return type;
	}
	
	public int getMainCmd()
	{
		return mainCmd;
	}
	
	public int getSubCmd()
	{
		return subCmd;
	}
	
	public String getCheckCode()
	{
		return checkCode;
	}
}
