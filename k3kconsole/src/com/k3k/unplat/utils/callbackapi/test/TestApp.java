package com.k3k.unplat.utils.callbackapi.test;

import com.k3k.unplat.utils.callbackapi.ApiResult;
import com.k3k.unplat.utils.callbackapi.LoginServerApi;
import com.k3k.unplat.utils.callbackapi.sender.CmdSyncUserInfo;

public class TestApp
{
	public static void main(String [] args)
	{
		
	/* 登录服务器	
		LoginServerApi LoginServer = new LoginServerApi();
		boolean ret = LoginServer.connectionToServer("127.0.0.1", 9099,true);
		if(ret)
		{

			//增加类型
//			CmdAddType cmd = new CmdAddType();
//			cmd.setName("发电机");
//			cmd.setSort(5);
//			cmd.setUnionId(1);
//			cmd.setTypeId(7);
	
			//删除类型
//			CmdDelType cmd = new CmdDelType();
//			cmd.setTypeId(7);
//			cmd.setUnionId(1);
			
			// 更新类型
//			CmdUpdateType cmd = new CmdUpdateType();
//			cmd.setName("麻将游戏gupai");
//			cmd.setSort(9);
//			cmd.setUnionId(1);
//			cmd.setTypeId(2);			
			
			// 增加类型URL
//			CmdAddTypeUrl cmd = new CmdAddTypeUrl();
//			cmd.setName("测试URL");
//			cmd.setSort(9);
//			cmd.setTarget("new");
//			cmd.setTypeId(2010);
//			cmd.setUnionId(1);
//			cmd.setUrl("http://www.k3k.com");
			
			// 删除类型URL
//			CmdDelTypeUrl cmd = new CmdDelTypeUrl();
//			cmd.setUnionId(1);
//			cmd.setTypeId(2002);
			
			//更新类型URL
//			CmdUpdateTypeUrl cmd = new CmdUpdateTypeUrl();
//			cmd.setName("修改URL");
//			cmd.setSort(9);
//			cmd.setTarget("new");
//			cmd.setUnionId(1);
//			cmd.setTypeId(2001);			
//			cmd.setUrl("http://www.k3k.com");
			
			//增加游戏
//			CmdAddKind cmd = new CmdAddKind();
//			cmd.setKindId(1101);
//			cmd.setTypeId(2);
//			cmd.setName("新斗地主");
//			cmd.setSort(9);
//			cmd.setUnionId(1);
//			cmd.setVersion(0);
			
			//删除游戏
//			CmdDelKind cmd = new CmdDelKind();
//			cmd.setKindId(1101);
//			cmd.setTypeId(2);
//			cmd.setUnionId(1);
			
			//修改游戏
//			CmdUpdateKind cmd = new CmdUpdateKind();
//			cmd.setKindId(1006);
//			cmd.setTypeId(1);
//			cmd.setName("泰兴麻将123");
//			cmd.setSort(9);
//			cmd.setUnionId(1);
//			cmd.setVersion(0);			
			
			//增加联盟
//			CmdAddUnionConfig cmd = new CmdAddUnionConfig();
//			cmd.setUnionID(10);
//			cmd.setAppTitle("联盟10");
//			cmd.setDownUrl("http://www.163.com");
//			cmd.setGameAdUrl("http://www.163.com");
//			cmd.setGameInfoUrl("http://www.163.com");
//			cmd.setHomePage("http://www.163.com");
//			cmd.setMatchUrl("http://www.163.com");
//			cmd.setPayUrl("http://www.163.com");
//			cmd.setPlazaTopAdUrl("http://www.163.com");
//			cmd.setRoomAdUrl("http://www.163.com");
//			cmd.setShopUrl("http://www.163.com");
//			cmd.setUserCenterUrl("http://www.163.com");

//			//不需要删除联盟功能
			
			//修改联盟
			CmdUpdateUnionConfig cmd = new CmdUpdateUnionConfig();
			cmd.setUnionID(1);
			cmd.setAppTitle("联盟10");
			cmd.setDownUrl("http://www.163.com");
			cmd.setGameAdUrl("http://www.163.com");
			cmd.setGameInfoUrl("http://www.163.com");
			cmd.setHomePage("http://www.163.com");
			cmd.setMatchUrl("http://www.163.com");
			cmd.setPayUrl("http://www.163.com");
			cmd.setPlazaTopAdUrl("http://www.163.com");
			cmd.setRoomAdUrl("http://www.163.com");
			cmd.setShopUrl("http://www.163.com");
			cmd.setUserCenterUrl("http://www.163.com");			
			
			ApiResult result = LoginServer.sendComment(cmd);
			System.out.println("结果:"+result.isResult()+" 消息内容:"+result.getMessages());
		}
		LoginServer.close();
		*/
		
		//列表服务器
		LoginServerApi LoginServer = new LoginServerApi();
		
//		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
//		Mobileitf m = (Mobileitf) ctx.getBean("mobileitf");
		
		String ip = "42.96.172.238";//42.96.172.238:444
		String port = "7788";
		boolean ret = LoginServer.connectionToServer(ip, Integer.parseInt(port), true);
		if(ret)
		{
			//删除类型
//			CmdDelKindUrl cmd = new CmdDelKindUrl();
//			cmd.setKindId(1001);
//			cmd.setServerId(2);
//			cmd.setUnionId(1);

			System.out.println("Connecting ... " + ip + ":" + port);
//			CmdVerify vcmd = new CmdVerify("");
//			ApiResult vr = LoginServer.sendComment(vcmd);
//			System.out.println("验证结果："+vr.isResult() + " 消息内容："+vr.getMessages());
			//同步用户信息
			CmdSyncUserInfo cmd = new CmdSyncUserInfo();
			cmd.setUserId(3668582);
			cmd.setType(1);
//			System.out.println(cmd.isLoginServer());
			ApiResult result = LoginServer.sendComment(cmd);
			
			System.out.println("结果:"+result.isResult()+" 消息内容:"+result.getMessages());
		}
		LoginServer.close();
		System.out.println("Disconnected ... ");
//		ctx = null;
	}
}
