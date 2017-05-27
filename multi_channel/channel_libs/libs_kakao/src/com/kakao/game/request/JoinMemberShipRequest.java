package com.kakao.game.request;

import com.kakao.auth.Session;
import com.kakao.auth.network.request.ApiRequest;
import com.kakao.gameutil.helper.GameServerProtocol;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by house.dr on 2016. 10. 20..
 */

public class JoinMemberShipRequest extends ApiRequest {
    @Override
    public String getMethod() {
        return POST;
    }

    @Override
    public String getUrl() {
        return GameServerProtocol.ZINNY3_API_AUTHORITY + GameServerProtocol.GET_MEMBERSHIP_CAPRI_PATH + "/join";
    }

    @Override
    public String getRawString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("accessToken", Session.getCurrentSession().getAccessToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
