package com.k3k.unplat.utils.callbackapi.tool;

public class Constants
{
	/**
	 * 网络包头大小
	 */
	public static final int NET_DATA_HEADER_SIZE		= 48;
	
	public static final String NET_API_PASSWORD = "jnysLoginInterJava@8088";
	
	/**
	 *  验证包主ID
	 */
	public static final int S_MAIN_VERIFY = 1;
	/**
	 * 验证包子ID
	 */
	public static final int S_SUB_VERIFY = 1;
	
	
	////////////////////////////////////////////////

	public static final int S_MAIN_TYPE_OP			=100;		//!< 类型操作主ID
	
	public static final int S_SUB_ADD_TYPE			=101;		//!< 增加类型
	public static final int S_SUB_DEL_TYPE			=102;		//!< 删除类型
	public static final int S_SUB_UP_TYPE				=103;		//!< 更新类型
	public static final int S_SUB_ADD_TYPE_URL	=104;		//!< 增加类型URL
	public static final int S_SUB_DEL_TYPE_URL	=105;		//!< 删除类型URL
	public static final int S_SUB_UP_TYPE_URL		=106;		//!< 删除类型URL
	
	////////////////////////////////////////////////

	
	////////////////////////////////////////////////
	public static final int S_MAIN_KIND_OP			=200;		//!< 游戏操作主ID
	
	public static final int S_SUB_ADD_KIND			=201;		//!< 增加游戏
	public static final int S_SUB_DEL_KIND			=202;		//!< 删除游戏
	public static final int S_SUB_UP_KIND			=203;		//!< 删除游戏
	public static final int S_SUB_ADD_KIND_URL	=204;		//!< 增加游戏URL
	public static final int S_SUB_DEL_KIND_URL	=205;		//!< 删除游戏URL
	public static final int S_SUB_UP_KIND_URL	=206;		//!< 删除游戏URL
	
	////////////////////////////////////////////////

	////////////////////////////////////////////////

	public static final int  S_MAIN_UNION_OP		=300;		//!< 联盟操作
	public static final int  S_SUB_ADD_UNION		=301;		//!< 增加联盟
	public static final int  S_SUB_DEL_UNION		=302;		//!< 删除联盟
	public static final int  S_SUB_UP_UNION		=303;		//!< 更新联盟
	
	////////////////////////////////////////////////
	
	public static final int S_MAIN_SYNC_USER_INFO = 600;  //同步用户信息
	public static final int S_SUB_SYNC_USER_INFO = 601;//

}
