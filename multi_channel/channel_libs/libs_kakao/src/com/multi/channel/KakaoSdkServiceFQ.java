package com.multi.channel;

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

public class KakaoSdkServiceFQ extends BaseSdkService {

	private SessionCallback callback;
	
	@Override
	public void doApplicationCreate(final Context context, boolean island) {
		super.doApplicationCreate(context, island);
		KakaoSDK.init(new KakaoAdapter() {
			@Override
			public ISessionConfig getSessionConfig() {
				return new ISessionConfig() {
					@Override
					public AuthType[] getAuthTypes() {
						return new AuthType[] { AuthType.KAKAO_TALK };
					}

					@Override
					public boolean isUsingWebviewTimer() {
						return false;
					}

					@Override
					public ApprovalType getApprovalType() {
						return ApprovalType.INDIVIDUAL;
					}

					@Override
					public boolean isSaveFormData() {
						return true;
					}
				};
			}

			@Override
			public IApplicationConfig getApplicationConfig() {
				return new IApplicationConfig() {
					@Override
					public Activity getTopActivity() {
						return mActivity;
					}

					@Override
					public Context getApplicationContext() {
						return context;
					}
				};
			}
		});
	}
	
	@Override
	public void doActivityInit(Activity activity, TianyouCallback tianyouCallback) {
		super.doActivityInit(activity, tianyouCallback);
		callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        Boolean isClosed = Session.getCurrentSession().checkAndImplicitOpen();
        LogUtils.d("isClosed:" + isClosed);
		mTianyouCallback.onResult(TianyouCallback.CODE_INIT, "");
	}
	
	@Override
	public void doLogin() {
		super.doLogin();
	    Session.getCurrentSession().open(AuthType.KAKAO_TALK, mActivity);
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
