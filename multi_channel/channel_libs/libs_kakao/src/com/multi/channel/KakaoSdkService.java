package com.multi.channel;

import java.util.Dictionary;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakaogame.KGIdpProfile;
import com.kakaogame.KGKakaoProfile;
import com.kakaogame.KGKakaoProfile.KGKakaoFriendsResponse;
import com.kakaogame.KGKakaoTalkMessage;
import com.kakaogame.KGLocalPlayer;
import com.kakaogame.KGPlayer;
import com.kakaogame.KGResult;
import com.kakaogame.KGResultCallback;
import com.kakaogame.KGSession;
import com.tianyou.channel.interfaces.BaseSdkService;
import com.tianyou.channel.interfaces.TianyouCallback;
import com.tianyou.channel.utils.LogUtils;
import com.tianyou.channel.utils.ToastUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class KakaoSdkService extends BaseSdkService {

	private SessionCallback callback;
	
	@Override
	public void doApplicationCreate(final Context context, boolean island) {
		super.doApplicationCreate(context, island);
	}
	
	@Override
	public void doActivityInit(Activity activity, TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		mLoginInfo.setIsOverseas(true);		// 设置海外地址
		KGSession.start(mActivity, mStartCallback);		//	当游戏开始时，调用Start函数来通知Kakao Game SDK游戏已经开始
		
	}
	@Override
	public void doLogin() {
		super.doLogin();
		KGSession.login(mActivity, mLoginCallback);
	}
//	1056441265
	@Override
	public void doLogout() {
		super.doLogout();
		KGSession.logout(mActivity, mLogoutCallback);
	}
	
	private void getCurrentPlayerData(){
		KGLocalPlayer currentPlayer = KGLocalPlayer.getCurrentPlayer();
		String playerId = currentPlayer.getPlayerId();
		LogUtils.d("getCurrentPlayerID palyerID= "+playerId);
	}
	
	private void doLoadFriendPlayers(){
		KGLocalPlayer.loadFriendPlayers(new KGResultCallback<List<KGPlayer>>() {

			@Override
			public void onResult(KGResult<List<KGPlayer>> result) {
				if (result.isSuccess()) {
					List<KGPlayer> friendPlayers = result.getContent();
					for (KGPlayer kgPlayer : friendPlayers) {
						KGIdpProfile idpProfile = kgPlayer.getIdpProfile();
						LogUtils.d("idProfile= "+idpProfile+"\n");
					}
				} else { LogUtils.d("load friendplayers failed");}
			}
		});
	}
	
	/**
	 * 查询kakao talk邀请对象列表
	 */
	private void loadInvitableFriendProfiles(){
		int offset = 0;
		int limit = 10;
		KGKakaoProfile.loadInvitableFriendProfiles(offset, limit, new KGResultCallback<KGKakaoProfile.KGKakaoFriendsResponse>() {

			@Override
			public void onResult(KGResult<KGKakaoFriendsResponse> kgResult) {
				if (kgResult.isSuccess()) {
					KGKakaoFriendsResponse response = kgResult.getContent();
					LogUtils.d("kgkakaoprofileresponse= "+response);
					int totalCount = response.getTotalCount();
					LogUtils.d("totalCount= "+totalCount);
					List<KGKakaoProfile> friendList = response.getFriendList();
					LogUtils.d("friendlist= "+friendList);
				} else {LogUtils.d("查询kakao talk邀请对象列表失败");}
			}
		});
	}
	
	/**
	 * 查找kakao talk推荐邀请对象列表
	 */
	private void loadRecommednedFriendProfiles() {
		int offset = 0;
		int limit = 10;
		KGKakaoProfile.loadRecommendedInvitableFriendProfiles(offset, limit, new KGResultCallback<KGKakaoProfile.KGKakaoFriendsResponse>() {

			@Override
			public void onResult(KGResult<KGKakaoFriendsResponse> kgResult) {
				if (kgResult.isSuccess()) {
					KGKakaoFriendsResponse response = kgResult.getContent();
					LogUtils.d("Recommended response= "+response);
					int totalCount = response.getTotalCount();
					LogUtils.d("Recommended totalCount= "+totalCount);
					List<KGKakaoProfile> friendList = response.getFriendList();
					LogUtils.d("Recommended friendList= "+friendList);
				} else { LogUtils.d("查找kakao talk推荐邀请对象列表失败");}
			}
		});
	}
	
	/**
	 * 发送kakao talk邀请消息
	 */
	private void sendMessage() {
		KGKakaoProfile kakaoProfile = null;
		String templateId = null;
		Map<String, String> args = new LinkedHashMap<String, String>();
		String nickname = ((KGKakaoProfile)KGLocalPlayer.getCurrentPlayer().getIdpProfile()).getNickname();
		args.put("${sender_name}",nickname);
		args.put("${invitation_event_id}", "");
		KGKakaoTalkMessage.sendInviteMessage(kakaoProfile, templateId, args, new KGResultCallback<Boolean>() {
			
			@Override
			public void onResult(KGResult<Boolean> kgResult) {
				if (kgResult.isSuccess()) {
					
				} else {
					
				}
			}
		});
	}
	
	/**
	 * 设置kakao talk游戏消息收据
	 */
	private void showAllowMessageView(){
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
	 * 查询kakao talk 游戏朋友列表
	 */
	private void loadFriendPlayers(){
		KGPlayer.loadFriendPlayers(new KGResultCallback<List<KGPlayer>>() {

			@Override
			public void onResult(KGResult<List<KGPlayer>> kgResult) {
				
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
				mTianyouCallback.onResult(TianyouCallback.CODE_LOGOUT, "用户注销成功");
				LogUtils.d("KGSession.logout success");
			} else { LogUtils.d("KGSession.logout failed");}	// 注销失败
		}
	};
	
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
        GameAPI.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                LogUtils.d(message);
                ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                if (result == ErrorCode.CLIENT_ERROR_CODE) {
                	ToastUtils.show(mActivity, "错误代码：" + ErrorCode.CLIENT_ERROR_CODE);
                } else {
//                    mTianyouCallback.onResult(TianyouCallback.CODE_LOGIN_SUCCESS, "");
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
//                redirectLoginActivity();
            }

            @Override
            public void onSuccess(UserProfile userProfile) {
                LogUtils.d("UserProfile : " + userProfile);
                mLoginInfo.setChannelUserId(userProfile.getId() + "");
                mLoginInfo.setIsOverseas(true);
                checkLogin();
            }

            @Override
            public void onNotSignedUp() {
                GameAPI.requestSignUp(new ApiResponseCallback<Long>() {
                    @Override
                    public void onSessionClosed(ErrorResult errorResult) {
                    	LogUtils.d("onSessionClosed:" + errorResult.toString());
                    }

                    @Override
                    public void onNotSignedUp() {
                    	LogUtils.d("onNotSignedUp");
                    }

                    @Override
                    public void onFailure(ErrorResult errorResult) {
                    	LogUtils.d("onFailure:" + errorResult.getErrorMessage());
                    }

                    @Override
                    public void onSuccess(Long result) {
                        requestMe();
                    }
                }, null);
            }
        });
    }
}
