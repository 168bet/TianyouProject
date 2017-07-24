package com.multi.channel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import com.google.gson.Gson;
import com.igaworks.IgawCommon;
import com.igaworks.adbrix.IgawAdbrix;
import com.igaworks.commerce.IgawCommerce;
import com.kakao.auth.ISessionCallback;
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
import com.kakaogame.KGGoogleGames;
import com.kakaogame.KGGoogleGamesAchievements;
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
import com.tianyou.channel.bean.RoleInfo;
import com.tianyou.channel.bean.OneStoreParam.ResultBean;
import com.tianyou.channel.bean.OneStoreParam.ResultBean.ProductBean;
import com.tianyou.channel.bean.OrderInfo.ResultBean.OrderinfoBean;
import com.tianyou.channel.bean.PayParam;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.tianyou.channel.utils.HttpUtils;
import com.tianyou.channel.utils.HttpUtils.HttpCallback;
import com.tianyou.channel.utils.AppUtils;
import com.tianyou.channel.utils.LogUtils;
import com.tianyou.channel.utils.ToastUtils;
import com.tianyou.channel.utils.URLHolder;

public class KakaoSdkService extends BaseSdkService {

	private SessionCallback callback;
	private IapPlugin mIapPlugin;		// onstore支付
	
	private Pgmp2Sdk pgmp2Sdk = null;
	
	@Override
	public void doApplicationCreate(final Context context, boolean island) {
		super.doApplicationCreate(context, island);
		IgawCommon.autoSessionTracking((Application)context);
		IgawAdbrix.retention("game_start");
	}
	
	/**
	 * 初始化
	 */
	@Override
	public void doActivityInit(Activity activity, TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		mLoginInfo.setIsOverseas(true);		// 设置海外地址
		mIapPlugin = IapPlugin.getPlugin(mActivity, "development");		// 初始化onestore支付插件
		pgmp2Sdk = Pgmp2Sdk.getInstance();		// 初始化pgp
		IgawAdbrix.retention("game_start");		// iga works统计代码
		initNavercafeListener();
		int pgmp2SdkInitGameResultCode = pgmp2Sdk.initGame("", "", mActivity, mPgmp2EventListener,mPgmp2NaverCafeSDKCallBackListener, true);
		if (pgmp2SdkInitGameResultCode == 1) {
			
		}
		KGSession.start(mActivity, mStartCallback);		//	当游戏开始时，调用Start函数来通知Kakao Game SDK游戏已经开始
	}
	
