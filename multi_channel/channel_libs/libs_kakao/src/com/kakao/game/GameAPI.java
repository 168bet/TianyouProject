package com.kakao.game;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;

import com.kakao.auth.ApiResponseCallback;
import com.kakao.auth.Session;
import com.kakao.auth.SingleNetworkTask;
import com.kakao.auth.network.response.ApiResponse;
import com.kakao.friends.api.FriendsApi;
import com.kakao.friends.response.FriendsResponse;
import com.kakao.friends.response.model.FriendInfo;
import com.kakao.game.request.AddPlusFriendRequest;
import com.kakao.game.request.AgreementRequest;
import com.kakao.game.request.CheckPlusFriendRequest;
import com.kakao.game.request.GameImageUploadRequest;
import com.kakao.game.request.InvitationEventListRequest;
import com.kakao.game.request.InvitationEventRequest;
import com.kakao.game.request.InvitationSenderListRequest;
import com.kakao.game.request.InvitationSenderRequest;
import com.kakao.game.request.InvitationStatesRequest;
import com.kakao.game.request.JoinMemberShipRequest;
import com.kakao.game.request.PostStoryRequest;
import com.kakao.game.request.RecommendedInvitableFriendsRequest;
import com.kakao.game.request.SendRecommendedInviteMessageRequest;
import com.kakao.game.request.SetAgreementRequest;
import com.kakao.game.response.AgreementResponse;
import com.kakao.game.response.ExtendedFriendsResponse;
import com.kakao.game.response.GameImageResponse;
import com.kakao.game.response.InvitationEventListResponse;
import com.kakao.game.response.InvitationEventResponse;
import com.kakao.game.response.InvitationSenderListResponse;
import com.kakao.game.response.InvitationSenderResponse;
import com.kakao.game.response.InvitationStatesResponse;
import com.kakao.game.response.PlusFriendResponse;
import com.kakao.game.response.model.ExtendedFriendInfo;
import com.kakao.guild.GuildsService;
import com.kakao.kakaostory.KakaoStoryService;
import com.kakao.kakaostory.callback.StoryResponseCallback;
import com.kakao.kakaostory.response.model.MyStoryInfo;
import com.kakao.kakaotalk.KakaoTalkService;
import com.kakao.kakaotalk.api.KakaoTalkApi;
import com.kakao.kakaotalk.callback.TalkResponseCallback;
import com.kakao.kakaotalk.response.ChatListResponse;
import com.kakao.kakaotalk.response.KakaoTalkProfile;
import com.kakao.kakaotalk.response.model.ChatInfo;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.network.response.ResponseData;
import com.kakao.network.tasks.KakaoResultTask;
import com.kakao.network.tasks.KakaoTaskQueue;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by house.dr on 15. 9. 3..
 */
public class GameAPI {
    private static final String KEY_AGREEMENT = "key_agreement";
    private static final String KEY_PLUS_FRIEND_ADD = "key_plus_friend_add";
    private static final Handler sHandler = new Handler(Looper.getMainLooper());

    public static void requestSignUp(final ApiResponseCallback<Long> callback, final Map<String, String> properties) {
        UserManagement.requestSignup(callback, properties);
    }

    public static void requestLogout(final LogoutResponseCallback callback) {
        UserManagement.requestLogout(callback);
        Session.getAppCache().remove(KEY_AGREEMENT);
    }

    public static void requestUnlink(final UnLinkResponseCallback callback) {
        UserManagement.requestUnlink(callback);
        Session.getAppCache().remove(KEY_AGREEMENT);
    }

    public static void requestMe(final MeResponseCallback callback) {
        UserManagement.requestMe(callback);
    }

    public static void requestUpdateProfile(final ApiResponseCallback<Long> callback, final Map<String, String> properties) {
        UserManagement.requestUpdateProfile(callback, properties);
    }

    public static void requestRegisteredFriends(final ResponseCallback<FriendsResponse> callback, final RegisteredFriendContext registeredFriendContext) {
        KakaoTaskQueue.getInstance().addTask(new KakaoResultTask<FriendsResponse>(callback) {
            @Override
            public FriendsResponse call() throws Exception {
                return FriendsApi.requestFriends(registeredFriendContext.getFriendContext());
            }
        });
    }

