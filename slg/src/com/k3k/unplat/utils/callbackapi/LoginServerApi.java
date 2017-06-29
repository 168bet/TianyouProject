package com.k3k.unplat.utils.callbackapi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Date;

import com.k3k.unplat.utils.callbackapi.revice.ReviceHeader;
import com.k3k.unplat.utils.callbackapi.revice.ReviceMsgResult;
import com.k3k.unplat.utils.callbackapi.sender.PackerAbstract;
import com.k3k.unplat.utils.callbackapi.tool.Constants;

public class LoginServerApi
{
	
	/**
	 * 连接socket
	 */
	private Socket m_Socket = null;
	private boolean m_bOutDebugInfo = false;
	private boolean m_bLogin = false;
	
	/**
	 *  连接登录服务器或者列表服务
	 * @param address 连接地址
	 * @param port 端口号
	 * @return 连接成功返回true,失败返回false
	 */
	public boolean connectionToServer(String address,int port,boolean loginServer)
	{
		m_bLogin = loginServer;
		if(address.length() <= 0) return false;
		if(port <= 0) return false;
		
		try
		{
			m_Socket = new Socket();
			if(null == m_Socket) return false;
			
			InetSocketAddress serverAddress = new InetSocketAddress(address, port);
			//连接超时5秒
			m_Socket.connect(serverAddress,5000);
			
			if(m_Socket.isConnected()) return true;
			
			return false;
		}catch(Exception e)
		{
			if(m_bOutDebugInfo)
			{
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			if(m_Socket.isConnected()) try {m_Socket.close(); } catch(Exception e1){}
			return false;
		}finally
		{
			//if(m_Socket.isConnected()) try {m_Socket.close(); } catch(Exception e1){}
		}
	}
	
	/**
	 * 关闭连接
	 */
	public void close()
	{
		if(null != m_Socket)
		{
			if(m_Socket.isConnected()) try {m_Socket.close(); } catch(Exception e1){}
		}
	}
	
//	/**
//	 * 发送验证包
//	 * @return 成功返回true,失败返回false
//	 */
//	public boolean sendVerify()
//	{
//		PackerCmdVerify commend = new PackerCmdVerify("jnysLoginInterJava@8088");
//		return send(commend.getByteArrayData());
//	}
	
	/**
	 * 发送数据
	 * @param data 待发送到数据 
	 * @return 成功返回true
	 */
	private boolean send(byte[] data)
	{
		if(! m_Socket.isConnected()) return false;
		try
		{
			OutputStream outPutStream = m_Socket.getOutputStream();
			if(null != outPutStream)
			{
				outPutStream.write(data,0,data.length);
				outPutStream.flush();
				//outPutStream.close();
			}
			//m_Socket.getOutputStream().write(data);
		} catch (IOException e)
		{
			if(m_bOutDebugInfo)
			{
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			if(m_Socket.isConnected()) try {m_Socket.close(); } catch(Exception e1){}
			return false;
		}
		
		return false;		
	}
	
	/**
	 * 获取返回消息
	 * @return
	 */
	private ApiResult getResultMessage()
	{
		ApiResult result = new ApiResult();
		result.setResult(false);
		result.setMessages("网络读取错误");
		if(! m_Socket.isConnected()) return result;
		
		try
		{
			byte []recvData = new byte[300];
			InputStream inputStream = m_Socket.getInputStream();
			if(null != inputStream)
			{
				Date date = new Date();
				long beginTime = date.getTime();
				int nRecvSize = 0;
				int nRecvPoint = 0;
				for(;;)
				{
					byte []recvBuffer = new byte[300];
					int nRecvLen = inputStream.read(recvBuffer);
					nRecvSize+=nRecvLen;
					if(nRecvSize == 0) continue;
					if(nRecvSize < 0) break;
					long nowTimer = date.getTime();
					if(nowTimer - beginTime >= 15)
					{
						//接受超时
						result.setResult(false);
						result.setMessages("网络读取超时");						
						break;
					}
					
					System.arraycopy(recvBuffer, 0, recvData, nRecvPoint, nRecvLen);
					nRecvPoint+=nRecvLen;
					
					// 小于网络包头
					if(nRecvSize < Constants.NET_DATA_HEADER_SIZE) continue;
					
					ReviceHeader RecvHeader = new ReviceHeader();
					if(true == RecvHeader.ParseCommd(recvData))
					{
						//读取到所有
						if(nRecvSize >=RecvHeader.getSize() )
						{
							int msgSize = RecvHeader.getSize()-Constants.NET_DATA_HEADER_SIZE;
							byte[] msgBuffer = new byte[msgSize+10];
							System.arraycopy(recvData, Constants.NET_DATA_HEADER_SIZE-1, msgBuffer, 0, msgSize);
							ReviceMsgResult msgResult = new ReviceMsgResult();
							if( true == msgResult.ParseCommd(msgBuffer))
							{
								result.setResult(msgResult.getResult());
								result.setMessages(msgResult.getMessages());
								break;
							}
						}else continue; //继续读取
					} // end  RecvHeader.ParseCommd(recvData)
				}// end for
				//inputStream.reset();
				inputStream.close();
			}
		}catch(Exception e)
		{
			if(m_bOutDebugInfo)
			{
				System.out.println(e.getMessage());
				e.printStackTrace();
			}			
		}
		return result;
	}

	/**
	 * 执行命令. 为单一执行.当该命令执行完毕,则会从服务端其断开,http模式,如果需要多次执行,需要多次连接. < br/>
	 *  每次执行完毕,服务端也会断开连接.客户端也必须断开连接.考虑到该执行操作并不是很多,所以不采用长连接<br />
	 *  另外如果联盟需要进行更改,请严格限定执行次数 <br />
	 * @param cmdAddType 命令
	 * @return @see ApiResult
	 */
	public ApiResult sendComment(PackerAbstract cmdAddType)
	{
		if(cmdAddType.isLoginServer() == m_bLogin)
		{
			send(cmdAddType.getByteArrayData());
			ApiResult result = getResultMessage();
			close();
			return result;
		}else
		{
			ApiResult result = new ApiResult();
			result.setMessages("服务器不符合,无法发送消息");
			result.setResult(false);
			return result;
		}
	}
	
}
