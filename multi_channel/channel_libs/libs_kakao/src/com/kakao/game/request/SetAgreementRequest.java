package com.kakao.game.request;

import com.kakao.auth.Session;
import com.kakao.auth.network.request.ApiRequest;
import com.kakao.gameutil.helper.GameServerProtocol;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by house.dr on 2016. 10. 20..
 */

public class SetAgreementRequest extends ApiRequest {
    private final Map<String, String> params;

    public SetAgreementRequest(Map<String, String> params) {
        this.params = params;
    }

    @Override
    public String getMethod() {
        return POST;
    }

    @Override
    public String getUrl() {
        return GameServerProtocol.ZINNY3_API_AUTHORITY + GameServerProtocol.GET_MEMBERSHIP_CAPRI_PATH + "/setAgreement";
    }

    @Override
    public String getRawString() {
        JSONObject jsonObject = new JSONObject();
        JSONObject agreement = new JSONObject();
        try {
            jsonObject.put("accessToken", Session.getCurrentSession().getAccessToken());
            for (Map.Entry<String, String> entry : params.entrySet()) {
                agreement.put(entry.getKey(), String.valueOf(entry.getValue()));
            }
            jsonObject.put("agreement", agreement);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}