    public static void requestReachInvitableFriends(final ResponseCallback<FriendsResponse> callback, final ReachInvitableFriendContext reachInvitableFriendContext) {
        KakaoTaskQueue.getInstance().addTask(new KakaoResultTask<FriendsResponse>(callback) {
            @Override
            public FriendsResponse call() throws Exception {
                return FriendsApi.requestFriends(reachInvitableFriendContext.getFriendContext());
            }
        });
    }

    public static void requestInvitableFriends(final ResponseCallback<FriendsResponse> callback, final InvitableFriendContext invitableFriendContext) {
        KakaoTaskQueue.getInstance().addTask(new KakaoResultTask<FriendsResponse>(callback) {
            @Override
            public FriendsResponse call() throws Exception {
                return FriendsApi.requestFriends(invitableFriendContext.getFriendContext());
            }
        });
    }

    public static void requestSendGameMessage(final TalkResponseCallback<Boolean> callback,
                                              final FriendInfo friendInfo,
                                              final String templateId,
                                              final Map<String, String> args) {
        KakaoTalkService.requestSendMessage(callback, friendInfo, templateId, args);
    }

    public static void requestSendInviteMessage(final TalkResponseCallback<Boolean> callback,
                                                final FriendInfo friendInfo,
                                                final String templateId,
                                                final Map<String, String> args) {
        KakaoTalkService.requestSendMessage(callback, friendInfo, templateId, args);
    }

    public static void requestSendMultiChatMessage(final TalkResponseCallback<Boolean> callback,
                                                   final ChatInfo chatInfo,
                                                   final String templateId,
                                                   final Map<String, String> args) {
        KakaoTalkService.requestSendMessage(callback, chatInfo, templateId, args);
    }

    public static void requestTalkProfile(final TalkResponseCallback<KakaoTalkProfile> callback) {
        KakaoTalkService.requestProfile(callback);
    }

    public static void requestMultiChatList(final TalkResponseCallback<ChatListResponse> callback, final GameMultichatContext gameMultichatContext) {
        KakaoTaskQueue.getInstance().addTask(new KakaoResultTask<ChatListResponse>(callback) {
            @Override
            public ChatListResponse call() throws Exception {
                return KakaoTalkApi.requestChatRoomList(gameMultichatContext.getChatListContext());
            }
        });
    }

    public static GameImageResponse requestGameImageUpload(List<File> fileList) throws Exception {
        SingleNetworkTask networkTask = new SingleNetworkTask();
        ResponseData result = networkTask.requestApi(new GameImageUploadRequest(fileList));
        return new GameImageResponse(result);
    }

