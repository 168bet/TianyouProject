package com.k3k.unplat.utils.callbackapi.sender;

import com.k3k.unplat.utils.callbackapi.tool.Constants;

/**
 * 增加联盟配置数据
 * @author Binyo
 *
 */
public class CmdAddUnionConfig extends PackerAbstract
{
	private int unionID;
	private String appTitle;
	private String homePage;
	private String plazaTopAdUrl;
	private String downUrl;
	private String gameInfoUrl;
	private String roomAdUrl;
	private String gameAdUrl;
	private String shopUrl;
	private String payUrl;
	private String matchUrl;
	private String userCenterUrl;
	
	private final int DATA_SZE = 2054;
	private final int PACK_SIZE = HEADER_SIZE+DATA_SZE;
	
	public CmdAddUnionConfig()
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
		 	unsigned int		nUnionID;
			char					szAppTitle[50];
			char					szHomePage[200];
			char					szPlazaTopAdUrl[200];
			char					szDownUrl[200];
			char					szGameInfoUrl[200];
			char					szRoomAdUrl[200];
			char					szGameAdUrl[200];
			char					szShopUrl[200];
			char					szPayUrl[200];
			char					szMatchUrl[200];
			char					szUserCenterUrl[200];
		 */
		addHeader(PACK_SIZE, 2, Constants.S_MAIN_UNION_OP, Constants.S_SUB_ADD_UNION);
		addInt(this.unionID);
		addString(this.appTitle, 50);
		addString(this.homePage, 200);
		addString(this.plazaTopAdUrl, 200);
		addString(this.downUrl, 200);
		addString(this.gameInfoUrl, 200);
		addString(this.roomAdUrl, 200);
		addString(this.gameAdUrl, 200);
		addString(this.shopUrl, 200);
		addString(this.payUrl, 200);
		addString(this.matchUrl, 200);
		addString(this.userCenterUrl, 200);
		
		return m_ByteArrayData;
	}

	/**
	 * 设置联盟ID 不可重复
	 * @param unionID 待设置的联盟ID
	 */
	public void setUnionID(int unionID)
	{
		this.unionID = unionID;
	}

	/**
	 * 设置名称
	 * @param appTitle
	 */
	public void setAppTitle(String appTitle)
	{
		this.appTitle = appTitle;
	}

	/**
	 * 主页地址
	 * @param homePage
	 */
	public void setHomePage(String homePage)
	{
		this.homePage = homePage;
	}

	/**
	 * 头部广告
	 * @param plazaTopAdUrl
	 */
	public void setPlazaTopAdUrl(String plazaTopAdUrl)
	{
		this.plazaTopAdUrl = plazaTopAdUrl;
	}

	/**
	 * 下载地址
	 * @param downUrl
	 */
	public void setDownUrl(String downUrl)
	{
		this.downUrl = downUrl;
	}

	/**
	 * 游戏介绍
	 * @param gameInfoUrl
	 */
	public void setGameInfoUrl(String gameInfoUrl)
	{
		this.gameInfoUrl = gameInfoUrl;
	}

	/**
	 * 房间广告
	 * @param roomAdUrl
	 */
	public void setRoomAdUrl(String roomAdUrl)
	{
		this.roomAdUrl = roomAdUrl;
	}

	/**
	 * 游戏界面广告
	 * @param gameAdUrl
	 */
	public void setGameAdUrl(String gameAdUrl)
	{
		this.gameAdUrl = gameAdUrl;
	}

	/**
	 * 商城广告
	 * @param shopUrl
	 */
	public void setShopUrl(String shopUrl)
	{
		this.shopUrl = shopUrl;
	}

	/**
	 * 比赛地址
	 * @param matchUrl
	 */
	public void setMatchUrl(String matchUrl)
	{
		this.matchUrl = matchUrl;
	}

	/**
	 * 用户中心
	 * @param userCenterUrl
	 */
	public void setUserCenterUrl(String userCenterUrl)
	{
		this.userCenterUrl = userCenterUrl;
	}

	/**
	 * 设置充值地址
	 * @param payUrl
	 */
	public void setPayUrl(String payUrl)
	{
		this.payUrl = payUrl;
	}
	
}