	private void initNavercafeListener(){
		Pgmp2NaverCafeSDKCallBackListener pgmp2NaverCafeSDKCallBackListener = new Pgmp2NaverCafeSDKCallBackListener() {
			
			@Override
			public void OnVotedListener(String arg0, String arg1, String arg2,String arg3, int arg4) {
				LogUtils.d("OnVotedListener");
			}
			
			@Override
			public void OnPostedCommentListener(String guid, String idsort, String is_guest, String email, int articleId) {
				LogUtils.d("OnPostedCommentListener");
				String url = "http://channel.tianyouxi.com/index.php?c=NaverCaff&a=OnPostedCommentListener";
				Map<String,String> map = new HashMap<String, String>();
				map.put("pid", mRoleInfo.getRoleId());
				map.put("is_guest", is_guest);
				map.put("appid", mChannelInfo.getAppId());
				map.put("email", email);
				map.put("userid", mLoginInfo.getTianyouUserId());
				map.put("guid", guid);
				map.put("articleId", articleId + "");
				map.put("sign", AppUtils.MD5(mRoleInfo.getRoleId() + mChannelInfo.getAppId() + mLoginInfo.getTianyouUserId() + guid));
				HttpUtils.post(mActivity, url, map, new HttpCallback() {
					@Override
					public void onSuccess(String data) { }
					
					@Override
					public void onFailed(String code) { }
				});
			}
			
			@Override
			public void OnPostedArticleListener(String guid, String idsort, String is_guest, String email, int menuId, int imageCount, int videoCount) {
				LogUtils.d("OnPostedArticleListener");
				String url = "http://channel.tianyouxi.com/index.php?c=NaverCaff&a=OnPostedArticleListener";
				Map<String,String> map = new HashMap<String, String>();
				map.put("pid", mRoleInfo.getRoleId());
				map.put("is_guest", is_guest);
				map.put("appid", mChannelInfo.getAppId());
				map.put("email", email);
				map.put("userid", mLoginInfo.getTianyouUserId());
				map.put("guid", guid);
				map.put("menuId", menuId + "");
				map.put("imageCount", imageCount + "");
				map.put("videoCount", videoCount + "");
				map.put("sign", AppUtils.MD5(mRoleInfo.getRoleId() + mChannelInfo.getAppId() + mLoginInfo.getTianyouUserId() + guid));
				HttpUtils.post(mActivity, url, map, new HttpCallback() {
					@Override
					public void onSuccess(String data) { }
					
					@Override
					public void onFailed(String code) { }
				});
			}
			
			@Override
			public void OnJoinedListener(String guid, String idsort, String is_guest, String email) {
				LogUtils.d("OnJoinedListener");
				String url = "http://channel.tianyouxi.com/index.php?c=NaverCaff&a=OnJoinedListener";
				Map<String,String> map = new HashMap<String, String>();
				map.put("pid", mRoleInfo.getRoleId());
				map.put("is_guest", is_guest);
				map.put("appid", mChannelInfo.getAppId());
				map.put("email", email);
				map.put("userid", mLoginInfo.getTianyouUserId());
				map.put("guid", guid);
				map.put("sign", AppUtils.MD5(mRoleInfo.getRoleId() + mChannelInfo.getAppId() + mLoginInfo.getTianyouUserId() + guid));
				HttpUtils.post(mActivity, url, map, new HttpCallback() {
					@Override
					public void onSuccess(String data) { }
					
					@Override
					public void onFailed(String code) { }
				});
			}
		};
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
	 * 谷歌登录
	 */
	public void doGooleLogin(){
		if (!KGGoogleGames.isLoggedIn()) {
			KGGoogleGames.login(mActivity, new KGResultCallback<Void>() {

				@Override
				public void onResult(KGResult<Void> kgResult) {
					LogUtils.d("google login kgResult.isSuccess= "+kgResult.isSuccess());
					String message = kgResult.getMessage();
					LogUtils.d("google login kgResult message= "+message);
				}
			});
		}
	}
	
	/**
	 * 谷歌注销
	 */
	public void doGoogleLogout(){
		KGGoogleGames.logout(new KGResultCallback<Void>() {

			@Override
			public void onResult(KGResult<Void> kgResult) {
				LogUtils.d("google logout kgResult.isSuccess= "+kgResult.isSuccess());
				LogUtils.d("google logout kgResult message= "+kgResult.getMessage());
			}
		});
	}
	
	/**
	 * 注销
	 */
	@Override
	public void doLogout() {
		super.doLogout();
		KGSession.logout(mActivity, mLogoutCallback);
	}
	
	/**
	 * 更新角色信息
	 */
	@Override
	public void doUpdateRoleInfo(RoleInfo roleInfo) {
		super.doUpdateRoleInfo(roleInfo);
		
		int level = Integer.parseInt(mRoleInfo.getRoleLevel());
		if (level <= 65) {
			if (level == 2 || level/5 == 0) {IgawAdbrix.firstTimeExperience("level_"+level);}
		}
		
		int vip = Integer.parseInt(mRoleInfo.getVipLevel());
		if (vip == 1 || vip == 2) {IgawAdbrix.firstTimeExperience("vip_"+vip);}
		
	}
	
	/**
	 * 完成成就
	 */
	@Override
	public void doGoogleAchieve(String achieve) {
		super.doGoogleAchieve(achieve);
		KGGoogleGamesAchievements.unlock(achieve);
	}
	
	/**
	 * 显示成就屏幕
	 */
	public void doGoogleAchieveActivity() {
		super.doGoogleAchieveActivity();
		KGGoogleGamesAchievements.showAchievementView(mActivity);
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
	
	@Override
	public void doChannelPay(final PayParam payInfo, final OrderinfoBean orderInfo) {
		super.doChannelPay(payInfo, orderInfo);
		
		mIapPlugin.sendPaymentRequest("OA00715316", "0910077154","165금화",//orderInfo.getProductId(), orderInfo.getProductName(), 
				orderInfo.getOrderID(), "", new RequestCallback() {
					
					@Override
					public void onResponse(IapResponse iapResponse) {
						final String response = iapResponse.getContentToString();
						LogUtils.d("response:" + response);
						OneStoreParam oneStoreParam = new Gson().fromJson(response, OneStoreParam.class);
						final ResultBean result = oneStoreParam.getResult();
						if (result.getCode().equals("0000")) {
							ProductBean product = result.getProduct().get(0);
							Map<String, String> param = new HashMap<String, String>();
							param.put("api_version", oneStoreParam.getApi_version());
							param.put("identifier", oneStoreParam.getIdentifier());
							param.put("method", oneStoreParam.getMethod());
							param.put("code", result.getCode());
							param.put("message", result.getMessage());
							param.put("count", result.getCount() + "");
							param.put("txid", result.getTxid());
							param.put("receipt", result.getReceipt());
							param.put("name", product.getName());
							param.put("kind", product.getKind());
							param.put("id", product.getId());
							param.put("price", product.getPrice() + "");
							String url = (mLoginInfo.getIsOverseas() ? URLHolder.URL_OVERSEAS : URLHolder.URL_BASE) + URLHolder.URL_ONE_STORE;
							HttpUtils.post(mActivity, url, param, new HttpCallback() {
								@Override
								public void onSuccess(String data) {
									LogUtils.d("CODE_PAY_SUCCESS");
									mTianyouCallback.onResult(TianyouCallback.CODE_PAY_SUCCESS, "");
									LogUtils.d("调用purchase方法" + orderInfo.getProductId());
									LogUtils.d("调用purchase方法" + orderInfo.getMoNey());
									LogUtils.d("调用purchase方法result.getTxid()" + result.getTxid());
									pgmp2Sdk.purchase(orderInfo.getProductId(), orderInfo.getMoNey(), 2, result.getTxid(), response, 1);
									LogUtils.d("purchase:" + orderInfo.getOrderID());
									LogUtils.d("purchase:" + orderInfo.getProductId());
									LogUtils.d("purchase:" + orderInfo.getProductName());
									LogUtils.d("purchase:" + Double.parseDouble(orderInfo.getMoNey()));
									LogUtils.d("purchase:" + Integer.parseInt(payInfo.getAmount()));
									
									IgawAdbrix.purchase(mActivity, orderInfo.getOrderID(), orderInfo.getProductId(), 
											orderInfo.getProductName(), Double.parseDouble(orderInfo.getMoNey()), 
											Integer.parseInt(payInfo.getAmount()), IgawCommerce.Currency.KR_KRW, "");
								}
								
								@Override
								public void onFailed(String code) {
									mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, code);
								}
							});
						} else {
							mTianyouCallback.onResult(TianyouCallback.CODE_PAY_FAILED, result.getMessage());
						}
					}
					
					@Override
					public void onError(String requestid, String errcode, String errmsg) {
						LogUtils.d("requestid:" + requestid);
						LogUtils.d("errcode:" + errcode);
						LogUtils.d("errmsg:" + errmsg);
					}
				});
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
	
	@Override
	public void doActivityResult(int requestCode, int resultCode, Intent data) {
		if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
		super.doActivityResult(requestCode, resultCode, data);
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
					requestMe();
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
				IgawAdbrix.firstTimeExperience("login");
				IgawAdbrix.retention("login");
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
			
		}
		
		@Override
		public void OnPostedCommentListener(String arg0, String arg1, String arg2,
				String arg3, int arg4) {
			
		}
		
		@Override
		public void OnPostedArticleListener(String arg0, String arg1, String arg2,
				String arg3, int arg4, int arg5, int arg6) {
			
		}
		
		@Override
		public void OnJoinedListener(String arg0, String arg1, String arg2,
				String arg3) {
			
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
		LogUtils.d("requestme---------");
		ToastUtils.show(mActivity, "requestMe start");
		List<String> propertyKeys = new ArrayList<String>();
		propertyKeys.add("kaccount_email");
		propertyKeys.add("nickname");
		propertyKeys.add("profile_image");
		propertyKeys.add("thumbnail_image");
		UserManagement.requestMe(new MeResponseCallback() {
			
			@Override
			public void onSuccess(UserProfile userProfile) {
				try {
					LogUtils.d("requestMe onSuccess");
					String email = userProfile.getProperty("kaccount_email");
					LogUtils.d("email= "+email);
					long id = userProfile.getId();
					LogUtils.d("id= "+id);
					String nickname = userProfile.getNickname();
					LogUtils.d("nickname= "+nickname);
					LogUtils.d("id= "+id+",nickname= "+nickname+",email= "+email);
					ToastUtils.show(mActivity, "id= "+id+",nickname= "+nickname+",email= "+email);
					pgmp2Sdk.kakaoLogin(id+"", nickname, email);
				} catch (Exception e) {
					ToastUtils.show(mActivity, "exception= "+e.getMessage());
					LogUtils.d("e= "+e.getMessage());
				}
			}
			
			@Override
			public void onSessionClosed(ErrorResult errorResult) {
				LogUtils.d("request onSessionClosed");
				ToastUtils.show(mActivity, "errorResult= "+errorResult);
			}
			
			@Override
			public void onNotSignedUp() {
				ToastUtils.show(mActivity, "not sign up");
				LogUtils.d("request onNotSignedUp");
			}
		});
    }
}
