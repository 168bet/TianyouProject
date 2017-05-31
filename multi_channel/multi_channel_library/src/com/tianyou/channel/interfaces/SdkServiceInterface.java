package com.tianyou.channel.interfaces;

import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.bean.PayParam;
import com.tianyou.channel.bean.RoleInfo;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;

/**
 * 多渠道接口
 * @author itstrong
 *
 */
public interface SdkServiceInterface {

	/**
	 * Application初始化
	 * @param context
	 * @param island
	 */
	void doApplicationCreate(Context context, boolean island);
	
	/**
	 * Application attachBaseContext方法
	 * @param base
	 */
	void doApplicationAttach(Context base);
	
	/**
	 * doApplicationTerminate
	 */
	void doApplicationTerminate();
	
	/**
	 * doApplicationConfigurationChanged()
	 */
	void doApplicationConfigurationChanged(Application application,Configuration newConfig);
	
	/**
	 * Activity初始化
	 * @param activity
	 * @param initCallback
	 * @param logoutCallback
	 */
	void doActivityInit(Activity activity, TianyouCallback tianyouCallback);
	
	/**
	 * 登录
	 */
	void doLogin();
	
	/**
	 * 登录(应用宝微信登录)
	 */
	void doLoginWechat();
	
	/**
	 * 打开NaverCafe
	 */
	void doOpenNaverCafe();

	/**
	 * 登出
	 */
	void doLogout();
	
	/**
	 * 客户服务
	 */
	void doCustomerService();
	
	/**
	 * 上传角色信息
	 */
	void doUploadRoleInfo(RoleInfo roleInfo);
	
	/**
	 * 数据统计
	 */
	void doDataStatistics(String content);
	
	/**
	 * 创建角色
	 */
	void doCreateRole(RoleInfo roleInfo);
	
	/**
	 * 进入游戏
	 */
	void doEntryGame();
	
	/**
	 * 更新角色信息
	 * @param roleInfo
	 */
	void doUpdateRoleInfo(RoleInfo roleInfo);
	
	/**
	 * 支付指定金额
	 * @param payInfo
	 */
	void doPay(PayParam payInfo);
	
	/**
	 * 执行渠道支付
	 * @param orderId
	 * @param price
	 * @param callback
	 */
	void doChannelPay(PayParam payInfo, OrderinfoBean orderInfo);
	
	/**
	 * 夜间推送开关
	 * @param isOpen true为开，false为关
	 */
	void doPushSwitch(boolean isOpen);
	
	/**
	 * 谷歌成就
	 * @param achieve
	 */
	void doGoogleAchieve(String achieve);
	
	/**
	 * 谷歌成就页面
	 */
	void doGoogleAchieveActivity();

	/**
	 * 退出游戏
	 */
	void doExitGame();

	// ------------------------ 生命周期方法 ------------------------ */
	/**
	 * 加载onResume方法
	 */
	void doResume();
	
	/**
	 * 加载onStart方法
	 */
	void doStart();
	
	/**
	 * 加载onPause方法
	 */
	void doPause();
	
	/**
	 * 加载onStop方法
	 */
	void doStop();
	
	/**
	 * 加载onRestart方法
	 */
	void doRestart();
	
	/**
	 * 加载onNewIntent方法
	 */
	void doNewIntent(Intent intent);
	
	/**
	 * 销毁
	 */
	void doDestory();
	
	/**
	 * doActivityResult
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	void doActivityResult(int requestCode, int resultCode, Intent data);
	
	/**
	 * 返回键
	 */
	void doBackPressed();
	
	/**
	 * 退出游戏开关
	 * @return true：不显示游戏的推出界面，false：显示游戏的退出界面
	 */
	boolean isShowExitGame();
	
	/**
	 * 注销按钮开关
	 * @return true：不显示游戏的注销按钮，false：显示游戏的注销按钮
	 */
	boolean isShowLogout();
	
	void doRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);
	
	void doConfigurationChanged(Configuration newConfig);
	
	/**
	 * 实名认证防沉迷
	 */
	void doVerifiedInfo();
	
	void doRegisterPhone();
	
	void doRegisterGenerate();
}