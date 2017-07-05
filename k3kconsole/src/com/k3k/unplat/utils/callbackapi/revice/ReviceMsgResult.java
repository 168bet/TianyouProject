package com.k3k.unplat.utils.callbackapi.revice;


/**
 * @author Binyo
 * 工具
 */
public class ReviceMsgResult extends ReviceAbstract
{
	private boolean result;
	private String messages;

	@Override
	public boolean ParseCommd(byte[] netData)
	{
		if(null == netData) return false;
		if(netData.length < 200) return false;
		
	/*
	struct tagMsgResult
	{
		int		bResult;						//!< 结果
		char	szMessage[200];		//!< 结果描述
	};
	*/
		
		int nResult = getInt(netData);
		if(nResult == 1) this.result = true;
		else this.result = false;
		
		this.messages = getString(netData, 200);
		
		return true;
	}

	public boolean getResult()
	{
		return result;
	}

	public String getMessages()
	{
		return messages;
	}
	
}
