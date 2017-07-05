package com.k3k.unplat.utils.callbackapi.sender;

import com.k3k.unplat.utils.callbackapi.tool.Constants;

/**
 * 增加游戏类型URL
 * @author Binyo
 */
public class CmdAddKindUrl extends PackerAbstract
{
	private int unionId;
	private int kindId;
	private int serverId;
	private int sort;
	private String type;
	private String target;
	private String name;
	private String url;

	private final int DATA_SZE = 446;
	private final int PACK_SIZE = HEADER_SIZE+DATA_SZE;
	
	public CmdAddKindUrl()
	{
		createPacker(PACK_SIZE);
	}
	
	@Override
	public boolean isLoginServer()
	{
		return false;
	}
	
	@Override
	public byte[] getByteArrayData()
	{
	
		/**
		 //!< 增加KINDURL
		struct CmdAddKindUrl
		{
			unsigned int		nUnionID;				//!< 联盟ID
			unsigned int		nKindID;				//!< 游戏ID
			unsigned int		nServerID;				//!< 服务编号
			unsigned int		nSort;					//!< 排序
			char					szType[10];			//!< 类型
			char					szTarget[20];			//!< 打开方式
			char					szName[200];			//!< 昵称
			char					szUrl[200];				//!< Url地址
		};
		 */
		addHeader(PACK_SIZE, 2, Constants.S_MAIN_KIND_OP,Constants.S_SUB_ADD_KIND_URL);
		addInt(this.unionId);
		addInt(this.kindId);
		addInt(this.serverId);
		addInt(this.sort);
		addString(this.type, 10);
		addString(this.target, 20);
		addString(this.name, 200);
		addString(this.url, 200);
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

	/**
	 * 设置归属KindID
	 * @param kindId
	 */
	public void setKindId(int kindId)
	{
		this.kindId = kindId;
	}

	/**
	 * 设置ServerID ServerID不能大于2000,必须大于0,不能重复 
	 * @param serverId
	 */
	public void setServerId(int serverId)
	{
		this.serverId = serverId;
	}

	/**
	 * 排序
	 * @param sort
	 */
	public void setSort(int sort)
	{
		this.sort = sort;
	}

	/**
	 * 类型 预留参数 请设置为 url
	 * @param type
	 */
	public void setType(String type)
	{
		this.type = type;
	}

	/**
	 * 打开方式 plaza 或者 new
	 * @param target
	 */
	public void setTarget(String target)
	{
		this.target = target;
	}

	/**
	 * 名称
	 * @param name
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * 打开URL URL中可以带参数 <br />
	 * 		%GAMEID 被替换成游戏中的GameID <br />
			%UNIONID 被替换成游戏的联盟ID <br />
			%PASS 被替换成游戏中的密码 <br />
	 * @param url 待设置URL
	 */
	public void setUrl(String url)
	{
		this.url = url;
	}

}
