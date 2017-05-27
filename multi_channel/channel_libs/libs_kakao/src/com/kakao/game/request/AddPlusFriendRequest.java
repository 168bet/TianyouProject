package com.kakao.game.request;

import com.kakao.auth.Session;
import com.kakao.auth.network.request.ApiRequest;
import com.kakao.gameutil.helper.GameServerProtocol;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by house.dr on 2017. 2. 1..
 */

public class AddPlusFriendRequest extends ApiRequest {
    private final int plusFriendId;

    public AddPlusFriendRequest(int plusFriendId) {
        this.plusFriendId = plusFriendId;
    }

    @Override
    public String getMethod() {
        return POST;
    }

    @Override
    public String getUrl() {
        return GameServerProtocol.ZINNY3_API_AUTHORITY + GameServerProtocol.API_VERSION_3 + GameServerProtocol.GET_PLUS_FRIEND_PATH + "/add";
    }

    @Override
    public String getRawString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("accessToken", Session.getCurrentSession().getAccessToken());
            jsonObject.put("plusFriendId", plusFriendId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
