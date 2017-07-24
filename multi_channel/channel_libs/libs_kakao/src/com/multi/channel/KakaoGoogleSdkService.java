package com.multi.channel;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.vending.billing.IInAppBillingService;
import com.google.gson.Gson;
import com.kakao.auth.ApiResponseCallback;
import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.ErrorCode;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;
import com.kakao.auth.Session;
import com.kakao.game.GameAPI;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakaogame.KGIdpProfile;
import com.kakaogame.KGKakaoInvitationEvent;
import com.kakaogame.KGKakaoInvitationHost;
import com.kakaogame.KGKakaoInvitationJoiner;
import com.kakaogame.KGKakaoInvitationJoiner.KGKakaoInvitationRewardState;
import com.kakaogame.KGKakaoProfile;
import com.kakaogame.KGKakaoProfile.KGKakaoFriendsResponse;
import com.kakaogame.KGKakaoTalkGroupChat;
import com.kakaogame.KGKakaoTalkGroupChat.KGKakaoTalkGroupChatsResponse;
import com.kakaogame.KGKakaoStory;
import com.kakaogame.KGKakaoTalkMessage;
import com.kakaogame.KGLocalPlayer;
import com.kakaogame.KGPlayer;
import com.kakaogame.KGResult;
import com.kakaogame.KGResultCallback;
import com.kakaogame.KGSession;
import com.panggame.pgmp2sdk.Pgmp2Sdk;
import com.panggame.pgmp2sdk.Interface.Pgmp2EventListener;
import com.panggame.pgmp2sdk.Interface.Pgmp2NaverCafeSDKCallBackListener;
import com.skplanet.dodo.IapPlugin;
import com.skplanet.dodo.IapPlugin.RequestCallback;
import com.skplanet.dodo.IapResponse;
import com.tianyou.channel.bean.OneStoreParam;
import com.tianyou.channel.bean.OneStoreParam.ResultBean;
import com.tianyou.channel.bean.OneStoreParam.ResultBean.ProductBean;
import com.tianyou.channel.bean.OrderInfo;
import com.tianyou.channel.bean.PayInfo;
import com.tianyou.channel.bean.PayParam;
import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.tianyou.channel.utils.ConfigHolder;
import com.tianyou.channel.utils.HttpUtils;
import com.tianyou.channel.utils.HttpUtils.HttpCallback;
import com.tianyou.channel.utils.LogUtils;
import com.tianyou.channel.utils.ToastUtils;
import com.tianyou.channel.utils.URLHolder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.util.Log;

public class KakaoGoogleSdkService extends BaseSdkService {

	private SessionCallback callback;
	private IapPlugin mIapPlugin;		// onstore支付
	
	private Pgmp2Sdk pgmp2Sdk = null;
	
	// 谷歌支付
	private IInAppBillingService mService;
	private ServiceConnection mServiceConn;
	
	@Override
	public void doApplicationCreate(final Context context, boolean island) {
		super.doApplicationCreate(context, island);
	}
	
