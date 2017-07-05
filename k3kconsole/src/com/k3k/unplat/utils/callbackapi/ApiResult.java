package com.k3k.unplat.utils.callbackapi;

/**
 * @author Binyo
 * SOCKET 返回数据
 */
public class ApiResult
{
	private boolean result;
	private String messages;
	
	/**
	 * 操作是否成功,当操作结果返回为失败的时候 getMessages 则有对应的消息
	 * @return 成功返回true
	 */
	public boolean isResult()
	{
		return result;
	}
	
	public void setResult(boolean result)
	{
		this.result = result;
	}
	
	/**
	 * 返回结果描述,成功只会返回success
	 * @return 消息描述内容
	 */ 
	public String getMessages()
	{
		return messages;
	}
	
	public void setMessages(String messages)
	{
		this.messages = messages;
	}
	
	
}