    public static void requestSendImageMessage(final TalkResponseCallback<Boolean> callback,
                                               final FriendInfo friendInfo,
                                               final String templateId,
                                               final Map<String, String> args,
                                               final Bitmap bitmap) {
        File dir = Session.getCurrentSession().getContext().getExternalCacheDir();
        if (dir != null && !dir.exists()) {
            dir = Session.getCurrentSession().getContext().getExternalFilesDir("temp");
        }
        final File file = new File(dir, "temp" + System.currentTimeMillis() + ".png");
        FileOutputStream fOut;
        try {
            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final List<File> fileList = new ArrayList<File>();
        fileList.add(file);

        KakaoTaskQueue.getInstance().addTask(new KakaoResultTask<Boolean>(callback) {
            @Override
            public Boolean call() throws Exception {
                String imageUrl;
                imageUrl = requestGameImageUpload(fileList).getImageUrl();
                file.delete();
                args.put("${image_url}", imageUrl);
                args.put("${imageWidth}", String.valueOf(bitmap.getWidth()));
                args.put("${imageHeight}", String.valueOf(bitmap.getHeight()));
                return KakaoTalkApi.requestSendMessage(friendInfo, templateId, args);
            }
        });
    }

    public static void requestIsStoryUser(final StoryResponseCallback<Boolean> callback) {
        KakaoStoryService.requestIsStoryUser(callback);
    }

    public static void requestPostStory(final ResponseCallback<Boolean> callback, final String templateId, final String content) {
        KakaoTaskQueue.getInstance().addTask(new KakaoResultTask<Boolean>(callback) {

            @Override
            public Boolean call() throws Exception {
                SingleNetworkTask networkTask = new SingleNetworkTask();
                ResponseData result = networkTask.requestApi(new PostStoryRequest(templateId, content));
                new ApiResponse.BlankApiResponse(result);
                return true;
            }
        });
    }

    /**
     * 카카오스토리에 이미지 포스팅 요청.
     * @param callback 포스팅 요청 결과에 대한 callback
     * @param filePath 요청할 이미지 경로.
     * @param content 카카오 스토리에 포스팅할 본문 내용. 2048자 제한
     */
    public static void requestPostPhotoStory(final StoryResponseCallback<MyStoryInfo> callback,
                                             final String filePath,
                                             final String content ) {
        List<File> fileList = new ArrayList<File>();
        final File uploadFile = new File(filePath);
        fileList.add(uploadFile);
        KakaoStoryService.requestPostPhoto(callback, fileList, content);
    }

    public static void requestJoinGuildChat(final Activity activity, final String worldId, final String guildId) {
        GuildsService.requestJoinGuildChat(activity, worldId, guildId);
    }

    public static void requestSendGuildMessage(final ResponseCallback<Boolean> callback, final String worldId, final String guildId, final String templateId, final Map<String, String> args) {
        GuildsService.requestSendGuildMessage(callback, worldId, guildId, templateId, args);
    }

    public static void showMessageBlockDialog(final Activity activity, final ResponseCallback<Boolean> callback) {
        GameMessageBlockDialog dialog = new GameMessageBlockDialog(activity, callback);
        dialog.show();
    }

    public static void requestInvitationEventList(final ResponseCallback<InvitationEventListResponse> callback) {
        KakaoTaskQueue.getInstance().addTask(new KakaoResultTask<InvitationEventListResponse>(callback) {
            @Override
            public InvitationEventListResponse call() throws Exception {
                SingleNetworkTask networkTask = new SingleNetworkTask();
                ResponseData result = networkTask.requestApi(new InvitationEventListRequest());
                return new InvitationEventListResponse(result);
            }
        });
    }

    public static void requestInvitationEvent(final ResponseCallback<InvitationEventResponse> callback, final int id) {
        KakaoTaskQueue.getInstance().addTask(new KakaoResultTask<InvitationEventResponse>(callback) {
            @Override
            public InvitationEventResponse call() throws Exception {
                SingleNetworkTask networkTask = new SingleNetworkTask();
                ResponseData result = networkTask.requestApi(new InvitationEventRequest(id));
                return new InvitationEventResponse(result);
            }
        });
    }

    public static void requestInvitationStates(final ResponseCallback<InvitationStatesResponse> callback, final int id) {
        KakaoTaskQueue.getInstance().addTask(new KakaoResultTask<InvitationStatesResponse>(callback) {
            @Override
            public InvitationStatesResponse call() throws Exception {
                SingleNetworkTask networkTask = new SingleNetworkTask();
                ResponseData result = networkTask.requestApi(new InvitationStatesRequest(id));
                return new InvitationStatesResponse(result);
            }
        });
    }

    public static void requestInvitationSender(final ResponseCallback<InvitationSenderResponse> callback, final int id) {
        KakaoTaskQueue.getInstance().addTask(new KakaoResultTask<InvitationSenderResponse>(callback) {
            @Override
            public InvitationSenderResponse call() throws Exception {
                SingleNetworkTask networkTask = new SingleNetworkTask();
                ResponseData result = networkTask.requestApi(new InvitationSenderRequest(id));
                return new InvitationSenderResponse(result);
            }
        });
    }

    public static void requestInvitationSenderList(final ResponseCallback<InvitationSenderListResponse> callback, final int id) {
        KakaoTaskQueue.getInstance().addTask(new KakaoResultTask<InvitationSenderListResponse>(callback) {
            @Override
            public InvitationSenderListResponse call() throws Exception {
                SingleNetworkTask networkTask = new SingleNetworkTask();
                ResponseData result = networkTask.requestApi(new InvitationSenderListRequest(id));
                return new InvitationSenderListResponse(result);
            }
        });
    }

    public static void requestRecommendedInvitableFriends(final ResponseCallback<ExtendedFriendsResponse> callback, final InvitableFriendContext invitableFriendContext) {
        KakaoTaskQueue.getInstance().addTask(new KakaoResultTask<ExtendedFriendsResponse>(callback) {
            @Override
            public ExtendedFriendsResponse call() throws Exception {
                SingleNetworkTask networkTask = new SingleNetworkTask();
                ResponseData result = networkTask.requestApi(new RecommendedInvitableFriendsRequest(invitableFriendContext));
                ExtendedFriendsResponse response = new ExtendedFriendsResponse(result);
                invitableFriendContext.getFriendContext().setBeforeUrl(response.getBeforeUrl());
                invitableFriendContext.getFriendContext().setAfterUrl(response.getAfterUrl());
                invitableFriendContext.getFriendContext().setId(response.getId());

                return response;
            }
        });
    }

    public static void requestSendRecommendedInviteMessage(final TalkResponseCallback<Boolean> callback,
                                                           final ExtendedFriendInfo friendInfo,
                                                           final String templateId,
                                                           final Map<String, String> args) {
        KakaoTaskQueue.getInstance().addTask(new KakaoResultTask<Boolean>(callback) {

            @Override
            public Boolean call() throws Exception {
                SingleNetworkTask networkTask = new SingleNetworkTask();
                ResponseData result = networkTask.requestApi(new SendRecommendedInviteMessageRequest(friendInfo, templateId, args));
                new ApiResponse.BlankApiResponse(result);
                return true;
            }
        });
    }

    private static void requestMemberShipAgreement(final ResponseCallback<AgreementResponse> callback) {
        KakaoTaskQueue.getInstance().addTask(new KakaoResultTask<AgreementResponse>(callback) {
            @Override
            public AgreementResponse call() throws Exception {
                SingleNetworkTask networkTask = new SingleNetworkTask();
                ResponseData result = networkTask.requestApi(new AgreementRequest());
                return new AgreementResponse(result);
            }
        });
    }

    private static void requestSetAgreement(final ResponseCallback<Boolean> callback, final Map<String, String> params) {
        KakaoTaskQueue.getInstance().addTask(new KakaoResultTask<Boolean>(callback) {
            @Override
            public Boolean call() throws Exception {
                SingleNetworkTask networkTask = new SingleNetworkTask();
                ResponseData result = networkTask.requestApi(new SetAgreementRequest(params));
                new ApiResponse.BlankApiResponse(result);
                return true;
            }
        });
    }

    private static void requestJoinMemberShip(final ResponseCallback<Boolean> callback) {
        KakaoTaskQueue.getInstance().addTask(new KakaoResultTask<Boolean>(callback) {
            @Override
            public Boolean call() throws Exception {
                SingleNetworkTask networkTask = new SingleNetworkTask();
                ResponseData result = networkTask.requestApi(new JoinMemberShipRequest());
                new ApiResponse.BlankApiResponse(result);
                return true;
            }
        });
    }

    private static Map<String, String> splitQuery(String params) {
        Map<String, String> query_pairs = new LinkedHashMap<String, String>();
        String[] pairs = params.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(pair.substring(0, idx), pair.substring(idx + 1));
        }
        return query_pairs;
    }

