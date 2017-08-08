///*
//Copyright (C) Huawei Technologies Co., Ltd. 2015. All rights reserved.
//See LICENSE.txt for this sample's licensing information.
// */
//package com.huawei.gb.huawei;
//
//import java.util.HashMap;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.Button;
//
//import com.huawei.gameservice.sdk.GameServiceSDK;
//import com.huawei.gameservice.sdk.control.GameEventHandler;
//import com.huawei.gameservice.sdk.model.Result;
//import com.huawei.gameservice.sdk.model.RoleInfo;
//import com.huawei.gameservice.sdk.model.UserResult;
//import com.huawei.gameservice.sdk.util.LogUtil;
//import com.huawei.gameservice.sdk.util.StringUtil;
//import com.huawei.gameservice.sdk.view.GameServiceBaseActivity;
//import com.huawei.gb.huawei.net.ReqTask;
//
//public class MainActivity extends GameServiceBaseActivity implements OnClickListener {
//
//	// 日志标签
//	// definition the log tag
//	public static final String TAG = "MainActivity";
//
//	// 返回键是否可用
//	// Identifies the back key is available
//	private static boolean isBackKeyEnable = true;
//
//	private Button start;
//
//	private Button getPlayerLevel;
//
//	private Button addPlayer;
//
//	private Button login;
//
//	private Handler uiHandler = null;
//
//	private int RELOAD_BUTTON = 1;
//	
//	private int HIDE_BUTTON = 2;
//
//	private String buoyPrivateKey = null;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//
//		// 设置无标题
//		// set the window without title
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		// 设置全屏
//		// set the window fullscreen
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//		setContentView(R.layout.main);
//
//		start = (Button) findViewById(R.id.enter_game_pay);
//		start.setOnClickListener(this);
//		start.setVisibility(View.GONE);
//
//		getPlayerLevel = (Button) findViewById(R.id.get_player_level_api);
//		getPlayerLevel.setOnClickListener(this);
//		getPlayerLevel.setVisibility(View.GONE);
//
//		addPlayer = (Button) findViewById(R.id.add_player_info_api);
//		addPlayer.setOnClickListener(this);
//		addPlayer.setVisibility(View.GONE);
//
//		login = (Button) findViewById(R.id.login_in);
//		login.setOnClickListener(this);
//
//		uiHandler = new Handler(this.getMainLooper()) {
//			@Override
//			public void handleMessage(Message msg) {
//				if (msg.getData() == null) {
//					return;
//				}
//				if (msg.what == RELOAD_BUTTON) {
//					start.setVisibility(View.VISIBLE);
//					getPlayerLevel.setVisibility(View.VISIBLE);
//					addPlayer.setVisibility(View.VISIBLE);
//					login.setVisibility(View.GONE);
//				}else if(msg.what==HIDE_BUTTON)
//				{
//					start.setVisibility(View.GONE);
//					getPlayerLevel.setVisibility(View.GONE);
//					addPlayer.setVisibility(View.GONE);
//					login.setVisibility(View.VISIBLE);
//				}
//				String errorMsg = msg.getData().getString("errorMsg");
//				if (!StringUtil.isNull(errorMsg)) {
//                    LogUtil.i(TAG, errorMsg);
//				}
//			}
//		};
//
//		// 为了安全把浮标密钥放到服务端，并使用https的方式获取下来存储到内存中，CP可以使用自己的安全方式处理
//		// For safety, buoy key put into the server and use the https way to get
//		// down into the client's memory.
//		// By the way CP can also use their safe approach.
//		ReqTask getBuoyPrivate = new ReqTask(new ReqTask.Delegate() {
//
//			@Override
//			public void execute(String result) {
//				/**
//				 * 从服务端获取的浮标私钥，由于没有部署最终的服务端，所以返回值写死一个值，CP需要从服务端获取，服务端代码参考华为Demo
//				 * The demo app did not deployed the server, so the return value
//				 * is written fixed.For real app,the CP need to get the buoy key
//				 * from server.
//				 */
//				buoyPrivateKey = GlobalParam.BUOY_SECRET;
//
//				// SDK初始化
//				// SDK initialization
//				init();
//			}
//		}, null, GlobalParam.GET_BUOY_PRIVATE_KEY);
//		getBuoyPrivate.execute();
//
//	}
//
//	/**
//	 * 对按钮进行响应 click event of buttons
//	 */
//	@Override
//	public void onClick(View view) {
//		int id = view.getId();
//		switch (id) {
//		// 点击支付按钮进行支付页面
//		// click the "Pay" button
//		case R.id.enter_game_pay:
//			Intent goInGame = new Intent(MainActivity.this, GameActivity.class);
//			startActivity(goInGame);
//			break;
//		// 点击 获取玩家等级按钮
//		// click the "Get Player Level" button
//		case R.id.get_player_level_api:
//			getPlayerLevel();
//			break;
//		// 点击 保存玩家信息按钮
//		// click the "Add Player Info" button
//		case R.id.add_player_info_api:
//			addPlayerInfo();
//			break;
//		case R.id.login_in:
//			login(1);
//			break;
//		default:
//			break;
//		}
//
//	}
//
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			if (isBackKeyEnable) {
//				return super.onKeyDown(keyCode, event);
//			} else {
//				return true;
//			}
//
//		}
//		return super.onKeyDown(keyCode, event);
//	}
//
//	/**
//	 * 生成游戏签名 generate the game sign
//	 */
//	private String createGameSign(String data) {
//
//		// 为了安全把浮标密钥放到服务端，并使用https的方式获取下来存储到内存中，CP可以使用自己的安全方式处理
//		// For safety, buoy key put into the server and use the https way to get
//		// down into the client's memory.
//		// By the way CP can also use their safe approach.
//
//		String str = data;
//		try {
//			String result = RSAUtil.sha256WithRsa(str.getBytes("UTF-8"), buoyPrivateKey);
//			return result;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
//
//	/**
//	 * 初始化 initialization
//	 */
//	private void init() {
//		GameServiceSDK.init(MainActivity.this, GlobalParam.APP_ID, GlobalParam.PAY_ID,
//				"com.huawei.gb.huawei.installnewtype.provider", new GameEventHandler() {
//
//					@Override
//					public void onResult(Result result) {
//						if (result.rtnCode != Result.RESULT_OK) {
//							handleError("init the game service SDK failed:" + result.rtnCode);
//							return;
//						}
//						login(1);
//						checkUpdate();
//					}
//
//					@Override
//					public String getGameSign(String appId, String cpId, String ts) {
//						return createGameSign(appId + cpId + ts);
//					}
//
//				});
//	}
//
//	/**
//	 * 帐号登录 Login
//	 */
//	private void login(int authType) {
//		GameServiceSDK.login(MainActivity.this, new GameEventHandler() {
//
//			@Override
//			public void onResult(Result result) {
//				if (result.rtnCode != Result.RESULT_OK) {
//					handleError("login failed:" + result.toString());
//					uiHandler.sendMessage(uiHandler.obtainMessage(HIDE_BUTTON));
//				} else {
//					UserResult userResult = (UserResult) result;
//					if (userResult.isAuth != null && userResult.isAuth == 1) {
//						boolean checkResult = checkSign(GlobalParam.APP_ID + userResult.ts + userResult.playerId,
//								userResult.gameAuthSign);
//						if (checkResult) {
//							handleError("login auth success:" + userResult.toString());
//							uiHandler.sendMessage(uiHandler.obtainMessage(RELOAD_BUTTON));
//						} else {
//							uiHandler.sendMessage(uiHandler.obtainMessage(HIDE_BUTTON));
//							handleError("login auth failed check game auth sign error");
//						}
//
//					} else if (userResult.isChange != null && userResult.isChange == 1) {
//						login(1);
//					} else {
//						handleError("login success:" + userResult.toString());
//					}
//
//				}
//			}
//
//			@Override
//			public String getGameSign(String appId, String cpId, String ts) {
//				return null;
//			}
//
//		}, authType);
//	}
//
//	/**
//	 * 校验签名 check the auth sign
//	 */
//	protected boolean checkSign(String data, String gameAuthSign) {
//
//		/*
//		 * 建议CP获取签名后去游戏自己的服务器校验签名，公钥值请参考开发指导书5.1 登录鉴权签名的验签公钥
//		 */
//		/*
//		 * The CP need to deployed a server for checking the sign.
//		 */
//		try {
//			return RSAUtil.verify(data.getBytes("UTF-8"), GlobalParam.LOGIN_RSA_PUBLIC, gameAuthSign);
//		} catch (Exception e) {
//			return false;
//		}
//	}
//
//	/**
//	 * 检测游戏更新 check the update for game
//	 */
//	private void checkUpdate() {
//		GameServiceSDK.checkUpdate(MainActivity.this, new GameEventHandler() {
//
//			@Override
//			public void onResult(Result result) {
//				if (result.rtnCode != Result.RESULT_OK) {
//					handleError("check update failed:" + result.rtnCode);
//				}
//			}
//
//			@Override
//			public String getGameSign(String appId, String cpId, String ts) {
//				return null;
//			}
//
//		});
//	}
//
//	private void addPlayerInfo() {
//		HashMap<String, String> playerInfo = new HashMap<String, String>();
//
//		/**
//		 * 将用户的等级等信息保存起来，必须的参数为RoleInfo.GAME_RANK(等级)/RoleInfo.GAME_ROLE(角色名称)
//		 * /RoleInfo.GAME_AREA(角色所属区)/RoleInfo.GAME_SOCIATY(角色所属公会名称)
//		 * 全部使用String类型存放
//		 */
//		/**
//		 * the CP save the level, role, area and sociaty of the game player into
//		 * the SDK
//		 */
//		playerInfo.put(RoleInfo.GAME_RANK, "15 level");
//		playerInfo.put(RoleInfo.GAME_ROLE, "hunter");
//		playerInfo.put(RoleInfo.GAME_AREA, "20");
//		playerInfo.put(RoleInfo.GAME_SOCIATY, "Red Cliff II");
//		GameServiceSDK.addPlayerInfo(MainActivity.this, playerInfo, new GameEventHandler() {
//
//			@Override
//			public void onResult(Result result) {
//				if (result.rtnCode != Result.RESULT_OK) {
//					handleError("add player info failed:" + result.rtnCode);
//				}
//
//			}
//
//			@Override
//			public String getGameSign(String appId, String cpId, String ts) {
//				return null;
//			}
//
//		});
//	}
//
//	private void getPlayerLevel() {
//
//		GameServiceSDK.getPlayerLevel(MainActivity.this, new GameEventHandler() {
//
//			@Override
//			public void onResult(Result result) {
//				if (result.rtnCode != Result.RESULT_OK) {
//					handleError("get player level failed:" + result.rtnCode);
//					return;
//				}
//				UserResult userResult = (UserResult) result;
//				handleError("player level:" + userResult.playerLevel);
//			}
//
//			@Override
//			public String getGameSign(String appId, String cpId, String ts) {
//				return null;
//			}
//
//		});
//	}
//
//	private void handleError(String errorMsg) {
//		Message msg = new Message();
//		Bundle data = new Bundle();
//		String strMsg = errorMsg;
//		data.putString("errorMsg", strMsg);
//		msg.setData(data);
//		uiHandler.sendMessage(msg);
//	}
//
//}