	/**
	 * 初始化
	 */
	@Override
	public void doActivityInit(Activity activity, TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		mLoginInfo.setIsOverseas(true);		// 设置海外地址
		mIapPlugin = IapPlugin.getPlugin(mActivity, "release");		// 初始化onestore支付插件
		pgmp2Sdk = Pgmp2Sdk.getInstance();		// 初始化pgp
		int pgmp2SdkInitGameResultCode = pgmp2Sdk.initGame("", "", mActivity, mPgmp2EventListener,mPgmp2NaverCafeSDKCallBackListener, true);
		if (pgmp2SdkInitGameResultCode == 1) {
			
		}
		KGSession.start(mActivity, mStartCallback);		//	当游戏开始时，调用Start函数来通知Kakao Game SDK游戏已经开始
		
		// 谷歌支付
		mServiceConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, android.os.IBinder service) {
                mService = IInAppBillingService.Stub.asInterface(service);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
            	Log.d("TAG","mService = null-----------------");
                mService = null;
            }
        };

        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        mActivity.bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);
	}
	
	/**
	 * 登录
	 */
	@Override
	public void doLogin() {
		super.doLogin();
		KGSession.login(mActivity, mLoginCallback);
	}
	
	/**
	 * 注销
	 */
	@Override
	public void doLogout() {
		super.doLogout();
		KGSession.logout(mActivity, mLogoutCallback);
	}
	
	@Override
	public void doOpenNaverCafe() {
		super.doOpenNaverCafe();
		if (pgmp2Sdk.isUseNaverCafe()) {
			pgmp2Sdk.openNaverCafeApp();
		}
	}
	
	@Override
	public void doCustomerService() {
		super.doCustomerService();
		mActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				pgmp2Sdk.openCSCenterAcitivity();
			}
		});
	}
	
	/**
	 * pgmp悬浮球接口,点开客服中心的时候调用
	 */
	public void doShowNaverCafePlugWidget(){
		mActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				pgmp2Sdk.showNaverCafePlugWidget();
			}
		});
	}
	
	/**
	 * 调用条款声明
	 */
	public void doOpenFirstAgreeActivity(){
		pgmp2Sdk.openFirstAgreeActivity();
	}
	
	/**
	 * 调用PGMP中,提供的 Promotion Event Popup时,使用的函数 
	 */
	public void doOpenPopupListActivity() {
		pgmp2Sdk.openPopupListActivity();
	}
	
	/**
	 * 谷歌支付
	 */
	@Override
	public void doPay(PayParam payInfo) {
//		super.doPay(payInfo);
		if (mRoleInfo == null) {
			LogUtils.d("调用支付接口0");
			ToastUtils.show(mActivity, "请先上传角色信息");
			return;
		}
		String payCode = payInfo.getPayCode();
		mPayInfo = ConfigHolder.getPayInfo(mActivity, payCode);
		createGoogleOrder(payInfo);
	}
	
	/**
	 * 创建谷歌订单
	 * @param payInfo
	 */
	protected void createGoogleOrder(final PayParam payInfo) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("userId", mLoginInfo.getTianyouUserId());
		param.put("appID", mChannelInfo.getGameId());
		param.put("roleId", mRoleInfo.getRoleId());
		param.put("serverID", mRoleInfo.getServerId());
		param.put("serverName", mRoleInfo.getServerName());
		param.put("customInfo", payInfo.getCustomInfo());
		param.put("productId", mPayInfo.getProductId());
		param.put("productName", mPayInfo.getProductName());
		param.put("productDesc", mPayInfo.getProductDesc());
		param.put("moNey", mPayInfo.getMoney());
		param.put("promotion", mChannelInfo.getChannelId());
		param.put("playerid", mLoginInfo.getChannelUserId());
		param.put("roleName", mRoleInfo.getRoleName());
		HttpUtils.post(mActivity, URLHolder.URL_OVERSEAS+URLHolder.CREATE_ORDER_URL, param, new HttpCallback() {
			@Override
			public void onSuccess(String data) {
				OrderInfo orderInfo = new Gson().fromJson(data, OrderInfo.class);
				if ("200".equals(orderInfo.getResult().getCode())) {
					doChannelPay(payInfo, orderInfo.getResult().getOrderinfo());
				} else {
					mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, "创建订单失败");
				}
			}
			
			@Override
			public void onFailed(String code) {
				mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, "创建订单失败");
			}
		});
	}
	
	private String money;
	@Override
	public void doChannelPay(PayParam payInfo, OrderinfoBean orderInfo) {
//		super.doChannelPay(payInfo, orderInfo);
		money = orderInfo.getMoNey();
		try {
			Log.d("TAG","get buyIntent packageName= "+mActivity.getPackageName()+",waresid= "+orderInfo.getWaresid()+",orderID= "+orderInfo.getOrderID());
			Log.d("TAG", "mService= "+mService);
			Bundle buyIntentBundle = mService.getBuyIntent(3, mActivity.getPackageName(), "com.kakao.pang.dragongod.google.001", "inapp", orderInfo.getOrderID());
			Log.d("TAG", "buyIntentBundle= "+buyIntentBundle);
			
	        int code = buyIntentBundle.getInt("RESPONSE_CODE");
	//        Log.d("TAG", "code= " + code);
	        PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
	        Log.d("TAG", "pendingIntent= "+pendingIntent);
	//    try {
	        mActivity.startIntentSenderForResult(pendingIntent.getIntentSender(),
	                1001, new Intent(), Integer.valueOf(0), Integer.valueOf(0),
	                Integer.valueOf(0));
		} catch(Exception e) {
			Log.d("TAG", "get buy intent exception= " + e.getMessage());
		}
	}
	
	
	/**
	 * 切换账号
	 */
	public void doSwitchAccount(){
		KGSession.connect(mActivity, new KGResultCallback<Void>() {

			@Override
			public void onResult(KGResult<Void> kgResult) {
				if (kgResult.isSuccess()) {
					LogUtils.d("switchAccount success");
				} else {
					LogUtils.d("switchAccount failed");
					 if(kgResult.getCode() == KGResult.KGResultCode.INVALID_PARAMETER){
		                //如果活动为空
						 LogUtils.e("kgResultCode.invalid_parameter");
		            } else if(kgResult.getCode()== KGResult.KGResultCode.NOT_AUTHORIZED){
		                //如果当前没有认证
		            	 LogUtils.e("kgResultCode.NOT_AUTHORIZED");
		            } else if(kgResult.getCode()== KGResult.KGResultCode.INVALID_STATE){
		                // If currently authenticated IDP is not guest, google or apple gamecenter authentication
		            	 LogUtils.e("kgResultCode.INVALID_STATE");
		            } else if (kgResult.getCode() == KGResult.KGResultCode.ALREADY_USED_IDP_ACCOUNT) {
		                // If trying to convert to an already used IDP account
		            	 LogUtils.e("kgResultCode.ALREADY_USED_IDP_ACCOUNT");
		            } else {
		                // Other errors
		            	 LogUtils.e("kgResultCode.other");
		            }
				}
			}
		});
	}
	
	
	
	/**
	 * 查询我(当前玩家)的数据
	 */
	public void getCurrentPlayerData(){
		KGLocalPlayer currentPlayer = KGLocalPlayer.getCurrentPlayer();
		String playerId = currentPlayer.getPlayerId();
		LogUtils.d("getCurrentPlayerID palyerID= "+playerId);
	}
	
	/**
	 * 更新玩家IDP数据
	 */
	public void doRefreshLocalIdpProfile(){
		KGIdpProfile.refreshLocalIdpProfile(new KGResultCallback<KGIdpProfile>() {
			@Override
			public void onResult(KGResult<KGIdpProfile> kgResult) {
				if (kgResult.isSuccess()) {		// 更新成功
					KGIdpProfile newIdpProfile = KGLocalPlayer.getCurrentPlayer().getIdpProfile();
					LogUtils.d("refreshLocalIdpProfile.success= "+newIdpProfile.toJSONString());
				} else {
					LogUtils.e("refreshLocalIdpProfile.failed");
				}
			}
		});
	}
	
	/**
	 * 查询朋友数据列表（好友中显示）
	 */
	public void doLoadFriendPlayers(){
		
		KGPlayer.loadFriendPlayers(new KGResultCallback<List<KGPlayer>>() {

			@Override
			public void onResult(KGResult<List<KGPlayer>> result) {
				if (result.isSuccess()) {
					List<KGPlayer> friendPlayers = result.getContent();
					LogUtils.d("friendPlayers.size= "+friendPlayers.size());
					for (KGPlayer kgPlayer : friendPlayers) {
						KGIdpProfile idpProfile = kgPlayer.getIdpProfile();
						LogUtils.d("idProfile= "+idpProfile+"\n");
					}
				} else { LogUtils.d("load friendplayers failed");}
			}
		});
	}
	
	
	
	/**
	 * 查询kakao talk邀请对象列表（所有可以邀请的对象,展示并添加邀请按钮）
	 */
	public void doLoadInvitableFriendProfiles(int offset,int limit){
		
		KGKakaoProfile.loadInvitableFriendProfiles(offset, limit, new KGResultCallback<KGKakaoProfile.KGKakaoFriendsResponse>() {
			@Override
			public void onResult(KGResult<KGKakaoFriendsResponse> kgResult) {
				if (kgResult.isSuccess()) {
					KGKakaoFriendsResponse response = kgResult.getContent();
					int totalCount = response.getTotalCount();
					List<KGKakaoProfile> friendList = response.getFriendList();
					LogUtils.d("invitablefriendprofileslist.size= "+friendList.size());
				} else {LogUtils.d("查询kakao talk邀请对象列表失败");}
			}
		});
	}
	
	/**
	 * 查找kakao talk推荐邀请对象列表（kakao推荐邀请的对象，活动之类的地方调用）
	 */
	public void doLoadRecommendedInvitableFriendProfiles(int offset,int limit) {
		
		KGKakaoProfile.loadRecommendedInvitableFriendProfiles(offset, limit, new KGResultCallback<KGKakaoProfile.KGKakaoFriendsResponse>() {
			@Override
			public void onResult(KGResult<KGKakaoFriendsResponse> kgResult) {
				if (kgResult.isSuccess()) {
					KGKakaoFriendsResponse response = kgResult.getContent();
					int totalCount = response.getTotalCount();
					List<KGKakaoProfile> friendList = response.getFriendList();
					LogUtils.d("recommendedinvitablefriendprofiles.size= "+friendList.size());
				} else { LogUtils.d("查找kakao talk推荐邀请对象列表失败");}
			}
		});
	}
	
	/**
	 * 发送kakao talk邀请消息（点击邀请对象列表的邀请按钮触发）
	 */
	public void doSendInviteMessage(KGKakaoProfile kakaoProfile) {
		String templateId = "4572";
		Map<String, String> args = new LinkedHashMap<String, String>();
		String nickname = ((KGKakaoProfile)KGLocalPlayer.getCurrentPlayer().getIdpProfile()).getNickname();
		args.put("${sender_name}",nickname);
		args.put("${invitation_event_id}", "110");
		KGKakaoTalkMessage.sendInviteMessage(kakaoProfile, templateId, args, new KGResultCallback<Boolean>() {
			
			@Override
			public void onResult(KGResult<Boolean> kgResult) {
				if (kgResult.isSuccess()) {
					LogUtils.d("kgresult.getcontent()= "+kgResult.getContent());
				} else {
					LogUtils.d("sendinvtemessage failed= "+kgResult.getContent());
				}
			}
		});
	}
	
	
	
	/**
	 * 设置kakao talk游戏消息收据
	 */
	public void doShowAllowMessageSettingView(){
		KGKakaoTalkMessage.showAllowMessageSettingView(mActivity, new KGResultCallback<Boolean>() {

			@Override
			public void onResult(KGResult<Boolean> kgResult) {
				if (kgResult.isSuccess()) {
					Boolean content = kgResult.getContent();
					LogUtils.d("content= "+content);
				} else {
					LogUtils.d("code= "+kgResult.getCode());
				}
			}
		});
	}
	
	
	
	/**
	 * 查询正在进行的补偿性邀请活动
	 */
	public void doLoadInvitationEvents(){
		KGKakaoInvitationEvent.loadInvitationEvents(new KGResultCallback<List<KGKakaoInvitationEvent>>() {

			@Override
			public void onResult(KGResult<List<KGKakaoInvitationEvent>> kgResult) {
				if (kgResult.isSuccess()) {
					List<KGKakaoInvitationEvent> invitationEventList = kgResult.getContent();
					for (KGKakaoInvitationEvent invitationEvent : invitationEventList) {
						int eventId = invitationEvent.getEventId();
						boolean isEventCardEnabled = invitationEvent.isEventCardEnabled();
						long startTime = invitationEvent.getStartTime();
						long finishTime = invitationEvent.getFinishTime();
						int maxHostRewardCount = invitationEvent.getMaxHostRewardCount();
						String hostRewardCode = invitationEvent.getHostRewardCode();
						String joinRewardCode = invitationEvent.getJoinRewardCode();
						String invitationUrl = invitationEvent.getInvitationUrl();
						int totalJoinerCount = invitationEvent.getTotalJoinerCount();
					}
				} else {
					// 调用失败
				}
			}
		});
	}
	
	/**
	 * 查询玩家谁邀请我
	 */
	public void doLoadInvitationHost(int eventId){
		KGKakaoInvitationHost.loadInvitationHost(eventId, new KGResultCallback<KGKakaoInvitationHost>() {

			@Override
			public void onResult(KGResult<KGKakaoInvitationHost> kgResult) {
				if (kgResult.isSuccess()) {
					KGKakaoInvitationHost invitationHost = kgResult.getContent();
					if (invitationHost != null) {
						KGPlayer player = invitationHost.getPlayer();
						int totalJoinerCount = invitationHost.getTotalJoinerCount();
					} else {
						// 没有人邀请我
					}
				} else {
					
				}
			}
		});
	}
	
	/**
	 * 查询通过我的邀请参加的玩家名单
	 */
	public void doLoadInvitationJoiners(int eventId){
		KGKakaoInvitationJoiner.loadInvitationJoiners(eventId, new KGResultCallback<List<KGKakaoInvitationJoiner>>() {

			@Override
			public void onResult(KGResult<List<KGKakaoInvitationJoiner>> kgResult) {
				if (kgResult.isSuccess()) {
					List<KGKakaoInvitationJoiner> invitationJoinerList = kgResult.getContent();
					for (KGKakaoInvitationJoiner invitationJoiner : invitationJoinerList) {
						KGPlayer player = invitationJoiner.getPlayer();
						int hostRewardCode = invitationJoiner.getHostRewardCode();
						KGKakaoInvitationRewardState hostRewardState = invitationJoiner.getHostRewardState();
						int joinRewardCode = invitationJoiner.getJoinRewardCode();
						KGKakaoInvitationRewardState joinRewardState = invitationJoiner.getJoinRewardState();
						long joinTime = invitationJoiner.getJoinTime();
					}
				} else {
					// 调用失败
				}
			}
		});
		
	}
	
	
	/**
	 * 查询kakao talk 游戏朋友列表（邀请菜单中显示）
	 */
	public void doKakaoTalkLoadFriendPlayers(){
		
		KGPlayer.loadFriendPlayers(new KGResultCallback<List<KGPlayer>>() {

			@Override
			public void onResult(KGResult<List<KGPlayer>> kgResult) {
				if (kgResult.isSuccess()) {
					List<KGPlayer> friendList = kgResult.getContent();
					LogUtils.d("kakaotalkfriendlist.size= "+friendList.size());
				} else {
					
				}
			}
		});
	}
	
	/**
	 * 发送kakao talk游戏信息(好友列表中点击邀请按钮触发)
	 */
	public void doSendGameMessage(KGKakaoProfile kakaoProfile){
		String templateId = "4572";
		Map<String, String> args = new LinkedHashMap<String, String>();
		String nickName = ((KGKakaoProfile)KGLocalPlayer.getCurrentPlayer().getIdpProfile()).getNickname();
		args.put("${sender_name}", nickName);
		KGKakaoTalkMessage.sendGameMessage(kakaoProfile, templateId, args, new KGResultCallback<Boolean>() {

			@Override
			public void onResult(KGResult<Boolean> kgResult) {
				if (kgResult.isSuccess()) {
					
				} else {
					
				}
			}
		});
	}
	
	/**
	 * 查询kakao talk 团体聊天室列表
	 */
	public void doLoadGroupChats(int offset,int limit){
		KGKakaoTalkGroupChat.loadGroupChats(offset, limit, new KGResultCallback<KGKakaoTalkGroupChat.KGKakaoTalkGroupChatsResponse>() {
			@Override
			public void onResult(KGResult<KGKakaoTalkGroupChatsResponse> kgResult) {
				if (kgResult.isSuccess()) {
					KGKakaoTalkGroupChatsResponse response = kgResult.getContent();
					int totalCount = response.getTotalCount();
					List<KGKakaoTalkGroupChat> groupChats = response.getGroupChats();
				} else {
					
				}
			}
		});
	}
	
	/**
	 * 发送kakao talk组聊天消息
	 */
	public void doSendGroupChatMessage(KGKakaoTalkGroupChat groupChat){
		String templateId = "4572";
		Map<String, String> args = new LinkedHashMap<String, String>();
		String nickname = ((KGKakaoProfile)KGLocalPlayer.getCurrentPlayer().getIdpProfile()).getNickname();
		args.put("${sender_name}", nickname);
		KGKakaoTalkMessage.sendGroupChatMessage(groupChat, templateId, args, new KGResultCallback<Boolean>() {
			@Override
			public void onResult(KGResult<Boolean> kgResult) {
				if (kgResult.isSuccess()) {
					
				} else {
					
				}
			}
		});
	}
	
	
	/*******************************	生命周期方法	***********************************************/
	@Override
	public void doPause() {
		KGSession.pause(mActivity, new KGResultCallback<Void>() {
			@Override
			public void onResult(KGResult<Void> result) { LogUtils.d("KGSession.pause= "+result);}
		});
	}
	
	@Override
	public void doResume() {
		KGSession.resume(mActivity, new KGResultCallback<Void>() {
			@Override
			public void onResult(KGResult<Void> result) {LogUtils.d("KGSession.resume= "+result);}
		});
	}
	
	@Override
	public void doDestory() {
		super.doDestory();
		mIapPlugin.exit();
		Session.getCurrentSession().removeCallback(callback);
	}
	
	private String googleOrderID = "";
	@Override
	public void doActivityResult(int requestCode, int resultCode, Intent data) {
		if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) { return;}
		
		// 谷歌支付
		if (requestCode == 1001) {  
            int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
            Log.d("TAG", "pay responsecode= " + responseCode);
            final String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
            String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");
            
            Log.d("TAG", "purchaseData= " + purchaseData + ",dataSignature= " + dataSignature);
            
            Log.d("TAG", "result code= " + resultCode);
            
            try {
				JSONObject jsonObject = new JSONObject(purchaseData);
				googleOrderID = jsonObject.getString("orderId");
				LogUtils.d("googleOrderID= "+googleOrderID);
			} catch (Exception e) {
				Log.d("TAG", "consumePurchase= "+e.getMessage());
			}
 
            if (resultCode == mActivity.RESULT_OK) {
            	Log.d("TAG", "mActivity.RESULT_OK---------------");
            	new Thread(new Runnable() {
        			@Override
        			public void run() {
        				// 消耗谷歌商品
        				JSONObject dataObject = null;
        				try {
        					dataObject = new JSONObject(purchaseData);
        					String purchaseToken = dataObject.getString("purchaseToken");LogUtils.d("purchaseToken= "+purchaseToken);
        					int response = mService.consumePurchase(3, mActivity.getPackageName(), purchaseToken);
        					LogUtils.d("packageName= "+mActivity.getPackageName()+",purchaseToken= "+purchaseToken);
        					LogUtils.d("onActivityRestul response= "+response);
        				} catch (Exception e) {
        					e.printStackTrace();
        				}
        			}
        		}).start();
            }
        	checkGoogleOrder(purchaseData, dataSignature);
		} else {
			mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, "支付失败");
		}
		
		super.doActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * 校验谷歌订单
	 */
	// 校验谷歌订单
		private void checkGoogleOrder(final String purchaseData,String dataSignature){
			Map<String, String> param = new HashMap<String, String>();
	        param.put("appID", mChannelInfo.getGameId());
	        param.put("inapp_purchase_data", purchaseData);
	        param.put("inapp_data_signature", dataSignature);
	        param.put("promotion", mChannelInfo.getChannelId());
	        Log.d("TAG", "google pay param= " + param);
	        HttpUtils.post(mActivity, URLHolder.CHECK_ORDER_GOOGLE, param, new HttpUtils.HttpCallback() {
	            @Override
	            public void onSuccess(String data) {
	                Log.d("TAG", "pay success data= " + data);
	                try {
						JSONObject jsonObject = new JSONObject(data);
						JSONObject result = jsonObject.getJSONObject("result");
						String code = result.getString("code");
						String msg = result.getString("msg");
						if ("200".equals(code)) {
							LogUtils.d("支付成功");
							LogUtils.d("google pay response= "+purchaseData+",money= "+money+",googleOrderId= "+googleOrderID);
							mTianyouCallback.onResult(TianyouCallback.CODE_PAY_SUCCESS, msg);
							pgmp2Sdk.purchase(mPayInfo.getProductId(), money, 2, googleOrderID, purchaseData, 1);
//							IgawAdbrix.purchase(mActivity, googleOrderID, mPayInfo.getProductId(), 
//									mPayInfo.getProductName(), Double.parseDouble(money), 
//									Integer.parseInt("1"), IgawAdbrix.Currency.KR_KRW, "");
						} else {
							mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, data);
						}
					} catch (JSONException e) {
						e.printStackTrace();
						mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, e.getMessage());
					}
	            }

	            @Override
	            public void onFailed(String code) {
	                Log.d("TAG", "pay failed----------code= " + code);
	            }
	        });
		}
	
	/*********************************	回调接口	**********************************************/
	
	/**
	 * 开始的回调接口
	 */
	private KGResultCallback<Void> mStartCallback = new KGResultCallback<Void>() {
		@Override
		public void onResult(KGResult<Void> result) {
			if (result.isSuccess()) {	// 开始成功
				mTianyouCallback.onResult(TianyouCallback.CODE_INIT, "SDK初始化完成");	// SDK初始化完成,通知游戏
				if  (KGSession.isLoggedIn()) {		// 自动登录检查,此时已经自动登录成功,可以获取当前玩家的信息
					String playerId = KGLocalPlayer.getCurrentPlayer().getPlayerId();		//  平台发行的当前玩家ID
					String accessToken = KGSession.getAccessToken();		// 平台访问令牌
					KGIdpProfile idpProfile = KGLocalPlayer.getCurrentPlayer().getIdpProfile();		// 平台发行的当前玩家ID
					LogUtils.d("KGSession.start login success playerID= "+playerId+",accessToken= "+accessToken+",profile= "+idpProfile);
					// 向天游后台校验登录状态并通知游戏
					mLoginInfo.setChannelUserId(playerId);
					mLoginInfo.setUserToken(accessToken);
					mLoginInfo.setNickname(idpProfile.getIdpUserId());
					checkLogin();
				} else {	// 不存在自动登录信息，跳转到登录页
					LogUtils.d("KGSession.start login failed= "+result.getCode());
				}
			} else {	// 开始失败
				LogUtils.d("KGSession.start failed= "+result.getCode()+",msg= "+result.getDescription());
			}
		}
	};
	
	/**
	 * 登录的回调接口
	 */
	private KGResultCallback<Void> mLoginCallback = new KGResultCallback<Void>() {
		@Override
		public void onResult(KGResult<Void> result) {
			if (result.isSuccess()) {	// IDP平台登录,登录成功
				
				String playerId = KGLocalPlayer.getCurrentPlayer().getPlayerId();	
				String accessToken = KGSession.getAccessToken();
				KGIdpProfile idpProfile = KGLocalPlayer.getCurrentPlayer().getIdpProfile();
				// 向天游后台校验登录状态
				LogUtils.d("KGSession.login login success playerID= "+playerId+",accessToken= "+accessToken+",profile= "+idpProfile);
				mLoginInfo.setChannelUserId(playerId);
				mLoginInfo.setUserToken(accessToken);
				mLoginInfo.setNickname(idpProfile.getIdpUserId());
				requestMe();
				checkLogin();
			} else {	// 登录失败
				LogUtils.d("KGSession.login failed= "+result.getCode());
				mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, "登录失败");		//登录失败通知游戏
			}
		}
	};
	
	/**
	 * 注销的回调接口
	 */
	private KGResultCallback<Void> mLogoutCallback = new KGResultCallback<Void>() {

		@Override
		public void onResult(KGResult<Void> result) {
			if (result.isSuccess()) {	// 注销成功通知游戏
				pgmp2Sdk.logout(mActivity, mActivity);
				mTianyouCallback.onResult(TianyouCallback.CODE_LOGOUT, "用户注销成功");
				LogUtils.d("KGSession.logout success");
			} else { LogUtils.d("KGSession.logout failed");}	// 注销失败
		}
	};
	
	/**
	 * 接收PGMP2SDK内函数的接口
	 */
	private Pgmp2EventListener mPgmp2EventListener = new Pgmp2EventListener() {
		
		@Override
		public void Pgmp2UnityNoLoginCloseListener(String msg) {
			
		}
		
		@Override
		public void Pgmp2UnityMAppPermissionAllGrantsClientGameListener() {
			
		}
		
		@Override
		public void Pgmp2UnityMAppPermissionActivityCloseListener() {
			
		}
		
		@Override
		public void Pgmp2UnityLogoutListener(String msg) {
			
		}
		
		@Override
		public void Pgmp2UnityLoginListener(String resultCode, String guid, String idsort,String guest, String email) {
			
		}
		
		@Override
		public void Pgmp2UnityAllFirstAgreeNoLoginCloseListener() {
			
		}
	};
	
	private Pgmp2NaverCafeSDKCallBackListener mPgmp2NaverCafeSDKCallBackListener = new Pgmp2NaverCafeSDKCallBackListener() {
		
		@Override
		public void OnVotedListener(String arg0, String arg1, String arg2,
				String arg3, int arg4) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void OnPostedCommentListener(String arg0, String arg1, String arg2,
				String arg3, int arg4) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void OnPostedArticleListener(String arg0, String arg1, String arg2,
				String arg3, int arg4, int arg5, int arg6) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void OnJoinedListener(String arg0, String arg1, String arg2,
				String arg3) {
			// TODO Auto-generated method stub
			
		}
	};
	
	/**************************************************************************************/
	
	public void doUnLink() {
		final String appendMessage = "Are you sure to unlink from this app?";
		new AlertDialog.Builder(mActivity).setMessage(appendMessage)
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					GameAPI.requestUnlink(new UnLinkResponseCallback() {
						@Override
						public void onFailure(ErrorResult errorResult) {
						}
	
						@Override
						public void onSessionClosed(ErrorResult errorResult) {
							LogUtils.d("onSessionClosed");
						}
	
						@Override
						public void onNotSignedUp() {
							LogUtils.d("onNotSignedUp");
						}
	
						@Override
						public void onSuccess(Long result) {
							LogUtils.d("onSuccess");
						}
					});
					dialog.dismiss();
				}
			}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			}).show();
	}
	
	private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
        	requestMe();
//        	mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_SUCCESS, "");
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
        	mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_FAILED, "");
        }
    }
	
	protected void requestMe() {
		List<String> propertyKeys = new ArrayList<String>();
		propertyKeys.add("kaccount_email");
		propertyKeys.add("nickname");
		propertyKeys.add("profile_image");
		propertyKeys.add("thumbnail_image");
		UserManagement.requestMe(new MeResponseCallback() {
			
			@Override
			public void onSuccess(UserProfile userProfile) {
				long id = userProfile.getId();
				String nickname = userProfile.getNickname();
				String email = userProfile.getProperty("kaccount_email");
				pgmp2Sdk.kakaoLogin(id+"", nickname, email);
				LogUtils.d("id= "+id+",nickname= "+nickname+",email= "+email);
			}
			
			
			@Override
			public void onSessionClosed(ErrorResult arg0) {
				
			}
			
			@Override
			public void onNotSignedUp() {
				
			}
		}, propertyKeys, false);
    }
}