    public static void requestShowAgreementView(final Activity activity, final ResponseCallback<Map<String, String>> callback) {
        Date appCacheDate = Session.getAppCache().getDate(KEY_AGREEMENT);
        long MILLIS_PER_DAY = 24 * 60 * 60 * 1000L;
//        long MILLIS_PER_DAY = 60 * 1000L;
        if (appCacheDate != null && (appCacheDate.getTime() + MILLIS_PER_DAY > System.currentTimeMillis())) {
            callback.onSuccess(null);
            return;
        }


        ResponseCallback<AgreementResponse> responseCallback = new ResponseCallback<AgreementResponse>() {

            @Override
            public void onFailure(ErrorResult errorResult) {
                Logger.d("AgreementResponse onFailure : " + errorResult.getHttpStatus() + ", " + errorResult.getErrorCode() + ", ", errorResult.getErrorMessage());
                callback.onFailure(errorResult);
                return;
            }

            @Override
            public void onSuccess(final AgreementResponse response) {

                if (!response.getAgreementPopup()) {
                    Session.getAppCache().put(KEY_AGREEMENT, System.currentTimeMillis());
                    callback.onSuccess(null);
                    return;
                }

                sHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ResultReceiver resultReceiver = new ResultReceiver(sHandler) {
                                @Override
                                protected void onReceiveResult(int resultCode, Bundle resultData) {
                                    if (resultCode == AgreementWebViewDialog.RESULT_SUCCESS) {
                                        String params = resultData.getString(AgreementWebViewDialog.KEY_PARAMS);
                                        if (params != null) {
                                            final Map<String, String> paramMap = splitQuery(params);

                                            if (paramMap.containsKey("joinMemberShip")) {
                                                if (paramMap.get("joinMemberShip").equals("y")) {
                                                    requestJoinMemberShip(new ResponseCallback<Boolean>() {
                                                        @Override
                                                        public void onFailure(ErrorResult errorResult) {
                                                            Logger.d("requestJoinMemberShip Failure : " + errorResult.getHttpStatus() + ", " + errorResult.getErrorCode() + ", ", errorResult.getErrorMessage());
                                                        }

                                                        @Override
                                                        public void onSuccess(Boolean result) {
                                                            Logger.d("requestJoinMemberShip Success");
                                                        }
                                                    });
                                                }
                                                paramMap.remove("joinMemberShip");
                                            }

                                            if (paramMap.containsKey("plusFriendIds")) {
                                                String plusFriendIds = "";
                                                try {
                                                    plusFriendIds = URLDecoder.decode(paramMap.get("plusFriendIds"), "utf-8");
                                                    Session.getAppCache().put(KEY_PLUS_FRIEND_ADD, plusFriendIds);
                                                } catch (UnsupportedEncodingException e) {
                                                    e.printStackTrace();
                                                }
                                                paramMap.remove("plusFriendIds");
                                            }

                                            requestSetAgreement(new ResponseCallback<Boolean>() {
                                                @Override
                                                public void onFailure(ErrorResult errorResult) {
                                                    Logger.d("requestSetAgreement Failure : " + errorResult.getHttpStatus() + ", " + errorResult.getErrorCode() + ", ", errorResult.getErrorMessage());
                                                    callback.onFailure(errorResult);
                                                }

                                                @Override
                                                public void onSuccess(Boolean result) {
                                                    Logger.d("requestSetAgreement Success");
                                                    Session.getAppCache().put(KEY_AGREEMENT, System.currentTimeMillis());
                                                    callback.onSuccess(paramMap);

                                                }
                                            }, paramMap);

                                        }
                                    } else if (resultCode == AgreementWebViewDialog.RESULT_ERROR) {
                                        KakaoException kakaoException = (KakaoException) resultData.getSerializable(AgreementWebViewDialog.KEY_EXCEPTION);
                                        callback.onFailure(new ErrorResult(kakaoException));
                                        Logger.d(kakaoException);
                                    }
                                }
                            };

                            final AgreementWebViewDialog dialog = new AgreementWebViewDialog(activity, response.getParams(), resultReceiver);
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dialog.show();
                                }
                            });
                        } catch (Exception e) {
                            Logger.d(e);
                            callback.onFailure(new ErrorResult(e));
                        }
                    }
                });
            }
        };

        Session.getCurrentSession().checkAccessTokenInfo();
        requestMemberShipAgreement(responseCallback);
    }

    public static void showCommunityWebView(final Activity activity, final ResponseCallback<Boolean> callback) {
        ResultReceiver resultReceiver = new ResultReceiver(sHandler) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                if (resultCode == CommunityWebViewActivity.RESULT_SUCCESS) {
                    callback.onSuccess(true);
                } else if (resultCode == CommunityWebViewActivity.RESULT_ERROR) {
                    KakaoException kakaoException = (KakaoException) resultData.getSerializable(CommunityWebViewActivity.KEY_EXCEPTION);
                    callback.onFailure(new ErrorResult(kakaoException));
                    Logger.d(kakaoException);
                }
            }
        };
        Intent intent = CommunityWebViewActivity.newIntent(activity);
        intent.putExtra(CommunityWebViewActivity.KEY_RESULT_RECEIVER, resultReceiver);
        activity.startActivity(intent);
    }

    public static void requestCheckPlusFriend(final ResponseCallback<PlusFriendResponse> callback, final int plusFriendId) {
        KakaoTaskQueue.getInstance().addTask(new KakaoResultTask<PlusFriendResponse>(callback) {
            @Override
            public PlusFriendResponse call() throws Exception {
                SingleNetworkTask networkTask = new SingleNetworkTask();
                final ResponseData result = networkTask.requestApi(new CheckPlusFriendRequest(plusFriendId));
                return new PlusFriendResponse(result);
            }
        });
    }

    public static void requestAddPlusFriend(final ResponseCallback<Boolean> callback, final int plusFriendId) {
        KakaoTaskQueue.getInstance().addTask(new KakaoResultTask<Boolean>(callback) {
            @Override
            public Boolean call() throws Exception {
                SingleNetworkTask networkTask = new SingleNetworkTask();
                ResponseData result = networkTask.requestApi(new AddPlusFriendRequest(plusFriendId));
                new ApiResponse.BlankApiResponse(result);
                return true;
            }
        });
    }
}
