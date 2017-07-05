package com.k3k.unplat.utils.callbackapi.sender;

import com.k3k.unplat.utils.callbackapi.tool.Constants;


/**
 * 验证数据包.
 * @author Binyo
 *
 */
public class CmdVerify extends PackerAbstract
{
	/**
	 * 数据包大小
	 */
	private final int DATA_SIZE = 32;
	
	/**
	 * 数据包大小
	 */
	private final int PACKE_SIZE = HEADER_SIZE+DATA_SIZE;
	
	
	/**
	 *  构造函数
	 */
	public CmdVerify()
	{
		
	}
	
	@Override
	public boolean isLoginServer()
	{
		return true;
	}
	
	
	/**
	 * 组建验证包
	 * @param nMainCmd
	 * @param nSubCmd
	 * @param strPass
	 */
	public CmdVerify(String strPass)
	{
		createPacker(PACKE_SIZE);
		strPass+='\0';

		byte[] bSize = m_DataTypeTransform.IntToByteArray(PACKE_SIZE);
		byte[] bType = m_DataTypeTransform.IntToByteArray(1);
		byte[] bMainCmd = m_DataTypeTransform.IntToByteArray(Constants.S_MAIN_VERIFY);
		byte[] bSubCmd = m_DataTypeTransform.IntToByteArray(Constants.S_SUB_VERIFY);
		
		int nDestSize = 0;
		System.arraycopy(bSize,0, m_ByteArrayData, nDestSize, bSize.length);  
		nDestSize+=bSize.length;
		System.arraycopy(bType,0, m_ByteArrayData, nDestSize, bType.length);  
		nDestSize+=bType.length;
		System.arraycopy(bMainCmd,0, m_ByteArrayData, nDestSize, bMainCmd.length);  
		nDestSize+=bMainCmd.length;
		System.arraycopy(bSubCmd,0, m_ByteArrayData, nDestSize, bSubCmd.length);  
		nDestSize+=bSubCmd.length;
		
		byte[] Strbyte = m_DataTypeTransform.StringToByteArray(strPass);  
		System.arraycopy(Strbyte,0, m_ByteArrayData, nDestSize, Strbyte.length);  
	}


	/**
	  * 把packer返回为字节包,用于发送
	 * @return 返回字节数据
	 */
	public byte[] getByteArrayData()
	 {  
	        return m_ByteArrayData;  
	 }  	
	
}
