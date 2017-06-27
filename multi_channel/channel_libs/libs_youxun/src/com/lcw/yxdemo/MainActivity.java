package com.lcw.yxdemo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.youxun.sdk.app.YouxunProxy;
import com.youxun.sdk.app.YouxunXF;
import com.youxun.sdk.app.model.MessageEvent;
import com.youxun.sdk.app.util.Util;

public class MainActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//注册EventBus
		EventBus.getDefault().register(this);
		
		String game = "我方提供game参数";
		String key = "我方提供的key参数";
		
		//初始化 game key
		YouxunProxy.init(game, key);
		
		findViewById(R.id.btn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//启动支付                               商品名称   支付金额  订单号           游戏区服
				YouxunProxy.startPay(MainActivity.this, "测试商品", "0.10",   Util.getOrder(), "0");
			}
		});
		
		findViewById(R.id.btn2).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//退出登录
				YouxunProxy.exitLogin(MainActivity.this);
			}
		});
		
		//启动登录
		YouxunProxy.startLogin(this);
		
		//创建悬浮图标          悬浮图标占屏幕的比例  0.4=>竖屏的十分之四处 
		YouxunXF.onCreate(this, 0.4f);
		
//		server 角色所在区服id标识
//		role 角色名称
//		level 角色等级
//		less 角色注册时间戳
//      gid 角色id
//      area 区服名称
		YouxunProxy.uploadRole(this, "server", "role", "level", "less", "gid", "area");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//销毁悬浮图标
		YouxunXF.onDestroy();
	}
	
	//方式一、通过EventBus接收返回的数据
	@Subscribe
    public void msgEventBus(MessageEvent message) {
		int code = message.getCode();
		Intent data = message.getIntent();
		//登录注册状态
		if (code == 0) {
			if (data.getStringExtra("data").equals("success")) {
				//登入成功
				String userid = data.getStringExtra("userid");
				String msg = data.getStringExtra("msg");
				String time = data.getStringExtra("time");
				String sign = data.getStringExtra("sign");
				
				Log.i("TAGTAG", 
						"userid===" + userid + "\n" + 
								"msg===" + msg + "\n" + 
								"time===" + time + "\n" + 
								"sign===" + sign + "\n");
				
				//检测版本
				YouxunProxy.updateDialog(MainActivity.this, data);
				
				//提示用户账号信息
				YouxunXF.hintUserInfo(MainActivity.this);
			}else {
				//登入失败
				
				Log.i("TAGTAG", "登入失败");
			}
		}
		
		//支付状态……
		if (code == 1) {
			if (data.getStringExtra("data").equals("success")) {
				//支付成功
				
				Log.i("TAGTAG", "支付成功");
			}else {
				//支付失败
				
				Log.i("TAGTAG", "支付失败");
			}
		}
		
		//切换账号……
		if (code == 2) {
			//销毁悬浮图标
//			YouxunXF.onDestroy();
			//启动登录
			YouxunProxy.startLogin(this);
		}
    }
	
	//方式二、通过onActivityResult接收返回的数据
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//登录注册状态
		if (requestCode == YouxunProxy.REQUEST_CODE_LOGIN && resultCode == YouxunProxy.RESULT_CODE_LOGIN) {
			if (data.getStringExtra("data").equals("success")) {
				//登入成功
				String userid = data.getStringExtra("userid");
				String msg = data.getStringExtra("msg");
				String time = data.getStringExtra("time");
				String sign = data.getStringExtra("sign");
				
				Log.i("TAGTAG", 
								"userid===" + userid + "\n" + 
								"msg===" + msg + "\n" + 
								"time===" + time + "\n" + 
								"sign===" + sign + "\n");
				
				//检测版本
				YouxunProxy.updateDialog(MainActivity.this, data);
				
				//提示用户账号信息
				YouxunXF.hintUserInfo(MainActivity.this);
			}else {
				//登入失败
			}
		}
		
		//支付状态……跟启动支付一起用
		if (requestCode == YouxunProxy.REQUEST_CODE_PAY && resultCode == YouxunProxy.RESULT_CODE_PAY) {
			if (data.getStringExtra("data").equals("success")) {
				//支付成功
			}else {
				//支付失败
			}
		}
		
		//切换账号……加入在有悬浮窗的 onActivityResult中 
		if (requestCode == YouxunXF.REQUEST_CODE_SWITCH_ACCOUNT && resultCode == YouxunXF.RESULT_CODE_SWITCH_ACCOUNT) {
			//销毁悬浮图标
//			YouxunXF.onDestroy();
			//启动登录
			YouxunProxy.startLogin(this);
		}
	}
	
	
}
